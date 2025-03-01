package com.pingpong.ui.view;

import com.pingpong.basicclass.enumeration.GameStatus;
import com.pingpong.basicclass.enumeration.TeamEnum;
import com.pingpong.basicclass.game.Game;
import com.pingpong.basicclass.game.MatchPointInfo;
import com.pingpong.basicclass.game.TeamState;
import com.pingpong.basicclass.player.Player;
import com.pingpong.basicclass.servicecount.AllServiceCount;
import com.pingpong.ui.services.ServicesButtons;
import com.pingpong.ui.services.ServicesRest;
import com.pingpong.ui.util.Utils;
import com.pingpong.ui.web.controller.GameController;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.IFrame;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;

import java.util.Map;
import java.util.Stack;


public class GameScore extends VerticalLayout {

    HorizontalLayout displayScore = new HorizontalLayout();

    //ClickThread clickListener;
    //Thread thread;

    HorizontalLayout scoring = new HorizontalLayout();

    IFrame matchPointYoutube = new IFrame();

    Game game;

    private final DisplayTeam displayTeamA = new DisplayTeam();
    private final DisplayTeam displayTeamB = new DisplayTeam();

    DisplayScore displayScoreTeamA = new DisplayScore();
    DisplayScore displayScoreTeamB = new DisplayScore();

    Registration clickScore;
    Registration clickTeamA;
    Registration clickTeamB;

    PageGame pageGame;

    Button muteUnmuteSounds = new Button("Mute Sounds");

    private Boolean isMute = false;
    Button pauseResumeGame = new Button("Pause Game");
    Button undo = new Button("Undo");
    Button newGame = new Button("New Game");

    HorizontalLayout buttonsDiv = new HorizontalLayout(pauseResumeGame, undo, newGame, muteUnmuteSounds);

    AllServiceCount serviceCountStats;

    AudioPlayer pointSoundTeamA = new AudioPlayer();
    AudioPlayer pointSoundTeamB = new AudioPlayer();
    AudioPlayer matchPointSoundA = new AudioPlayer();
    AudioPlayer matchPointSoundB = new AudioPlayer();
    AudioPlayer undoSound = new AudioPlayer();
    AudioPlayer pauseSound = new AudioPlayer();

    Stack<TeamEnum> scoringHistory = new Stack<>();

    public boolean isMute() {
        return isMute;
    }

    public GameScore() {

        System.out.println("Set game score a this dans GameScore");
        GameController.setGameScore(this);

        setupAudio(matchPointSoundA, "Legend of Zelda A Link to the Past sound effect - Treasure!.mp3");
        setupAudio(matchPointSoundB, "Final Fantasy Victory Fanfare.mp3");
        setupAudio(undoSound, "Bowser Laugh Epic Sound FX.mp3");
        setupAudio(pauseSound, "Super Mario Bros.-Pause Sound Effect.mp3");

//        clickListener = new ClickThread(this);
//        thread = new Thread(clickListener);
//        thread.start();

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

//        clickScore = scoring.addClickListener( e -> addClick(e));

        clickTeamA = displayTeamA.addClickListener(e -> updateGame(TeamEnum.TEAM_A));
        clickTeamB = displayTeamB.addClickListener(e -> updateGame(TeamEnum.TEAM_B));

        displayTeamA.setId("displayTeamA");
        displayTeamB.setId("displayTeamB");

        newGame.setVisible(false);

        Utils.setupIframe(matchPointYoutube, "0px","0px", true);


        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        add(buttonsDiv);


        add(pointSoundTeamA);
        add(pointSoundTeamB);
        add(matchPointSoundA);
        add(matchPointSoundB);
        add(undoSound);
        add(pauseSound);
        add(matchPointYoutube);

        pauseResumeGame.addClickListener(e-> pauseResumeGame());
        undo.addClickListener(e-> undoPoint());
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


        if (game.getScoringSoundTeamA() != null) {
            setupScoringAudio(pointSoundTeamA,  game.getScoringSoundTeamA());
        } else {
            setupScoringAudio(pointSoundTeamA, "Super Mario Bros.-Coin Sound Effect.mp3");
        }

        if (game.getScoringSoundTeamB() != null) {
            setupScoringAudio(pointSoundTeamB, game.getScoringSoundTeamB());
        } else {
            setupScoringAudio(pointSoundTeamB, "Yoshi's Mlem Sound Effect.mp3");
        }

        scoringHistory.clear();
        pauseResumeGame.setText("Pause Game");
        scoring.getElement().getStyle().set("background-image","url('pingpongtable.png')");
        undo.setVisible(true);
        newGame.setVisible(false);
    }

    public void playMatchPoint(Player playerToDisplay) {

        String emdebSong = playerToDisplay.getYoutubeEmbedVictorySongPathForMatchPoint(Utils.getTimeVictorySongForMatchPoint());

        remove(matchPointYoutube);
        matchPointYoutube.setSrc(emdebSong);
        add(matchPointYoutube);

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
        audio.setSource("sounds/" + src);
    }

    private void setupScoringAudio(AudioPlayer audio, String src) {
        audio.getElement().getStyle().set("display", "none");
        audio.setSource("scoringSounds/" + src);
    }

