package com.pingpong.ui.view;

import com.pingpong.basicclass.player.ListOfPlayers;
import com.pingpong.basicclass.player.Player;
import com.pingpong.ui.Constants;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.IFrame;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route("")
public class MainView extends VerticalLayout implements KeyNotifier {

    private PlayerEditor editor = new PlayerEditor();

    private Grid<Player> grid;

    private Button addNewBtn;

    GameSetting gameSetting;

    GameScore gameScore;

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

        Div pageGame = new Div();
        pageGame.setWidthFull();


        // compteur


        GameSetting gameSetting = new GameSetting(listPlayer(""), gameScore);
        gameSetting.setVisible(true);



        pageGame.add(gameSetting, gameScore);


        return pageGame;

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