package application.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDatabase {

    private ProductDatabase() {
        // Prevent ProductDatabase objects
    }

    public static void saveProduct(Product product) {
        String sql = """
                INSERT INTO products (
                    product_id,
                    name,
                    description,
                    price,
                    category,
                    available,
                    image_name
                )
                VALUES (?, ?, ?, ?, ?, ?, ?)
                ON CONFLICT(product_id) DO UPDATE SET
                    name = excluded.name,
                    description = excluded.description,
                    price = excluded.price,
                    category = excluded.category,
                    available = excluded.available,
                    image_name = excluded.image_name
                """;

        try (
                Connection connection =
                        DatabaseManager.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {
            statement.setString(
                    1,
                    product.getProductId()
            );

            statement.setString(
                    2,
                    product.getName()
            );

            statement.setString(
                    3,
                    product.getDescription()
            );

            statement.setDouble(
                    4,
                    product.getPrice()
            );

            statement.setString(
                    5,
                    product.getCategory()
            );

            statement.setInt(
                    6,
                    product.isAvailable() ? 1 : 0
            );

            statement.setString(
                    7,
                    product.getImageName()
            );

            statement.executeUpdate();

        } catch (SQLException exception) {
            throw new IllegalStateException(
                    "The product could not be saved.",
                    exception
            );
        }
    }

    public static List<Product> loadProducts() {
        List<Product> products = new ArrayList<>();

        String sql = """
                SELECT
                    product_id,
                    name,
                    description,
                    price,
                    category,
                    available,
                    image_name
                FROM products
                ORDER BY product_id
                """;

        try (
                Connection connection =
                        DatabaseManager.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet results =
                        statement.executeQuery()
        ) {
            while (results.next()) {
                products.add(
                        new Product(
                                results.getString("product_id"),
                                results.getString("name"),
                                results.getString("description"),
                                results.getDouble("price"),
                                results.getString("category"),
                                results.getInt("available") == 1,
                                results.getString("image_name")
                        )
                );
            }

        } catch (SQLException exception) {
            throw new IllegalStateException(
                    "Products could not be loaded from SQLite.",
                    exception
            );
        }

        return products;
    }

    public static int getProductCount() {
        String sql =
                "SELECT COUNT(*) FROM products";

        try (
                Connection connection =
                        DatabaseManager.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet results =
                        statement.executeQuery()
        ) {
            if (results.next()) {
                return results.getInt(1);
            }

            return 0;

        } catch (SQLException exception) {
            throw new IllegalStateException(
                    "The product count could not be loaded.",
                    exception
            );
        }
    }
}