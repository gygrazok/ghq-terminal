package com.terredegliangeli.ghq.terminal.configuration

import com.terredegliangeli.ghq.terminal.service.PanicModePasswordCheckService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.shell.result.CommandNotFoundMessageProvider




@Configuration
class TerminalConfiguration {
    @Bean
    fun customExceptionResolver(): CustomExceptionResolver {
        return CustomExceptionResolver()
    }

    @Bean
    fun commandNotFoundProvider(settings: TerminalSettings, passwordCheckService: PanicModePasswordCheckService): CommandNotFoundMessageProvider {
        return CustomCommandNotFoundMessageProvider(settings, passwordCheckService)
    }
}