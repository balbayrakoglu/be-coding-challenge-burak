package de.dkb.api.codeChallenge.notification.service

import de.dkb.api.codeChallenge.notification.model.NotificationTypeEntity
import de.dkb.api.codeChallenge.notification.model.enums.Category
import de.dkb.api.codeChallenge.notification.repository.NotificationTypeRepository
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class NotificationTypeCacheService(
    private val notificationTypeRepository: NotificationTypeRepository
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Cacheable("notificationTypesByCategory")
    fun getAllByCategory(category: Category): List<NotificationTypeEntity> {
        log.info("Cache MISS for category={}", category)
        return notificationTypeRepository.findAllByCategory(category)
    }
}