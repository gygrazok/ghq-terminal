package com.terredegliangeli.ghq.terminal.mqtt

import com.fasterxml.jackson.databind.ObjectMapper
import com.terredegliangeli.ghq.terminal.mqtt.handlers.AbstractMqttMessageHandler


class MockMqttClient(private val objectMapper: ObjectMapper):IMQTTClient {
    override fun <T> publish(topic: String, message: T) {
        println("Publishing message to topic $topic")
        println(objectMapper.writeValueAsString(message))
    }

    override fun subscribe(topic: String, handler: AbstractMqttMessageHandler<*>) {
        println("Subscribing to topic $topic")
    }
}
