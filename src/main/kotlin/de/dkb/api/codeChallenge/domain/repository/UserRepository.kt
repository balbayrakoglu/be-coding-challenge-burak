package de.dkb.api.codeChallenge.domain.repository

import de.dkb.api.codeChallenge.domain.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UserRepository : JpaRepository<User, UUID>