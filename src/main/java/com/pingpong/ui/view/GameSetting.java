package com.pingpong.ui.view;

import com.pingpong.basicclass.game.Game;
import com.pingpong.basicclass.game.Team;
import com.pingpong.basicclass.player.Player;
import com.pingpong.ui.Constants;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class GameSetting extends VerticalLayout {


    Div playerSelect = new Div();

    PlayerSelector playerSelectorTeamA;
    PlayerSelector playerSelectorTeamB;

    Game gameInProgess = null;

    int scoreMaxSelected = 21;

    public GameSetting(List<Player> listPlayer) {

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

        playerSelect.setVisible(false);


        Team teamA = playerSelectorTeamA.createTeam(true);
        Team teamB = playerSelectorTeamB.createTeam(false);

        Game game = new Game(teamA, teamB,scoreMaxSelected);

        gameInProgess = createGame(game);

    }

    private Game createGame(Game game) {
        String uri = Constants.SERVICE_GAME_URL +  "createGame";

        //TODO: Autowire the RestTemplate in all the examples
        RestTemplate restTemplate = new RestTemplate();

        Game savedGame = restTemplate.postForObject(uri, game, Game.class);

        return savedGame;
    }

}