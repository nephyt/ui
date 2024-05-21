package com.pingpong.ui.util;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UtilsTest {

    @Test
    void listSoundAvalaible() {
        List<String> list = Utils.listSoundAvailable();
        assertNotEquals(0, list.size());
        assertTrue(list.contains("Super Mario Bros.-Coin Sound Effect.mp3"));
        assertTrue(list.contains("Yoshi's Mlem Sound Effect.mp3"));
    }
}