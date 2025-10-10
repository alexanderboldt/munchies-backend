package com.alex.munchies.util

import com.alex.munchies.domain.LabelResponse
import com.alex.munchies.domain.RecipeResponse
import io.restassured.common.mapper.TypeRef
import io.restassured.response.ResponseBodyExtractionOptions

fun ResponseBodyExtractionOptions.asLabels(): List<LabelResponse> = `as`(object : TypeRef<List<LabelResponse>>() {})
fun ResponseBodyExtractionOptions.asLabel(): LabelResponse = `as`(object : TypeRef<LabelResponse>() {})

fun ResponseBodyExtractionOptions.asRecipes(): List<RecipeResponse> = `as`(object : TypeRef<List<RecipeResponse>>() {})
fun ResponseBodyExtractionOptions.asRecipe(): RecipeResponse = `as`(object : TypeRef<RecipeResponse>() {})
