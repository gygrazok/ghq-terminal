package com.terredegliangeli.ghq.terminal.shell

import com.terredegliangeli.ghq.terminal.configuration.TerminalSettings
import com.terredegliangeli.ghq.terminal.mqtt.dto.VideotelMessage
import com.terredegliangeli.ghq.terminal.service.CommunicationService
import com.terredegliangeli.ghq.terminal.utils.delayedMessage
import com.terredegliangeli.ghq.terminal.utils.printMessageLineByLine
import org.springframework.shell.component.flow.ComponentFlow
import org.springframework.shell.component.flow.ResultMode
import org.springframework.shell.standard.AbstractShellComponent
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import org.springframework.shell.standard.ShellOption


@ShellComponent
class CommunicationCommands(val settings: TerminalSettings, val componentFlowBuilder: ComponentFlow.Builder,
    val communicationService: CommunicationService
) :
    AbstractShellComponent() {

    @ShellMethod(key = ["VIDEOTEL TEXT MESSAGE"])
    fun videotelSend(@ShellOption(arity = 1, defaultValue = "") target: String) {
        if (settings.panicMode) {
            throw Exception("SIP: linea telefonica non disponibile.")
        }
        if (target == "") {
            throw Exception("SIP: inserire il numero dell'utenza Videotel dopo il comando VIDEOTEL TEXT MESSAGE")
        }
        val flow: ComponentFlow = componentFlowBuilder.clone().reset()
            .withStringInput("message")
            .name("Enter message:")
            .resultMode(ResultMode.ACCEPT)
            .and()
            .build()
        val result = flow.run()
        val message = result.context.get<String>("message")
        sendVideotelMessage(target, message)
    }

    @ShellMethod(key = ["VIDEOTEL READ MESSAGE"])
    fun videotelRead() {
        if (settings.panicMode) {
            throw Exception("SIP: linea telefonica non disponibile.")
        }
        if (communicationService.countUnreadMessages() == 0) {
            println("Nessun messaggio da leggere")
            return
        }
        println("Hai ${communicationService.countUnreadMessages()} messaggi non letti.")
        delayedMessage("Ricezione messaggio in corso", "fatto!", minDelay = 0.1, maxDelay = 0.5, amount = 8)
        val message = communicationService.readMessage()
        printMessageLineByLine(message.message)
    }

    private fun sendVideotelMessage(target: String, message: String) {
        val recipientName = when (target) {
            "4933069" -> {
                "Ufficio Paramedia"
            }
            "29406214", "29406215", "0229406214", "0229406215" -> {
                "Immobiliare Colombo"
            }
            "579705" -> {
                "Dott. Omeopata Lapo Bengodi "
            }
            "5552368" -> {
                "GHQ"
            }
            else -> {
                throw Exception("SIP: utenza Videotel non valida.")
            }
        }
        println("Invio messaggio a utenza Videotel $target: $message")
        communicationService.sendMessage(message, target, recipientName)

        if (recipientName == "Immobiliare Colombo") {
            communicationService.receiveMessage(VideotelMessage("RISPOSTA AUTOMATICA: Siamo spiacenti, dopo la morte di mia sorella Fabiana l'agenzia è chiusa a tempo indefinito. Cordiali saluti, Marco Colombo", "terminal", target, recipientName))
        }
    }
}