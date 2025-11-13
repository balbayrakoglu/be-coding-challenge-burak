package de.dkb.api.codeChallenge.config

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import java.time.Duration

@Configuration
@EnableCaching
class RedisConfig {

    @Bean
    fun cacheManager(connectionFactory: RedisConnectionFactory): RedisCacheManager {
        val objectMapper: ObjectMapper = jacksonObjectMapper()
            .setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY)

        val serializationPair = RedisSerializationContext.SerializationPair
            .fromSerializer(GenericJackson2JsonRedisSerializer(objectMapper))

        val cacheConfig = RedisCacheConfiguration.defaultCacheConfig()
            .serializeValuesWith(serializationPair)
            .entryTtl(Duration.ofMinutes(10))

        return RedisCacheManager.builder(connectionFactory)
            .cacheDefaults(cacheConfig)
            .build()
    }
}