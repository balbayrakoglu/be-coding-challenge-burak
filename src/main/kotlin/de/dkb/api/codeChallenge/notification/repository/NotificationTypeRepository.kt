package de.dkb.api.codeChallenge.notification.repository


import de.dkb.api.codeChallenge.notification.model.enums.Category
import de.dkb.api.codeChallenge.notification.model.NotificationTypeEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface NotificationTypeRepository : JpaRepository<NotificationTypeEntity, UUID> {
    fun findByNameIgnoreCase(name: String): NotificationTypeEntity?
    fun findAllByCategory(category: Category): List<NotificationTypeEntity>
}
