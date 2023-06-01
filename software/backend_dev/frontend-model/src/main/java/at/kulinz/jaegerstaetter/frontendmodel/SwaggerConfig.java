package at.kulinz.jaegerstaetter.frontendmodel;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class SwaggerConfig {

    @Bean
    public OpenAPI jaegerstaetterOpenApi() {
        return new OpenAPI()
                .info(new Info().title("Edition Jägerstätter API")
                        .description("Offene REST-API der Edition Jägerstätter")
                        .version("v1.0.0")
                        .license(new License().name("CC By 4.0").url("https://edition.jaegerstaetter.at")));
    }

    @Bean
    public GroupedOpenApi resourcesOpenApi() {
        return GroupedOpenApi.builder()
                .group("edition-resource")
                .displayName("API für Transkriptionen")
                .pathsToMatch("/api/resource/", "/api/resource/norm/*", "/api/resource/xml/*", "/api/resource/diplo/*", "/api/resource/norm_pdf/*")
                .build();
    }

    @Bean
    public GroupedOpenApi searchOpenApi() {
        return GroupedOpenApi.builder()
                .group("edition-search")
                .displayName("API für die Suche")
                .pathsToMatch(("/api/search/**"))
                .build();
    }

    @Bean
    public GroupedOpenApi registryOpenApi() {
        return GroupedOpenApi.builder()
                .group("edition-registry")
                .displayName("API für die Register-Einträge")
                .pathsToMatch("/api/registry/**")
                .build();
    }

    @Bean
    public GroupedOpenApi biographyOpenApi() {
        return GroupedOpenApi.builder()
                .group("edition-biography")
                .displayName("API für die Biografie-Daten")
                .pathsToMatch("/api/biography/**")
                .build();
    }
}
