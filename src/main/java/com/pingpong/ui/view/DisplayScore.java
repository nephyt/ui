package com.pingpong.ui.view;

import com.pingpong.ui.util.Utils;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;

import java.util.HashMap;
import java.util.Map;

public class DisplayScore {

//    private static String PATH_DIGITS = "digits/jeff/";
    private static String PATH_DIGITS = "digits/";
//    private static String EXTENSION_DIGITS = ".png";
    private static String EXTENSION_DIGITS = ".jpg";

    Image scoreDizaine = new Image();
    Image scoreUnit = new Image();

    Map<Integer, String> cacheDigitScore = new HashMap<>();

    Div score = new Div();

    public DisplayScore() {

        score.setWidth("50%");

        setupImageScore(scoreDizaine);
        setupImageScore(scoreUnit);

        for (int i = 0; i < 10; ++i) {
            cacheDigitScore.put(i, PATH_DIGITS + i + EXTENSION_DIGITS);
        }

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
            scoreDizaine.setSrc(PATH_DIGITS + "0" + EXTENSION_DIGITS);
            scoreUnit.setSrc(cacheDigitScore.get(score));
        } else {
            scoreDizaine.setSrc(cacheDigitScore.get(score/10));
            scoreUnit.setSrc(cacheDigitScore.get(score%10));
        }
    }

}
