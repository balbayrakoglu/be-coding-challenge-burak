package de.dk.api.codeChallenge.common.mapper;

import de.dk.api.codeChallenge.notification.model.dto.RegisterRequestDto;
import de.dk.api.codeChallenge.notification.model.dto.UserRegisterRequestDto;
import java.util.Objects;
import org.springframework.stereotype.Component;

@Component
public class RegisterUserDtoConverter {
    public RegisterRequestDto convert(UserRegisterRequestDto from) {
        Objects.requireNonNull(from.id());
        return new RegisterRequestDto(
                from.id(),
                from.notifications());
    }
}
