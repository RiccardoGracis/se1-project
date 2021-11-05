package it.polito.ezshop.integrationTests;

import static org.junit.Assert.*;
import org.junit.*;
import it.polito.ezshop.data.*;
import it.polito.ezshop.exceptions.*;
import java.io.IOException;
import java.sql.SQLException;

public class UC6Scenarios {

    @Before
    public void initEnvironment() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, SQLException, IOException {
        JDBC db = new JDBC();
        //shop.reset();
        //create
        db.deleteEntireDB();
        //EZShop shop = new EZShop();
        db.initDB(null, null, null, null);
        db.openConnection();
        // create tables in the DB
        db.createCustomerTable();
        db.createLoyaltyCardTable();
        db.createUserTable();
        db.createBalanceTable();
        db.createProductTypeTable();
        //Load Product in to the system
        db.insertProductType(1, "11-AA-11", "12345678901231", "Product1", 19.90, 30, 0.00, "testing product, please remove if present");
        db.insertProductType(2, "11-BB-11", "12345678901248", "Product2", 29.90, 30, 0.20, "testing product, please remove if present");
        db.insertProductType(3, "11-CC-11", "12345678901255", "Product3", 39.90, 30, 0.00, "testing product, please remove if present");
        db.insertProductType(4, "11-DD-11", "12345678901262", "Product4", 49.90, 30, 0.00, "testing product, please remove if present");
        db.insertCustomer(1, "customer");
        db.insertCustomerCard("1234567890", 0, true);
        db.attachCustomerCard(1, "1234567890");
        db.insertUser(1, "admin", "admin", "Administrator");
        db.insertUser(2, "cashier", "cashier", "Cashier");
        db.closeConnection();
        //Create users (Administrator and cashier)
        //shop.createUser("admin", "admin", "Administrator");
        //shop.createUser("cashier", "cashier", "Cashier");
        //reset credit card file
        CreditCard.resetCCFile("./creditcards.txt");
    }

    @After
    public void resetEnvironment() throws SQLException, IOException {
        JDBC db = new JDBC();
        db.deleteEntireDB();
        CreditCard.resetCCFile("./creditcards.txt");
    }

    @Test
    public void scenario6_1ATest() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidQuantityException, InvalidTransactionIdException, InvalidProductCodeException, InvalidPaymentException {
        EZShop shop = new EZShop();
        shop.login("cashier", "cashier");
        Integer saleId = shop.startSaleTransaction(); //#STEP1
        assertTrue(shop.addProductToSale(saleId, "12345678901231", 3)); // 3 * 19.90 = 59.70 #STEP2-3 (PRE-CONDITION)
        shop.logout(); //this is not done in the scenarios but is only to verify specific step which requires high privileges to be executed
        shop.login("admin", "admin");
        assertEquals((int) shop.getProductTypeByBarCode("12345678901231").getQuantity(), 27); //#STEP 4
        shop.logout();
        shop.login("cashier", "cashier");
        assertTrue(shop.endSaleTransaction(saleId)); //Close the transaction #STEP5
        assertEquals(shop.getSaleTransaction(saleId).getPrice(), 59.70, 0.01);
        //System ask for the payment type (CASH is selected) #STEP6
        assertEquals(shop.receiveCashPayment(saleId, 70.00), 10.30, 0.01); //#STEP 7-8
        //Print receipt #STEP 9
        //POST-CONDITION
        shop.logout(); //this is not done in the scenarios but is only to verify specific step which requires high privileges to be executed
        shop.login("admin", "admin");
        assertEquals(shop.computeBalance(), shop.getSaleTransaction(saleId).getPrice(), 0.01); //#STEP 10
    }

    @Test
    public void scenario6_1BTest() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidQuantityException, InvalidTransactionIdException, InvalidProductCodeException, InvalidPaymentException, InvalidCreditCardException {
        EZShop shop = new EZShop();
        shop.login("cashier", "cashier");
        Integer saleId = shop.startSaleTransaction(); //#STEP1
        assertTrue(shop.addProductToSale(saleId, "12345678901231", 3)); // 3 * 19.90 = 59.70 #STEP2-3 (PRE-CONDITION)
        shop.logout(); //this is not done in the scenarios but is only to verify specific step which requires high privileges to be executed
        shop.login("admin", "admin");
        assertEquals((int) shop.getProductTypeByBarCode("12345678901231").getQuantity(), 27); //#STEP 4
        shop.logout();
        shop.login("cashier", "cashier");
        assertTrue(shop.endSaleTransaction(saleId)); //Close the transaction #STEP5
        assertEquals(shop.getSaleTransaction(saleId).getPrice(), 59.70, 0.01);
        //System ask for the payment type (CREDIT CARD is selected) #STEP6
        assertTrue(shop.receiveCreditCardPayment(saleId, "4485370086510891")); //#STEP 7-8
        //Print receipt #STEP 9
        //POST-CONDITION
        shop.logout(); //this is not done in the scenarios but is only to verify specific step which requires high privileges to be executed
        shop.login("admin", "admin");
        assertEquals(shop.computeBalance(), shop.getSaleTransaction(saleId).getPrice(), 0.01); //#STEP 10
    }

