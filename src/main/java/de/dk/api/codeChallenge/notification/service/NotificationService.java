package de.dk.api.codeChallenge.notification.service;

import de.dk.api.codeChallenge.notification.model.NotificationTypeEntity;
import de.dk.api.codeChallenge.notification.model.UserEntity;
import de.dk.api.codeChallenge.notification.model.dto.NotificationDto;
import de.dk.api.codeChallenge.notification.model.dto.RegisterRequestDto;
import de.dk.api.codeChallenge.notification.model.dto.UserResponseDto;
import de.dk.api.codeChallenge.notification.model.enums.Category;
import de.dk.api.codeChallenge.notification.repository.NotificationTypeRepository;
import de.dk.api.codeChallenge.notification.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final UserRepository userRepository;
    private final NotificationTypeRepository notificationTypeRepository;
    private final NotificationTypeProvider notificationTypeProvider;
    private final Logger log = LoggerFactory.getLogger(getClass());

    public NotificationService(
            UserRepository userRepository,
            NotificationTypeRepository notificationTypeRepository,
            NotificationTypeProvider notificationTypeProvider) {
        this.userRepository = userRepository;
        this.notificationTypeRepository = notificationTypeRepository;
        this.notificationTypeProvider = notificationTypeProvider;
    }

    @Transactional
    public UserResponseDto register(RegisterRequestDto request) {
        UserEntity user = userRepository.findById(request.userId())
                .orElseGet(() -> new UserEntity(request.userId()));

        Set<String> incomingTypeNames = request.notificationTypeNames().stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());

        Set<NotificationTypeEntity> resolvedTypes = new HashSet<>();
        for (String name : incomingTypeNames) {
            NotificationTypeEntity type = notificationTypeRepository.findByNameIgnoreCase(name);
            if (type != null) {
                log.debug("Resolved type: '{}'", name);
                resolvedTypes.add(type);
            } else {
                log.warn("Unknown type name: '{}'", name);
            }
        }

        Set<Category> subscribedCategories = resolvedTypes.stream()
                .map(NotificationTypeEntity::getCategory)
                .collect(Collectors.toSet());

        Set<NotificationTypeEntity> fullTypesByCategory;
        try {
            fullTypesByCategory = subscribedCategories.stream()
                    .flatMap(category -> notificationTypeProvider.getAllByCategory(category).stream())
                    .collect(Collectors.toSet());
        } catch (Exception ex) {
            log.error("Error while resolving types by category", ex);
            throw ex;
        }

        user.getNotifications().clear();
        user.getNotifications().addAll(fullTypesByCategory);
        userRepository.save(user);

        log.info(
                "User {} registered with categories: {} and types: {}",
                user.getId(),
                subscribedCategories.stream().map(Enum::name).collect(Collectors.joining(", ")),
                fullTypesByCategory.stream().map(NotificationTypeEntity::getName).collect(Collectors.joining(", ")));

        return new UserResponseDto(
                user.getId(),
                fullTypesByCategory.stream().map(NotificationTypeEntity::getName).collect(Collectors.toSet()));
    }

    public void sendNotification(NotificationDto notificationDto) {
        NotificationTypeEntity notificationType = notificationTypeRepository
                .findByNameIgnoreCase(notificationDto.notificationType());
        if (notificationType == null) {
            throw new IllegalArgumentException("Unknown notification type: " + notificationDto.notificationType());
        }

        UserEntity user = userRepository.findByIdWithNotifications(notificationDto.userId())
                .orElseThrow(() -> new NoSuchElementException("User not found: " + notificationDto.userId()));

        boolean isUserSubscribedToCategory = user.getNotifications().stream()
                .anyMatch(nt -> nt.getCategory() == notificationType.getCategory());

        if (isUserSubscribedToCategory) {
            log.info(
                    "sendNotification.allowed user: {} type: '{}' category: {} message= '{}'",
                    user.getId(), notificationType.getName(), notificationType.getCategory(),
                    notificationDto.message());
        } else {
            log.info(
                    "sendNotification.denied user: '{}' type: '{}' category: {}",
                    user.getId(), notificationType.getName(), notificationType.getCategory());
        }
    }
}
