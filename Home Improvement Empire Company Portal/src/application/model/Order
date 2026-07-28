package application.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Order {

    private static final DateTimeFormatter DISPLAY_DATE_FORMAT =
            DateTimeFormatter.ofPattern("MMM d, yyyy h:mm a");

    private String orderId;
    private String username;
    private LocalDateTime orderDate;
    private List<OrderItem> items;
    private double subtotal;
    private double discountRate;
    private double discountAmount;
    private double tax;
    private double total;

    public Order(
            String orderId,
            String username,
            LocalDateTime orderDate,
            List<OrderItem> items,
            double subtotal,
            double discountRate,
            double discountAmount,
            double tax,
            double total
    ) {
        this.orderId = orderId;
        this.username = username;
        this.orderDate = orderDate;
        this.items = new ArrayList<>(items);
        this.subtotal = subtotal;
        this.discountRate = discountRate;
        this.discountAmount = discountAmount;
        this.tax = tax;
        this.total = total;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getUsername() {
        return username;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public String getFormattedOrderDate() {
        return orderDate.format(DISPLAY_DATE_FORMAT);
    }

    public List<OrderItem> getItems() {
        return new ArrayList<>(items);
    }

    public int getItemCount() {
        int itemCount = 0;

        for (OrderItem item : items) {
            itemCount += item.getQuantity();
        }

        return itemCount;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public double getTax() {
        return tax;
    }

    public double getTotal() {
        return total;
    }

    public String getFormattedTotal() {
        return String.format("$%.2f", total);
    }

    @Override
    public String toString() {
        return "Order " + orderId
                + " - " + getFormattedOrderDate()
                + " - " + getFormattedTotal();
    }
}
