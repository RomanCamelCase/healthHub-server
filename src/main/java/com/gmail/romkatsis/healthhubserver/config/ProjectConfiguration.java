package com.gmail.romkatsis.healthhubserver.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class ProjectConfiguration {

    @Bean
    @Profile("test")
    public OpenAPI openAPI() {
        return new OpenAPI().info(new Info().version("v1"));
    }
}
