package com.pingpong.ui.view;

import com.pingpong.basicclass.game.Game;
import com.pingpong.basicclass.game.Team;
import com.pingpong.basicclass.game.TeamEnum;
import com.pingpong.basicclass.player.ListOfPlayers;
import com.pingpong.basicclass.player.Player;
import com.pingpong.ui.Constants;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.IFrame;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.util.*;

@Route("")
public class MainView extends VerticalLayout implements KeyNotifier {

    private PlayerEditor editor = new PlayerEditor();

    private Grid<Player> grid;

    private Button addNewBtn;


    Label team1 = new Label();
    Label team2 = new Label();

    Label scoreTeam2 = new Label();
    Label scoreTeam1 = new Label();


    Div playerSelect = new Div();

    PlayerSelector playerSelectorTeamA;
    PlayerSelector playerSelectorTeamB;

    int scoreMaxSelected = 21;

    Game gameInProgess;

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

        List<Grid.Column<Player>> orderColumn = new ArrayList<>();

        orderColumn.add(grid.getColumnByKey("picture"));
        orderColumn.add(grid.getColumnByKey("name"));
        orderColumn.add(grid.getColumnByKey("song"));
        orderColumn.add(grid.getColumnByKey("status"));

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
        Div score = new Div();
        score.getElement().getStyle().set("background-image","url('pingpongtable.png')");

        score.setWidthFull();
        score.setHeight("600px");

        playerSelectorTeamA = new PlayerSelector(listPlayer(""), "Select player(s) team A :");
        playerSelectorTeamB = new PlayerSelector(listPlayer(""), "Select player(s) team B :");

        VerticalLayout scoreMaxDiv = new VerticalLayout();
        scoreMaxDiv.setAlignItems(Alignment.CENTER);
        scoreMaxDiv.setJustifyContentMode(JustifyContentMode.CENTER);


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

        scoreMaxDiv.add(selectScoreMax, scoreMax, btnStartGame);

        playerSelect.add(playerSelectorTeamA, playerSelectorTeamB);
        playerSelect.add(scoreMaxDiv);

        score.add(playerSelect);

        btnStartGame.addClickListener(e -> startGame());

        Div pageGame = new Div();
        pageGame.setWidthFull();


        Div teamName = new Div(team1, team2);
        teamName.setWidthFull();


        // compteur


        Div teamScore = new Div(scoreTeam1, scoreTeam2);
        teamScore.setWidthFull();
        teamScore.setHeight("400");

        pageGame.add(playerSelect, teamName, teamScore);

        pageGame.addClickListener( e -> updateGame(e));

        return pageGame;

    }



    private void updateGame(ClickEvent event) {

        if (gameInProgess != null) {
            System.out.println("YESYEYS");

            if (event.getClickCount() == 1) {
                gameInProgess.getTeamA().incrementScore();
            } else if (event.getClickCount() == 2) {
                gameInProgess.getTeamA().decrementScore(); // undo the count 1
                gameInProgess.getTeamB().incrementScore();
            }

            gameInProgess.updateGame();

            team1.setText(playerSelectorTeamA.getLabelTeam());
            team2.setText(playerSelectorTeamB.getLabelTeam());

            scoreTeam1.setText(gameInProgess.getTeamA().getScore() + "");
            scoreTeam2.setText(gameInProgess.getTeamB().getScore() + "");

        }


    }

    private void startGame() {

        playerSelect.setVisible(false);


        Team teamA = playerSelectorTeamA.createTeam(true);
        Team teamB = playerSelectorTeamB.createTeam(false);

        Game game = new Game(teamA, teamB,scoreMaxSelected);

        gameInProgess = createGame(game);




    }

    private void setupIframe(IFrame frame, String height, String witdth, boolean isVisible) {
        frame.setHeight("315px");
        frame.setWidth("560px");
        frame.setAllow("accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture");
        frame.getElement().setAttribute("allowfullscreen", true);
        frame.getElement().setAttribute("frameborder", "0");
        frame.setVisible(isVisible);
    }

    private void showVideo(IFrame frame) {
        frame .setSrc("https://www.youtube.com/embed/e8X3ACToii0?autoplay=1");
        frame.setVisible(true);
    }

    private void fillGrid(String filterText) {

        grid.setItems(listPlayer(filterText));
    }

    private Game createGame(Game game) {
        String uri = Constants.SERVICE_GAME_URL +  "createGame";

        //TODO: Autowire the RestTemplate in all the examples
        RestTemplate restTemplate = new RestTemplate();

        Game savedGame = restTemplate.postForObject(uri, game, Game.class);

        return savedGame;
    }

    private List<Player> listPlayer(String filterText) {
        String uri = Constants.SERVICE_PLAYER_URL +  "Players";

        //TODO: Autowire the RestTemplate in all the examples
        RestTemplate restTemplate = new RestTemplate();

        if (!StringUtils.isEmpty(filterText)) {
            uri = Constants.SERVICE_PLAYER_URL +  "PlayersWithName/" + filterText;
        }

        ListOfPlayers result = restTemplate.getForObject(uri, ListOfPlayers.class);

        return result.getPlayers();
    }

}