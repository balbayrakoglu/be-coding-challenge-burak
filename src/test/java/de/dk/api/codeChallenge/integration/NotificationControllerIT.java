package de.dk.api.codeChallenge.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.dk.api.codeChallenge.notification.model.dto.NotificationDto;
import de.dk.api.codeChallenge.notification.model.dto.UserRegisterRequestDto;
import de.dk.api.codeChallenge.notification.repository.UserRepository;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class NotificationControllerIT extends AbstractIntegrationTestBase {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void cleanDatabase() {
        userRepository.deleteAll();
    }

    @Test
    void registerUser_shouldReturnOk() throws JsonProcessingException {
        UserRegisterRequestDto user = new UserRegisterRequestDto(UUID.randomUUID(), Set.of("type1"));
        ResponseEntity<UserRegisterRequestDto> response = sendPostRequest("/register", user, UserRegisterRequestDto.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void registerUser_givenUnknownType_shouldReturnBadRequest() throws JsonProcessingException {
        UserRegisterRequestDto user = new UserRegisterRequestDto(UUID.randomUUID(), Set.of("unknown-type"));
        ResponseEntity<String> response = sendPostRequest("/register", user, String.class);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void registerUser_givenEmptyNotifications_shouldReturnBadRequest() throws JsonProcessingException {
        UserRegisterRequestDto user = new UserRegisterRequestDto(UUID.randomUUID(), Set.of());
        ResponseEntity<String> response = sendPostRequest("/register", user, String.class);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void notify_givenUnknownType_shouldReturnBadRequest() throws JsonProcessingException {
        NotificationDto dto = new NotificationDto(UUID.randomUUID(), "unknown-type", "hello");
        ResponseEntity<String> response = sendPostRequest("/notify", dto, String.class);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}