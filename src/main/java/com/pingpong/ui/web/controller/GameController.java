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

        if (gameScore != null) {
            gameScore.getUI().get().access(() -> {
                gameScore.updateGame(teamScored);
                //     gameScore.getUI().get().access(() -> gameScore.getUI().get().push());
            });
        }
        String result = "A";
        if (gameScore != null && gameScore.getGame().getTeamStateB().hasService()) {
            result = "B";
        }
        if (gameScore != null && gameScore.getGame().getTeamWinnerId() != null) {
            result = "WIN_A";
            if (gameScore.getGame().getTeamB().getId().equals(gameScore.getGame().getTeamWinnerId())) {
                result = "WIN_B";
            }
        }

        return result;
    }

    @GetMapping(value = "/teamService")
    public String getTeamService() {

        String result = "A";
        if (gameScore != null && gameScore.getGame().getTeamStateB().hasService()) {
            result = "B";
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
