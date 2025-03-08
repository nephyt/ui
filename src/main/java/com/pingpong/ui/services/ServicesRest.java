package com.pingpong.ui.services;

import com.pingpong.basicclass.game.Game;
import com.pingpong.basicclass.game.Team;
import com.pingpong.basicclass.player.ListOfPlayers;
import com.pingpong.basicclass.player.Player;
import com.pingpong.basicclass.servicecount.AllServiceCount;
import com.pingpong.basicclass.servicecount.UpdatePlayer;
import com.pingpong.basicclass.stats.PlayersStats;
import com.pingpong.basicclass.stats.TeamStats;
import com.pingpong.ui.client.ServiceCountClient;
import com.pingpong.ui.client.ServiceGameClient;
import com.pingpong.ui.client.ServicePlayersClient;
import com.pingpong.ui.config.InitHttpConfig;
import com.pingpong.ui.config.UrlConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;


@Component
public class ServicesRest {

    private static final InitHttpConfig initHttpConfig = new InitHttpConfig();
    private static ServiceCountClient serviceCountClient;
    private static ServiceGameClient serviceGameClient;
    private static ServicePlayersClient servicePlayersClient;

    static RestTemplate restTemplate = new RestTemplate();

    public ServicesRest(UrlConfig config) {
        serviceCountClient = initHttpConfig.serviceCountClient(config.getServicecount());
        serviceGameClient = initHttpConfig.serviceGameClient(config.getServicegame());
        servicePlayersClient = initHttpConfig.servicePlayersClient(config.getServiceplayer());
    }

    // count service
    public static void updatePlayersCountService(AllServiceCount updatePlayers) {
        serviceCountClient.updatePlayersCountService(updatePlayers);
    }

    public static void updatePlayerCountService(UpdatePlayer updatePlayer) {
        serviceCountClient.updatePlayerCountService(updatePlayer);
    }

    public static void deleteServiceGame(Integer gameId) {
        serviceCountClient.deleteServiceGame(gameId);
    }

    public static AllServiceCount getPlayerCountService() {
        return serviceCountClient.getPlayerCountService();
    }

    public static AllServiceCount getGameCountService(Integer gameId) {
        return serviceCountClient.getGameCountService(gameId);
    }

    public static AllServiceCount getTeamsServiceCount(Integer teamId1, Integer teamId2) {
        return serviceCountClient.getServiceCountForTeams(teamId1, teamId2);
    }


    // Game

    public static void deleteGame(Integer gameId) {
        serviceGameClient.deleteGame(gameId);
    }

    public static void pauseGame(Integer gameId) {
        serviceGameClient.pauseGame(gameId);
    }

    public static void resumeGame(Integer gameId) {
        serviceGameClient.resumeGame(gameId);
    }

    public static PlayersStats getPlayersStats() {
        return serviceGameClient.getPlayersStats();
    }

    public static TeamStats getTeamStatsByPlayer(Team team) {
        String teamAParam = getParamStringForPlayers(team);
        return serviceGameClient.getTeamStatsByPlayer(teamAParam);
    }

    public static Game saveGame(Game game) {
        return serviceGameClient.saveGame(game);
    }

    public static TeamStats getTeamVSStatsByPlayer(Team teamA, Team teamB) {
        String teamAParam = getParamStringForPlayers(teamA);
        String teamBParam = getParamStringForPlayers(teamB);

        return  serviceGameClient.getTeamVSStatsByPlayer(teamAParam, teamBParam);
    }

    private static String getParamStringForPlayers(Team team) {
        String paramTeam = team.getTeamPlayer1Id().toString();
        if (team.getTeamPlayer2() != null) {
            paramTeam +=  ("_" + team.getTeamPlayer2Id());
        }
        return paramTeam;
    }

    public static List<Game> getPausedGames() {
        return serviceGameClient.getPausedGames().getGames();
    }

    public static List<Team> getAllTeams() {
        return serviceGameClient.getAllTeams().getTeams();
    }

    public static List<Game> getHistoricGames(Integer nbHours, Integer teamId) {
        if (teamId == null) {
            return serviceGameClient.getHistoricGames(nbHours).getGames();
        } else {
            return serviceGameClient.getHistoricGames(nbHours, teamId).getGames();
        }
    }

    // PLayer

    public static void deletePlayer(Player player) {
        servicePlayersClient.delete(player.getId());
    }

    public static void restorePlayer(Player player) {
        servicePlayersClient.restorePlayer(player.getId());
    }

    public static void savePlayer(Player player) {
        servicePlayersClient.savePlayer(player);
    }

    public static Player getAPlayer(int id) {
        return servicePlayersClient.getAPlayer(id);
    }

    private static ListOfPlayers getListOfPlayers(String filterText) {
        if (!StringUtils.isEmpty(filterText)) {
            return servicePlayersClient.getListOfPlayersWithName(filterText);
        }

        return servicePlayersClient.getListOfPlayers();
    }

    public static List<Player> listPlayer(String filterText) {
        ListOfPlayers result = getListOfPlayers(filterText);

        return result.getPlayers();
    }

    public static Map<Integer, Player> mapPlayer(String filterText) {
        ListOfPlayers result = getListOfPlayers(filterText);

        return result.getMapPlayers();
    }





}
