package de.dk.api.codeChallenge.notification.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.dk.api.codeChallenge.notification.model.NotificationTypeEntity;
import de.dk.api.codeChallenge.notification.model.UserEntity;
import de.dk.api.codeChallenge.notification.model.dto.NotificationDto;
import de.dk.api.codeChallenge.notification.model.dto.NotificationResultDto;
import de.dk.api.codeChallenge.notification.model.dto.RegisterRequestDto;
import de.dk.api.codeChallenge.notification.model.dto.UserResponseDto;
import de.dk.api.codeChallenge.notification.model.enums.Category;
import de.dk.api.codeChallenge.notification.repository.NotificationTypeRepository;
import de.dk.api.codeChallenge.notification.repository.UserRepository;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NotificationServiceTest {

    private UserRepository userRepository;
    private NotificationTypeRepository notificationTypeRepository;
    private NotificationTypeProvider notificationTypeProvider;
    private NotificationService service;

    @BeforeEach
    void setup() {
        userRepository = mock(UserRepository.class);
        notificationTypeRepository = mock(NotificationTypeRepository.class);
        notificationTypeProvider = mock(NotificationTypeProvider.class);
        service = new NotificationService(userRepository, notificationTypeRepository, notificationTypeProvider);
    }

    @Test
    void register_givenValidCategory_shouldSaveUserWithAllCategoryTypes() {
        UUID userId = UUID.randomUUID();
        RegisterRequestDto req = new RegisterRequestDto(userId, Set.of("type1"));

        NotificationTypeEntity type1 = new NotificationTypeEntity("type1", Category.A);
        NotificationTypeEntity type2 = new NotificationTypeEntity("type2", Category.A);

        when(notificationTypeRepository.findByNameIgnoreCase("type1")).thenReturn(type1);
        when(notificationTypeProvider.getAllByCategory(Category.A)).thenReturn(List.of(type1, type2));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        UserResponseDto result = service.register(req);

        assertEquals(userId, result.userId());
        assertEquals(Set.of("type1", "type2"), result.notificationTypeNames());
        verify(userRepository, times(1)).save(any());
    }

    @Test
    void register_givenUnknownType_shouldThrowBadRequest() {
        UUID userId = UUID.randomUUID();
        RegisterRequestDto req = new RegisterRequestDto(userId, Set.of("unknown-type"));

        when(notificationTypeRepository.findByNameIgnoreCase("unknown-type")).thenReturn(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.register(req));
        assertEquals("Unknown notification type(s): unknown-type", ex.getMessage());
    }

    @Test
    void register_givenEmptyNotificationSet_shouldThrowBadRequest() {
        UUID userId = UUID.randomUUID();
        RegisterRequestDto req = new RegisterRequestDto(userId, Set.of());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.register(req));
        assertEquals("notificationTypeNames must not be empty", ex.getMessage());
    }

    @Test
    void sendNotification_givenSubscribedUser_shouldReturnSent() {
        UUID userId = UUID.randomUUID();

        NotificationTypeEntity notificationType = new NotificationTypeEntity("type1", Category.A);
        UserEntity userEntity = new UserEntity(userId, new HashSet<>(Set.of(notificationType)));

        when(notificationTypeRepository.findByNameIgnoreCase("type1")).thenReturn(notificationType);
        when(userRepository.findByIdWithNotifications(userId)).thenReturn(Optional.of(userEntity));

        NotificationDto dto = new NotificationDto(userId, "type1", "Hello");

        NotificationResultDto result = service.sendNotification(dto);

        assertEquals("SENT", result.status());
        verify(userRepository, times(1)).findByIdWithNotifications(userId);
        verify(notificationTypeRepository, times(1)).findByNameIgnoreCase("type1");
    }

    @Test
    void sendNotification_givenUnsubscribedUser_shouldReturnSkipped() {
        UUID userId = UUID.randomUUID();

        NotificationTypeEntity notificationType = new NotificationTypeEntity("type1", Category.A);
        UserEntity userEntity = new UserEntity(userId, new HashSet<>());

        when(notificationTypeRepository.findByNameIgnoreCase("type1")).thenReturn(notificationType);
        when(userRepository.findByIdWithNotifications(userId)).thenReturn(Optional.of(userEntity));

        NotificationDto dto = new NotificationDto(userId, "type1", "Hello");

        NotificationResultDto result = service.sendNotification(dto);

        assertEquals("SKIPPED", result.status());
        assertEquals("USER_NOT_SUBSCRIBED_TO_CATEGORY", result.reason());
    }

    @Test
    void sendNotification_givenUnknownType_shouldThrowBadRequest() {
        UUID userId = UUID.randomUUID();

        when(notificationTypeRepository.findByNameIgnoreCase("type1")).thenReturn(null);

        NotificationDto dto = new NotificationDto(userId, "type1", "Hello");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.sendNotification(dto));
        assertEquals("Unknown notification type: type1", ex.getMessage());
    }

    @Test
    void sendNotification_givenMissingUser_shouldThrowNotFound() {
        UUID userId = UUID.randomUUID();
        NotificationTypeEntity notificationType = new NotificationTypeEntity("type1", Category.A);

        when(notificationTypeRepository.findByNameIgnoreCase("type1")).thenReturn(notificationType);
        when(userRepository.findByIdWithNotifications(userId)).thenReturn(Optional.empty());

        NotificationDto dto = new NotificationDto(userId, "type1", "Hello");

        NoSuchElementException ex = assertThrows(NoSuchElementException.class, () -> service.sendNotification(dto));
        assertEquals("User not found: " + userId, ex.getMessage());
    }
}