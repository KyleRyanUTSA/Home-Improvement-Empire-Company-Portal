# Home Improvement Empire Customer Portal

Home Improvement Empire is a JavaFX desktop application that simulates an online home improvement store. Users can create accounts, log in, browse products, search and filter inventory, manage a shopping cart, complete checkout, and view previous orders.

This project was developed for CS3773 Software Engineering.

## Team Members

- Kyle Ryan
- Zane Mullinax
- Memo Gonzales

## Main Features

### Account Management

- Create a customer account
- Log in with saved credentials
- Prevent duplicate usernames
- Validate required registration fields
- Store an optional phone number
- View customer profile information
- Log out of the application

### Product Catalog

- Display products as dynamically generated cards
- Show product image, name, description, category, price, and availability
- Search products by name or category
- Filter products by category
- Sort products by name, price, or availability
- Prevent unavailable products from being added to the cart
- Display a default image when a product image is missing

### Shopping Cart and Checkout

- Add products to the shopping cart
- Increase quantity when the same product is added again
- Display the current cart item count
- Remove selected products one quantity at a time
- Display subtotal, tax, and final total
- Apply promo codes
- Preserve the active cart between the shopping and checkout screens
- Save completed orders before clearing the cart

Supported promo codes: `SAVE10` and `SAVE20`

### Order History

- Save completed orders locally
- Display orders for the currently logged-in customer
- Show itemized order details
- Display subtotal, discount, tax, and final total
- Return to the shopping page without resetting the session

## Product Database

The product inventory is stored in a local SQLite database.

SQLite does not require a separate database server. The application connects to the database through the SQLite JDBC driver.

The original `products.csv` file is used as seed data. On the first run, the application:

1. Creates the SQLite database
2. Creates the `products` table
3. Imports all 30 products from the CSV file
4. Loads the product catalog from SQLite

The database is stored at:

    C:\Users\<username>\.heicp\store.db

The `products` table stores:

- Product ID
- Name
- Description
- Price
- Category
- Availability
- Image filename

Completed orders are currently stored at:

    C:\Users\<username>\.heicp\orders.txt

## Technologies Used

- Java 21
- JavaFX 21
- FXML
- SQLite
- SQLite JDBC
- CSV
- Git and GitHub
- IntelliJ IDEA
- Eclipse

## Setup

### Requirements

- JDK 21
- JavaFX 21
- SQLite JDBC driver

The project uses:

    sqlite-jdbc-3.53.2.1.jar

Place the JAR in the project's `lib` folder and add it to the project build path.

### IntelliJ IDEA

1. Open **File → Project Structure**
2. Select **Modules → Dependencies**
3. Add the SQLite JDBC JAR
4. Set the scope to **Compile**

### Eclipse

1. Right-click the project
2. Select **Build Path → Configure Build Path**
3. Open the **Libraries** tab
4. Add the SQLite JDBC JAR from the `lib` folder

The JavaFX run configuration should include:

    --module-path "PATH_TO_JAVAFX_LIB"
    --add-modules javafx.controls,javafx.fxml

Run the application using:

    application.Main

On the first successful run, the console should show that the database was created and the 30 products were imported.

## Viewing the Database

The SQLite database can be opened with the standalone application **DB Browser for SQLite**.

Open:

    C:\Users\<username>\.heicp\store.db

Then select the `products` table under the **Browse Data** tab.

## Current Limitations

- Password reset has not been implemented
- Customer accounts are still stored using a file-based system
- Orders are stored in a text file instead of SQLite
- Local data is stored separately on each computer
- Some additional delivery options may still need development

## Future Improvements

- Store customer accounts in SQLite
- Store orders in SQLite
- Add regular, express, and overnight delivery options
- Add inventory quantities
- Add administrator product-management features
- Improve password security and validation
- Convert the project to Maven for easier dependency management

## Team Responsibilities

- **Kyle Ryan:** Login, registration, credential validation, and account creation, Checkout screen
- **Zane Mullinax:** Main shopping view, product catalog, search, sorting, shopping cart, SQLite product database, and integration fixes
- **Memo Gonzales:** Order History interface

## License

This project was created for educational purposes as part of the CS3773 Software Engineering course.
