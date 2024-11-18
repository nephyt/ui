package com.pingpong.ui.thread;

import com.pingpong.ui.services.MqttListener;

public class CheckMqttConnection extends Thread {


    @Override
    public void run() {

        while (true) {

            if (!MqttListener.getMqttClient().isConnected()) {
                MqttListener.getMqttClient();
            }
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
