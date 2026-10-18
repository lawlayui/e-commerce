# e-commerce-
# E-Commerce DDD Architecture Design

**Status:** Design Phase (Pre-Implementation)  
**Last Updated:** 2026-10-04  
**Version:** 1.0

---

## Bounded Contexts Overview

| # | Context | Responsibility | Aggregate Root | Type |
|---|---------|---|---|---|
| 1 | ACCOUNT | Authentication, user profile | User | Core |
| 2 | CATALOG | Product discovery (read-only) | CatalogProduct | Core |
| 3 | SELLER_PRODUCT | Seller manage products | SellerProduct | Core |
| 4 | INVENTORY | Physical stock management | InventoryItem | Core |
| 5 | CART | Temporary product holding | Cart | Core |
| 6 | ORDER | Order management & pricing | Order | Core |
| 7 | PAYMENT | Payment gateway integration | PaymentTransaction | Core |
| 8 | RATING_REVIEW | Product rating & review | Review | Core |
| 9 | NOTIFICATION | Send notifications | Notification | Supporting |

---


## Context Detail & Entities

### 1. ACCOUNT Context

**Responsibility:**
- User registration & login
- User profile management
- Role assignment (CUSTOMER, SELLER)
- Note: Authorization at infrastructure layer, not domain

**Entities:**

#### User (Aggregate Root)
```
Attributes:
- userId: UUID (PK, identity)
- email: string (unique)
- hashedPassword: string (never stored plain)
- role: Enum [CUSTOMER, SELLER]
- isActive: boolean
- createdAt: timestamp
- updatedAt: timestamp

Value Objects:
- Email (email format validation)
- Password (hashing algorithm)
- Role (CUSTOMER | SELLER)
```

#### UserProfile (Entity)
```
Attributes:
- profileId: UUID (PK)
- userId: UUID (FK to User)
- fullName: string
- phoneNumber: string
- address: string
- updatedAt: timestamp

Value Objects:
- PhoneNumber (validation)
- Address (address formatting)
```

**Invariants:**
- Email must be unique
- Password must be hashed
- Role must be valid enum
- One user can have only one profile
- Address must be in the format (street, sub-district, district, city | regency, postal code).

**Events Published:**
- `UserRoleChanged` (userId, oldRole, newRole)

**Commands (Use Cases):**
- RegisterUser
- UpdateUserProfile
- ChangeUserRole

---

### 2. CATALOG Context 

**Responsibility:**
- Product discovery for customers
- Read-only product information
- Aggregate rating from RATING_REVIEW context
- Driven by SELLER_PRODUCT context (published products)

**Entities:**

#### CatalogProduct (Aggregate Root)
```
Attributes:
- catalogProductId: UUID (PK, identity)
- sku: string (unique, from SELLER_PRODUCT)
- sellerId: UUID (reference to seller)
- name: string
- description: string
- price: Money (amount + currency)
- categories: List<Category>
- imageUrl: string
- availability: Enum [AVAILABLE, OUT_OF_STOCK, DISCONTINUED]
- averageRating: decimal (0.0 - 5.0, from RATING_REVIEW)
- reviewCount: integer
- createdAt: timestamp
- updatedAt: timestamp

Value Objects:
- SKU (unique identifier, immutable)
- Money (amount, currency)
- Category (standardized categories)
- ProductStatus (AVAILABLE | OUT_OF_STOCK | DISCONTINUED)
- Rating (0.0 - 5.0)
```

**Invariants:**
- SKU must be unique
- Price must be >= 0
- Rating aggregate only from verified purchases (from RATING_REVIEW)
- Product is read-only (no direct update from CATALOG context)

**Events Published:**

**Events Listened:**
- `ProductPublished` (from SELLER_PRODUCT) → Create or update CatalogProduct
- `ProductUnpublished` (from SELLER_PRODUCT) → Mark as discontinued
- `ReviewPublished` (from RATING_REVIEW) → Update rating aggregate
- `ReviewDeleted` (from RATING_REVIEW) → Update rating aggregate
- `StockReserved` (from INVENTORY) → Update availability
- `StockRestored` (from INVENTORY) → Update availability

**Commands (Use Cases):**
- BrowseProducts
- SearchProducts (by name, category)
- FilterProducts (by price range, rating, etc)
- GetProductDetail

