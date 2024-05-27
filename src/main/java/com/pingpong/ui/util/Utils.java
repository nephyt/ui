package com.pingpong.ui.util;

import com.pingpong.ui.Constants;
import com.vaadin.flow.server.VaadinService;
import jakarta.servlet.http.Cookie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Utils {

    public static String PATH_DIGITS = "digits/jeff3/";
    public static String EXTENSION_DIGITS = ".png";

    private static Boolean isMute = null;

    private static Boolean needUpdate = false;

    private static final List<String> filenames = new ArrayList<>();
    private static final List<String> filenamesWithRandom = new ArrayList<>();;

    public static String RANDOM = "Random";

    static {

        filenames.add("Bazinga.mp3");
        filenames.add("DingSoundEffect.mp3");
        filenames.add("Chuis Bo.mp3");
        filenames.add("HERE WE GO SOUND EFFECT (MARIO).mp3");
        filenames.add("I'm On A Boat.mp3");
        filenames.add("LetsGoToTheBeach.mp3");
        filenames.add("Nelson HaHa sound effect.mp3");
        filenames.add("PikaCheeringSmash 64.mp3");
        filenames.add("PikaCheeringSmash 64 twice.mp3");
        filenames.add("Super Mario Bros.-Coin Sound Effect.mp3");
        filenames.add("Witch Doctor - Ooh Eeh Ooh .mp3");
        filenames.add("YAHOO SOUND EFFECT (MARIO).mp3");
        filenames.add("Yoshi's Mlem Sound Effect.mp3");

        Collections.sort(filenames);

        filenamesWithRandom.add(RANDOM);
        filenamesWithRandom.addAll(filenames);
    }


    public static List<String> listSoundAvailable() {
        return filenames;
    }

    public static List<String> listSoundAvailableWithRandom() {
        return filenamesWithRandom;
    }

    public static void setPathDigits(String pathDigits) {
        PATH_DIGITS = pathDigits;
    }

    public static void setExtensionDigits(String extensionDigits) {
        EXTENSION_DIGITS = extensionDigits;
    }

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

            if (cookies != null) {
                // Iterate to find cookie by its name
                for (Cookie cookie : cookies) {
                    if (name.equals(cookie.getName())) {
                        return cookie;
                    }
                }
            }
        }

        Cookie myCookie = new Cookie(name, defaultValue);
        myCookie.setMaxAge(60 * 60 * 3); // define after how many *seconds* the cookie should expire
        myCookie.setPath("/"); // single slash means the cookie is set for your whole application.
        myCookie.setDomain("localhost");

        return myCookie;
    }

}
