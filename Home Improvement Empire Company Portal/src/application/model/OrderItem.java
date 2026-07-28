package application.model;

public class OrderItem {

    private String productId;
    private String productName;
    private double unitPrice;
    private int quantity;

    public OrderItem(String productId, String productName, double unitPrice, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getSubtotal() {
        return unitPrice * quantity;
    }

    @Override
    public String toString() {
        return productName
                + " x" + quantity
                + " - $" + String.format("%.2f", getSubtotal());
    }
}
