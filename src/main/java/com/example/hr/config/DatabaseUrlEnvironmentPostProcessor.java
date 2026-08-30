package com.example.hr.config;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

public class DatabaseUrlEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {
    private static final String PROPERTY_SOURCE_NAME = "normalizedDatabaseUrl";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String databaseUrl = firstPresent(
                environment.getProperty("DB_URL"),
                environment.getProperty("DATABASE_URL"),
                environment.getProperty("spring.datasource.url")
        );

        String normalizedUrl = normalizeDatabaseUrl(databaseUrl);
        if (normalizedUrl == null) {
            return;
        }

        Map<String, Object> properties = new HashMap<>();
        properties.put("spring.datasource.url", normalizedUrl);

        String lowerUrl = normalizedUrl.toLowerCase(Locale.ROOT);
        if (lowerUrl.startsWith("jdbc:postgresql:")) {
            properties.putIfAbsent("spring.datasource.driver-class-name", "org.postgresql.Driver");
            properties.putIfAbsent("spring.jpa.database-platform", "org.hibernate.dialect.PostgreSQLDialect");
        } else if (lowerUrl.startsWith("jdbc:mysql:")) {
            properties.putIfAbsent("spring.datasource.driver-class-name", "com.mysql.cj.jdbc.Driver");
            properties.putIfAbsent("spring.jpa.database-platform", "org.hibernate.dialect.MySQLDialect");
        }

        environment.getPropertySources().addFirst(new MapPropertySource(PROPERTY_SOURCE_NAME, properties));
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    private static String firstPresent(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value.trim();
            }
        }
        return null;
    }

    private static String normalizeDatabaseUrl(String databaseUrl) {
        if (databaseUrl == null) {
            return null;
        }

        String value = databaseUrl.trim();
        if (value.isEmpty()) {
            return null;
        }

        String lowerValue = value.toLowerCase(Locale.ROOT);
        if (lowerValue.startsWith("dbc:")) {
            return "j" + value;
        }
        if (lowerValue.startsWith("postgresql://")) {
            return "jdbc:" + value;
        }
        if (lowerValue.startsWith("postgres://")) {
            return "jdbc:postgresql://" + value.substring("postgres://".length());
        }
        if (lowerValue.startsWith("mysql://")) {
            return "jdbc:" + value;
        }

        return value;
    }
}
