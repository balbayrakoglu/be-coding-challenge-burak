package de.dkb.api.codeChallenge.application.service

import de.dkb.api.codeChallenge.application.dto.NotificationDto
import de.dkb.api.codeChallenge.domain.model.NotificationType
import de.dkb.api.codeChallenge.domain.model.User
import de.dkb.api.codeChallenge.domain.repository.UserRepository
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.LinkedHashSet
import java.util.Locale

@Service
class NotificationService(private val userRepository: UserRepository) {

    private val log = LoggerFactory.getLogger(NotificationService::class.java)

    @Transactional
    fun registerUser(user: User, rawTypes: Collection<String>): Set<NotificationType> {
        val types = rawTypes.asSequence()
            .map(String::trim)
            .filter(String::isNotEmpty)
            .map { it.uppercase(Locale.ROOT) }
            .mapNotNull { runCatching { NotificationType.valueOf(it) }.getOrNull() }
            .toCollection(LinkedHashSet())
        user.notifications = types.toMutableSet()
        userRepository.save(user)
        log.info("User {} subscribed to {}", user.id, types)
        return types
    }


    fun sendNotification(notificationDto: NotificationDto): Boolean =
        userRepository.findById(notificationDto.userId)
            .map { user ->
                if (user.notifications.contains(notificationDto.notificationType)) {
                    // Here you'd call a publisher port (Kafka) instead of println
                    log.info(
                        "Sending {} to user {}: {}",
                        notificationDto.notificationType,
                        user.id,
                        notificationDto.message
                    )
                    true
                } else {
                    log.info("Ignored: user {} not subscribed to {}", user.id, notificationDto.notificationType)
                    false
                }
            }
            .orElse(false)
}