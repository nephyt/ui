package com.pingpong.ui.servicesrest;

import com.pingpong.basicclass.game.Game;
import com.pingpong.basicclass.player.ListOfPlayers;
import com.pingpong.basicclass.player.Player;
import com.pingpong.basicclass.servicecount.AllServiceCount;
import com.pingpong.basicclass.servicecount.UpdatePlayer;
import com.pingpong.basicclass.stats.PlayerStats;
import com.pingpong.basicclass.stats.PlayersStats;
import com.pingpong.ui.Constants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

public class ServicesRest {

    static RestTemplate restTemplate = new RestTemplate();

    public static void updatePlayerCountService(UpdatePlayer updatePlayer) {
        String uri = Constants.SERVICE_COUNT_URL +  "updatePlayerCountService";
        restTemplate.put(uri, updatePlayer, PlayersStats.class);
    }

    public static AllServiceCount getPlayerCountService() {
        String uri = Constants.SERVICE_COUNT_URL +  "getPlayerCountService";
        AllServiceCount result = restTemplate.getForObject(uri, AllServiceCount.class);

        return result;
    }

    public static Map<Integer, PlayerStats> getPlayersStats() {
        String uri = Constants.SERVICE_GAME_URL +  "getPlayersStats";
        PlayersStats result = restTemplate.getForObject(uri, PlayersStats.class);

        return result.getPlayersStats();
    }



    public static List<Player> listPlayer(String filterText) {
        String uri = Constants.SERVICE_PLAYER_URL +  "Players";

        if (!StringUtils.isEmpty(filterText)) {
            uri = Constants.SERVICE_PLAYER_URL +  "PlayersWithName/" + filterText;
        }

        ListOfPlayers result = restTemplate.getForObject(uri, ListOfPlayers.class);

        return result.getPlayers();
    }

    public static Game createGame(Game game) {
        String uri = Constants.SERVICE_GAME_URL +  "createGame";

        Game savedGame = restTemplate.postForObject(uri, game, Game.class);

        return savedGame;
    }

    public static void updateGame(Game game) {
        String uri = Constants.SERVICE_GAME_URL +  "updateGame";
        restTemplate.put(uri, game);
    }
}
