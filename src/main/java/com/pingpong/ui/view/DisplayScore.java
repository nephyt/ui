package com.pingpong.ui.view;

import com.pingpong.ui.util.Utils;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;

import java.util.HashMap;
import java.util.Map;

public class DisplayScore {

    Image scoreDizaine;
    Image scoreUnit;

    Map<Integer, String> cacheDigitScore = new HashMap<>();

    Div score = new Div();

    public DisplayScore() {

        score.setWidth("50%");
        scoreDizaine = getImageScore();
        scoreUnit = getImageScore();

        for (int i = 0; i < 10; ++i) {
            cacheDigitScore.put(i,"digits/" + i + ".jpg");
        }

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
        if (score < 10) {
            scoreDizaine.setSrc("digits/0.jpg");
            scoreUnit.setSrc(cacheDigitScore.get(score));
        } else {
            scoreDizaine.setSrc(cacheDigitScore.get(score/10));
            scoreUnit.setSrc(cacheDigitScore.get(score%10));
        }
    }

}
