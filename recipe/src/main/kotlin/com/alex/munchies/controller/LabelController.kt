package com.alex.munchies.controller

import com.alex.munchies.domain.LabelRequest
import com.alex.munchies.service.LabelService
import com.alex.munchies.util.Path
import com.alex.munchies.util.RateLimit
import io.github.resilience4j.ratelimiter.annotation.RateLimiter
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

@Suppress("unused")
@RateLimiter(name = RateLimit.BASIC)
@RestController
@RequestMapping(Path.LABEL)
class LabelController(private val labelService: LabelService) {

    // create

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody label: LabelRequest) = labelService.create(label)

    // read

    @GetMapping
    fun readAll() = labelService.readAll()

    @GetMapping(Path.ID)
    fun read(@PathVariable id: Long) = labelService.read(id)

    // update

    @PutMapping(Path.ID)
    fun update(@PathVariable id: Long, @RequestBody labelNew: LabelRequest) = labelService.update(id, labelNew)

    // delete

    @DeleteMapping(Path.ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) = labelService.delete(id)
}
