package de.dkb.api.codeChallenge.notification.service

import de.dkb.api.codeChallenge.notification.model.enums.Category
import de.dkb.api.codeChallenge.notification.model.NotificationTypeEntity
import de.dkb.api.codeChallenge.notification.repository.NotificationTypeRepository
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.CacheEvict
import org.springframework.stereotype.Service

@Service
class NotificationTypeService(
    private val notificationTypeRepository: NotificationTypeRepository
) {

    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    @CacheEvict(value = ["notificationTypesByCategory"], allEntries = true)
    fun createOrGet(name: String, category: String): NotificationTypeEntity {
        val normalizedName = name.trim().lowercase()
        val existing = notificationTypeRepository.findByNameIgnoreCase(normalizedName)
        if (existing != null) return existing

        val parsedCategory = try {
            Category.valueOf(category.uppercase())
        } catch (ex: IllegalArgumentException) {
            log.error("createOrGet.invalid_category name='{}' category='{}'", name, category)
            throw IllegalArgumentException("Invalid category '$category'")
        }

        return notificationTypeRepository.save(
            NotificationTypeEntity(name = normalizedName, category = parsedCategory)
        ).also {
            log.info("createOrGet.created name='{}' category={}", it.name, it.category)
        }
    }
}