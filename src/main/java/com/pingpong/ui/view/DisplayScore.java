package com.pingpong.ui.view;

import com.pingpong.ui.util.Utils;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;

public class DisplayScore {

    Image scoreDizaine;
    Image scoreUnit;

    Div score = new Div();

    public DisplayScore() {

        score.setWidth("50%");
        scoreDizaine = getImageScore();
        scoreUnit = getImageScore();

        refreshImageScore(0);

        score.add(scoreDizaine, scoreUnit);

    }

    public Div getScore() {
        return score;
    }

    private Image getImageScore() {
        Image scoreImg = new Image();

        scoreImg.setWidth("50%");

        Utils.disableSelection(scoreImg);

        return scoreImg;
    }

    public void refreshImageScore(int score) {

        String scoreStr = String.valueOf(score);

        if (score < 10) {
            scoreDizaine.setSrc("digits/0.jpg");
            scoreUnit.setSrc("digits/" + scoreStr + ".jpg");
        } else {
            scoreDizaine.setSrc("digits/" + scoreStr.charAt(0) + ".jpg");
            scoreUnit.setSrc("digits/" + scoreStr.charAt(1) +  ".jpg");
        }
    }

}
