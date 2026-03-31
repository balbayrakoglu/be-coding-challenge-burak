package de.dk.api.codeChallenge.notification.controller;

import de.dk.api.codeChallenge.common.mapper.RegisterUserDtoConverter;
import de.dk.api.codeChallenge.notification.model.dto.NotificationDto;
import de.dk.api.codeChallenge.notification.model.dto.NotificationResultDto;
import de.dk.api.codeChallenge.notification.model.dto.RegisterRequestDto;
import de.dk.api.codeChallenge.notification.model.dto.UserRegisterRequestDto;
import de.dk.api.codeChallenge.notification.model.dto.UserResponseDto;
import de.dk.api.codeChallenge.notification.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping
public class NotificationController {

    private final NotificationService notificationService;
    private final RegisterUserDtoConverter registerUserDtoConverter;

    public NotificationController(
            NotificationService notificationService,
            RegisterUserDtoConverter registerUserDtoConverter
    ) {
        this.notificationService = notificationService;
        this.registerUserDtoConverter = registerUserDtoConverter;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> registerUser(@Valid @RequestBody UserRegisterRequestDto userApiModel) {
        RegisterRequestDto dto = registerUserDtoConverter.convert(userApiModel);
        UserResponseDto result = notificationService.register(dto);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/notify")
    public ResponseEntity<NotificationResultDto> sendNotification(@Valid @RequestBody NotificationDto notificationDto) {
        NotificationResultDto result = notificationService.sendNotification(notificationDto);
        return ResponseEntity.ok(result);
    }
}