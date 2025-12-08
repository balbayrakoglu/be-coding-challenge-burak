package de.dk.api.codeChallenge.notification.model.dto;

import java.util.Set;
import java.util.UUID;

public record UserResponseDto(
        UUID userId,
        Set<String> notificationTypeNames) {
}
