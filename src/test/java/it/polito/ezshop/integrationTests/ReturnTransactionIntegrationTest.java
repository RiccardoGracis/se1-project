package it.polito.ezshop.integrationTests;

import it.polito.ezshop.data.*;
import it.polito.ezshop.exceptions.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.junit.Assert.fail;

public class ReturnTransactionIntegrationTest {

    @Before
    public void initEnvironment() {
        JDBC db = new JDBC();
        SaleTransactionImpl fooSale = new SaleTransactionImpl(0.0, "CLOSED");
        fooSale.setBalanceId(10);
        ReturnTransaction fooReturn = new ReturnTransaction("CLOSED", fooSale);
        fooReturn.setBalanceId(9);
        //Delete pre-existent DB
        try {
            db.deleteEntireDB();
        } catch (SQLException e) {
            fail(e.getErrorCode() + "\n\t" + Arrays.toString(e.getStackTrace()));
        }
        //Create DB tables
        try {
            db.initDB(null, null, null, null);
            db.openConnection();
            db.createUserTable();
            db.createProductTypeTable();
            db.closeConnection();
        } catch (SQLException e) {
            fail(e.getErrorCode() + "\n\t" + Arrays.toString(e.getStackTrace()));
        }
        //Add environment parameters
        try {
            db.addReturnTransactionDB(fooReturn);
            db.addSaleTransactionDB(fooSale);
            db.openConnection();
            db.insertUser(1, "admin", "admin", "Administrator"); //add user
            db.insertProductType(1, "11-AA-11", "12345678901231", "foo1", 19.90, 50, 0.00, "Testing product: remove from db if present!"); //add some products
            db.insertProductType(2, "11-BB-11", "12345678901248", "foo2", 29.90, 50, 0.00, "Testing product: remove from db if present!");
            db.insertProductType(3, "11-CC-11", "12345678901255", "foo3", 39.00, 50, 0.20, "Testing product: remove from db if present!");
            db.loadUsers();
            db.loadInventory();
            db.closeConnection();
        } catch (SQLException e) {
            fail(e.getErrorCode() + "\n\t" + Arrays.toString(e.getStackTrace()));
        }
        //Add some transaction
        EZShop tmpShop = new EZShop();
        try {
            tmpShop.login("admin", "admin");
            Integer saleId_1 = tmpShop.startSaleTransaction(); //saleId = 11 (!)
            tmpShop.addProductToSale(saleId_1, "12345678901231", 5);
            tmpShop.addProductToSale(saleId_1, "12345678901248", 3);
            tmpShop.endSaleTransaction(saleId_1);
            tmpShop.receiveCashPayment(saleId_1, 200.00);
            Integer saleId_2 = tmpShop.startSaleTransaction(); //saleId = 12 (!)
            tmpShop.addProductToSale(saleId_2, "12345678901231", 2);
            tmpShop.addProductToSale(saleId_2, "12345678901248", 2);
            tmpShop.addProductToSale(saleId_2, "12345678901255", 3);
            tmpShop.endSaleTransaction(saleId_2);
            tmpShop.receiveCreditCardPayment(saleId_2, "4485370086510891");
        } catch (Exception e) {
            fail(e.getMessage() + "\n\t" + Arrays.toString(e.getStackTrace()));
        }
    }

    @After
    public void resetEnvironment() {
        JDBC db = new JDBC();
        try {
            db.deleteEntireDB();
        } catch (SQLException e) {
            fail(e.getErrorCode() + "\n\t" + Arrays.toString(e.getStackTrace()));
        }
    }

    @Test
    public void testStartReturnTransaction() throws InvalidPasswordException, InvalidUsernameException, InvalidTransactionIdException, UnauthorizedException {
        EZShop shop = new EZShop();
        shop.login("admin", "admin");
        Integer returnId = shop.startReturnTransaction(11); //11 is the returnId based on definition
        assertEquals((int) returnId, 13);
    }

    @Test
    public void testStartReturnTransaction_ExceptionHandling() throws InvalidPasswordException, InvalidUsernameException, InvalidTransactionIdException, UnauthorizedException {
        EZShop shop = new EZShop();
        assertThrows(UnauthorizedException.class, () -> shop.startReturnTransaction(11));
        shop.login("admin", "admin");
        assertThrows(InvalidTransactionIdException.class, () -> shop.startReturnTransaction(-1));
        assertThrows(InvalidTransactionIdException.class, () -> shop.startReturnTransaction(0));
    }

    @Test
    public void testStartReturnTransaction_ReturnValue() throws InvalidPasswordException, InvalidUsernameException, InvalidTransactionIdException, UnauthorizedException {
        EZShop shop = new EZShop();
        shop.login("admin", "admin");
        assertEquals((int) shop.startReturnTransaction(20), -1);
        assertEquals((int) shop.startReturnTransaction(10), -1);
    }