    public void pauseResumeGame() {
        if (game != null) {
            if (GameStatus.ACTIVE.getCode().equals(game.getGameStatus().getCode())) {
                playSound(pauseSound);
                ServicesButtons.getInstance().pauseGame();
                game.pauseGame();
                scoringHistory.clear();
                game = ServicesRest.saveGame(game);
                ServicesRest.updatePlayersCountService(serviceCountStats);
                serviceCountStats.getServiceCount().clear();
                pauseResumeGame.setText("Resume Game");
                newGame.setVisible(true);
                undo.setVisible(false);
                scoring.getElement().getStyle().set("background-image","url('pause.jpg')");

            } else if (GameStatus.PAUSE.getCode().equals(game.getGameStatus().getCode())) {
                playSound(pauseSound);
                ServicesButtons.getInstance().startServerModeButton(game.determineServerState());
                game.resumeGame();
                game = ServicesRest.saveGame(game);
                pauseResumeGame.setText("Pause Game");
                newGame.setVisible(false);
                undo.setVisible(true);
                undo.setEnabled(!scoringHistory.isEmpty());
                scoring.getElement().getStyle().set("background-image","url('pingpongtable.png')");
            }
        }
    }

    public void refreshScreen(boolean isInit) {
        if (!isInit) {
            // it will be done later
            ServicesButtons.getInstance().startServerModeButton(game.determineServerState());
        }

        displayTeamA.refreshTeam(game.getTeamStateA(), TeamEnum.TEAM_A);

        // Ici score
        displayScoreTeamA.refreshImageScore(game.getScoreTeamA());
        displayScoreTeamB.refreshImageScore(game.getScoreTeamB());


        displayTeamB.refreshTeam(game.getTeamStateB(), TeamEnum.TEAM_B);

        undo.setEnabled(!scoringHistory.empty());
    }

//    private void addClick(ClickEvent event) {
//
//        if (game != null) {
//            synchronized (clickListener) {
//                clickListener.addClickEvent(event);
//                clickListener.notify();
//            }
//        }
//    }

    public void undoPoint() {

        if (!scoringHistory.empty()) {
            if (game != null && GameStatus.ACTIVE.getCode().equals(game.getGameStatus().getCode())) {
                playSound(undoSound);

                serviceCountStats.undoServiceCount();

                game.undo(scoringHistory.pop());
                game.determineServerState();

                refreshScreen(false);
            }
        }
    }

    public void updateGame(TeamEnum teamScored) {


        if (game != null && GameStatus.ACTIVE.getCode().equals(game.getGameStatus().getCode())) {

            TeamEnum hasServe;
            Integer server;
            Integer receiver;

            if (TeamEnum.TEAM_A.getCode().equals(teamScored.getCode() )) {
                updateServiceCount(game.getTeamStateA(), game.getTeamStateB());

                game.incrementScoreTeamA();

            }
            else if (TeamEnum.TEAM_B.getCode().equals(teamScored.getCode() )) {
                updateServiceCount(game.getTeamStateB(), game.getTeamStateA());
                game.incrementScoreTeamB();
            }

            if (game.getTeamStateA().hasService()) {
                hasServe = TeamEnum.TEAM_A;
                server = game.getTeamStateA().getServer();
                receiver = determineReceiver(game.getTeamStateA(), game.getTeamStateB());
            } else {
                hasServe = TeamEnum.TEAM_B;
                server = game.getTeamStateB().getServer();
                receiver = determineReceiver(game.getTeamStateB(), game.getTeamStateA());
            }

            scoringHistory.add(teamScored);
            game.updateGame();

            // done after the click or double click to end the game correctly
            game.updateGameIfFinish();

            if (game.getTeamWinnerId() != null) {

                // update count for service when the game is complete
                ServicesRest.updatePlayersCountService(serviceCountStats);

                pageGame.showWinnerScreen();
            } else {
                game = ServicesRest.saveGame(game); // save state in DB

                MatchPointInfo info = game.isMatchPoint();
                if (info.isMatchPoint()) {
                    if (Utils.isUseVictorySongForMatchPoint()) {
                        playMatchPoint(determninePlayerSoundMatchPoint(info, hasServe, server, receiver, displayTeamA, displayTeamB));
                    } else {
                        if (info.getTeamWithMatchPoint().equals(TeamEnum.TEAM_A)) {
                            playSound(matchPointSoundA);
                        } else {
                            playSound(matchPointSoundB);
                        }
                    }
                } else {
                    if (teamScored.equals(TeamEnum.TEAM_A)) {
                        playSound(pointSoundTeamA);
                    } else {
                        playSound(pointSoundTeamB);
                    }
                }
                refreshScreen(false);
            }
        }
    }

    protected Player determninePlayerSoundMatchPoint(MatchPointInfo info, TeamEnum hasServe, Integer server, Integer receiver, DisplayTeam displayTeamA, DisplayTeam displayTeamB) {
        Player player;
        if (info.getTeamWithMatchPoint().equals(TeamEnum.TEAM_A)) {
            if (hasServe.equals(TeamEnum.TEAM_A)) {
                player = displayTeamA.getPlayerById(server);
            } else {
                player = displayTeamA.getPlayerById(receiver);
            }
        } else {
            if (hasServe.equals(TeamEnum.TEAM_B)) {
                player = displayTeamB.getPlayerById(server);
            } else {
                player = displayTeamB.getPlayerById(receiver);
            }
        }

        return player;
    }

    protected Integer determineReceiver(TeamState teamServe, TeamState teamReceive) {
        Integer receiver;

        int server = teamServe.getServer();
        if (teamServe.getRightPlayer() != null && teamServe.getRightPlayer() == server) {
            receiver = teamReceive.getRightPlayer();
        } else {
            receiver = teamReceive.getLeftPlayer();
        }
        return receiver;
    }

    private void playSound(AudioPlayer audio) {
        if (!isMute) {
            audio.getElement().callJsFunction("play");
        }
    }

    private void updateServiceCount(TeamState teamScored, TeamState teamLost) {
        serviceCountStats.updateServiceCount(teamScored, teamLost);
     //   new ServiceCountThread(server, winServe).run();
     //   new Thread(new ServiceCountThread(server, winServe)).start();
    }
}
