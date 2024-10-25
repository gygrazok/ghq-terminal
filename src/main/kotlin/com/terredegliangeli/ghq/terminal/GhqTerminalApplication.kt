package com.terredegliangeli.ghq.terminal

import com.terredegliangeli.ghq.terminal.configuration.ApplicationProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties::class)
class GhqTerminalApplication

fun main(args: Array<String>) {
	runApplication<GhqTerminalApplication>(*args)
}
