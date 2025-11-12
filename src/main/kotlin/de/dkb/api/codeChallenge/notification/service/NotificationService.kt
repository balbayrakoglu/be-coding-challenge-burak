package de.dkb.api.codeChallenge.notification.service


import de.dkb.api.codeChallenge.notification.model.dto.NotificationDto
import de.dkb.api.codeChallenge.notification.model.dto.RegisterRequestDto
import de.dkb.api.codeChallenge.notification.model.dto.UserResponseDto
import de.dkb.api.codeChallenge.notification.model.enums.Category
import de.dkb.api.codeChallenge.notification.model.NotificationTypeEntity
import de.dkb.api.codeChallenge.notification.model.UserEntity
import de.dkb.api.codeChallenge.notification.repository.NotificationTypeRepository
import de.dkb.api.codeChallenge.notification.repository.UserRepository
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class NotificationService(
    private val userRepository: UserRepository,
    private val notificationTypeRepository: NotificationTypeRepository
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun register(req: RegisterRequestDto): UserResponseDto {
       val user = userRepository.findById(req.userId).orElse(UserEntity(id = req.userId))

       val incomingNames: Set<String> = req.notificationTypeNames
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .toSet()

       val resolved: Set<NotificationTypeEntity> = incomingNames.mapNotNull { name ->
            notificationTypeRepository.findByNameIgnoreCase(name).also {
                if (it == null) log.warn("register.unknown_type name='{}'", name)
            }
        }.toSet()

        val categories: Set<Category> = resolved.map { it.category }.toSet()
        val fullByCategory: MutableSet<NotificationTypeEntity> = categories
            .flatMap { cat -> notificationTypeRepository.findAllByCategory(cat) }
            .toMutableSet()

        user.notifications.clear()
        user.notifications.addAll(fullByCategory)
        userRepository.save(user)

        return UserResponseDto(
            userId = user.id,
            notificationTypeNames = fullByCategory.map { it.name }.toSet()
        )
    }

    fun sendNotification(notificationDto: NotificationDto) {
        val notificationType = notificationTypeRepository.findByNameIgnoreCase(notificationDto.notificationType)
            ?: run {
                log.warn("Unknown notification type: '{}'", notificationDto.notificationType)
                return
            }

        val user = userRepository.findByIdWithNotifications(notificationDto.userId).orElse(null)
            ?: run {
                log.info("User not found: {}", notificationDto.userId)
                return
            }

        val allowed = user.notifications.any { it.category == notificationType.category }

        if (allowed) {
            log.info(
                "Sending '{}' (cat={}) to {}: {}",
                notificationType.name, notificationType.category, user.id, notificationDto.message
            )
        } else {
            log.info(
                "Denied '{}' (cat={}) to {}",
                notificationType.name, notificationType.category, user.id
            )
        }
    }
}