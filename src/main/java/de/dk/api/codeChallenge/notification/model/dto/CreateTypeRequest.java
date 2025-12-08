package de.dk.api.codeChallenge.notification.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreateTypeRequest(
        @NotNull String name,
        @Pattern(regexp = "A|B") String category) {
}
