package de.dkb.api.codeChallenge.notification.model.dto

import java.util.UUID

data class UserRegisterRequestDto(
    val id: UUID,
    val notifications: MutableSet<String> = mutableSetOf()
) {
}