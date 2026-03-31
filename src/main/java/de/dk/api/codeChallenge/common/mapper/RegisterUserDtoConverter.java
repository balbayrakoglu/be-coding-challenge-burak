package de.dk.api.codeChallenge.common.mapper;

import de.dk.api.codeChallenge.notification.model.dto.RegisterRequestDto;
import de.dk.api.codeChallenge.notification.model.dto.UserRegisterRequestDto;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class RegisterUserDtoConverter {

    public RegisterRequestDto convert(UserRegisterRequestDto from) {
        Set<String> normalizedNotifications = from.notifications().stream()
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .collect(Collectors.toSet());

        return new RegisterRequestDto(from.id(), normalizedNotifications);
    }
}