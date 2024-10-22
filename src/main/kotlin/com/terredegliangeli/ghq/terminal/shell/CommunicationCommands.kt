package com.terredegliangeli.ghq.terminal.shell

import com.terredegliangeli.ghq.terminal.configuration.TerminalSettings
import org.springframework.shell.component.flow.ComponentFlow
import org.springframework.shell.component.flow.ResultMode
import org.springframework.shell.standard.AbstractShellComponent
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import org.springframework.shell.standard.ShellOption


@ShellComponent
class CommunicationCommands(val settings: TerminalSettings, val componentFlowBuilder: ComponentFlow.Builder) :
    AbstractShellComponent() {

    @ShellMethod(key = ["VIDEOTEL TEXT MESSAGE"])
    fun videotel(@ShellOption(arity = 1, defaultValue = "") target: String) {
        if (settings.panicMode) {
            throw Exception("Phone line is not available.")
        }
        if (target == "") {
            println("Please provide a phone number to deliver the message to.")
            return
        }
        val flow: ComponentFlow = componentFlowBuilder.clone().reset()
            .withStringInput("message")
            .name("Enter message:")
            .resultMode(ResultMode.ACCEPT)
            .and()
            .build()
        val message = flow.run().context.get<String>("message")
        sendVideotelMessage(target, message)
    }

    private fun sendVideotelMessage(target: String, message: String) {
        println("Sending message to $target: $message")
    }




}

