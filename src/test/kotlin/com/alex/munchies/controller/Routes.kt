package com.alex.munchies.controller

object Routes {
    object Label {
        const val main = "/api/v1/labels"
        const val detail = "$main/{id}"
    }
    object Recipe {
        const val main = "/api/v1/recipes"
        const val detail = "$main/{id}"
    }
}