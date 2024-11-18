package com.pingpong.ui.client;

import com.pingpong.basicclass.game.Game;
import com.pingpong.basicclass.game.ListOfGames;
import com.pingpong.basicclass.game.ListOfTeams;
import com.pingpong.basicclass.stats.PlayersStats;
import com.pingpong.basicclass.stats.TeamStats;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.*;

@HttpExchange(accept = MediaType.APPLICATION_JSON_VALUE, contentType = MediaType.APPLICATION_JSON_VALUE)
public interface ServiceGameClient {

    @GetExchange("/getPlayersStats")
    PlayersStats getPlayersStats();

    @GetExchange("/getTeamStatsByPlayer/{team}")
    TeamStats getTeamStatsByPlayer(@PathVariable String team);

    @GetExchange("/getTeamVSStatsByPlayer/{teamA}/{teamB}")
    TeamStats getTeamVSStatsByPlayer(@PathVariable String teamA, @PathVariable String teamB);

    @GetExchange("/getAllTeams")
    ListOfTeams getAllTeams();

    @GetExchange("/getHistoricGames/{nbHours}")
    ListOfGames getHistoricGames(@PathVariable Integer nbHours);

    @GetExchange("/getHistoricGames/{nbHours}/{teamId}")
    ListOfGames getHistoricGames(@PathVariable Integer nbHours, @PathVariable Integer teamId);

    @GetExchange("/getPausedGames")
    ListOfGames getPausedGames();

    @PutExchange("/pauseGame/{gameId}")
    void pauseGame(@PathVariable Integer gameId);

    @PutExchange("/resumeGame/{gameId}")
    void resumeGame(@PathVariable Integer gameId);

    @DeleteExchange("/deleteGame/{gameId}")
    void deleteGame(@PathVariable Integer gameId);

    @PostExchange("/saveGame")
    Game saveGame(@RequestBody Game game);
}
