package application.model;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

// Loads product data from a CSV file
public class ProductLoader {

    public static List<Product> loadProducts() {
        List<Product> products = new ArrayList<>();

        // Finds the products.csv file inside the Data folder
        InputStream is = ProductLoader.class.getResourceAsStream("/Data/products.csv");

        if (is == null) {
            System.out.println("products.csv was not found.");
            return products;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;

            // Skip the first row because it is the header
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                // Makes sure the row has all needed values
                if (parts.length < 7) {
                    continue;
                }

                String productId = parts[0].trim();
                String name = parts[1].trim();
                String description = parts[2].trim();
                double price = Double.parseDouble(parts[3].trim());
                String category = parts[4].trim();
                boolean available = Boolean.parseBoolean(parts[5].trim());
                String imageName = parts[6].trim();

                Product product = new Product(productId, name, description, price, category, available,imageName);
                products.add(product);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return products;
    }
}
