package com.alex.munchies.util

import com.alex.munchies.domain.LabelResponse
import com.alex.munchies.domain.Recipe
import io.restassured.common.mapper.TypeRef
import io.restassured.response.ResponseBodyExtractionOptions

fun ResponseBodyExtractionOptions.asLabels(): List<LabelResponse> = `as`(object : TypeRef<List<LabelResponse>>() {})
fun ResponseBodyExtractionOptions.asLabel(): LabelResponse = `as`(object : TypeRef<LabelResponse>() {})

fun ResponseBodyExtractionOptions.asRecipes(): List<Recipe> = `as`(object : TypeRef<List<Recipe>>() {})
fun ResponseBodyExtractionOptions.asRecipe(): Recipe = `as`(object : TypeRef<Recipe>() {})
