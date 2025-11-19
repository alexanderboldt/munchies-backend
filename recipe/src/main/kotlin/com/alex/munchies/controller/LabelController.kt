package com.alex.munchies.controller

import com.alex.munchies.Path
import com.alex.munchies.domain.LabelRequest
import com.alex.munchies.service.LabelService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@Suppress("unused")
@RestController
@RequestMapping(Path.LABEL)
class LabelController(private val labelService: LabelService) {

    // create

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestHeader userId: String, @RequestBody label: LabelRequest) = labelService.create(userId, label)

    // read

    @GetMapping
    fun readAll(@RequestHeader userId: String) = labelService.readAll(userId)

    @GetMapping(Path.ID)
    fun read(@RequestHeader userId: String, @PathVariable id: Long) = labelService.read(userId, id)

    // update

    @PutMapping(Path.ID)
    fun update(@RequestHeader userId: String, @PathVariable id: Long, @RequestBody labelNew: LabelRequest) = labelService.update(userId, id, labelNew)

    // delete

    @DeleteMapping(Path.ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@RequestHeader userId: String, @PathVariable id: Long) = labelService.delete(userId, id)
}
