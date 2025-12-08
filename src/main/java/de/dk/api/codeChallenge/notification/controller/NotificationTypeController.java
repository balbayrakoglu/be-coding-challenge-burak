package de.dk.api.codeChallenge.notification.controller;

import de.dk.api.codeChallenge.notification.model.NotificationTypeEntity;
import de.dk.api.codeChallenge.notification.model.dto.CreateTypeRequest;
import de.dk.api.codeChallenge.notification.service.NotificationTypeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/notification-types")
public class NotificationTypeController {

    private final NotificationTypeService notificationTypeService;

    public NotificationTypeController(NotificationTypeService notificationTypeService) {
        this.notificationTypeService = notificationTypeService;
    }

    @PostMapping
    public ResponseEntity<NotificationTypeEntity> create(@Valid @RequestBody CreateTypeRequest body) {
        NotificationTypeEntity created = notificationTypeService.createOrGet(body.name(), body.category());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }
}
