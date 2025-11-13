package de.dkb.api.codeChallenge.notification.model

import de.dkb.api.codeChallenge.notification.model.enums.Category
import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import java.util.UUID

@Entity
@Table(
    name = "notification_types"
)
class NotificationTypeEntity() {

    @Id
    @UuidGenerator
    @Column(columnDefinition = "uuid")
    var id: UUID? = null

    @Column(nullable = false, length = 64)
    lateinit var name: String

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    lateinit var category: Category

    constructor(name: String, category: Category) : this() {
        this.name = name
        this.category = category
    }
}


