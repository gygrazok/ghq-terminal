package com.terredegliangeli.ghq.terminal.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration


@Configuration
class TerminalSettings {
    var scriptMode = false

    @Value("\${panic:false}")
    var panicMode:Boolean = false

    var egonPassword = false
    var rayPassword = false
    var peterPassword = false
}