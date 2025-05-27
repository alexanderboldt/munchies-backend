package com.alex.munchies.controller

object Routes {
    object Label {
        const val MAIN = "/api/v1/labels"
        const val DETAIL = "$MAIN/{id}"
    }
    object Recipe {
        const val MAIN = "/api/v1/recipes"
        const val DETAIL = "$MAIN/{id}"
    }
}