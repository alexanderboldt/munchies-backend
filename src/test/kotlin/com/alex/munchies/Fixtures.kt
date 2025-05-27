package com.alex.munchies

import com.alex.munchies.domain.Label
import com.alex.munchies.domain.Meal
import com.alex.munchies.domain.Recipe
import com.alex.munchies.repository.label.LabelEntity
import com.alex.munchies.repository.recipe.RecipeEntity

object Fixtures {

    object User {
        const val userId = "12345"
    }

    object Labels {
        object Domain {
            val vegetarian = Label(0, "", name = "Vegetarian", 1747138632, 1747138632)
            val vegan = Label(0, "", name = "Vegan", 1747138632, 1747138632)
        }
        object Entity {
            val vegetarian = LabelEntity(12, User.userId, "Vegetarian", 16438423489, 162131233)
        }
    }

    object Recipes {
        object Domain {
            val pizza = Recipe(0, "", null, "Pizza", "lecker", 1000, 1747138632, 1747138632)
            val burger = Recipe(0, "", null, "Burger", "juicy", 2000, 1747138632, 1747138632)
            val meal = Meal("231", "Spaghetti Bolognese", "Pasta")
        }
        object Entity {
            val pizza = RecipeEntity(12, User.userId, 10, "Pizza", "Italian Style", 1000, 16438423489, 162131233)
        }
    }
}