import org.example.config.AppConfig;
import org.example.config.DatabaseConnectionManager;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class ConfigTest {

    @Test
    void testAppConfig() {
        String url = AppConfig.getProperty("db.url");
        assertNotNull(url);
    }
    @Test
    void testDatabaseConnectionManager() throws Exception {
        try (var conn = DatabaseConnectionManager.getDataSource().getConnection()) {
            assertNotNull(conn);
        }
    }
}