package com.pingpong.ui.view;

import com.pingpong.basicclass.player.Player;
import com.pingpong.basicclass.servicecount.AllServiceCount;
import com.pingpong.basicclass.servicecount.ServiceCount;
import com.pingpong.basicclass.stats.PlayerStats;
import com.pingpong.ui.servicesrest.ServicesRest;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.IFrame;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Push
@Route("")
public class MainView extends VerticalLayout implements KeyNotifier {

    private PlayerEditor editor = new PlayerEditor();

    private Grid<Player> grid;

    private Button addNewBtn;

    public MainView() {
      //  add(new Button("Click me", e -> Notification.show("Hello, Spring+Vaadin user!")));


        addNewBtn = new Button("New player", VaadinIcon.PLUS.create());

        Tab tabPlayer = new Tab("Players");
        Tab tabGame = new Tab("Game");

        Div pagePlayers = new Div();

        Div pageGame = buildPageGame();

        pageGame.setVisible(false);


        this.grid = new Grid<>(Player.class);

        grid.removeColumnByKey("creationDate");
        grid.removeColumnByKey("picture");
        grid.removeColumnByKey("victorySongPath");
        grid.removeColumnByKey("youtubeEmbedVictorySongPath");
        grid.removeColumnByKey("id");

        grid.addComponentColumn(player -> {
            Image result = new Image();
            result.setWidth("60px");
            result.setHeight("60px");
            if (player.getPicture() != null) {
                StreamResource resource = new StreamResource("dummyImageName.jpg", () -> new ByteArrayInputStream(player.getPicture()));

                result.setSrc(resource);
            }
            return result;
        }).setHeader("Picture").setKey("picture");

        grid.addComponentColumn(player -> {
            IFrame result = new IFrame();
            result.setWidth("60px");
            result.setHeight("60px");
            if (player.getVictorySongPath() != null) {
                String victorySong = player.getYoutubeEmbedVictorySongPath();

                if (victorySong != null) {
                    result.setSrc(victorySong);
                }

            }
            return result;
        }).setHeader("Victory Song").setKey("song");

        Map<Integer, PlayerStats> playersStats = ServicesRest.getPlayersStats();

        grid.addColumn(player -> {
            PlayerStats stats = playersStats.get(player.getId());

            int result = 0;
            if (stats != null) {
                result = stats.getNumberOfGamePlayed();
            }

            return result;
        }).setHeader("Game played").setKey("gamePlayed");

        grid.addColumn(player -> {
            PlayerStats stats = playersStats.get(player.getId());

            int result = 0;
            if (stats != null) {
                result = stats.getNumberOfGameWin();
            }

            return result;
        }).setHeader("Win").setKey("win");

        grid.addColumn(player -> {
            PlayerStats stats = playersStats.get(player.getId());

            int result = 0;
            if (stats != null) {
                result = stats.getNumberOfGameLost();
            }

            return result;
        }).setHeader("Lost").setKey("lost");

        AllServiceCount serviceCount = ServicesRest.getPlayerCountService();

        grid.addColumn(player -> {
            ServiceCount stats = serviceCount.getServiceCountForPlayer(player.getId());

            String result = stats.getBallServe() + "/";
            result += stats.getBallServeWin() + "/";
            result += stats.getBallServeFail();

            return result;
        }).setHeader("#Serve/W/L").setKey("ballServe");

        grid.addColumn(player -> {
            PlayerStats stats = playersStats.get(player.getId());

            long timeInSeconde = 0;
            if (stats != null) {
                timeInSeconde = stats.getTimePlayed();
            }

            long hours = TimeUnit.SECONDS.toHours(timeInSeconde);
            long minutes = TimeUnit.SECONDS.toMinutes(timeInSeconde) -
                           TimeUnit.HOURS.toSeconds(hours);

            long secondes = timeInSeconde - TimeUnit.HOURS.toSeconds(hours) - TimeUnit.MINUTES.toSeconds(minutes);

            return String.format("%02d:%02d:%02d",
                    hours,
                    minutes,
                    secondes);

        }).setHeader("Time").setKey("time");



        List<Grid.Column<Player>> orderColumn = new ArrayList<>();

        orderColumn.add(grid.getColumnByKey("picture"));
        orderColumn.add(grid.getColumnByKey("name"));
        orderColumn.add(grid.getColumnByKey("song"));
        orderColumn.add(grid.getColumnByKey("status"));
        orderColumn.add(grid.getColumnByKey("gamePlayed"));
        orderColumn.add(grid.getColumnByKey("win"));
        orderColumn.add(grid.getColumnByKey("lost"));
        orderColumn.add(grid.getColumnByKey("ballServe"));
        orderColumn.add(grid.getColumnByKey("time"));



        grid.setColumnOrder(orderColumn);


        TextField filter = new TextField();
        filter.setPlaceholder("Filter by last name");
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> fillGrid(e.getValue()));

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

        // Connect selected Customer to editor or hide if none is selected
        grid.asSingleSelect().addValueChangeListener(e -> {
            editor.editCustomer(e.getValue());
        });

        // Instantiate and edit new Customer the new button is clicked
        addNewBtn.addClickListener(e -> editor.editCustomer(new Player()));

        // Listen changes made by the editor, refresh data from backend
        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            fillGrid(filter.getValue());
        });

        fillGrid("");
    }

    private Div buildPageGame() {
/*
        byte[] targetArray = FileUtils.readFileToByteArray(new File(player.getPicturePath()));
        StreamResource resource = new StreamResource("dummyImageName.jpg", () -> new ByteArrayInputStream(targetArray));

        image.setSrc(resource);
        image.setVisible(true);
*/

        Div pageGame = new Div();
        pageGame.setWidthFull();

        GameSetting gameSetting = new GameSetting(ServicesRest.listPlayer(""), pageGame);
        gameSetting.setVisible(true);

        pageGame.add(gameSetting);

        return pageGame;

    }

    private void fillGrid(String filterText) {

        grid.setItems(ServicesRest.listPlayer(filterText));
    }


}