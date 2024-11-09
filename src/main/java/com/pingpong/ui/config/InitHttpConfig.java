package com.pingpong.ui.config;

import com.pingpong.ui.client.ServiceCountClient;
import com.pingpong.ui.client.ServiceGameClient;
import com.pingpong.ui.client.ServicePlayersClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class InitHttpConfig {


    private final ExchangeStrategies exchangeStrategies = ExchangeStrategies
            .builder()
            .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(1000000 * 1024))
            .build();

    @Bean
    public ServiceCountClient serviceCountClient(@Value("${url.servicecount}") String baseUrl) {
        return HttpServiceProxyFactory
                .builder()
                .exchangeAdapter(WebClientAdapter.create(WebClient.builder()
                        .baseUrl(baseUrl)
                        .exchangeStrategies(exchangeStrategies)
                        .build()))
                .build()
                .createClient(ServiceCountClient.class);
    }

    @Bean
    public ServiceGameClient serviceGameClient(@Value("${url.servicegame}") String baseUrl) {
        return HttpServiceProxyFactory
                .builder()
                .exchangeAdapter(WebClientAdapter.create(WebClient.builder()
                        .baseUrl(baseUrl)
                        .exchangeStrategies(exchangeStrategies)
                        .build()))
                .build()
                .createClient(ServiceGameClient.class);
    }

    @Bean
    public ServicePlayersClient servicePlayersClient(@Value("${url.serviceplayer}") String baseUrl) {
        return HttpServiceProxyFactory
                .builder()
                .exchangeAdapter(WebClientAdapter.create(WebClient.builder()
                        .baseUrl(baseUrl)
                        .exchangeStrategies(exchangeStrategies)
                        .build()))
                .build()
                .createClient(ServicePlayersClient.class);
    }

}
