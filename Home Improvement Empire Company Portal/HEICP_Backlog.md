# Home Improvement Empire Customer Portal — Product Backlog

| Backlog Item ID | Type of Backlog Item | Description | Priority | Story Points | Status | Assignee | Testable State |
|---|---|---|---|---|---|---|---|
| 1 | Technical Improvement | User account management improvements | High | 20 | Complete | Kyle Ryan | Users can create accounts by using a button on the sign in page. |
| 2 | Technical Improvement | User account management improvements | High | 15 | To Do | — | Ability to reset user account passwords by clicking a button on the sign in page. |
| 3 | Quality of Life improvement | Cart Purchase system improvements | Moderate | 5 | To Do | — | When pressing 'checkout' on cart menu, display message saying that the cart is empty |
| 4 | Technical Improvement | Cart Purchase system improvements | Moderate | 5 | To Do | — | Prevent unavailable items from being added to a cart |
| 5 | Quality of Life improvement | Cart Purchase system improvements | Moderate | 7 | Complete | Zane | Add button which when pressed, clears all items from the current cart |
| 6 | Technical Improvement | Shopping system improvements | High | 15 | Complete | Zane | Add imageName column to products.csv and add imageName field to products, and allow product loader to read imageNames so that items can display their associated images |
| 7 | Technical Improvement | Shopping system improvements | Low | 1 | Complete | Memo | Add default image to display when an item does not have an associated image to display |
| 8 | Technical Improvement | Shopping system improvements | Moderate | 13 | Complete | Zane | Add separate checkout screen with optional promo codes at checkout |
| 9 | Account management | Sign up improvements | High | 5 | Complete | Kyle | Prevent users from creating duplicate accounts with the same username on registration |
| 10 | Account management | Sign up improvements | High | 5 | Complete | Kyle | Prevent users from creating accounts without a password by leaving the password field empty |
| 11 | Account management | Sign up improvements | low | 1 | Complete | Kyle | Add optional Phone number field |
| 12 | Technical Improvement | Add product loading from a CSV file | High | 8 | Complete | Zane | Products are loaded from products.csv and displayed automatically on the main shopping page. |
| 13 | Shopping System Improvement | Add products to the shopping cart from the main product catalog | High | 8 | Complete | Zane | Users can add available products to the cart by pressing the Add to Cart button. |
| 14 | Cart Purchase System Improvement | Allow users to remove products and update cart quantities | High | 8 | Complete | Zane | Users can remove selected products from the cart, and repeated products are tracked using item quantities. |
| 15 | Technical Improvement | Connect the shopping cart to the checkout controller | High | 13 | Complete | Zane | The active shopping cart is passed to the checkout screen so the correct items, quantities, and subtotal are displayed. |
| 16 | Quality of Life Improvement | Display the current cart item count on the main shopping page | Moderate | 3 | Complete | Zane | The Cart button updates automatically to show the total number of items currently in the cart. |
| 17 | Technical Improvement | Generate product cards dynamically from loaded inventory data | High | 8 | Complete | Zane | The main shopping page automatically creates a product card for every product loaded into the application. |
| 18 | Shopping System Improvement | Add product search functionality to the main shopping page | High | 8 | Complete | Zane | Users can enter a product name or category in the search field and view matching products. |
| 19 | Shopping System Improvement | Add category filtering to the product catalog | Moderate | 5 | Complete | Zane | Users can select a category from the dropdown menu and only products in that category are displayed. |
| 20 | Shopping System Improvement | Add multiple product sorting options | Moderate | 5 | Complete | Zane | Users can sort products alphabetically, by price, or by availability using the sorting dropdown menu. |
| 21 | Quality of Life Improvement | Add a button to clear search and filter selections | Low | 2 | Complete | Zane | Pressing Clear Filters resets the search field, category filter, sorting selection, and full product catalog. |
| 22 | Account Management Improvement | Display the logged-in customer's profile information | Low | 3 | Complete | Zane | Users can open the Profile window and view their username, address, and optional phone number. |
| 23 | Order Management Improvement | Add an Order History page for completed purchases | High | 13 | Complete | Memo | Logged-in users can open the Order History page and view a list of completed orders. |
| 24 | Order Management Improvement | Display itemized information for selected completed orders | Moderate | 5 | Complete | Memo | Selecting an order displays its products, quantities, prices, subtotal, discount, tax, and final total. |
| 25 | Technical Improvement | Save completed orders before clearing the shopping cart | High | 8 | Complete | Zane | Completing checkout creates and saves an Order object before the active shopping cart is cleared. |
| 26 | Technical Improvement | Store and load order history separately for each customer | High | 8 | Complete | Zane | Completed orders are saved locally and the Order History page only displays orders belonging to the logged-in username. |
| 27 | Quality of Life Improvement | Fix navigation between the shopping, cart, checkout, and order history screens | Moderate | 5 | Complete | Zane | Return buttons restore the correct shopping or cart view without logging out the user or resetting the active cart. |
| 28 | Technical Improvement | Migrate product inventory storage from CSV to SQLite | High | 13 | Complete | Zane | On first launch, all 30 products are imported from products.csv into SQLite, and the shopping page loads inventory from the products database table. |
| 29 | Quality of Life Improvement | Improve Sign in and registration page | low | 5 | Complete | Kyle | Improve the aesthetics of the login page and the registration page, make the formatting match the main view |
| 30 | Technical Improvement | Checkout Page | High | 5 | Complete | Kyle | Fix checkout screen issue where discount code and delivery labels do not change when clicked |
| 31 | Technical Improvement | Checkout Page | High | 5 | Complete | Kyle | Fix checkout screen issue where selecting a shipping type will charge for the shipping despite no items in cart |