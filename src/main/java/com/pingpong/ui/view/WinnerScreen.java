package com.pingpong.ui.view;

import com.pingpong.basicclass.game.Game;
import com.pingpong.basicclass.game.Team;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class WinnerScreen extends VerticalLayout {

    Div pageGame;

    public WinnerScreen(Div pageGame) {
        this.pageGame = pageGame;
    }

    public void showWinner(Game game, DisplayTeam displayTeamA, DisplayTeam displayTeamB) {

        Integer winnerTeamId = game.getTeamWinnerId();
        DisplayPlayer winner = null;

        if (winnerTeamId == game.getTeamA().getId()) {
            if (game.getTeamA().getRightPlayer() != null) {
                winner = displayTeamA.getDisplayPlayerById(game.getTeamA().getRightPlayer());
            } else {
                winner = displayTeamA.getDisplayPlayerById(game.getTeamA().getLeftPlayer());
            }
        } else {
            if (game.getTeamB().getRightPlayer() != null) {
                winner = displayTeamB.getDisplayPlayerById(game.getTeamB().getRightPlayer());
            } else {
                winner = displayTeamB.getDisplayPlayerById(game.getTeamB().getLeftPlayer());
            }
        }

        add(winner.getVictorySong());


        pageGame.add(this);



    }

}
