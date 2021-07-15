package com.galid.hemuser.integration

import com.galid.hemuser.service.TokenService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import kotlin.test.Test

@SpringBootTest
@TestPropertySource("/application.yml")
class TokenIntegrationTest {
    @Autowired
    lateinit var tokenService: TokenService

}