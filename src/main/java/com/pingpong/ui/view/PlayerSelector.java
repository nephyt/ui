package com.pingpong.ui.view;

import com.pingpong.basicclass.enumeration.TeamEnum;
import com.pingpong.basicclass.game.Team;
import com.pingpong.basicclass.player.Player;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.List;

public class PlayerSelector extends VerticalLayout implements KeyNotifier {

    Player player1;
    Player player2;

    ComboBox<Player> cboPlayer1 = new ComboBox<>();
    ComboBox<Player> cboPlayer2 = new ComboBox<>();

    List<Player> listPlayer1;
    int indexPlayer1 = -1;
    List<Player> listPlayer2;
    int indexPlayer2 = -1;

    TeamEnum teamEnum = TeamEnum.TEAM_A;
    H2 selectTeam = new H2("Select player(s) team A :");

    GameSetting parent;

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public String getPlayer1Name() {
        String name = "NONE";
        if (cboPlayer1.getValue() != null) {
            name = cboPlayer1.getValue().getName();
        }
        return name;
    }

    public String getPlayer2Name() {
        String name = "NONE";
        if (cboPlayer2.getValue() != null) {
            name = cboPlayer2.getValue().getName();
        }
        return name;
    }

    public void nextPlayer1() {
        ++indexPlayer1;

        if (indexPlayer1 >= listPlayer1.size()) {
            indexPlayer1 = -1;
            cboPlayer1.setValue(null);
        } else {
           cboPlayer1.setValue(listPlayer1.get(indexPlayer1));
        }
    }

    public void nextPlayer2() {
        ++indexPlayer2;

        if (indexPlayer2 >= listPlayer2.size()) {
            indexPlayer2 = -1;
            cboPlayer2.setValue(null);
        } else {
            cboPlayer2.setValue(listPlayer2.get(indexPlayer2));
        }
    }

    public void refreshListPlayer(List<Player> listPlayer) {
        this.listPlayer1 = listPlayer;
        this.listPlayer2 = listPlayer;

        cboPlayer1.setValue(null);
        cboPlayer2.setValue(null);

        cboPlayer1.setItems(listPlayer1);
        cboPlayer2.setItems(listPlayer2);

        indexPlayer1 = -1;
        indexPlayer2 = -1;
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

        cboPlayer1.setId("player1" + teamEnum.getCode());
        cboPlayer1.setLabel("Player 1 : ");
        cboPlayer1.setItemLabelGenerator(Player::getName);
        cboPlayer1.setItems(listPlayer1);


        cboPlayer2.setId("player2" + teamEnum.getCode());
        cboPlayer2.setLabel("Player 2 : ");
        cboPlayer2.setItemLabelGenerator(Player::getName);
        cboPlayer2.setItems(listPlayer2);

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(cboPlayer1, cboPlayer2);
        horizontalLayout.setJustifyContentMode(JustifyContentMode.CENTER);

        add(selectTeam, horizontalLayout);

        cboPlayer1.addValueChangeListener(event -> {
            if (event.getValue() != null) {
                player1 = event.getValue();

                indexPlayer1 = findIndexPlayer(listPlayer1, player1);

            } else {
                indexPlayer1 = -1;
                player1 = null;
            }

            informParent();
        });

        cboPlayer2.addValueChangeListener(event -> {
            if (event.getValue() != null) {
                player2 = event.getValue();

                indexPlayer2 = findIndexPlayer(listPlayer2, player2);
            } else {
                player2 = null;
                indexPlayer2 = -1;
            }

            informParent();
        });
    }

    private int findIndexPlayer(List<Player> listPlayer, Player player) {
        for (int i = 0; i < listPlayer.size() ; ++i) {
            if (listPlayer.get(i).getId().equals(player.getId())) {
                return i;
            }
        }
        throw new RuntimeException("Player Id not found in the list : findIndexPlayer");
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
