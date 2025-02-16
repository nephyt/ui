package com.pingpong.ui.util;

import com.pingpong.ui.Constants;
import com.vaadin.flow.component.html.IFrame;
import com.vaadin.flow.server.VaadinService;
import jakarta.servlet.http.Cookie;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class Utils {

    public static String PATH_DIGITS_DIZAINES = "digits/Normal/dizaines/";
    public static String PATH_DIGITS_UNITS = "digits/Normal/units/";

    public static String MARGIN_DIGITS = "23%";
    public static String EXTENSION_DIGITS = ".png";

    private static Boolean isMute = null;

    private static Boolean needUpdate = false;

    private static final List<String> filenames = new ArrayList<>();
    private static final List<String> filenamesAdult = new ArrayList<>();
    private static final List<String> filenamesWithRandom = new ArrayList<>();;

    private static boolean allScoringSound = false;

    private static boolean useVictorySongForMatchPoint = false;



    private static int timeVictorySongForMatchPoint = 5;

    public static String RANDOM = "Random";

    static {

        filenames.add("1-UP.mp3");
        filenames.add("Bazinga.mp3");
        filenames.add("DingSoundEffect.mp3");
        filenames.add("Everything Is AWESOME!.mp3");
        filenames.add("Chuis Bo.mp3");
        filenames.add("HERE WE GO SOUND EFFECT (MARIO).mp3");
        filenames.add("I'm On A Boat.mp3");
        filenames.add("LetsGoToTheBeach.mp3");
        filenames.add("Nelson HaHa sound effect.mp3");
        filenames.add("PikaCheeringSmash 64.mp3");
        filenames.add("PikaCheeringSmash 64 twice.mp3");
        filenames.add("Star Wars-The Imperial March.mp3");
        filenames.add("Super Mario Bros.-Coin Sound Effect.mp3");
        filenames.add("Witch Doctor - Ooh Eeh Ooh .mp3");
        filenames.add("YAHOO SOUND EFFECT (MARIO).mp3");
        filenames.add("Yoshi's Mlem Sound Effect.mp3");
        filenames.add("Whippet.mp3");

        filenamesAdult.add("Jizz In My Pants.mp3");
        filenamesAdult.add("I Just Had Sex.mp3");

        updateScoringSoundList(false);
    }

    private static final Map<String, ConfigScore> folderExtension = new HashMap<>();

    static {
        folderExtension.put("chiffreRouge", new ConfigScore(".png", "digits/chiffreRouge/", "digits/chiffreRouge/", "1%"));
        folderExtension.put("fondNoir", new ConfigScore(".png", "digits/fondNoir/", "digits/fondNoir/","1%"));
        folderExtension.put("flamme", new ConfigScore(".png", "digits/flamme/", "digits/flamme/", "12%"));
        folderExtension.put("Normal", new ConfigScore(".png", "digits/Normal/dizaines/", "digits/Normal/units/","23%"));
        folderExtension.put("original", new ConfigScore(".jpg", "digits/original/", "digits/original/", "15%"));
    }


    public static List<String> listSoundAvailable() {
        return filenames;
    }

    public static List<String> listSoundAvailableWithRandom() {
        return filenamesWithRandom;
    }

    public static void setPathDigits(ConfigScore configScore) {
        PATH_DIGITS_UNITS = configScore.pathUnits();
        PATH_DIGITS_DIZAINES = configScore.pathDizaines();
    }

    public static void setMarginDigits(String marginDigits) {
        MARGIN_DIGITS = marginDigits;
    }

    public static void setExtensionDigits(ConfigScore configScore) {
        EXTENSION_DIGITS = configScore.extension();
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

    public static Set<String> getFolderAvalaible() {
        return folderExtension.keySet();
    }

    public static void setupDigitsWithNewConfig(String folder) {
        ConfigScore configScore = folderExtension.get(folder);

        Utils.setPathDigits(configScore);
        Utils.setExtensionDigits(configScore);
        Utils.setMarginDigits(configScore.margin());
    }

    public static void setAllScoringSound(boolean allScoringSound) {
        Utils.allScoringSound = allScoringSound;
        updateScoringSoundList(Utils.allScoringSound);
    }

    public static boolean isUseVictorySongForMatchPoint() {
        return useVictorySongForMatchPoint;
    }

    public static void setUseVictorySongForMatchPoint(boolean useVictorySongForMatchPoint) {
        Utils.useVictorySongForMatchPoint = useVictorySongForMatchPoint;
    }

    private static void updateScoringSoundList(boolean isAll) {
        filenamesWithRandom.clear();
        if (isAll) {
            filenamesWithRandom.addAll(filenamesAdult);
        }

        filenamesWithRandom.addAll(filenames);

        Collections.sort(filenames);

        filenamesWithRandom.addFirst(RANDOM);

    }

    public static int getTimeVictorySongForMatchPoint() {
        return timeVictorySongForMatchPoint;
    }

    public static void setTimeVictorySongForMatchPoint(int timeVictorySongForMatchPoint) {
        Utils.timeVictorySongForMatchPoint = timeVictorySongForMatchPoint;
    }

    public static void setupIframe(IFrame frame, String height, String witdth, boolean isVisible) {
        frame.setHeight(height);
        frame.setWidth(witdth);
        frame.setAllow("accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture");
        frame.getElement().setAttribute("allowfullscreen", false);
        frame.getElement().setAttribute("frameborder", "0");
        frame.setVisible(isVisible);
    }
}
