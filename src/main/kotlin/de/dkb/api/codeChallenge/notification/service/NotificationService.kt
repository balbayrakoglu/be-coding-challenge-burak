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
    private val notificationTypeRepository: NotificationTypeRepository,
    private val notificationTypeCacheService: NotificationTypeCacheService
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    fun register(request: RegisterRequestDto): UserResponseDto {
        val user = userRepository.findById(request.userId).orElseGet { UserEntity(id = request.userId) }

        val incomingTypeNames: Set<String> = request.notificationTypeNames
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .toSet()

        val resolvedTypes: Set<NotificationTypeEntity> = incomingTypeNames.mapNotNull { name ->
            notificationTypeRepository.findByNameIgnoreCase(name)?.also {
                log.debug("Resolved type: '{}'", name)
            } ?: log.warn("Unknown type name: '{}'", name).let { null }
        }.toSet()

        val subscribedCategories: Set<Category> = resolvedTypes.map { it.category }.toSet()

        val fullTypesByCategory = subscribedCategories
            .flatMap { category -> notificationTypeCacheService.getAllByCategory(category) }
            .toMutableSet()

        user.notifications.clear()
        user.notifications.addAll(fullTypesByCategory)
        userRepository.save(user)

        log.info(
            "User {} registered with categories: {} and types: {}",
            user.id,
            subscribedCategories.joinToString(", ") { it.name },
            fullTypesByCategory.joinToString(", ") { it.name }
        )

        return UserResponseDto(
            userId = user.id,
            notificationTypeNames = fullTypesByCategory.map { it.name }.toSet()
        )
    }

    fun sendNotification(notificationDto: NotificationDto) {
        val notificationType = notificationTypeRepository.findByNameIgnoreCase(notificationDto.notificationType)
            ?: run {
                log.warn("sendNotifitacion.unknown_type type: '{}'", notificationDto.notificationType)
                return
            }

        val user = userRepository.findByIdWithNotifications(notificationDto.userId).orElse(null)
            ?: run {
                log.info("sendNotification.user_not_found user: {}", notificationDto.userId)
                return
            }

        val isUserSubscribedToCategory = user.notifications.any { it.category == notificationType.category }

        if (isUserSubscribedToCategory) {
            log.info(
                "sendNotification.allowed user: {} type: '{}' category: {} message= '{}'",
                notificationType.name, notificationType.category, user.id, notificationDto.message
            )
        } else {
            log.info(
                "sendNotification.denied user: '{}' type: '{}' category: {}",
                user.id, notificationType.name, notificationType.category
            )
        }
    }
}