package com.pingpong.ui.view;

import com.pingpong.basicclass.game.Game;
import com.pingpong.basicclass.player.Player;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.Map;

public class GameScore extends VerticalLayout {

    Div displayScore;

    Game game;

    DisplayTeam displayTeamA;
    DisplayTeam displayTeamB;

    Label scoreTeamA = new Label("00");
    Label scoreTeamB = new Label("00");

    public GameScore(Game gameToManage, DisplayTeam displayTeamA, DisplayTeam displayTeamB) {
        game = gameToManage;
        this.displayTeamA = displayTeamA;
        this.displayTeamB = displayTeamB;


        this.addClickListener( e -> updateGame(e));
    }

    public void refreshScreen() {

        displayScore.add(displayTeamA.refreshTeam(game.getTeamA()));

        // Ici score
        displayScore.add(generateLabelScore(game.getTeamA().getScore()));
        displayScore.add(generateLabelScore(game.getTeamB().getScore()));

        displayScore.add(displayTeamA.refreshTeam(game.getTeamB()));
    }

    private Label generateLabelScore(int score) {
        Label scoreLabel = new Label("");
        String scoreStr = "";
        if (score == 0) {
            scoreStr = "0";
        }
        scoreStr += score;

        scoreLabel.setText(scoreStr);

        return scoreLabel;
    }

    private void updateGame(ClickEvent event) {

        if (game != null) {
            System.out.println("YESYEYS");

            if (event.getClickCount() == 1) {
                game.getTeamA().incrementScore();
            } else if (event.getClickCount() == 2) {
                game.getTeamA().decrementScore(); // undo the count 1
                game.getTeamB().incrementScore();
            }

            game.updateGame();

            refreshScreen();

        }
    }
}
