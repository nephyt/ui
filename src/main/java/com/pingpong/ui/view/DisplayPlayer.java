package com.pingpong.ui.view;

import com.pingpong.basicclass.player.Player;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.StreamResource;

import java.io.ByteArrayInputStream;

public class DisplayPlayer extends VerticalLayout {

    Player player;

    public DisplayPlayer(Player playerTodisplay) {

        player = playerTodisplay;

        Image imgPlayer = new Image();
        imgPlayer.setWidth("120px");
        imgPlayer.setHeight("240px");
        if (player.getPicture() != null) {
            StreamResource resource = new StreamResource("dummyImageName.jpg", () -> new ByteArrayInputStream(player.getPicture()));

            imgPlayer.setSrc(resource);
        }

        add(imgPlayer);

    }

}
