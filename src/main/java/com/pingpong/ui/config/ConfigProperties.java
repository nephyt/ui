package com.pingpong.ui.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigProperties {
    @Bean
    @ConfigurationProperties(prefix = "url")
    public UrlConfig urlConfig() {
        return new UrlConfig();
    }

}
