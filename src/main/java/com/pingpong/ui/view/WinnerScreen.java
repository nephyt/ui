package com.pingpong.ui.view;

import com.pingpong.basicclass.game.Game;
import com.pingpong.basicclass.game.TeamEnum;
import com.pingpong.basicclass.game.TeamState;
import com.pingpong.basicclass.player.Player;
import com.pingpong.ui.servicesrest.ServicesRest;
import com.pingpong.ui.util.Utils;
import com.pingpong.ui.web.controller.GameController;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class WinnerScreen extends VerticalLayout {

    Div pageGame;
    boolean isMute = false;

    public WinnerScreen(Div pageGame, boolean isMute) {
        this.pageGame = pageGame;
        this.isMute = isMute;

        pageGame.removeAll();

        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        Utils.disableSelection(this);
    }

    public void showWinner(Game game, DisplayTeam displayTeamA, DisplayTeam displayTeamB) {

        Integer winnerTeamId = game.getTeamWinnerId();
        Player winner1;
        Player winner2 = null;

        DisplayTeam loserDisplayTeam;
        TeamState loserTeam;

        String finalScoreStr = "";

        if (winnerTeamId.equals(game.getTeamA().getId())) {
            finalScoreStr = game.getScoreTeamA() + " - " + game.getScoreTeamB();
            loserDisplayTeam = displayTeamB;
            loserTeam = game.getTeamStateB();

            if (game.getTeamStateA().getRightPlayer() != null) {
                winner1 = displayTeamA.getPlayerById(game.getTeamStateA().getRightPlayer());

                if (game.getTeamStateA().getLeftPlayer() != null) {
                    winner2 = displayTeamA.getPlayerById(game.getTeamStateA().getLeftPlayer());
                }
            } else {
                winner1 = displayTeamA.getPlayerById(game.getTeamStateA().getLeftPlayer());
                if (game.getTeamStateA().getLeftPlayer() != null) {
                    winner1 = displayTeamA.getPlayerById(game.getTeamStateA().getLeftPlayer());
                }
            }
        } else {
            finalScoreStr = game.getScoreTeamB() + " - " + game.getScoreTeamA();
            loserDisplayTeam = displayTeamA;
            loserTeam = game.getTeamStateA();
            if (game.getTeamStateB().getRightPlayer() != null) {
                winner1 = displayTeamB.getPlayerById(game.getTeamStateB().getRightPlayer());

                if (game.getTeamStateB().getLeftPlayer() != null) {
                    winner2 = displayTeamB.getPlayerById(game.getTeamStateB().getLeftPlayer());
                }
            } else {
                winner1 = displayTeamB.getPlayerById(game.getTeamStateB().getLeftPlayer());
            }
        }


        String playersNames = winner1.getName();

        if (winner2 != null) {
            playersNames += " & " + winner2.getName();
        }
        Html winnerName = new Html("<font>" + playersNames + "</font>");
        winnerName.getElement().getStyle().set("font-size", "49px");


        Html finalScore = new Html("<font>" + finalScoreStr + "</font>");
        finalScore.getElement().getStyle().set("font-size", "30px");

        String loserNameStr = "";
        if (loserTeam.getRightPlayer() != null) {
            loserNameStr = loserDisplayTeam.getPlayerById(loserTeam.getRightPlayer()).getName();
        }
        if (loserTeam.getLeftPlayer() != null) {
            if (!"".equals(loserNameStr)) {
                loserNameStr += " & ";
            }
            loserNameStr += loserDisplayTeam.getPlayerById(loserTeam.getLeftPlayer()).getName();
        }

        Html loserName = new Html("<font>" + loserNameStr + "</font>");
        loserName.getElement().getStyle().set("font-size", "24px");



        Html time = new Html("<font>Time : " + game.toStringTimePlayed() + "</font>");
        loserName.getElement().getStyle().set("font-size", "24px");


        Image winnerImg = new Image();
        winnerImg.setWidth("25%");
        winnerImg.setSrc("winner/winner1.jpg");


        add(winnerName);
        add(finalScore);
        add(loserName);
        add(time);
      //  add(winnerImg);

        DisplayPlayer winnerDisplaySong = new DisplayPlayer();
        add(winnerDisplaySong.getVictorySong(winner1, isMute));

        HorizontalLayout buttonsDiv = new HorizontalLayout();

        Button rematch = new Button("Rematch");
        Button changePlayers = new Button("Change Players");

        buttonsDiv.add(rematch);
        buttonsDiv.add(changePlayers);
        rematch.addClickListener(e -> rematchGame(game, displayTeamA, displayTeamB));
        changePlayers.addClickListener(e -> changePlayers());

        add(buttonsDiv);

        // save game after using TeamState for the Winning screen
        ServicesRest.saveGame(game); // save state in DB


        pageGame.add(this);



    }

    private TeamEnum getTeamEnumWin(Integer idTeamWin, Integer idTeamA) {
        if (idTeamWin.equals(idTeamA)) {
            return TeamEnum.TEAM_A;
        }
        return TeamEnum.TEAM_B;
    }

    private void changePlayers() {
        pageGame.removeAll();
        GameController.setGameScore(null);
        pageGame.add(new GameSetting(ServicesRest.listPlayer(""), pageGame));
    }

    private void rematchGame(Game game, DisplayTeam displayTeamA, DisplayTeam displayTeamB) {

        GameController.setGameScore(null);
        pageGame.removeAll();

        Game newGame = new Game(game.getTeamA(), game.getTeamB(), getTeamEnumWin(game.getTeamWinnerId(), game.getTeamA().getId()), game.getMaxScore());

        Game gameInProgress = ServicesRest.saveGame(newGame);

        GameScore gameScore = new GameScore(pageGame, gameInProgress, displayTeamA, displayTeamB);
        gameScore.refreshScreen();
        pageGame.add(gameScore);
        gameScore.setVisible(true);

    }

}
