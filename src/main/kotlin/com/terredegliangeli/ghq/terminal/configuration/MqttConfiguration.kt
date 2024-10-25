package com.terredegliangeli.ghq.terminal.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.hivemq.client.internal.mqtt.datatypes.MqttUtf8StringImpl
import com.hivemq.client.internal.mqtt.lifecycle.MqttClientAutoReconnectImpl
import com.hivemq.client.internal.mqtt.message.auth.MqttSimpleAuth
import com.hivemq.client.internal.mqtt.util.MqttChecks
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client
import com.hivemq.client.mqtt.mqtt5.Mqtt5ClientBuilder
import com.terredegliangeli.ghq.terminal.mqtt.IMQTTClient
import com.terredegliangeli.ghq.terminal.mqtt.GhqMqttClient
import com.terredegliangeli.ghq.terminal.mqtt.MockMqttClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.util.StringUtils
import java.nio.ByteBuffer
import java.util.*

@Configuration
class MqttConfiguration {
    @Value("\${mqtt.client.id:ghq-terminal}")
    private var clientId: String = "ghq-terminal"

    @Value("\${mqtt.url}")
    private var url: String = ""

    @Value("\${mqtt.port:1883}")
    private var port: String = "1883"

    @Value("\${mqtt.ssl:false}")
    private var useSSL = false

    @Value("\${mqtt.username:}")
    private var username: String? = null

    @Value("\${mqtt.password:}")
    private var password: String? = null

    @Value("\${mqtt.enabled}")
    private var enabled: Boolean = true


    @Bean
    @ConditionalOnProperty(prefix = "mqtt", name = ["enabled"], havingValue = "true")
    fun getGhqMqttClient(client: Mqtt5Client, objectMapper: ObjectMapper): GhqMqttClient {
        return GhqMqttClient(client, objectMapper)
    }

    @Bean
    @ConditionalOnMissingBean(IMQTTClient::class)
    fun getMockMqttClient(objectMapper: ObjectMapper): MockMqttClient {
        return MockMqttClient(objectMapper)
    }

    @Bean
    @ConditionalOnProperty(prefix = "mqtt", name = ["enabled"], havingValue = "true")
    fun buildMqttClient(): Mqtt5Client {
        var builder: Mqtt5ClientBuilder = Mqtt5Client.builder()
            .identifier(clientId)
            .serverHost(url)
            .serverPort(port.toInt())
            .automaticReconnect(MqttClientAutoReconnectImpl.DEFAULT)

        val mqttUsername: MqttUtf8StringImpl? = if (StringUtils.hasText(username)) null
                                                else MqttUtf8StringImpl.of(username, "Username")
        val mqttPassword: ByteBuffer? = if (StringUtils.hasText(password)) null
                                        else MqttChecks.binaryData(password!!.toByteArray(), "Password")
        if (username != null || password != null) {
            builder = builder.simpleAuth(MqttSimpleAuth(mqttUsername, mqttPassword))
        }
        if (useSSL) {
            builder = builder.sslWithDefaultConfig()
        }
        val client: Mqtt5Client = builder.build()
        client.toBlocking().connectWith().cleanStart(false).send()
        return client
    }

}
