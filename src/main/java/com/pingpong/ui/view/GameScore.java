package com.pingpong.ui.view;

import com.pingpong.basicclass.enumeration.GameStatus;
import com.pingpong.basicclass.enumeration.TeamEnum;
import com.pingpong.basicclass.game.Game;
import com.pingpong.basicclass.game.TeamState;
import com.pingpong.basicclass.player.Player;
import com.pingpong.basicclass.servicecount.AllServiceCount;
import com.pingpong.basicclass.servicecount.ServiceCount;
import com.pingpong.ui.services.ServicesButtons;
import com.pingpong.ui.services.ServicesRest;
import com.pingpong.ui.thread.ClickThread;
import com.pingpong.ui.util.Utils;
import com.pingpong.ui.web.controller.GameController;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;

import java.util.Map;


public class GameScore extends VerticalLayout {

    HorizontalLayout displayScore = new HorizontalLayout();

    ClickThread clickListener;
    Thread thread;

    HorizontalLayout scoring = new HorizontalLayout();

    Game game;

    private DisplayTeam displayTeamA = new DisplayTeam();
    private DisplayTeam displayTeamB = new DisplayTeam();

    DisplayScore displayScoreTeamA = new DisplayScore();
    DisplayScore displayScoreTeamB = new DisplayScore();

    Registration clickScore;
    Registration clickTeamA;
    Registration clickTeamB;

    PageGame pageGame;

    Button muteUnmuteSounds = new Button("Mute Sounds");

    private Boolean isMute = false;
    Button pauseResumeGame = new Button("Pause Game");
    Button newGame = new Button("New Game");

    AllServiceCount serviceCountStats;

    AudioPlayer pointSoundTeamA = new AudioPlayer();
    AudioPlayer pointSoundTeamB = new AudioPlayer();
    AudioPlayer matchPointSound = new AudioPlayer();

    public boolean isMute() {
        return isMute;
    }

    public GameScore() {
        GameController.setGameScore(this);

        setupAudio(pointSoundTeamA, "Super Mario Bros.-Coin Sound Effect.mp3");
        setupAudio(pointSoundTeamB, "Yoshi's Mlem Sound Effect.mp3");
        setupAudio(matchPointSound, "Legend of Zelda A Link to the Past sound effect - Treasure!.mp3");

        clickListener = new ClickThread(this);
        thread = new Thread(clickListener);
        thread.start();

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


        newGame.setVisible(false);

        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        add(pauseResumeGame);
        add(newGame);
        add(muteUnmuteSounds);
        add(pointSoundTeamA);
        add(pointSoundTeamB);
        add(matchPointSound);

        pauseResumeGame.addClickListener(e-> pauseResumeGame());
        newGame.addClickListener(e-> createNewGame());
        muteUnmuteSounds.addClickListener(e-> muteUnmuteSounds());

        if (Utils.isMute()) {
            muteUnmuteSounds.setText("Unmute Sounds");
            isMute = true;
        }

    }

    public DisplayTeam getDisplayTeamA() {
        return displayTeamA;
    }

    public DisplayTeam getDisplayTeamB() {
        return displayTeamB;
    }


    public void createNewGame() {
        pageGame.showGameSetting();
    }

    public Game getGame() {
        return game;
    }

    public void initGameScore(PageGame pageGame, Game gameToManage, Map<Integer, Player> mapPlayerTeamA, Map<Integer, Player> mapPlayerTeamB) {
        this.game = gameToManage;
        this.pageGame = pageGame;
        this.displayTeamA.setMapIdPlayer(mapPlayerTeamA);
        this.displayTeamB.setMapIdPlayer(mapPlayerTeamB);
        this.serviceCountStats = new AllServiceCount(gameToManage);

        pauseResumeGame.setText("Pause Game");
        newGame.setVisible(false);
    }


    private void muteUnmuteSounds() {
        if (isMute) {
            isMute = false;
            muteUnmuteSounds.setText("Mute Sounds");
        } else {
            isMute = true;
            muteUnmuteSounds.setText("Unmute Sounds");
        }
        Utils.setIsMute(isMute);
    }

    private void setupAudio(AudioPlayer audio, String src) {
        audio.getElement().getStyle().set("display", "none");
        audio.getElement().getStyle().set("display", "none");

        audio.setSource("sounds/" + src);
    }

    private void pauseResumeGame() {
        if (game != null) {
            if (GameStatus.ACTIVE.getCode().equals(game.getGameStatus().getCode())) {
                ServicesButtons.getInstance().pauseGame();
                game.pauseGame();
                game = ServicesRest.saveGame(game);
                ServicesRest.updatePlayersCountService(serviceCountStats);
                serviceCountStats.getServiceCount().clear();
                pauseResumeGame.setText("Resume Game");
                newGame.setVisible(true);
            } else if (GameStatus.PAUSE.getCode().equals(game.getGameStatus().getCode())) {
                ServicesButtons.getInstance().startServerModeButton(game.determineServerState());
                game.resumeGame();
                game = ServicesRest.saveGame(game);
                pauseResumeGame.setText("Pause Game");
                newGame.setVisible(false);
            }
        }
    }

    public void refreshScreen() {
        ServicesButtons.getInstance().startServerModeButton(game.determineServerState());

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

                // update count for service when the game is complete
                ServicesRest.updatePlayersCountService(serviceCountStats);

                pageGame.showWinnerScreen();
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
