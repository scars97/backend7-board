package com.backend7.frameworkstudy.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private final String JWT = "JWT";

    @Bean
    public OpenAPI boardApi() {
        Components components = components();
        Info info = info();
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(JWT);

        return new OpenAPI()
                .components(components)
                .info(info)
                .addSecurityItem(securityRequirement);
    }

    private Components components() {
        SecurityScheme securityScheme = new SecurityScheme()
                .name(JWT)
                .type(SecurityScheme.Type.HTTP)
                .scheme("Bearer")
                .bearerFormat("JWT");

        return new Components().addSecuritySchemes(JWT, securityScheme);
    }

    private Info info() {
        return new Info()
                .title("Board")
                .version("0.2")
                .description("프레임워크 사전 스터디");
    }
}
