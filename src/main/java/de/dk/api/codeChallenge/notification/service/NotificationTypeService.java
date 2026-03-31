package de.dk.api.codeChallenge.notification.service;

import de.dk.api.codeChallenge.notification.model.NotificationTypeEntity;
import de.dk.api.codeChallenge.notification.model.enums.Category;
import de.dk.api.codeChallenge.notification.repository.NotificationTypeRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
public class NotificationTypeService {

    private final NotificationTypeRepository notificationTypeRepository;
    private final Logger log = LoggerFactory.getLogger(getClass());

    public NotificationTypeService(NotificationTypeRepository notificationTypeRepository) {
        this.notificationTypeRepository = notificationTypeRepository;
    }

    @Transactional
    @CacheEvict(value = "notificationTypesByCategory", allEntries = true)
    public NotificationTypeEntity createOrGet(String name, String category) {
        String normalizedName = name == null ? null : name.trim().toLowerCase();
        if (normalizedName == null || normalizedName.isBlank()) {
            throw new IllegalArgumentException("Notification type name must not be blank");
        }

        NotificationTypeEntity existing = notificationTypeRepository.findByNameIgnoreCase(normalizedName);
        if (existing != null) {
            return existing;
        }

        Category parsedCategory;
        try {
            parsedCategory = Category.valueOf(category.trim().toUpperCase());
        } catch (Exception ex) {
            log.error("createOrGet.invalid_category name='{}' category='{}'", name, category);
            throw new IllegalArgumentException("Invalid category '" + category + "'");
        }

        NotificationTypeEntity entity = new NotificationTypeEntity(normalizedName, parsedCategory);
        entity = notificationTypeRepository.save(entity);

        log.info("createOrGet.created name='{}' category={}", entity.getName(), entity.getCategory());
        return entity;
    }
}