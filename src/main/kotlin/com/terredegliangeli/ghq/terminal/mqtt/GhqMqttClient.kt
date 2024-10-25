package com.terredegliangeli.ghq.terminal.mqtt

import com.fasterxml.jackson.databind.ObjectMapper
import com.hivemq.client.mqtt.datatypes.MqttQos
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish
import com.terredegliangeli.ghq.terminal.mqtt.handlers.AbstractMqttMessageHandler


class GhqMqttClient(private val mqttClient: Mqtt5Client, private val objectMapper: ObjectMapper):IMQTTClient {
    override fun <T> publish(topic: String, message: T) {
        val mqttMessage: Mqtt5Publish = Mqtt5Publish.builder()
            .topic(topic)
            .qos(MqttQos.EXACTLY_ONCE)
            .payload(objectMapper.writeValueAsBytes(message))
            .build()
        mqttClient.toBlocking().publish(mqttMessage)

    }

    override fun subscribe(topic: String, handler: AbstractMqttMessageHandler<*>) {
        mqttClient.toAsync().subscribeWith()
            .topicFilter(topic)
            .callback(handler)
            .send()
    }
}
