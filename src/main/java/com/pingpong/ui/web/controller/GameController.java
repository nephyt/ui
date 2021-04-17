package com.pingpong.ui.web.controller;

import com.pingpong.basicclass.enumeration.TeamEnum;
import com.pingpong.ui.view.GameScore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameController {


    static GameScore gameScore;

    static public void setGameScore(GameScore pGameScore) {
        gameScore = pGameScore;
    }

    @GetMapping(value = "/teamScored/{teamScored}")
    public String teamScored(@PathVariable TeamEnum teamScored) {

        String result = ""; // next ser

        if (gameScore != null) {
            gameScore.getUI().get().access(() -> {
                gameScore.updateGame(teamScored);
                gameScore.getUI().get().access(() -> gameScore.getUI().get().push());
            });

            result = gameScore.getGame().determineServerState(); // equip A right player
        }

        return result;
    }

    @GetMapping(value = "/teamService")
    public String getTeamService() {

        String result = "";
        if (gameScore != null) {
            result = gameScore.getGame().determineServerState();
        }

        return result;
    }

    @GetMapping(value = "/teamWinner")
    public String getTeamWinner() {

        String result = null;
        if (gameScore != null && gameScore.getGame().getTeamWinnerId() != null) {
            result = "A";
            if (gameScore.getGame().getTeamB().getId().equals(gameScore.getGame().getTeamWinnerId())) {
                result = "B";
            }
        }

        return result;
    }

}
