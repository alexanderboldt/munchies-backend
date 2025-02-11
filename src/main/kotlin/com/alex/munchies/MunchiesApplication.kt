package com.alex.munchies

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MunchiesApplication

fun main(args: Array<String>) {
	runApplication<MunchiesApplication>(*args)
}
