package application.model;

import java.util.ArrayList;
import java.util.List;

// ShoppingCart stores all cart items and handles cart math
public class ShoppingCart {

    private List<CartItem> items;
    private final double TAX_RATE = 0.0825;
    private double discountRate;

    public ShoppingCart() {
        items = new ArrayList<>();
        discountRate = 0.0;
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

    // Adds up all cart items before discount and tax
    public double getSubtotal() {
        double subtotal = 0;

        for (CartItem item : items) {
            subtotal += item.getSubtotal();
        }

        return subtotal;
    }

    // Returns the amount removed by the discount
    public double getDiscountAmount() {
        return getSubtotal() * discountRate;
    }

    // Returns the subtotal after discount
    public double getDiscountedSubtotal() {
        return getSubtotal() - getDiscountAmount();
    }

    // Calculates 8.25% tax after discount
    public double getTax() {
        return getDiscountedSubtotal() * TAX_RATE;
    }

    // Final total after discount and tax
    public double getTotal() {
        return getDiscountedSubtotal() + getTax();
    }

    // Applies a discount code
    public boolean applyDiscountCode(String code) {
        if (code == null) {
            return false;
        }

        String cleanedCode = code.trim().toUpperCase();

        if (cleanedCode.equals("SAVE10")) {
            discountRate = 0.10;
            return true;
        }

        if (cleanedCode.equals("SAVE20")) {
            discountRate = 0.20;
            return true;
        }

        discountRate = 0.0;
        return false;
    }

    // Returns the current discount percentage as a decimal
    public double getDiscountRate() {
        return discountRate;
    }

    // Clears cart after checkout
    public void clearCart() {
        items.clear();
        discountRate = 0.0;
    }
}