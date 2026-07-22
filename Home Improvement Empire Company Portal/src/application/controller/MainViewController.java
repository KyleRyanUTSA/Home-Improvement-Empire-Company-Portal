package application.controller;

import application.model.CartItem;
import application.model.Product;
import application.model.ProductLoader;
import application.model.ShoppingCart;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// Controller for the main customer store screen
public class MainViewController {

    @FXML
    private TextField searchField;

    @FXML
    private ListView<Product> productListView;

    @FXML
    private ListView<CartItem> cartListView;

    @FXML
    private Label productDetailsLabel;

    @FXML
    private Label subtotalLabel;

    @FXML
    private Label taxLabel;

    @FXML
    private Label totalLabel;

    @FXML
    private Label messageLabel;

    private List<Product> allProducts;
    private ShoppingCart cart;

    // Runs when the FXML screen opens
    @FXML
    private void initialize() {
        cart = new ShoppingCart();

        // Load products from the CSV file
        allProducts = ProductLoader.loadProducts();

        showProducts(allProducts);
        updateCartDisplay();

        // Double click a product to add it to the cart
        productListView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                handleAddToCart();
            }
        });

        // Right click a cart item to remove one quantity
        cartListView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                handleRemoveFromCart();
            }
        });

        messageLabel.setText("Welcome to Home Improvement Empire!");
    }

    // Shows products in the product list
    private void showProducts(List<Product> products) {
        ObservableList<Product> productObservableList = FXCollections.observableArrayList(products);
        productListView.setItems(productObservableList);
    }

    // Searches products by name or category
    @FXML
    private void handleSearch() {
        String searchText = searchField.getText().toLowerCase();
        List<Product> results = new ArrayList<>();

        for (Product product : allProducts) {
            if (product.getName().toLowerCase().contains(searchText)
                    || product.getCategory().toLowerCase().contains(searchText)) {
                results.add(product);
            }
        }

        showProducts(results);
        messageLabel.setText("Search results shown.");
    }

    // Sorts products by lowest price first
    @FXML
    private void handleSortByPrice() {
        List<Product> sortedProducts = new ArrayList<>(productListView.getItems());

        sortedProducts.sort(Comparator.comparingDouble(Product::getPrice));

        showProducts(sortedProducts);
        messageLabel.setText("Products sorted by price.");
    }

    // Sorts products with in-stock items first
    @FXML
    private void handleSortByAvailability() {
        List<Product> sortedProducts = new ArrayList<>(productListView.getItems());

        sortedProducts.sort((p1, p2) -> Boolean.compare(p2.isAvailable(), p1.isAvailable()));

        showProducts(sortedProducts);
        messageLabel.setText("Available products shown first.");
    }

    // Shows information about the selected product
    @FXML
    private void handleViewDetails() {
        Product selectedProduct = productListView.getSelectionModel().getSelectedItem();

        if (selectedProduct == null) {
            messageLabel.setText("Please select a product first.");
            return;
        }

        String availability = selectedProduct.isAvailable() ? "In Stock" : "Out of Stock";

        productDetailsLabel.setText(
                "Name: " + selectedProduct.getName()
                        + "\nCategory: " + selectedProduct.getCategory()
                        + "\nPrice: $" + String.format("%.2f", selectedProduct.getPrice())
                        + "\nAvailability: " + availability
                        + "\nDescription: " + selectedProduct.getDescription()
        );

        messageLabel.setText("Product details shown.");
    }

    // Adds selected product to cart
    @FXML
    private void handleAddToCart() {
        Product selectedProduct = productListView.getSelectionModel().getSelectedItem();

        if (selectedProduct == null) {
            messageLabel.setText("Please select a product first.");
            return;
        }

        if (!selectedProduct.isAvailable()) {
            messageLabel.setText("This item is out of stock.");
            return;
        }

        cart.addProduct(selectedProduct);
        updateCartDisplay();

        messageLabel.setText(selectedProduct.getName() + " added to cart.");
    }

    // Removes one quantity from selected cart item
    @FXML
    private void handleRemoveFromCart() {
        CartItem selectedItem = cartListView.getSelectionModel().getSelectedItem();

        if (selectedItem == null) {
            messageLabel.setText("Please select a cart item first.");
            return;
        }

        cart.removeOneProduct(selectedItem);
        updateCartDisplay();

        messageLabel.setText("Item removed from cart.");
    }

    // Simple checkout placeholder for now
    @FXML
    private void handleCheckout() {
        if (cart.getItems().isEmpty()) {
            messageLabel.setText("Cart is empty.");
            return;
        }

        messageLabel.setText("Order placed! Checkout screen can be added later.");
        cart.clearCart();
        updateCartDisplay();
    }

    // Updates the cart list and price totals
    private void updateCartDisplay() {
        cartListView.setItems(FXCollections.observableArrayList(cart.getItems()));

        subtotalLabel.setText("Subtotal: $" + String.format("%.2f", cart.getSubtotal()));
        taxLabel.setText("Tax: $" + String.format("%.2f", cart.getTax()));
        totalLabel.setText("Total: $" + String.format("%.2f", cart.getTotal()));
    }
}