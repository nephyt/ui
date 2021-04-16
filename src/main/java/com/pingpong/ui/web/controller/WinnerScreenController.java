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
        if (winnerScreen != null) {
            winnerScreen.getUI().get().access(() -> {
                winnerScreen.rematchGame();
                //     gameScore.getUI().get().access(() -> gameScore.getUI().get().push());
            });
        }

        return new GameController().getTeamService();
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
