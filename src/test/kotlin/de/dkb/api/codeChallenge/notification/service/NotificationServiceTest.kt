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
import java.util.*
import kotlin.test.assertFailsWith

class NotificationServiceTest {

    private lateinit var userRepository: UserRepository
    private lateinit var notificationTypeRepository: NotificationTypeRepository
    private lateinit var cacheService: NotificationTypeCacheService
    private lateinit var service: NotificationService

    @BeforeEach
    fun setup() {
        userRepository = mockk()
        notificationTypeRepository = mockk()
        cacheService = mockk()
        service = NotificationService(userRepository, notificationTypeRepository, cacheService)
    }

    @Test
    fun register_givenValidCategory_shouldSaveUserWithAllCategoryTypes() {

        val userId = UUID.randomUUID()
        val req = RegisterRequestDto(userId, setOf("type1"))

        val type1 = NotificationTypeEntity("type1", Category.A)
        val type2 = NotificationTypeEntity("type2", Category.A)

        every { notificationTypeRepository.findAllByCategory(Category.A) } returns listOf(type1)
        every { cacheService.getAllByCategory(Category.A) } returns listOf(type1, type2)
        every { userRepository.findById(userId) } returns Optional.empty()
        every { userRepository.save(any()) } answers { firstArg() }

        val result = service.register(req)

        assertEquals(userId, result.userId)
        assertEquals(setOf("type1", "type2"), result.notificationTypeNames)
        verify(exactly = 1) { userRepository.save(any()) }
    }

    @Test
    fun sendNotification_givenSubscribedUser_shouldLogSend() {

        val userId = UUID.randomUUID()
        val notificationType = NotificationTypeEntity("type1", Category.A)
        val userEntity = UserEntity(id = userId, notifications = mutableSetOf(notificationType))

        every { notificationTypeRepository.findByNameIgnoreCase("type1") } returns notificationType
        every { userRepository.findByIdWithNotifications(userId) } returns Optional.of(userEntity)

        val dto = NotificationDto(userId, "type1", "Hello")

        service.sendNotification(dto)

        verify(exactly = 1) { userRepository.findByIdWithNotifications(userId) }
        verify(exactly = 1) { notificationTypeRepository.findByNameIgnoreCase("type1") }
    }

    @Test
    fun sendNotification_givenUnsubscribedUser_shouldThrowOrSkip() {
        val userId = UUID.randomUUID()
        val notificationType = NotificationTypeEntity("type1", Category.A)
        val userEntity = UserEntity(id = userId, notifications = mutableSetOf())

        every { notificationTypeRepository.findByNameIgnoreCase("type1") } returns notificationType
        every { userRepository.findByIdWithNotifications(userId) } returns Optional.of(userEntity)

        val dto = NotificationDto(userId, "type1", "Hello")

        assertFailsWith<IllegalArgumentException> {
            service.sendNotification(dto)
        }

        verify(exactly = 1) { userRepository.findByIdWithNotifications(userId) }
    }
}
