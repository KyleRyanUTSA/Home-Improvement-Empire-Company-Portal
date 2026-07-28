package application.model;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

// Loads the original product seed data from products.csv
public class ProductLoader {

    private ProductLoader() {
        // Prevent ProductLoader objects
    }

    public static List<Product> loadProductsFromCsv() {
        List<Product> products = new ArrayList<>();

        InputStream inputStream =
                ProductLoader.class.getResourceAsStream(
                        "/Data/products.csv"
                );

        if (inputStream == null) {
            throw new IllegalStateException(
                    "products.csv was not found at /Data/products.csv."
            );
        }

        try (
                BufferedReader reader =
                        new BufferedReader(
                                new InputStreamReader(inputStream)
                        )
        ) {
            // Skip the CSV header
            reader.readLine();

            String line;

            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }

                String[] parts =
                        line.split(",", -1);

                if (parts.length < 6) {
                    System.out.println(
                            "Skipping invalid product row: "
                                    + line
                    );

                    continue;
                }

                String productId =
                        parts[0].trim();

                String name =
                        parts[1].trim();

                String description =
                        parts[2].trim();

                double price =
                        Double.parseDouble(
                                parts[3].trim()
                        );

                String category =
                        parts[4].trim();

                boolean available =
                        Boolean.parseBoolean(
                                parts[5].trim()
                        );

                String imageName =
                        parts.length > 6
                                && !parts[6].trim().isBlank()
                                ? parts[6].trim()
                                : "default-product.jpg";

                products.add(
                        new Product(
                                productId,
                                name,
                                description,
                                price,
                                category,
                                available,
                                imageName
                        )
                );
            }

        } catch (Exception exception) {
            throw new IllegalStateException(
                    "Products could not be loaded from CSV.",
                    exception
            );
        }

        return products;
    }
}