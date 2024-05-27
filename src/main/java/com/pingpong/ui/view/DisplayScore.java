package com.pingpong.ui.view;

import com.pingpong.ui.util.Utils;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;

public class DisplayScore {

    Image scoreDizaine = new Image();
    Image scoreUnit = new Image();

    Div score = new Div();

    public DisplayScore() {

        score.setWidth("50%");

        setupImageScore(scoreDizaine);
        setupImageScore(scoreUnit);

        refreshImageScore(0);
        score.add(scoreDizaine, scoreUnit);
    }

    public Div getScore() {
        return score;
    }

    private void setupImageScore(Image scoreImg) {
        scoreImg.setWidth("50%");
        Utils.disableSelection(scoreImg);
    }

    public void refreshImageScore(int score) {
        if (score < 10) {
            scoreDizaine.setSrc(Utils.PATH_DIGITS + "0" + Utils.EXTENSION_DIGITS);
            scoreUnit.setSrc(Utils.PATH_DIGITS + score + Utils.EXTENSION_DIGITS);
        } else {
            scoreDizaine.setSrc(Utils.PATH_DIGITS + (score/10) + Utils.EXTENSION_DIGITS);
            scoreUnit.setSrc(Utils.PATH_DIGITS + (score%10) + Utils.EXTENSION_DIGITS);
        }
    }

}
