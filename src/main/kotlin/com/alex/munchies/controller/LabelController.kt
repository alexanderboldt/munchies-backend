package com.alex.munchies.controller

import com.alex.munchies.service.RabbitMqProducer
import com.alex.munchies.exception.LabelNotFoundException
import com.alex.munchies.repository.UserService
import com.alex.munchies.repository.api.ApiModelLabel
import com.alex.munchies.repository.database.label.LabelRepository
import com.alex.munchies.repository.mapping.newDbModel
import com.alex.munchies.repository.mapping.plus
import com.alex.munchies.repository.mapping.toApiModel
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/labels")
class LabelController(
    private val userService: UserService,
    private val labelRepository: LabelRepository,
    private val rabbitMqProducer: RabbitMqProducer
) {

    // create

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody label: ApiModelLabel): ApiModelLabel {
        rabbitMqProducer.sendMessage("created label")
        return labelRepository.save(label.newDbModel(userService.userId)).toApiModel()
    }

    // read

    @GetMapping
    fun readAll(): List<ApiModelLabel> {
        return labelRepository.findAllByUserId(userService.userId).map { it.toApiModel() }
    }

    @GetMapping("{id}")
    fun read(@PathVariable("id") id: Long): ApiModelLabel {
        val label = labelRepository.findByIdAndUserId(id, userService.userId) ?: throw LabelNotFoundException()
        return label.toApiModel()
    }

    // update

    @PutMapping("{id}")
    fun update(@PathVariable("id") id: Long, @RequestBody labelNew: ApiModelLabel): ApiModelLabel {
        val labelExisting = labelRepository.findByIdAndUserId(id, userService.userId) ?: throw LabelNotFoundException()
        return labelRepository.save(labelNew + labelExisting).toApiModel()
    }

    // delete

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable("id") id: Long) {
        labelRepository.apply {
            if (!existsByIdAndUserId(id, userService.userId)) throw LabelNotFoundException()
            deleteById(id)
        }
    }
}