package com.terredegliangeli.ghq.terminal.mqtt.handlers

import com.fasterxml.jackson.databind.ObjectMapper
import com.terredegliangeli.ghq.terminal.mqtt.dto.PrintMessage
import com.terredegliangeli.ghq.terminal.service.FilePrintService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class PrintMessageHandler(private val filePrintService: FilePrintService, override val objectMapper: ObjectMapper) :
    AbstractMqttMessageHandler<PrintMessage>() {
    override val messageType = PrintMessage::class.java
    override val topic: String = "ghq/print"
    val logger = LoggerFactory.getLogger(FilePrintService::class.java)
    override fun onMessageArrived(message: PrintMessage) {
        try {
            filePrintService.printFile(message.file)
        } catch (e: Exception) {
            logger.error("Error printing file", e)
        }
        logger.info("Operation completed")
    }

}
