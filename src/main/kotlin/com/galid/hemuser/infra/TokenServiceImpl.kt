package com.galid.hemuser.infra

import com.galid.hemuser.domain.user.UserRepository
import com.galid.hemuser.service.TokenService
import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Service
import java.security.KeyPair
import java.util.*

@Service
class TokenServiceImpl(
    private val userRepository: UserRepository
): TokenService, ApplicationContextAware {
    lateinit var keyPair: KeyPair
    lateinit var jwtParser: JwtParser

    override fun createRefreshToken(
        userId: Long
    ): String {
        return Jwts.builder()
            .signWith(keyPair.private, ENCRYPTION_ALGORITHM)
            .setIssuer(ISSUER)
            .addClaims(mapOf(USER_ID_KEY to userId))
            .setExpiration(getExpiration(AuthTokenType.REFRESH))
            .compact()
    }

    override fun renewAuthToken(
        refreshToken: String,
    ): String {
        val parsedToken = verifyToken(refreshToken)
        val userId = parsedToken.body.get(USER_ID_KEY)
            .toString()
            .toLong()

        val foundUser = userRepository.findById(userId)
            .orElseThrow{ RuntimeException("존재하지 않는 사용자입니다.") }

        return Jwts.builder()
            .signWith(keyPair.private, ENCRYPTION_ALGORITHM)
            .setIssuer(ISSUER)
            .addClaims(mapOf(USER_ID_KEY to userId))
            .addClaims(mapOf(USER_EMAIL_KEY to foundUser.userInformation.email))
            .addClaims(mapOf(USER_PROFILE_IMAEGE_KEY to foundUser.userInformation.profileImageUrl))
            .addClaims(mapOf(USER_NICKNAME_KEY to foundUser.userInformation.nickName))
            .compact()
    }

    internal fun verifyToken(token: String): Jws<Claims> {
        return jwtParser.parseClaimsJws(token)
    }

    internal fun getExpiration(tokenType: AuthTokenType): Date {
        val today = Date()
        val expirationTime: Long

        when(tokenType) {
            AuthTokenType.AUTH -> expirationTime = REFRESH_TOKEN_EXPIRATION_TIME
            AuthTokenType.REFRESH -> expirationTime = AUTH_TOKEN_EXPIRATION_TIME
        }

        return Date(today.time + expirationTime)
    }

    override fun setApplicationContext(ctx: ApplicationContext) {
        //TODO 여기서 키쌍이 생성되므로 이것을 다른 곳에서 생성해서 나누어야 함!
        keyPair = Keys.keyPairFor(SignatureAlgorithm.RS256)
        jwtParser = Jwts.parserBuilder()
            .setSigningKey(keyPair.private)
            .build()
        setExpirationTimeVariable(ctx)
    }

    internal fun setExpirationTimeVariable(ctx: ApplicationContext) {
        val propertyPrefix = "hem.token.expiration"
        AUTH_TOKEN_EXPIRATION_TIME = ctx.environment.getProperty("$propertyPrefix.auth")
            ?.toLong()?.let { it * DAY }
            ?: DEFAULT_AUTH_TOKEN_EXPIRATION_TIME

        REFRESH_TOKEN_EXPIRATION_TIME = ctx.environment.getProperty("$propertyPrefix.refresh")
            ?.toLong()?.let { it * DAY }
            ?: DEFAULT_REFRESH_TOKEN_EXPIRATION_TIME
    }

    enum class AuthTokenType {
        REFRESH, AUTH
    }

    companion object {
        val ENCRYPTION_ALGORITHM = SignatureAlgorithm.RS256
        val ISSUER = "HEM_USER"
        val DAY = 24 * 60 * 60 * 1000L

        // claim keys
        val USER_ID_KEY = "userId"
        val USER_NICKNAME_KEY = "nickName"
        val USER_PROFILE_IMAEGE_KEY = "profileImage"
        val USER_EMAIL_KEY = "email"

        val DEFAULT_AUTH_TOKEN_EXPIRATION_TIME = 1 * DAY
        val DEFAULT_REFRESH_TOKEN_EXPIRATION_TIME = 30 * DAY
        var AUTH_TOKEN_EXPIRATION_TIME = 0L
        var REFRESH_TOKEN_EXPIRATION_TIME = 0L
    }

}
