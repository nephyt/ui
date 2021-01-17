package com.pingpong.ui.view;

import com.pingpong.basicclass.player.Player;
import com.pingpong.ui.util.Utils;
import com.vaadin.flow.component.html.IFrame;
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

    public IFrame getVictorySong(Player playerToDisplay, boolean isMute) {
        IFrame frame = new IFrame();

        setupIframe(frame, "480px", "600px", true);

        String autoPlay = "?autoplay=1";
        if (isMute) {
            autoPlay = "";
        }

        String emdebSong = playerToDisplay.getYoutubeEmbedVictorySongPath() + autoPlay;

        frame.setSrc(emdebSong);

        return frame;

    }

    private void setupIframe(IFrame frame, String height, String witdth, boolean isVisible) {
        frame.setHeight("315px");
        frame.setWidth("560px");
        frame.setAllow("accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture");
        frame.getElement().setAttribute("allowfullscreen", true);
        frame.getElement().setAttribute("frameborder", "0");
        frame.setVisible(isVisible);
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
