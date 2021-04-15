package com.pingpong.ui.view;

import com.pingpong.basicclass.game.Game;
import com.pingpong.basicclass.game.Team;
import com.pingpong.basicclass.player.Player;
import com.pingpong.basicclass.servicecount.AllServiceCount;
import com.pingpong.ui.services.ServicesRest;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import java.util.Map;

public class HistoricGameVIew extends Div {

    private final Integer DEFAULT_TIME = 3;

    private Map<Integer, Player> mapPlayer;

    private IntegerField txtNbHours = new IntegerField("Nombre d'heures", DEFAULT_TIME,"Temps de recul en heure");

    private ComboBox<Team> cboTeam = new ComboBox<>();

    private Grid<Game> grid = new Grid<>(Game.class);

    public HistoricGameVIew() {
        cboTeam.setLabel("Equipe");
        cboTeam.setItemLabelGenerator(e -> getTeamPlayerName(mapPlayer, e));
        cboTeam.addValueChangeListener(event -> {
            fillGrid(txtNbHours.getIntValue());
        });

        txtNbHours.addValueChangeListener(e -> fillGrid(txtNbHours.getIntValue()));

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(txtNbHours, cboTeam);

        add(horizontalLayout);

        setupGrid();

        setVisible(false);

    }

    public void refreshPage() {
        cboTeam.setItems(ServicesRest.getAllTeams());
        cboTeam.setValue(null);
        txtNbHours.setValue("");
        fillGrid(txtNbHours.getIntValue());
    }

    public void fillGrid(Integer nbHours) {
        Integer teamId = null;
        if (cboTeam.getValue() != null) {
            teamId = cboTeam.getValue().getId();
        }
        grid.setItems(ServicesRest.getHistoricGames(nbHours, teamId));
    }

    private void setupGrid() {

        grid.removeAllColumns();

        mapPlayer = ServicesRest.mapPlayer("");

        grid.addColumn(game -> getTeamPlayerName(mapPlayer, game.getWinnerTeam()) )
                .setAutoWidth(true).setResizable(true).setSortable(true).setHeader("Winner").setKey("winner");

        grid.addColumn(game -> getTeamPlayerName(mapPlayer, game.getLoserTeam()))
                .setAutoWidth(true).setResizable(true).setSortable(true).setHeader("Loser").setKey("loser");

        grid.addColumn(game -> formatScore(game))
                .setAutoWidth(true).setResizable(true).setSortable(true).setHeader("Score").setKey("score");

        grid.addColumn(game -> formatServingStats(game))
                .setAutoWidth(true).setResizable(true).setSortable(true).setHeader("#BallsServe/W/L").setKey("ballServe");

        grid.addColumn(game -> game.toStringFirstTimePlayed())
                .setAutoWidth(true).setResizable(true).setSortable(true).setHeader("Start on").setKey("StartOn");

        grid.addColumn(game -> game.toStringLastTimePlayed())
                .setAutoWidth(true).setResizable(true).setSortable(true).setHeader("Complete on").setKey("CompleteOn");

        grid.addColumn(game -> game.toStringTimePlayed())
                .setAutoWidth(true).setResizable(true).setSortable(true).setHeader("Time Played").setKey("timePlayed");

        add(grid);
    }

    private String formatServingStats(Game game) {
        AllServiceCount allServiceCount = ServicesRest.getGameCountService(game.getId());

        String stats = getServiceStatForTeam(allServiceCount, game.getWinnerTeam());
        stats += " vs ";
        stats += getServiceStatForTeam(allServiceCount, game.getLoserTeam());

        return stats;
    }

    private String getServiceStatForTeam(AllServiceCount allServiceCount, Team teamWinner) {
        String stats = allServiceCount.getServiceCountForPlayer(teamWinner.getTeamPlayer1Id()).toStringBallServe();

        if (teamWinner.getTeamPlayer2Id() != null) {
            stats += " - ";
            stats += allServiceCount.getServiceCountForPlayer(teamWinner.getTeamPlayer2Id()).toStringBallServe();
        }
        return stats;
    }

    private String formatScore(Game game) {
        String score;

        if (game.getTeamWinnerId().equals(game.getTeamA().getId())) {
            score = game.getScoreTeamA() + " - " + game.getScoreTeamB();
        } else {
            score = game.getScoreTeamB() + " - " + game.getScoreTeamA();
        }

        return score;
    }

    private String getTeamPlayerName(Map<Integer, Player> mapPlayer, Team team) {
        String result = mapPlayer.get(team.getTeamPlayer1().getPlayerId()).getName();

        if (team.getTeamPlayer2() != null) {
            result += " & " + mapPlayer.get(team.getTeamPlayer2().getPlayerId()).getName();
        }
        return result;
    }

}
