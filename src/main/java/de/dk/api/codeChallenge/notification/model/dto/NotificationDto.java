package de.dk.api.codeChallenge.notification.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record NotificationDto(
        @NotNull(message = "userId must not be null")
        UUID userId,

        @NotBlank(message = "notificationType must not be blank")
        String notificationType,

        @NotBlank(message = "message must not be blank")
        String message
) {
}