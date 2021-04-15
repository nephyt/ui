package com.pingpong.ui.services;

import com.pingpong.basicclass.enumeration.TeamEnum;
import com.pingpong.basicclass.game.Game;
import com.pingpong.ui.thread.ButtonsThreads;

public class ServicesButtons  {

    static private ServicesButtons instance = null;

    private ButtonsThreads buttonsThread;

    private ServicesButtons() {
        buttonsThread = new ButtonsThreads();
        buttonsThread.start();
    }

    public static ServicesButtons getInstance() {
        if (instance == null) {
            instance = new ServicesButtons();
        }
        return instance;
    }

    public void standBy() {
        buttonsThread.standBy();
    }

    public void startMatch(String serverStat) {
        buttonsThread.startMatch(serverStat);
    }

    public void playerSelection() {
        buttonsThread.playerSelection();
    }

    public void startModeWinner(Game game) {
        TeamEnum teamWinner = TeamEnum.TEAM_A;
        if (game.getTeamB().getId().equals(game.getTeamWinnerId())) {
            teamWinner = TeamEnum.TEAM_B;
        }
        buttonsThread.startModeWinner(teamWinner);
    }

    public void startServerModeButton(String serverState) {
        buttonsThread.startServerMode(serverState);
    }

    public void pauseGame() {
        buttonsThread.pauseGame();
    }
}
