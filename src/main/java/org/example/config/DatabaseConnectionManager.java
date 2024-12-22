package org.example.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class DatabaseConnectionManager {
    private static final HikariDataSource dataSource;

    static {
        try {
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(AppConfig.getProperty("db.url"));
            config.setUsername(AppConfig.getProperty("db.username"));
            config.setPassword(AppConfig.getProperty("db.password"));
            config.setDriverClassName(AppConfig.getProperty("db.driver"));

            dataSource = new HikariDataSource(config);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize DataSource", e);
        }
    }

    public static DataSource getDataSource() {
        return dataSource;
    }
}
