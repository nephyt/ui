package com.pingpong.ui.thread;

import com.pingpong.basicclass.enumeration.TeamEnum;
import com.pingpong.ui.view.GameScore;
import com.vaadin.flow.component.ClickEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClickThread implements Runnable {

    List<ClickEvent> listClicks = Collections.synchronizedList(new ArrayList<>());

    GameScore gameScore;

    boolean isRunning = true;

    public ClickThread(GameScore gameScore) {
        this.gameScore = gameScore;
    }

    public void addClickEvent(ClickEvent clickEvent) {
        listClicks.add(clickEvent);
    }

    public void stopThread() {
        isRunning = false;
        gameScore = null;
    }

    @Override
    public void run() {

        while (isRunning) {
            if (listClicks.size() == 1) {
                // wait to see if we have a second click
                try {
                    Thread.sleep(600);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (listClicks.size() == 1) {
                    // do action 1 click
                    gameScore.getUI().get().access(() -> {
                        gameScore.updateGame(TeamEnum.TEAM_A);
                   //     gameScore.getUI().get().access(() -> gameScore.getUI().get().push());
                    });

                } else if (listClicks.size() == 2 && listClicks.get(1).getClickCount() == 2){
                    // do action 2 clicks
                    gameScore.getUI().get().access(() -> {
                        gameScore.updateGame(TeamEnum.TEAM_B);
                   //     gameScore.getUI().get().access(() -> gameScore.getUI().get().push());
                    });
                }

                listClicks.clear();

            }
            else {
                synchronized (this) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