    @Test
    public void testReturnProduct() throws InvalidTransactionIdException, UnauthorizedException, InvalidPasswordException, InvalidUsernameException, InvalidQuantityException, InvalidProductCodeException {
        EZShop shop = new EZShop();
        shop.login("admin", "admin");
        Integer returnId = shop.startReturnTransaction(11); //11 is the returnId based on definition
        assertTrue(shop.returnProduct(returnId, "12345678901231", 2));
    }

    @Test
    public void testReturnProduct_ExceptionHandling() throws InvalidTransactionIdException, UnauthorizedException, InvalidPasswordException, InvalidUsernameException, InvalidQuantityException, InvalidProductCodeException {
        EZShop shop = new EZShop();
        shop.login("admin", "admin");
        Integer returnId = shop.startReturnTransaction(11); //11 is the returnId based on definition
        shop.logout();
        assertThrows(UnauthorizedException.class, () -> shop.returnProduct(returnId, "12345678901231", 2));
        shop.login("admin", "admin");
        assertThrows(InvalidProductCodeException.class, () -> shop.returnProduct(returnId, "", 2));
        assertThrows(InvalidProductCodeException.class, () -> shop.returnProduct(returnId, "1351346245", 2));
        assertThrows(InvalidProductCodeException.class, () -> shop.returnProduct(returnId, null, 2));
        assertThrows(InvalidTransactionIdException.class, () -> shop.returnProduct(-1, "12345678901231", 2));
        assertThrows(InvalidTransactionIdException.class, () -> shop.returnProduct(0, "12345678901231", 2));
        assertThrows(InvalidTransactionIdException.class, () -> shop.returnProduct(null, "12345678901231", 2));
        assertThrows(InvalidQuantityException.class, () -> shop.returnProduct(returnId, "12345678901231", 0));
        assertThrows(InvalidQuantityException.class, () -> shop.returnProduct(returnId, "12345678901231", -1));
    }

    @Test
    public void testReturnProduct_ReturnValue() throws InvalidTransactionIdException, UnauthorizedException, InvalidPasswordException, InvalidUsernameException, InvalidQuantityException, InvalidProductCodeException {
        EZShop shop = new EZShop();
        shop.login("admin", "admin");
        Integer returnId = shop.startReturnTransaction(11);
        assertFalse(shop.returnProduct(22, "12345678901231", 2)); //returnId does not exist
        assertFalse(shop.returnProduct(returnId, "12345678901255", 2)); //ProductCode is not present in saleTransaction cart
        assertFalse(shop.returnProduct(returnId, "12345678901286", 2)); //ProductCode is not present in the inventory
        assertFalse(shop.returnProduct(returnId, "12345678901231", 6)); //returnId does not exist
    }

    @Test
    public void testEndReturnTransaction1() throws InvalidTransactionIdException, UnauthorizedException, InvalidQuantityException, InvalidProductCodeException, InvalidPasswordException, InvalidUsernameException {
        EZShop shop = new EZShop();
        shop.login("admin", "admin");
        Integer returnId = shop.startReturnTransaction(11); //11 is the returnId based on definition
        shop.returnProduct(returnId, "12345678901231", 2);
        shop.returnProduct(returnId, "12345678901231", 1);
        shop.returnProduct(returnId, "12345678901248", 1);
        assertTrue(shop.endReturnTransaction(returnId, true));
    }

    @Test
    public void testEndReturnTransaction2() throws InvalidTransactionIdException, UnauthorizedException, InvalidQuantityException, InvalidProductCodeException, InvalidPasswordException, InvalidUsernameException {
        EZShop shop = new EZShop();
        shop.login("admin", "admin");
        Integer returnId = shop.startReturnTransaction(11); //11 is the returnId based on definition
        shop.returnProduct(returnId, "12345678901231", 2);
        shop.returnProduct(returnId, "12345678901231", 3); //Remove completely the reference of an item in the sale Transaction cart
        shop.returnProduct(returnId, "12345678901248", 1);
        assertTrue(shop.endReturnTransaction(returnId, true));
    }

    @Test
    public void testEndReturnTransaction3() throws InvalidTransactionIdException, UnauthorizedException, InvalidQuantityException, InvalidProductCodeException, InvalidPasswordException, InvalidUsernameException {
        EZShop shop = new EZShop();
        shop.login("admin", "admin");
        Integer returnId = shop.startReturnTransaction(11); //11 is the returnId based on definition
        shop.returnProduct(returnId, "12345678901231", 2);
        shop.returnProduct(returnId, "12345678901231", 3); //Remove completely the reference of an item in the sale Transaction cart
        shop.returnProduct(returnId, "12345678901248", 1);
        assertTrue(shop.endReturnTransaction(returnId, false));
    }

