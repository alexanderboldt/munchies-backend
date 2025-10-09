package com.alex.munchies

import com.alex.munchies.domain.LabelRequest
import com.alex.munchies.domain.Recipe
import java.io.File

object Fixtures {

    object User {
        const val USER_ID = "12345"
    }

    object Labels {
        val vegetarian = LabelRequest("Vegetarian")
        val vegan = LabelRequest("Vegan")
    }

    object Recipes {
        object Domain {
            val pizza = Recipe(
                0,
                "",
                null,
                "Pizza",
                "Italian Style",
                1000,
                null,
                1747138632,
                1747138632
            )
            val burger = Recipe(
                0,
                "",
                null,
                "Burger",
                "juicy",
                2000,
                null,
                1747138632,
                1747138632
            )
        }
    }

    val image: File = File.createTempFile("image", ".jpg").apply {
        writeText("Image Content")
        deleteOnExit()
    }
}
