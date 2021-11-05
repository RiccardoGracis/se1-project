# Integration and API Test Documentation

Authors: Rocco Luca Iamello, Massimo Di Natale, Paolo Trungadi, Riccardo Gracis

Date: 26/05/2021

Version: 1.10

# Contents

- [Dependency graph](#dependency graph)

- [Integration approach](#integration)

- [Tests](#tests)

- [Scenarios](#scenarios)

- [Coverage of scenarios and FR](#scenario-coverage)
- [Coverage of non-functional requirements](#nfr-coverage)



# Dependency graph 

     <report the here the dependency graph of the classes in EzShop, using plantuml>
```plantuml
'top to bottom direction
left to right direction
scale 1

note "Only class names are reported, for class attributes and methods, refer to Low Level Design (where EZShop class is called only Shop)" as N1

interface EZShopInterface
interface Order
interface ProductType
interface User
interface Customer
interface TicketEntry
interface SaleTransaction


EZShopInterface <|- EZShop : implements
Order <|-u- OrderImpl : implements
ProductType <|-u- ProductTypeImpl : implements
User <|-u- UserImpl : implements
Customer <|-u- CustomerImpl : implements
SaleTransaction <|-u- SaleTransactionImpl: implements
TicketEntry <|-u- CartItem: implements

class EZShop{
}
class JDBC{
}
class AccountBook {
}
class BalanceOperation {
}
class Credit 
class Debit
class OrderImpl{
}
class UserImpl{   
}
class ProductTypeImpl{  
}
class Position {   
}
class SaleTransactionImpl {
}
class ReturnTransaction { 
}
class CartItem {   
}
class CustomerImpl {
}
class LoyaltyCard {
}
class Payment {	
}
class CashPayment {
}
class CreditCardPayment {	
}
class CreditCard {	
}
CashPayment -|> Payment
CreditCardPayment -|> Payment
CreditCard <- CreditCardPayment
Credit -|> BalanceOperation
Debit -|> BalanceOperation
OrderImpl -> Debit
ReturnTransaction -|> Debit
SaleTransactionImpl -|> Credit
ProductTypeImpl -> Position
EZShop --> CustomerImpl
EZShop --> LoyaltyCard
EZShop --> ProductTypeImpl
EZShop --> UserImpl
EZShop --> CreditCard
EZShop --> OrderImpl
EZShop --> AccountBook 
EZShop --> SaleTransactionImpl
EZShop -> JDBC
AccountBook -> BalanceOperation
ReturnTransaction  -> CartItem
SaleTransactionImpl -> CartItem
CartItem -> ProductTypeImpl
SaleTransactionImpl <- ReturnTransaction
SaleTransactionImpl -> ReturnTransaction
SaleTransactionImpl ->  Payment
ReturnTransaction ->  Payment
CustomerImpl -> LoyaltyCard

```

# Integration approach

    <Write here the integration sequence you adopted, in general terms (top down, bottom up, mixed) and as sequence
    (ex: step1: class A, step 2: class A+B, step 3: class A+B+C, etc)> 
    <Some steps may  correspond to unit testing (ex step1 in ex above), presented in other document UnitTestReport.md>
    <One step will  correspond to API testing>

Since we have coded all the classes before starting testing we will use in the majority of cases a bottom up approach. By unit testing we have tested independent classes and now we integrate the upper classes without writing any stub.

#  Tests

   <define below a table for each integration step. For each integration step report the group of classes under test, and the names of
     JUnit test cases applied to them> JUnit test classes should be here src/test/java/it/polito/ezshop

## Step 1
| Classes  | JUnit test cases |
|--|--|
|Position, OrderImpl, ProductTypeImpl, JDBC, AccountBook, BalanceOperationImpl, CashPayment,CreditCard, CreditCardPayment, Payment, UserImpl, LoyaltyCard|All from TestEzShops.java (Suite for unit test WB+BB)|


## Step 2
| Classes  | JUnit test cases - package it.polito.ezshop.IntegrationTests |
|--|--|
|ProductTypeImpl, Position|testInitialNullPos() - Class ProductTypeImpl_Position_Int.java<br />testCorrectPos() - Class ProductTypeImpl_Position_Int.java<br />testResetPos1() - Class ProductTypeImpl_Position_Int.java<br />testResetPos2() - Class ProductTypeImpl_Position_Int.java<br />testChangePos() - Class ProductTypeImpl_Position_Int.java<br />|
| AccountBook | testAddBalanceOp_UpdateBalance_GetByDate() - AccountBook_intT.java <br /> testReset() - AccountBook_intT.java |
|LoyaltyCard, CustomerImpl, JDBC (Customer part)|testGetSetId() - CustomerImpl_Int.java<br />testGetSetCustomerName() - CustomerImpl_Int.java<br />testGetSetCustomerCard() - CustomerImpl_Int.java<br />testGetSetPoints() - CustomerImpl_Int.java<br />testModifyCustomer() - CustomerImpl_Int.java<br />testAttachCardToCustomer() - CustomerImpl_Int.java<br />testDetachCardFromCustomer() - CustomerImpl_Int.java<br />testCreateCustomerTable() - CustomerDb_Int.java<br />testInsertCustomerExistingId() - CustomerDb_Int.java<br />testInsertCustomerCorrect() - CustomerDb_Int.java<br />testModifyCustomerEmptyLoyaltyCard() - CustomerDb_Int.java<br />testModifyCustomerValidLoyaltyCard() - CustomerDb_Int.java<br />testModifyCustomerName() - CustomerDb_Int.java<br />testDeleteCustomer() - CustomerDb_Int.java<br />testLoadCustomers() - CustomerDb_Int.java|
| CartItem | testAddDecreaseQuantity - Class: CartItem_IntegrationTest.java <br/> testGetQuantity - Class: CartItem_IntegrationTest.java <br/> testGetProduct - Class: CartItem_IntegrationTest.java <br/> testGetSetProductDiscount - Class: CartItem_IntegrationTest.java <br/> testGetSetBarCode - Class: CartItem_IntegrationTest.java <br/> testGetSetProductDescription - Class: CartItem_IntegrationTest.java <br/> testGetSetAmount - Class: CartItem_IntegrationTest.java <br/> testGetSetPricePerUnit - Class: CartItem_IntegrationTest.java <br/> testGetSetDiscountRate - Class: CartItem_IntegrationTest.java <br/>|
| SaleTransactionImpl | testGetSetCart - Class: SaleTransactionImpl_IntegrationTest.java <br/> testApplyProdDiscount - Class: SaleTransactionImpl_IntegrationTest.java <br/> testComputeTransactionTotal - Class: SaleTransactionImpl_IntegrationTest.java <br/> testSetGetPayment - Class: SaleTransactionImpl_IntegrationTest.java <br/> testIsCardPayment - Class: SaleTransactionImpl_IntegrationTest.java <br/>  testGetPaymentCreditCard - Class: SaleTransactionImpl_IntegrationTest.java <br/> testGetPaymentCash - Class: SaleTransactionImpl_IntegrationTest.java <br/> testSetGetEntries - Class: SaleTransactionImpl_IntegrationTest.java | 
| ReturnTransaction | testGetSaleTransaction - Class: ReturnTransaction_IntegrationTest.java <br/> testGetSetReturnedCart - Class: ReturnTransaction_IntegrationTest.java <br/> testGetSetStatus - Class: ReturnTransaction_IntegrationTest.java <br/> testComputeReturnValue - Class: ReturnTransaction_IntegrationTest.java <br/> testSetGetPayment - Class: ReturnTransaction_IntegrationTest.java <br/> testIsPayed - Class: ReturnTransaction_IntegrationTest.java <br/> testGetPaymentCreditCard - Class: ReturnTransaction_IntegrationTest.java <br/> testGetPaymentCash - Class: ReturnTransaction_IntegrationTest.java <br/> testIsCardPayment - Class: ReturnTransaction_IntegrationTest.java <br/> | 
| JDBC | testSaleTransactionPaymentDB - Class: SaleTransactionDB_IntegrationTest.java <br/> testSaleTransactionPaymentDB_ErrorHandling1 - Class: SaleTransactionDB_IntegrationTest.java <br/> testSaleTransactionPaymentDB_ErrorHandling2 - Class: SaleTransactionDB_IntegrationTest.java <br/> testSaleTransactionPaymentDB_ErrorHandling3 - Class: SaleTransactionDB_IntegrationTest.java <br> testSaleTransactionPaymentDB_ErrorHandling4 - Class: SaleTransactionDB_IntegrationTest.java <br/> <br/> testAddReturnTransactionDB - Class: ReturnTransactionDB_IntegrationTest.java <br/> testAddReturnTransactionDB_ErrorHandling1 - Class: ReturnTransactionDB_IntegrationTest.java <br/> testAddReturnTransactionDB_ErrorHandling2 - Class: ReturnTransactionDB_IntegrationTest.java <br/> testAddReturnTransactionDB_ErrorHandling3 - Class: ReturnTransactionDB_IntegrationTest.java <br/> testDeleteReturnTransactionDB - Class: ReturnTransactionDB_IntegrationTest.java <br/> testDeleteReturnTransactionDB_ErrorHandling1 - Class: ReturnTransactionDB_IntegrationTest.java <br/> testDeleteReturnTransactionDB_ErrorHandling2 - Class: ReturnTransactionDB_IntegrationTest.java <br/> testCommitReturnTransaction - Class: ReturnTransactionDB_IntegrationTest.java <br/> testCommitReturnTransaction_ErrorHandling1 - Class: ReturnTransactionDB_IntegrationTest.java <br/>testCommitReturnTransaction_ErrorHandling2 - Class: ReturnTransactionDB_IntegrationTest.java <br/> testCommitReturnTransaction_ErrorHandling3 - Class: ReturnTransactionDB_IntegrationTest.java <br/> testCommitReturnTransaction_ErrorHandling4 - Class: ReturnTransactionDB_IntegrationTest.java <br/> testCommitReturnTransaction_ErrorHandling5 - Class: ReturnTransactionDB_IntegrationTest.java <br/> testCommitReturnTransaction_ErrorHandling6 - Class: ReturnTransactionDB_IntegrationTest.java <br/> testReturnTransactionPaymentDB - Class: ReturnTransactionDB_IntegrationTest.java <br/><br/> testLoadSaleTransaction - Class: SaleReturnTransactionLoadDB_IntegrationTest.java <br/> testLoadSaleTransaction_ErrorHandling1 - Class: SaleReturnTransactionLoadDB_IntegrationTest.java <br/> testLoadSaleTransaction_ErrorHandling3 - Class: SaleReturnTransactionLoadDB_IntegrationTest.java <br/> testLoadSaleTransaction_ErrorHandling2 - Class: SaleReturnTransactionLoadDB_IntegrationTest.java <br/> testLoadReturnTransaction - Class: SaleReturnTransactionLoadDB_IntegrationTest.java <br/> testLoadReturnTransaction_HandlingError1 - Class: SaleReturnTransactionLoadDB_IntegrationTest.java <br/> testLoadReturnTransaction_HandlingError2 - Class: SaleReturnTransactionLoadDB_IntegrationTest.java <br/> testLoadReturnTransaction_HandlingError3 - Class: SaleReturnTransactionLoadDB_IntegrationTest.java <br/> testLoadReturnTransaction_HandlingError4 - Class: SaleReturnTransactionLoadDB_IntegrationTest.java <br/> |



## Step 3 

| Classes                                                   | JUnit test cases - package it.polito.ezshop.IntegrationTests |
| --------------------------------------------------------- | ------------------------------------------------------------ |
| EzShop(partial, API for FR1), UserImpl, JDBC  | **createUser**<br />createUserTestUsernameInvalid() - EZShop_Users_Int.java<br />createUserTestUsernameAlreadyExisting() - EZShop_Users_Int.java<br />createUserTestPasswordInvalid() - EZShop_Users_Int.java<br />createUserTestRoleInvalid() - EZShop_Users_Int.java<br />createUserTestCorrect() - EZShop_Users_Int.java<br />**deleteUser**<br />deleteUserTestUnauthorizedOperation() - EZShop_Users_Int.java<br />deleteUserTestInvalidId() - EZShop_Users_Int.java<br />deleteUserTestCorrect() - EZShop_Users_Int.java<br />**getAllUsers**<br />getAllUsersTestUnauthorizedOperation() - EZShop_Users_Int.java<br />getAllUsersTestCorrect() - EZShop_Users_Int.java<br />**getUser**<br />getUserTestInvalid() - EZShop_Users_Int.java<br />getUserTestCorrect() - EZShop_Users_Int.java<br />**updateUserRights**<br />updateUserRightsTestInvalid() - EZShop_Users_Int.java<br />updateUserRightsCorrect() - EZShop_Users_Int.java|

## Step 4 

| Classes                                                   | JUnit test cases - package it.polito.ezshop.IntegrationTests |
| --------------------------------------------------------- | ------------------------------------------------------------ |
| EzShop (partial, only API for FR3), JDBC, UserImpl, ProductTypeImpl | **createProductType**<br />testUnauthorized() - Class EzShop_ProductTypeImpl_Int <br />testInvalidPricePerUnit() - Class EzShop_ProductTypeImpl_Int <br />testInvalidProductCode() - Class EzShop_ProductTypeImpl_Int <br />testInvalidProductDescription() - Class EzShop_ProductTypeImpl_Int <br />testNegRetValue() - Class EzShop_ProductTypeImpl_Int <br />testCorrectValue() - Class EzShop_ProductTypeImpl_Int <br />**updateProduct**<br />testUpdUnauthorized() - Class EzShop_ProductTypeImpl_Int <br />testUpdInvalidPricePerUnit() - Class EzShop_ProductTypeImpl_Int <br />testUpdInvalidProductCode() - Class EzShop_ProductTypeImpl_Int <br />testUpdInvalidProductDescription() - Class EzShop_ProductTypeImpl_Int <br />testUpdInvalidProductId() - Class EzShop_ProductTypeImpl_Int <br />testUpdFalse() - Class EzShop_ProductTypeImpl_Int <br />testUpdTrue() - Class EzShop_ProductTypeImpl_Int <br />**deleteProductType**<br />testDeleteUnauthorized() - Class EzShop_ProductTypeImpl_Int <br />testDeleteInvalidProductId() - Class EzShop_ProductTypeImpl_Int <br />testDeleteFalse() - Class EzShop_ProductTypeImpl_Int <br />testDeleteTrue() - Class EzShop_ProductTypeImpl_Int <br />**getAllProductTypes**<br />testGetAllUnauthorized() - Class EzShop_ProductTypeImpl_Int <br />testGetAllEmpty() - Class EzShop_ProductTypeImpl_Int <br />testGetAllSuccess() - Class EzShop_ProductTypeImpl_Int <br />**getProductTypeByBarCode**<br />testByCodeUnauthorized() - Class EzShop_ProductTypeImpl_Int <br />testByCodeInvalidProductCode() - Class EzShop_ProductTypeImpl_Int <br />testByCodeNull() - Class EzShop_ProductTypeImpl_Int <br />testByCodeSuccess() - Class EzShop_ProductTypeImpl_Int <br />**getProductTypesByDescription**<br />testByDescriptionUnauthorized() - Class EzShop_ProductTypeImpl_Int <br />testGetByCodeEmpty() - Class EzShop_ProductTypeImpl_Int <br />testGetByCodeSuccess() - Class EzShop_ProductTypeImpl_Int <br /> |

## Step 5

| Classes                                                      | JUnit test cases- package it.polito.ezshop.IntegrationTests  |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| EzShop (partial, only API for FR3 & FR4), JDBC, UserImpl, ProductTypeImpl, Position,  | **updatePosition**<br />testUpdPosUnauthorized() - Class EzShop_ProdType_Position_Int <br />testUpdPosInvalidProductId() - Class EzShop_ProdType_Position_Int <br />testUpdPosInvalidLocation() - Class EzShop_ProdType_Position_Int <br />testUpdPosTrue() - Class EzShop_ProdType_Position_Int <br />testUpdPosFalse() - Class EzShop_ProdType_Position_Int <br />**updateQuantity**<br />testUpdQTYUnauthorized() - Class EzShop_ProdType_Position_Int <br />testUpdQTYInvalidProductId() - Class EzShop_ProdType_Position_Int <br />testUpdQTYFalse() - Class EzShop_ProdType_Position_Int <br />testUpdQTYTrue() - Class EzShop_ProdType_Position_Int <br /> |

## Step 6

| Classes                                                      | JUnit test cases- package it.polito.ezshop.IntegrationTests  |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| EzShop (partial, only API for FR3 & FR4), JDBC, UserImpl, ProductTypeImpl, Position, OrderImpl | **issueOrder**<br />testIssueUnauthorized() - Class EzShop_Order_ProdType_Int<br />testIssueInvalidPPU() - Class EzShop_Order_ProdType_Int<br />testIssueInvalidQTY() - Class EzShop_Order_ProdType_Int<br />testIssueInvalidProductCode() - Class EzShop_Order_ProdType_Int<br />testIssueInvalid() - Class EzShop_Order_ProdType_Int<br />testIssueValid() - Class EzShop_Order_ProdType_Int<br />**payOrderFor**<br />testPayOrderForUnauthorized() - Class EzShop_Order_ProdType_Int<br />testPayOrderForInvalidPPU() - Class EzShop_Order_ProdType_Int<br />testPayOrderForInvalidQTY() - Class EzShop_Order_ProdType_Int<br />testPayOrderForInvalidProductCode() - Class EzShop_Order_ProdType_Int<br />testPayOrderForInvalid() - Class EzShop_Order_ProdType_Int<br />testPayOrderForValid() - Class EzShop_Order_ProdType_Int<br />**payOrder**<br />testPayOrderUnauthorized() - Class EzShop_Order_ProdType_Int<br />testPayOrderInvalidOrderId() - Class EzShop_Order_ProdType_Int<br />testPayOrderFalse() - Class EzShop_Order_ProdType_Int<br />testPayOrderTrue() - Class EzShop_Order_ProdType_Int<br />**recordOrderArrival**<br />testRecordOrderUnauthorized() - Class EzShop_Order_ProdType_Int<br />testRecordOrderInvalidOrderId() - Class EzShop_Order_ProdType_Int<br />testRecordOrderInvalidLocation() - Class EzShop_Order_ProdType_Int<br />testRecordOrderFalse() - Class EzShop_Order_ProdType_Int<br />testRecordOrderTrue() - Class EzShop_Order_ProdType_Int<br />**recordOrderArrivalRFID**<br />testRecordOrderRFIDEXC() - Class EzShop_Order_ProdType_Int<br />testRecordOrderRFIDReturn() - Class EzShop_Order_ProdType_Int<br />**getAllOrders**<br />testGetAllUnauthorized() - Class EzShop_Order_ProdType_Int<br />testGetAllEmpty() - Class EzShop_Order_ProdType_Int<br />testGetAllValid() - Class EzShop_Order_ProdType_Int<br /> |

## Step 7

| Classes | JUnit test cases |
| ------- | ---------------- |
|  EzShop (partial, API for FR5), JDBC, UserImpl, ProductTypeImpl, Position,OrderImpl, CustomerImpl, LoyaltyCard   | **defineCustomer**<br />defineCustomerTestInvalid() - EZShop_Customers_Int.java<br />defineCustomerTestCorrect() - EZShop_Customers_Int.java<br />**modifyCustomer**<br />modifyCustomerTestInvalid() - EZShop_Customers_Int.java<br />modifyCustomerTestCorrect() - EZShop_Customers_Int.java<br />**deleteCustomer**<br />deleteCustomerTestInvalid() - EZShop_Customers_Int.java<br />deleteCustomerTestCorrect() - EZShop_Customers_Int.java<br />**getCustomer**<br />getCustomerTestInvalid() - EZShop_Customers_Int.java<br />getCustomerTestCorrect() - EZShop_Customers_Int.java<br />**getAllCustomers**<br />getAllCustomersTest() - EZShop_Customers_Int.java<br />**createCard**<br />createCardTest() - EZShop_Customers_Int.java<br />**attachCardToCustomer**<br />attachCardToCustomerTest() - EZShop_Customers_Int.java<br />**modifyPointsOnCard**<br />modifyPointsOnCardTest() - EZShop_Customers_Int.java       |

## Step 8

| Classes | JUnit test cases |
| ------- | ---------------- |
| EzShop (partial, only API for FR6 and is used some API from FR7), JDBC, UserImpl, ProductTypeImpl, Position, OrderImpl, CustomerImpl, LoyaltyCard, CartItem, SaleTransactonImpl      |   **StartSaleTransaction**<br/>testStartSaleTransaction() - Class: SaleTransactionIntegrationTest.java <br/> testStartSaleTransaction_ExceptionHandling - Class: SaleTransactionIntegrationTest.java <br/> **AddProductToSale**<br/>  testAddProductsToSale - Class: SaleTransactionIntegrationTest.java <br/> testAddProductToSale_ExceptionHandling - Class: SaleTransactionIntegrationTest.java <br/>  testAddProductToSale_ReturnValue - Class: SaleTransactionIntegrationTest.java <br/>   **DeleteProductFromSale**  <br/>  testDeleteProductFromSale - Class: SaleTransactionIntegrationTest.java <br/>  testDeleteProductFromSale_ExceptionHandling - Class: SaleTransactionIntegrationTest.java <br/> testDeleteProductFromSale_ReturnValue - Class: SaleTransactionIntegrationTest.java <br/>**AddProductToSaleRFID** <br/>  testAddProductToSaleRFID - Class: EzShop_Sale_Return_RFID_IntT.java <br/>testAddProductToSaleRFIDEXC - Class: EzShop_Sale_Return_RFID_IntT.java <br/> **DeleteProductFromSaleRFID** <br/>  testDeleteProductFromSaleRFID - Class: EzShop_Sale_Return_RFID_IntT.java <br/>testDeleteProductFromSaleRFIDEXC - Class: EzShop_Sale_Return_RFID_IntT.java <br/>**ApplyDiscountRateToProduct** <br/> testApplyDiscountRateToProduct - Class: SaleTransactionIntegrationTest.java <br/> testApplyDiscountRateToProduct_ExceptionHandling - Class: SaleTransactionIntegrationTest.java <br/> testApplyDiscountRateToProduct_ReturnValue - Class: SaleTransactionIntegrationTest.java <br/> **ApplyDiscountRateToSale** <br/> testApplyDiscountRateToSale - Class: SaleTransactionIntegrationTest.java <br/> testApplyDiscountRateToSale_ExceptionHandling - Class: SaleTransactionIntegrationTest.java <br/> testApplyDiscountRateToSale_ReturnValue - Class: SaleTransactionIntegrationTest.java <br/> **ComputePointForSale** <br/> testComputePointForSale - Class: SaleTransactionIntegrationTest.java <br/> testComputePointForSale_ExceptionHandling - Class: SaleTransactionIntegrationTest.java <br/> testComputePointForSale_ReturnValue - Class: SaleTransactionIntegrationTest.java <br/> **EndSaleTransaction** <br/> testEndSaleTransaction - Class: SaleTransactionIntegrationTest.java <br/> testEndSaleTransaction_ExceptionHandling - Class: SaleTransactionIntegrationTest.java <br/> testEndSaleTransaction_ReturnValue - Class: SaleTransactionIntegrationTest.java <br/> **DeleteReturnTransaction** <br/> testDeleteSaleTransaction - Class: SaleTransactionIntegrationTest.java <br/> testDeleteSaleTransaction_ExceptionHandling - Class: SaleTransactionIntegrationTest.java <br/>  testDeleteSaleTransaction_ReturnValue - Class: SaleTransactionIntegrationTest.java <br/> **GetSaleTransaction** <br/> testGetSaleTransaction - Class: SaleTransactionIntegrationTest.java<br/> testGetSaleTransaction_ExceptionHandling - Class: SaleTransactionIntegrationTest.java <br/> testGetSaleTransaction_ReturnValue - Class: SaleTransactionIntegrationTest.java <br/>|
|  EzShop (partial, only API for FR6 and is used some API from FR7), JDBC, UserImpl, ProductTypeImpl, Position, OrderImpl, CustomerImpl, LoyaltyCard, CartItem, SaleTransactonImpl, ReturnTransaction | **StartReturnTransaction** <br/> testStartReturnTransaction - Class: ReturnTransationIntegrationTest.java <br/> testStartReturnTransaction_ExceptionHandling - Class: ReturnTransationIntegrationTest.java <br/> testStartReturnTransaction_ReturnValue - Class: ReturnTransationIntegrationTest.java <br/> **ReturnProduct** <br/> testReturnProduct - Class: ReturnTransationIntegrationTest.java <br/> testReturnProduct_ExceptionHandling - Class: ReturnTransationIntegrationTest.java <br/> testReturnProduct_ReturnValue - Class: ReturnTransationIntegrationTest.java <br/> **ReturnProductRFID**<br/>  testreturnProdRFIDEXC - Class: EzShop_Sale_Return_RFID_IntT.java <br/> testreturnProdRFIDRet - Class: EzShop_Sale_Return_RFID_IntT.java <br/> **EndReturnTransaction** <br/> testEndReturnTransaction1 - Class: ReturnTransationIntegrationTest.java <br/> testEndReturnTransaction2 - Class: ReturnTransationIntegrationTest.java <br/> testEndReturnTransaction3 - Class: ReturnTransationIntegrationTest.java <br/> testEndReturnTransaction4 - Class: ReturnTransationIntegrationTest.java <br/> testEndReturnTransaction_ExceptionHandling - Class: ReturnTransationIntegrationTest.java <br/> testEndReturnTransaction_ReturnValue - Class: ReturnTransationIntegrationTest.java <br/> **DeleteReturnTransaction** <br/> testDeleteReturnTransaction - Class: ReturnTransationIntegrationTest.java <br/> testDeleteReturnTransaction_ExceptionHandling - Class: ReturnTransationIntegrationTest.java <br/> testDeleteReturnTransaction_ReturnValue - Class: ReturnTransationIntegrationTest.java <br/>|


## Step 9

| Classes | JUnit test cases |
| ------- | ---------------- |
|    EzShop (all FRs except FR8), JDBC, UserImpl,ProductTypeImpl, Position, OrderImpl, CustomerImpl, LoyaltyCard, CartItem, SaleTransactonImpl, ReturnTransaction, Payment    | **ReceiveCashPayment**<br /> testCashPaymentEXC() - EzShop_Payments_intT.java <br />  testCashPaymentRet() - EzShop_Payments_intT.java <br /> **ReceiveCreditCardPayment**<br /> testCCPaymentEXC() - EzShop_Payments_intT.java <br /> testCCPaymentRet() - EzShop_Payments_intT.java <br />			**ReturnCashPayment**<br /> testRCashPaymentEXC() - EzShop_Payments_intT.java <br /> testRCashPaymentRet() - EzShop_Payments_intT.java <br />   **ReturnCreditCardPayment**<br /> testRCCPaymentEXC() - EzShop_Payments_intT.java <br /> testRCCPaymentRet() - EzShop_Payments_intT.java <br />      |

## Step 10

| Classes | JUnit test cases |
| ------- | ---------------- |
| EzShop all FR, JDBC, UserImpl, ProductTypeImpl, Position, OrderImpl, CustomerImpl, LoyaltyCard, CartItem, SaleTransactonImpl, ReturnTransaction, Payment, BalanceOperationImpl  | **RecordBalanceUpdate**<br /> testBalanceUpdEXC() - EzShop_Balance_intT.java <br />  testBalanceUpdRet() - EzShop_Balance_intT.java <br /> **getCreditsAndDebits**<br /> testGetCredDebEXC() - EzShop_Balance_intT.java <br /> testGetCredDebRet() - EzShop_Balance_intT.java <br />			**ComputeBalance**<br /> testComputeBalanceEXC() - EzShop_Balance_intT.java <br /> testComputeBalanceRet() - EzShop_Balance_intT.java <br />| 






# Scenarios


<If needed, define here additional scenarios for the application. Scenarios should be named
 referring the UC in the OfficialRequirements that they detail>

## Scenario 2-4

| Scenario | search User |
| ------------- |:-------------:|
|  Precondition     | Admin A exists and logged in<br />Account U exists |
|  Post condition     |                                                    |
| Step#        | Description  |
|  1     |      A inserts info about user U to retrieve       |
|  2     | U is retrieved from the system |

## Scenario 2-5

| Scenario       |             List all Users              |
| -------------- | :-------------------------------------: |
| Precondition   |   Admin A exists and logged in<br />    |
| Post condition |                                         |
| Step#          |               Description               |
| 1              |       A selects to list all users       |
| 2              | All users are retrieved from the system |

## Scenario 4-5

| Scenario       |           delete a Customer            |
| -------------- | :------------------------------------: |
| Precondition   | User U logged in<br />Account C exists |
| Post condition |           Account C deleted            |
| Step#          |              Description               |
| 1              |          U selects account C           |
| 2              |       C deleted from the system        |



## Scenario 4-6

| Scenario       |              search a Customer              |
| -------------- | :-----------------------------------------: |
| Precondition   |   User U logged in<br />Account C exists    |
| Post condition |                                             |
| Step#          |                 Description                 |
| 1              | U inserts info about customer C to retrieve |
| 2              |       C is retrieved from the system        |

## Scenario 4-7

| Scenario       |             List all Customers              |
| -------------- | :-----------------------------------------: |
| Precondition   |              User U logged in               |
| Post condition |                                             |
| Step#          |                 Description                 |
| 1              |       U selects to list all customers       |
| 2              | All customers are retrieved from the system |

## Scenario 4-8

| Scenario       |                Create Customer Card                 |
| -------------- | :-------------------------------------------------: |
| Precondition   | User U logged in<br />Customer Card C doesn't exist |
| Post condition |                      C exists                       |
| Step#          |                     Description                     |
| 1              |                U selects to create C                |
| 2              |            C is inserted into the system            |

## Scenario 4-9

| Scenario       |         Modify points on Customer Card          |
| -------------- | :---------------------------------------------: |
| Precondition   |  User U logged in<br />Customer Card C exists   |
| Post condition |               Points on C updated               |
| Step#          |                   Description                   |
| 1              | U selects the card C where to update the points |
| 2              |     U inserts the points to be added into C     |
| 3              |             C's points are updated              |




# Coverage of Scenarios and FR


<Report in the following table the coverage of  scenarios (from official requirements and from above) vs FR. 
Report also for each of the scenarios the (one or more) API JUnit tests that cover it. >




| Scenario ID | Functional Requirements covered | JUnit  Test(s) |
| ----------- | ------------------------------- | ----------- |
|  1.1         | FR3.1 + FR4.2                             |    Package: it.polito.ezshop.IntegrationTests<br/>Class:UC1Scenarios.java<br/>     testScenario1_1()    |
|  1.2         | FR4.2                             |     Package: it.polito.ezshop.IntegrationTests<br/>Class:UC1Scenarios.java<br/>     testScenario1_2()        |
|  1.3         | FR3.1                             |    Package: it.polito.ezshop.IntegrationTests<br/>Class:UC1Scenarios.java<br/>     testScenario1_3()         |
| 2.1 | FR1.1 (Define a new user) | Package: it.polito.ezshop.IntegrationTests<br />Class: UC2Scenarios.java<br />     testScenario2_1() |
| 2.2 | FR1.2 | Package: it.polito.ezshop.IntegrationTests<br />Class: UC2Scenarios.java<br />     testScenario2_2() |
| 2.3 | FR1.1 (Modify an existing user)           | Package: it.polito.ezshop.IntegrationTests<br />Class: UC2Scenarios.java<br />     testScenario2_3() |
| 2.4 | FR1.4 | Package: it.polito.ezshop.IntegrationTests<br/>Class: EzShop_Users_Int.java<br/>    getUserTest(); |
| 2.5 | FR1.3 | Package: it.polito.ezshop.IntegrationTests<br/>Class: EzShop_Users_Int.java<br/>    getAllUsersTest(); |
| / | FR1.5 | Package: it.polito.ezshop.IntegrationTests<br/>All tests are performed by checking the user rights when needed |
|  3.1         | FR4.3                             |   Package: it.polito.ezshop.IntegrationTests<br/>Class:UC3Scenarios.java<br/>     testScenario3_1()          |
|  3.2        | FR4.5                             |    Package: it.polito.ezshop.IntegrationTests<br/>Class:UC3Scenarios.java<br/>     testScenario3_2()         |
|  3.3        | FR4.6                             |     Package: it.polito.ezshop.IntegrationTests<br/>Class:UC3Scenarios.java<br/>     testScenario3_3()        |
| 4.1 | FR5.1 (Define a new customer) | Package: it.polito.ezshop.IntegrationTests<br />Class: UC4Scenarios.java<br />     testScenario4_1() |
| 4.2 | FR5.6 | Package: it.polito.ezshop.IntegrationTests<br />Class: UC4Scenarios.java<br />     testScenario4_2() |
| 4.3 | FR5.6 | Package: it.polito.ezshop.IntegrationTests<br />Class: UC4Scenarios.java<br />     testScenario4_3() |
| 4.4 | FR5.1 (Modify customer) | Package: it.polito.ezshop.IntegrationTests<br />Class: UC4Scenarios.java<br />     testScenario4_4() |
| 4.5 | FR5.2 | Package: it.polito.ezshop.IntegrationTests<br/>Class: EzShop_Customers_Int.java<br/>    deleteCustomerTest(); |
| 4.6 | FR5.3 | Package: it.polito.ezshop.IntegrationTests<br/>Class: EzShop_Customers_Int.java<br/>    getCustomerTest(); |
| 4.7 | FR5.4 | Package: it.polito.ezshop.IntegrationTests<br/>Class: EzShop_Customers_Int.java<br/>    getAllCustomersTest(); |
| 4.8 | FR5.5 | Package: it.polito.ezshop.IntegrationTests<br/>Class: EzShop_Customers_Int.java<br/>    createCardTest(); |
| 4.9 | FR5.6 | Package: it.polito.ezshop.IntegrationTests<br/>Class: EzShop_Customers_Int.java<br/>    modifyPointsOnCardTest(); |
| 5.1 | FR- (Login) | Package: it.polito.ezshop.IntegrationTests<br />Class: UC5Scenarios.java<br />     testScenario5_1() |
| 5.2 | FR- (Logout) | Package: it.polito.ezshop.IntegrationTests<br />Class: UC4Scenarios.java<br />     testScenario5_2() |
|  /         | FR3  (and all sub-FRs such as FR3.1 etc)                           |    Package: it.polito.ezshop.IntegrationTests<br/>Class:EzShop_ProductTypeImpl_Int.java<br/>     ALL TESTS         |
|  /         | FR4   (and all sub-FRs such as FR4.1 etc)                           |      Package: it.polito.ezshop.IntegrationTests<br/>Classes:EzShop_ProdType_Position_Int.java<br/> EzShop_Order_ProdType_Int.java<br/>    ALL TESTS       |
| 6.1 | FR: 4.1 6.1, 6.2, 6.7*, 6.10, 6.11, 7.1 or 7.2, 8.2  | Package: it.polito.ezshop.IntegrationTests<br/>Class:UC6Scenarios.java<br/>     TEST: scenario6_1ATest(), scenario6_1BTest()      |
| 6.2 | FR: 4.1, 6.1, 6.2, 6.5 6.7*, 6.10, 6.11, 7.1 or 7.2, 8.2   | Package: it.polito.ezshop.IntegrationTests<br/>Class:UC6Scenarios.java<br/>    TEST: scenario6_2ATest(), scenario6_2BTest()  |
| 6.3 | FR: 4.1, 6.1, 6.2, 6.4 6.7*, 6.10, 6.11, 7.1 or 7.2, 8.2  | Package: it.polito.ezshop.IntegrationTests<br/>Class:UC6Scenarios.java<br/>    TEST: scenario6_3ATest(), scenario6_3BTest()  | 
| 6.4 | FR: 4.1, 5.7, 6.1, 6.2, 6.6 6.7*, 6.10, 6.11, 7.1 or 7.2, 8.2 |  Package: it.polito.ezshop.IntegrationTests<br/>Class:UC6Scenarios.java<br/>    TEST: scenario6_4ATest(), scenario6_4BTest() |
| 6.5 | FR: 4.1 6.1, 6.2, 6.7*, 6.10, 6.11, 7(payment canceled)  |  Package: it.polito.ezshop.IntegrationTests<br/>Class:UC6Scenarios.java<br/>    TEST: scenario6_5Test() |
| 6.6 | FR: 4.1 6.1, 6.2, 6.7*, 6.10, 6.11, 7.1 or 7.2, 8.2 |  Package: it.polito.ezshop.IntegrationTests<br/>Class:UC6Scenarios.java<br/>     TEST: scenario6_6Test() |
| 7.1         | FR7 + FR7.2 + FR8.2             | Package: it.polito.ezshop.IntegrationTests<br/>Class: EzShop_Payments_Int.java<br/> testCCPaymentRet();            |
| 7.2         | FR7 + FR7.2                     | Package: it.polito.ezshop.IntegrationTests<br/>Class: EzShop_Payments_Int.java<br/> testCCPaymentEXC();            |
| 7.3         | FR7 + FR7.2                     | Package: it.polito.ezshop.IntegrationTests<br/>Class: EzShop_Payments_Int.java<br/> testCCPaymentRet();            |
| 7.4         | FR7 + FR7.1 + FR8.2             | Package: it.polito.ezshop.IntegrationTests<br/>Class: EzShop_Payments_Int.java<br/> testCashPaymentRet();            |
| 8.1 | FR: 4.1, 6.12, 6.13, 6.14, 6.15, 7.2, 8.1  | Package: it.polito.ezshop.IntegrationTests<br/>Class:UC8Scenarios.java<br/>     TEST: scenario8_1Test() |
| 8.2 | FR: 4.1, 6.12, 6.13, 6.14, 6.15, 7.1, 8.1 |  Package: it.polito.ezshop.IntegrationTests<br/>Class:UC8Scenarios.java<br/>     TEST: scenario8_2Test()|
| 9.1        | FR8 + FR8.3                      | Package: it.polito.ezshop.IntegrationTests<br/>Class: EzShop_Balance_Int.java<br/> testGetCredDebRet();            |
| 10.1        | FR7 + FR7.4 + FR8.1             | Package: it.polito.ezshop.IntegrationTests<br/>Class: EzShop_Payments_Int.java<br/> testRCCPaymentRet();            |
| 10.2        | FR7 + FR7.3 + FR8.1             | Package: it.polito.ezshop.IntegrationTests<br/>Class: EzShop_Payments_Int.java<br/> testRCashPaymentRet();            |

_NOTE_: * means FR is partially covered (barcode is required but hardcoded in the test).

# Coverage of Non Functional Requirements


<Report in the following table the coverage of the Non Functional Requirements of the application - only those that can be tested with automated testing frameworks.>


### 

| Non Functional Requirement | Test name |
| -------------------------- | --------- |
|        NFR2                    | Package it.polito.ezshop<br/>Class: TestEzShopIntegrationSuite.java<br/>ALL TESTS.<br/>By running the suite all of the test methods are executed in ~0.3sec, this means that the 0.5s requirement for functioning is respected. |
|        NFR4                    | Package it.polito.ezshop.BBTests<br/>Class:  ProductTypeImplTestvalidateProdCode.java<br/>ALL TEST CASES      |
| NFR5 | Package: it.polito.ezshop.BBTests<br/>Class: CreditCardBBTests.java<br/>    testCCValid(), testCCnotValid(); <br/>  <br/> Package: it.polito.ezshop.WBTests<br/>Class: CreditCardWBTests.java<br/>    testValidator();|
| NFR6 | Package: it.polito.ezshop.IntegrationTests<br/>Class: EzShop_Customer_Int.java<br/>    createCardTest(); |


