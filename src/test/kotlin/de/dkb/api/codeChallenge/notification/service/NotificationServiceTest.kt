package de.dkb.api.codeChallenge.notification.service

import de.dkb.api.codeChallenge.notification.model.NotificationTypeEntity
import de.dkb.api.codeChallenge.notification.model.UserEntity
import de.dkb.api.codeChallenge.notification.model.dto.NotificationDto
import de.dkb.api.codeChallenge.notification.model.dto.RegisterRequestDto
import de.dkb.api.codeChallenge.notification.model.enums.Category
import de.dkb.api.codeChallenge.notification.repository.NotificationTypeRepository
import de.dkb.api.codeChallenge.notification.repository.UserRepository
import io.mockk.*
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.verify
import java.util.*

class NotificationServiceTest {

    private val userRepository = mockk<UserRepository>(relaxed = true)
    private val notificationTypeRepository = mockk<NotificationTypeRepository>(relaxed = true)
    private val cacheService = mockk<NotificationTypeCacheService>(relaxed = true)

    private lateinit var service: NotificationService

    @BeforeEach
    fun setup() {
        service = NotificationService(userRepository, notificationTypeRepository, cacheService)
    }

    @Test
    fun register_givenValidCategory_shouldSaveUserWithAllCategoryTypes() {

        val userId = UUID.randomUUID()
        val req = RegisterRequestDto(userId, setOf("type1"))
        val type1 = NotificationTypeEntity(UUID.randomUUID(), "type1", Category.A)
        val type2 = NotificationTypeEntity(UUID.randomUUID(), "type2", Category.A)

        every { notificationTypeRepository.findAllByCategory(Category.A) } returns listOf(type1)
        every { cacheService.getAllByCategory(Category.A) } returns listOf(type1, type2)
        every { userRepository.findById(any()) } returns Optional.empty()
        every { userRepository.save(any()) } answers { firstArg() }

        val result = service.register(req)

        assertEquals(userId, result.userId)
        assertEquals(setOf("type1", "type2"), result.notificationTypeNames)
        verify { userRepository.save(any()) }
    }

    @Test
    fun sendNotification_givenSubscribedUser_shouldLogSend() {

        val userId = UUID.randomUUID()
        val notificationType = NotificationTypeEntity(UUID.randomUUID(), "type1", Category.A)
        val userEntity = UserEntity(id = userId, notifications = mutableSetOf(notificationType))
        every { notificationTypeRepository.findByNameIgnoreCase("type1") } returns notificationType
        every { userRepository.findByIdWithNotifications(userId) } returns Optional.of(userEntity)

        val dto = NotificationDto(userId, "type1", "Hello")

        service.sendNotification(dto)

        verify(exactly = 1) { userRepository.findByIdWithNotifications(userId) }
    }
}
