package com.pingpong.ui.config;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

public class MqttConfig {

    private static final MqttConnectOptions options = new MqttConnectOptions();
    public static final String BUTTON_PRESS = "BUTTON_PRESS";
    public static final String BUTTON_LIGHT = "BUTTON_LIGHT";
    private static final MqttClient MQTT_CLIENT;

    static {
        try {

            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(1000000);
            options.setKeepAliveInterval(10);

            MQTT_CLIENT = new MqttClient("tcp://192.168.0.115:1883", "PING_PONG_UI");

        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    public static MqttClient getMqttClient() {
        if (!MQTT_CLIENT.isConnected()) {
            try {
                MQTT_CLIENT.connect();
            } catch (MqttException e) {
                throw new RuntimeException(e);
            }
        }
        return  MQTT_CLIENT;
    }

}
