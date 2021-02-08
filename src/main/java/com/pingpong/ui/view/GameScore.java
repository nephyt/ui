package com.pingpong.ui.view;

import com.pingpong.basicclass.enumeration.GameStatus;
import com.pingpong.basicclass.enumeration.TeamEnum;
import com.pingpong.basicclass.game.Game;
import com.pingpong.basicclass.game.TeamState;
import com.pingpong.basicclass.servicecount.AllServiceCount;
import com.pingpong.basicclass.servicecount.ServiceCount;
import com.pingpong.ui.Constants;
import com.pingpong.ui.servicesrest.ServicesRest;
import com.pingpong.ui.thread.ClickThread;
import com.pingpong.ui.util.Utils;
import com.pingpong.ui.web.controller.GameController;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.shared.Registration;

import javax.servlet.http.Cookie;


public class GameScore extends VerticalLayout {

    HorizontalLayout displayScore = new HorizontalLayout();

    ClickThread clickListener;
    Thread thread;

    HorizontalLayout scoring = new HorizontalLayout();

    public Game getGame() {
        return game;
    }

    Game game;

    DisplayTeam displayTeamA;
    DisplayTeam displayTeamB;

    DisplayScore displayScoreTeamA = new DisplayScore();
    DisplayScore displayScoreTeamB = new DisplayScore();

    Registration clickScore;
    Registration clickTeamA;
    Registration clickTeamB;

    Div pageGame;


    Button muteUnmuteSounds;
    Boolean isMute = false;
    Button pauseResumeGame;

    AllServiceCount serviceCountStats;

    AudioPlayer pointSoundTeamA = new AudioPlayer();
    AudioPlayer pointSoundTeamB = new AudioPlayer();
    AudioPlayer matchPointSound = new AudioPlayer();

    public GameScore(Div pageGame, Game gameToManage, DisplayTeam displayTeamA, DisplayTeam displayTeamB) {
        this.game = gameToManage;
        this.pageGame = pageGame;
        this.displayTeamA = displayTeamA;
        this.displayTeamB = displayTeamB;
        this.serviceCountStats = new AllServiceCount(gameToManage);

        setupAudio(pointSoundTeamA, "Super Mario Bros.-Coin Sound Effect.mp3");
        setupAudio(pointSoundTeamB, "Yoshi's Mlem Sound Effect.mp3");
        setupAudio(matchPointSound, "Legend of Zelda A Link to the Past sound effect - Treasure!.mp3");

        clickListener = new ClickThread(this);
        thread = new Thread(clickListener);
        thread.start();

        GameController.setGameScore(this);

        setWidthFull();
        displayScore.setWidthFull();

        displayScore.add(displayTeamA);

        // Table ping pong
        scoring.setAlignItems(Alignment.CENTER);
        scoring.setJustifyContentMode(JustifyContentMode.CENTER);
        scoring.setWidth("50%");
        scoring.getElement().getStyle().set("background-image","url('pingpongtable.png')");
        scoring.getElement().getStyle().set("background-repeat","no-repeat");
        scoring.getElement().getStyle().set("background-size","100% 100%");

        scoring.add(displayScoreTeamA.getScore());
        scoring.add(displayScoreTeamB.getScore());

        displayScore.add(scoring);


        displayScore.add(displayTeamB);


        add(displayScore);

        clickScore = scoring.addClickListener( e -> addClick(e));

        clickTeamA = displayTeamA.addClickListener(e -> updateGame(TeamEnum.TEAM_A));
        clickTeamB = displayTeamB.addClickListener(e -> updateGame(TeamEnum.TEAM_B));

        pauseResumeGame = new Button("Pause Game");

        muteUnmuteSounds = new Button("Mute Sounds");

        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        add(pauseResumeGame);
        add(muteUnmuteSounds);
        add(pointSoundTeamA);
        add(pointSoundTeamB);
        add(matchPointSound);

        pauseResumeGame.addClickListener(e-> pauseResumeGame());
        muteUnmuteSounds.addClickListener(e-> muteUnmuteSounds());

        Cookie cookie = Utils.getCookieByName(Constants.COOKIE_MUTE, "false");
        if (Boolean.valueOf(cookie.getValue())) {
            muteUnmuteSounds.setText("Unmute Sounds");
            isMute = true;
        }

        ServicesRest.startMatchButton();

    }


