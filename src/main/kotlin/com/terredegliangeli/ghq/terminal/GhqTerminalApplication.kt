package com.terredegliangeli.ghq.terminal

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GhqTerminalApplication

fun main(args: Array<String>) {
	runApplication<GhqTerminalApplication>(*args)
}