    @Test
    public void scenario6_2ATest() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidQuantityException, InvalidTransactionIdException, InvalidProductCodeException, InvalidPaymentException, InvalidCreditCardException, InvalidDiscountRateException {
        EZShop shop = new EZShop();
        shop.login("cashier", "cashier");
        Integer saleId = shop.startSaleTransaction(); //#STEP1
        assertTrue(shop.addProductToSale(saleId, "12345678901231", 3)); // 3 * 19.90 = 59.70 #STEP2-3 (PRE-CONDITION)
        shop.logout(); //this is not done in the scenarios but is only to verify specific step which requires high privileges to be executed
        shop.login("admin", "admin");
        assertEquals((int) shop.getProductTypeByBarCode("12345678901231").getQuantity(), 27); //#STEP 4
        shop.logout();
        shop.login("cashier", "cashier");
        assertTrue(shop.applyDiscountRateToProduct(saleId, "12345678901231", 0.20)); //#STEP5
        assertTrue(shop.endSaleTransaction(saleId)); //Close the transaction #STEP6
        SaleTransactionImpl tmp = (SaleTransactionImpl) shop.getSaleTransaction(saleId); //verify step 5
        assertEquals(tmp.getCart().get("12345678901231").getDiscountRate(), 0.20, 0.01);
        assertEquals(shop.getSaleTransaction(saleId).getPrice(), 47.76, 0.01); // -> 59.70*0.8 = 47.76
        //System ask for the payment type (CASH is selected) #STEP7
        assertEquals(shop.receiveCashPayment(saleId, 70.00), 22.24, 0.01); //#STEP 8-9
        //Print receipt #STEP 10
        shop.logout(); //this is not done in the scenarios but is only to verify specific step which requires high privileges to be executed
        shop.login("admin", "admin");
        assertEquals(shop.computeBalance(), shop.getSaleTransaction(saleId).getPrice(), 0.001); //#STEP 11
    }

    @Test
    public void scenario6_2BTest() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidQuantityException, InvalidTransactionIdException, InvalidProductCodeException, InvalidPaymentException, InvalidCreditCardException, InvalidDiscountRateException {
        EZShop shop = new EZShop();
        shop.login("cashier", "cashier");
        Integer saleId = shop.startSaleTransaction(); //#STEP1
        assertTrue(shop.addProductToSale(saleId, "12345678901231", 3)); // 3 * 19.90 = 59.70 #STEP2-3 (PRE-CONDITION)
        shop.logout(); //this is not done in the scenarios but is only to verify specific step which requires high privileges to be executed
        shop.login("admin", "admin");
        assertEquals((int) shop.getProductTypeByBarCode("12345678901231").getQuantity(), 27); //#STEP 4
        shop.logout();
        shop.login("cashier", "cashier");
        assertTrue(shop.applyDiscountRateToProduct(saleId, "12345678901231", 0.20)); //#STEP5
        assertTrue(shop.endSaleTransaction(saleId)); //Close the transaction #STEP6
        SaleTransactionImpl tmp = (SaleTransactionImpl) shop.getSaleTransaction(saleId); //verify step 5
        assertEquals(tmp.getCart().get("12345678901231").getDiscountRate(), 0.20, 0.01);
        assertEquals(shop.getSaleTransaction(saleId).getPrice(), 47.76, 0.01); // -> 59.70*0.8 = 47.76
        //System ask for the payment type (CREDIT CARD is selected) #STEP7
        assertTrue(shop.receiveCreditCardPayment(saleId, "4485370086510891")); //#STEP 8-9
        //Print receipt #STEP 10
        //POST-CONDITION
        shop.logout(); //this is not done in the scenarios but is only to verify specific step which requires high privileges to be executed
        shop.login("admin", "admin");
        assertEquals(shop.computeBalance(), shop.getSaleTransaction(saleId).getPrice(), 0.01); //#STEP 11
    }

