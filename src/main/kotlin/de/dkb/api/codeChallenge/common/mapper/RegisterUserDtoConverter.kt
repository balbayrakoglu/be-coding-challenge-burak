package de.dkb.api.codeChallenge.common.mapper

import de.dkb.api.codeChallenge.notification.model.dto.UserRegisterRequestDto
import de.dkb.api.codeChallenge.notification.model.dto.RegisterRequestDto
import org.springframework.stereotype.Component

@Component
class RegisterUserDtoConverter {
    fun convert(from: UserRegisterRequestDto): RegisterRequestDto =
        RegisterRequestDto(
            userId = requireNotNull(from.id),
            notificationTypeNames = from.notifications.toSet()
        )
}