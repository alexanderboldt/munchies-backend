package com.alex.munchies.exception

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class LabelNotFoundException : ResponseStatusException(HttpStatus.BAD_REQUEST, "Label not found with the given id!")
class RecipeNotFoundException : ResponseStatusException(HttpStatus.BAD_REQUEST, "Recipe not found with the given id!")
