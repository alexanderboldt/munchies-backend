package com.alex.munchies.service

import com.alex.munchies.configuration.RabbitMqConfiguration
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RabbitMqProducer {

    @Autowired
    private lateinit var rabbitTemplate: RabbitTemplate

    fun sendMessage(message: String) {
        rabbitTemplate.convertAndSend( RabbitMqConfiguration.Companion.queue, message)
    }
}