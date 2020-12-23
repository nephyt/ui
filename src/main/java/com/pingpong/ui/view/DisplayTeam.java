package com.pingpong.ui.view;

import com.pingpong.basicclass.game.Team;
import com.pingpong.basicclass.game.TeamEnum;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.Map;

public class DisplayTeam extends VerticalLayout {

    Map<Integer, DisplayPlayer> mapIdPlayer;

    public DisplayTeam(Map<Integer, DisplayPlayer> mapIdPlayer) {
        this.mapIdPlayer = mapIdPlayer;
    }

    public DisplayPlayer getDisplayPlayerById(Integer id) { return mapIdPlayer.get(id);}

    public Component refreshTeam(Team team, TeamEnum teamSide) {

        VerticalLayout displayTeam = new VerticalLayout();

        setWidth("25%");

        if (TeamEnum.TEAM_A.getCode().equals(teamSide.getCode())) {
            displayTeam.add(mapIdPlayer.get(team.getLeftPlayer()).getDisplayPlayer(hasServe(team.getServer(), team.getLeftPlayer())));
            displayTeam.add(mapIdPlayer.get(team.getRightPlayer()).getDisplayPlayer(hasServe(team.getServer(),team.getRightPlayer())));
        } else {
            displayTeam.add(mapIdPlayer.get(team.getRightPlayer()).getDisplayPlayer(hasServe(team.getServer(), team.getRightPlayer())));
            displayTeam.add(mapIdPlayer.get(team.getLeftPlayer()).getDisplayPlayer(hasServe(team.getServer(), team.getLeftPlayer())));
        }
        removeAll();
        add(displayTeam);

        return this;
    }

    private boolean hasServe(Integer server, Integer playerId) {
        if (server == null) {
            return false;
        }
        return server == playerId;

    }

}
