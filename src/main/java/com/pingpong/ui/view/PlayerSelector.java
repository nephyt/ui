package com.pingpong.ui.view;

import com.pingpong.basicclass.game.Team;
import com.pingpong.basicclass.player.Player;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.util.ArrayList;
import java.util.List;

public class PlayerSelector extends VerticalLayout implements KeyNotifier {

    Integer playerId1 = null;
    Integer playerId2 = null;

    String namePlayer1 = null;
    String namePlayer2 = null;

    List<Player> listPlayer1;
    List<Player> listPlayer2;

    Label selectTeam = new Label("Select player(s) team A :");

    Label labelPlayer1 = new Label("Player 1 : ");
    Label labelPlayer2 = new Label("Player 2 : ");

    public PlayerSelector(List<Player> listPlayer, String textSelectTeam) {

        this.listPlayer1 = listPlayer;
        this.listPlayer2 = listPlayer;

        selectTeam.setText(textSelectTeam);

        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        ComboBox<Player> player1 = new ComboBox<>();
        player1.setItemLabelGenerator(Player::getName);
        player1.setItems(listPlayer1);

        ComboBox<Player> player2 = new ComboBox<>();
        player2.setItemLabelGenerator(Player::getName);
        player2.setItems(listPlayer2);
        player2.setVisible(false);
        labelPlayer2.setVisible(false);

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(labelPlayer1, player1, labelPlayer2, player2);
        horizontalLayout.setJustifyContentMode(JustifyContentMode.CENTER);

        add(selectTeam, horizontalLayout);

        player1.addValueChangeListener(event -> {
            if (event.getValue() != null) {
                playerId1 = event.getValue().getId();
                namePlayer1 = event.getValue().getName();
                labelPlayer2.setVisible(true);
                player2.setVisible(true);
            }
        });

        player2.addValueChangeListener(event -> {
            if (event.getValue() != null) {
                playerId2 = event.getValue().getId();
                namePlayer2 = event.getValue().getName();
            }
        });
    }

    public Team createTeam(boolean hasService) {
        return new Team(playerId1, playerId2, hasService);
    }

    public String getLabelTeam() {
        return namePlayer1 + (namePlayer2 != null ? " & " + namePlayer2: "");
    }



}
