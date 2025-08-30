package com.alex.munchies

import com.alex.munchies.domain.Label
import com.alex.munchies.domain.Recipe

object Fixtures {

    object User {
        const val USER_ID = "12345"
    }

    object Labels {
        object Domain {
            val vegetarian = Label(
                0,
                "",
                "Vegetarian",
                1747138632,
                1747138632
            )
            val vegan = Label(
                0,
                "",
                "Vegan",
                1747138632,
                1747138632
            )
        }
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
}
