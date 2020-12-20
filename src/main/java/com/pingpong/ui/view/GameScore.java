package com.pingpong.ui.view;

import com.pingpong.basicclass.game.Game;
import com.pingpong.basicclass.player.Player;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.Map;

public class GameScore extends VerticalLayout {

    Div displayScore;

    Game game;

    DisplayTeam displayTeamA;
    DisplayTeam displayTeamB;

    public GameScore(Game gameToManage, DisplayTeam displayTeamA, DisplayTeam displayTeamB) {
        game = gameToManage;
        this.displayTeamA = displayTeamA;
        this.displayTeamB = displayTeamB;
    }

    public void refreshScreen() {

        displayScore.add(displayTeamA.refreshTeam(game.getTeamA()));
        displayScore.add(displayTeamA.refreshTeam(game.getTeamB()));

    }
}
