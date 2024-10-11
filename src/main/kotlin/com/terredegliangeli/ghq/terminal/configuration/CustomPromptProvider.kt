package com.terredegliangeli.ghq.terminal.configuration

import org.jline.utils.AttributedString
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.shell.jline.PromptProvider


@Configuration
class CustomPromptProvider {

    var scriptMode = false

    @Bean
    fun myPromptProvider(): PromptProvider? {
        return PromptProvider {
            if (scriptMode) {
                AttributedString(">")
            } else {
                AttributedString("ghq:>")
            }
        }
    }
}