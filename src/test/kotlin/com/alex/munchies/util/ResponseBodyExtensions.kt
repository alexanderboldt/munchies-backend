package com.alex.munchies.util

import com.alex.munchies.domain.Label
import com.alex.munchies.domain.Recipe
import io.restassured.common.mapper.TypeRef
import io.restassured.response.ResponseBodyExtractionOptions

fun ResponseBodyExtractionOptions.asLabels(): List<Label> = `as`(object : TypeRef<List<Label>>() {})
fun ResponseBodyExtractionOptions.asLabel(): Label = `as`(object : TypeRef<Label>() {})

fun ResponseBodyExtractionOptions.asRecipes(): List<Recipe> = `as`(object : TypeRef<List<Recipe>>() {})
fun ResponseBodyExtractionOptions.asRecipe(): Recipe = `as`(object : TypeRef<Recipe>() {})
