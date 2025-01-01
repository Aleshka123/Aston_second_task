package other;

import org.example.config.AppConfig;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigTest {

    private static final PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>("postgres:15.3")
                    .withDatabaseName("testdb")
                    .withUsername("testuser")
                    .withPassword("testpass");

    static {
        // Запускаем контейнер
        postgresContainer.start();

        // Перезаписываем параметры подключения
        System.setProperty("db.url", postgresContainer.getJdbcUrl());
        System.setProperty("db.username", postgresContainer.getUsername());
        System.setProperty("db.password", postgresContainer.getPassword());
        System.setProperty("db.driver", "org.postgresql.Driver");
    }

    @Test
    void testAppConfig() {
        String url = AppConfig.getProperty("db.url");
        assertNotNull(url);
    }

    @Test
    void testDatabaseConnectionManager() throws Exception {
        try (Connection conn = org.example.config.DatabaseConnectionManager.getDataSource().getConnection()) {
            assertNotNull(conn);

            Statement statement = conn.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS test_table (id SERIAL PRIMARY KEY, name VARCHAR(100))");
            statement.execute("INSERT INTO test_table (name) VALUES ('test name')");

            ResultSet resultSet = statement.executeQuery("SELECT name FROM test_table WHERE id = 1");
            assertTrue(resultSet.next());
            assertEquals("test name", resultSet.getString("name"));
        }
    }
}
