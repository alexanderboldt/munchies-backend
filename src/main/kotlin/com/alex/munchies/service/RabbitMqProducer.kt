package com.alex.munchies.service

import com.alex.munchies.configuration.SpringProfile
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

/**
 * Producer class for RabbitMQ.
 *
 * @property queue The queue as a `String` will automatically be extracted from the configuration-file.
 */
@Service
@Profile(SpringProfile.DEVELOPMENT)
class RabbitMqProducer(@param:Value("\${values.rabbitmq.queue}") private val queue: String) {

    @Autowired
    private lateinit var rabbitTemplate: RabbitTemplate

    /**
     * Sends a message to the message-broker.
     *
     * @param message The message as a `String`.
     */
    fun sendMessage(message: String) {
        rabbitTemplate.convertAndSend( queue, message)
    }
}
