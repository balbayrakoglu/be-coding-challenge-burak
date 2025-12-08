package de.dk.api.codeChallenge.notification.model.dto;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public record UserRegisterRequestDto(
        UUID id,
        Set<String> notifications) {
    public UserRegisterRequestDto(UUID id, Set<String> notifications) {
        this.id = id;
        this.notifications = notifications != null ? notifications : new HashSet<>();
    }
}
