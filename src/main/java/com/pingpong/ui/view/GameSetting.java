package com.pingpong.ui.view;

import com.pingpong.basicclass.enumeration.TeamEnum;
import com.pingpong.basicclass.game.Game;
import com.pingpong.basicclass.game.Team;
import com.pingpong.basicclass.player.Player;
import com.pingpong.basicclass.stats.TeamStats;
import com.pingpong.ui.services.ServicesRest;
import com.pingpong.ui.util.Utils;
import com.pingpong.ui.web.controller.GameSettingController;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameSetting extends VerticalLayout {

    ComboBox<Integer> scoreMax = new ComboBox<>();


    Div playerSelect = new Div();

    PlayerSelector playerSelectorTeamA;
    PlayerSelector playerSelectorTeamB;

    PageGame pageGame;

    int scoreMaxSelected = 11;

    public PlayerSelector getPlayerSelectorTeamA() {
        return playerSelectorTeamA;
    }

    public PlayerSelector getPlayerSelectorTeamB() {
        return playerSelectorTeamB;
    }

    public void nextScoreMax() {
        if (scoreMaxSelected == 11) {
            scoreMax.setValue(21);
        } else {
            scoreMax.setValue(11);
        }
    }

    public Map<Integer, Player> getDisplayPlayerTeamA() {
        return getPlayer(playerSelectorTeamA);
    }

    public Map<Integer, Player> getDisplayPlayerTeamB() {
        return getPlayer(playerSelectorTeamB);
    }

    private Map<Integer, Player> getPlayer(PlayerSelector team) {
        Map<Integer, Player> map = new HashMap<>();

        map.put(team.getPlayer1().getId(), team.getPlayer1());

        if (team.getPlayer2() != null) {
            map.put(team.getPlayer2().getId(), team.getPlayer2());
        } else {
            map.put(null, new Player());
        }

        return map;
    }


    public GameSetting(List<Player> listPlayer, PageGame pageGame) {

        GameSettingController.setGameSetting(this);

        this.pageGame = pageGame;

        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        playerSelectorTeamA = new PlayerSelector(listPlayer, TeamEnum.TEAM_A, this);
        playerSelectorTeamB = new PlayerSelector(listPlayer, TeamEnum.TEAM_B, this);

        VerticalLayout scoreMaxDiv = new VerticalLayout();
        scoreMaxDiv.setAlignItems(FlexComponent.Alignment.CENTER);
        scoreMaxDiv.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);


        Label selectScoreMax = new Label("Select score max :");
        scoreMax.setItems(11, 21);
        scoreMax.setValue(scoreMaxSelected);

        scoreMax.addValueChangeListener(event -> {
            if (event.getValue() != null) {
                scoreMaxSelected = event.getValue();
            }
        });

        Button btnStartGame = new Button("Start Game");

        btnStartGame.addClickListener(e -> startGame());

        scoreMaxDiv.add(selectScoreMax, scoreMax, btnStartGame);

        playerSelect.add(playerSelectorTeamA, playerSelectorTeamB);
        playerSelect.add(scoreMaxDiv);

        add(playerSelect);

    }

    public void updatePlayerStats(TeamEnum teamEnum) {

        Team teamA = playerSelectorTeamA.createTeam();
        Team teamB = playerSelectorTeamB.createTeam();

        if (teamEnum.getCode().equals(TeamEnum.TEAM_A.getCode()) && teamA != null) {
            // show stat team A
            TeamStats teamStats = ServicesRest.getTeamStatsByPlayer(teamA);

            displayNotification(formatTeamStats(playerSelectorTeamA, teamStats));
        }
        if (teamEnum.getCode().equals(TeamEnum.TEAM_B.getCode()) && teamB != null) {
            // show stat team B
            TeamStats teamStats = ServicesRest.getTeamStatsByPlayer(teamA);
            displayNotification(formatTeamStats(playerSelectorTeamB, teamStats));
        }

        if (teamA != null && teamB != null) {
            // show stat team A vs team B

            TeamStats teamStats = ServicesRest.getTeamVSStatsByPlayer(teamA, teamB);
            displayNotification(formatTeamVsTeamStats(teamStats));
        }
    }

    private void displayNotification(String text) {
        Html textHtml = new Html(text);
        Notification notification = new Notification(textHtml);
        notification.setDuration(6000);
        notification.open();
    }

    private String formatTeamStats(PlayerSelector teamSelector, TeamStats teamStats) {

        String result = "<DIV><b>" + teamSelector.getLabelTeam() + "</b><br/>";
        result += "# game played : " + teamStats.getNumberOfGamePlayed() + "<br/>";
        result += "# game win : " + teamStats.getNumberOfGameWin() + "<br/>";
        result += "# game lost : " + teamStats.getNumberOfGameLost() + "<br/>";
        result += "time played : " + Utils.formatTimePlayed(teamStats.getTimePlayed());
        result += "<DIV>";
        return result;
    }

    private String formatTeamVsTeamStats(TeamStats teamStats) {

        String result = "<DIV><b>" + playerSelectorTeamA.getLabelTeam() + " VS " + playerSelectorTeamB.getLabelTeam() + "</b><br/>";
        result += "# game played : " + teamStats.getNumberOfGamePlayed() + "<br/>";
        result += "# game win by " + playerSelectorTeamA.getLabelTeam() + " : " + teamStats.getNumberOfGameWin() + "<br/>";
        result += "# game win by " + playerSelectorTeamB.getLabelTeam() + " : " + teamStats.getNumberOfGameLost() + "<br/>";
        result += "time played together : " + Utils.formatTimePlayed(teamStats.getTimePlayed());
        result += "<DIV>";
        return result;
    }

    public void startGame() {

        setVisible(false);

        Team teamA = playerSelectorTeamA.createTeam();
        Team teamB = playerSelectorTeamB.createTeam();

        Game game = new Game(teamA, teamB, TeamEnum.TEAM_A,scoreMaxSelected);

        game = ServicesRest.saveGame(game);

        pageGame.initialiseGameScore(game, getDisplayPlayerTeamA(), getDisplayPlayerTeamB());
        pageGame.showGameScore();
    }

}
