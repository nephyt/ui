package com.pingpong.ui.view;

import com.pingpong.basicclass.enumeration.TeamEnum;
import com.pingpong.basicclass.game.Game;
import com.pingpong.basicclass.game.TeamState;
import com.pingpong.basicclass.player.Player;
import com.pingpong.ui.services.MqttListener;
import com.pingpong.ui.services.ServicesButtons;
import com.pingpong.ui.services.ServicesRest;
import com.pingpong.ui.util.Utils;
import com.pingpong.ui.web.controller.GameController;
import com.pingpong.ui.web.controller.WinnerScreenController;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.IFrame;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class WinnerScreen extends VerticalLayout {

    PageGame pageGame;

    Button rematch = new Button("Rematch");
    Button changePlayers = new Button("Change Players");

    ComboBox<TeamEnum> serverTeam = new ComboBox<>("Équipe qui servira :");

    HorizontalLayout buttonsDiv = new HorizontalLayout(rematch, changePlayers);
    IFrame frame = new IFrame();

    public WinnerScreen(PageGame pageGame) {
        this.pageGame = pageGame;

        WinnerScreenController.setWinnerScreen(this);
        MqttListener.setStateWinnerScreen();

        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        Utils.setupIframe(frame, "315px", "560px", true);

        rematch.addClickListener(e -> rematchGame());
        rematch.setId("rematch");
        changePlayers.addClickListener(e -> changePlayers());
        changePlayers.setId("changementJoueur");

        serverTeam.setItems(TeamEnum.TEAM_A, TeamEnum.TEAM_B);
        serverTeam.setId("serverTeamWinnerScreen");

        serverTeam.addValueChangeListener(event -> {
            displayNotification(formatNotification(event.getValue()));
        });

        Utils.disableSelection(this);
    }

    private String formatNotification(TeamEnum teamServer) {
        String res = "<DIV><b>Prochain Serveur : ";

        TeamState team =  (TeamEnum.TEAM_A.equals(teamServer) ? pageGame.gameScore.game.getTeamStateA() : pageGame.gameScore.game.getTeamStateB());
        DisplayTeam displayTeam =  (TeamEnum.TEAM_A.equals(teamServer) ? pageGame.gameScore.getDisplayTeamA() : pageGame.gameScore.getDisplayTeamB());

        int serverId;
        if (team.isSingle()) {
            serverId = ((TeamEnum.TEAM_A.equals(teamServer) ? pageGame.gameScore.game.getTeamA().getTeamPlayer1().getPlayerId()
                                                            : pageGame.gameScore.game.getTeamB().getTeamPlayer1().getPlayerId()));
        } else {
            // undo le mouvement pour avoir la personne qui va servir le prochain coup
            team.undoMovePlayer();
            serverId = team.getRightPlayer();
            team.movePlayer(); //  undo le undo pour prendre en compte le prochain undo lors de la création de la partie

        }

        Player server = displayTeam.getPlayerById(serverId);

        res += server.getName() + "</b></DIV>";

        return res;

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
                // in double, right player will never be null, so here we are in single, no need to check winner 2
                winner1 = displayTeamA.getPlayerById(game.getTeamStateA().getLeftPlayer());
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


        TeamEnum winnerTeam = getTeamEnumWin(game.getTeamWinnerId(), game.getTeamA().getId());
        if (winnerTeam.equals(serverTeam.getValue())) {
            displayNotification(formatNotification(winnerTeam));
        } else {
            serverTeam.setValue(winnerTeam);
        }

        add(winnerName);
        add(finalScore);
        add(loserName);
        add(time);

        if (winner1.getVictorySongPath() != null) {
            add(getVictorySong(winner1, isMute));
        }

        add(serverTeam);
        add(buttonsDiv);

        // save game after using TeamState for the Winning screen
        ServicesRest.saveGame(game); // save state in DB


    }

    public IFrame getVictorySong(Player playerToDisplay, boolean isMute) {

        String emdebSong = playerToDisplay.getYoutubeEmbedVictorySongPath(!isMute);
        frame.setSrc(emdebSong);

        return frame;

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


    public void changeServerTeam() {
        if (TeamEnum.TEAM_A.equals(serverTeam.getValue())) {
            serverTeam.setValue(TeamEnum.TEAM_B);
        } else {
            serverTeam.setValue(TeamEnum.TEAM_A);
        }
    }

    private void displayNotification(String text) {
        Html textHtml = new Html(text);
        Notification notification = new Notification(textHtml);
        notification.setDuration(6000);
        notification.open();
    }

    public void rematchGame() {

        System.out.println("Set game score a null dans rematchGame");
        GameController.setGameScore(null);

        Game game = pageGame.gameScore.getGame();
        DisplayTeam displayTeamA = pageGame.gameScore.getDisplayTeamA();
        DisplayTeam displayTeamB = pageGame.gameScore.getDisplayTeamB();


        Game newGame = new Game(game.getTeamA(), game.getTeamB(), serverTeam.getValue(), game.getMaxScore(), game.getScoringSoundTeamA(), game.getScoringSoundTeamB());

        setCorrectPositionForDouble(game.getTeamStateA(), newGame.getTeamStateA());
        setCorrectPositionForDouble(game.getTeamStateB(), newGame.getTeamStateB());

        ServicesButtons.getInstance().startMatch(serverTeam.getValue());
        Game gameInProgress = ServicesRest.saveGame(newGame);

        pageGame.initialiseGameScore(gameInProgress, displayTeamA.getMapIdPlayer(), displayTeamB.getMapIdPlayer());
        pageGame.showGameScore();
    }

    private static void setCorrectPositionForDouble(TeamState oldTeamState, TeamState newTeamState) {
        if (!oldTeamState.isSingle()) {
            // simulate a undo pour avoir les bonne position
            oldTeamState.undoMovePlayer();
            if (!oldTeamState.getRightPlayer().equals(newTeamState.getRightPlayer())) {
                newTeamState.swapPosition();
                if (newTeamState.hasService()) {
                    newTeamState.setServer(newTeamState.getRightPlayer());
                }
            }
        }
    }

}
