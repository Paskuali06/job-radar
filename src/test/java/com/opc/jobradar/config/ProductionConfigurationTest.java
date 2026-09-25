package com.opc.jobradar.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class ProductionConfigurationTest {

    private final ApplicationContextRunner contextRunner =
            new ApplicationContextRunner();

    @Test
    void shouldLoadDatabaseConfigurationFromEnvironmentVariables() {
        contextRunner
                .withPropertyValues(
                        "spring.profiles.active=prod",
                        "spring.datasource.url=jdbc:postgresql://production:5432/jobradar",
                        "spring.datasource.username=production-user",
                        "spring.datasource.password=production-password"
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
}