package de.dkb.api.codeChallenge.notification.service

import de.dkb.api.codeChallenge.notification.model.enums.Category
import de.dkb.api.codeChallenge.notification.model.NotificationTypeEntity
import de.dkb.api.codeChallenge.notification.repository.NotificationTypeRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class NotificationTypeService(
    private val notificationTypeRepository: NotificationTypeRepository
) {
    @Transactional
    fun createOrGet(name: String, category: String): NotificationTypeEntity {
        val existing = notificationTypeRepository.findByNameIgnoreCase(name)
        if (existing != null) return existing
        val cat = Category.valueOf(category.uppercase())
        return notificationTypeRepository.save(NotificationTypeEntity(name = name.trim(), category = cat))
    }
}