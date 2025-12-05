package com.alex.munchies.util

import com.alex.munchies.Path

val Path.STEP_ID: String
    get() = "$RECIPE_STEP/$ID"