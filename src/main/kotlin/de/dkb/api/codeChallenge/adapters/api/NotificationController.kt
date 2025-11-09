package de.dkb.api.codeChallenge.adapters.api

import de.dkb.api.codeChallenge.application.dto.NotificationDto
import de.dkb.api.codeChallenge.domain.model.User
import de.dkb.api.codeChallenge.application.service.NotificationService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping()
class NotificationController(private val notificationService: NotificationService) {

    data class RegisterRequest(
        val userId: UUID,
        val notifications: Set<String> // e.g. ["type1","type3"]
    )

    data class RegisterResponse(val userId: UUID, val subscribedTypes: Set<String>)
    data class NotifyResponse(val sent: Boolean, val reason: String?)


  /*  @PostMapping("/register")
    fun registerUser(@RequestBody user: User) =
        notificationService.registerUser(user)*/


/*

    @PostMapping("/notify")
    fun sendNotification(@RequestBody notificationDto: NotificationDto) =
        notificationService.sendNotification(notificationDto)
*/

    @PostMapping("/register")
    fun registerUser(@RequestBody @Valid body: RegisterRequest): ResponseEntity<RegisterResponse> {
        val user = User(id = body.userId) // keep entity internal
        val types = notificationService.registerUser(user, body.notifications)
        return ResponseEntity.ok(RegisterResponse(body.userId, types.map { it.name }.toSet()))
    }

    @PostMapping("/notify")
    fun sendNotification(@RequestBody @Valid notificationDto: NotificationDto): ResponseEntity<NotifyResponse> {
        val sent = notificationService.sendNotification(notificationDto)
        val res = if (sent) NotifyResponse(true, "published") else NotifyResponse(false, "user not subscribed")
        return ResponseEntity.ok(res)
    }
}