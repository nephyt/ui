package com.pingpong.ui.services;

import com.pingpong.basicclass.enumeration.TeamEnum;
import com.pingpong.ui.config.MessageParser;
import com.pingpong.ui.config.MqttConfig;
import com.pingpong.ui.config.State;
import com.pingpong.ui.web.controller.GameController;
import com.pingpong.ui.web.controller.GameSettingController;
import com.pingpong.ui.web.controller.WinnerScreenController;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Service;
@Service
public class MqttListener {

    private GameController gameController;
    private WinnerScreenController winnerScreenController;
    private GameSettingController gameSettingController;
    static private State state;


    MqttClient mqttClient;
    MqttListener(GameSettingController gameSettingController, GameController gameController, WinnerScreenController winnerScreenController) {
        this.gameSettingController = gameSettingController;
        this.gameController = gameController;
        this.winnerScreenController = winnerScreenController;
        mqttClient = MqttConfig.getMqttClient();
        try {
            mqttClient.subscribe(MqttConfig.BUTTON_PRESS, this::manageMessage);
        } catch (MqttException e) {
            throw new RuntimeException(e);
        }
    }

    private void manageMessage(String topic, MqttMessage msg) {
        MessageParser message = new MessageParser(msg.getPayload());

        // ... payload handling omitted
        System.out.println("MESSAGE RECU topic - score: " + topic + " " + message);


        switch (state) {
            case GAME ->  manageGame(message);
            case SETTING -> manageSetting(message);
            case WINNER -> manageWinner(message);
        }
    }

    private void manageGame(MessageParser message) {
        System.out.println("Changer le score");

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

    private void manageSetting(MessageParser message) {
        System.out.println("changer les seeting");

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
    private void manageWinner(MessageParser message) {
        System.out.println("recommencer une game");

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
}
