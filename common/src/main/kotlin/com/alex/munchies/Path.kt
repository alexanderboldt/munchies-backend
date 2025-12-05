package com.alex.munchies

/**
 * Contains the paths of the resources.
 */
object Path {
    // labels
    const val LABELS = "labels"
    const val LABELS_ID = "labels/{label_id}"

    const val RECIPE = "recipes"
    const val RECIPE_IMAGE = "recipes/{id}/images"
    const val RECIPE_STEP = "recipes/{recipeId}/steps"
    const val ID = "{id}"
}

/**
 * Contains the corresponding parameters from the path resources.
 */
object PathParam {
    const val LABEL_ID = "label_id"
}
