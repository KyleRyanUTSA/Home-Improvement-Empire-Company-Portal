package application.controller;

import application.model.CartItem;
import application.model.Product;
import application.model.ProductLoader;
import application.model.ShoppingCart;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// Controller for the main shopping page
public class MainViewController {

    private static final String IMAGE_FOLDER = "/Data/ProductImages/";
    private static final String FALLBACK_IMAGE = "default-product.png";

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private TextField searchField;

    @FXML
    private TilePane productTilePane;

    @FXML
    private ScrollPane catalogScrollPane;

    @FXML
    private Button cartButton;

    @FXML
    private Label messageLabel;

    private List<Product> allProducts;
    private List<Product> shownProducts;
    private ShoppingCart cart;

    // Runs when the main screen opens
    @FXML
    private void initialize() {
        cart = new ShoppingCart();

        // Load products from CSV
        allProducts = ProductLoader.loadProducts();
        shownProducts = new ArrayList<>(allProducts);

        showCatalogPage();
        messageLabel.setText("Welcome to Home Improvement Empire!");
    }

    // Shows the main shopping catalog
    private void showCatalogPage() {
        productTilePane.getChildren().clear();

        for (Product product : shownProducts) {
            productTilePane.getChildren().add(createProductCard(product));
        }

        mainBorderPane.setCenter(catalogScrollPane);
    }

    // Creates one product card for the catalog
    private VBox createProductCard(Product product) {
        VBox card = new VBox(8);
        card.setPrefWidth(235);
        card.setPadding(new Insets(12));

        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #dddddd;" +
                        "-fx-border-radius: 6;" +
                        "-fx-background-radius: 6;"
        );

        // Displays the product image from the Product Images folder
        ImageView productImage = createProductImage(product);

        Label nameLabel = new Label(product.getName());
        nameLabel.setWrapText(true);
        nameLabel.setStyle(
                "-fx-font-size: 15px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #0046be;"
        );

        Label categoryLabel = new Label(product.getCategory());
        categoryLabel.setStyle("-fx-text-fill: #666666;");

        Label priceLabel = new Label(
                "$" + String.format("%.2f", product.getPrice())
        );
        priceLabel.setStyle(
                "-fx-font-size: 17px;" +
                        "-fx-font-weight: bold;"
        );

        String stockText = product.isAvailable()
                ? "In Stock"
                : "Out of Stock";

        Label stockLabel = new Label(stockText);

        if (product.isAvailable()) {
            stockLabel.setStyle(
                    "-fx-text-fill: green;" +
                            "-fx-font-weight: bold;"
            );
        } else {
            stockLabel.setStyle(
                    "-fx-text-fill: red;" +
                            "-fx-font-weight: bold;"
            );
        }

        Button addButton = new Button(
                product.isAvailable()
                        ? "Add to Cart"
                        : "Unavailable"
        );

        addButton.setMaxWidth(Double.MAX_VALUE);

        if (product.isAvailable()) {
            addButton.setStyle(
                    "-fx-background-color: #FFD814;" +
                            "-fx-font-weight: bold;"
            );
        } else {
            addButton.setDisable(true);
        }

        // Add item when button is clicked
        addButton.setOnAction(
                event -> addProductToCart(product)
        );

