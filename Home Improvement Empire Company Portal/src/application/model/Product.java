package application.model;

// Product class stores information about one store item
public class Product {

    private String productId;
    private String name;
    private String description;
    private double price;
    private String category;
    private boolean available;

    public Product(String productId, String name, String description, double price, String category, boolean available) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.available = available;
    }

    // Getters keep the fields protected but still usable
    public String getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public boolean isAvailable() {
        return available;
    }

    // This controls how products show up inside the ListView
    @Override
    public String toString() {
        String stockText = available ? "In Stock" : "Out of Stock";
        return name + " - $" + String.format("%.2f", price) + " - " + stockText;
    }
}