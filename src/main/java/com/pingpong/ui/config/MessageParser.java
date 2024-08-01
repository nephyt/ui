package com.pingpong.ui.config;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class MessageParser {

    public static String BUTTON = "BUTTON";
    public static String LONG_PRESS = "LONG";

    private final Map<String, String> values = new HashMap<>();

    public MessageParser(byte[] payload) {
        String data = new String(payload, Charset.defaultCharset());
        String[] datas = data.split(",");

        for (String s : datas) {
            String[] key_Value = s.split(":");
            values.put(key_Value[0], key_Value[1]);
        }
    }

    public String getButtonPress() {
        return values.get(BUTTON);
    }
    public boolean getButtonLong() {
        return Boolean.parseBoolean(values.get(LONG_PRESS));
    }

    public String toString() {

        return values.toString();
    }

    public boolean is1or3Player() {
        return "0".equals(getButtonPress()) || "2".equals(getButtonPress());
    }

}
