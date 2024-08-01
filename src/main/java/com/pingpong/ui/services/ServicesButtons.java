package com.pingpong.ui.services;

import com.pingpong.basicclass.enumeration.TeamEnum;
import com.pingpong.basicclass.game.Game;
import com.pingpong.ui.config.MqttConfig;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class ServicesButtons  {

    static private ServicesButtons instance = null;

    //private ButtonsThreads buttonsThread;

    private ServicesButtons() {
//        buttonsThread = new ButtonsThreads();
//        buttonsThread.start();
    }

    public static ServicesButtons getInstance() {
        if (instance == null) {
            instance = new ServicesButtons();
        }
        return instance;
    }

    private static void publishToMqtt(MqttMessage mqttMessage) {
        try {
            MqttConfig.getMqttClient().publish(MqttConfig.BUTTON_LIGHT, mqttMessage);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    public void standBy() {
        MqttMessage mqttMessage = createMqttMessage("STANDBY");
        publishToMqtt(mqttMessage);
    }


    public void startMatch(TeamEnum serverStat) {
        String server = ("A".equals(serverStat.getCode()) ? "0" : "2");

        MqttMessage mqttMessage = createMqttMessage("STARTMATCH_" +server);
        publishToMqtt(mqttMessage);
    }

    public void playerSelection() {
        MqttMessage mqttMessage = createMqttMessage("PLAYERSELECTION");
        publishToMqtt(mqttMessage);
    }

    public void startModeWinner(Game game) {
        TeamEnum teamWinner = TeamEnum.TEAM_A;
        if (game.getTeamB().getId().equals(game.getTeamWinnerId())) {
            teamWinner = TeamEnum.TEAM_B;
        }
        MqttMessage mqttMessage = createMqttMessage("WINNER_" + teamWinner.getCode());
        publishToMqtt(mqttMessage);
    }

    public void startServerModeButton(String serverState) {
        MqttMessage mqttMessage = createMqttMessage("SERVER_" + serverState); //serverState = 0 to 3
        publishToMqtt(mqttMessage);
    }

    public void pauseGame() {
        MqttMessage mqttMessage = createMqttMessage("PAUSE");
        publishToMqtt(mqttMessage);
    }

    private static MqttMessage createMqttMessage(String msg) {
        byte[] payload = msg.getBytes();
        return new MqttMessage(payload);
    }
}
