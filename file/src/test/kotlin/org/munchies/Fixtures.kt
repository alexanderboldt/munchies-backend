package org.munchies

import java.io.File

object Fixtures {

    val image: File = File.createTempFile("filename", ".jpg").apply {
        writeText("Image Content")
        deleteOnExit()
    }
}
