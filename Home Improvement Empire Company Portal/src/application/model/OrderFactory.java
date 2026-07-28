package application.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrderFactory {

    private OrderFactory() {
        // Utility class
    }

    public static Order createOrderFromCart(ShoppingCart cart, Credential customer) {
        if (cart == null) {
            throw new IllegalArgumentException("Shopping cart cannot be null.");
        }

        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null.");
        }

        if (cart.getItems().isEmpty()) {
            throw new IllegalStateException("Cannot create an order from an empty cart.");
        }

        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cart.getItems()) {
            Product product = cartItem.getProduct();

            orderItems.add(new OrderItem(
                    product.getProductId(),
                    product.getName(),
                    product.getPrice(),
                    cartItem.getQuantity()
            ));
        }

        return new Order(
                generateOrderId(),
                customer.getUsername(),
                LocalDateTime.now(),
                orderItems,
                cart.getSubtotal(),
                cart.getDiscountRate(),
                cart.getDiscountAmount(),
                cart.getTax(),
                cart.getTotal()
        );
    }

    private static String generateOrderId() {
        String timestamp = LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
        );

        String randomPart = UUID.randomUUID()
                .toString()
                .substring(0, 6)
                .toUpperCase();

        return timestamp + "-" + randomPart;
    }
}