    @Test
    public void testEndReturnTransaction4() throws InvalidTransactionIdException, UnauthorizedException, InvalidQuantityException, InvalidProductCodeException, InvalidPasswordException, InvalidUsernameException {
        EZShop shop = new EZShop();
        shop.login("admin", "admin");
        Integer returnId = shop.startReturnTransaction(11); //11 is the returnId based on definition
        shop.returnProduct(returnId, "12345678901231", 2);
        shop.returnProduct(returnId, "12345678901231", 1);
        shop.returnProduct(returnId, "12345678901248", 1);
        assertTrue(shop.endReturnTransaction(returnId, false));
    }

    @Test
    public void testEndReturnTransaction_ExceptionHandling() throws InvalidPasswordException, InvalidUsernameException, InvalidTransactionIdException, UnauthorizedException, InvalidQuantityException, InvalidProductCodeException {
        EZShop shop = new EZShop();
        shop.login("admin", "admin");
        Integer returnId = shop.startReturnTransaction(11); //11 is the returnId based on definition
        shop.returnProduct(returnId, "12345678901231", 2);
        shop.returnProduct(returnId, "12345678901231", 1);
        shop.returnProduct(returnId, "12345678901248", 1);
        shop.logout();
        assertThrows(UnauthorizedException.class, () -> shop.endReturnTransaction(returnId, true));
        assertThrows(UnauthorizedException.class, () -> shop.endReturnTransaction(returnId, false));
        shop.login("admin", "admin");
        assertThrows(InvalidTransactionIdException.class, () -> shop.endReturnTransaction(-1, true));
        assertThrows(InvalidTransactionIdException.class, () -> shop.endReturnTransaction(0, false));
    }

    @Test
    public void testEndReturnTransaction_ReturnValue() throws InvalidPasswordException, InvalidUsernameException, InvalidTransactionIdException, UnauthorizedException, InvalidQuantityException, InvalidProductCodeException {
        EZShop shop = new EZShop();
        shop.login("admin", "admin");
        Integer returnId = shop.startReturnTransaction(11); //11 is the returnId based on definition
        shop.returnProduct(returnId, "12345678901231", 2);
        shop.returnProduct(returnId, "12345678901231", 1);
        shop.returnProduct(returnId, "12345678901248", 1);
        assertFalse(shop.endReturnTransaction(22, true));
        assertFalse(shop.endReturnTransaction(22, false));
        assertFalse(shop.endReturnTransaction(22, true));
        assertFalse(shop.endReturnTransaction(9, true)); //Return transaction id = 9 is already CLOSED
        assertFalse(shop.endReturnTransaction(9, false));
    }

    @Test
    public void testDeleteReturnTransaction() throws InvalidPasswordException, InvalidUsernameException, InvalidTransactionIdException, UnauthorizedException, InvalidQuantityException, InvalidProductCodeException {
        EZShop shop = new EZShop();
        shop.login("admin", "admin");
        Integer returnId = shop.startReturnTransaction(11); //11 is the returnId based on definition
        shop.returnProduct(returnId, "12345678901231", 2);
        shop.returnProduct(returnId, "12345678901231", 1);
        shop.returnProduct(returnId, "12345678901248", 1);
        shop.endReturnTransaction(returnId, true);
        assertTrue(shop.deleteReturnTransaction(returnId));
    }

    @Test
    public void testDeleteReturnTransaction_ExceptionHandling() throws InvalidPasswordException, InvalidUsernameException, InvalidTransactionIdException, UnauthorizedException, InvalidQuantityException, InvalidProductCodeException {
        EZShop shop = new EZShop();
        shop.login("admin", "admin");
        Integer returnId = shop.startReturnTransaction(11); //11 is the returnId based on definition
        shop.returnProduct(returnId, "12345678901231", 2);
        shop.returnProduct(returnId, "12345678901231", 1);
        shop.returnProduct(returnId, "12345678901248", 1);
        shop.endReturnTransaction(returnId, true);
        shop.logout();
        assertThrows(UnauthorizedException.class, () -> shop.deleteReturnTransaction(returnId));
        shop.login("admin", "admin");
        assertThrows(InvalidTransactionIdException.class, () -> shop.deleteReturnTransaction(-1));
        assertThrows(InvalidTransactionIdException.class, () -> shop.deleteReturnTransaction(0));
    }

    @Test
    public void testDeleteReturnTransaction_ReturnValue() throws InvalidPasswordException, InvalidUsernameException, InvalidTransactionIdException, UnauthorizedException, InvalidQuantityException, InvalidProductCodeException {
        EZShop shop = new EZShop();
        shop.login("admin", "admin");
        Integer returnId = shop.startReturnTransaction(11); //11 is the returnId based on definition
        shop.returnProduct(returnId, "12345678901231", 2);
        shop.returnProduct(returnId, "12345678901231", 1);
        shop.returnProduct(returnId, "12345678901248", 1);
        shop.endReturnTransaction(returnId, true);
        assertFalse(shop.deleteReturnTransaction(30));
        shop.returnCashPayment(returnId);
        assertFalse(shop.deleteReturnTransaction(returnId));
    }
}