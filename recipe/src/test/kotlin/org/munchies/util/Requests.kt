package org.munchies.util

import org.munchies.Fixtures
import org.munchies.Header
import org.munchies.MultipartParam
import org.munchies.Path
import org.munchies.domain.LabelRequest
import org.munchies.domain.LabelResponse
import org.munchies.domain.RecipeRequest
import org.munchies.domain.RecipeResponse
import org.munchies.domain.StepRequest
import org.munchies.domain.StepResponse
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
        post(Path.LABELS)
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
        post(Path.RECIPES)
    } Then {
        statusCode(HttpStatus.SC_CREATED)
    } Extract {
        asRecipe()
    }
}

fun uploadRecipeImage(id: Long): RecipeResponse {
    return Given {
        multiPart(MultipartParam.IMAGE, Fixtures.image)
        header(Header.USER_ID, Fixtures.User.USER_ID)
        contentType(ContentType.MULTIPART)
    } When {
        post(Path.RECIPES_IMAGES, id)
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
        post(Path.RECIPES_STEPS, recipeId)
    } Then {
        statusCode(HttpStatus.SC_CREATED)
    } Extract {
        asStep()
    }
}
