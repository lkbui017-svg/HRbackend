package com.example.hr;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HRManagementApplication {
    public static void main(String[] args) {
        configureRenderDatabaseUrl();
        SpringApplication.run(HRManagementApplication.class, args);
    }

    private static void configureRenderDatabaseUrl() {
        String configuredDbUrl = firstNonBlank(System.getenv("DB_URL"), System.getProperty("DB_URL"));
        if (configuredDbUrl != null && configuredDbUrl.startsWith("jdbc:")) {
            return;
        }

        String renderDatabaseUrl = firstNonBlank(
                System.getenv("DATABASE_URL"),
                System.getenv("INTERNAL_DATABASE_URL"),
                configuredDbUrl
        );

        if (renderDatabaseUrl == null || renderDatabaseUrl.startsWith("jdbc:")) {
            return;
        }

        URI uri = URI.create(renderDatabaseUrl);
        String scheme = uri.getScheme();
        if (!"postgres".equals(scheme) && !"postgresql".equals(scheme)) {
            return;
        }

        String jdbcUrl = "jdbc:postgresql://" + uri.getHost();
        if (uri.getPort() > 0) {
            jdbcUrl += ":" + uri.getPort();
        }
        jdbcUrl += uri.getPath();
        if (uri.getQuery() != null && !uri.getQuery().isBlank()) {
            jdbcUrl += "?" + uri.getQuery();
        }

        System.setProperty("DB_URL", jdbcUrl);
        System.setProperty("DB_DRIVER", "org.postgresql.Driver");
        System.setProperty("DB_PLATFORM", "org.hibernate.dialect.PostgreSQLDialect");

        String userInfo = uri.getUserInfo();
        if (userInfo != null && !userInfo.isBlank()) {
            String[] credentials = userInfo.split(":", 2);
            System.setProperty("DB_USERNAME", decode(credentials[0]));
            if (credentials.length > 1) {
                System.setProperty("DB_PASSWORD", decode(credentials[1]));
            }
        }
    }

    private static String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return null;
    }

    private static String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }
}
