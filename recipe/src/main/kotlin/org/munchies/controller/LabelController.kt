package org.munchies.controller

import org.munchies.Header
import org.munchies.Path
import org.munchies.PathParam
import org.munchies.domain.LabelRequest
import org.munchies.service.LabelService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class LabelController(private val labelService: LabelService) {

    // create

    @PostMapping(Path.LABELS, version = "1")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @RequestHeader(Header.USER_ID) userId: String,
        @RequestBody label: LabelRequest
    ) = labelService.create(userId, label)

    // read

    @GetMapping(Path.LABELS, version = "1")
    fun readAll(@RequestHeader(Header.USER_ID) userId: String) = labelService.readAll(userId)

    @GetMapping(Path.LABELS_ID, version = "1")
    fun read(
        @RequestHeader(Header.USER_ID) userId: String,
        @PathVariable(PathParam.LABEL_ID) id: Long
    ) = labelService.read(userId, id)

    // update

    @PutMapping(Path.LABELS_ID, version = "1")
    fun update(
        @RequestHeader(Header.USER_ID) userId: String,
        @PathVariable(PathParam.LABEL_ID) id: Long,
        @RequestBody labelNew: LabelRequest
    ) = labelService.update(userId, id, labelNew)

    // delete

    @DeleteMapping(Path.LABELS_ID, version = "1")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(
        @RequestHeader(Header.USER_ID) userId: String,
        @PathVariable(PathParam.LABEL_ID) id: Long
    ) = labelService.delete(userId, id)
}
