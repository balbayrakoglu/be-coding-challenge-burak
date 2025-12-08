package de.dk.api.codeChallenge.notification.repository;

import de.dk.api.codeChallenge.notification.model.UserEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    @Query("select distinct u from UserEntity u left join fetch u.notifications where u.id = :id")
    @EntityGraph(attributePaths = { "notifications" })
    Optional<UserEntity> findByIdWithNotifications(@Param("id") UUID id);
}
