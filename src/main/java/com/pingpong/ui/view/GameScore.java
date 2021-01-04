package com.pingpong.ui.view;

import com.pingpong.basicclass.game.Game;
import com.pingpong.basicclass.game.Team;
import com.pingpong.basicclass.game.TeamEnum;
import com.pingpong.ui.servicesrest.ServicesRest;
import com.pingpong.ui.thread.ClickThread;
import com.pingpong.ui.thread.ServiceCountThread;
import com.pingpong.ui.web.controller.GameController;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;


public class GameScore extends VerticalLayout {

    HorizontalLayout displayScore = new HorizontalLayout();

    ClickThread clickListener;
    Thread thread;

    HorizontalLayout scoring = new HorizontalLayout();

    Game game;

    DisplayTeam displayTeamA;
    DisplayTeam displayTeamB;

    DisplayScore displayScoreTeamA = new DisplayScore();
    DisplayScore displayScoreTeamB = new DisplayScore();

    Registration clickScore;
    Registration clickTeamA;
    Registration clickTeamB;

    Div pageGame;

    public GameScore(Div pageGame, Game gameToManage, DisplayTeam displayTeamA, DisplayTeam displayTeamB) {
        this.game = gameToManage;
        this.pageGame = pageGame;
        this.displayTeamA = displayTeamA;
        this.displayTeamB = displayTeamB;

        clickListener = new ClickThread(this);
        thread = new Thread(clickListener);
        thread.start();

        GameController.setGameScore(this);

        setWidthFull();
        displayScore.setWidthFull();

        displayScore.add(displayTeamA);

        // Table ping pong
        scoring.setAlignItems(Alignment.CENTER);
        scoring.setJustifyContentMode(JustifyContentMode.CENTER);
        scoring.setWidth("50%");
        scoring.getElement().getStyle().set("background-image","url('pingpongtable.png')");
        scoring.getElement().getStyle().set("background-repeat","no-repeat");
        scoring.getElement().getStyle().set("background-size","100% 100%");

        scoring.add(displayScoreTeamA.getScore());
        scoring.add(displayScoreTeamB.getScore());

        displayScore.add(scoring);


        displayScore.add(displayTeamB);


        add(displayScore);

        clickScore = scoring.addClickListener( e -> addClick(e));

        clickTeamA = displayTeamA.addClickListener(e -> updateGame(TeamEnum.TEAM_A));
        clickTeamB = displayTeamB.addClickListener(e -> updateGame(TeamEnum.TEAM_B));

    }

    public void refreshScreen() {

        displayTeamA.refreshTeam(game.getTeamA(), TeamEnum.TEAM_A);

        // Ici score

        displayScoreTeamA.refreshImageScore(game.getTeamA().getScore());
        displayScoreTeamB.refreshImageScore(game.getTeamB().getScore());


        displayTeamB.refreshTeam(game.getTeamB(), TeamEnum.TEAM_B);
    }



    private Label generateLabelScore(int score) {
        Label scoreLabel = new Label("");
        String scoreStr = "";
        if (score < 10) {
            scoreStr = "0";
        }
        scoreStr += score;
        scoreLabel.setText(scoreStr);

        return scoreLabel;
    }

    private void addClick(ClickEvent event) {

        if (game != null) {
            synchronized (clickListener) {
                clickListener.addClickEvent(event);
                clickListener.notify();
            }
        }
    }

    public void updateGame(TeamEnum teamScored) {

        if (game != null) {
            if (TeamEnum.TEAM_A.getCode().equals(teamScored.getCode() )) {
                updateServiceCount(game.getTeamA(), game.getTeamB());

                game.getTeamA().incrementScore();
                game.updateGame();
            }
            else if (TeamEnum.TEAM_B.getCode().equals(teamScored.getCode() )) {
                updateServiceCount(game.getTeamB(), game.getTeamA());

                game.getTeamB().incrementScore();
                game.updateGame();
            }

            // done after the click or double click to end the game correctly
            game.updateGameIfFinish();

            ServicesRest.updateGame(game); // save state in DB

            if (game.getTeamWinnerId() != null) {
                clickScore.remove();
                clickTeamA.remove();
                clickTeamB.remove();

                synchronized (clickListener) {
                    clickListener.stopThread();
                    clickListener.notify();
                }

                GameController.setGameScore(null);

                WinnerScreen winnerScreen = new WinnerScreen(pageGame);
                winnerScreen.showWinner(game, displayTeamA, displayTeamB);
            } else {
                refreshScreen();
            }
        }
    }

    private void updateServiceCount(Team teamScored, Team teamLost) {
        boolean winServe = false;
        Integer server = teamLost.getServer();
        if (teamScored.hasService()) {
            winServe = true;
            server = teamScored.getServer();
        }
        new ServiceCountThread(server, winServe).run();
     //   new Thread(new ServiceCountThread(server, winServe)).start();
    }
}
