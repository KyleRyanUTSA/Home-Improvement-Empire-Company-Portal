package application.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import application.SceneManager;

public class CheckoutController {
    
    @FXML
    private TableView<CheckoutItem> cartTable;
    @FXML
    private TableColumn<CheckoutItem, String> itemNameColumn;
    @FXML
    private TableColumn<CheckoutItem, Integer> quantityColumn;
    @FXML
    private TableColumn<CheckoutItem, Double> priceColumn;
    @FXML
    private TableColumn<CheckoutItem, Double> totalColumn;
    
    @FXML
    private Label subtotalLabel;
    @FXML
    private Label taxLabel;
    @FXML
    private Label totalLabel;
    
    @FXML
    private TextField promoCodeField;
    @FXML
    private Button applyPromoButton;
    
    @FXML
    private Button completeCheckoutButton;
    @FXML
    private Button continueShoppingButton;
    @FXML
    private Button cancelButton;
    
    private ObservableList<CheckoutItem> cartItems;
    private double subtotal = 0.0;
    private double taxRate = 0.08; // 8% tax rate
    
    @FXML
    public void initialize() {
        // Set up table columns
        itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));
        
        // Initialize cart items
        cartItems = FXCollections.observableArrayList();
        cartTable.setItems(cartItems);
        
        // Add sample items (replace with actual cart data)
        addSampleItems();
        updateTotals();
    }
    
    /**
     * Add sample items to the cart for testing
     */
    private void addSampleItems() {
        cartItems.add(new CheckoutItem("Paint (Gallon)", 1, 45.99));
        cartItems.add(new CheckoutItem("Wood Flooring (per sq ft)", 150, 3.50));
        cartItems.add(new CheckoutItem("Light Fixtures", 2, 89.99));
    }
    
    /**
     * Update subtotal, tax, and total
     */
    private void updateTotals() {
        subtotal = 0.0;
        for (CheckoutItem item : cartItems) {
            subtotal += item.getTotal();
        }
        
        double tax = subtotal * taxRate;
        double total = subtotal + tax;
        
        subtotalLabel.setText(String.format("$%.2f", subtotal));
        taxLabel.setText(String.format("$%.2f", tax));
        totalLabel.setText(String.format("$%.2f", total));
    }
    
    /**
     * Handle promo code application
     */
    @FXML
    private void handleApplyPromo() {
        String promoCode = promoCodeField.getText().trim();
        if (promoCode.isEmpty()) {
            return;
        }
        
        // Simple promo code logic (replace with actual backend logic)
        double discount = 0.0;
        if (promoCode.equalsIgnoreCase("SAVE10")) {
            discount = subtotal * 0.10;
        } else if (promoCode.equalsIgnoreCase("SAVE20")) {
            discount = subtotal * 0.20;
        }
        
        if (discount > 0) {
            subtotal -= discount;
            promoCodeField.setText("Discount Applied: -$" + String.format("%.2f", discount));
            updateTotals();
        } else {
            promoCodeField.setText("Invalid Promo Code");
        }
    }
    
    /**
     * Handle complete checkout
     */
    @FXML
    private void handleCompleteCheckout() {
        if (cartItems.isEmpty()) {
            System.out.println("Cart is empty");
            return;
        }
        
        // Process payment and create order
        System.out.println("Processing checkout...");
        System.out.println("Subtotal: $" + String.format("%.2f", subtotal));
        System.out.println("Total Items: " + cartItems.size());
        
        // TODO: Implement actual checkout logic (payment processing, order creation, etc.)
        
        // Clear cart and show confirmation
        cartItems.clear();
        subtotalLabel.setText("$0.00");
        taxLabel.setText("$0.00");
        totalLabel.setText("$0.00");
        promoCodeField.clear();
    }
    
    /**
     * Continue shopping - return to previous screen
     */
    @FXML
    private void handleContinueShopping() {
        SceneManager.switchTo("/Data/views/HIELS.fxml"); // Adjust path as needed
    }
    
    /**
     * Cancel checkout - return to previous screen
     */
    @FXML
    private void handleCancel() {
        SceneManager.switchTo("/Data/views/HIELS.fxml"); // Adjust path as needed
    }
    
    /**
     * Remove item from cart
     */
    public void removeItem(CheckoutItem item) {
        cartItems.remove(item);
        updateTotals();
    }
    
    /**
     * Update item quantity
     */
    public void updateQuantity(CheckoutItem item, int newQuantity) {
        if (newQuantity > 0) {
            item.setQuantity(newQuantity);
            cartTable.refresh();
            updateTotals();
        } else {
            removeItem(item);
        }
    }
    
    /**
     * Inner class representing a checkout item
     */
    public static class CheckoutItem {
        private String itemName;
        private int quantity;
        private double price;
        
        public CheckoutItem(String itemName, int quantity, double price) {
            this.itemName = itemName;
            this.quantity = quantity;
            this.price = price;
        }
        
        public String getItemName() { return itemName; }
        public void setItemName(String name) { this.itemName = name; }
        
        public int getQuantity() { return quantity; }
        public void setQuantity(int qty) { this.quantity = qty; }
        
        public double getPrice() { return price; }
        public void setPrice(double p) { this.price = p; }
        
        public double getTotal() { return quantity * price; }
    }
}
