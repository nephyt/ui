package com.pingpong.ui.view;

import com.pingpong.basicclass.player.ListOfPlayers;
import com.pingpong.basicclass.player.Player;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Route
public class MainView extends VerticalLayout {

    public MainView() {
        add(new Button("Click me", e -> Notification.show("Hello, Spring+Vaadin user!")));


        final String uri = "http://localhost:8090/Players";

        //TODO: Autowire the RestTemplate in all the examples
        RestTemplate restTemplate = new RestTemplate();

        ListOfPlayers result = restTemplate.getForObject(uri, ListOfPlayers.class);
        System.out.println(result.getPlayers().size());
        System.out.println(result.getPlayers().get(1).getName());
        System.out.println(result.getPlayers().get(0).getName());
    }
}