package de.dkb.api.codeChallenge.notification.model.dto


import java.util.UUID
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class RegisterRequestDto(
    @field:NotNull val userId: UUID,
    @field:NotEmpty val notificationTypeNames: Set<String>
)