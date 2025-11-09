package de.dkb.api.codeChallenge.domain.model

import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "users")
data class User(
    @Id
    @Column(columnDefinition = "uuid")
    val id: UUID,

    @Convert(converter = NotificationTypeSetConverter::class)
    var notifications: MutableSet<NotificationType> = mutableSetOf(),
) {
    // Default constructor for Hibernate
    constructor() : this(UUID.randomUUID(), mutableSetOf())
}