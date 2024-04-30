package com.meysam.csvmanager.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;

public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(new Info().title("CSV MANAGER")
                        .description("Spring Boot Application For Uploading And Managing CSV File")
                        .version("v0.0.1"));

                //we haven't security yet
                /*.components(
                        new Components().addSecuritySchemes(
                                "api",
                                new SecurityScheme()
                                        .scheme("bearer")
                                        .type(SecurityScheme.Type.HTTP)
                                        .bearerFormat("jwt") //if it is your case
                                        .name("api")
                        ));*/

    }
}
