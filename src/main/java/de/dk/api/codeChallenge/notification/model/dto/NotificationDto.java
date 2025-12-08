package de.dk.api.codeChallenge.notification.model.dto;

import java.util.UUID;

public record NotificationDto(
        UUID userId,
        String notificationType,
        String message) {
}
