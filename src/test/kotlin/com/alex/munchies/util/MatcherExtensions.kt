package com.alex.munchies.util

import com.alex.munchies.Fixtures
import com.alex.munchies.domain.LabelRequest
import com.alex.munchies.domain.LabelResponse
import com.alex.munchies.domain.RecipeRequest
import com.alex.munchies.domain.RecipeResponse
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.shouldBe

// region label

infix fun List<LabelResponse>.shouldBeLabels(expected: List<LabelRequest>) {
    zip(expected).forEach { (labelActual, labelExpected) ->
        labelActual shouldBeLabel labelExpected
    }
}

infix fun LabelResponse.shouldBeLabel(expected: LabelRequest) {
    id shouldBeGreaterThan 0
    userId shouldBe Fixtures.User.USER_ID
    name shouldBe expected.name
    createdAt shouldBeGreaterThan 0
    updatedAt shouldBeGreaterThan 0
}

// endregion

// region recipe

infix fun List<RecipeResponse>.shouldBeRecipes(expected: List<RecipeRequest>) {
    zip(expected).forEach { (recipeActual, recipeExpected) ->
        recipeActual shouldBeRecipe recipeExpected
    }
}

infix fun RecipeResponse.shouldBeRecipe(expected: RecipeRequest) {
    id shouldBeGreaterThan 0
    userId shouldBe Fixtures.User.USER_ID
    title shouldBe expected.title
    description shouldBe expected.description
    duration shouldBe expected.duration
    createdAt shouldBeGreaterThan 0
    updatedAt shouldBeGreaterThan 0
}

// endregion
