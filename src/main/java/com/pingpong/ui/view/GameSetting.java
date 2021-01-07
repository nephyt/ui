package com.pingpong.ui.view;

import com.pingpong.basicclass.game.Game;
import com.pingpong.basicclass.game.Team;
import com.pingpong.basicclass.player.Player;
import com.pingpong.ui.servicesrest.ServicesRest;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameSetting extends VerticalLayout {


    Div playerSelect = new Div();

    PlayerSelector playerSelectorTeamA;
    PlayerSelector playerSelectorTeamB;

    Game gameInProgress = null;


    Div pageGame;

    int scoreMaxSelected = 21;

    public Game getGameInProgress() {
        return gameInProgress;
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


    public GameSetting(List<Player> listPlayer, Div pageGame) {

        this.pageGame = pageGame;

        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        playerSelectorTeamA = new PlayerSelector(listPlayer, "Select player(s) team A :");
        playerSelectorTeamB = new PlayerSelector(listPlayer, "Select player(s) team B :");

        VerticalLayout scoreMaxDiv = new VerticalLayout();
        scoreMaxDiv.setAlignItems(FlexComponent.Alignment.CENTER);
        scoreMaxDiv.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);


        Label selectScoreMax = new Label("Select score max :");
        ComboBox<Integer> scoreMax = new ComboBox<>();
        scoreMax.setItems(11, 21);
        scoreMax.setValue(21);

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

    private void startGame() {

        setVisible(false);

        Team teamA = playerSelectorTeamA.createTeam(true);
        Team teamB = playerSelectorTeamB.createTeam(false);

        Game game = new Game(teamA, teamB,scoreMaxSelected);

        gameInProgress = ServicesRest.createGame(game);

        GameScore gameScore = new GameScore(pageGame, getGameInProgress(), new DisplayTeam(getDisplayPlayerTeamA()), new DisplayTeam(getDisplayPlayerTeamB()));
        gameScore.setVisible(true);

        gameScore.refreshScreen();

        pageGame.add(gameScore);

    }



}
