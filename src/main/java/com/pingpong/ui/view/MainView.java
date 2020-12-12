package com.pingpong.ui.view;

import com.pingpong.basicclass.player.ListOfPlayers;
import com.pingpong.basicclass.player.Player;
import com.pingpong.ui.Constants;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.IFrame;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Route("players")
public class MainView extends VerticalLayout {

    private PlayerEditor editor = new PlayerEditor();

    private Grid<Player> grid;

    private Button addNewBtn;

    private IFrame iFrame = new IFrame();

    public MainView() {
      //  add(new Button("Click me", e -> Notification.show("Hello, Spring+Vaadin user!")));


        addNewBtn = new Button("New player", VaadinIcon.PLUS.create());

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

        HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);


        pagePlayers.add(actions,grid, editor);

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

        // Connect selected Customer to editor or hide if none is selected
        grid.asSingleSelect().addValueChangeListener(e -> {
            editor.editCustomer(e.getValue());
        });

        // Instantiate and edit new Customer the new button is clicked
        addNewBtn.addClickListener(e -> editor.editCustomer(new Player()));

        // Listen changes made by the editor, refresh data from backend
        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            listPlayer(filter.getValue());
        });

        listPlayer("");


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


        Button play = new Button("Play");

        // compteur

        Label scoreTeam2 = new Label();
        scoreTeam2.setText("5");

        Label scoreTeam1 = new Label();
        scoreTeam1.setText("5");

        Div teamScore = new Div(scoreTeam1, scoreTeam2);
        teamScore.setWidthFull();
        teamScore.setHeight("400");

        pageGame.add(teamName, teamScore);

//        IFrame iFrame = new IFrame("https://www.youtube.com/embed/dQw4w9WgXcQ");
        iFrame.setHeight("315px");
        iFrame.setWidth("560px");
        iFrame.setAllow("accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture");
        iFrame.getElement().setAttribute("allowfullscreen", true);
        iFrame.getElement().setAttribute("frameborder", "0");

        iFrame.setVisible(false);

        pageGame.add(iFrame, play);

        play.addClickListener(e -> showVideo());

        return pageGame;

    }

    private void showVideo() {
        iFrame .setSrc("https://www.youtube.com/embed/e8X3ACToii0?autoplay=1");

        iFrame.setVisible(true);

    }

    private void listPlayer(String filterText) {
        String uri = Constants.SERVICE_URL +  "Players";

        //TODO: Autowire the RestTemplate in all the examples
        RestTemplate restTemplate = new RestTemplate();

        if (!StringUtils.isEmpty(filterText)) {
            uri = Constants.SERVICE_URL +  "PlayersWithName/" + filterText;
        }

        ListOfPlayers result = restTemplate.getForObject(uri, ListOfPlayers.class);

        grid.setItems(result.getPlayers());
    }

}