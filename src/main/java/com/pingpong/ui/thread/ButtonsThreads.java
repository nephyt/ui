package com.pingpong.ui.thread;

import com.pingpong.basicclass.enumeration.TeamEnum;
import com.pingpong.ui.Constants;
import org.springframework.web.client.RestTemplate;

public class ButtonsThreads extends Thread {

    static RestTemplate restTemplate = new RestTemplate();

    boolean isStartMatch = false;
    boolean isStartServerMode = false;
    boolean isPauseGame = false;
    boolean isStandBy = false;
    boolean isPlayerSelection = false;
    boolean isStartModeWinner = false;

    String serverState = "";
    String server = "";
    String team = "";
    boolean isRunning = true;

    public void stopThread() {
        isRunning = false;
    }

    public void playerSelection() {
        synchronized (this) {
            isPlayerSelection = true;
            notify();
        }
    }

    public void standBy() {
        synchronized (this) {
            isStandBy = true;
            notify();
        }
    }

    public void pauseGame() {
        synchronized (this) {
            isPauseGame = true;
            notify();
        }
    }
    public void startMatch(String server) {
        synchronized (this) {
            isStartMatch = true;
            this.server = server;
            notify();
        }
    }

    public void startModeWinner(TeamEnum teamWinner) {
        synchronized (this) {
            isStartModeWinner = true;
            team = teamWinner.getCode();
            notify();
        }
    }

    public void startServerMode(String serverState) {
        synchronized (this) {
            isStartServerMode = true;
            this.serverState = serverState;
            notify();
        }
    }

    private void standByButton() {
        String uri = Constants.BUTTONS_URL +  "H";
        restTemplate.getForObject(uri, String.class);
    }

    private void startServerModeButton() {
        String uri = Constants.BUTTONS_URL +  "ServerMode=" + serverState + "=State";
        restTemplate.getForObject(uri, String.class);
    }


    private void startModeWinnerButton() {
        String uri = Constants.BUTTONS_URL +  "ModeWinner" + team;
        restTemplate.getForObject(uri, String.class);
    }

    private void startMatchButton() {
        String uri = Constants.BUTTONS_URL +  "StartMatch" + server;
        restTemplate.getForObject(uri, String.class);
    }

    private void playerSelectionButton() {
        String uri = Constants.BUTTONS_URL +  "PlayerSelection";
        restTemplate.getForObject(uri, String.class);
    }

    private void pauseGameButton() {
        String uri = Constants.BUTTONS_URL +  "LoopLightOn";
        restTemplate.getForObject(uri, String.class);
    }



    @Override
    public void run() {

        while (isRunning) {
            if (isStartMatch) {
                isStartMatch = false;
                startMatchButton();
            } else if (isPlayerSelection) {
                isPlayerSelection = false;
                playerSelectionButton();
            } else if (isPauseGame) {
                isPauseGame = false;
                pauseGameButton();
            } else if (isStandBy) {
                isStandBy = false;
                standByButton();
            } else if (isStartModeWinner) {
                isStartModeWinner = false;
                startModeWinnerButton();
            } else if (isStartServerMode) {
                isStartServerMode = false;
                startServerModeButton();
            }else {
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