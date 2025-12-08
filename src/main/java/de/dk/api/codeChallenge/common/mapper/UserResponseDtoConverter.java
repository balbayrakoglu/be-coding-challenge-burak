package de.dk.api.codeChallenge.common.mapper;

import de.dk.api.codeChallenge.notification.model.dto.UserRegisterRequestDto;
import de.dk.api.codeChallenge.notification.model.dto.UserResponseDto;
import java.util.HashSet;
import org.springframework.stereotype.Component;

@Component
public class UserResponseDtoConverter {
    public UserRegisterRequestDto convert(UserResponseDto from) {
        return new UserRegisterRequestDto(
                from.userId(),
                new HashSet<>(from.notificationTypeNames()));
    }
}
