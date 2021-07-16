package com.galid.hemuser.service

import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Service
import java.security.KeyPair
import java.util.*

@Service
class TokenService: ApplicationContextAware {
    lateinit var keyPair: KeyPair
    lateinit var jwtParser: JwtParser

    fun createRefreshToken(
        userId: Long
    ): String {
        return Jwts.builder()
            .signWith(keyPair.private, ENCRYPTION_ALGORITHM)
            .setIssuer(ISSUER)
            .addClaims(mapOf("userId" to userId))
            .setExpiration(getExpiration(AuthTokenType.REFRESH))
            .compact()
    }

    fun renewAuthToken(
        refreshToken: String,
    ): String {
        val parsedToken = verifyToken(refreshToken)

        return Jwts.builder()
            .signWith(keyPair.private, ENCRYPTION_ALGORITHM)
            .setIssuer(ISSUER)
            .addClaims(mapOf("userId" to parsedToken.body.get("userId")))
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
            ?:DEFAULT_AUTH_TOKEN_EXPIRATION_TIME

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

        val DEFAULT_AUTH_TOKEN_EXPIRATION_TIME = 1 * DAY
        val DEFAULT_REFRESH_TOKEN_EXPIRATION_TIME = 30 * DAY
        var AUTH_TOKEN_EXPIRATION_TIME = 0L
        var REFRESH_TOKEN_EXPIRATION_TIME = 0L
    }

}
