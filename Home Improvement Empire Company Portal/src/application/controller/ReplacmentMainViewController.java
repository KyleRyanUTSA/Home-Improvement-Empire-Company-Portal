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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


// Controller for the main shopping page
public class ReplacmentMainViewController {


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



    @FXML
    private void initialize() {


        cart = new ShoppingCart();


        allProducts =
                ProductLoader.loadProducts();


        shownProducts =
                new ArrayList<>(allProducts);



        showCatalogPage();


        messageLabel.setText(
                "Welcome to Home Improvement Empire!"
        );

    }




    private void showCatalogPage(){


        productTilePane.getChildren().clear();



        for(Product product : shownProducts){


            productTilePane.getChildren()
                    .add(createProductCard(product));

        }



        mainBorderPane.setCenter(
                catalogScrollPane
        );

    }





    private VBox createProductCard(Product product){



        VBox card = new VBox(8);


        card.setPrefWidth(235);


        card.setPadding(
                new Insets(12)
        );



        card.setStyle(
                "-fx-background-color:white;" +
                "-fx-border-color:#dddddd;" +
                "-fx-border-radius:6;" +
                "-fx-background-radius:6;"
        );





        /*
         * PRODUCT IMAGE
         * Loads imageName from Product
         * Uses default.png if missing
         */


        ImageView imageBox =
                new ImageView();



        imageBox.setFitWidth(210);

        imageBox.setFitHeight(130);

        imageBox.setPreserveRatio(true);




        try{


            InputStream stream =
                    getClass()
                    .getResourceAsStream(
                       "/images/" 
                       //+ product.getImageName()
                    );



            if(stream == null){


                stream =
                getClass()
                .getResourceAsStream(
                    "/images/default.png"
                );

            }



            if(stream != null){


                imageBox.setImage(
                    new Image(stream)
                );

            }



        }
        catch(Exception e){

            e.printStackTrace();

        }






        Label nameLabel =
                new Label(
                    product.getName()
                );


        nameLabel.setWrapText(true);


        nameLabel.setStyle(
                "-fx-font-size:15px;" +
                "-fx-font-weight:bold;" +
                "-fx-text-fill:#0046be;"
        );





        Label categoryLabel =
                new Label(
                    product.getCategory()
                );


        categoryLabel.setStyle(
                "-fx-text-fill:#666666;"
        );





        Label priceLabel =
                new Label(
                    "$" +
                    String.format(
                    "%.2f",
                    product.getPrice()
                    )
                );


        priceLabel.setStyle(
                "-fx-font-size:17px;" +
                "-fx-font-weight:bold;"
        );





        Label stockLabel =
                new Label(
                    product.isAvailable()
                    ?
                    "In Stock"
                    :
                    "Out of Stock"
                );




        if(product.isAvailable()){


            stockLabel.setStyle(
                    "-fx-text-fill:green;" +
                    "-fx-font-weight:bold;"
            );


        }
        else{


            stockLabel.setStyle(
                    "-fx-text-fill:red;" +
                    "-fx-font-weight:bold;"
            );


        }





        Button addButton =
                new Button(
                    product.isAvailable()
                    ?
                    "Add to Cart"
                    :
                    "Unavailable"
                );



        addButton.setMaxWidth(
                Double.MAX_VALUE
        );



        if(product.isAvailable()){


            addButton.setStyle(
                    "-fx-background-color:#FFD814;" +
                    "-fx-font-weight:bold;"
            );



            addButton.setOnAction(event -> {

                addProductToCart(product);

            });



        }
        else{


            addButton.setDisable(true);


        }






        card.setOnMouseClicked(event -> {



            if(event.getButton()
                    == MouseButton.PRIMARY){


                showProductDetails(product);


            }




            if(event.getButton()
                    == MouseButton.PRIMARY
                    &&
               event.getClickCount()==2){


                addProductToCart(product);


            }


        });







        card.getChildren().addAll(

                imageBox,

                nameLabel,

                categoryLabel,

                priceLabel,

                stockLabel,

                addButton

        );



        return card;


    }
      // Adds product to shopping cart
    private void addProductToCart(Product product) {


        // Prevent unavailable products
        if(!product.isAvailable()){


            messageLabel.setText(
                    product.getName()
                    + " is out of stock."
            );


            return;

        }



        cart.addProduct(product);


        updateCartButton();



        messageLabel.setText(
                product.getName()
                + " added to cart."
        );

    }





    // Displays product details
    private void showProductDetails(Product product){


        String availability =
                product.isAvailable()
                ?
                "In Stock"
                :
                "Out of Stock";



        messageLabel.setText(

                product.getName()
                + " | "
                + product.getCategory()
                + " | $"
                + String.format(
                        "%.2f",
                        product.getPrice()
                )
                + " | "
                + availability
                + " | "
                + product.getDescription()

        );


    }





    // Search products
    @FXML
    private void handleSearch(){


        String searchText =
                searchField.getText()
                .toLowerCase();



        shownProducts =
                new ArrayList<>();




        for(Product product : allProducts){


            if(product.getName()
                    .toLowerCase()
                    .contains(searchText)
                    ||

               product.getCategory()
                    .toLowerCase()
                    .contains(searchText)){



                shownProducts.add(product);


            }


        }




        showCatalogPage();



        messageLabel.setText(
                "Search results shown."
        );


    }





