package application.model;

import java.util.List;

public class ProductDatabaseInitializer {

    private ProductDatabaseInitializer() {
        // Prevent ProductDatabaseInitializer objects
    }

    public static void initializeProducts() {
        DatabaseManager.initializeDatabase();

        int existingProductCount =
                ProductDatabase.getProductCount();

        if (existingProductCount > 0) {
            System.out.println(
                    existingProductCount
                            + " products already exist in SQLite."
            );

            return;
        }

        List<Product> csvProducts =
                ProductLoader.loadProductsFromCsv();

        for (Product product : csvProducts) {
            ProductDatabase.saveProduct(product);
        }

        System.out.println(
                csvProducts.size()
                        + " products imported from CSV into SQLite."
        );
    }
}