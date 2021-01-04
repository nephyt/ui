package com.pingpong.ui.web.controller;

import com.pingpong.basicclass.game.TeamEnum;
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
    public String getCurrentGame(@PathVariable TeamEnum teamScored) {

        String result = "YES";
        if (gameScore != null) {
            gameScore.getUI().get().access(() -> {
                gameScore.updateGame(teamScored);
                //     gameScore.getUI().get().access(() -> gameScore.getUI().get().push());
            });
        }

        return result;
    }


}
