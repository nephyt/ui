package com.pingpong.ui.view;

import com.pingpong.basicclass.enumeration.TeamEnum;
import com.pingpong.basicclass.game.Team;
import com.pingpong.basicclass.player.Player;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.List;

public class PlayerSelector extends VerticalLayout implements KeyNotifier {

    Player player1;
    Player player2;

    List<Player> listPlayer1;
    List<Player> listPlayer2;

    TeamEnum teamEnum = TeamEnum.TEAM_A;
    Label selectTeam = new Label("Select player(s) team A :");

    Label labelPlayer1 = new Label("Player 1 : ");
    Label labelPlayer2 = new Label("Player 2 : ");

    GameSetting parent;

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public PlayerSelector(List<Player> listPlayer, TeamEnum teamEnum, GameSetting parent) {

        this.listPlayer1 = listPlayer;
        this.listPlayer2 = listPlayer;

        this.parent = parent;

        if (TeamEnum.TEAM_B.getCode().equals(teamEnum.getCode())) {
            this.teamEnum = teamEnum;
            selectTeam.setText("Select player(s) team B :");
        }

        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        ComboBox<Player> cboPlayer1 = new ComboBox<>();
        cboPlayer1.setItemLabelGenerator(Player::getName);
        cboPlayer1.setItems(listPlayer1);

        ComboBox<Player> cboPlayer2 = new ComboBox<>();
        cboPlayer2.setItemLabelGenerator(Player::getName);
        cboPlayer2.setItems(listPlayer2);
        cboPlayer2.setVisible(false);
        labelPlayer2.setVisible(false);

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(labelPlayer1, cboPlayer1, labelPlayer2, cboPlayer2);
        horizontalLayout.setJustifyContentMode(JustifyContentMode.CENTER);

        add(selectTeam, horizontalLayout);

        cboPlayer1.addValueChangeListener(event -> {
            if (event.getValue() != null) {
                player1 = event.getValue();
                labelPlayer2.setVisible(true);
                cboPlayer2.setVisible(true);
            } else {
                player2 = null;
                player1 = null;
                labelPlayer2.setVisible(false);
                cboPlayer2.setVisible(false);
                cboPlayer2.setValue(null);
            }

            informParent();
        });

        cboPlayer2.addValueChangeListener(event -> {
            if (event.getValue() != null) {
                player2 = event.getValue();
            } else {
                player2 = null;
            }

            informParent();
        });
    }

    private void informParent() {
        if (parent != null) {
            parent.updatePlayerStats(teamEnum);
        }
    }

    public Team createTeam() {
        if (player1 == null) {
            return null;
        }
        return new Team(player1.getId(),  (player2==null? null :player2.getId()));
    }

    public byte[] getPicturePlayer1() {
        return player1.getPicture();
    }

    public byte[] getPicturePlayer2() {
        if (player2 != null) {
            return player2.getPicture();
        }
        return new byte[1];
    }

    public String getLabelTeam() {
        String names = "";
        if (player1 != null) {
            names = player1.getName();
        }
        if (player2 != null) {
            names += (" & " + player2.getName());
        }
        return names;
    }



}
