package com.alex.munchies.controller

import com.alex.munchies.repository.api.ApiModelLabel
import com.alex.munchies.repository.api.ApiModelRecipe

object Fixtures {
    object Labels {
        val vegetarian = ApiModelLabel(0, "", name = "Vegetarian", 1747138632, 1747138632)
        val vegan = ApiModelLabel(0, "", name = "Vegan", 1747138632, 1747138632)
    }
    object Recipes {
        val pizza = ApiModelRecipe(0, "", null,"Pizza", "lecker", 1000, 1747138632, 1747138632)
        val burger = ApiModelRecipe(0, "", null, "Burger", "juicy", 2000, 1747138632, 1747138632)
    }
}