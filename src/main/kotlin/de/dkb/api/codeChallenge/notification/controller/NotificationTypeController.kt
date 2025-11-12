package de.dkb.api.codeChallenge.notification.controller

import de.dkb.api.codeChallenge.notification.model.dto.CreateTypeRequest
import de.dkb.api.codeChallenge.notification.service.NotificationTypeService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Validated
@RestController
@RequestMapping("/notification-types")
class NotificationTypeController(
    private val notificationTypeService: NotificationTypeService
) {
    @PostMapping
    fun create(@Valid @RequestBody body: CreateTypeRequest): ResponseEntity<Any> {
        val created = notificationTypeService.createOrGet(body.name, body.category)
        return ResponseEntity
            .status(201)
            .body(created)
    }
}