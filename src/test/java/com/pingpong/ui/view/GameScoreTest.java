package com.pingpong.ui.view;

import com.pingpong.basicclass.enumeration.PlayerStatus;
import com.pingpong.basicclass.enumeration.TeamEnum;
import com.pingpong.basicclass.game.Game;
import com.pingpong.basicclass.game.MatchPointInfo;
import com.pingpong.basicclass.game.Team;
import com.pingpong.basicclass.game.TeamState;
import com.pingpong.basicclass.player.Player;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

//@SpringBootTest(
//        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
//        classes = UiApplication.class)
class GameScoreTest {

    // call les service et cré des Object invalide dans BD
    // oups
    //@Test
    void undoPoint() throws IOException {

        Team a = new Team(1,2);

        byte[] fileContent = null; //Files.readAllBytes(fi.toPath());

        //name, byte[] picture, PlayerStatus status, String victorySongPath
        Player player1 = new Player(1, "joueur1",fileContent, PlayerStatus.ACTIVE, "");
        Player player2 = new Player(2, "joueur2",fileContent, PlayerStatus.ACTIVE, "");
        Player player3 = new Player(10, "joueur3",fileContent, PlayerStatus.ACTIVE, "");
        Player player4 = new Player(20, "joueur4",fileContent, PlayerStatus.ACTIVE, "");

        Map<Integer, Player> mapPlayerA = new HashMap<>();
        mapPlayerA.put(player1.getId(), player1);
        mapPlayerA.put(player2.getId(), player2);

        Map<Integer, Player> mapPlayerB = new HashMap<>();
        mapPlayerB.put(player3.getId(), player3);
        mapPlayerB.put(player4.getId(), player4);

        Team b = new Team(10,20);

        Game game = new Game(a, b, TeamEnum.TEAM_A,11);


        GameScore gameScore = new GameScore();
        gameScore.initGameScore(new PageGame(), game, mapPlayerA, mapPlayerB);


        gameScore.updateGame(TeamEnum.TEAM_A);

        // team A a le service

        assertEquals(1, gameScore.getGame().getScoreTeamA());
        assertEquals(2, gameScore.getGame().getTeamStateA().getRightPlayer());
        assertEquals(1, gameScore.getGame().getTeamStateA().getLeftPlayer());
        assertTrue( gameScore.getGame().getTeamStateA().hasService());
        assertEquals( 1, gameScore.getGame().getTeamStateA().getServer());
        assertFalse( gameScore.getGame().getTeamStateB().hasService());

        gameScore.undoPoint();

        assertEquals(0, gameScore.getGame().getScoreTeamA());
        assertEquals(1, gameScore.getGame().getTeamStateA().getRightPlayer());
        assertEquals(2, gameScore.getGame().getTeamStateA().getLeftPlayer());
        assertTrue( gameScore.getGame().getTeamStateA().hasService());
        assertEquals( 1, gameScore.getGame().getTeamStateA().getServer());
        assertFalse( gameScore.getGame().getTeamStateB().hasService());

        // undo dans le vide
        gameScore.undoPoint();

        assertEquals(0, gameScore.getGame().getScoreTeamA());
        assertEquals(1, gameScore.getGame().getTeamStateA().getRightPlayer());
        assertEquals(2, gameScore.getGame().getTeamStateA().getLeftPlayer());
        assertTrue( gameScore.getGame().getTeamStateA().hasService());
        assertEquals( 1, gameScore.getGame().getTeamStateA().getServer());
        assertFalse( gameScore.getGame().getTeamStateB().hasService());


        // point service A
        gameScore.updateGame(TeamEnum.TEAM_A);
        gameScore.updateGame(TeamEnum.TEAM_A);

        // service rendu a l'équipe B

        gameScore.undoPoint();

        // service devrait retourné Équipe A pour leur deuxieme service

        assertEquals(1, gameScore.getGame().getScoreTeamA());
        assertEquals(0, gameScore.getGame().getScoreTeamB());
        assertEquals(2, gameScore.getGame().getTeamStateA().getRightPlayer());
        assertEquals(1, gameScore.getGame().getTeamStateA().getLeftPlayer());
        assertTrue( gameScore.getGame().getTeamStateA().hasService());
        assertEquals( 1, gameScore.getGame().getTeamStateA().getServer());
        assertFalse( gameScore.getGame().getTeamStateB().hasService());


        gameScore.undoPoint();

        // service devrait retourné Équipe A pour leur premier service

        assertEquals(0, gameScore.getGame().getScoreTeamA());
        assertEquals(0, gameScore.getGame().getScoreTeamB());
        assertEquals(1, gameScore.getGame().getTeamStateA().getRightPlayer());
        assertEquals(2, gameScore.getGame().getTeamStateA().getLeftPlayer());
        assertTrue( gameScore.getGame().getTeamStateA().hasService());
        assertEquals( 1, gameScore.getGame().getTeamStateA().getServer());
        assertFalse( gameScore.getGame().getTeamStateB().hasService());

        // point service 2 point B et 1 point A
        gameScore.updateGame(TeamEnum.TEAM_B);
        gameScore.updateGame(TeamEnum.TEAM_A);
        gameScore.updateGame(TeamEnum.TEAM_B);

        assertEquals(1, gameScore.getGame().getScoreTeamA());
        assertEquals(2, gameScore.getGame().getScoreTeamB());
        assertEquals(2, gameScore.getGame().getTeamStateA().getRightPlayer());
        assertEquals(1, gameScore.getGame().getTeamStateA().getLeftPlayer());
        assertEquals(20, gameScore.getGame().getTeamStateB().getRightPlayer());
        assertEquals(10, gameScore.getGame().getTeamStateB().getLeftPlayer());
        assertTrue( gameScore.getGame().getTeamStateB().hasService());
        assertEquals( 10, gameScore.getGame().getTeamStateB().getServer());
        assertFalse( gameScore.getGame().getTeamStateA().hasService());

        gameScore.undoPoint();

        // premier service de l'équipe B

        assertEquals(1, gameScore.getGame().getScoreTeamA());
        assertEquals(1, gameScore.getGame().getScoreTeamB());
        assertEquals(2, gameScore.getGame().getTeamStateA().getRightPlayer());
        assertEquals(1, gameScore.getGame().getTeamStateA().getLeftPlayer());
        assertEquals(10, gameScore.getGame().getTeamStateB().getRightPlayer());
        assertEquals(20, gameScore.getGame().getTeamStateB().getLeftPlayer());
        assertTrue( gameScore.getGame().getTeamStateB().hasService());
        assertEquals( 10, gameScore.getGame().getTeamStateB().getServer());
        assertFalse( gameScore.getGame().getTeamStateA().hasService());


        gameScore.undoPoint();

        // deuxieme service de l'équipe A

        assertEquals(0, gameScore.getGame().getScoreTeamA());
        assertEquals(1, gameScore.getGame().getScoreTeamB());
        assertEquals(2, gameScore.getGame().getTeamStateA().getRightPlayer());
        assertEquals(1, gameScore.getGame().getTeamStateA().getLeftPlayer());
        assertEquals(10, gameScore.getGame().getTeamStateB().getRightPlayer());
        assertEquals(20, gameScore.getGame().getTeamStateB().getLeftPlayer());
        assertFalse( gameScore.getGame().getTeamStateB().hasService());
        assertEquals(1, gameScore.getGame().getTeamStateA().getServer());
        assertTrue(gameScore.getGame().getTeamStateA().hasService());

    }

