package com.pingpong.ui.view;

import com.pingpong.basicclass.player.ListOfPlayers;
import com.pingpong.basicclass.player.Player;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Route("players")
public class MainView extends VerticalLayout {

    Grid<Player> grid;

    public MainView() {
      //  add(new Button("Click me", e -> Notification.show("Hello, Spring+Vaadin user!")));

        Tab tabPlayer = new Tab("Players");
        Tab tabGame = new Tab("Game");

        Div pagePlayers = new Div();

        Div pageGame = buildPageGame();

        pageGame.setVisible(false);

        this.grid = new Grid<>(Player.class);
        TextField filter = new TextField();
        filter.setPlaceholder("Filter by last name");
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> listPlayer(e.getValue()));

        grid.setWidthFull();
        pagePlayers.add(filter,grid);

        pagePlayers.setWidthFull();

        Map<Tab, Component> tabsToPages = new HashMap<>();
        tabsToPages.put(tabPlayer, pagePlayers);
        tabsToPages.put(tabGame, pageGame);

        Tabs tabs = new Tabs(tabPlayer, tabGame);
        tabs.setWidthFull();
        tabs.setFlexGrowForEnclosedTabs(1);

        Div pages = new Div(pagePlayers, pageGame);
        pages.setWidthFull();

        tabs.addSelectedChangeListener(event -> {
            tabsToPages.values().forEach(page -> page.setVisible(false));
            Component selectedPage = tabsToPages.get(tabs.getSelectedTab());
            selectedPage.setVisible(true);
        });

        add(tabs, pages);

        //add(filter, grid);



    }

    private Div buildPageGame() {

        Div pageGame = new Div();
        pageGame.setWidthFull();

        Label team1 = new Label();
        team1.setText("Myriam");

        Label team2 = new Label();
        team2.setText("Cedric");

        Div teamName = new Div(team1, team2);
        teamName.setWidthFull();


        // compteur

        Label scoreTeam2 = new Label();
        scoreTeam2.setText("5");

        Label scoreTeam1 = new Label();
        scoreTeam1.setText("5");

        Div teamScore = new Div(scoreTeam1, scoreTeam2);
        teamScore.setWidthFull();
        teamScore.setHeight("400");

        pageGame.add(teamName, teamScore);


        return pageGame;

    }

    private void listPlayer(String filterText) {
        //String uri = "http://localhost:8090/Players";
        String baseUri = "https://pingpongplayersservice.herokuapp.com/";
        String uri = baseUri +  "Players";

        //TODO: Autowire the RestTemplate in all the examples
        RestTemplate restTemplate = new RestTemplate();

        if (!StringUtils.isEmpty(filterText)) {
            uri = baseUri +  "PlayersWithName/" + filterText;
        }

        ListOfPlayers result = restTemplate.getForObject(uri, ListOfPlayers.class);

        grid.setItems(result.getPlayers());
    }

}