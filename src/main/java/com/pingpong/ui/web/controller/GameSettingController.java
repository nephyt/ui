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
    public String nextPlayer1TeamA() {
        if (gameSetting != null) {
            gameSetting.getUI().get().access(() -> {
                gameSetting.getPlayerSelectorTeamA().nextPlayer1();
            });
        }
        return gameSetting.getPlayerSelectorTeamA().getPlayer1Name();
    }

    @GetMapping(value = "/nextPlayer1")
    public String nextPlayer2TeamA() {
        if (gameSetting != null) {
            gameSetting.getUI().get().access(() -> {
                gameSetting.getPlayerSelectorTeamA().nextPlayer2();
          });
        }
        return gameSetting.getPlayerSelectorTeamA().getPlayer2Name();
    }

    @GetMapping(value = "/nextPlayer2")
    public String nextPlayer1TeamB() {
        if (gameSetting != null) {
            gameSetting.getUI().get().access(() -> {
                gameSetting.getPlayerSelectorTeamB().nextPlayer1();
            });
        }
        return gameSetting.getPlayerSelectorTeamB().getPlayer1Name();
    }

    @GetMapping(value = "/nextPlayer3")
    public String nextPlayer2TeamB() {
        if (gameSetting != null) {
            gameSetting.getUI().get().access(() -> {
                gameSetting.getPlayerSelectorTeamB().nextPlayer2();
            });
        }
        return gameSetting.getPlayerSelectorTeamB().getPlayer2Name();
    }

    @GetMapping(value = "/nextScoreMax")
    public void nextScoreMax() {
        if (gameSetting != null) {
            gameSetting.getUI().get().access(() -> {
                gameSetting.nextScoreMax();
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