    private void muteUnmuteSounds() {
        Cookie cookie = Utils.getCookieByName(Constants.COOKIE_MUTE, isMute.toString());

        if (isMute) {
            isMute = false;
            muteUnmuteSounds.setText("Mute Sounds");
        } else {
            isMute = true;
            muteUnmuteSounds.setText("Unmute Sounds");
        }
        cookie.setValue(isMute.toString());
        VaadinService.getCurrentResponse().addCookie(cookie);
    }

    private void setupAudio(AudioPlayer audio, String src) {
        audio.getElement().getStyle().set("display", "none");
        audio.getElement().getStyle().set("display", "none");

        audio.setSource("sounds/" + src);
    }

    private void pauseResumeGame() {
        if (game != null) {
            if (GameStatus.ACTIVE.getCode().equals(game.getGameStatus().getCode())) {
                game.pauseGame();
                game = ServicesRest.saveGame(game);
                pauseResumeGame.setText("Resume Game");
            } else if (GameStatus.PAUSE.getCode().equals(game.getGameStatus().getCode())) {
                game.resumeGame();
                game = ServicesRest.saveGame(game);
                pauseResumeGame.setText("Pause Game");
            }
        }
    }

    public void refreshScreen() {

        displayTeamA.refreshTeam(game.getTeamStateA(), TeamEnum.TEAM_A);

        // Ici score

        displayScoreTeamA.refreshImageScore(game.getScoreTeamA());
        displayScoreTeamB.refreshImageScore(game.getScoreTeamB());


        displayTeamB.refreshTeam(game.getTeamStateB(), TeamEnum.TEAM_B);
    }



    private Label generateLabelScore(int score) {
        Label scoreLabel = new Label("");
        String scoreStr = "";
        if (score < 10) {
            scoreStr = "0";
        }
        scoreStr += score;
        scoreLabel.setText(scoreStr);

        return scoreLabel;
    }

    private void addClick(ClickEvent event) {

        if (game != null) {
            synchronized (clickListener) {
                clickListener.addClickEvent(event);
                clickListener.notify();
            }
        }
    }

    public void updateGame(TeamEnum teamScored) {

        if (game != null && GameStatus.ACTIVE.getCode().equals(game.getGameStatus().getCode())) {
            if (TeamEnum.TEAM_A.getCode().equals(teamScored.getCode() )) {
                updateServiceCount(game.getTeamStateA(), game.getTeamStateB());

                game.incrementScoreTeamA();
                game.updateGame();
            }
            else if (TeamEnum.TEAM_B.getCode().equals(teamScored.getCode() )) {
                updateServiceCount(game.getTeamStateB(), game.getTeamStateA());

                game.incrementScoreTeamB();
                game.updateGame();
            }

            // done after the click or double click to end the game correctly
            game.updateGameIfFinish();


            if (game.getTeamWinnerId() != null) {
                clickScore.remove();
                clickTeamA.remove();
                clickTeamB.remove();

                synchronized (clickListener) {
                    clickListener.stopThread();
                    clickListener.notify();
                }

                // update count for service when the game is completec
                ServicesRest.updatePlayersCountService(serviceCountStats);

                WinnerScreen winnerScreen = new WinnerScreen(pageGame, isMute);
                winnerScreen.showWinner(game, displayTeamA, displayTeamB);
            } else {
                game = ServicesRest.saveGame(game); // save state in DB

                if (game.isMatchPoint()) {
                    playSound(matchPointSound);
                } else {
                    if (teamScored.equals(TeamEnum.TEAM_A)) {
                        playSound(pointSoundTeamA);
                    } else {
                        playSound(pointSoundTeamB);
                    }
                }
                refreshScreen();
            }
        }
    }

    private void playSound(AudioPlayer audio) {
        if (!isMute) {
            audio.getElement().callJsFunction("play");
        }
    }

    private void updateServiceCount(TeamState teamScored, TeamState teamLost) {
        boolean winServe = false;
        Integer server = teamLost.getServer();
        if (teamScored.hasService()) {
            winServe = true;
            server = teamScored.getServer();
        }

        ServiceCount  serviceCount = serviceCountStats.getServiceCountForPlayer(server);
        serviceCount.incrementCounter(winServe);
        serviceCountStats.putServiceCount(serviceCount);

     //   new ServiceCountThread(server, winServe).run();
     //   new Thread(new ServiceCountThread(server, winServe)).start();
    }
}
