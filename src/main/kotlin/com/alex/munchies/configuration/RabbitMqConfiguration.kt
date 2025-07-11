package com.alex.munchies.configuration

import org.springframework.amqp.core.Queue
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Suppress("unused")
@Configuration
@Profile(SpringProfile.DEVELOPMENT)
class RabbitMqConfiguration(@param:Value("\${values.rabbitmq.queue}") private val queue: String) {

    @Bean
    fun queue(): Queue {
        return Queue(queue, false)
    }
}
