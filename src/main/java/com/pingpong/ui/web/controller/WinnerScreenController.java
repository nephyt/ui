package com.pingpong.ui.web.controller;

import com.pingpong.ui.view.WinnerScreen;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WinnerScreenController {


    static WinnerScreen winnerScreen;

    static public void setWinnerScreen(WinnerScreen pWinnerScreen) {
        winnerScreen = pWinnerScreen;
    }

    @GetMapping(value = "/rematch")
    public String rematch() {
    String result;

        if (winnerScreen != null) {
            winnerScreen.getUI().get().access(() -> {
                winnerScreen.rematchGame();
                //     gameScore.getUI().get().access(() -> gameScore.getUI().get().push());
            });
        }

        GameController gameController = new GameController();
        return gameController.getTeamService();
    }

    @GetMapping(value = "/newGame")
    public void newGame() {
        if (winnerScreen != null) {
            winnerScreen.getUI().get().access(() -> {
                winnerScreen.newMatch();
                //     gameScore.getUI().get().access(() -> gameScore.getUI().get().push());
            });
        }
    }


}
