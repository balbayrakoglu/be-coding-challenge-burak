package de.dk.api.codeChallenge.notification.model.dto;

import java.util.UUID;

public record NotificationResultDto(
        UUID userId,
        String notificationType,
        String status,
        String reason
) {
    public static NotificationResultDto sent(UUID userId, String notificationType) {
        return new NotificationResultDto(userId, notificationType, "SENT", null);
    }

    public static NotificationResultDto skipped(UUID userId, String notificationType, String reason) {
        return new NotificationResultDto(userId, notificationType, "SKIPPED", reason);
    }
}