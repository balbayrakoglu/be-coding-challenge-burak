package de.dk.api.codeChallenge.notification.repository;

import de.dk.api.codeChallenge.notification.model.NotificationTypeEntity;
import de.dk.api.codeChallenge.notification.model.enums.Category;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationTypeRepository extends JpaRepository<NotificationTypeEntity, UUID> {
    NotificationTypeEntity findByNameIgnoreCase(String name);

    List<NotificationTypeEntity> findAllByCategory(Category category);
}
