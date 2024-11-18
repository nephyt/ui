package com.pingpong.ui.view;

import com.pingpong.basicclass.game.Game;
import com.pingpong.basicclass.game.Team;
import com.pingpong.basicclass.player.Player;
import com.pingpong.ui.services.ServicesRest;
import com.pingpong.ui.web.controller.GameController;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.tabs.Tabs;

import java.util.HashMap;
import java.util.Map;

public class PausedGameVIew extends Div {

    private Button resumeGameBtn = new Button("Resume Game");
    private Button deleteGameBtn = new Button("Delete Game");
    PageGame pageGame;

    Map<Integer, Player> mapPlayer;

    private Game gameSelected = null;
    private Tabs tabs;
    Grid<Game> grid = new Grid<>(Game.class);


    public PausedGameVIew(PageGame pageGame, Tabs tabs) {
        this.pageGame = pageGame;
        this.tabs = tabs;

        setupGrid();

        resumeGameBtn.addClickListener(e -> resumeGame());
        resumeGameBtn.setVisible(false);
        add(resumeGameBtn);

        deleteGameBtn.addClickListener(e -> deleteGame());
        deleteGameBtn.setVisible(false);
        add(deleteGameBtn);

        setVisible(false);

    }

    private void resumeGame() {
        if (gameSelected != null) {
            gameSelected.resumeGame();

            pageGame.initialiseGameScore(gameSelected, getMapPlayerTeam(gameSelected.getTeamA(), mapPlayer), getMapPlayerTeam(gameSelected.getTeamB(), mapPlayer));
            pageGame.showGameScore(true);

            tabs.setSelectedIndex(2);
        }
    }

    private void deleteGame() {
        if (gameSelected != null) {
            GameController gameController = new GameController();
            gameController.deleteGame(gameSelected.getId());
            fillGrid();
            resumeGameBtn.setVisible(false);
            deleteGameBtn.setVisible(false);
        }
    }

    private Map<Integer, Player> getMapPlayerTeam(Team team, Map<Integer, Player> mapPlayer) {
        Map<Integer, Player> map = new HashMap<>();

        map.put(team.getTeamPlayer1().getPlayerId(), mapPlayer.get(team.getTeamPlayer1().getPlayerId()));

        if (team.getTeamPlayer2() != null) {
            map.put(team.getTeamPlayer2().getPlayerId(), mapPlayer.get(team.getTeamPlayer2().getPlayerId()));
        } else {
            map.put(null, new Player());
        }

        return map;
    }

    public void fillGrid() {
        grid.setItems(ServicesRest.getPausedGames());
    }

    private void setupGrid() {

        grid.removeColumnByKey("gameTime");
        grid.removeColumnByKey("winnerTeam");
        grid.removeColumnByKey("loserTeam");
       // grid.removeColumnByKey("id");
        grid.removeColumnByKey("teamA");
        grid.removeColumnByKey("teamB");
        grid.removeColumnByKey("teamWinnerId");
        grid.removeColumnByKey("teamStateA");
        grid.removeColumnByKey("teamStateB");
        grid.removeColumnByKey("scoreTeamA");
        grid.removeColumnByKey("scoreTeamB");

        mapPlayer = ServicesRest.mapPlayer("");


        grid.addColumn(game -> getTeamPlayerName(mapPlayer, game.getTeamA()) + " VS " + getTeamPlayerName(mapPlayer, game.getTeamB()))
                .setAutoWidth(true).setSortable(true).setHeader("Players").setKey("players");

        grid.addColumn(game -> game.getScoreTeamA() + " - " + game.getScoreTeamB())
                .setAutoWidth(true).setSortable(true).setHeader("Score").setKey("score");

        grid.addColumn(game -> game.toStringLastTimePlayed())
                .setSortable(true).setHeader("Last Time Played").setKey("lastTimePlayed");

        grid.addColumn(game -> game.toStringTimePlayed())
                .setSortable(true).setHeader("Time Played").setKey("timePlayed");


        // Connect selected Customer to editor or hide if none is selected
        grid.asSingleSelect().addValueChangeListener(e -> {
            resumeGameBtn.setVisible(true);
            deleteGameBtn.setVisible(true);
            gameSelected = e.getValue();
        });

        add(grid);
    }

    private String getTeamPlayerName(Map<Integer, Player> mapPlayer, Team teamA) {

        Player player = mapPlayer.get(teamA.getTeamPlayer1().getPlayerId());
        if (player == null) {
            // need to refresh the player list
            mapPlayer = ServicesRest.mapPlayer("");
            player = mapPlayer.get(teamA.getTeamPlayer1().getPlayerId());
        }

        String result = player.getName();

        if (teamA.getTeamPlayer2() != null) {
            result += " & " + mapPlayer.get(teamA.getTeamPlayer2().getPlayerId()).getName();
        }
        return result;
    }

}
