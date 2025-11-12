package de.dkb.api.codeChallenge.notification.model.dto


import java.util.UUID
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class NotificationDto(
    @field:NotNull val userId: UUID,
    @field:NotBlank val notificationType: String,
    @field:NotBlank val message: String
)