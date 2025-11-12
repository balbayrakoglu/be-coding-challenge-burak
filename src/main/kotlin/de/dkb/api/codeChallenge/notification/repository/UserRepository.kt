package de.dkb.api.codeChallenge.notification.repository

import de.dkb.api.codeChallenge.notification.model.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<UserEntity, UUID> {
    @Query("select distinct u from UserEntity u left join fetch u.notifications where u.id = :id")
    fun findByIdWithNotifications(@Param("id") id: UUID): Optional<UserEntity>
}