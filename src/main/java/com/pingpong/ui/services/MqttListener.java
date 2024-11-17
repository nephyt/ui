package com.pingpong.ui.services;

import com.pingpong.basicclass.enumeration.TeamEnum;
import com.pingpong.ui.config.MessageParser;
import com.pingpong.ui.config.State;
import com.pingpong.ui.thread.CheckMqttConnection;
import com.pingpong.ui.web.controller.GameController;
import com.pingpong.ui.web.controller.GameSettingController;
import com.pingpong.ui.web.controller.WinnerScreenController;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Service;
@Service
public class MqttListener {

    private static final MqttConnectOptions options = new MqttConnectOptions();
    public static final String BUTTON_PRESS = "BUTTON_PRESS";
    public static final String BUTTON_LIGHT = "BUTTON_LIGHT";
    private static final MqttClient MQTT_CLIENT;

    private static GameController gameController;
    private static WinnerScreenController winnerScreenController;
    private static GameSettingController gameSettingController;
    static private State state = State.NONE;

    CheckMqttConnection checkMqttConnection;

    static {
        try {

            options.setCleanSession(true);
            options.setConnectionTimeout(15);
            options.setKeepAliveInterval(60000);

            MQTT_CLIENT = new MqttClient("tcp://localhost:1883", "PING_PONG_UI");
//            MQTT_CLIENT = new MqttClient("tcp://192.168.0.115:1883", "PING_PONG_UI");

        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    MqttListener(GameSettingController gameSettingController, GameController gameController, WinnerScreenController winnerScreenController) {
        this.gameSettingController = gameSettingController;
        this.gameController = gameController;
        this.winnerScreenController = winnerScreenController;

        checkMqttConnection = new CheckMqttConnection();
        checkMqttConnection.start();
    }

    public static MqttClient getMqttClient() {
        if (!MQTT_CLIENT.isConnected()) {
            try {
                MQTT_CLIENT.connect(options);

                try {
                    MQTT_CLIENT.subscribe(MqttListener.BUTTON_PRESS, MqttListener::manageMessage);
                } catch (MqttException e) {
                    System.out.println("Error lors du subscribe au topic " + MqttListener.BUTTON_PRESS + " : " + e.getMessage());
                    System.out.println("ERRO : ");
                    e.printStackTrace(System.out);
                    throw new RuntimeException(e);
                }
            } catch (MqttException e) {
                System.out.println("Error connexion a MQTT dans getMqttClient()" + e.getMessage());
                e.printStackTrace();
            }
        }
        return  MQTT_CLIENT;
    }

    private static void manageMessage(String topic, MqttMessage msg) {
        MessageParser message = new MessageParser(msg.getPayload());

        // ... payload handling omitted
        System.out.println("MESSAGE RECU " + topic + " " + message);

        switch (state) {
            case GAME ->  manageGame(message);
            case SETTING -> manageSetting(message);
            case WINNER -> manageWinner(message);
            case NONE -> System.out.println("none");
        }
    }

    private static void manageGame(MessageParser message) {
        System.out.println("manageGame : " + message.toString());

        if (message.getButtonLong()) {
            switch (message.getButtonPress()) {
                case "0" -> gameController.pauseGame();
                case "1" -> gameController.undo();
                case "2" -> gameController.pauseGame();
                case "3" -> gameController.undo();
            }
        } else {
            switch (message.getButtonPress()) {
                case "0" -> gameController.teamScored(TeamEnum.TEAM_A);
                case "1" -> gameController.teamScored(TeamEnum.TEAM_A);
                case "2" -> gameController.teamScored(TeamEnum.TEAM_B);
                case "3" -> gameController.teamScored(TeamEnum.TEAM_B);
            }
        }
    }

    private static void manageSetting(MessageParser message) {
        System.out.println("manageSetting : " + message.toString());

        if (message.getButtonLong()) {
            switch (message.getButtonPress()) {
                case "0" -> gameSettingController.startGame();
                case "1" -> gameSettingController.nextScoreMax();
                case "2" -> gameSettingController.startGame();
                case "3" -> gameSettingController.changeServerTeam();
            }
        } else {
            switch (message.getButtonPress()) {
                case "0" -> gameSettingController.nextPlayer1TeamA();
                case "1" -> gameSettingController.nextPlayer2TeamA();
                case "2" -> gameSettingController.nextPlayer1TeamB();
                case "3" -> gameSettingController.nextPlayer2TeamB();
            }
        }
    }
    private static void manageWinner(MessageParser message) {
        System.out.println("manageWinner : " + message.toString());

        if (message.getButtonLong()) {
            winnerScreenController.changeServerTeam();
        } else {
            switch (message.getButtonPress()) {
                case "0" -> winnerScreenController.rematch();
                case "1" -> winnerScreenController.newGame();
                case "2" -> winnerScreenController.rematch();
                case "3" -> winnerScreenController.newGame();
            }
        }
    }

    public static void setStateGameScore() {
        state = State.GAME;
    }

    public static void setStateWinnerScreen() {
        state = State.WINNER;
    }

    public static void setStateGameSetting() {
        state = State.SETTING;
    }

    public static void setStateNone() {
        state = State.NONE;
    }
}
