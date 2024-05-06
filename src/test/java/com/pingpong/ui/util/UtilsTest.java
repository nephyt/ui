package com.pingpong.ui.util;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UtilsTest {

    @Test
    void listSoundAvalaible() {
        List<String> list = Utils.listSoundAvalaible("src/test/resources/static/sounds");
        assertEquals(2, list.size());
        assertEquals("Super Mario Bros.-Pause Sound Effect.mp3", list.get(0));
        assertEquals("Yoshi's Mlem Sound Effect.mp3", list.get(1));
    }
}