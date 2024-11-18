package com.pingpong.ui.client;

import com.pingpong.basicclass.servicecount.AllServiceCount;
import com.pingpong.basicclass.servicecount.UpdatePlayer;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PutExchange;

@HttpExchange(accept = MediaType.APPLICATION_JSON_VALUE, contentType = MediaType.APPLICATION_JSON_VALUE)
public interface ServiceCountClient {

    @GetExchange("/getPlayerCountService")
    AllServiceCount getPlayerCountService();

    @GetExchange("/getGameCountService/{gameId}")
    AllServiceCount getGameCountService(@PathVariable Integer gameId);

    @GetExchange("/getServiceCountForTeams/{teamId1}/{teamId2}")
    AllServiceCount getServiceCountForTeams(@PathVariable Integer teamId1, @PathVariable Integer teamId2);

    @PutExchange("/updatePlayersCountService")
    void updatePlayersCountService(@RequestBody AllServiceCount updatePlayers);
    @PutExchange("/updatePlayerCountService")
    void updatePlayerCountService(@RequestBody UpdatePlayer updatePlayers);

    @DeleteExchange("/deleteServiceGame/{gameId}")
    void deleteServiceGame(@PathVariable Integer gameId);
}
