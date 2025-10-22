package com.alex.munchies.util

import com.alex.munchies.Fixtures
import com.alex.munchies.domain.LabelRequest
import com.alex.munchies.domain.LabelResponse
import com.alex.munchies.domain.RecipeRequest
import com.alex.munchies.domain.RecipeResponse
import com.alex.munchies.domain.StepRequest
import com.alex.munchies.domain.StepResponse
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
    labelId shouldBe expected.labelId
    title shouldBe expected.title
    description shouldBe expected.description
    duration shouldBe expected.duration
    createdAt shouldBeGreaterThan 0
    updatedAt shouldBeGreaterThan 0
}

// endregion

// region step

infix fun List<StepResponse>.shouldBeSteps(expected: List<StepRequest>) {
    zip(expected).forEach { (stepActual, stepExpected) ->
        stepActual shouldBeStep stepExpected
    }
}

infix fun StepResponse.shouldBeStep(expected: StepRequest) {
    id shouldBeGreaterThan 0
    userId shouldBe Fixtures.User.USER_ID
    recipeId shouldBeGreaterThan 0
    number shouldBe expected.number
    title shouldBe expected.title
    description shouldBe expected.description
    createdAt shouldBeGreaterThan 0
    updatedAt shouldBeGreaterThan 0
}

// endregion
