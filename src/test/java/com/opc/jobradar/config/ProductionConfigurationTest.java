package com.opc.jobradar.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class ProductionConfigurationTest {

    private final ApplicationContextRunner contextRunner =
        new ApplicationContextRunner()
                .withInitializer(new ConfigDataApplicationContextInitializer())
                .withSystemProperties("spring.profiles.active=prod");

    @Test
    void shouldLoadDatabaseConfigurationFromEnvironmentVariables() {
        contextRunner
                .withPropertyValues(
                        "DB_URL=jdbc:postgresql://production:5432/jobradar",
                        "DB_USERNAME=production-user",
                        "DB_PASSWORD=production-password"
                )
                .run(context -> {
                    assertThat(
                            context.getEnvironment()
                                    .getProperty("spring.datasource.url")
                    ).isEqualTo(
                            "jdbc:postgresql://production:5432/jobradar"
                    );

                    assertThat(
                            context.getEnvironment()
                                    .getProperty("spring.datasource.username")
                    ).isEqualTo("production-user");

                    assertThat(
                            context.getEnvironment()
                                    .getProperty("spring.datasource.password")
                    ).isEqualTo("production-password");
                });
    }

    @Test
    void shouldDisableApiDocsInProduction() {
        contextRunner
                .run(context -> {
                    assertThat(
                            context.getEnvironment()
                                    .getProperty("springdoc.api-docs.enabled")
                    ).isEqualTo("false");
                });
    }

    @Test
    void shouldDisableSwaggerUiInProduction() {
        contextRunner
                .run(context -> {
                    assertThat(
                            context.getEnvironment()
                                    .getProperty("springdoc.swagger-ui.enabled")
                    ).isEqualTo("false");
                });
    }
    @Test
    void shouldExposeOnlyHealthActuatorEndpointInProduction() {
        contextRunner
            .run(context -> {
                assertThat(
                        context.getEnvironment()
                                .getProperty("management.endpoints.web.exposure.include")
                ).isEqualTo("health");
            });
}

        @Test
        void shouldNotExposeHealthDetailsInProduction() {
                contextRunner
                        .run(context -> {
                                assertThat(
                                        context.getEnvironment()
                                .getProperty("management.endpoint.health.show-details")
                ).isEqualTo("never");
            });
}
}