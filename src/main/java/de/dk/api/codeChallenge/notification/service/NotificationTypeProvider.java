package de.dk.api.codeChallenge.notification.service;

import de.dk.api.codeChallenge.notification.model.NotificationTypeEntity;
import de.dk.api.codeChallenge.notification.model.enums.Category;
import de.dk.api.codeChallenge.notification.repository.NotificationTypeRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class NotificationTypeProvider {

    private final NotificationTypeRepository notificationTypeRepository;
    private final Logger log = LoggerFactory.getLogger(getClass());

    public NotificationTypeProvider(NotificationTypeRepository notificationTypeRepository) {
        this.notificationTypeRepository = notificationTypeRepository;
    }

    @Cacheable("notificationTypesByCategory")
    public List<NotificationTypeEntity> getAllByCategory(Category category) {
        log.info("Cache MISS for category={}", category);
        return notificationTypeRepository.findAllByCategory(category);
    }
}
