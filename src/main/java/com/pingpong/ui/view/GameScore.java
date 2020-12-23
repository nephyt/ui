package com.pingpong.ui.view;

import com.pingpong.basicclass.game.Game;
import com.pingpong.basicclass.game.TeamEnum;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;


public class GameScore extends VerticalLayout {

    HorizontalLayout displayScore = new HorizontalLayout();

    long lastClickTime = 0;

    Game game;

    DisplayTeam displayTeamA;
    DisplayTeam displayTeamB;

    Div pageGame;

    public GameScore(Div pageGame, Game gameToManage, DisplayTeam displayTeamA, DisplayTeam displayTeamB) {
        this.game = gameToManage;
        this.pageGame = pageGame;
        this.displayTeamA = displayTeamA;
        this.displayTeamB = displayTeamB;

        setWidthFull();
        displayScore.setWidthFull();

        add(displayScore);

        this.addClickListener( e ->{
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
                    updateGame(e);
                }
                );
    }

    public void refreshScreen() {


        displayScore.removeAll();

        displayScore.add(displayTeamA.refreshTeam(game.getTeamA(), TeamEnum.TEAM_A) );


        // Ici score

        HorizontalLayout scoring = new HorizontalLayout();


        scoring.setAlignItems(Alignment.CENTER);
        scoring.setJustifyContentMode(JustifyContentMode.CENTER);

        scoring.setWidth("50%");
        scoring.getElement().getStyle().set("background-image","url('pingpongtable.png')");
        scoring.getElement().getStyle().set("background-repeat","no-repeat");
        scoring.getElement().getStyle().set("background-size","100% 100%");
//cover

        scoring.add(generateImageScore(game.getTeamA().getScore()));
        scoring.add(generateImageScore(game.getTeamB().getScore()));

        displayScore.add(scoring);

        displayScore.add(displayTeamB.refreshTeam(game.getTeamB(), TeamEnum.TEAM_B));
    }

    private Image getImageScore() {
        Image scoreImg = new Image();

        //scoreImg.setWidth("80px");
        //scoreImg.setHeight("120px");
        scoreImg.setWidth("50%");

        scoreImg.getElement().setAttribute("position","relative");
        scoreImg.getElement().setAttribute("left","30px");

        return scoreImg;
    }

    private Div generateImageScore(int score) {
        Div scoreDiv = new Div();

        scoreDiv.setWidth("50%");
        Image scoreDizaine = getImageScore();
        Image scoreUnit = getImageScore();

        String scoreStr = String.valueOf(score);

        if (score < 10) {
            scoreDizaine.setSrc("digits/0.jpg");
            scoreUnit.setSrc("digits/" + scoreStr + ".jpg");
        } else {
            scoreDizaine.setSrc("digits/" + scoreStr.charAt(0) + ".jpg");
            scoreUnit.setSrc("digits/" + scoreStr.charAt(1) +  ".jpg");
        }

        scoreDiv.add(scoreDizaine, scoreUnit);

        return scoreDiv;
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

    private void updateGame(ClickEvent event) {

        if (game != null) {
            System.out.println("YESYEYS");

            if (event.getClickCount() == 1) {
                game.getTeamA().incrementScore();
                game.updateGame();
            } else if (event.getClickCount() == 2) {
                game.getTeamA().decrementScore(); // undo the count 1
                game.getTeamB().incrementScore();
            }


            if (game.getTeamWinnerId() != null) {
                WinnerScreen winnerScreen = new WinnerScreen(pageGame);
                winnerScreen.showWinner(game, displayTeamA, displayTeamB);
            } else {
                refreshScreen();
            }
        }
    }
}
