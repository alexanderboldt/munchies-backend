package com.alex.munchies.controller

import com.alex.munchies.Fixtures
import com.alex.munchies.service.RabbitMqProducer
import com.alex.munchies.service.UserService
import io.restassured.RestAssured
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.bean.override.mockito.MockitoBean

open class BaseResourceTest {

    @MockitoBean
    private lateinit var userService: UserService

    @Suppress("unused")
    @MockitoBean
    private lateinit var rabbitMqProducer: RabbitMqProducer

    @LocalServerPort
    private var port: Int = 0

    @BeforeEach
    fun beforeEach() {
        RestAssured.port = port

        Mockito.`when`(userService.userId).thenReturn(Fixtures.User.USER_ID)
    }
}