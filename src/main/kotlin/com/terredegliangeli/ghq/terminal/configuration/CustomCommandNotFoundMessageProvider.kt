package com.terredegliangeli.ghq.terminal.configuration

import com.terredegliangeli.ghq.terminal.service.PanicModePasswordCheckService
import org.jline.utils.AttributedString
import org.jline.utils.AttributedStyle
import org.springframework.shell.result.CommandNotFoundMessageProvider

class CustomCommandNotFoundMessageProvider(private val settings: TerminalSettings,
    private val passwordCheckService: PanicModePasswordCheckService) : CommandNotFoundMessageProvider {
    override fun apply(context: CommandNotFoundMessageProvider.ProviderContext): String {
        return if (settings.panicMode) {
            return passwordCheckService.checkPassword(context.commands()[0])
        } else {
            AttributedString(context.error().message, AttributedStyle.DEFAULT.foreground(AttributedStyle.RED)
            ).toAnsi()
        }
    }
}