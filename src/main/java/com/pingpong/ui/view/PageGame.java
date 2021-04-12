package com.pingpong.ui.view;

import com.pingpong.basicclass.enumeration.GameStatus;
import com.pingpong.basicclass.game.Game;
import com.pingpong.basicclass.player.Player;
import com.pingpong.ui.services.ServicesButtons;
import com.pingpong.ui.services.ServicesRest;
import com.pingpong.ui.util.Utils;
import com.vaadin.flow.component.html.Div;

import java.util.Map;

public class PageGame extends Div {

    GameSetting gameSetting = new GameSetting(ServicesRest.listPlayer(""), this);

    GameScore gameScore = new GameScore();

    WinnerScreen winnerScreen = new WinnerScreen(this);

    public PageGame() {
        setWidthFull();
        setVisible(false);

        gameSetting.setVisible(true);

        gameScore.setVisible(false);

        add(gameSetting);
        add(gameScore);
        add(winnerScreen);
    }

    public void refreshStatPage() {
        if (gameSetting.isVisible()) {
            ServicesButtons.getInstance().playerSelection();
            if (Utils.getNeedUpdate()) {
                Utils.setNeedUpdate(false);
                gameSetting.getPlayerSelectorTeamA().refreshListPlayer(ServicesRest.listPlayer(""));
            }
        } else if (gameScore.isVisible()) {
            if (GameStatus.ACTIVE.getCode().equals(gameScore.getGame().getGameStatus().getCode())) {
                ServicesButtons.getInstance().startServerModeButton(gameScore.getGame().determineServerState());
            } else if (GameStatus.PAUSE.getCode().equals(gameScore.getGame().getGameStatus().getCode())) {
                ServicesButtons.getInstance().pauseGame();
            }
        } else if (winnerScreen.isVisible()) {
            ServicesButtons.getInstance().startModeWinner(gameScore.getGame());
        }
    }

    public void initialiseGameScore(Game gameToManage, Map<Integer, Player> mapPlayerTeamA, Map<Integer, Player> mapPlayerTeamB) {

        gameScore.initGameScore(this, gameToManage, mapPlayerTeamA, mapPlayerTeamB);
        gameScore.refreshScreen();
    }

    public void showGameSetting() {
        ServicesButtons.getInstance().playerSelection();

        gameScore.setVisible(false);
        gameSetting.setVisible(true);
        remove(winnerScreen);
    }

    public void showGameScore() {
        ServicesButtons.getInstance().startMatch(gameScore.getGame().determineServerState());

        gameSetting.setVisible(false);
        gameScore.setVisible(true);
        remove(winnerScreen);
    }

    public void showWinnerScreen() {
        ServicesButtons.getInstance().startModeWinner(gameScore.getGame());

        gameScore.setVisible(false);
        add(winnerScreen);
        winnerScreen.showWinner(gameScore.getGame(), gameScore.getDisplayTeamA(), gameScore.getDisplayTeamB(), gameScore.isMute());
        winnerScreen.setVisible(true);
    }

}
