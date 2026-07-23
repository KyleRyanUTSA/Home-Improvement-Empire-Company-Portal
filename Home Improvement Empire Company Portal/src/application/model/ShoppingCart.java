package application.model;

import java.util.ArrayList;
import java.util.List;

// ShoppingCart stores all cart items and handles cart math
public class ShoppingCart {

    private List<CartItem> items;
    private final double TAX_RATE = 0.0825;

    public ShoppingCart() {
        items = new ArrayList<>();
    }

    public List<CartItem> getItems() {
        return items;
    }

    // Adds a product or increases quantity if it is already in the cart
    public void addProduct(Product product) {
        for (CartItem item : items) {
            if (item.getProduct().getProductId().equals(product.getProductId())) {
                item.increaseQuantity();
                return;
            }
        }

        items.add(new CartItem(product));
    }

    // Removes one quantity from the selected cart item
    public void removeOneProduct(CartItem selectedItem) {
        if (selectedItem == null) {
            return;
        }

        selectedItem.decreaseQuantity();

        if (selectedItem.getQuantity() <= 0) {
            items.remove(selectedItem);
        }
    }

    // Adds up all cart items before tax
    public double getSubtotal() {
        double subtotal = 0;

        for (CartItem item : items) {
            subtotal += item.getSubtotal();
        }

        return subtotal;
    }

    // Calculates 8.25% tax
    public double getTax() {
        return getSubtotal() * TAX_RATE;
    }

    // Final total after tax
    public double getTotal() {
        return getSubtotal() + getTax();
    }

    // Clears cart after checkout
    public void clearCart() {
        items.clear();
    }
}