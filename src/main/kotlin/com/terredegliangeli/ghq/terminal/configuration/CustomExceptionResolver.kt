package com.terredegliangeli.ghq.terminal.configuration

import org.springframework.shell.command.CommandExceptionResolver
import org.springframework.shell.command.CommandHandlingResult

class CustomExceptionResolver : CommandExceptionResolver {
    override fun resolve(ex: Exception?): CommandHandlingResult {
        return CommandHandlingResult.of("\u001B[31mError: ${ex?.message}\u001B[0m\n")
    }
}