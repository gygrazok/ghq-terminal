package com.terredegliangeli.ghq.terminal.mqtt.handlers

import com.fasterxml.jackson.databind.ObjectMapper
import com.terredegliangeli.ghq.terminal.mqtt.dto.VideotelMessage
import com.terredegliangeli.ghq.terminal.service.CommunicationService
import org.springframework.stereotype.Component

@Component
class VideotelMessageHandler(private val communicationService: CommunicationService, override val objectMapper: ObjectMapper) :
    AbstractMqttMessageHandler<VideotelMessage>() {
    override val messageType = VideotelMessage::class.java
    override val topic: String = "ghq/message"
    override fun onMessageArrived(message: VideotelMessage) {
        communicationService.receiveMessage(message)
    }

}