    // Sort by price
    @FXML
    private void handleSortByPrice(){



        shownProducts.sort(
                Comparator.comparingDouble(
                        Product::getPrice
                )
        );



        showCatalogPage();



        messageLabel.setText(
                "Products sorted by price."
        );


    }





    // Sort available items first
    @FXML
    private void handleSortByAvailability(){



        shownProducts.sort(
                (p1,p2) ->
                Boolean.compare(
                    p2.isAvailable(),
                    p1.isAvailable()
                )
        );



        showCatalogPage();



        messageLabel.setText(
                "Available products shown first."
        );


    }





    // Open cart
    @FXML
    private void handleShowCart(){


        showCartPage();


    }







    // Builds cart page
    private void showCartPage(){



        VBox cartPage =
                new VBox(15);



        cartPage.setPadding(
                new Insets(25)
        );



        cartPage.setStyle(
                "-fx-background-color:white;"
        );






        Label titleLabel =
                new Label(
                        "Your Shopping Cart"
                );


        titleLabel.setStyle(
                "-fx-font-size:26px;" +
                "-fx-font-weight:bold;"
        );







        ListView<CartItem> cartListView =
                new ListView<>();



        cartListView.setPrefHeight(
                320
        );





        // Empty cart message
        if(cart.getItems().isEmpty()){


            Label emptyLabel =
                    new Label(
                        "Your cart is empty."
                    );


            emptyLabel.setStyle(
                    "-fx-font-size:18px;" +
                    "-fx-font-weight:bold;"
            );



            cartPage.getChildren()
                    .add(emptyLabel);



        }
        else{


            cartListView.setItems(
                FXCollections.observableArrayList(
                    cart.getItems()
                )
            );



        }








        // Remove one item
        cartListView.setOnMouseClicked(event -> {



            if(event.getButton()
                    == MouseButton.SECONDARY){



                CartItem selected =
                    cartListView
                    .getSelectionModel()
                    .getSelectedItem();




                if(selected != null){



                    cart.removeOneProduct(
                            selected
                    );


                    showCartPage();


                    updateCartButton();



                    messageLabel.setText(
                            "Item removed."
                    );


                }


            }


        });








        Button removeButton =
                new Button(
                        "Remove Selected Item"
                );



        removeButton.setOnAction(event -> {



            CartItem selected =
                cartListView
                .getSelectionModel()
                .getSelectedItem();




            if(selected == null){


                messageLabel.setText(
                    "Select an item first."
                );


                return;

            }



            cart.removeOneProduct(
                    selected
            );


            showCartPage();


            updateCartButton();



            messageLabel.setText(
                    "Item removed."
            );


        });








        Label subtotalLabel =
                new Label(
                "Subtotal: $"
                +
                String.format(
                    "%.2f",
                    cart.getSubtotal()
                )
        );



        Label taxLabel =
                new Label(
                "Tax: $"
                +
                String.format(
                    "%.2f",
                    cart.getTax()
                )
        );



        Label totalLabel =
                new Label(
                "Total: $"
                +
                String.format(
                    "%.2f",
                    cart.getTotal()
                )
        );



        totalLabel.setStyle(
                "-fx-font-size:18px;" +
                "-fx-font-weight:bold;"
        );







        // Clear cart button
        Button clearButton =
                new Button(
                        "Clear Cart"
                );



        clearButton.setOnAction(event -> {



            cart.clearCart();



            updateCartButton();



            showCartPage();



            messageLabel.setText(
                    "Cart cleared."
            );


        });








        // Checkout button
        Button checkoutButton =
                new Button(
                        "Checkout"
                );



        checkoutButton.setStyle(
                "-fx-font-weight:bold;"
        );



        // Disable checkout when empty
        checkoutButton.setDisable(
                cart.getItems().isEmpty()
        );



        checkoutButton.setOnAction(event -> {


            handleCheckout();


        });






        Button backButton =
                new Button(
                        "Back to Shopping"
                );



        backButton.setOnAction(event -> {



            showCatalogPage();



            messageLabel.setText(
                    "Back to shopping."
            );


        });
        // Add all cart components to page
        cartPage.getChildren().addAll(

                titleLabel,

                cartListView,

                removeButton,

                subtotalLabel,

                taxLabel,

                totalLabel,

                clearButton,

                checkoutButton,

                backButton

        );



        mainBorderPane.setCenter(cartPage);



        messageLabel.setText(
                "Cart opened."
        );


    }







    // Checkout process
    @FXML
    private void handleCheckout(){



        if(cart.getItems().isEmpty()){


            messageLabel.setText(
                    "Cart is empty."
            );


            return;


        }





        // Order processing placeholder
        System.out.println(
                "Processing order..."
        );



        System.out.println(
                "Total: $"
                +
                String.format(
                    "%.2f",
                    cart.getTotal()
                )
        );






        cart.clearCart();



        updateCartButton();



        showCatalogPage();



        messageLabel.setText(
                "Order placed successfully!"
        );


    }








    // Updates cart button count
    private void updateCartButton(){



        int totalItems = 0;



        for(CartItem item : cart.getItems()){


            totalItems += item.getQuantity();


        }




        cartButton.setText(
                "Cart (" 
                + totalItems 
                + ")"
        );


    }



}
