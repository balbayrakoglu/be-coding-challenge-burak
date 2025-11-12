package de.dkb.api.codeChallenge.notification.controller

import de.dkb.api.codeChallenge.notification.service.NotificationTypeService

import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.constraints.Pattern


@Validated
@RestController
@RequestMapping("/notification-types")
class NotificationTypeController(
    private val notificationTypeService: NotificationTypeService
) {
    data class CreateTypeRequest(
        @field:Pattern(regexp = "^[a-zA-Z0-9_-]{1,64}$") val name: String,
        @field:Pattern(regexp = "A|B") val category: String
    )

    @PostMapping
    fun create(@RequestBody body: CreateTypeRequest) =
        ResponseEntity.ok(notificationTypeService.createOrGet(body.name, body.category))
}