package application.model;

// CartItem connects a product with a quantity
public class CartItem {

    private Product product;
    private int quantity;

    public CartItem(Product product) {
        this.product = product;
        this.quantity = 1;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    // Adds one more of the same product
    public void increaseQuantity() {
        quantity++;
    }

    // Removes one item but does not go below zero
    public void decreaseQuantity() {
        if (quantity > 0) {
            quantity--;
        }
    }

    // Calculates price for this cart line
    public double getSubtotal() {
        return product.getPrice() * quantity;
    }

    @Override
    public String toString() {
        return product.getName() + " x" + quantity + " - $" + String.format("%.2f", getSubtotal());
    }
}