    @Test
    public void scenario6_3ATest() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidQuantityException, InvalidTransactionIdException, InvalidProductCodeException, InvalidPaymentException, InvalidCreditCardException, InvalidDiscountRateException {
        EZShop shop = new EZShop();
        shop.login("cashier", "cashier");
        Integer saleId = shop.startSaleTransaction(); //#STEP1
        assertTrue(shop.addProductToSale(saleId, "12345678901231", 3)); // 3 * 19.90 = 59.70 #STEP2-3 (PRE-CONDITION)
        shop.logout(); //this is not done in the scenarios but is only to verify specific step which requires high privileges to be executed
        shop.login("admin", "admin");
        assertEquals((int) shop.getProductTypeByBarCode("12345678901231").getQuantity(), 27); //#STEP 4
        shop.logout();
        shop.login("cashier", "cashier");
        assertTrue(shop.applyDiscountRateToSale(saleId, 0.50)); //#STEP5
        assertTrue(shop.endSaleTransaction(saleId)); //Close the transaction #STEP6
        assertEquals(shop.getSaleTransaction(saleId).getDiscountRate(), 0.50, 0.01); //Verify step 5
        assertEquals(shop.getSaleTransaction(saleId).getPrice(), 29.85, 0.01); // -> 59.70*0.5 = 29.85
        //System ask for the payment type (CASH is selected) #STEP7
        assertEquals(shop.receiveCashPayment(saleId, 70.00), 40.16, 0.01); //#STEP 8-9
        //Print receipt #STEP 10
        //POST-CONDITION
        shop.logout(); //this is not done in the scenarios but is only to verify specific step which requires high privileges to be executed
        shop.login("admin", "admin");
        assertEquals(shop.computeBalance(), shop.getSaleTransaction(saleId).getPrice(), 0.01); //#STEP 11
    }

    @Test
    public void scenario6_3BTest() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidQuantityException, InvalidTransactionIdException, InvalidProductCodeException, InvalidPaymentException, InvalidCreditCardException, InvalidDiscountRateException {
        EZShop shop = new EZShop();
        shop.login("cashier", "cashier");
        Integer saleId = shop.startSaleTransaction(); //#STEP1
        assertTrue(shop.addProductToSale(saleId, "12345678901231", 3)); // 3 * 19.90 = 59.70 #STEP2-3 (PRE-CONDITION)
        shop.logout(); //this is not done in the scenarios but is only to verify specific step which requires high privileges to be executed
        shop.login("admin", "admin");
        assertEquals((int) shop.getProductTypeByBarCode("12345678901231").getQuantity(), 27); //#STEP 4
        shop.logout();
        shop.login("cashier", "cashier");
        assertTrue(shop.applyDiscountRateToSale(saleId, 0.50)); //#STEP5
        assertTrue(shop.endSaleTransaction(saleId)); //Close the transaction #STEP6
        assertEquals(shop.getSaleTransaction(saleId).getDiscountRate(), 0.50, 0.01); //Verify step 5
        assertEquals(shop.getSaleTransaction(saleId).getPrice(), 29.85, 0.01); // -> 59.70*0.5 = 29.85
        //System ask for the payment type (CASH is selected) #STEP7
        assertTrue(shop.receiveCreditCardPayment(saleId, "4485370086510891")); //#STEP 8-9
        //Print receipt #STEP 10
        //POST-CONDITION
        shop.logout(); //this is not done in the scenarios but is only to verify specific step which requires high privileges to be executed
        shop.login("admin", "admin");
        assertEquals(shop.computeBalance(), shop.getSaleTransaction(saleId).getPrice(), 0.01); //#STEP 11
    }

    @Test
    public void scenario6_4ATest() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidQuantityException, InvalidTransactionIdException, InvalidProductCodeException, InvalidPaymentException, InvalidCreditCardException, InvalidDiscountRateException, InvalidCustomerCardException, InvalidCustomerIdException {
        EZShop shop = new EZShop();
        shop.login("cashier", "cashier");
        Integer saleId = shop.startSaleTransaction(); //#STEP1
        assertTrue(shop.addProductToSale(saleId, "12345678901231", 3)); // 3 * 19.90 = 59.70 #STEP2-3 (PRE-CONDITION)
        shop.logout(); //this is not done in the scenarios but is only to verify specific step which requires high privileges to be executed
        shop.login("admin", "admin");
        assertEquals((int) shop.getProductTypeByBarCode("12345678901231").getQuantity(), 27); //#STEP 4
        shop.logout();
        shop.login("cashier", "cashier");
        assertTrue(shop.endSaleTransaction(saleId)); //Close the transaction #STEP5
        assertEquals(shop.getSaleTransaction(saleId).getPrice(), 59.70, 0.01);
        //System ask for the payment type (CASH is selected) #STEP6
        //Cashier ask the loyalty card #STEP7 -> AABB2345
        assertEquals(shop.receiveCashPayment(saleId, 70.00), 10.30, 0.01); //#STEP 8-9
        assertTrue(shop.modifyPointsOnCard("1234567890", shop.computePointsForSale(saleId))); //#STEP10 -> int_part(59.79/10) = 5
        //Print receipt #STEP 11
        //POST-CONDITION
        shop.logout(); //this is not done in the scenarios but is only to verify specific step which requires high privileges to be executed
        shop.login("admin", "admin");
        assertEquals((int) shop.getCustomer(1).getPoints(), 5);
        assertEquals(shop.computeBalance(), shop.getSaleTransaction(saleId).getPrice(), 0.01); //#STEP 12
    }

