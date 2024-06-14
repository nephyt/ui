package com.pingpong.ui.view;

import com.pingpong.ui.util.Utils;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.dom.Style;


@CssImport("./css/score.css")
public class DisplayScore {

    Image scoreDizaine = new Image();
    Image scoreUnit = new Image();

    Div score = new Div();

    public DisplayScore() {

        score.setWidth("100%");
        score.setHeight("50%");
        score.getStyle().setAlignItems(Style.AlignItems.CENTER);
        score.setClassName("grid-container");


        setupImageScore(scoreDizaine, "image1");
        setupImageScore(scoreUnit, "image2");

        refreshImageScore(0);
        score.add(scoreDizaine, scoreUnit);
    }

    public Div getScore() {
        return score;
    }

    private void setupImageScore(Image scoreImg, String className) {
        scoreImg.setWidth("100%");
        scoreImg.setHeight("100%");

        scoreImg.setClassName(className);
        Utils.disableSelection(scoreImg);

        if ("image2".equals(className)) {
            scoreImg.getStyle().setMarginLeft("-" + Utils.MARGIN_DIGITS);
        } else {
            scoreImg.getStyle().setMarginLeft(Utils.MARGIN_DIGITS);
        }
    }

    public void refreshImageScore(int score) {

        scoreDizaine.getStyle().setMarginLeft(Utils.MARGIN_DIGITS);
        scoreUnit.getStyle().setMarginLeft("-" + Utils.MARGIN_DIGITS);

        if (score < 10) {
            scoreDizaine.setSrc(Utils.PATH_DIGITS_DIZAINES + "0" + Utils.EXTENSION_DIGITS);
            scoreUnit.setSrc(Utils.PATH_DIGITS_UNITS + score + Utils.EXTENSION_DIGITS);
        } else {
            scoreDizaine.setSrc(Utils.PATH_DIGITS_DIZAINES + (score/10) + Utils.EXTENSION_DIGITS);
            scoreUnit.setSrc(Utils.PATH_DIGITS_UNITS + (score%10) + Utils.EXTENSION_DIGITS);
        }
    }

}
