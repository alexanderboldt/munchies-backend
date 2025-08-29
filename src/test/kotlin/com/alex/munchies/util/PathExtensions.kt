package com.alex.munchies.util

val Path.LABEL_ID: String
    get() = "$LABEL/$ID"

val Path.RECIPE_ID: String
    get() = "$RECIPE/$ID"