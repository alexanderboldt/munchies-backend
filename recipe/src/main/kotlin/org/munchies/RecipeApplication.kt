package org.munchies

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RecipeApplication

@Suppress("SpreadOperator") // vararg is here required
fun main(args: Array<String>) {
	runApplication<RecipeApplication>(*args)
}
