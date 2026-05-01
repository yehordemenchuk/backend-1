package com.gamestudio.users.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "auth-api",
                version = "v1",
                description = "Responsible for shop user-management and authentication implementation."
        )
)
public class SwaggerConfig {
}
