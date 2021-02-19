package com.pingpong.ui.web.controller;


import com.pingpong.ui.view.GameSetting;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameSettingController {

    static GameSetting gameSetting;


    static public void setGameSetting(GameSetting pGmeSetting) {
        gameSetting = pGmeSetting;
    }


    @GetMapping(value = "/nextPlayer0")
    public void nextPlayer1TeamA() {
        if (gameSetting != null) {
            gameSetting.getUI().get().access(() -> {
                gameSetting.getPlayerSelectorTeamA().nextPlayer1();
            });
        }
    }

    @GetMapping(value = "/nextPlayer1")
    public void nextPlayer2TeamA() {
        if (gameSetting != null) {
            gameSetting.getUI().get().access(() -> {
                gameSetting.getPlayerSelectorTeamA().nextPlayer2();
          });
        }
    }

    @GetMapping(value = "/nextPlayer2")
    public void nextPlayer1TeamB() {
        if (gameSetting != null) {
            gameSetting.getUI().get().access(() -> {
                gameSetting.getPlayerSelectorTeamB().nextPlayer1();
            });
        }
    }

    @GetMapping(value = "/nextPlayer3")
    public void nextPlayer2TeamB() {
        if (gameSetting != null) {
            gameSetting.getUI().get().access(() -> {
                gameSetting.getPlayerSelectorTeamB().nextPlayer2();
            });
        }
    }


    @GetMapping(value = "/startGame")
    public void startGame() {

        if (gameSetting != null) {
            gameSetting.getUI().get().access(() -> {
                gameSetting.startGame();

                //     gameScore.getUI().get().access(() -> gameScore.getUI().get().push());
            });
        }
        gameSetting = null;
    }



}
