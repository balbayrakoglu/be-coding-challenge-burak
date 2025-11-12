package de.dkb.api.codeChallenge.notification.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "users")
class UserEntity(

    @Id
    @Column(columnDefinition = "uuid")
    val id: UUID = UUID.randomUUID(),

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_notifications",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "notification_type_id")]
    )
    val notifications: MutableSet<NotificationTypeEntity> = mutableSetOf()
)