package de.dk.api.codeChallenge.notification.controller;

import de.dk.api.codeChallenge.common.mapper.RegisterUserDtoConverter;
import de.dk.api.codeChallenge.common.mapper.UserResponseDtoConverter;
import de.dk.api.codeChallenge.notification.model.dto.NotificationDto;
import de.dk.api.codeChallenge.notification.model.dto.RegisterRequestDto;
import de.dk.api.codeChallenge.notification.model.dto.UserRegisterRequestDto;
import de.dk.api.codeChallenge.notification.model.dto.UserResponseDto;
import de.dk.api.codeChallenge.notification.service.NotificationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class NotificationController {

    private final NotificationService notificationService;
    private final RegisterUserDtoConverter registerUserDtoConverter;
    private final UserResponseDtoConverter userResponseDtoConverter;

    public NotificationController(
            NotificationService notificationService,
            RegisterUserDtoConverter registerUserDtoConverter,
            UserResponseDtoConverter userResponseDtoConverter) {
        this.notificationService = notificationService;
        this.registerUserDtoConverter = registerUserDtoConverter;
        this.userResponseDtoConverter = userResponseDtoConverter;
    }

    @PostMapping("/register")
    public UserRegisterRequestDto registerUser(@RequestBody UserRegisterRequestDto userApiModel) {
        RegisterRequestDto dto = registerUserDtoConverter.convert(userApiModel);
        UserResponseDto result = notificationService.register(dto);
        return userResponseDtoConverter.convert(result);
    }

    @PostMapping("/notify")
    public void sendNotification(@RequestBody NotificationDto notificationDto) {
        notificationService.sendNotification(notificationDto);
    }
}