---


### 3. SELLER_PRODUCT Context

**Responsibility:**
- Seller manage their own products
- Control product information (name, price, description)
- Publish/unpublish products to CATALOG
- Cannot manage stock (managed by INVENTORY)

**Entities:**

#### SellerProduct (Aggregate Root)
```
Attributes:
- sellerProductId: UUID (PK, identity)
- sku: string (unique per seller + product)
- sellerId: UUID (FK, owner of product)
- name: string
- description: string
- price: Money (amount + currency)
- category: List<Category>
- imageUrl: string
- isPublished: boolean (is visible in CATALOG)
- status: Enum [ACTIVE, INACTIVE, DISCONTINUED]
- createdAt: timestamp
- updatedAt: timestamp

Value Objects:
- SKU (unique identifier)
- Money (amount, currency)
- Category
- ProductStatus (ACTIVE | INACTIVE | DISCONTINUED)
```

**Invariants:**
- Seller can only manage their own products (sellerId validation)
- Price must be >= 0
- Name and description cannot be empty
- SKU is immutable after creation
- Cannot modify stock (belongs to INVENTORY context)

**Events Published:**
- `ProductCreated` (sellerProductId, sku, sellerId, name, price)
- `ProductNameChanged` (sku, oldName, newName)
- `ProductPriceChanged` (sku, oldPrice, newPrice)
- `ProductDescriptionChanged` (sku, oldDescription, newDescription)
- `ProductPublished` (sku, sellerId) → triggers CATALOG to create/update
- `ProductUnpublished` (sku) → triggers CATALOG to discontinue
- `ProductStatusChanged` (sku, oldStatus, newStatus)
- `ProductDeleted` (sku)

**Commands (Use Cases):**
- CreateProduct (sellerId, name, description, price, category)
- UpdateProductName (sku, newName)
- UpdateProductPrice (sku, newPrice)
- UpdateProductDescription (sku, newDescription)
- PublishProduct (sku) → make visible in CATALOG
- UnpublishProduct (sku) → hide from CATALOG
- ChangeProductStatus
- DeleteProduct (soft delete)

---

### 4. INVENTORY Context

**Responsibility:**
- Source of truth for physical stock
- Track stock per SKU
- Handle stock reservation and deduction
- Manage stock batches (weight, size, expiredTime)
- Publish stock events for CATALOG and ORDER contexts

**Entities:**

#### InventoryItem (Aggregate Root)
```
Attributes:
- inventoryItemId: UUID (PK, identity)
- sku: string (unique, reference to product)
- quantity: integer (available quantity)
- quantityReserved: integer (soft-reserved for pending orders)
- quantityAvailable: integer (quantity - quantityReserved)
- weight: decimal (in kg)
- size: string (dimensions: e.g., "10x10x10 cm")
- expiredTime: timestamp (nullable, for perishable products)
- location: string (warehouse location)
- lastRestocked: timestamp
- createdAt: timestamp
- updatedAt: timestamp

Value Objects:
- SKU
- Quantity (non-negative integer)
- Weight (positive decimal)
- Size (string format)
- ExpirationDate (nullable timestamp)
- Location (warehouse section)
```

#### StockAdjustment (Entity, optional tracking)
```
Attributes:
- adjustmentId: UUID (PK)
- inventoryItemId: UUID (FK)
- adjustmentType: Enum [RESTOCK, DAMAGE, LOSS, ADJUSTMENT]
- quantity: integer (positive or negative)
- reason: string
- createdBy: UUID (admin/staff who made adjustment)
- createdAt: timestamp
```

**Invariants:**
- Quantity must never be negative (hard constraint)
- quantityReserved must be <= quantity
- quantityAvailable = quantity - quantityReserved
- Cannot deduct stock below 0
- Stock can only be deducted after payment confirmed

**Events Published:**
- `StockReserved` (sku, quantity, orderId) → soft-reserve for pending order
- `StockDeducted` (sku, quantity) → hard-deduct after payment confirmed
- `StockRestored` (sku, quantity, reason) → restore if payment failed or order cancelled
- `StockAdjusted` (sku, quantity, adjustmentType, reason)
- `StockLow` (sku, currentQuantity, threshold) → alert seller/admin
- `StockExpired` (sku, expiredTime) → alert before expiration

