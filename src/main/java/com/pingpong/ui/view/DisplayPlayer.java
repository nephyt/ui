package com.pingpong.ui.view;

import com.pingpong.basicclass.player.Player;
import com.pingpong.ui.util.Utils;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.server.StreamResource;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

public class DisplayPlayer {

    private final String IMAGE_STYLE = "user-select: none; width: 200px; height: 200px;";

    Image imgPlayer = new Image();

    Map<Integer, StreamResource> playerPicture = new HashMap<>();

    public DisplayPlayer() {
        Utils.disableSelection(imgPlayer);
    }

    public Image getPlayerImage() {

        return imgPlayer;
    }

    public Image refreshDisplayPlayer(Player player, boolean hasServe, String buttonColor) {

        if (player.getPicture() != null) {
            StreamResource resource = playerPicture.get(player.getId());
            if (resource == null) {
                resource = new StreamResource(player.getName() + "-DisplayPlayer.jpg", () -> new ByteArrayInputStream(player.getPicture()));
                playerPicture.put(player.getId(), resource);
            }
            imgPlayer.setSrc(resource);
        }
        else {
            imgPlayer.setSrc("blank.png");
        }

        if (hasServe) {
            imgPlayer.getElement().setAttribute("style", IMAGE_STYLE + " border:6px solid " + buttonColor);
        } else {
            imgPlayer.getElement().setAttribute("style", IMAGE_STYLE);
        }
        return imgPlayer;

    }

}
