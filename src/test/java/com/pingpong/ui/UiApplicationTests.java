package com.pingpong.ui;

import com.pingpong.ui.util.Utils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UiApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	public void formatTimePlayedTest() {

		int timeInSeconde = 60;

		assertEquals("00:01:00", Utils.formatTimePlayed(timeInSeconde));

		timeInSeconde = 120;
		assertEquals("00:02:00", Utils.formatTimePlayed(timeInSeconde));

		timeInSeconde = 121;
		assertEquals("00:02:01", Utils.formatTimePlayed(timeInSeconde));

		timeInSeconde = 3600;
		assertEquals("01:00:00", Utils.formatTimePlayed(timeInSeconde));

		timeInSeconde = 3660;
		assertEquals("01:01:00", Utils.formatTimePlayed(timeInSeconde));

		timeInSeconde = 3673;
		assertEquals("01:01:13", Utils.formatTimePlayed(timeInSeconde));

	}


}
