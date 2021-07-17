package com.galid.hemuser.infra

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Paths
import java.security.KeyPair
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class JwtsTest {

    val keyPair: KeyPair = Keys.keyPairFor(SignatureAlgorithm.RS256)

    @Test
    fun `private key 로 읽기`() {
        // when
        val userIdClaim = 1L
        val jws = Jwts.builder()
            .signWith(keyPair.private, SignatureAlgorithm.RS256)
            .addClaims(mapOf("userId" to userIdClaim))
            .compact()

        // then
        val parsedToken = Jwts.parserBuilder()
            .setSigningKey(keyPair.private)
            .build()
            .parseClaimsJws(jws)

        assertEquals(userIdClaim.toInt(), parsedToken.body.get("userId"))
    }

    @Test
    fun `public key로 읽기`() {
        // given
        val userIdClaim = 1L
        val jws = Jwts.builder()
            .signWith(keyPair.private, SignatureAlgorithm.RS256)
            .addClaims(mapOf("userId" to userIdClaim))
            .compact()

        // when
        val parsedToken = Jwts.parserBuilder()
            .setSigningKey(keyPair.public)
            .build()
            .parseClaimsJws(jws)

        // then
        assertEquals(userIdClaim.toInt(), parsedToken.body.get("userId"))
    }

    @Test
    fun `유효기간이 지난 토큰 Exception`() {
        // given
        val jws = Jwts.builder()
            .signWith(keyPair.private, SignatureAlgorithm.RS256)
            .setExpiration(Date(Date().time - 1000))
            .compact()

        // when, then
        assertFails {
            Jwts.parserBuilder()
                .setSigningKey(keyPair.private)
                .build()
                .parseClaimsJws(jws)
        }
    }

    @Test
    fun `key 파일로 export하기`() {
        val path = "${System.getProperty("user.home")}/develop/private.pem"
        val file = File(path).createNewFile()
        val outputStream = FileOutputStream(path)

        val privateKey = keyPair.private.toString()
        outputStream.write(privateKey.toByteArray())

//        // given
//
//        // when
//        val fo = FileOutputStream("private.pem")
//        fo.write(privateKey.toByteArray())


        // then
    }
}