package com.pingpong.ui.util;

public class Utils {


    public static void disableSelection(com.vaadin.flow.component.Component scoreImg) {
        scoreImg.getElement().getStyle().set("user-select", "none");
        scoreImg.getElement().getStyle().set("-o-user-select", "none");
        scoreImg.getElement().getStyle().set("-moz-user-select", "none");
        scoreImg.getElement().getStyle().set("-khtml-user-select", "none");
        scoreImg.getElement().getStyle().set("-webkit-user-select", "none");
    }

}
