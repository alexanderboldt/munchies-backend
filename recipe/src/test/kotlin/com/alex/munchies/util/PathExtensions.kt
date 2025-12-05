package com.alex.munchies.util

import com.alex.munchies.Path

val Path.RECIPE_ID: String
    get() = "$RECIPE/$ID"

val Path.STEP_ID: String
    get() = "$RECIPE_STEP/$ID"