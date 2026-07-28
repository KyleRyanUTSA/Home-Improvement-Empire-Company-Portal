package application.controller;

import application.model.CartItem;
import application.model.Credential;
import application.model.Order;
import application.model.OrderFactory;
import application.model.OrderHistoryLoader;
import application.model.ShoppingCart;
import application.model.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

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
    private ShoppingCart shoppingCart;
    private Runnable returnToShoppingAction;

    @FXML
    public void initialize() {
        itemNameColumn.setCellValueFactory(
                new PropertyValueFactory<>("itemName")
        );

        quantityColumn.setCellValueFactory(
                new PropertyValueFactory<>("quantity")
        );

        priceColumn.setCellValueFactory(
                new PropertyValueFactory<>("price")
        );

        totalColumn.setCellValueFactory(
                new PropertyValueFactory<>("total")
        );

        cartItems = FXCollections.observableArrayList();
        cartTable.setItems(cartItems);

        updateTotals();
    }

    public void setCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;

        cartItems.clear();

        for (CartItem item : shoppingCart.getItems()) {
            cartItems.add(
                    new CheckoutItem(
                            item.getProduct().getName(),
                            item.getQuantity(),
                            item.getProduct().getPrice()
                    )
            );
        }

        updateTotals();
    }

    private void updateTotals() {
        if (shoppingCart == null) {
            subtotalLabel.setText("$0.00");
            taxLabel.setText("$0.00");
            totalLabel.setText("$0.00");
            return;
        }

        subtotalLabel.setText(
                String.format(
                        "$%.2f",
                        shoppingCart.getDiscountedSubtotal()
                )
        );

        taxLabel.setText(
                String.format(
                        "$%.2f",
                        shoppingCart.getTax()
                )
        );

        totalLabel.setText(
                String.format(
                        "$%.2f",
                        shoppingCart.getTotal()
                )
        );
    }

    @FXML
    private void handleApplyPromo() {
        if (shoppingCart == null) {
            promoCodeField.setText("Cart could not be loaded");
            return;
        }
        String promoCode = promoCodeField.getText().trim();

        if (promoCode.isEmpty()) {
            promoCodeField.setText("Enter a promo code");
            return;
        }

        boolean applied =
                shoppingCart.applyDiscountCode(promoCode);

        if (applied) {
            promoCodeField.setText(
                    promoCode.toUpperCase() + " Applied"
            );
        } else {
            promoCodeField.setText("Invalid Promo Code");
        }

        updateTotals();
    }

    @FXML
    private void handleCompleteCheckout() {
        if (shoppingCart == null
                || shoppingCart.getItems().isEmpty()) {

            System.out.println("Cart is empty.");
            return;
        }

        Credential loggedInUser =
                UserSession.getLoggedInUser();

        if (loggedInUser == null) {
            System.out.println(
                    "No user is currently logged in."
            );
            return;
        }

        try {
            Order completedOrder =
                    OrderFactory.createOrderFromCart(
                            shoppingCart,
                            loggedInUser
                    );

            OrderHistoryLoader.saveOrder(
                    completedOrder
            );

            System.out.println(
                    "Order saved successfully: "
                            + completedOrder.getOrderId()
            );

            shoppingCart.clearCart();
            cartItems.clear();

            subtotalLabel.setText("$0.00");
            taxLabel.setText("$0.00");
            totalLabel.setText("$0.00");

            promoCodeField.setText(
                    "Order completed successfully"
            );

            completeCheckoutButton.setDisable(true);

        } catch (IllegalArgumentException
                 | IllegalStateException exception) {

            exception.printStackTrace();

            promoCodeField.setText(
                    "Checkout could not be completed"
            );
        }
    }

    @FXML
    private void handleContinueShopping() {
        if (returnToShoppingAction != null) {
            returnToShoppingAction.run();
        }
    }

    @FXML
    private void handleCancel() {
        if (returnToShoppingAction != null) {
            returnToShoppingAction.run();
        }
    }

    public void removeItem(CheckoutItem item) {
        if (item == null) {
            return;
        }

        cartItems.remove(item);
        updateTotals();
    }

    public void updateQuantity(
            CheckoutItem item,
            int newQuantity
    ) {
        if (item == null) {
            return;
        }

        if (newQuantity > 0) {
            item.setQuantity(newQuantity);
            cartTable.refresh();
        } else {
            removeItem(item);
        }

        updateTotals();
    }

    public void setReturnToShoppingAction(
            Runnable returnToShoppingAction
    ) {
        this.returnToShoppingAction =
                returnToShoppingAction;
    }

    public static class CheckoutItem {

        private String itemName;
        private int quantity;
        private double price;

        public CheckoutItem(
                String itemName,
                int quantity,
                double price
        ) {
            this.itemName = itemName;
            this.quantity = quantity;
            this.price = price;
        }

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public double getTotal() {
            return quantity * price;
        }
    }
}