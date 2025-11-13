package de.dkb.api.codeChallenge.notification.messaging.kafka

import de.dkb.api.codeChallenge.notification.model.dto.NotificationDto
import de.dkb.api.codeChallenge.notification.service.NotificationService
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.annotation.RetryableTopic
import org.springframework.retry.annotation.Backoff
import org.springframework.stereotype.Component

@Component
class NotificationSubscriber(
    private val notificationService: NotificationService
) {
    private val log = LoggerFactory.getLogger(javaClass)

/*    @RetryableTopic(
        attempts = "3",
        backoff = Backoff(delay = 2000),
        dltTopicSuffix = "-dlq",
        autoCreateTopics = "true"
    )*/
    @KafkaListener(
        topics = ["notifications"],
        groupId = "codechallenge_group",
        autoStartup = "\${kafka.listener.enabled:false}"
    )
    fun consumeNotification(notificationDto: NotificationDto) {
        try {
            notificationService.sendNotification(notificationDto)
        } catch (ex: Exception) {
            log.error("sendNotification failed for message: {}", notificationDto, ex)
            throw ex
        }
    }
}