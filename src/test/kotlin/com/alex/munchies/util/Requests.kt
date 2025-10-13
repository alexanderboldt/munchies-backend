package com.alex.munchies.util

import com.alex.munchies.Fixtures
import com.alex.munchies.domain.LabelRequest
import com.alex.munchies.domain.LabelResponse
import com.alex.munchies.domain.RecipeRequest
import com.alex.munchies.domain.RecipeResponse
import com.alex.munchies.domain.StepRequest
import com.alex.munchies.domain.StepResponse
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.apache.http.HttpStatus

fun postLabel(label: LabelRequest): LabelResponse {
    return Given {
        body(label)
    } When {
        post(Path.LABEL)
    } Then {
        statusCode(HttpStatus.SC_CREATED)
    } Extract {
        asLabel()
    }
}

fun postRecipe(recipe: RecipeRequest): RecipeResponse {
    return Given {
        body(recipe)
    } When {
        post(Path.RECIPE)
    } Then {
        statusCode(HttpStatus.SC_CREATED)
    } Extract {
        asRecipe()
    }
}

fun uploadRecipeImage(id: Long): RecipeResponse {
    return Given {
        multiPart("image", Fixtures.image)
        contentType(ContentType.MULTIPART)
    } When {
        post(Path.RECIPE_IMAGE, id)
    } Then {
        statusCode(HttpStatus.SC_OK)
    } Extract {
        asRecipe()
    }
}

fun postStep(recipeId: Long, step: StepRequest): StepResponse {
    return Given {
        body(step)
    } When {
        post(Path.STEP, recipeId)
    } Then {
        statusCode(HttpStatus.SC_CREATED)
    } Extract {
        asStep()
    }
}
