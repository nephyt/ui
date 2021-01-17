package com.pingpong.ui.util;

import com.vaadin.flow.server.VaadinService;

import javax.servlet.http.Cookie;

public class Utils {


    public static void disableSelection(com.vaadin.flow.component.Component scoreImg) {
        scoreImg.getElement().getStyle().set("user-select", "none");
        scoreImg.getElement().getStyle().set("-o-user-select", "none");
        scoreImg.getElement().getStyle().set("-moz-user-select", "none");
        scoreImg.getElement().getStyle().set("-khtml-user-select", "none");
        scoreImg.getElement().getStyle().set("-webkit-user-select", "none");
    }


    public static Cookie getCookieByName(String name, String defaultValue) {
        // Fetch all cookies from the request
        Cookie[] cookies = VaadinService.getCurrentRequest().getCookies();

        // Iterate to find cookie by its name
        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                return cookie;
            }
        }

        Cookie myCookie = new Cookie(name, defaultValue);
        myCookie.setMaxAge(60 * 60 * 3); // define after how many *seconds* the cookie should expire
        myCookie.setPath("/"); // single slash means the cookie is set for your whole application.

        return myCookie;
    }

}
