package com.pingpong.ui.view;

import com.pingpong.basicclass.player.Player;
import com.pingpong.ui.util.Utils;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.server.StreamResource;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

public class DisplayPlayer {

    Image imgPlayer = new Image();

    Map<Integer, StreamResource> playerPicture = new HashMap<>();

    public DisplayPlayer() {

        //imgPlayer.setWidthFull();

        imgPlayer.setWidth("200px");
        imgPlayer.setHeight("200px");

        Utils.disableSelection(imgPlayer);

    }



    public Image getPlayerImage() {

        return imgPlayer;
    }

    public Image refreshDisplayPlayer(Player player, boolean hasServe) {

        if (player.getPicture() != null) {
            StreamResource resource = playerPicture.get(player.getId());
            if (resource == null) {
                resource = new StreamResource("dummyImageName.jpg", () -> new ByteArrayInputStream(player.getPicture()));
                playerPicture.put(player.getId(), resource);
            }
            imgPlayer.setSrc(resource);
        }
        else {
            imgPlayer.setSrc("blank.png");
        }

        if (hasServe) {
            imgPlayer.getElement().setAttribute("border", "5px solid #73AD21");
        } else {
            imgPlayer.getElement().removeAttribute("border");
        }
        return imgPlayer;

    }

}
