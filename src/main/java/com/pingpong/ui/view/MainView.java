package com.pingpong.ui.view;

import com.pingpong.basicclass.player.ListOfPlayers;
import com.pingpong.basicclass.player.Player;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.client.RestTemplate;

@Route("players")
public class MainView extends VerticalLayout {

    Grid<Player> grid;

    public MainView() {
        add(new Button("Click me", e -> Notification.show("Hello, Spring+Vaadin user!")));

        this.grid = new Grid<>(Player.class);
        TextField filter = new TextField();
        filter.setPlaceholder("Filter by last name");
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> listPlayer(e.getValue()));
        add(filter, grid);


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