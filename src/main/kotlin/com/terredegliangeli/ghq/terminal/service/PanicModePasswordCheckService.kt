package com.terredegliangeli.ghq.terminal.service

import com.terredegliangeli.ghq.terminal.configuration.TerminalSettings
import org.springframework.stereotype.Service

@Service
class PanicModePasswordCheckService(private val terminalSettings: TerminalSettings) {
    fun checkPassword(s: String): String {
        var out:String
        when {
            isEgonPassword(s) -> {
                terminalSettings.egonPassword = true
                out = "\u001B[32mPassword utente SPENGLER ... OK!\u001B[0m"
            }
            isRayPassword(s) -> {
                terminalSettings.rayPassword = true
                out = "\u001B[32mPassword utente STANTZ... OK!\u001B[0m"
            }
            isPeterPassword(s) -> {
                terminalSettings.peterPassword = true
                out = "\u001B[32mPassword utente VENKMAN... OK!\u001B[0m"
            }
            else -> {
                return "\u001B[31mPassword non valida\u001B[0m"
            }
        }

        if (terminalSettings.egonPassword && terminalSettings.rayPassword && terminalSettings.peterPassword) {
            terminalSettings.panicMode = false
            out += "\n\u001B[32mCollegamento remoto ripristinato!\u001B[0m"
        }

        return out
    }


    private fun isEgonPassword(password: String): Boolean {
        return password == "2125"
    }

    private fun isRayPassword(password: String): Boolean {
        return password == "53"
    }

    private fun isPeterPassword(password: String): Boolean {
        return password.uppercase() == "WEEKEND"
    }

}