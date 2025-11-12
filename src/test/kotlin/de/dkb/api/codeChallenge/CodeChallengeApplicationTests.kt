package de.dkb.api.codeChallenge

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CodeChallengeApplicationTests {

    companion object {

        @Container
        @JvmStatic
        private val postgres = PostgreSQLContainer<Nothing>("postgres:15").apply {
            withDatabaseName("codechallenge_db")
            withUsername("postgres")
            withPassword("postgres")
            withReuse(true)
        }

        @JvmStatic
        @DynamicPropertySource
        fun registerPostgresProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", postgres::getJdbcUrl)
            registry.add("spring.datasource.username", postgres::getUsername)
            registry.add("spring.datasource.password", postgres::getPassword)
            registry.add("spring.datasource.driver-class-name") { "org.postgresql.Driver" }
            registry.add("spring.jpa.hibernate.ddl-auto") { "validate" }
            registry.add("spring.liquibase.enabled") { true }
        }
    }

    @Test
    fun contextLoads() {
        println("✅ Application context loaded successfully with PostgreSQL Testcontainer at: ${postgres.jdbcUrl}")
    }
}
