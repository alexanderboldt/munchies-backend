package com.alex.munchies.util

import com.alex.munchies.Path

val Path.LABEL_ID: String
    get() = "$LABEL/$ID"

val Path.RECIPE_ID: String
    get() = "$RECIPE/$ID"

val Path.STEP_ID: String
    get() = "$RECIPE_STEP/$ID"