package de.dkb.api.codeChallenge.integration

import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.utility.DockerImageName

open class IntegrationTestContainer {
    companion object {
        val postgresContainer: PostgreSQLContainer<*>? = if (System.getProperty("jenkins") == null) {
            val postgresImage = DockerImageName.parse("postgres:15")
                .asCompatibleSubstituteFor("postgres")

            PostgreSQLContainer(postgresImage)
                .withDatabaseName("codechallenge_db")
                .withUsername("postgres")
                .withPassword("postgres")
                .waitingFor(Wait.forLogMessage(".*database system is ready to accept connections.*\\n", 1))
                .apply { start() }
        } else null
    }
}