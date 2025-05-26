package com.alex.munchies.service

import com.alex.munchies.configuration.SpringProfile
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Service
@Profile(SpringProfile.DEVELOPMENT)
class RabbitMqProducer(@Value("\${values.rabbitmq.queue}") private val queue: String) {

    @Autowired
    private lateinit var rabbitTemplate: RabbitTemplate

    fun sendMessage(message: String) {
        rabbitTemplate.convertAndSend( queue, message)
    }
}