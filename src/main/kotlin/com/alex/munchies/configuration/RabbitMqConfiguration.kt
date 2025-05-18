package com.alex.munchies.configuration

import org.springframework.amqp.core.Queue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitMqConfiguration {

    companion object {
        const val queue = "statistic"
    }

    @Bean
    fun queue(): Queue = Queue(queue, false)
}