package com.alex.munchies.util

import com.alex.munchies.Fixtures
import com.alex.munchies.domain.Label
import com.alex.munchies.domain.Recipe
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.apache.http.HttpStatus

fun postLabel(label: Label): Label {
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

fun postRecipe(recipe: Recipe): Recipe {
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

fun uploadRecipeImage(id: Long): Recipe {
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
