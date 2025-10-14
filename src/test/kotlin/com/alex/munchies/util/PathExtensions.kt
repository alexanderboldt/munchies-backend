package com.alex.munchies.util

val Path.LABEL_ID: String
    get() = "$LABEL/$ID"

val Path.RECIPE_ID: String
    get() = "$RECIPE/$ID"

val Path.STEP_ID: String
    get() = "$STEP/$ID"