package de.dkb.api.codeChallenge.notification.model.dto

import java.util.UUID

data class UserResponseDto(
    val userId: UUID,
    val notificationTypeNames: Set<String>
)