    @Test
    public void scenario6_4BTest() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidQuantityException, InvalidTransactionIdException, InvalidProductCodeException, InvalidPaymentException, InvalidCreditCardException, InvalidDiscountRateException, InvalidCustomerCardException, InvalidCustomerIdException {
        EZShop shop = new EZShop();
        shop.login("cashier", "cashier");
        Integer saleId = shop.startSaleTransaction(); //#STEP1
        assertTrue(shop.addProductToSale(saleId, "12345678901231", 3)); // 3 * 19.90 = 59.70 #STEP2-3 (PRE-CONDITION)
        shop.logout(); //this is not done in the scenarios but is only to verify specific step which requires high privileges to be executed
        shop.login("admin", "admin");
        assertEquals((int) shop.getProductTypeByBarCode("12345678901231").getQuantity(), 27); //#STEP 4
        shop.logout();
        shop.login("cashier", "cashier");
        assertTrue(shop.endSaleTransaction(saleId)); //Close the transaction #STEP5
        assertEquals(shop.getSaleTransaction(saleId).getPrice(), 59.70, 0.01);
        //System ask for the payment type (CASH is selected) #STEP6
        //Cashier ask the loyalty card #STEP7 -> AABB2345
        assertTrue(shop.receiveCreditCardPayment(saleId, "4485370086510891"));//#STEP 8-9
        assertTrue(shop.modifyPointsOnCard("1234567890", shop.computePointsForSale(saleId))); //#STEP10 -> int_part(59.79/10) = 5
        //Print receipt #STEP 11
        //POST-CONDITION
        shop.logout(); //this is not done in the scenarios but is only to verify specific step which requires high privileges to be executed
        shop.login("admin", "admin");
        assertEquals((int) shop.getCustomer(1).getPoints(), 5); //Verify #STEP 10
        assertEquals(shop.computeBalance(), shop.getSaleTransaction(saleId).getPrice(), 0.01); //#STEP 12
    }

    @Test
    public void scenario6_5Test() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidQuantityException, InvalidTransactionIdException, InvalidProductCodeException, InvalidPaymentException, InvalidCreditCardException, InvalidDiscountRateException, InvalidCustomerCardException, InvalidCustomerIdException, InvalidRoleException {
        EZShop shop = new EZShop();
        shop.login("cashier", "cashier");
        Integer saleId = shop.startSaleTransaction(); //#STEP1
        assertTrue(shop.addProductToSale(saleId, "12345678901231", 3)); // 3 * 19.90 = 59.70 #STEP2-3 (PRE-CONDITION)
        assertTrue(shop.logout()); //this is not done in the scenarios but is only to verify specific step which requires high privileges to be executed
        shop.createUser("admin", "admin", "Administrator");
        shop.login("admin", "admin");
        assertEquals((int) shop.getProductTypeByBarCode("12345678901231").getQuantity(), 27); //#STEP 4
        shop.logout();
        shop.login("cashier", "cashier");
        assertTrue(shop.endSaleTransaction(saleId)); //Close the transaction #STEP5
        assertEquals(shop.getSaleTransaction(saleId).getPrice(), 59.70, 0.01);
        //System ask for the payment type (CASH is selected) #STEP6
        //Customer cancels the payment #STEP7
        assertTrue(shop.deleteSaleTransaction(saleId)); //#STEP8
        //POST-CONDITION
        shop.logout(); //this is not done in the scenarios but is only to verify specific step which requires high privileges to be executed
        shop.login("admin", "admin");
        assertEquals(shop.computeBalance(), 0.00, 0.01); //verify step 8
    }

    @Test
    public void scenario6_6Test() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidQuantityException, InvalidTransactionIdException, InvalidProductCodeException, InvalidPaymentException, InvalidCreditCardException, InvalidDiscountRateException, InvalidCustomerCardException, InvalidCustomerIdException {
        scenario6_1ATest();
        //IT IS THE SAME!
    }
}
