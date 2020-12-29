package com.pingpong.ui.view;

import com.pingpong.basicclass.game.Game;
import com.pingpong.basicclass.game.TeamEnum;
import com.pingpong.ui.servicesrest.ServicesRest;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;


public class GameScore extends VerticalLayout {

    HorizontalLayout displayScore = new HorizontalLayout();


    HorizontalLayout scoring = new HorizontalLayout();

    long lastClickTime = 0;

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

        clickScore = scoring.addClickListener( e ->{
                /*long currentClickTime = System.currentTimeMillis();

                System.out.println("Debut : lastClickTime " + lastClickTime + " current " + currentClickTime + " click count : " + e.getClickCount());
                if (e.getClickCount() == 1) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                    System.out.println("apres le sleep : lastClickTime " + lastClickTime + " current " + currentClickTime + " click count : " + e.getClickCount());
                    System.out.println("apres slee : currentClickTime - lastClickTime " + (currentClickTime - lastClickTime));


                    if (currentClickTime - lastClickTime > 500) {
                        lastClickTime = currentClickTime;
                        updateGame(e);
                    }
                } else {
                    lastClickTime = currentClickTime;
                    updateGame(e);
                }*/
                    updateGame(e, null);
                }
                );

        clickTeamA = displayTeamA.addClickListener(e -> updateGame(e, TeamEnum.TEAM_A));
        clickTeamB = displayTeamB.addClickListener(e -> updateGame(e, TeamEnum.TEAM_B));
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

    private void updateGame(ClickEvent event, TeamEnum teamScored) {

        if (game != null) {
            if (teamScored == null) {
                if (event.getClickCount() == 1) {
                    game.getTeamA().incrementScore();
                    game.updateGame();
                } else if (event.getClickCount() == 2) {
                    game.getTeamA().decrementScore(); // undo the count 1
                    game.getTeamB().incrementScore();
                }
            }
            else if (TeamEnum.TEAM_A.getCode().equals(teamScored.getCode() )) {
                game.getTeamA().incrementScore();
                game.updateGame();
            }
            else if (TeamEnum.TEAM_B.getCode().equals(teamScored.getCode() )) {
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

                WinnerScreen winnerScreen = new WinnerScreen(pageGame);
                winnerScreen.showWinner(game, displayTeamA, displayTeamB);
            } else {
                refreshScreen();
            }
        }
    }
}
