package de.dkb.api.codeChallenge.notification.service

import de.dkb.api.codeChallenge.notification.model.enums.Category
import de.dkb.api.codeChallenge.notification.model.NotificationTypeEntity
import de.dkb.api.codeChallenge.notification.repository.NotificationTypeRepository
import io.mockk.*
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class NotificationTypeServiceTest {

    private val repository = mockk<NotificationTypeRepository>(relaxed = true)
    private lateinit var service: NotificationTypeService

    @BeforeEach
    fun setup() {
        service = NotificationTypeService(repository)
    }

    @Test
    fun createOrGet_givenExistingType_shouldReturnExisting() {
        val existing = NotificationTypeEntity(name = "type1", category = Category.A)
        every { repository.findByNameIgnoreCase("type1") } returns existing

        val result = service.createOrGet("type1", "A")

        assertEquals(existing, result)
        verify(exactly = 0) { repository.save(any()) }
    }

    @Test
    fun createOrGet_givenNewType_shouldSaveAndReturn() {
        every { repository.findByNameIgnoreCase("type2") } returns null
        every { repository.save(any()) } answers { firstArg() }

        val result = service.createOrGet("type2", "B")

        assertEquals("type2", result.name)
        assertEquals(Category.B, result.category)
        verify { repository.save(any()) }
    }
}
