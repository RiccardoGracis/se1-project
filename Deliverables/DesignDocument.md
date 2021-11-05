# Design Document 

Authors: Rocco Luca Iamello, Massimo Di Natale, Paolo Trungadi, Riccardo Gracis

Date: 06/06/2021

Version: 1.6

# Contents

- [High level design](#high-level-design)
- [Low level design](#low-level-design)
- [Verification traceability matrix](#verification-traceability-matrix)
- [Verification sequence diagrams](#verification-sequence-diagrams)

# Instructions

The design must satisfy the Official Requirements document, notably functional and non functional requirements

# High level design 

<discuss architectural styles used, if any>
<report package diagram>


Architectural styles used: MVC and 3 Tier. 
In our case, for simplicity we decided to use two packages. One for gui and another forModel and Logic.

```plantuml
package GUI
package ApplicationLogicAndModel
package Exceptions
GUI --> ApplicationLogicAndModel
Exceptions -->ApplicationLogicAndModel
```


For High level design we selected the Façade for ApplicationLogicAndModel and we suppose the GUI will be implemented with an Observer-Observable pattern + Abstract Factory.

# Low level design

<for each package, report class diagram>


#### Package ApplicationLogicAndModel

```plantuml
left to right direction
scale 0.65
note "For simplicity and cleaning of diagram, Constructors, Getters and setters are not defined" as N1

note top of Shop : We will store all the collections and attributes\n from Shop in a DB. 
note bottom of AccountBook : We will store all the collections\n from AccountBook in a DB. 
note bottom of BalanceOperation : Instances are persistent in the collection.
note top of ProductTypeImpl : Instances are persistent in the collection.
note right of User : Instances are persistent in the collection.
note top of Customer : Instances are persistent in the collection.
note right of LoyaltyCard : Instances are persistent in the collection.
note top of SaleTransactionImpl : Instances are persistent in the collection.
note top of ReturnTransaction : Instances are persistent in the collection.
note bottom of CreditCard : Instances are persistent in the collection.
note top of OrderImpl : Instances are persistent in the collection.
note top of Payment : Instance is persistent in SaleTransaction.
note left of Position : Instance is persistent in the collection.
note bottom of CartItem : Instance is persistent in SaleTransaction.
note top of EZShopInterface : Methods are the same of https://git-softeng.polito.it/se-2021/ezshop/-/blob/master/API/EZShopInterface.java not reported in order to reduce noise

interface EZShopInterface
interface Order
interface ProductType
interface User
interface Customer
interface TicketEntry
interface SaleTransaction


EZShopInterface <|- Shop : implements
Order <|- OrderImpl : implements
ProductType <|- ProductTypeImpl : implements
User <|- UserImpl : implements
Customer <|- CustomerImpl : implements
SaleTransaction <|- SaleTransactionImpl: implements
TicketEntry <|- CartItem: implements

class Shop{
    -a: AccountBook
    -users : HashMap<Integer,User>
    -{static}loggeduser : UserImpl
    -inventory : HashMap<String,ProductType>
    -products : HashMap<String,Product>
    -customers : HashMap<Integer,Customer>
    -loyaltyCards : HashMap<String,LoyaltyCard>
    -saletransactions : HashMap<Integer,SaleTransaction> 
    -returntransactions : HashMap<Integer,ReturnTransaction>
	-creditcards: HashMap <String, CreditCard>
    -orders: HashMap <Integer,Order>
    -fileCC: String
    -db: JDBC
    -positions: ArrayList<String>


    +void reset()
	+Integer createUser(String username, String password, String role)
    +boolean deleteUser(Integer id)
    +List<User> getAllUsers()
    +User getUser(Integer id)
    +boolean updateUserRights(Integer id, String role)
    +User login(String username, String password)
    +boolean logout()
    +Integer createProductType(String description, String productCode, double pricePerUnit, String note)
    +boolean updateProduct(Integer id, String newDescription, String newCode, double newPrice, String newNote)
    +boolean deleteProductType(Integer id)
    +List<ProductType> getAllProductTypes()
    +ProductType getProductTypeByBarCode(String barCode)
    +List<ProductType> getProductTypesByDescription(String description)
    +boolean updateQuantity(Integer productId, int toBeAdded)
    +boolean updatePosition(Integer productId, String newPos)
    +Integer issueReorder(String productCode, int quantity, double pricePerUnit)
    +Integer payOrderFor(String productCode, int quantity, double pricePerUnit)
    +boolean payOrder(Integer orderId)
    +boolean recordOrderArrival(Integer orderId)
    +boolean recordOrderArrivalRFID(Integer orderId, String RFIDfrom)
    +List<Order> getAllOrders()
    +Integer defineCustomer(String customerName)
    +boolean modifyCustomer(Integer id, String newCustomerName, String newCustomerCard)
    +boolean deleteCustomer(Integer id)
    +Customer getCustomer(Integer id)
    +List<Customer> getAllCustomers()
    +String createCard()
    +boolean attachCardToCustomer(String customerCard, Integer customerId)
    +boolean modifyPointsOnCard(String customerCard, int pointsToBeAdded)
    +Integer startSaleTransaction()
    +boolean addProductToSale(Integer transactionId, String productCode, int amount)
    +boolean addProductToSaleRFID(Integer transactionId, String RFID)
    +boolean deleteProductFromSale(Integer transactionId, String productCode, int amount)
    +boolean deleteProductFromSaleRFID(Integer transactionId, String RFID)
    +boolean applyDiscountRateToProduct(Integer transactionId, String productCode, double discountRate)
    +boolean applyDiscountRateToSale(Integer transactionId, double discountRate)
    +int computePointsForSale(Integer transactionId)
    +boolean endSaleTransaction(Integer transactionId)
    +boolean deleteSaleTransaction(Integer transactionId)
    +SaleTransaction getSaleTransaction(Integer transactionId)
    +Integer startReturnTransaction(Integer transactionId)
    +boolean returnProduct(Integer returnId, String productCode, int amount)
    +boolean returnProductRFID(Integer returnId, String RFID)
    +boolean endReturnTransaction(Integer returnId, boolean commit)
    +boolean deleteReturnTransaction(Integer returnId)
    +double receiveCashPayment(Integer transactionID, double cash)
    +boolean receiveCreditCardPayment(Integer transactionID, String creditCard)
    +double returnCashPayment(Integer returnId)
    +double returnCreditCardPayment(Integer returnId, String creditCard)
    +boolean recordBalanceUpdate(double toBeAdded)
    +List<BalanceOperation> getCreditsAndDebits(LocalDate from, LocalDate to)
    +double computeBalance()


}

class JDBC{
    -{static} jdbcUrl:String
    -connection: Connection
    +Connection openConnection()
    +boolean closeConnection()
}

class AccountBook {
    -transactions: TreeMap<LocalDate,ArrayList<BalanceOperation>>
    -balance : double
    ~boolean recordBalanceUpdate(double toBeAdded)
    ~List<BalanceOperation> getCreditsAndDebits(LocalDate from, LocalDate to)
    ~double computeBalance()
}

class BalanceOperation {
 -balanceID: Integer
 -description : String
 -amount : double
 -date : LocalDate
}

class Credit 
class Debit

class OrderImpl{
    -{static}idCount : Integer
    - id: Integer
    -supplier : string
    -pricePerUnit : double
    -quantity : int
    -status : string
    -productCode : string
    -b : Debit
}

class UserImpl{
    -{static}id : Integer
    -username : String
    -password : String
    -role : String
    ~boolean updateRight(String role)
    ~boolean isAdministrator()
    ~boolean isCashier()
    ~boolean isShopManager()
}

class ProductTypeImpl{
    -position : Position<Optional>
    -{static}idCount : Integer
    -id: Integer
    -barCode : String
    -description : String
    -sellPrice : double
    -quantity : int
    -discountRate : double
    -notes : String
    ~{static}boolean validateDescr (String description)
    ~{static}boolean validateProdCode (String gtin)
    ~Double getPPUWithDiscount()
}

class Product{
    -RFID: String
    -barCode : String
    ~{static}boolean validateRFID (String RFID)
}

class Position {
    -aisleID : int
    -rackID : string
    -levelID : int
    ~{static}boolean validatePosition (String position)
}

class SaleTransactionImpl {
    -discountrate : double
    -cart : HashMap<String,CartItem>
    -status : String
    -p: Optional<Payment>
    ~void applyProdDiscount(Double discountRate, String productCode)
    ~void computeTransactionTotal()
    ~Boolean isCardPayment()
    ~CreditCardPayment getPaymentCreditCard()
    ~CashPayment getPaymentCash()

}

class ReturnTransaction {
  -returnedCart : HashMap<String,CartItem>
  -status : String
  -p: Optional<Payment>
  -saleTrans: SaleTransaction
  ~void computeReturnValue()
  ~Boolean isCardPayment()
  ~CreditCardPayment getPaymentCreditCard()
  ~CashPayment getPaymentCash()
}

class CartItem {
    -quantity : Integer
    -product : ProductType
    -productDiscount: double
    -RFIDs : ArrayList<String>
    ~addQuantity(Integer quantity)
}

class CustomerImpl {
    -{static}id : Integer
    -name : String
    -surname : String
    -card: <LoyaltyCard>
    ~void modifyCustomer(String newCustomerName, String newCustomerCard)
    ~void attachCardToCustomer(LoyaltyCard customerCard)
    ~void detachCardFromCustomer()

}
class LoyaltyCard {
    -{static}ID : String
    -points : int
    -assigned : boolean
    ~int modifyPointsOnCard(int pointsToBeAdded)
    ~void detach()

}



class Payment {
	-amount: double
}

class CashPayment {
	-change: double
}

class CreditCardPayment {
	-c: CreditCard
}

class CreditCard {
	-number: String
	-valid: boolean
	~boolean validate(String number)
}
CashPayment --|> Payment
CreditCardPayment --|> Payment

CreditCard <-- CreditCardPayment
Credit --|> BalanceOperation
Debit --|> BalanceOperation
OrderImpl --> Debit
ReturnTransaction --|> Debit
SaleTransactionImpl --|> Credit

ProductTypeImpl -> "0..1" Position
ProductTypeImpl <- Product

Shop --> "*" CustomerImpl
Shop ---> "*" LoyaltyCard
Shop --> "*" ProductTypeImpl
Shop -> "*" UserImpl
Shop -> "*" CreditCard
Shop -> "*" OrderImpl
Shop -> AccountBook 
Shop --> "*" SaleTransactionImpl
Shop -> JDBC


AccountBook -> "*" BalanceOperation

ReturnTransaction  --> "*" CartItem
SaleTransactionImpl --> "*" CartItem
CartItem --> ProductTypeImpl

SaleTransactionImpl -- "*" ReturnTransaction
SaleTransactionImpl --> "0..1" Payment
ReturnTransaction --> "0..1" Payment

CustomerImpl --> "0..1"LoyaltyCard

```


# Verification traceability matrix

\<for each functional requirement from the requirement document, list which classes concur to implement it>

|| Shop | AccountBook | BalanceOperation | Credit | Debit | OrderImpl | User | ProductTypeImpl | Position | SaleTransaction | ReturnTransaction | CartItem | Customer | LoyaltyCard | Payment | CashPayment | CreditCardPayment | CreditCard |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| Manage users and rights (users are Administrator, ShopManager, Cashier) | X |  |  |  |  |  | X |  |  |  |  |  |  |  |  |  |  |  |
| Define a new user, or modify an existing user | X |  |  |  |  |  | X |  |  |  |  |  |  |  |  |  |  |  |
| Delete a user | X |  |  |  |  |  | X |  |  |  |  |  |  |  |  |  |  |  |
| List all users  | X |  |  |  |  |  | X |  |  |  |  |  |  |  |  |  |  |  |
| Search a user | X |  |  |  |  |  | X |  |  |  |  |  |  |  |  |  |  |  |
| Manage rights. Authorize access to functions to specific actors according to access rights | X |  |  |  |  |  | X |  |  |  |  |  |  |  |  |  |  |  |
| Manage product catalog  | X |  |  |  |  |  |  | X |  |  |  |  |  |  |  |  |  |  |
| Define a new product type, or modify an existing product type | X |  |  |  |  |  |  | X |  |  |  |  |  |  |  |  |  |  |
| Delete a product type | X |  |  |  |  |  |  | X |  |  |  |  |  |  |  |  |  |  |
| List all products types | X |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |
| Search a product type (by bar code, by description) | X |  |  |  |  |  |  | X |  |  |  |  |  |  |  |  |  |  |
| Manage inventory | X |  |  |  |  |  |  | X |  |  |  |  |  |  |  |  |  |  |
| Modify quantity available for a product type | X |  |  |  |  |  |  | X |  |  |  |  |  |  |  |  |  |  |
| Modify position for a product type | X |  |  |  |  |  |  | X | X |  |  |  |  |  |  |  |  |  |
| Issue a reorder warning for a product type | X |  |  |  |  | X |  | X |  |  |  |  |  |  |  |  |  |  |
| Send and pay an order for a product type | X |  |  |  | X | X |  | X |  |  |  |  |  |  |  |  |  |  |
| Pay an issued reorder warning | X |  |  |  | X | X |  |  |  |  |  |  |  |  |  |  |  |  |
| Record order arrival | X |  |  |  |  | X |  |  |  |  |  |  |  |  |  |  |  |  |
| List all orders (issued, payed, completed) | X |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |
| Manage customers and cards | X |  |  |  |  |  |  |  |  |  |  |  | X | X |  |  |  |  |
| Define or modify a customer | X |  |  |  |  |  |  |  |  |  |  |  | X |  |  |  |  |  |
| Delete a customer | X |  |  |  |  |  |  |  |  |  |  |  | X |  |  |  |  |  |
| Search a customer | X |  |  |  |  |  |  |  |  |  |  |  | X |  |  |  |  |  |
| List  all customers | X |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |
| Create a card | X |  |  |  |  |  |  |  |  |  |  |  |  | X |  |  |  |  |
| Attach card to a customer | X |  |  |  |  |  |  |  |  |  |  |  | X | X |  |  |  |  |
| Modify points on a card | X |  |  |  |  |  |  |  |  |  |  |  |  | X |  |  |  |  |
| Manage a sale transaction | X |  |  |  |  |  |  |  |  | X |  |  |  |  |  |  |  |  |
| Start a sale | X |  |  |  |  |  |  |  |  | X |  |  |  |  |  |  |  |  |
| Add  a product to a sale | X |  |  |  |  |  |  |X  |  | X |  | X |  |  |  |  |  |  |
| Delete a product from a sale | X |  |  |  |  |  |  | X |  | X |  | X |  |  |  |  |  |  |
| Apply discount rate to a sale | X |  |  |  |  |  |  |  |  | X |  |  |  |  |  |  |  |  |
| Apply discount rate to a product type | X |  |  |  |  |  |  | X |  | X |  | X |  |  |  |  |  |  |
| Compute points for a sale | X |  |  |  |  |  |  |  |  | X |  | X |  | X |  |  |  |  |
| Read bar code on product | X |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |
| Print sale ticket | X |  |  |  |  |  |  |  |  | X |  |  |  |  |  |  |  |  |
| Get sale ticket from ticket number | X |  |  |  |  |  |  |  |  | X |  |  |  |  |  |  |  |  |
| Close  a sale transaction | X |  |  |  |  |  |  |  |  | X |  |  |  |  |  |  |  |  |
| Rollback or commit a closed sale transaction | X | X | X |  |  |  |  |  |  | X |  |  |  |  |  |  |  |  |
| Start  a return transaction | X |  |  |  |  |  |  |  |  |  | X |  |  |  |  |  |  |  |
| Return a product listed in a sale ticket | X |  |  |  |  |  |  |  |  | X | X | X |  |  |  |  |  |  |
| Close  a return transaction | X |  |  |  |  |  |  |  |  |  | X |  |  |  |  |  |  |  |
| Rollback or commit a closed return transaction | X | X | X |  |  |  |  |  |  |  | X |  |  |  |  |  |  |  |
| Manage payment | X |  |  | X | X |  |  |  |  |  |  |  |  |  | X | X | X | X |
| Receive payment cash | X |  |  | X |  |  |  |  |  |  |  |  |  |  |  | X |  |  |
| Receive payment credit card  | X |  |  | X |  |  |  |  |  |  |  |  |  |  |  |  | X | X |
| Return payment cash | X |  |  |  | X |  |  |  |  |  |  |  |  |  |  | X |  |  |
| Return payment credit card  | X |  |  |  | X |  |  |  |  |  |  |  |  |  |  |  | X | X |
| Accounting | X | X | X | X | X |  |  |  |  |  |  |  |  |  |  |  |  |  |
| Record debit | X | X | X |  | X |  |  |  |  |  |  |  |  |  |  |  |  |  |
| Record credit | X | X | X | X |  |  |  |  |  |  |  |  |  |  |  |  |  |  |
| Show credits and debits over a period of time | X | X | X | X |  |  |  |  |  |  |  |  |  |  |  |  |  |  |
| Compute balance | X | X | X | X | X |  |  |  |  |  |  |  |  |  |  |  |  |  |

###### _Note:_ 
In the following sequence diagram were used some primitive functions in order to highlight and to provide basic features among objects and data structures. 
###### 
They are:
- _store()_: user will access in write-mode the system
- _get()_: user will access in read-mode the system

###### 
The last two methods implement some interface functions related to the data structures involved in the class diagram.
Then, are provided also setters, getters and costructors related to each class.


###### Scenario 1-1: Create product type X
```plantuml
actor User
participant Shop
participant ProductTypeImpl
note right of User
    //createProductType()// could throw
    //InvalidProductDescriptionException// //InvalidProductCodeException// 
    //InvalidPricePerUnitException// and //UnauthorizedException//
end note
User -> Shop : 1. createProductType(description, productCode, pricePerUnit,note,locations)
Shop -> ProductTypeImpl: 2. prod = new ProductTypeImpl(description, productCode, pricePerUnit, note, locations)
ProductTypeImpl -> Shop: 3. prod
Shop -> User: //Product added successfully//
```

###### Scenario 1.2: Modify product type location
```plantuml
actor User
participant Shop
participant ProductTypeImpl
participant Position
note right of User
    //getProductTypeByBarcode()// could throw 
    //InvalidProductCodeException// //UnauthorizedException//
end note
User -> Shop: 1. updatePosition(prodId, newPosition)
Shop -> ProductTypeImpl: 3. getProduct(prodId)
Shop -> Position: 4. prodPosition.setAisleID(newPosition) 
Shop -> User: 5. //Position updated successfully//
```

###### Scenario 1-3:Modify product type price per unit
```plantuml
actor User
participant Shop
participant ProductTypeImpl
note right of User
    //updateProduct()// could throw 
    //InvalidProductIdException//, //InvalidProductDescriptionException//, 
    //InvalidProductCodeException//, //InvalidPricePerUnitException// and
    //UnauthorizedException//
end note
User -> Shop: 1. updateProduct(prodID, descr, code, price, note)
Shop -> ProductTypeImpl: 2. prodID.setSellPrice(price) 
Shop -> User: 3. //Product updated successfully//
```

###### Scenario 2-1: Create user and defines rights
```plantuml
actor Administrator
participant Shop
participant User
note right of Administrator
    //createUser()// could throw 
    //InvalidUsernameException//, 
    //InvalidPasswordException// 
    and //InvalidRoleException//.
end note
Administrator -> Shop: 1. createUser(username, pwd, role)
Shop -> User: 2. usr = new User(ID, username, pwd, role)
User -> Shop: usr
Shop -> Shop: 3. store(usr.getId(), usr)
Shop -> Administrator: 4. //User added successfully//
```
 
###### Scenario 2-2: Delete user
```plantuml
actor Administrator
participant Shop
Administrator -> Shop: 1. deleteUser(userID)
note right of Administrator
    //deleteUser()// could throw
    //invalidUserIdException//
    //UnauthorizedException//.
end note
Shop -> Shop: 2. remove(userID)
Shop -> Administrator: 3. //User removed successfully//
```

###### Scenario 2-3: Modify user rights
```plantuml
actor Administrator
participant Shop
Administrator -> Shop: 1. updateUserRights(userID,role)
note right of Administrator
    //deleteUser()// could throw
    //invalidUserIdException//,
    //UnauthorizedException//
    and //InvalidRoleException//.
end note
Shop -> Shop: 2. usr = get(userID)
Shop -> User: 3. usr.setRole(role)
Shop -> Administrator: 3. //User's rights modified successfully//
```

###### Scenario 3-1: Order of product type X issued
```plantuml
actor ShopManager
participant Shop 
participant OrderImpl
ShopManager -> Shop: 1. issueOrder(productID, quantity, pricePerUnit)
note right of ShopManager
    //issueOrder()// could throw
    //UnauthorizedException//,
    //invalidQuantityException//,
    //invalidPricePerUnitexception//,
    and //invalidProductCodeException//.
end note
Shop -> OrderImpl: 2. order = new OrderImpl(orderID, supplier, pricePerUnits, quantity, status, productTypeID)
OrderImpl  -> Shop: 3. order
Shop -> Shop: 4. store(order.getId(), order)
Shop -> ShopManager: 5 //Order issued successfully//
```

###### Scenario 3-2: Order of product type X payed
```plantuml
actor ShopManager
participant Shop 
participant OrderImpl
participant Debit
participant AccountBook
note right of ShopManager
    //payOrder()// could throw //UnauthorizedException//,
    //InvalidQuantityException//,
   //InvalidProductCodeException// and
   //InvalidpricePerUnitsException//.
end note
ShopManager -> Shop: 1. payOrder(orderID)
Shop -> OrderImpl: 2. order = get(orderID)
Shop -> OrderImpl: 3. order.setStatus(payedStatus)
Shop -> Debit:6. deb = new Debit(descriptionOrder, tot, LocalDate.now())
Shop -> AccountBook:7. store(deb)
Shop -> ShopManager:8. //Order successfully set as payed//
```

###### Scenario 3-3: Record order of product type X arrival
```plantuml
actor ShopManager
participant Shop
participant ProductTypeImpl
note right of ShopManager
    //recordOrderArrival()// could throw 
    //UnauthorizedException//, 
    //InvalidOrderIdException//
    and //InvalidLocationException//.
end note
ShopManager -> Shop: 1. recordOrderArrival(orderID)
Shop -> Shop: 2. order = get(orderID)
Shop -> Shop: 2. prod = get(order.getProdId()) 
Shop -> ProductTypeImpl: 3. prod.updateQuantity(order.getQuantity())
Shop -> ShopManager: 4. //Order arrival marked successfully//
```

###### Scenario 4-1: Create customer record
```plantuml
actor User
participant Shop
participant Customer
note right of User
    //defineCustomer()// could throw 
    //InvalidCostumerNameException//
    //UnauthorizedException//
end note
User -> Shop: 1. defineCustomer(nameAndSurname) 
Shop -> Customer: 2. customer = new Customer(id, name, surname)
Shop -> Shop: 3. store(customer.getId(),customer)
Shop -> User: 4 //User added successfully//
```

###### Scenario 4-2: Attach Loyalty card to customer record
```plantuml
actor User
participant Shop
note right of User
    //attachCardToCustomer()// could throw 
    //InvalidCostumerIdException//,
    //InvalidCustoerCardIdException//
    and //UnauthorizedException//.
end note
User -> Shop:
User -> Shop: 1. attachCardToCustomer(cardId, customerId)
Shop -> Shop: 2. cardID = createCardID() 
Shop -> Loyaltycard: 3. card = new LoyaltyCard(cardID, initPoints, assigned)
Shop -> Shop: 4. store(card.getId(), card)
Shop -> Shop: 5. customer.setCard(card)
Shop -> User: 6. //Card attached successfully//
```

###### Scenario 4-3: Detach Loyalty card to customer record
```plantuml
actor User
participant Shop
participant LoyaltyCard
participant Customer
note right of User 
    //modifyCustomer()// could throw
    //InvalidCustomerNameException//, //InvalidCustomerCardException//, 
    //InvalidCustomerIdException// and //UnauthorizedException//.
end note
User -> Shop: 1. modifyCustomer(custId, escapeNameValue, escapeCardDisableValue)
Shop -> Shop: 2. cust = get(custId)
Shop -> LoyaltyCard: 3. cardID = cust.getCard().getCardId()
Shop -> Shop: 4. get(cardID).setAssigned(false)
Shop -> Customer: 5. cust.setCard(none)
Shop -> User: 6. //Card detached  successfully//
```

###### Scenario 4-4: Update customer record
```plantuml
actor User
participant Shop
participant Customer
note right of User 
    //modifyCustomer()// could throw
    //InvalidCustomerNameException//, //InvalidCustomerCardException//, 
    //InvalidCustomerIdException// and //UnauthorizedException//.
end note
User -> Shop: 1. modifyCustomer(custID, newNameAndSurname, escapeCardValue)
Shop -> Shop: 2. cust = get(custId)
Shop -> Customer: 3-1. cust.setName(newName)
Shop -> Customer: 3-2. cust.setSurname(newSurname)
Shop -> User: 4. //Customer record edited successfully//
```

###### Scenario 5-1: Login
```plantuml
actor unloggedUser
participant Shop
note left of Shop
    //logIn()// could throw 
    //InvalidPasswordException// and
    //InvalidUsernameIdException//.
    
end note
unloggedUser -> Shop: 1.        logIn(username, password) 
Shop -> Shop: 2. loggedUser = get(username)
Shop -> unloggedUser: 3. //Logged in successfully//
```
###### Scenario 5-2: Logout
```plantuml
actor loggedUser
participant Shop
loggedUser -> Shop: 1. logOut()
Shop -> Shop: 2. loggedUser=none
Shop -> loggedUser: 2. //Logged out succesfully//
```
###### All Scenarios from UC6 and UC7: Manage Sale Transaction and Payment method
```plantuml
actor User 
participant Shop 
participant SaleTransaction
participant Credit
participant CartItem
note right of User 
    //sartSaleTransaction()// could throw //UnauthorizedException//
end note
User -> Shop: 1. startSaleTransaction()
Shop -> SaleTransaction: 2-1. saleTr = new SaleTransaction(id, date, time, status)
SaleTransaction -> Shop: 2-1. saleTr
Shop -> User: 2-2. //sale transaction is opened//
note right of User
    UC 6 with all the possible scenarios.
    Pay Attetion: Are used two types 
    of sequence block:
    - optional: customerCard/productDiscount/saleDiscount
    - mutual exclusive: cash Payment or card Payment
end note
loop for each product -> Until user will not close the transaction
    note right of User
        //addProductToSale()// could throw 
        //InvalidTransactionIdException//, //InvalidProductCodeException//, 
        //InvalidQuantityException// and //UnauthorizedException//.
    end note
    User -> Shop: 3. addProductToSale(prodType, quantity)
    Shop -> CartItem: 3-1. new CartItem(prodType, quantity)
    Shop -> SaleTransaction: 4. transaction.setCost(+=prod.getSellPrice())
    group optional (scenario 6_2) (productDiscount) 
         note right of User
            //applyDiscountRateToProduct()// could throw 
            //InvalidTransactionIdException//, //InvalidProductCodeException//, 
            //InvalidDiscountRateException// and //UnauthorizedException//.
        end note
        User -> Shop: 4-1. applyDiscountRateToProduct(transactionId, prodId, discount)
        Shop -> SaleTransaction: 4-2. transaction.setCost(evaluatedCost)
    end

    group optional (scenario 6_3) (saleDiscount) -> Inserted by User)
     note right of User
            //applyDiscountToSale()// could throw 
            //InvalidTransactionIdException//, //InvalidDiscountRateException// 
            and //UnauthorizedException//.
        end note
        User -> Shop: 4-3. applyDiscoutRateToSale(saleTr.getId(), discountRate)
        Shop -> SaleTransaction: 4-4. saleTr.setDiscountrate(discountRate)
    end

    group optional (scenario 6_4) (deleteProductInCart based on product info) -> Could happen several times
    note right of User
            //deleteProductFromSale()// could throw 
            //InvalidTransactionIdException//, //InvalidProductCodeException//, 
            //InvalidQuantityException// and //UnauthorizedException//.
        end note
        User -> Shop: 5. deleteProductFromSale(transactionID, product.getID(), product.getPrice)
        Shop -> SaleTransaction: 5-1. remove(productID)
        Shop -> SaleTransaction: 5-2. saleTr.setCost(evaluatedCost)
    end
    SaleTransaction -> User: //Current transaction cost//
end

group optional (Scenario 6_5) (Bind CustomerCard and Transaction then update points)
note right of User 
    //modifypointsOnCard()// could throw  //InvalidCustomerNameException//, 
    //InvalidCustomerCardException//, //InvalidCustomerIdException//
    and //UnauthorizedException//.
end note
User -> Shop: 6-1. modifyPointsOnCard(cardId, point)
Shop -> Shop: 6-2. card.setPoint(newPointAmount)
end
group CloseCart (User Get the final transaction amount)
Shop -> SaleTransaction: 7-2.saleTr.getCost()
SaleTransaction -> Shop: totalPrice
Shop -> User: 7-3. //Total transaction amount is <totalAmount>//
end
group CASH PAYMENT mutualExclusive with CARD PAYMENT
User -> Shop: 8-1. receiveCashPayment(totalAmount)
note left of Shop
     //receiveCashdPayment()// could throw 
     //InvalidPaymentException// and //InvalidTransactionIdException//
end note
end
group CARD PAYMENT mutualExclusive with CASH PAYMENT
User -> Shop: 8-2. receiveCreditCardPayment(totalAmount,creditCardID)
note left of Shop
     //receiveCreditCardPayment()// could throw 
     //InvalidTransactionIdException// 
     and //UnauthorizedException//
end note
end
note right of User
     //receiveCreditCardPayment()// could throw 
     //InvalidPaymentException//, //InvalidTransactionIdException// 
     and //InvalidCreditCardException//
end note
User -> Shop: 9-1. endSaleTransaction(transactionID)
Shop -> Shop: 9-2: store(saleTr.getId(), saleTr)
Shop -> Credit: 9-3. new Sale(deriptionSaleTr, saleTr.getCost(), localdate)
Credit -> Shop: 9-4. sale
Shop -> Shop: 9-5. store(sale.getLocaldate(), sale)
Shop -> User: //Transaction and payment registered successfully//
```

###### Scenario 9-1: List credits and debits
```plantuml
actor ShopManager
participant Shop
ShopManager -> Shop: 1. getCreditsAndDebits(from, to)
Shop -> Shop:2. getFinantialtransaction(from, to)
Shop -> ShopManager: 3. //formatted results//
note right of Shop
    //getCreditAndDebits()// could throw
    //UnauthorizedException//.
end note
```

###### all scenarios from UC8-UC10: manage return and payment method
```plantuml
actor User
participant Shop
participant ProductTypeImpl
participant ReturnTransaction
participant Debit

User -> Shop: 1. startReturnTransaction(transactionID)
note right of User
     //startReturnTransaction()// could throw
     //InvalidTransactionIdException()// 
     and //UnauthorizedException//
end note
Shop -> Shop: 2. saleTr = get(transactionID)
Shop -> ReturnTransaction: 3. ret = new ReturnTransaction(descriptionReturn, amount=0, localDate)
loop for each returned product
note right of User
     //returProduct()// could throw
     //InvalidTransactionIdException()//, 
     //InvalidProductCodeException//, //InvalidQuantityException//
     and //UnauthorizedException//
end note
User -> Shop: 4. returnProduct(returnID, productID, amountTot)
Shop -> ProductTypeImpl: 5. get(productID).updateQuantity(1)
Shop -> Shop: 6. ret.setAmunt(newValue)
Shop -> User: //Current return amount//
end

group CASH PAYMENT mutualExclusive with CARD PAYMENT
User -> Shop: 7-1. returnCashPayment(totalAmount)
note right of User
     //receiveCashPayment(totalAmount)// could throw 
     //InvalidPaymentException//  and //UnauthorizedException//
end note
Shop -> User: //Operation successfully completed//
end
group CARD PAYMENT mutualExclusive with CASH PAYMENT
User -> Shop: 7-2. returnCreditCardPayment(totalAmount,creditCardID)
note right of User
     //receiveCreditCardPayment(totalAmount)// could throw 
     //InvalidPaymentException//, 
     //InvalidTransactionIdException//,
    //InvalidCreditCardException// and //UnauthorizedException//
end note
Shop -> User: //Operation successfully completed//
end

User -> Shop: 8. endReturnTransaction(returnId, true)
Shop -> Shop: 9. store(ret.getId(), ret)
Shop -> Credit: 10. debit = new Debit(ret.getAmount(),getCurrentDate())
Shop -> Shop: 11 store(debit)
Shop -> User: //Return operation successfully completed//
```
