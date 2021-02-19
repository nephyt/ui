package com.pingpong.ui.thread;

import com.pingpong.basicclass.servicecount.UpdatePlayer;
import com.pingpong.ui.services.ServicesRest;

public class ServiceCountThread implements Runnable {

    UpdatePlayer updatePlayer;

    public ServiceCountThread(int playerId, boolean win) {
        updatePlayer = new UpdatePlayer(playerId, win);
    }

    @Override
    public void run() {
        ServicesRest.updatePlayerCountService(updatePlayer);
    }
}
