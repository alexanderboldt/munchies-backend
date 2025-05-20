package com.alex.munchies.controller

import com.alex.munchies.domain.Label
import com.alex.munchies.domain.Recipe

object Fixtures {
    object Labels {
        val vegetarian = Label(0, "", name = "Vegetarian", 1747138632, 1747138632)
        val vegan = Label(0, "", name = "Vegan", 1747138632, 1747138632)
    }
    object Recipes {
        val pizza = Recipe(0, "", null,"Pizza", "lecker", 1000, 1747138632, 1747138632)
        val burger = Recipe(0, "", null, "Burger", "juicy", 2000, 1747138632, 1747138632)
    }
}