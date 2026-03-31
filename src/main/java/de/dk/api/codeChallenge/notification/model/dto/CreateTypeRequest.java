package de.dk.api.codeChallenge.notification.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CreateTypeRequest(
        @NotBlank(message = "name must not be blank")
        String name,

        @NotBlank(message = "category must not be blank")
        @Pattern(regexp = "A|B", message = "category must be either A or B")
        String category
) {
}