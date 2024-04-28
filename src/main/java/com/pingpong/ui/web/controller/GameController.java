package com.pingpong.ui.web.controller;

import com.pingpong.basicclass.enumeration.TeamEnum;
import com.pingpong.ui.services.ServicesRest;
import com.pingpong.ui.view.GameScore;
import org.springframework.web.bind.annotation.DeleteMapping;
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

        final String[] result = {""}; // next ser

        if (gameScore != null) {
            gameScore.getUI().get().access(() -> {
                gameScore.updateGame(teamScored);
                result[0] = gameScore.getGame().determineServerState();
                gameScore.getUI().get().push();
            });

        }

        return result[0];
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

    @DeleteMapping(value = "/deleteGame/{gameId}")
    public String deleteGame(@PathVariable Integer gameId) {
        // call service game
        ServicesRest.deleteGame(gameId);
        // call serviceCount
        ServicesRest.deleteServiceGame(gameId);

        return "game id " + gameId + " deleted";
    }


}
