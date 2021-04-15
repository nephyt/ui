package com.pingpong.ui.services;

import com.pingpong.basicclass.game.Game;
import com.pingpong.basicclass.game.ListOfGames;
import com.pingpong.basicclass.game.ListOfTeams;
import com.pingpong.basicclass.game.Team;
import com.pingpong.basicclass.player.ListOfPlayers;
import com.pingpong.basicclass.player.Player;
import com.pingpong.basicclass.servicecount.AllServiceCount;
import com.pingpong.basicclass.servicecount.UpdatePlayer;
import com.pingpong.basicclass.stats.PlayersStats;
import com.pingpong.basicclass.stats.TeamStats;
import com.pingpong.ui.Constants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

public class ServicesRest {

    static RestTemplate restTemplate = new RestTemplate();

    public static void updatePlayersCountService(AllServiceCount updatePlayers) {
        String uri = Constants.SERVICE_COUNT_URL +  "updatePlayersCountService";
        restTemplate.put(uri, updatePlayers, AllServiceCount.class);
    }

    public static void updatePlayerCountService(UpdatePlayer updatePlayer) {
        String uri = Constants.SERVICE_COUNT_URL +  "updatePlayerCountService";
        restTemplate.put(uri, updatePlayer, PlayersStats.class);
    }

    public static void pauseGame(Integer gameId) {
        String uri = Constants.SERVICE_GAME_URL +  "pauseGame/" + gameId;
        restTemplate.put(uri, null);
    }

    public static void resumeGame(Integer gameId) {
        String uri = Constants.SERVICE_GAME_URL +  "resumeGame/" + gameId;
        restTemplate.put(uri, null);
    }

    public static AllServiceCount getPlayerCountService() {
        String uri = Constants.SERVICE_COUNT_URL +  "getPlayerCountService";
        AllServiceCount result = restTemplate.getForObject(uri, AllServiceCount.class);

        return result;
    }

    public static AllServiceCount getGameCountService(Integer gameId) {
        String uri = Constants.SERVICE_COUNT_URL +  "getGameCountService/" + gameId;
        AllServiceCount result = restTemplate.getForObject(uri, AllServiceCount.class);

        return result;
    }

    public static AllServiceCount getTeamsServiceCount(Integer teamId1, Integer teamId2) {
        String uri = Constants.SERVICE_COUNT_URL +  "getServiceCountForTeams/" + teamId1 + "/" + teamId2;
        AllServiceCount result = restTemplate.getForObject(uri, AllServiceCount.class);

        return result;
    }

    public static PlayersStats getPlayersStats() {
        String uri = Constants.SERVICE_GAME_URL +  "getPlayersStats";
        PlayersStats result = restTemplate.getForObject(uri, PlayersStats.class);

        return result;
    }

    public static TeamStats getTeamStatsByPlayer(Team team) {
        String uri = Constants.SERVICE_GAME_URL +  "getTeamStatsByPlayer/";

        String teamAParam = getParamStringForPlayers(team);

        uri += (teamAParam);

        TeamStats teamStats = restTemplate.getForObject(uri, TeamStats.class);

        return teamStats;
    }

    public static TeamStats getTeamVSStatsByPlayer(Team teamA, Team teamB) {
        String uri = Constants.SERVICE_GAME_URL +  "getTeamVSStatsByPlayer/";

        String teamAParam = getParamStringForPlayers(teamA);
        String teamBParam = getParamStringForPlayers(teamB);

        uri += (teamAParam + "/" + teamBParam);

        TeamStats teamStats = restTemplate.getForObject(uri, TeamStats.class);

        return teamStats;
    }

    private static String getParamStringForPlayers(Team team) {
        String paramTeam = team.getTeamPlayer1Id().toString();
        if (team.getTeamPlayer2() != null) {
            paramTeam +=  ("_" + team.getTeamPlayer2Id());
        }
        return paramTeam;
    }

    public static List<Game> getPausedGames() {
        String uri = Constants.SERVICE_GAME_URL +  "getPausedGames";

        ListOfGames result = restTemplate.getForObject(uri, ListOfGames.class);

        return result.getGames();
    }

    public static List<Team> getAllTeams() {
        String uri = Constants.SERVICE_GAME_URL +  "getAllTeams";

        ListOfTeams result = restTemplate.getForObject(uri, ListOfTeams.class);

        return result.getTeams();
    }

    public static List<Game> getHistoricGames(Integer nbHours, Integer teamId) {
        String uri = Constants.SERVICE_GAME_URL +  "getHistoricGames/" + nbHours;

        if (teamId != null) {
            uri += "/" + teamId;
        }

        ListOfGames result = restTemplate.getForObject(uri, ListOfGames.class);

        return result.getGames();
    }


    public static List<Player> listPlayer(String filterText) {
        ListOfPlayers result = getListOfPlayers(filterText);

        return result.getPlayers();
    }

    public static Map<Integer, Player> mapPlayer(String filterText) {
        ListOfPlayers result = getListOfPlayers(filterText);

        return result.getMapPlayers();
    }

    private static ListOfPlayers getListOfPlayers(String filterText) {
        String uri = Constants.SERVICE_PLAYER_URL +  "Players";

        if (!StringUtils.isEmpty(filterText)) {
            uri = Constants.SERVICE_PLAYER_URL +  "PlayersWithName/" + filterText;
        }

        return restTemplate.getForObject(uri, ListOfPlayers.class);
    }

    public static Game saveGame(Game game) {
        String uri = Constants.SERVICE_GAME_URL +  "saveGame";

        return restTemplate.postForObject(uri, game, Game.class);
    }

}
