# FoodHub - Database Design

## 1. User

Stores information about customers, restaurant owners, and administrators.

### Fields

```
id
name
email
password
phone
role
status
created_at
updated_at
```

### Role

```
CUSTOMER
RESTAURANT_OWNER
ADMIN
```

---

## 2. Restaurant

Stores restaurant information and identifies its owner.

### Fields

```
id
owner_id
name
description
phone
address_line
city
state
pincode
latitude
longitude
status
opening_time
closing_time
created_at
updated_at
```

### Relationship

```
User 1 ───────── N Restaurant
```

The `owner_id` references the `User` who owns the restaurant.

Only users with the `RESTAURANT_OWNER` role should be allowed to manage the restaurant.

---

## 3. Category

Stores food categories belonging to a restaurant.

Examples:

```
Pizza
Burgers
Beverages
Desserts
Main Course
```

### Fields

```
id
restaurant_id
name
description
created_at
updated_at
```

### Relationship

```
Restaurant 1 ───────── N Category
```

---

## 4. FoodItem

Stores food/menu items offered by a restaurant.

### Fields

```
id
restaurant_id
category_id
name
description
price
image_url
is_available
created_at
updated_at
```

### Relationships

```
Restaurant 1 ───────── N FoodItem

Category 1 ───────── N FoodItem
```

---

## 5. Address

Stores reusable delivery addresses belonging to customers.

### Fields

```
id
user_id
address_line
city
state
pincode
latitude
longitude
is_default
created_at
updated_at
```

### Relationship

```
User 1 ───────── N Address
```

A customer can save multiple delivery addresses.

---

## 6. Cart

Stores the active shopping cart of a customer.

### Fields

```
id
user_id
restaurant_id
created_at
updated_at
```

### Relationships

```
User 1 ───────── 1 Active Cart

Restaurant 1 ───────── N Cart
```

A cart can contain food items from only one restaurant.

---

## 7. CartItem

Stores individual food items inside a cart.

### Fields

```
id
cart_id
food_item_id
quantity
price
created_at
updated_at
```

### Relationships

```
Cart 1 ───────── N CartItem

FoodItem 1 ───────── N CartItem
```

The `price` represents the food item's price when it was added or updated in the cart.

However, the backend must revalidate the current `FoodItem` price during checkout.

---

## 8. Order

Stores customer orders.

### Fields

```
id
user_id
restaurant_id

total_amount
discount_amount
delivery_fee
final_amount

status
payment_status

delivery_address_line
delivery_city
delivery_state
delivery_pincode
delivery_latitude
delivery_longitude

created_at
updated_at
```

### Relationships

```
User 1 ───────── N Order

Restaurant 1 ───────── N Order

Order 1 ───────── N OrderItem

Order 1 ───────── N Payment
```

### Order Status

```
CREATED
PAYMENT_PENDING
PAID
PAYMENT_FAILED
CONFIRMED
PREPARING
READY
OUT_FOR_DELIVERY
DELIVERED
CANCELLED
```

The delivery address stored in the order is a **snapshot of the customer's address at the time the order is placed**.

This prevents changes to the customer's saved address from modifying historical orders.

---

## 9. OrderItem

Stores food items purchased as part of an order.

### Fields

```
id
order_id
food_item_id
food_name
quantity
price
subtotal
created_at
```

### Relationships

```
Order 1 ───────── N OrderItem

FoodItem 1 ───────── N OrderItem
```

### Important

The `price` stored in `OrderItem` represents the price at the time the order was placed.

This ensures that historical orders remain accurate even if the restaurant changes the current food price.

Example:

```
Current Pizza Price = ₹250

Previous Order:

Pizza
Quantity = 2
Price at purchase = ₹200

Order Total = ₹400
```

If the restaurant later changes the pizza price to ₹250, the historical order should still show ₹200.

---

## 10. Payment

Stores payment attempts associated with an order.

### Fields

```
id
order_id
transaction_id
amount
payment_method
status
created_at
updated_at
```

### Relationship

```
Order 1 ───────── N Payment
```

An order can have multiple payment attempts.

Example:

```
Payment #1 → FAILED
Payment #2 → FAILED
Payment #3 → SUCCESS
```

### Payment Status

```
PENDING
SUCCESS
FAILED
REFUNDED
```

### Payment Methods

```
CARD
UPI
NET_BANKING
WALLET
CASH_ON_DELIVERY
```

---

## 11. Review

Stores customer reviews for restaurants.

### Fields

```
id
user_id
restaurant_id
order_id
rating
comment
created_at
updated_at
```

### Relationships

```
User 1 ───────── N Review

Restaurant 1 ───────── N Review

Order 1 ───────── N Review
```

### Rating

```
1 - Very Poor
2 - Poor
3 - Average
4 - Good
5 - Excellent
```

---

# 12. Relationships

## User → Address

One user can have multiple addresses.

```
User 1 ───────── N Address
```

---

## User → Restaurant

A restaurant owner can own multiple restaurants.

```
User 1 ───────── N Restaurant
```

The `Restaurant.owner_id` references `User.id`.

---

## User → Cart

A customer has one active cart.

```
User 1 ───────── 1 Active Cart
```

---

## Cart → CartItem

One cart can contain multiple cart items.

```
Cart 1 ───────── N CartItem
```

---

## FoodItem → CartItem

A food item can appear in multiple carts.

```
FoodItem 1 ───────── N CartItem
```

---

## Restaurant → Category

A restaurant can have multiple categories.

```
Restaurant 1 ───────── N Category
```

---

## Category → FoodItem

A category can contain multiple food items.

```
Category 1 ───────── N FoodItem
```

---

## Restaurant → FoodItem

A
