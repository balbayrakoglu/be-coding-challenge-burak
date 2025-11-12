package de.dkb.api.codeChallenge.notification.controller

import de.dkb.api.codeChallenge.notification.model.dto.NotificationDto
import de.dkb.api.codeChallenge.notification.model.dto.RegisterRequestDto
import de.dkb.api.codeChallenge.notification.model.dto.UserRegisterRequestDto
import de.dkb.api.codeChallenge.notification.model.dto.UserResponseDto
import de.dkb.api.codeChallenge.common.mapper.RegisterUserDtoConverter
import de.dkb.api.codeChallenge.common.mapper.UserResponseDtoConverter
import de.dkb.api.codeChallenge.notification.service.NotificationService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping
class NotificationController(
    private val notificationService: NotificationService,
    private val registerUserDtoConverter: RegisterUserDtoConverter,
    private val userResponseDtoConverter: UserResponseDtoConverter
) {

    @PostMapping("/register")
    fun registerUser( @RequestBody userApiModel: UserRegisterRequestDto): UserRegisterRequestDto {
        val dto: RegisterRequestDto = registerUserDtoConverter.convert(userApiModel)   // API -> DTO
        val result: UserResponseDto = notificationService.register(dto)        // Service
        return userResponseDtoConverter.convert(result)                        // DTO -> API
    }

    @PostMapping("/notify")
    fun sendNotification( @RequestBody notificationDto: NotificationDto) {
        notificationService.sendNotification(notificationDto)
    }
}