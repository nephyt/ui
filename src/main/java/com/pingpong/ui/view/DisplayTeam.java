package com.pingpong.ui.view;

import com.pingpong.basicclass.enumeration.TeamEnum;
import com.pingpong.basicclass.game.TeamState;
import com.pingpong.basicclass.player.Player;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.Map;

public class DisplayTeam extends VerticalLayout {

    Map<Integer, Player> mapIdPlayer;

    VerticalLayout displayTeam = new VerticalLayout();

    DisplayPlayer player1 = new DisplayPlayer();
    DisplayPlayer player2 = new DisplayPlayer();

    public DisplayTeam() {

        setWidth("25%");

        displayTeam.add(player1.getPlayerImage(), player2.getPlayerImage());

        add(displayTeam);
    }

    public Map<Integer, Player> getMapIdPlayer() {
        return mapIdPlayer;
    }

    public void setMapIdPlayer(Map<Integer, Player> mapIdPlayer) {
        this.mapIdPlayer = mapIdPlayer;
    }

    public Player getPlayerById(Integer id) { return mapIdPlayer.get(id);}

    public void refreshTeam(TeamState team, TeamEnum teamSide) {
        if (TeamEnum.TEAM_A.getCode().equals(teamSide.getCode())) {
            player1.refreshDisplayPlayer(mapIdPlayer.get(team.getLeftPlayer()), hasServe(team.getServer(), team.getLeftPlayer()));
            player2.refreshDisplayPlayer(mapIdPlayer.get(team.getRightPlayer()), hasServe(team.getServer(), team.getRightPlayer()));
        } else {
            player1.refreshDisplayPlayer(mapIdPlayer.get(team.getRightPlayer()), hasServe(team.getServer(), team.getRightPlayer()));
            player2.refreshDisplayPlayer(mapIdPlayer.get(team.getLeftPlayer()), hasServe(team.getServer(), team.getLeftPlayer()));
        }
    }

    private boolean hasServe(Integer server, Integer playerId) {
        if (server == null) {
            return false;
        }
        return server.equals(playerId);

    }

}
