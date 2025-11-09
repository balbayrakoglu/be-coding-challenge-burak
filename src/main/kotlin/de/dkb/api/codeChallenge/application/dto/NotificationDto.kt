package de.dkb.api.codeChallenge.application.dto

import de.dkb.api.codeChallenge.domain.model.NotificationType
import java.util.UUID

data class NotificationDto(
    val userId: UUID,
    val notificationType: NotificationType,
    val message: String,
)