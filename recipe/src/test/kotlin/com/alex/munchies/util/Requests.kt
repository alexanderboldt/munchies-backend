package com.alex.munchies.util

import com.alex.munchies.Fixtures
import com.alex.munchies.Header
import com.alex.munchies.Path
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

fun createLabel(label: LabelRequest): LabelResponse {
    return Given {
        body(label)
        header(Header.USER_ID, Fixtures.User.USER_ID)
    } When {
        post(Path.LABEL)
    } Then {
        statusCode(HttpStatus.SC_CREATED)
    } Extract {
        asLabel()
    }
}

fun createRecipe(recipe: RecipeRequest): RecipeResponse {
    return Given {
        body(recipe)
        header(Header.USER_ID, Fixtures.User.USER_ID)
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
        header(Header.USER_ID, Fixtures.User.USER_ID)
        contentType(ContentType.MULTIPART)
    } When {
        post(Path.RECIPE_IMAGE, id)
    } Then {
        statusCode(HttpStatus.SC_OK)
    } Extract {
        asRecipe()
    }
}

fun createStep(recipeId: Long, step: StepRequest): StepResponse {
    return Given {
        body(step)
        header(Header.USER_ID, Fixtures.User.USER_ID)
    } When {
        post(Path.STEP, recipeId)
    } Then {
        statusCode(HttpStatus.SC_CREATED)
    } Extract {
        asStep()
    }
}
