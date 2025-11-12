package de.dkb.api.codeChallenge.notification.model

import de.dkb.api.codeChallenge.notification.model.enums.Category
import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import java.util.UUID

@Entity
@Table(
    name = "notification_types",
    uniqueConstraints = [UniqueConstraint(name = "uk_notification_types_name", columnNames = ["name"])],
    indexes = [Index(name = "idx_notification_types_category", columnList = "category")]
)
class NotificationTypeEntity(

    @Id
    @UuidGenerator
    @Column(columnDefinition = "uuid")
    val id: UUID? = null,

    @Column(nullable = false, length = 64)
    val name: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    val category: Category
)

