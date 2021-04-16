package com.pingpong.ui.view;

import com.pingpong.basicclass.enumeration.TeamEnum;
import com.pingpong.basicclass.game.Game;
import com.pingpong.basicclass.game.TeamState;
import com.pingpong.basicclass.player.Player;
import com.pingpong.ui.services.ServicesRest;
import com.pingpong.ui.util.Utils;
import com.pingpong.ui.web.controller.WinnerScreenController;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.IFrame;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class WinnerScreen extends VerticalLayout {

    PageGame pageGame;

    Button rematch = new Button("Rematch");
    Button changePlayers = new Button("Change Players");

    HorizontalLayout buttonsDiv = new HorizontalLayout(rematch, changePlayers);
    IFrame frame = new IFrame();

    public WinnerScreen(PageGame pageGame) {
        this.pageGame = pageGame;

        WinnerScreenController.setWinnerScreen(this);

        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        setupIframe(frame, "315px", "560px", true);

        rematch.addClickListener(e -> rematchGame());
        changePlayers.addClickListener(e -> changePlayers());

        Utils.disableSelection(this);
    }

    public void showWinner(Game game, DisplayTeam displayTeamA, DisplayTeam displayTeamB, boolean isMute) {

        Integer winnerTeamId = game.getTeamWinnerId();
        Player winner1;
        Player winner2 = null;

        DisplayTeam loserDisplayTeam;
        TeamState loserTeam;

        String finalScoreStr;

        removeAll();

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


        add(winnerName);
        add(finalScore);
        add(loserName);
        add(time);

        add(getVictorySong(winner1, isMute));

        add(buttonsDiv);

        // save game after using TeamState for the Winning screen
        ServicesRest.saveGame(game); // save state in DB


    }

    public IFrame getVictorySong(Player playerToDisplay, boolean isMute) {

        String autoPlay = "?autoplay=1";
        if (isMute) {
            autoPlay = "";
        }

        String emdebSong = playerToDisplay.getYoutubeEmbedVictorySongPath() + autoPlay;

        frame.setSrc(emdebSong);

        return frame;

    }

    private void setupIframe(IFrame frame, String height, String witdth, boolean isVisible) {
        frame.setHeight(height);
        frame.setWidth(witdth);
        frame.setAllow("accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture");
        frame.getElement().setAttribute("allowfullscreen", true);
        frame.getElement().setAttribute("frameborder", "0");
        frame.setVisible(isVisible);
    }

    public void newMatch() {
        changePlayers.click();
    }

    private TeamEnum getTeamEnumWin(Integer idTeamWin, Integer idTeamA) {
        if (idTeamWin.equals(idTeamA)) {
            return TeamEnum.TEAM_A;
        }
        return TeamEnum.TEAM_B;
    }

    private void changePlayers() {
        pageGame.showGameSetting();
    }

    public void rematchGame() {

        Game game = pageGame.gameScore.getGame();
        DisplayTeam displayTeamA = pageGame.gameScore.getDisplayTeamA();
        DisplayTeam displayTeamB = pageGame.gameScore.getDisplayTeamB();

        Game newGame = new Game(game.getTeamA(), game.getTeamB(), getTeamEnumWin(game.getTeamWinnerId(), game.getTeamA().getId()), game.getMaxScore());

        Game gameInProgress = ServicesRest.saveGame(newGame);

        pageGame.initialiseGameScore(gameInProgress, displayTeamA.getMapIdPlayer(), displayTeamB.getMapIdPlayer());
        pageGame.showGameScore();
    }

}