**Events Listened:**
- `OrderCreated` (from ORDER) → Reserve stock
- `PaymentSucceeded` (from PAYMENT) → Hard-deduct stock
- `PaymentFailed` (from PAYMENT) → Restore reserved stock
- `OrderCancelled` (from ORDER) → Restore reserved stock

**Commands (Use Cases):**
- AddStock (sku, quantity, weight, size, expiredTime)
- ReserveStock (sku, quantity, orderId)
- DeductStock (sku, quantity) [only after payment confirmed]
- RestoreStock (sku, quantity, reason)
- AdjustStock (sku, quantity, adjustmentType, reason)
- GetInventoryItem (sku)
- CheckAvailability (sku, quantity) → returns true/false

---

### 5. CART Context

**Responsibility:**
- Temporary holding of products before checkout
- No pricing calculation (belongs to ORDER context)
- Can be abandoned or expired
- Simple add/remove operations

**Entities:**

#### Cart (Aggregate Root)
```
Attributes:
- cartId: UUID (PK, identity)
- customerId: UUID (FK, owner of cart)
- items: List<CartItem>
- status: Enum [ACTIVE, ABANDONED, CONVERTED_TO_ORDER]
- expiresAt: timestamp (e.g., 30 days from last update)
- createdAt: timestamp
- updatedAt: timestamp

Value Objects:
- CartStatus (ACTIVE | ABANDONED | CONVERTED_TO_ORDER)
```

#### CartItem (Entity, part of Cart aggregate)
```
Attributes:
- cartItemId: UUID (PK)
- sku: string (reference to product, not full product data)
- quantity: integer
- addedAt: timestamp

Value Objects:
- SKU
- Quantity
```

**Invariants:**
- Cart belongs to one customer only
- CartItem quantity must be > 0
- Cannot have duplicate SKUs in same cart (merge if added again)
- Cart can be abandoned after expiration time

**Events Published:**
- `CartAbandoned` (cartId, reason)
- `CartConvertedToOrder` (cartId, orderId)

**Commands (Use Cases):**
- CreateCart (customerId)
- AddItemToCart (cartId, sku, quantity)
- RemoveItemFromCart (cartId, sku)
- UpdateItemQuantity (cartId, sku, newQuantity)
- ClearCart (cartId)
- GetCart (customerId)

---

### 6. ORDER Context

**Responsibility:**
- Manage order lifecycle (PENDING → PAID → SHIPPED → DELIVERED)
- Calculate order total (price, tax, shipping)
- Create order from cart
- Track order status
- Handle order cancellation

**Entities:**

#### Order (Aggregate Root)
```
Attributes:
- orderId: UUID (PK, identity)
- customerId: UUID (FK, customer)
- cartId: UUID (reference to cart)
- orderItems: List<OrderItem> (snapshot of products + prices at order time)
- orderStatus: Enum [PENDING, AWAITING_PAYMENT, PAID, SHIPPED, DELIVERED, CANCELLED]
- totalPrice: Money (subtotal + tax + shipping)
- subtotal: Money (sum of item prices)
- taxAmount: Money
- shippingCost: Money
- discountAmount: Money (if any)
- paymentMethodId: UUID (reference to chosen payment method)
- shippingAddress: Address
- createdAt: timestamp
- updatedAt: timestamp

Value Objects:
- OrderStatus (PENDING | AWAITING_PAYMENT | PAID | SHIPPED | DELIVERED | CANCELLED)
- Money (amount + currency)
- Address (shipping address details)
```

#### OrderItem (Entity, part of Order aggregate)
```
Attributes:
- orderItemId: UUID (PK)
- sku: string
- productName: string
- quantity: integer
- unitPrice: Money (price at order creation time - snapshot)
- totalPrice: Money (unitPrice * quantity)
- sellerProductId: UUID (reference)
- status: Enum [PENDING, RESERVED, PAID, SHIPPED, DELIVERED]
- createdAt: timestamp

Value Objects:
- SKU
- Money
- OrderItemStatus
```

