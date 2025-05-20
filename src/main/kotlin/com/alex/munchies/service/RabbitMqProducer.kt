package com.alex.munchies.service

import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class RabbitMqProducer(@Value("\${values.rabbitmq.queue}") private val queue: String) {

    @Autowired
    private lateinit var rabbitTemplate: RabbitTemplate

    fun sendMessage(message: String) {
        rabbitTemplate.convertAndSend( queue, message)
    }
}