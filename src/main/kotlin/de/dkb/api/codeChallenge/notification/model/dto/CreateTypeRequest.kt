package de.dkb.api.codeChallenge.notification.model.dto

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
data class CreateTypeRequest(

    @field:NotNull
    val name: String,
    @field:Pattern(regexp = "A|B")
    val category: String
)