**Invariants:**
- Order must have at least 1 item
- Quantity must be > 0
- Total price must be calculated correctly
- Order status transitions must be valid
- OrderItem prices are immutable (snapshot from order time)
- Cannot cancel paid order (only after delivered)

**Events Published:**
- `OrderCreated` (orderId, customerId, orderItems, totalPrice)
  - Triggers INVENTORY to reserve stock
- `OrderStatusChanged` (orderId, oldStatus, newStatus)
- `OrderAwaitingPayment` (orderId)
- `OrderPaid` (orderId)
- `OrderCancelled` (orderId, reason)
- `OrderShipped` (orderId)
- `OrderDelivered` (orderId)

**Events Listened:**
- `CartConvertedToOrder` (from CART) → Create new Order
- `StockReserved` (from INVENTORY) → Confirm order can proceed
- `PaymentSucceeded` (from PAYMENT) → Change order status to PAID
- `PaymentFailed` (from PAYMENT) → Change order status to AWAITING_PAYMENT (allow retry)

**Commands (Use Cases):**
- CreateOrderFromCart (cartId, customerId, shippingAddress)
- CalculateOrderTotal (orderId) [includes tax, shipping calculation]
- ChoosePaymentMethod (orderId, paymentMethodId)
- ProcessCheckout (orderId) → initiates PAYMENT
- CancelOrder (orderId, reason)
- UpdateOrderStatus (orderId, newStatus)
- GetOrder (orderId)

---

### 7. PAYMENT Context

**Responsibility:**
- Payment gateway integration (Stripe, Xendit, etc)
- Process payment transactions
- Handle payment callbacks (success/failure)
- Manage payment methods

**Entities:**

#### PaymentTransaction (Aggregate Root)
```
Attributes:
- paymentTransactionId: UUID (PK, identity)
- orderId: UUID (FK, associated order)
- customerId: UUID (FK)
- amount: Money
- paymentMethod: Enum [CREDIT_CARD, BANK_TRANSFER, E_WALLET, CASH]
- externalTransactionId: string (from payment gateway: Stripe ID, Xendit ID)
- status: Enum [PENDING, PROCESSING, SUCCEEDED, FAILED, REFUNDED]
- failureReason: string (nullable, if failed)
- attemptCount: integer
- lastAttemptAt: timestamp
- succeededAt: timestamp (nullable)
- expiresAt: timestamp (e.g., 15 minutes from creation)
- createdAt: timestamp
- updatedAt: timestamp

Value Objects:
- Money
- PaymentMethod (CREDIT_CARD | BANK_TRANSFER | E_WALLET | CASH)
- PaymentStatus (PENDING | PROCESSING | SUCCEEDED | FAILED | REFUNDED)
```

**Invariants:**
- Amount must be > 0
- Payment must expire after certain time (e.g., 15 minutes)
- Each payment attempt must be tracked
- Cannot refund more than original amount
- External transaction ID is immutable after success

**Events Published:**
- `PaymentInitiated` (paymentTransactionId, orderId, amount)
- `PaymentProcessing` (paymentTransactionId, externalTransactionId)
- `PaymentSucceeded` (paymentTransactionId, orderId, amount)
  - Triggers INVENTORY to hard-deduct stock
  - Triggers ORDER to change status to PAID
- `PaymentFailed` (paymentTransactionId, orderId, failureReason)
  - Triggers INVENTORY to restore reserved stock
  - Triggers ORDER to allow retry
- `PaymentExpired` (paymentTransactionId, orderId)
- `PaymentRefunded` (paymentTransactionId, refundAmount, reason)

**External API Calls:**
- POST to payment gateway (Stripe/Xendit) to initiate payment
- Webhook callback from payment gateway

**Commands (Use Cases):**
- InitiatePayment (orderId, amount, paymentMethod)
- ProcessPaymentCallback (externalTransactionId, status, response)
- RetryPayment (paymentTransactionId)
- RefundPayment (paymentTransactionId, reason)
- CheckPaymentStatus (paymentTransactionId)

---

### 8. RATING_REVIEW Context

**Responsibility:**
- Manage product reviews and ratings
- Aggregate ratings for products
- Ensure only verified buyers can review
- Publish review events for CATALOG to update rating

**Entities:**

