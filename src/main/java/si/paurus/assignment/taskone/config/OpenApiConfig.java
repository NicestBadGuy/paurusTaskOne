package si.paurus.assignment.taskone.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI().info(new Info()
                                          .title("Task One API")
                                          .description("REST API for taxation calculations (assignment for Paurus)")
                                          .version("1.0.0"));
    }
}
