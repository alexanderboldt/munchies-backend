package com.alex.munchies.controller

import com.alex.munchies.Fixtures
import com.alex.munchies.configuration.SpringProfile
import com.alex.munchies.service.UserService
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.bean.override.mockito.MockitoBean

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(SpringProfile.TESTS)
class BaseResourceTest {

    @MockitoBean
    private lateinit var userService: UserService

    @LocalServerPort
    private var port: Int = 0

    @BeforeEach
    fun beforeEach() {
        RestAssured.port = port
        RestAssured.requestSpecification = RestAssured.given().contentType(ContentType.JSON)

        Mockito.`when`(userService.userId).thenReturn(Fixtures.User.USER_ID)
    }
}