#### Review (Aggregate Root)
```
Attributes:
- reviewId: UUID (PK, identity)
- customerId: UUID (FK, reviewer)
- sku: string (product being reviewed)
- rating: Decimal (1.0 - 5.0)
- title: string
- content: string
- isVerifiedPurchase: boolean (only if customer bought this product)
- helpfulCount: integer (upvotes)
- unhelpfulCount: integer (downvotes)
- status: Enum [PENDING, APPROVED, REJECTED, DELETED]
- createdAt: timestamp
- updatedAt: timestamp
- deletedAt: timestamp (nullable, soft delete)

Value Objects:
- Rating (1.0 - 5.0 decimal)
- ReviewStatus (PENDING | APPROVED | REJECTED | DELETED)
```

**Invariants:**
- Only verified buyers (who ordered and received product) can review
- Rating must be between 1.0 and 5.0
- Review content cannot be empty
- One customer can have only one review per product (or update existing)
- Review cannot be edited after N days (e.g., 30 days)

**Events Published:**
- `ReviewCreated` (reviewId, sku, customerId, rating)
  - Triggers CATALOG to recalculate average rating
- `ReviewUpdated` (reviewId, sku, newRating)
  - Triggers CATALOG to recalculate average rating
- `ReviewDeleted` (reviewId, sku)
  - Triggers CATALOG to recalculate average rating
- `ReviewApproved` (reviewId, sku)
- `ReviewRejected` (reviewId, sku)

**Events Listened:**
- `OrderDelivered` (from ORDER) → Allow customer to create review

**Commands (Use Cases):**
- CreateReview (customerId, sku, rating, title, content) [verify purchase first]
- UpdateReview (reviewId, rating, title, content) [within edit window]
- DeleteReview (reviewId, reason)
- ApproveReview (reviewId) [by admin]
- RejectReview (reviewId, reason) [by admin]
- GetProductReviews (sku) [paginated]
- GetAverageRating (sku) → returns decimal (0.0 - 5.0)

---

### 9. NOTIFICATION Context

**Responsibility:**
- Send notifications (email, SMS, push)
- Listen to events from other contexts
- Track notification status
- Retry mechanism for failed notifications

**Note:** This is a supporting context, not core domain logic.

**Entities:**

#### Notification (Aggregate Root)
```
Attributes:
- notificationId: UUID (PK, identity)
- recipientId: UUID (customer or seller)
- recipientEmail: string
- notificationType: Enum [ORDER_CONFIRMATION, PAYMENT_SUCCESS, PAYMENT_FAILED, STOCK_ALERT, REVIEW_CREATED, SHIPPING_UPDATE]
- subject: string
- body: string
- channel: Enum [EMAIL, SMS, PUSH]
- status: Enum [PENDING, SENT, FAILED, RETRIED]
- attemptCount: integer
- lastAttemptAt: timestamp
- sentAt: timestamp (nullable)
- failureReason: string (nullable)
- relatedOrderId: UUID (nullable)
- relatedSku: string (nullable)
- createdAt: timestamp

Value Objects:
- NotificationType
- Channel (EMAIL | SMS | PUSH)
- NotificationStatus (PENDING | SENT | FAILED | RETRIED)
```

**Invariants:**
- Recipient email/phone must be valid
- Subject and body cannot be empty
- Max retry attempts = 3

**Events Published:**
- `NotificationSent` (notificationId, recipientId, channel)
- `NotificationFailed` (notificationId, reason)
- `NotificationRetried` (notificationId)

**Events Listened (Subscribers):**
- `UserRegistered` (from ACCOUNT) → Send welcome email
- `OrderCreated` (from ORDER) → Send order confirmation email
- `PaymentSucceeded` (from PAYMENT) → Send payment success email
- `PaymentFailed` (from PAYMENT) → Send payment failed email
- `OrderShipped` (from ORDER) → Send shipping notification
- `OrderDelivered` (from ORDER) → Send delivery confirmation
- `ReviewCreated` (from RATING_REVIEW) → Notify seller
- `StockLow` (from INVENTORY) → Alert seller
- `ProductPublished` (from SELLER_PRODUCT) → Confirm to seller

**Commands (Use Cases):**
- SendNotification (recipientId, notificationType, templateData)
- RetryFailedNotification (notificationId)
- GetNotificationStatus (notificationId)
