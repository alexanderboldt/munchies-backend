package org.munchies

/**
 * Contains the paths of the resources.
 */
object Path {
    // auth
    const val AUTH_LOGIN = "auth/login"

    // keycloak
    const val KEYCLOAK_REALM = "/realm"

    // labels
    const val LABELS = "labels"
    const val LABELS_ID = "labels/{label_id}"

    // recipes
    const val RECIPES = "recipes"
    const val RECIPES_ID = "recipes/{recipe_id}"

    // recipes - images
    const val RECIPES_IMAGES = "recipes/{recipe_id}/images"

    // recipes - steps
    const val RECIPES_STEPS = "recipes/{recipe_id}/steps"
    const val RECIPES_STEPS_ID = "recipes/{recipe_id}/steps/{step_id}"

    // files
    const val FILES_BUCKET = "files/{bucket}"
    const val FILES_BUCKET_FILENAME = "files/{bucket}/{filename}"
}

/**
 * Contains the corresponding parameters from the path resources.
 */
object PathParam {
    const val LABEL_ID = "label_id"
    const val RECIPE_ID = "recipe_id"
    const val STEP_ID = "step_id"
    const val BUCKET = "bucket"
    const val FILENAME = "filename"
}
