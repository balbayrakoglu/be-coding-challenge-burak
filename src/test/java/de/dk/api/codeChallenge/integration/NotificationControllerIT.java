package de.dk.api.codeChallenge.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.dk.api.codeChallenge.notification.model.dto.UserRegisterRequestDto;
import de.dk.api.codeChallenge.notification.repository.UserRepository;
import java.util.HashSet;
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
        Set<String> notifications = new HashSet<>();
        notifications.add("type1");
        UserRegisterRequestDto user = new UserRegisterRequestDto(UUID.randomUUID(), notifications);

        ResponseEntity<UserRegisterRequestDto> response = sendPostRequest("/register", user,
                UserRegisterRequestDto.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
