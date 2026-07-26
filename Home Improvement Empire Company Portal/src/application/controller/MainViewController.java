package application.controller;

import application.model.CartItem;
import application.model.Product;
import application.model.ProductLoader;
import application.model.ShoppingCart;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// Controller for the main shopping page
public class MainViewController {

    private static final String IMAGE_FOLDER = "/image/";
    private static final String FALLBACK_IMAGE = "default-product.jpg";

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

    // Stores the selected checkout option
    private String fulfillmentOption = "";
    private String deliveryAddress = "";

    // Runs when the main screen opens
    @FXML
    private void initialize() {
        cart = new ShoppingCart();

        // Load products from CSV
        allProducts = ProductLoader.loadProducts();
        shownProducts = new ArrayList<>(allProducts);

        showCatalogPage();
        updateCartButton();
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

        // Loads the product image
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
        addButton.setOnAction(event -> addProductToCart(product));

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

        URL imageUrl = null;
        String imageName = product.getImageName();

        // Try to load the assigned product image
        if (imageName != null && !imageName.isBlank()) {
            imageUrl = getClass().getResource(
                    IMAGE_FOLDER + imageName
            );
        }

        // Use the fallback image if the assigned image is missing
        if (imageUrl == null) {
            imageUrl = getClass().getResource(
                    IMAGE_FOLDER + FALLBACK_IMAGE
            );
        }

        if (imageUrl != null) {
            productImage.setImage(
                    new Image(imageUrl.toExternalForm())
            );
        } else {
            System.out.println(
                    "Product image and fallback image were not found."
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
                FXCollections.observableArrayList(cart.getItems())
        );
        cartListView.setPrefHeight(260);

        // Right click removes one quantity
        cartListView.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                CartItem selectedItem =
                        cartListView.getSelectionModel().getSelectedItem();

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
            CartItem selectedItem =
                    cartListView.getSelectionModel().getSelectedItem();

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

        TextField discountField = new TextField();
        discountField.setPromptText("Enter discount code");
        discountField.setPrefWidth(180);

        Button applyDiscountButton = new Button("Apply Code");

        applyDiscountButton.setOnAction(event -> {
            boolean valid =
                    cart.applyDiscountCode(discountField.getText());

            showCartPage();

            if (valid) {
                int percent =
                        (int) (cart.getDiscountRate() * 100);

                messageLabel.setText(
                        percent + "% discount applied."
                );
            } else {
                messageLabel.setText(
                        "Invalid discount code."
                );
            }
        });

        HBox discountBox = new HBox(
                10,
                discountField,
                applyDiscountButton
        );

        // Delivery or pickup section
        Label fulfillmentLabel = new Label(
                "Choose delivery or store pickup:"
        );
        fulfillmentLabel.setStyle("-fx-font-weight: bold;");

        RadioButton deliveryButton = new RadioButton("Delivery");
        RadioButton pickupButton = new RadioButton("Store Pickup");

        ToggleGroup fulfillmentGroup = new ToggleGroup();
        deliveryButton.setToggleGroup(fulfillmentGroup);
        pickupButton.setToggleGroup(fulfillmentGroup);

        // Restore the selected option when the cart refreshes
        if (fulfillmentOption.equals("Delivery")) {
            deliveryButton.setSelected(true);
        } else if (fulfillmentOption.equals("Pickup")) {
            pickupButton.setSelected(true);
        }

        HBox fulfillmentBox = new HBox(
                20,
                deliveryButton,
                pickupButton
        );

        // Delivery address fields
        TextField streetField = new TextField();
        streetField.setPromptText("Street address");

        TextField cityField = new TextField();
        cityField.setPromptText("City");

        TextField stateField = new TextField();
        stateField.setPromptText("State");
        stateField.setPrefWidth(90);

        TextField zipField = new TextField();
        zipField.setPromptText("ZIP code");
        zipField.setPrefWidth(110);

        HBox cityStateZipBox = new HBox(
                10,
                cityField,
                stateField,
                zipField
        );

        VBox addressBox = new VBox(
                8,
                streetField,
                cityStateZipBox
        );

        // Only show address fields when delivery is selected
        addressBox.setVisible(deliveryButton.isSelected());
        addressBox.setManaged(deliveryButton.isSelected());

        deliveryButton.setOnAction(event -> {
            fulfillmentOption = "Delivery";

            addressBox.setVisible(true);
            addressBox.setManaged(true);

            messageLabel.setText("Delivery selected.");
        });

        pickupButton.setOnAction(event -> {
            fulfillmentOption = "Pickup";
            deliveryAddress = "";

            addressBox.setVisible(false);
            addressBox.setManaged(false);

            messageLabel.setText("Store pickup selected.");
        });

        Label subtotalLabel = new Label(
                "Subtotal: $" +
                        String.format(
                                "%.2f",
                                cart.getSubtotal()
                        )
        );

        Label discountLabel = new Label(
                "Discount: -$" +
                        String.format(
                                "%.2f",
                                cart.getDiscountAmount()
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

        checkoutButton.setOnAction(event -> {
            if (deliveryButton.isSelected()) {
                String street = streetField.getText().trim();
                String city = cityField.getText().trim();
                String state = stateField.getText().trim();
                String zip = zipField.getText().trim();

                if (street.isEmpty()
                        || city.isEmpty()
                        || state.isEmpty()
                        || zip.isEmpty()) {

                    messageLabel.setText(
                            "Please complete the delivery address."
                    );
                    return;
                }

                fulfillmentOption = "Delivery";

                deliveryAddress =
                        street + ", "
                                + city + ", "
                                + state + " "
                                + zip;
            } else if (pickupButton.isSelected()) {
                fulfillmentOption = "Pickup";
                deliveryAddress = "";
            }

            handleCheckout();
        });

        Button backButton = new Button("Back to Shopping");

        backButton.setOnAction(event -> {
            showCatalogPage();
            messageLabel.setText("Back to shopping.");
        });

        cartPage.getChildren().addAll(
                titleLabel,
                cartListView,
                removeButton,
                discountBox,
                fulfillmentLabel,
                fulfillmentBox,
                addressBox,
                subtotalLabel,
                discountLabel,
                taxLabel,
                totalLabel,
                checkoutButton,
                backButton
        );

        // Allows the cart page to scroll when address fields are shown
        ScrollPane cartScrollPane = new ScrollPane(cartPage);
        cartScrollPane.setFitToWidth(true);

        mainBorderPane.setCenter(cartScrollPane);
        messageLabel.setText("Cart opened.");
    }

    // Completes the checkout
    @FXML
    private void handleCheckout() {
        if (cart.getItems().isEmpty()) {
            messageLabel.setText("Cart is empty.");
            return;
        }

        if (fulfillmentOption.isEmpty()) {
            messageLabel.setText(
                    "Please choose delivery or store pickup."
            );
            return;
        }

        String confirmationMessage;

        if (fulfillmentOption.equals("Delivery")) {
            confirmationMessage =
                    "Order placed for delivery to "
                            + deliveryAddress
                            + "!";
        } else {
            confirmationMessage =
                    "Order placed for store pickup!";
        }

        cart.clearCart();
        fulfillmentOption = "";
        deliveryAddress = "";

        updateCartButton();
        showCatalogPage();

        messageLabel.setText(confirmationMessage);
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