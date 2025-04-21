package com.gdt.chess.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
  @Bean
  public OpenAPI chessOpenAPI() {
    return new OpenAPI()
      .info(new Info()
        .title("Chess Service API")
        .version("v1.0")
        .description("REST endpoints for managing chess games, moves, and players"));
  }
}