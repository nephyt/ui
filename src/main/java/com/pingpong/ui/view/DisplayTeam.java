package com.pingpong.ui.view;

import com.pingpong.basicclass.game.Team;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.Map;

public class DisplayTeam extends VerticalLayout {

    Div displayTeam = new Div();


    Map<Integer, DisplayPlayer> mapIdPlayer;

    public DisplayTeam(Map<Integer, DisplayPlayer> mapIdPlayer) {
        this.mapIdPlayer = mapIdPlayer;
    }

    public Component refreshTeam(Team team) {

        Div displayTeam = new Div();

        displayTeam.add(mapIdPlayer.get(team.getRightPlayer()));
        displayTeam.add(mapIdPlayer.get(team.getLeftPlayer()));

        removeAll();
        add(displayTeam);

        return this;
    }


}
