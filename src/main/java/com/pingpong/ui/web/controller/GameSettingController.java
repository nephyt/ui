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


    @GetMapping(value = "/nxtPlayer1TeamA")
    public void nxtPlayer1TeamA() {

        if (gameSetting != null) {
            gameSetting.getUI().get().access(() -> {
                gameSetting.getPlayerSelectorTeamA().nextPlayer1();

                //     gameScore.getUI().get().access(() -> gameScore.getUI().get().push());
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
