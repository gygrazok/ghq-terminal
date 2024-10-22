package com.terredegliangeli.ghq.terminal.configuration

import org.jline.utils.AttributedString
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.shell.jline.PromptProvider


@Configuration
class CustomPromptProvider {

    @Bean
    fun myPromptProvider(settings:TerminalSettings): PromptProvider? {

        return PromptProvider {
            if (settings.panicMode) {
                AttributedString("ghq:(offline)>")
            } else if (settings.scriptMode) {
                AttributedString(">")
            } else {
                AttributedString("ghq:>")
            }
        }
    }
}