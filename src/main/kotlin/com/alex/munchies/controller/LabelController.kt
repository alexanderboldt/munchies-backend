package com.alex.munchies.controller

import com.alex.munchies.domain.Label
import com.alex.munchies.service.LabelService
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
class LabelController(private val labelService: LabelService) {

    // create

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody label: Label) = labelService.create(label)

    // read

    @GetMapping
    fun readAll() = labelService.readAll()

    @GetMapping("{id}")
    fun read(@PathVariable("id") id: Long) = labelService.read(id)

    // update

    @PutMapping("{id}")
    fun update(@PathVariable("id") id: Long, @RequestBody labelNew: Label) = labelService.update(id, labelNew)

    // delete

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable("id") id: Long) = labelService.delete(id)
}