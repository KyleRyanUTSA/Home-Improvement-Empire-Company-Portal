package application.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class OrderHistoryLoader {

    private static final Path ORDER_FILE = Paths.get(
            System.getProperty("user.home"),
            ".heicp",
            "orders.txt"
    );

    private OrderHistoryLoader() {
        // Utility class
    }

    public static void saveOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null.");
        }

        try {
            Files.createDirectories(ORDER_FILE.getParent());

            try (BufferedWriter writer = Files.newBufferedWriter(
                    ORDER_FILE,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            )) {
                writer.write(String.join("|",
                        "ORDER",
                        encode(order.getOrderId()),
                        encode(order.getUsername()),
                        order.getOrderDate().toString(),
                        Double.toString(order.getSubtotal()),
                        Double.toString(order.getDiscountRate()),
                        Double.toString(order.getDiscountAmount()),
                        Double.toString(order.getTax()),
                        Double.toString(order.getTotal())
                ));
                writer.newLine();

                for (OrderItem item : order.getItems()) {
                    writer.write(String.join("|",
                            "ITEM",
                            encode(item.getProductId()),
                            encode(item.getProductName()),
                            Double.toString(item.getUnitPrice()),
                            Integer.toString(item.getQuantity())
                    ));
                    writer.newLine();
                }

                writer.write("END");
                writer.newLine();
            }
        } catch (IOException exception) {
            throw new IllegalStateException("The order could not be saved.", exception);
        }
    }

    public static List<Order> loadOrdersForUser(String username) {
        List<Order> matchingOrders = new ArrayList<>();

        if (username == null || username.isBlank() || !Files.exists(ORDER_FILE)) {
            return matchingOrders;
        }

        for (Order order : loadAllOrders()) {
            if (username.equals(order.getUsername())) {
                matchingOrders.add(order);
            }
        }

        matchingOrders.sort(
                (first, second) -> second.getOrderDate().compareTo(first.getOrderDate())
        );

        return matchingOrders;
    }

    private static List<Order> loadAllOrders() {
        List<Order> orders = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(
                ORDER_FILE,
                StandardCharsets.UTF_8
        )) {
            String orderId = null;
            String username = null;
            LocalDateTime orderDate = null;
            double subtotal = 0.0;
            double discountRate = 0.0;
            double discountAmount = 0.0;
            double tax = 0.0;
            double total = 0.0;
            List<OrderItem> items = new ArrayList<>();

            String line;

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split("\\|", -1);

                if (fields.length == 0) {
                    continue;
                }

                if ("ORDER".equals(fields[0]) && fields.length == 9) {
                    orderId = decode(fields[1]);
                    username = decode(fields[2]);
                    orderDate = LocalDateTime.parse(fields[3]);
                    subtotal = Double.parseDouble(fields[4]);
                    discountRate = Double.parseDouble(fields[5]);
                    discountAmount = Double.parseDouble(fields[6]);
                    tax = Double.parseDouble(fields[7]);
                    total = Double.parseDouble(fields[8]);
                    items = new ArrayList<>();
                } else if ("ITEM".equals(fields[0]) && fields.length == 5) {
                    items.add(new OrderItem(
                            decode(fields[1]),
                            decode(fields[2]),
                            Double.parseDouble(fields[3]),
                            Integer.parseInt(fields[4])
                    ));
                } else if ("END".equals(fields[0])
                        && orderId != null
                        && username != null
                        && orderDate != null) {
                    orders.add(new Order(
                            orderId,
                            username,
                            orderDate,
                            items,
                            subtotal,
                            discountRate,
                            discountAmount,
                            tax,
                            total
                    ));

                    orderId = null;
                    username = null;
                    orderDate = null;
                    items = new ArrayList<>();
                }
            }
        } catch (IOException | IllegalArgumentException exception) {
            throw new IllegalStateException("The order history could not be loaded.", exception);
        }

        return orders;
    }

    private static String encode(String value) {
        String safeValue = value == null ? "" : value;

        return Base64.getEncoder().encodeToString(
                safeValue.getBytes(StandardCharsets.UTF_8)
        );
    }

    private static String decode(String value) {
        return new String(
                Base64.getDecoder().decode(value),
                StandardCharsets.UTF_8
        );
    }
}
