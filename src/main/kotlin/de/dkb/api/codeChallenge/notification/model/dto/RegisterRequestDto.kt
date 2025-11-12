package de.dkb.api.codeChallenge.notification.model.dto


import java.util.UUID

data class RegisterRequestDto(
    val userId: UUID,
    val notificationTypeNames: Set<String>
)