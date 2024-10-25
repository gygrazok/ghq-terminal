package com.terredegliangeli.ghq.terminal.mqtt

import com.terredegliangeli.ghq.terminal.mqtt.handlers.AbstractMqttMessageHandler


interface IMQTTClient {
    fun <T> publish(topic: String, message: T)

    fun subscribe(topic: String, handler: AbstractMqttMessageHandler<*>)
}
