package de.dkb.api.codeChallenge.common.mapper

import de.dkb.api.codeChallenge.notification.model.dto.UserRegisterRequestDto
import de.dkb.api.codeChallenge.notification.model.dto.UserResponseDto
import org.springframework.stereotype.Component


@Component
class UserResponseDtoConverter {
    fun convert(from: UserResponseDto): UserRegisterRequestDto =
        UserRegisterRequestDto(
            id = from.userId,
            notifications = from.notificationTypeNames.toMutableSet()
        )
}
