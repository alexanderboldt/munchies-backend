package com.alex.munchies.domain

data class Meals(
    val meals: List<Meal>
)

data class Meal(
    val idMeal: String = "",
    val strMeal: String = "",
    val strCategory: String = ""
)
