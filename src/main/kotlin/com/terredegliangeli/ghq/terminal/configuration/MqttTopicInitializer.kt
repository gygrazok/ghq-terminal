package com.terredegliangeli.ghq.terminal.configuration

import com.terredegliangeli.ghq.terminal.mqtt.GhqMqttClient
import com.terredegliangeli.ghq.terminal.mqtt.handlers.AbstractMqttMessageHandler
import org.springframework.beans.factory.InitializingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component

@Component
@ConditionalOnProperty(prefix = "mqtt", name = ["enabled"], havingValue = "true")
class MqttTopicInitializer(
    private val mqtt: GhqMqttClient,
    private val handlers: List<AbstractMqttMessageHandler<*>>
) : InitializingBean {

    override fun afterPropertiesSet() {
        handlers.forEach {
            mqtt.subscribe(it.topic, it)
        }
        println("Connection with GHQ correctly established")
    }

}
