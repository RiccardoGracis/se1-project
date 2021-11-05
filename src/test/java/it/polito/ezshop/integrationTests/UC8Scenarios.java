package it.polito.ezshop.integrationTests;

import static org.junit.Assert.*;
import org.junit.*;
import it.polito.ezshop.data.*;
import it.polito.ezshop.exceptions.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;

public class UC8Scenarios {
    @Before
    public void initEnvironment() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, SQLException, IOException {
        JDBC db = new JDBC();
        //create
        db.deleteEntireDB();
        db.initDB(null,null,null,null);
        db.openConnection();
        // create tables in the DB
        db.createCustomerTable();
        db.createLoyaltyCardTable();
        db.createUserTable();
        db.createBalanceTable();
        db.createProductTypeTable();
        //Load Product in to the system
        db.insertProductType(1, "11-AA-11","12345678901231","Product1",19.90,30,0.00,"testing product, please remove if present" );
        db.insertProductType(2, "11-BB-11","12345678901248","Product2",29.90,30,0.20,"testing product, please remove if present" );
        db.insertProductType(3, "11-CC-11","12345678901255","Product3",39.90,30,0.00,"testing product, please remove if present" );
        db.insertProductType(4, "11-DD-11","12345678901262","Product4",49.90,30,0.00,"testing product, please remove if present" );
        db.insertCustomer(1,"customer");
        db.insertCustomerCard("1234567890", 0, true);
        db.attachCustomerCard(1,"1234567890");
        db.insertUser(1,"admin","admin","Administrator");
        db.insertUser(2,"cashier","cashier","Cashier");
        db.closeConnection();
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
    public void scenario8_1() throws InvalidPasswordException, InvalidUsernameException, InvalidTransactionIdException, UnauthorizedException, InvalidQuantityException, InvalidProductCodeException, InvalidCreditCardException, SQLException {
        JDBC db = new JDBC();
        SaleTransactionImpl sale = new SaleTransactionImpl(0.00,"PAYED");
        HashMap<String,ProductType> inv =  new HashMap<String,ProductType>();
        db.openConnection();
        inv = db.loadInventory();
        db.closeConnection();
        HashMap<String,CartItem> cartSale = new HashMap<String,CartItem>();
        CartItem cartItem1 = new CartItem(5,inv.get("12345678901231"));
        CartItem cartItem2 = new CartItem(5,inv.get("12345678901248"));
        cartSale.put(cartItem1.getBarCode(),cartItem1);
        cartSale.put(cartItem2.getBarCode(),cartItem2);
        sale.setCart(cartSale);
        sale.computeTransactionTotal();
        CashPayment p = new CashPayment(sale.getPrice(),0.00);
        sale.setPayment(p);
        db.addSaleTransactionDB(sale);
        db.saleTransactionPaymentDB(sale, p);
        //(PRE-CONDITION)
        Integer retId;
        EZShop shop = new EZShop();
        shop.login("admin","admin");
        shop.recordBalanceUpdate(100.00);
        shop.logout();
        shop.login("cashier", "cashier");
        SaleTransactionImpl tmp = (SaleTransactionImpl) shop.getSaleTransaction(sale.getBalanceId());
        assertEquals("PAYED", tmp.getStatus());
        //start scenario
        retId = shop.startReturnTransaction(sale.getBalanceId());
        assertTrue(retId != -1);  //#STEP 1-2
        assertTrue(shop.returnProduct(retId,"12345678901231", 3)); //#STEP3-4 (tot returned = 19.90*3 = 59.70)
        assertTrue(shop.endReturnTransaction(retId,true)); //#STEP7
        shop.returnCreditCardPayment(retId,"4485370086510891"); //#STEP 6 TODO: inconsistency between interface gui and scenario steps
        //POST-CONDITION
        shop.logout(); //this is not done in the scenarios but is only to verify specific step which requires high privileges to be executed
        shop.login("admin", "admin");
        assertEquals((int) shop.getProductTypeByBarCode("12345678901231").getQuantity(), 33); //#STEP 5 with verification (only in local memory)
        assertEquals(shop.computeBalance(),40.30,0.01); // (check initial balance) 100.00 - 59.70 = 40.30
        shop.reset();
    }

    @Test
    public void scenario8_2() throws InvalidPasswordException, InvalidUsernameException, InvalidTransactionIdException, UnauthorizedException, InvalidQuantityException, InvalidProductCodeException, InvalidCreditCardException, SQLException {
        JDBC db = new JDBC();
        SaleTransactionImpl sale = new SaleTransactionImpl(0.00,"PAYED");
        HashMap<String,ProductType> inv =  new HashMap<String,ProductType>();
        db.openConnection();
        inv = db.loadInventory();
        db.closeConnection();
        HashMap<String,CartItem> cartSale = new HashMap<String,CartItem>();
        CartItem cartItem1 = new CartItem(5,inv.get("12345678901231"));
        CartItem cartItem2 = new CartItem(5,inv.get("12345678901248"));
        cartSale.put(cartItem1.getBarCode(),cartItem1);
        cartSale.put(cartItem2.getBarCode(),cartItem2);
        sale.setCart(cartSale);
        sale.computeTransactionTotal();
        CashPayment p = new CashPayment(sale.getPrice(),0.00);
        sale.setPayment(p);
        db.addSaleTransactionDB(sale);
        db.saleTransactionPaymentDB(sale, p);
        //(PRE-CONDITION)
        Integer retId;
        EZShop shop = new EZShop();
        shop.login("admin","admin");
        shop.recordBalanceUpdate(100.00);
        shop.logout();
        shop.login("cashier", "cashier");
        SaleTransactionImpl tmp = (SaleTransactionImpl) shop.getSaleTransaction(sale.getBalanceId());
        assertEquals("PAYED", tmp.getStatus());
        //start scenario
        retId = shop.startReturnTransaction(sale.getBalanceId());
        assertTrue(retId != -1);  //#STEP 1-2
        assertTrue(shop.returnProduct(retId,"12345678901231", 3)); //#STEP3-4 (tot returned = 19.90*3 = 59.70)
        assertTrue(shop.endReturnTransaction(retId,true)); //#STEP7
        shop.returnCashPayment(retId); //#STEP 6 TODO: inconsistency between interface gui and scenario steps
        //POST-CONDITION
        shop.logout(); //this is not done in the scenarios but is only to verify specific step which requires high privileges to be executed
        shop.login("admin", "admin");
        assertEquals((int) shop.getProductTypeByBarCode("12345678901231").getQuantity(), 33); //#STEP 5 with verification (only in local memory)
        assertEquals(shop.computeBalance(),40.30,0.01); // (check initial balance) 100.00 - 59.70 = 40.30
        shop.reset();
    }
}
