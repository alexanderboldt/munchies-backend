package org.munchies.util

import org.munchies.domain.LabelResponse
import org.munchies.domain.RecipeResponse
import org.munchies.domain.StepResponse
import io.restassured.common.mapper.TypeRef
import io.restassured.response.ResponseBodyExtractionOptions

fun ResponseBodyExtractionOptions.asLabels(): List<LabelResponse> = `as`(object : TypeRef<List<LabelResponse>>() {})
fun ResponseBodyExtractionOptions.asLabel(): LabelResponse = `as`(object : TypeRef<LabelResponse>() {})

fun ResponseBodyExtractionOptions.asRecipes(): List<RecipeResponse> = `as`(object : TypeRef<List<RecipeResponse>>() {})
fun ResponseBodyExtractionOptions.asRecipe(): RecipeResponse = `as`(object : TypeRef<RecipeResponse>() {})

fun ResponseBodyExtractionOptions.asSteps(): List<StepResponse> = `as`(object : TypeRef<List<StepResponse>>() {})
fun ResponseBodyExtractionOptions.asStep(): StepResponse = `as`(object : TypeRef<StepResponse>() {})
