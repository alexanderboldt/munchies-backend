package com.alex.munchies

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
class MunchiesApplication

@Suppress("SpreadOperator") // vararg is here required
fun main(args: Array<String>) {
	runApplication<MunchiesApplication>(*args)
}
