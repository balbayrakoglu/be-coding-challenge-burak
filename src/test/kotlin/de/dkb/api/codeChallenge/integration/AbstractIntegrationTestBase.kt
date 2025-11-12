package de.dkb.api.codeChallenge.integration

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.util.function.Supplier

@Testcontainers
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Profile("test")
abstract class AbstractIntegrationTestBase : IntegrationTestContainer() {

    companion object {
        @Container
        @JvmStatic
        private val postgres = PostgreSQLContainer<Nothing>("postgres:15").apply {
            withDatabaseName("codechallenge_db")
            withUsername("postgres")
            withPassword("postgres")
            withReuse(true) // reuse across test runs
        }

        private val jdbcUrl: Supplier<Any> = Supplier {
            postgres.jdbcUrl ?: "jdbc:postgresql://localhost:5432/codechallenge_db"
        }
        private val username: Supplier<Any> = Supplier { postgres.username ?: "postgres" }
        private val password: Supplier<Any> = Supplier { postgres.password ?: "postgres" }

        @JvmStatic
        @DynamicPropertySource
        fun registerDatabaseProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", jdbcUrl)
            registry.add("spring.datasource.username", username)
            registry.add("spring.datasource.password", password)
            registry.add("spring.datasource.driver-class-name") { "org.postgresql.Driver" }
            registry.add("spring.jpa.hibernate.ddl-auto") { "validate" }
            registry.add("spring.liquibase.enabled") { true }
        }
    }

    @Autowired
    protected lateinit var mockMvc: MockMvc

    @Autowired
    protected lateinit var restTemplate: TestRestTemplate

    @Autowired
    protected lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun printContainerInfo() {
        println("🧩 Test DB URL: ${postgres.jdbcUrl}")
    }

    protected fun <T> sendGetRequest(endpoint: String, responseType: Class<T>): ResponseEntity<T> {
        val headers = buildHttpHeaders()
        val entity = HttpEntity<String>(headers)
        return restTemplate.exchange(endpoint, HttpMethod.GET, entity, responseType)
    }

    protected fun <T> sendPostRequest(endpoint: String, payload: Any, responseType: Class<T>): ResponseEntity<T> {
        val headers = buildHttpHeaders()
        val json = objectMapper.writeValueAsString(payload)
        val entity = HttpEntity(json, headers)
        return restTemplate.exchange(endpoint, HttpMethod.POST, entity, responseType)
    }

    private fun buildHttpHeaders(): HttpHeaders =
        HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
            accept = listOf(MediaType.APPLICATION_JSON)
        }
}