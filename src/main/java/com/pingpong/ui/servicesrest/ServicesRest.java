package com.pingpong.ui.servicesrest;

import com.pingpong.basicclass.game.Game;
import com.pingpong.basicclass.player.ListOfPlayers;
import com.pingpong.basicclass.player.Player;
import com.pingpong.basicclass.stats.PlayerStats;
import com.pingpong.basicclass.stats.PlayersStats;
import com.pingpong.ui.Constants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

public class ServicesRest {




    public static Map<Integer, PlayerStats> getPlayersStats() {
        String uri = Constants.SERVICE_GAME_URL +  "getPlayersStats";

        //TODO: Autowire the RestTemplate in all the examples
        RestTemplate restTemplate = new RestTemplate();


        PlayersStats result = restTemplate.getForObject(uri, PlayersStats.class);

        return result.getPlayersStats();
    }

    public static List<Player> listPlayer(String filterText) {
        String uri = Constants.SERVICE_PLAYER_URL +  "Players";

        //TODO: Autowire the RestTemplate in all the examples
        RestTemplate restTemplate = new RestTemplate();

        if (!StringUtils.isEmpty(filterText)) {
            uri = Constants.SERVICE_PLAYER_URL +  "PlayersWithName/" + filterText;
        }

        ListOfPlayers result = restTemplate.getForObject(uri, ListOfPlayers.class);

        return result.getPlayers();
    }

    public static Game createGame(Game game) {
        String uri = Constants.SERVICE_GAME_URL +  "createGame";

        //TODO: Autowire the RestTemplate in all the examples
        RestTemplate restTemplate = new RestTemplate();

        Game savedGame = restTemplate.postForObject(uri, game, Game.class);

        return savedGame;
    }

    public static void updateGame(Game game) {
        String uri = Constants.SERVICE_GAME_URL +  "updateGame";

        //TODO: Autowire the RestTemplate in all the examples
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.put(uri, game);

    }
}
