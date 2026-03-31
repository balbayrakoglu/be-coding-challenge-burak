package de.dk.api.codeChallenge.notification.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public record UserRegisterRequestDto(
        @NotNull(message = "id must not be null")
        UUID id,

        @NotNull(message = "notifications must not be null")
        @NotEmpty(message = "notifications must not be empty")
        Set<@NotNull(message = "notification type must not be null") String> notifications
) {
    public UserRegisterRequestDto(UUID id, Set<String> notifications) {
        this.id = id;
        this.notifications = notifications != null ? notifications : new HashSet<>();
    }
}