    @Test
    void determineReceiver() {

        GameScore gameScore = new GameScore();

        TeamState teamServe = new TeamState();
        TeamState teamReceive = new TeamState();

        teamServe.setRightPlayer(1);
        teamServe.setServer(1);
        teamReceive.setRightPlayer(2);
        assertEquals(2, gameScore.determineReceiver(teamServe, teamReceive));

        teamServe.setRightPlayer(null);
        teamServe.setLeftPlayer(1);
        teamServe.setServer(1);
        teamReceive.setLeftPlayer(2);
        assertEquals(2, gameScore.determineReceiver(teamServe, teamReceive));

        // test en double

        teamServe.setRightPlayer(1);
        teamServe.setLeftPlayer(2);
        teamServe.setServer(1);
        teamReceive.setRightPlayer(3);
        teamReceive.setLeftPlayer(4);
        assertEquals(3, gameScore.determineReceiver(teamServe, teamReceive));

        teamServe.setServer(2);
        assertEquals(4, gameScore.determineReceiver(teamServe, teamReceive));
    }

    @Test
    void determninePlayerSoundMatchPoint() {
        GameScore gameScore = new GameScore();

        MatchPointInfo info = new MatchPointInfo();
        info.setMatchPoint(true);
        info.setTeamWithMatchPoint(TeamEnum.TEAM_A);

        DisplayTeam displayTeamA = new DisplayTeam();
        DisplayTeam displayTeamB = new DisplayTeam();

        Map<Integer, Player> playersA = new HashMap<>();
        playersA.put(1, new Player(1, "1", null, null,""));
        displayTeamA.setMapIdPlayer(playersA);

        Map<Integer, Player> playersB = new HashMap<>();
        playersB.put(1, new Player(2, "2", null, null,""));
        displayTeamB.setMapIdPlayer(playersB);


        assertEquals(1, gameScore.determninePlayerSoundMatchPoint(info, TeamEnum.TEAM_A, 1, 2, displayTeamA, displayTeamB).getId());
        assertEquals(2, gameScore.determninePlayerSoundMatchPoint(info, TeamEnum.TEAM_B, 1, 2, displayTeamA, displayTeamB).getId());

        info.setTeamWithMatchPoint(TeamEnum.TEAM_B);

        assertEquals(2, gameScore.determninePlayerSoundMatchPoint(info, TeamEnum.TEAM_A, 1, 2, displayTeamA, displayTeamB).getId());
        assertEquals(1, gameScore.determninePlayerSoundMatchPoint(info, TeamEnum.TEAM_B, 1, 2, displayTeamA, displayTeamB).getId());
    }
}