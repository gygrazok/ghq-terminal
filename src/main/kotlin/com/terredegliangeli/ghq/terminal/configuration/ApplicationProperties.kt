package com.terredegliangeli.ghq.terminal.configuration

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "ghq")
class ApplicationProperties {
    lateinit var printerName: String

}