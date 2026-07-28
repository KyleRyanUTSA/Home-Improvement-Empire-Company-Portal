package application.controller;

import application.SceneManager;
import application.model.CartItem;
import application.model.Credential;
import application.model.Product;
import application.model.ProductLoader;
import application.model.ProductDatabase;
import application.model.ShoppingCart;
import application.model.UserSession;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.control.Separator;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import application.controller.OrderHistoryController;

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
    private Button profileButton;

    @FXML
    private ComboBox<String> categoryComboBox;

    @FXML
    private ComboBox<String> sortComboBox;

    @FXML
    private Label messageLabel;

    private List<Product> allProducts;
    private List<Product> shownProducts;
    private ShoppingCart cart;

    // Runs when the main screen opens
    @FXML
    private void initialize() {
        cart = new ShoppingCart();

        allProducts = ProductDatabase.loadProducts();
        shownProducts = new ArrayList<>(allProducts);

        initializeCategoryOptions();
        initializeSortOptions();

        showCatalogPage();
        updateCartButton();

        Credential loggedInUser =
                UserSession.getLoggedInUser();

        if (loggedInUser != null) {
            profileButton.setText(
                    "Profile: " + loggedInUser.getUsername()
            );

            messageLabel.setText(
                    "Welcome, "
                            + loggedInUser.getUsername()
                            + "!"
            );
        } else {
            messageLabel.setText(
                    "Welcome to Home Improvement Empire!"
            );
        }
    }

    // Adds the available product categories to the dropdown
    private void initializeCategoryOptions() {
        Set<String> categories = new LinkedHashSet<>();

        for (Product product : allProducts) {
            if (product.getCategory() != null
                    && !product.getCategory().isBlank()) {

                categories.add(product.getCategory());
            }
        }

        categoryComboBox.getItems().add(
                "All Categories"
        );

        categoryComboBox.getItems().addAll(
                categories
        );

        categoryComboBox.setValue(
                "All Categories"
        );
    }

    // Adds the available sorting choices to the dropdown
    private void initializeSortOptions() {
        sortComboBox.getItems().addAll(
                "Name: A to Z",
                "Name: Z to A",
                "Price: Low to High",
                "Price: High to Low",
                "In Stock First"
        );
    }

    // Shows the main shopping catalog
    private void showCatalogPage() {
        productTilePane.getChildren().clear();

        for (Product product : shownProducts) {
            productTilePane.getChildren().add(
                    createProductCard(product)
            );
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

        Label categoryLabel = new Label(
                product.getCategory()
        );

        categoryLabel.setStyle(
                "-fx-text-fill: #666666;"
        );

        Label priceLabel = new Label(
                "$" + String.format(
                        "%.2f",
                        product.getPrice()
                )
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

        URL imageUrl = null;
        String imageName = product.getImageName();

        // Try to load the assigned product image
        if (imageName != null && !imageName.isBlank()) {
            imageUrl = getClass().getResource(
                    IMAGE_FOLDER + imageName
            );
        }

        // Use fallback image if the assigned image is missing
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
        applyFilters();

        messageLabel.setText(
                shownProducts.size()
                        + " search result(s) shown."
        );
    }

    // Filters products using the search text and category
    private void applyFilters() {
        String searchText = searchField.getText();

        if (searchText == null) {
            searchText = "";
        }

        searchText = searchText
                .trim()
                .toLowerCase();

        String selectedCategory =
                categoryComboBox.getValue();

        shownProducts = new ArrayList<>();

        for (Product product : allProducts) {
            String productName = product.getName() == null
                    ? ""
                    : product.getName().toLowerCase();

            String productCategory =
                    product.getCategory() == null
                            ? ""
                            : product.getCategory().toLowerCase();

            boolean matchesSearch =
                    productName.contains(searchText)
                            || productCategory.contains(searchText);

            boolean matchesCategory =
                    selectedCategory == null
                            || selectedCategory.equals(
                            "All Categories"
                    )
                            || productCategory.equals(
                            selectedCategory.toLowerCase()
                    );

            if (matchesSearch && matchesCategory) {
                shownProducts.add(product);
            }
        }

        applySelectedSort();
        showCatalogPage();
    }

    // Filters products when a category is selected
    @FXML
    private void handleCategoryFilter() {
        applyFilters();

        messageLabel.setText(
                shownProducts.size()
                        + " product(s) shown."
        );
    }

    // Sorts products using the selected dropdown option
    @FXML
    private void handleSortSelection() {
        applySelectedSort();
        showCatalogPage();

        String selectedSort = sortComboBox.getValue();

        if (selectedSort != null) {
            messageLabel.setText(
                    "Products sorted by "
                            + selectedSort
                            + "."
            );
        }
    }

    // Applies the selected sorting option
    private void applySelectedSort() {
        String selectedSort =
                sortComboBox.getValue();

        if (selectedSort == null) {
            return;
        }

        if (selectedSort.equals("Name: A to Z")) {
            shownProducts.sort(
                    Comparator.comparing(
                            Product::getName,
                            String.CASE_INSENSITIVE_ORDER
                    )
            );
        } else if (selectedSort.equals("Name: Z to A")) {
            shownProducts.sort(
                    Comparator.comparing(
                            Product::getName,
                            String.CASE_INSENSITIVE_ORDER
                    ).reversed()
            );
        } else if (selectedSort.equals(
                "Price: Low to High"
        )) {
            shownProducts.sort(
                    Comparator.comparingDouble(
                            Product::getPrice
                    )
            );
        } else if (selectedSort.equals(
                "Price: High to Low"
        )) {
            shownProducts.sort(
                    Comparator.comparingDouble(
                            Product::getPrice
                    ).reversed()
            );
        } else if (selectedSort.equals(
                "In Stock First"
        )) {
            shownProducts.sort(
                    (p1, p2) -> Boolean.compare(
                            p2.isAvailable(),
                            p1.isAvailable()
                    )
            );
        }
    }

    // Clears search, category, and sorting selections
    @FXML
    private void handleClearFilters() {
        searchField.clear();

        categoryComboBox.setValue(
                "All Categories"
        );

        sortComboBox
                .getSelectionModel()
                .clearSelection();

        shownProducts =
                new ArrayList<>(allProducts);

        showCatalogPage();

        messageLabel.setText(
                "Filters cleared."
        );
    }

    // Original price sorting method
    @FXML
    private void handleSortByPrice() {
        shownProducts.sort(
                Comparator.comparingDouble(Product::getPrice)
        );

        showCatalogPage();

        messageLabel.setText(
                "Products sorted by price."
        );
    }

    // Original availability sorting method
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
        cartPage.setStyle(
                "-fx-background-color: white;"
        );

        Label titleLabel = new Label(
                "Your Shopping Cart"
        );

        titleLabel.setStyle(
                "-fx-font-size: 26px;" +
                        "-fx-font-weight: bold;"
        );

        ListView<CartItem> cartListView =
                new ListView<>();

        cartListView.setItems(
                FXCollections.observableArrayList(
                        cart.getItems()
                )
        );

        cartListView.setPrefHeight(320);

        // Right click removes one quantity
        cartListView.setOnMouseClicked(event -> {
            if (event.getButton()
                    == MouseButton.SECONDARY) {

                CartItem selectedItem =
                        cartListView
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
            CartItem selectedItem =
                    cartListView
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

        // Cart only shows subtotal before checkout
        Label subtotalLabel = new Label(
                "Cart Subtotal: $"
                        + String.format(
                        "%.2f",
                        cart.getSubtotal()
                )
        );

        subtotalLabel.setStyle(
                "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;"
        );

        Button checkoutButton =
                new Button("Checkout");

        checkoutButton.setStyle(
                "-fx-background-color: #1F4D2B;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;"
        );

        checkoutButton.setPrefWidth(180);

        // Opens the separate checkout screen
        checkoutButton.setOnAction(event -> {
            if (cart.getItems().isEmpty()) {
                messageLabel.setText(
                        "Cart is empty."
                );
                return;
            }

            openCheckoutPage();
        });

        Button backButton =
                new Button("Back to Shopping");

        backButton.setOnAction(event -> {
            showCatalogPage();

            messageLabel.setText(
                    "Back to shopping."
            );
        });

        cartPage.getChildren().addAll(
                titleLabel,
                cartListView,
                removeButton,
                subtotalLabel,
                checkoutButton,
                backButton
        );

        mainBorderPane.setCenter(cartPage);
        messageLabel.setText("Cart opened.");
    }

    // Opens the logged-in user's profile
    @FXML
    private void handleShowProfile() {
        Credential loggedInUser =
                UserSession.getLoggedInUser();

        if (loggedInUser == null) {
            Alert alert = new Alert(
                    Alert.AlertType.WARNING
            );

            alert.setTitle("Profile");
            alert.setHeaderText("No User Logged In");

            alert.setContentText(
                    "Please log in to view your profile."
            );

            alert.showAndWait();
            return;
        }

        showProfileDialog(loggedInUser);
    }

    // Displays the logged-in user's account information
    private void showProfileDialog(Credential user) {
        Dialog<ButtonType> profileDialog = new Dialog<>();

        profileDialog.setTitle("Customer Profile");
        profileDialog.setHeaderText(null);

        profileDialog.getDialogPane()
                .getButtonTypes()
                .add(ButtonType.CLOSE);

        VBox profilePage = new VBox(18);
        profilePage.setPrefWidth(430);
        profilePage.setPadding(new Insets(0));

        // Green profile header
        VBox profileHeader = new VBox(6);
        profileHeader.setPadding(
                new Insets(22, 25, 22, 25)
        );

        profileHeader.setStyle(
                "-fx-background-color: #1F4D2B;" +
                        "-fx-background-radius: 6 6 0 0;"
        );

        Label profileIcon = new Label("👤");
        profileIcon.setStyle(
                "-fx-font-size: 34px;"
        );

        Label profileTitle = new Label(
                "Customer Profile"
        );

        profileTitle.setStyle(
                "-fx-text-fill: white;" +
                        "-fx-font-size: 22px;" +
                        "-fx-font-weight: bold;"
        );

        Label profileSubtitle = new Label(
                "View your account information"
        );

        profileSubtitle.setStyle(
                "-fx-text-fill: #D5E5D9;" +
                        "-fx-font-size: 12px;"
        );

        profileHeader.getChildren().addAll(
                profileIcon,
                profileTitle,
                profileSubtitle
        );

        // Main account information section
        VBox informationSection = new VBox(15);
        informationSection.setPadding(
                new Insets(5, 25, 5, 25)
        );

        Label informationTitle = new Label(
                "Account Details"
        );

        informationTitle.setStyle(
                "-fx-font-size: 16px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #1F4D2B;"
        );

        Separator separator = new Separator();

        GridPane profileGrid = new GridPane();
        profileGrid.setHgap(20);
        profileGrid.setVgap(18);

        Label usernameTitle = new Label(
                "Username"
        );

        usernameTitle.setStyle(
                "-fx-font-weight: bold;" +
                        "-fx-text-fill: #555555;"
        );

        Label usernameValue = new Label(
                user.getUsername()
        );

        usernameValue.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-text-fill: #222222;"
        );

        Label addressTitle = new Label(
                "Address"
        );

        addressTitle.setStyle(
                "-fx-font-weight: bold;" +
                        "-fx-text-fill: #555555;"
        );

        Label addressValue = new Label(
                user.getAddress()
        );

        addressValue.setWrapText(true);
        addressValue.setMaxWidth(260);

        addressValue.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-text-fill: #222222;"
        );

        Label phoneTitle =
                new Label("Phone Number");

        phoneTitle.setStyle(
                "-fx-font-weight: bold;" +
                        "-fx-text-fill: #555555;"
        );

        String phoneNumber = user.getPhoneNumber();

        if (phoneNumber == null || phoneNumber.isBlank()) {
            phoneNumber = "Not provided";
        }

        Label phoneValue =
                new Label(phoneNumber);

        phoneValue.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-text-fill: #222222;"
        );

        profileGrid.add(usernameTitle, 0, 0);
        profileGrid.add(usernameValue, 1, 0);

        profileGrid.add(addressTitle, 0, 1);
        profileGrid.add(addressValue, 1, 1);

        profileGrid.add(phoneTitle, 0, 2);
        profileGrid.add(phoneValue, 1, 2);

        informationSection.getChildren().addAll(
                informationTitle,
                separator,
                profileGrid
        );

        Label accountMessage = new Label(
                "Your account information is used to help complete your orders."
        );

        accountMessage.setWrapText(true);
        accountMessage.setPadding(
                new Insets(12)
        );

        accountMessage.setStyle(
                "-fx-background-color: #EEF5F0;" +
                        "-fx-background-radius: 6;" +
                        "-fx-text-fill: #4A5D50;" +
                        "-fx-font-size: 12px;"
        );

        VBox.setMargin(
                accountMessage,
                new Insets(0, 25, 5, 25)
        );

        profilePage.getChildren().addAll(
                profileHeader,
                informationSection,
                accountMessage
        );

        DialogPane dialogPane =
                profileDialog.getDialogPane();

        dialogPane.setContent(profilePage);

        dialogPane.setStyle(
                "-fx-background-color: white;"
        );

        // Style the Close button to match the store theme
        Button closeButton =
                (Button) dialogPane.lookupButton(
                        ButtonType.CLOSE
                );

        closeButton.setText("Close Profile");

        closeButton.setStyle(
                "-fx-background-color: #1F4D2B;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 5;" +
                        "-fx-padding: 8 18 8 18;"
        );

        profileDialog.showAndWait();
    }
    // Logs the current user out
    @FXML
    private void handleLogout() {
        UserSession.logout();

        SceneManager.switchTo(
                "/Data/views/HIELS.fxml"
        );
    }

    // Opens Checkout.fxml and passes the existing cart
    private void openCheckoutPage() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/Data/views/Checkout.fxml"
                    )
            );

            Parent checkoutPage = loader.load();

            CheckoutController checkoutController =
                    loader.getController();

            // Pass the real shopping cart to checkout
            checkoutController.setCart(cart);

            // Allows checkout to return to the cart page
            checkoutController.setReturnToShoppingAction(
                    () -> {
                        showCartPage();
                        updateCartButton();

                        messageLabel.setText(
                                "Returned to cart."
                        );
                    }
            );

            mainBorderPane.setCenter(checkoutPage);

            messageLabel.setText(
                    "Checkout opened."
            );

        } catch (IOException e) {
            e.printStackTrace();

            messageLabel.setText(
                    "The checkout page could not be opened."
            );
        }
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

    @FXML
    private void handleShowOrderHistory() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/Data/views/OrderHistory.fxml"
                    )
            );

            Parent orderHistoryPage = loader.load();

            OrderHistoryController orderHistoryController =
                    loader.getController();

            orderHistoryController.setReturnToShoppingAction(() -> {
                showCatalogPage();
                updateCartButton();

                messageLabel.setText(
                        "Back to shopping."
                );
            });

            mainBorderPane.setCenter(orderHistoryPage);

            messageLabel.setText(
                    "Order history opened."
            );

        } catch (IOException e) {
            e.printStackTrace();

            messageLabel.setText(
                    "The order history page could not be opened."
            );
        }
    }
}