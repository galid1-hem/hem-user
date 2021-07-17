package com.galid.hemuser.integration

import com.galid.hemuser.infra.TokenServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource

@SpringBootTest
@TestPropertySource("/application.yml")
class TokenIntegrationTest {
    @Autowired
    lateinit var tokenServiceImpl: TokenServiceImpl

}