        // Show details when card is clicked
        card.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                showProductDetails(product);
            }

            // Double click also adds to cart
            if (event.getButton() == MouseButton.PRIMARY
                    && event.getClickCount() == 2) {

                addProductToCart(product);
            }
        });

        card.getChildren().addAll(
                productImage,
                nameLabel,
                categoryLabel,
                priceLabel,
                stockLabel,
                addButton
        );

        return card;
    }

    // Creates and loads the image for one product
    private ImageView createProductImage(Product product) {
        ImageView productImage = new ImageView();

        productImage.setFitWidth(210);
        productImage.setFitHeight(130);
        productImage.setPreserveRatio(true);
        productImage.setSmooth(true);

        String imageName = product.getImageName();

        URL imageUrl = null;

        // Try to load the product's assigned image
        if (imageName != null && !imageName.isBlank()) {
            String imagePath = IMAGE_FOLDER + imageName;
            imageUrl = getClass().getResource(imagePath);
        }

        // Use the fallback image when the assigned image is missing
        if (imageUrl == null) {
            imageUrl = getClass().getResource(
                    IMAGE_FOLDER + FALLBACK_IMAGE
            );
        }

        if (imageUrl != null) {
            Image image = new Image(imageUrl.toExternalForm());
            productImage.setImage(image);
        } else {
            System.out.println(
                    "Could not find product image or fallback image."
            );
        }

        return productImage;
    }

    // Adds a product to the shopping cart
    private void addProductToCart(Product product) {
        if (!product.isAvailable()) {
            messageLabel.setText(
                    product.getName() + " is out of stock."
            );
            return;
        }

        cart.addProduct(product);
        updateCartButton();

        messageLabel.setText(
                product.getName() + " added to cart."
        );
    }

    // Shows basic product info at the bottom
    private void showProductDetails(Product product) {
        String availability = product.isAvailable()
                ? "In Stock"
                : "Out of Stock";

        messageLabel.setText(
                product.getName()
                        + " | " + product.getCategory()
                        + " | $" + String.format(
                        "%.2f",
                        product.getPrice()
                )
                        + " | " + availability
                        + " | " + product.getDescription()
        );
    }

    // Searches products by name or category
    @FXML
    private void handleSearch() {
        String searchText = searchField
                .getText()
                .toLowerCase();

        shownProducts = new ArrayList<>();

        for (Product product : allProducts) {
            if (product.getName()
                    .toLowerCase()
                    .contains(searchText)
                    || product.getCategory()
                    .toLowerCase()
                    .contains(searchText)) {

                shownProducts.add(product);
            }
        }

        showCatalogPage();
        messageLabel.setText("Search results shown.");
    }

    // Sorts current products by price
    @FXML
    private void handleSortByPrice() {
        shownProducts.sort(
                Comparator.comparingDouble(Product::getPrice)
        );

        showCatalogPage();
        messageLabel.setText("Products sorted by price.");
    }

    // Sorts current products with available items first
    @FXML
    private void handleSortByAvailability() {
        shownProducts.sort(
                (p1, p2) -> Boolean.compare(
                        p2.isAvailable(),
                        p1.isAvailable()
                )
        );

        showCatalogPage();
        messageLabel.setText(
                "In-stock products shown first."
        );
    }

    // Opens the cart page
    @FXML
    private void handleShowCart() {
        showCartPage();
    }

    // Builds the cart page
    private void showCartPage() {
        VBox cartPage = new VBox(15);
        cartPage.setPadding(new Insets(25));
        cartPage.setStyle("-fx-background-color: white;");

        Label titleLabel = new Label("Your Shopping Cart");
        titleLabel.setStyle(
                "-fx-font-size: 26px;" +
                        "-fx-font-weight: bold;"
        );

        ListView<CartItem> cartListView = new ListView<>();
        cartListView.setItems(
                FXCollections.observableArrayList(
                        cart.getItems()
                )
        );
        cartListView.setPrefHeight(320);

        // Right click removes one quantity
        cartListView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                CartItem selectedItem = cartListView
                        .getSelectionModel()
                        .getSelectedItem();

                if (selectedItem != null) {
                    cart.removeOneProduct(selectedItem);
                    showCartPage();
                    updateCartButton();
                    messageLabel.setText(
                            "Item removed from cart."
                    );
                }
            }
        });

        Button removeButton = new Button(
                "Remove Selected Item"
        );

        removeButton.setOnAction(event -> {
            CartItem selectedItem = cartListView
                    .getSelectionModel()
                    .getSelectedItem();

            if (selectedItem == null) {
                messageLabel.setText(
                        "Please select a cart item first."
                );
                return;
            }

            cart.removeOneProduct(selectedItem);
            showCartPage();
            updateCartButton();
            messageLabel.setText(
                    "Item removed from cart."
            );
        });

        Label subtotalLabel = new Label(
                "Subtotal: $" +
                        String.format(
                                "%.2f",
                                cart.getSubtotal()
                        )
        );

        Label taxLabel = new Label(
                "Tax: $" +
                        String.format(
                                "%.2f",
                                cart.getTax()
                        )
        );

        Label totalLabel = new Label(
                "Total: $" +
                        String.format(
                                "%.2f",
                                cart.getTotal()
                        )
        );

        totalLabel.setStyle(
                "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;"
        );

        Button checkoutButton = new Button("Checkout");
        checkoutButton.setStyle(
                "-fx-background-color: #f5f5f5;" +
                        "-fx-font-weight: bold;"
        );
        checkoutButton.setPrefWidth(180);
        checkoutButton.setOnAction(
                event -> handleCheckout()
        );

        Button backButton = new Button("Back to Shopping");
        backButton.setOnAction(event -> {
            showCatalogPage();
            messageLabel.setText("Back to shopping.");
        });

        cartPage.getChildren().addAll(
                titleLabel,
                cartListView,
                removeButton,
                subtotalLabel,
                taxLabel,
                totalLabel,
                checkoutButton,
                backButton
        );

        mainBorderPane.setCenter(cartPage);
        messageLabel.setText("Cart opened.");
    }

    // Simple checkout placeholder
    @FXML
    private void handleCheckout() {
        if (cart.getItems().isEmpty()) {
            messageLabel.setText("Cart is empty.");
            return;
        }

        cart.clearCart();
        updateCartButton();
        showCatalogPage();

        messageLabel.setText("Order placed!");
    }

    // Updates cart button count
    private void updateCartButton() {
        int totalItems = 0;

        for (CartItem item : cart.getItems()) {
            totalItems += item.getQuantity();
        }

        cartButton.setText(
                "Cart (" + totalItems + ")"
        );
    }
}