package com.pingpong.ui.util;

import com.pingpong.ui.Constants;
import com.vaadin.flow.server.VaadinService;

import javax.servlet.http.Cookie;
import java.util.concurrent.TimeUnit;

public class Utils {

    private static Boolean isMute = null;

    private static Boolean needUpdate = false;

    public static void setNeedUpdate(Boolean pNeedUpdate) {
        needUpdate = pNeedUpdate;
    }

    public static Boolean getNeedUpdate() {
        return needUpdate;
    }

    public static String formatTimePlayed(long timeInSeconde) {
        long hours = TimeUnit.SECONDS.toHours(timeInSeconde);
        long minutes = TimeUnit.SECONDS.toMinutes(timeInSeconde) -
                TimeUnit.HOURS.toMinutes(hours);

        long secondes = timeInSeconde - TimeUnit.HOURS.toSeconds(hours) - TimeUnit.MINUTES.toSeconds(minutes);

        return String.format("%02d:%02d:%02d",
                hours,
                minutes,
                secondes);
    }

    public static void disableSelection(com.vaadin.flow.component.Component scoreImg) {
        scoreImg.getElement().getStyle().set("user-select", "none");
        scoreImg.getElement().getStyle().set("-o-user-select", "none");
        scoreImg.getElement().getStyle().set("-moz-user-select", "none");
        scoreImg.getElement().getStyle().set("-khtml-user-select", "none");
        scoreImg.getElement().getStyle().set("-webkit-user-select", "none");
    }

    public static void setIsMute(Boolean isMuteToSet) {
        Cookie cookie = Utils.getCookieByName(Constants.COOKIE_MUTE, isMute.toString());
        cookie.setValue(isMuteToSet.toString());
        VaadinService.getCurrentResponse().addCookie(cookie);

        isMute = isMuteToSet;
    }

    public static boolean isMute() {

        if (isMute == null) {
            Cookie cookie = Utils.getCookieByName(Constants.COOKIE_MUTE, "false");
            isMute = Boolean.valueOf(cookie.getValue());
        }

        return isMute;
    }

    public static Cookie getCookieByName(String name, String defaultValue) {
        // Fetch all cookies from the request
        if (VaadinService.getCurrentRequest() != null) {
            Cookie[] cookies = VaadinService.getCurrentRequest().getCookies();

            // Iterate to find cookie by its name
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return cookie;
                }
            }
        }

        Cookie myCookie = new Cookie(name, defaultValue);
        myCookie.setMaxAge(60 * 60 * 3); // define after how many *seconds* the cookie should expire
        myCookie.setPath("/"); // single slash means the cookie is set for your whole application.

        return myCookie;
    }

}
