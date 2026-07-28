package application.model;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private static final Path DATABASE_DIRECTORY = Paths.get(
            System.getProperty("user.home"),
            ".heicp"
    );

    private static final Path DATABASE_FILE =
            DATABASE_DIRECTORY.resolve("store.db");

    private static final String DATABASE_URL =
            "jdbc:sqlite:" + DATABASE_FILE.toAbsolutePath();

    private DatabaseManager() {
        // Prevent DatabaseManager objects
    }

    public static Connection getConnection()
            throws SQLException {

        try {
            Files.createDirectories(DATABASE_DIRECTORY);

            // Explicitly loads the SQLite driver
            Class.forName("org.sqlite.JDBC");

        } catch (Exception exception) {
            throw new SQLException(
                    "The SQLite driver or database folder could not be initialized.",
                    exception
            );
        }

        return DriverManager.getConnection(DATABASE_URL);
    }

    public static void initializeDatabase() {
        String createProductsTable = """
                CREATE TABLE IF NOT EXISTS products (
                    product_id TEXT PRIMARY KEY,
                    name TEXT NOT NULL,
                    description TEXT,
                    price REAL NOT NULL,
                    category TEXT,
                    available INTEGER NOT NULL,
                    image_name TEXT
                )
                """;

        try (
                Connection connection = getConnection();
                Statement statement = connection.createStatement()
        ) {
            statement.execute(createProductsTable);

            System.out.println(
                    "SQLite database initialized at: "
                            + DATABASE_FILE.toAbsolutePath()
            );

        } catch (SQLException exception) {
            throw new IllegalStateException(
                    "The SQLite database could not be initialized.",
                    exception
            );
        }
    }

    public static Path getDatabaseFile() {
        return DATABASE_FILE;
    }
}