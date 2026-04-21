package org.munchies.controller

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
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
    suspend fun create(
        @NotBlank @RequestHeader(Header.USER_ID) userId: String,
        @Valid @RequestBody label: LabelRequest
    ) = labelService.create(userId, label)

    // read

    @GetMapping(Path.LABELS, version = "1")
    suspend fun readAll(@NotBlank @RequestHeader(Header.USER_ID) userId: String) = labelService.readAll(userId)

    @GetMapping(Path.LABELS_ID, version = "1")
    suspend fun read(
        @NotBlank @RequestHeader(Header.USER_ID) userId: String,
        @Positive @PathVariable(PathParam.LABEL_ID) id: Long
    ) = labelService.read(userId, id)

    // update

    @PutMapping(Path.LABELS_ID, version = "1")
    suspend fun update(
        @NotBlank @RequestHeader(Header.USER_ID) userId: String,
        @Positive @PathVariable(PathParam.LABEL_ID) id: Long,
        @Valid @RequestBody labelNew: LabelRequest
    ) = labelService.update(userId, id, labelNew)

    // delete

    @DeleteMapping(Path.LABELS_ID, version = "1")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    suspend fun delete(
        @NotBlank @RequestHeader(Header.USER_ID) userId: String,
        @Positive @PathVariable(PathParam.LABEL_ID) id: Long
    ) = labelService.delete(userId, id)
}
