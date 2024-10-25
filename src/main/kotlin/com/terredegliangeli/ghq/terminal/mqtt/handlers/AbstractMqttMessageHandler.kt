package com.terredegliangeli.ghq.terminal.mqtt.handlers

import com.fasterxml.jackson.databind.ObjectMapper
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish
import java.util.function.Consumer


abstract class AbstractMqttMessageHandler<T> : Consumer<Mqtt5Publish> {
    protected abstract val objectMapper: ObjectMapper
    protected abstract val messageType: Class<T>?

    abstract val topic: String

    override fun accept(message: Mqtt5Publish) {
        onMessageArrived(mapMessage(message))
    }

    protected abstract fun onMessageArrived(message: T)
    private fun mapMessage(message: Mqtt5Publish): T {
        return objectMapper.readValue(String(message.payloadAsBytes), messageType)
    }
}
