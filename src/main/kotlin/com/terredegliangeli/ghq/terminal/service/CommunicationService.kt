package com.terredegliangeli.ghq.terminal.service

import com.terredegliangeli.ghq.terminal.mqtt.IMQTTClient
import com.terredegliangeli.ghq.terminal.mqtt.dto.VideotelMessage
import org.springframework.stereotype.Service

@Service
class CommunicationService(val mqttClient: IMQTTClient) {

    val topic = "ghq/message"
    val unreadMessages = mutableListOf<VideotelMessage>()
    val readMessages = mutableListOf<VideotelMessage>()

    fun sendMessage(message: String, recipient:String, recipientName:String) {
        mqttClient.publish(topic, VideotelMessage(message, "terminal", recipient, recipientName))
    }

    fun receiveMessage(message: VideotelMessage) {
        if (message.from == "terminal") {
            return
        }
        unreadMessages.add(message)
        println("")
        println("Nuovo messaggio")
        println("Per leggerlo digitare VIDEOTEL READ MESSAGE")
    }

    fun countUnreadMessages(): Int {
        return unreadMessages.size
    }

    fun readMessage(): VideotelMessage {
        check(unreadMessages.isNotEmpty()) { "Nessun messaggio da leggere" }
        val message = unreadMessages.removeAt(0)
        readMessages.add(message)
        return message
    }
}