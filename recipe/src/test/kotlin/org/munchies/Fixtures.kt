package org.munchies

import org.munchies.domain.LabelRequest
import org.munchies.domain.RecipeRequest
import org.munchies.domain.StepRequest
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
        val pizza = RecipeRequest(
            null,
            "Pizza",
            "Italian Style",
            1000
        )

        val burger = RecipeRequest(
            null,
            "Burger",
            "juicy",
            2000
        )
    }

    object Steps {
        val dough = StepRequest(
            1,
            "Create the dough",
            "Mix flour, salt and water."
        )

        val sauce = StepRequest(
            1,
            "Create the sauce",
            "Mix tomato, salt and olive oil."
        )
    }

    val image: File = File.createTempFile("filename", ".jpg").apply {
        writeText("Image Content")
        deleteOnExit()
    }
}
