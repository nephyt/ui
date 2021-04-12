package com.pingpong.ui.view;

import com.pingpong.basicclass.game.Game;
import com.pingpong.basicclass.game.Team;
import com.pingpong.basicclass.player.Player;
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

        grid.removeColumnByKey("gameStatus");
        grid.removeColumnByKey("gameTime");
        grid.removeColumnByKey("matchPoint");
        grid.removeColumnByKey("maxScore");
        grid.removeColumnByKey("id");
        grid.removeColumnByKey("teamA");
        grid.removeColumnByKey("teamB");
        grid.removeColumnByKey("teamWinnerId");
        grid.removeColumnByKey("teamStateA");
        grid.removeColumnByKey("teamStateB");
        grid.removeColumnByKey("scoreTeamA");
        grid.removeColumnByKey("scoreTeamB");
        grid.removeColumnByKey("gameType");

        mapPlayer = ServicesRest.mapPlayer("");


        grid.addColumn(game -> getTeamPlayerName(mapPlayer, game, true) )
                .setAutoWidth(true).setSortable(true).setHeader("Winner").setKey("winner");

        grid.addColumn(game -> getTeamPlayerName(mapPlayer, game, false))
                .setAutoWidth(true).setSortable(true).setHeader("Loser").setKey("loser");

        grid.addColumn(game -> formatScore(game))
                .setAutoWidth(true).setSortable(true).setHeader("Score").setKey("score");

        grid.addColumn(game -> game.toStringFirstTimePlayed())
                .setAutoWidth(true).setSortable(true).setHeader("Start on").setKey("StartOn");

        grid.addColumn(game -> game.toStringLastTimePlayed())
                .setAutoWidth(true).setSortable(true).setHeader("Complete on").setKey("CompleteOn");

        grid.addColumn(game -> game.toStringTimePlayed())
                .setAutoWidth(true).setSortable(true).setHeader("Time Played").setKey("timePlayed");


        // Connect selected Customer to editor or hide if none is selected
        /*grid.asSingleSelect().addValueChangeListener(e -> {
            resumeGameBtn.setVisible(true);
            gameSelected = e.getValue();
        });*/

        add(grid);
    }

    private String formatScore(Game game) {
        String score = null;

        if (game.getTeamWinnerId() == game.getTeamA().getId()) {
            score = game.getScoreTeamA() + " - " + game.getScoreTeamB();
        } else {
            score = game.getScoreTeamB() + " - " + game.getScoreTeamA();
        }

        return score;
    }

    private String getTeamPlayerName(Map<Integer, Player> mapPlayer, Game game, boolean winner) {

        Team team = game.getTeamA();
        if (winner) {
            if (!game.getTeamWinnerId().equals(team.getId())) {
                team = game.getTeamB();
            }
        } else {
            if (game.getTeamWinnerId().equals(team.getId())) {
                team = game.getTeamB();
            }
        }

        return getTeamPlayerName(mapPlayer, team);
    }

    private String getTeamPlayerName(Map<Integer, Player> mapPlayer, Team team) {
        String result = mapPlayer.get(team.getTeamPlayer1().getPlayerId()).getName();

        if (team.getTeamPlayer2() != null) {
            result += " & " + mapPlayer.get(team.getTeamPlayer2().getPlayerId()).getName();
        }
        return result;
    }

}
