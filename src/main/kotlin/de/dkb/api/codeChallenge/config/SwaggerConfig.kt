package de.dkb.api.codeChallenge.config

import org.springframework.context.annotation.Configuration
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info

@Configuration
@OpenAPIDefinition(
    info = Info(
        title = "Code Challenge API",
        version = "1.0.0",
        description = "Notification Service for user registration and notifications"
    )
)
class SwaggerConfig