package com.pingpong.ui.client;

import com.pingpong.basicclass.game.Game;
import com.pingpong.basicclass.player.ListOfPlayers;
import com.pingpong.basicclass.player.Player;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.*;

@HttpExchange(accept = MediaType.APPLICATION_JSON_VALUE, contentType = MediaType.APPLICATION_JSON_VALUE)
public interface ServicePlayersClient {

    @GetExchange("/Players/{playerId}")
    Player getAPlayer(@PathVariable Integer playerId);

    @GetExchange("/Players")
    ListOfPlayers getListOfPlayers();

    @GetExchange("/PlayersWithName/{filer}")
    ListOfPlayers getListOfPlayersWithName(@PathVariable String filer);

    @PutExchange("/Players/{playerId}")
    void restorePlayer(@PathVariable Integer playerId);

    @DeleteExchange("/Players/{playerId}")
    void delete(@PathVariable Integer playerId);

    @PostExchange("/SavePlayer")
    Game savePlayer(@RequestBody Player player);
}
