package de.dkb.api.codeChallenge.integration

import de.dkb.api.codeChallenge.notification.model.dto.UserRegisterRequestDto
import de.dkb.api.codeChallenge.notification.repository.UserRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import java.util.*

class NotificationControllerIT : AbstractIntegrationTestBase() {

    @BeforeEach
    fun cleanDatabase(@Autowired userRepository: UserRepository) {
        userRepository.deleteAll()
    }

    @Test
    fun registerUser_shouldReturnOk() {
        val user = UserRegisterRequestDto(UUID.randomUUID(), mutableSetOf("type1"))

        val response = sendPostRequest("/register", user, UserRegisterRequestDto::class.java)

        Assertions.assertEquals(HttpStatus.OK, response.statusCode)
    }
}
