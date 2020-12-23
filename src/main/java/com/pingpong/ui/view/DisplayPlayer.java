package com.pingpong.ui.view;

import com.pingpong.basicclass.player.Player;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.StreamResource;

import java.io.ByteArrayInputStream;

public class DisplayPlayer {

    Player player;

    public DisplayPlayer(Player playerToDisplay) {

        player = playerToDisplay;

    }

    public Image getDisplayPlayer(boolean hasServe) {

        Image imgPlayer = new Image();
        //imgPlayer.setWidthFull();

        imgPlayer.setWidth("200px");
        imgPlayer.setHeight("200px");
        if (player.getPicture() != null) {
            StreamResource resource = new StreamResource("dummyImageName.jpg", () -> new ByteArrayInputStream(player.getPicture()));

            imgPlayer.setSrc(resource);
        }
        //else {
         //   imgPlayer.setSrc("dummyImg.png");
       // }

        if (hasServe) {
            imgPlayer.getElement().setAttribute("border", "5px solid #73AD21");
        }


        return imgPlayer;

    }

}
