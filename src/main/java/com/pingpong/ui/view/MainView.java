package com.pingpong.ui.view;

import com.pingpong.basicclass.player.Player;
import com.pingpong.basicclass.servicecount.AllServiceCount;
import com.pingpong.basicclass.stats.PlayerStats;
import com.pingpong.basicclass.stats.PlayersStats;
import com.pingpong.ui.services.ServicesButtons;
import com.pingpong.ui.services.ServicesRest;
import com.pingpong.ui.util.Utils;
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
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route("")
@PreserveOnRefresh
public class MainView extends VerticalLayout implements KeyNotifier {

    private PlayerEditor editor = new PlayerEditor();

    private Grid<Player> grid;

    private Button addNewBtn;

    PlayersStats playersStats;
    AllServiceCount serviceCount;


    public MainView() {

        // init cookie for mute
        Utils.isMute();

        Tab tabPlayer = new Tab("Players");
        Tab tabGame = new Tab("Game");
        tabGame.setId("tabGame");
        Tab tabGamePaused = new Tab("Game Paused");
        Tab tabGameHistoric = new Tab("Game Historic");

        Tabs tabs = new Tabs(tabPlayer, tabGamePaused, tabGame, tabGameHistoric);
        tabs.setWidthFull();
        tabs.setFlexGrowForEnclosedTabs(1);

        Div pagePlayers = buildPagePlayers();
        PageGame pageGame = new PageGame();
        PausedGameVIew pageGamePaused = new PausedGameVIew(pageGame, tabs);
        HistoricGameVIew pageGameHistoric = new HistoricGameVIew();

        Map<Tab, Component> tabsToPages = new HashMap<>();
        tabsToPages.put(tabPlayer, pagePlayers);
        tabsToPages.put(tabGamePaused, pageGamePaused);
        tabsToPages.put(tabGame, pageGame);
        tabsToPages.put(tabGameHistoric, pageGameHistoric);

        Div pages = new Div(pagePlayers, pageGamePaused, pageGame, pageGameHistoric);
        pages.setWidthFull();

        tabs.addSelectedChangeListener(event -> {
            tabsToPages.values().forEach(page -> page.setVisible(false));
            Component selectedPage = tabsToPages.get(tabs.getSelectedTab());
            selectedPage.setVisible(true);

            if (tabs.getSelectedIndex() == 0) {
                ServicesButtons.getInstance().standBy();
                fillGrid("");
            }
            if (tabs.getSelectedIndex() == 1) {
                ServicesButtons.getInstance().standBy();
                ((PausedGameVIew)selectedPage).fillGrid();
            }
            if (tabs.getSelectedIndex() == 2) {
                ((PageGame)selectedPage).refreshStatePage();
            }
            if (tabs.getSelectedIndex() == 3) {
                ServicesButtons.getInstance().standBy();
                ((HistoricGameVIew)selectedPage).refreshPage();
            }

        });

        add(tabs, pages);

    }

    private Div buildPagePlayers() {

        addNewBtn = new Button("New player", VaadinIcon.PLUS.create());

        Div pagePlayers = new Div();

        setupGridPlayers();

        TextField filter = new TextField();
        filter.setPlaceholder("Filter by last name");
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> fillGrid(e.getValue()));

        HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);

        pagePlayers.add(actions,grid, editor);
        pagePlayers.setWidthFull();

        // Instantiate and edit new Customer the new button is clicked
        addNewBtn.addClickListener(e -> editor.editCustomer(new Player()));

        // Listen changes made by the editor, refresh data from backend
        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            fillGrid(filter.getValue());
        });

        fillGrid("");

        return pagePlayers;
    }

    private void setupGridPlayers() {

        grid = new Grid<>(Player.class);

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
                StreamResource resource = new StreamResource(player.getName() + "-MainView.jpg", () -> new ByteArrayInputStream(player.getPicture()));

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

        grid.addColumn(player -> {
            PlayerStats stats = playersStats.getPlayerStatsForPlayer(player.getId());

            String result = stats.getNumberOfGamePlayed() + "/";
            result += stats.getNumberOfGameWin() + "/";
            result += stats.getNumberOfGameLost();

            return result;
        }).setHeader("#Games/W/L").setSortable(true).setKey("gamePlayed");


        grid.addColumn(player ->
            serviceCount.getServiceCountForPlayer(player.getId()).toStringBallServe()
        ).setHeader("#BallsServe/W/L").setSortable(true).setKey("ballServe");

        grid.addColumn(player -> {
            PlayerStats stats = playersStats.getPlayerStatsForPlayer(player.getId());

            return Utils.formatTimePlayed(stats.getTimePlayed());

        }).setSortable(true).setHeader("Time Played").setKey("time");


        List<Grid.Column<Player>> orderColumn = new ArrayList<>();

        orderColumn.add(grid.getColumnByKey("picture"));
        orderColumn.add(grid.getColumnByKey("name"));
        orderColumn.add(grid.getColumnByKey("song"));
        orderColumn.add(grid.getColumnByKey("status"));
        orderColumn.add(grid.getColumnByKey("gamePlayed"));
        orderColumn.add(grid.getColumnByKey("ballServe"));
        orderColumn.add(grid.getColumnByKey("time"));

        grid.setColumnOrder(orderColumn);

        grid.setWidthFull();

        // Connect selected Customer to editor or hide if none is selected
        grid.asSingleSelect().addValueChangeListener(e -> {
            editor.editCustomer(e.getValue());
        });
    }

    private void fillGrid(String filterText) {
        if ("".equals(filterText)){
            playersStats = ServicesRest.getPlayersStats();
            serviceCount = ServicesRest.getPlayerCountService();
        }
        grid.setItems(ServicesRest.listPlayer(filterText));
    }


}