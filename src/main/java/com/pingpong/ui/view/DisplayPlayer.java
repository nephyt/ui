package com.pingpong.ui.view;

import com.pingpong.basicclass.player.Player;
import com.vaadin.flow.component.html.IFrame;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.StreamResource;

import java.io.ByteArrayInputStream;

public class DisplayPlayer {

    Player player;

    public DisplayPlayer(Player playerToDisplay) {

        player = playerToDisplay;

    }

    public String getPlayerName() {
        return player.getName();
    }

    public IFrame getVictorySong() {
        IFrame frame = new IFrame();

        setupIframe(frame, "480px", "600px", true);

        String emdebSong = player.getYoutubeEmbedVictorySongPath() + "?autoplay=1";

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
