package de.dk.api.codeChallenge.notification.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.dk.api.codeChallenge.notification.model.NotificationTypeEntity;
import de.dk.api.codeChallenge.notification.model.enums.Category;
import de.dk.api.codeChallenge.notification.repository.NotificationTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NotificationTypeServiceTest {

    private NotificationTypeRepository repository;
    private NotificationTypeService service;

    @BeforeEach
    void setup() {
        repository = mock(NotificationTypeRepository.class);
        service = NotificationTypeService(repository);
    }

    private NotificationTypeService NotificationTypeService(NotificationTypeRepository repository) {
        return new NotificationTypeService(repository);
    }

    @Test
    void createOrGet_givenExistingType_shouldReturnExisting() {
        NotificationTypeEntity existing = new NotificationTypeEntity("type1", Category.A);
        when(repository.findByNameIgnoreCase("type1")).thenReturn(existing);

        NotificationTypeEntity result = service.createOrGet("type1", "A");

        assertEquals(existing, result);
        verify(repository, never()).save(any());
    }

    @Test
    void createOrGet_givenNewType_shouldSaveAndReturn() {
        when(repository.findByNameIgnoreCase("type2")).thenReturn(null);
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        NotificationTypeEntity result = service.createOrGet("type2", "B");

        assertEquals("type2", result.getName());
        assertEquals(Category.B, result.getCategory());
        verify(repository).save(any());
    }
}
