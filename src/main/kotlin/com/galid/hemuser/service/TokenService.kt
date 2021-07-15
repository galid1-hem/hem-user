package com.galid.hemuser.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import com.auth0.jwt.interfaces.JWTVerifier
import com.galid.hemuser.domain.user.UserRepository
import com.galid.hemuser.utils.PemUtils
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Service
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.util.*

@Service
class TokenService: ApplicationContextAware {
    lateinit var algorithm: Algorithm
    lateinit var tokenVerifier: JWTVerifier

    fun createRefreshToken(
        userId: Long
    ): String {
        return JWT.create()
            .withIssuer(ISSUER)
            .withExpiresAt(getExpiration(AuthTokenType.REFRESH))
            .withClaim("userId", userId)
            .sign(algorithm)
    }

    fun renewAuthToken(
        refreshToken: String,
    ): String {
        val decodedToken = verifyToken(refreshToken)
        val userId = decodedToken.claims["userId"].toString().replace("\"", "")

        return JWT.create()
            .withIssuer(ISSUER)
            .withExpiresAt(getExpiration(AuthTokenType.REFRESH))
            .withClaim("userId", userId)
            .sign(algorithm)
    }

    internal fun verifyToken(token: String): DecodedJWT {
        return tokenVerifier.verify(token)
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
        val publicKeyFilePath = ctx.getResource("classpath:/public-key.pem").file.path
        val privateKeyFilePath = ctx.getResource("classpath:/private-key.pem").file.path
        val publicKey = PemUtils.readPublicKeyFromFile(publicKeyFilePath, ENCRYPTION_ALGORITHM) as RSAPublicKey
        val privateKey = PemUtils.readPrivateKeyFromFile(privateKeyFilePath, ENCRYPTION_ALGORITHM) as RSAPrivateKey
        algorithm = Algorithm.RSA256(publicKey, privateKey)
        tokenVerifier = JWT.require(algorithm)
            .withIssuer(ISSUER)
            .build()

        setExpirationTime(ctx)
    }

    internal fun setExpirationTime(ctx: ApplicationContext) {
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
        val ENCRYPTION_ALGORITHM = "RSA"
        val ISSUER = "HEM_USER"
        val DAY = 24 * 60 * 60 * 1000L

        val DEFAULT_AUTH_TOKEN_EXPIRATION_TIME = 1 * DAY
        val DEFAULT_REFRESH_TOKEN_EXPIRATION_TIME = 30 * DAY
        var AUTH_TOKEN_EXPIRATION_TIME = 0L
        var REFRESH_TOKEN_EXPIRATION_TIME = 0L
    }

}
