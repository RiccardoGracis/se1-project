package it.polito.ezshop.integrationTests;

import it.polito.ezshop.data.*;
import it.polito.ezshop.exceptions.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.Arrays;

import static org.junit.Assert.*;


public class SaleTransactionIntegrationTest {
    @Before
    public void initEnvironment() {
        JDBC db = new JDBC();
        SaleTransactionImpl fooSale = new SaleTransactionImpl(0.0, "CLOSED");
        fooSale.setBalanceId(10);
        SaleTransactionImpl fooSalePayed = new SaleTransactionImpl(0.0, "PAYED");
        fooSalePayed.setBalanceId(3);
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
    public void testStartSaleTransaction() throws UnauthorizedException, InvalidPasswordException, InvalidUsernameException {
        EZShop shop = new EZShop();
        shop.login("admin", "admin");
        Integer saleId = shop.startSaleTransaction();
        assertEquals((int) saleId, 11); //Take care of fooSale in initEnvironment with ID = 10
    }

    @Test
    public void testStartSaleTransaction_ExceptionHandling() throws UnauthorizedException {
        EZShop shop = new EZShop();
        Assert.assertThrows(UnauthorizedException.class, shop::startSaleTransaction);
    }

    @Test
    public void testAddProductsToSale() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidQuantityException, InvalidTransactionIdException, InvalidProductCodeException {
        EZShop shop = new EZShop();
        shop.login("admin", "admin");
        Integer saleId = shop.startSaleTransaction();
        assertTrue(shop.addProductToSale(saleId, "12345678901231", 5));
        assertTrue(shop.addProductToSale(saleId, "12345678901248", 4));
        assertTrue(shop.addProductToSale(saleId, "12345678901231", 3));
    }

    @Test
    public void testAddProductToSale_ExceptionHandling() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidQuantityException, InvalidTransactionIdException, InvalidProductCodeException {
        EZShop shop = new EZShop();
        shop.login("admin", "admin");
        Integer saleId = shop.startSaleTransaction();
        shop.logout();
        Assert.assertThrows(UnauthorizedException.class, () -> shop.addProductToSale(saleId, "12345678901231", -5));
        shop.login("admin", "admin");
        Assert.assertThrows(InvalidTransactionIdException.class, () -> shop.addProductToSale(null, "12345678901231", 5));
        Assert.assertThrows(InvalidTransactionIdException.class, () -> shop.addProductToSale(-4, "12345678901231", 5));
        Assert.assertThrows(InvalidTransactionIdException.class, () -> shop.addProductToSale(0, "12345678901231", 5));
        Assert.assertThrows(InvalidQuantityException.class, () -> shop.addProductToSale(saleId, "12345678901231", -5));
        //(zero check) Assert.assertThrows(InvalidQuantityException.class, () -> shop.addProductToSale(saleId, "12345678901231", 0));
        Assert.assertThrows(InvalidProductCodeException.class, () -> shop.addProductToSale(saleId, "0000", 5));
        Assert.assertThrows(InvalidProductCodeException.class, () -> shop.addProductToSale(saleId, "", 5));
        Assert.assertThrows(InvalidProductCodeException.class, () -> shop.addProductToSale(saleId, null, 5));
    }

    @Test
    public void testAddProductToSale_ReturnValue() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidQuantityException, InvalidTransactionIdException, InvalidProductCodeException {
        EZShop shop = new EZShop();
        shop.login("admin", "admin");
        Integer saleId = shop.startSaleTransaction();
        assertFalse(shop.addProductToSale(1, "12345678901286", 5)); //Product is not present
        assertFalse(shop.addProductToSale(5, "12345678901231", 5)); //transaction does not exist
        //transaction is closed/payed
        assertFalse(shop.addProductToSale(10, "12345678901231", 5)); //Transaction already closed
        assertFalse(shop.addProductToSale(10, "12345678901231", 51)); //products can not satisfy amount request
    }

    @Test
    public void testDeleteProductFromSale() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidQuantityException, InvalidTransactionIdException, InvalidProductCodeException {
        EZShop shop = new EZShop();
        shop.login("admin", "admin");
        Integer saleId = shop.startSaleTransaction();
        assertTrue(shop.addProductToSale(saleId, "12345678901231", 5));
        assertTrue(shop.addProductToSale(saleId, "12345678901248", 4));
        assertTrue(shop.deleteProductFromSale(saleId, "12345678901231", 3));
        assertTrue(shop.deleteProductFromSale(saleId, "12345678901248", 4));
    }

    @Test
    public void testDeleteProductFromSale_ExceptionHandling() throws UnauthorizedException, InvalidPasswordException, InvalidUsernameException, InvalidQuantityException, InvalidTransactionIdException, InvalidProductCodeException {
        EZShop shop = new EZShop();
        shop.login("admin", "admin");
        Integer saleId = shop.startSaleTransaction();
        assertTrue(shop.addProductToSale(saleId, "12345678901231", 5));
        assertTrue(shop.addProductToSale(saleId, "12345678901248", 4));
        shop.logout();
        Assert.assertThrows(UnauthorizedException.class, () -> shop.deleteProductFromSale(saleId, "12345678901231", 5));
        shop.login("admin", "admin");
        Assert.assertThrows(InvalidQuantityException.class, () -> shop.deleteProductFromSale(saleId, "12345678901231", -4));
        Assert.assertThrows(InvalidTransactionIdException.class, () -> shop.deleteProductFromSale(null, "12345678901231", 5));
        Assert.assertThrows(InvalidTransactionIdException.class, () -> shop.deleteProductFromSale(0, "12345678901231", 5));
        Assert.assertThrows(InvalidTransactionIdException.class, () -> shop.deleteProductFromSale(-4, "12345678901231", 5));
        Assert.assertThrows(InvalidProductCodeException.class, () -> shop.deleteProductFromSale(saleId, "0000", 5));
        Assert.assertThrows(InvalidProductCodeException.class, () -> shop.deleteProductFromSale(saleId, "", 5));
        Assert.assertThrows(InvalidProductCodeException.class, () -> shop.deleteProductFromSale(saleId, null, 5));
    }

    @Test
    public void testDeleteProductFromSale_ReturnValue() throws UnauthorizedException, InvalidPasswordException, InvalidUsernameException, InvalidQuantityException, InvalidTransactionIdException, InvalidProductCodeException {
        EZShop shop = new EZShop();
        shop.login("admin", "admin");
        Integer saleId = shop.startSaleTransaction();
        assertTrue(shop.addProductToSale(saleId, "12345678901231", 5));
        assertFalse(shop.deleteProductFromSale(22, "12345678901231", 2)); //Sale transaction does not exist
        assertFalse(shop.deleteProductFromSale(10, "12345678901231", 5)); //Transaction already closed
        assertFalse(shop.deleteProductFromSale(saleId, "12345678901286", 2)); //product does not exist in inventory
        assertFalse(shop.deleteProductFromSale(saleId, "12345678901248", 2)); //product is not contained in the cart
        assertFalse(shop.deleteProductFromSale(saleId, "12345678901231", 20)); //exceed the cart item quantity
    }

    @Test
    public void testApplyDiscountRateToProduct() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidQuantityException, InvalidTransactionIdException, InvalidProductCodeException, InvalidDiscountRateException {
        EZShop shop = new EZShop();
        shop.login("admin", "admin");
        Integer saleId = shop.startSaleTransaction();
        assertTrue(shop.addProductToSale(saleId, "12345678901231", 5));
        assertTrue(shop.addProductToSale(saleId, "12345678901248", 4));
        assertTrue(shop.addProductToSale(saleId, "12345678901231", 3));
        assertTrue(shop.applyDiscountRateToProduct(saleId, "12345678901231", 0.20));
        assertTrue(shop.applyDiscountRateToProduct(saleId, "12345678901248", 0.00));
    }

    @Test
    public void testApplyDiscountRateToProduct_ExceptionHandling() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidQuantityException, InvalidTransactionIdException, InvalidProductCodeException {
        EZShop shop = new EZShop();
        shop.login("admin", "admin");
        Integer saleId = shop.startSaleTransaction();
        assertTrue(shop.addProductToSale(saleId, "12345678901231", 5));
        shop.logout();
        Assert.assertThrows(UnauthorizedException.class, () -> shop.applyDiscountRateToProduct(saleId, "12345678901231", 0.20));
        shop.login("admin", "admin");
        Assert.assertThrows(InvalidTransactionIdException.class, () -> shop.applyDiscountRateToProduct(-1, "12345678901231", 0.20));
        Assert.assertThrows(InvalidTransactionIdException.class, () -> shop.applyDiscountRateToProduct(0, "12345678901231", 0.20));
        Assert.assertThrows(InvalidTransactionIdException.class, () -> shop.applyDiscountRateToProduct(null, "12345678901231", 0.20));
        Assert.assertThrows(InvalidProductCodeException.class, () -> shop.applyDiscountRateToProduct(1, "123101231", 0.20));
        Assert.assertThrows(InvalidProductCodeException.class, () -> shop.applyDiscountRateToProduct(1, "", 0.20));
        Assert.assertThrows(InvalidProductCodeException.class, () -> shop.applyDiscountRateToProduct(1, null, 0.20));
        Assert.assertThrows(InvalidDiscountRateException.class, () -> shop.applyDiscountRateToProduct(saleId, "12345678901231", 1.20));
        Assert.assertThrows(InvalidDiscountRateException.class, () -> shop.applyDiscountRateToProduct(saleId, "12345678901231", -0.20));
        Assert.assertThrows(InvalidDiscountRateException.class, () -> shop.applyDiscountRateToProduct(saleId, "12345678901231", 1.00));
    }

    @Test
    public void testApplyDiscountRateToProduct_ReturnValue() throws UnauthorizedException, InvalidPasswordException, InvalidUsernameException, InvalidQuantityException, InvalidTransactionIdException, InvalidProductCodeException, InvalidDiscountRateException {
        EZShop shop = new EZShop();
        shop.login("admin", "admin");
        Integer saleId = shop.startSaleTransaction();
        assertTrue(shop.addProductToSale(saleId, "12345678901231", 5));
        assertFalse(shop.applyDiscountRateToProduct(10, "12345678901231", 0.20));
        assertFalse(shop.applyDiscountRateToProduct(22, "12345678901231", 0.20));
        assertFalse(shop.applyDiscountRateToProduct(saleId, "12345678901286", 0.20));
        assertFalse(shop.applyDiscountRateToProduct(saleId, "12345678901248", 0.20));
    }

    @Test
    public void testApplyDiscountRateToSale() throws UnauthorizedException, InvalidPasswordException, InvalidUsernameException, InvalidQuantityException, InvalidTransactionIdException, InvalidProductCodeException, InvalidDiscountRateException {
        EZShop shop = new EZShop();
        shop.login("admin", "admin");
        Integer saleId = shop.startSaleTransaction();
        assertTrue(shop.applyDiscountRateToSale(saleId, 0.20));
        assertTrue(shop.applyDiscountRateToSale(saleId, 0.00));
        //Product addition could be avoided
        assertTrue(shop.addProductToSale(saleId, "12345678901231", 5));
        assertTrue(shop.addProductToSale(saleId, "12345678901248", 4));
        assertTrue(shop.addProductToSale(saleId, "12345678901231", 3));
        assertTrue(shop.applyDiscountRateToSale(saleId, 0.20));
        assertTrue(shop.applyDiscountRateToSale(saleId, 0.00));
    }

    @Test
    public void testApplyDiscountRateToSale_ExceptionHandling() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidQuantityException, InvalidTransactionIdException, InvalidProductCodeException {
        EZShop shop = new EZShop();
        shop.login("admin", "admin");
        Integer saleId = shop.startSaleTransaction();
        //Product addition could be avoided
        assertTrue(shop.addProductToSale(saleId, "12345678901231", 5));
        assertTrue(shop.addProductToSale(saleId, "12345678901248", 4));
        assertTrue(shop.addProductToSale(saleId, "12345678901231", 3));
        shop.logout();
        Assert.assertThrows(UnauthorizedException.class, () -> shop.applyDiscountRateToSale(saleId, 0.20));
        shop.login("admin", "admin");
        Assert.assertThrows(InvalidDiscountRateException.class, () -> shop.applyDiscountRateToSale(saleId, -0.20));
        Assert.assertThrows(InvalidDiscountRateException.class, () -> shop.applyDiscountRateToSale(saleId, 1.20));
        Assert.assertThrows(InvalidDiscountRateException.class, () -> shop.applyDiscountRateToSale(saleId, 1.00));
        Assert.assertThrows(InvalidTransactionIdException.class, () -> shop.applyDiscountRateToSale(-1, 0.20));
        Assert.assertThrows(InvalidTransactionIdException.class, () -> shop.applyDiscountRateToSale(0, 0.20));
        Assert.assertThrows(InvalidTransactionIdException.class, () -> shop.applyDiscountRateToSale(null, 0.20));
    }

    @Test
    public void testApplyDiscountRateToSale_ReturnValue() throws UnauthorizedException, InvalidPasswordException, InvalidUsernameException, InvalidQuantityException, InvalidTransactionIdException, InvalidProductCodeException {
        EZShop shop = new EZShop();
        shop.login("admin", "admin");
        Integer saleId = shop.startSaleTransaction();
        //Product addition could be avoided
        assertTrue(shop.addProductToSale(saleId, "12345678901231", 5));
        assertTrue(shop.addProductToSale(saleId, "12345678901248", 4));
        assertTrue(shop.addProductToSale(saleId, "12345678901231", 3));
        assertFalse(shop.addProductToSale(22, "12345678901231", 3));
        assertFalse(shop.addProductToSale(10, "12345678901231", 3));
    }

    @Test
    public void testComputePointForSale() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidQuantityException, InvalidTransactionIdException, InvalidProductCodeException, InvalidDiscountRateException {
        EZShop shop = new EZShop();
        shop.login("admin", "admin");
        Integer saleId = shop.startSaleTransaction();
        //Product addition could be avoided
        assertTrue(shop.addProductToSale(saleId, "12345678901231", 5)); //5*19.90 = 99.50
        assertTrue(shop.addProductToSale(saleId, "12345678901248", 4)); //4 * 29.90 = 119.60
        assertTrue(shop.addProductToSale(saleId, "12345678901231", 3)); //3 * 19.90 = 59.70
        // tot = 278.80 -> 278.80/10 = 27
        assertEquals(shop.computePointsForSale(saleId), 27);
        //Affect solution with sale discount
        // 278.80 * (1 - 0.20) = 222.4 -> 222.4/10 = 22
        assertTrue(shop.applyDiscountRateToSale(saleId, 0.20));
        assertEquals(shop.computePointsForSale(saleId), 22);
        //affect also the product discount
        assertTrue(shop.applyDiscountRateToProduct(saleId, "12345678901231", 0.20)); // 8 * (1 - 0.20) * 19.90 = 127.36
        //tot = 246.96 -> saleDiscount(0.20) = 196.57 -> 196.57/10=19
        assertEquals(shop.computePointsForSale(saleId), 19);
    }

    @Test
    public void testComputePointForSale_ExceptionHandling() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidQuantityException, InvalidTransactionIdException, InvalidProductCodeException {
        EZShop shop = new EZShop();
        shop.login("admin", "admin");
        Integer saleId = shop.startSaleTransaction();
        assertTrue(shop.addProductToSale(saleId, "12345678901231", 5));
        assertTrue(shop.addProductToSale(saleId, "12345678901248", 4));
        assertTrue(shop.addProductToSale(saleId, "12345678901231", 3));
        shop.logout();
        Assert.assertThrows(UnauthorizedException.class, () -> shop.computePointsForSale(saleId));
        shop.login("admin", "admin");
        Assert.assertThrows(InvalidTransactionIdException.class, () -> shop.computePointsForSale(-1));
        Assert.assertThrows(InvalidTransactionIdException.class, () -> shop.computePointsForSale(0));
    }

    @Test
    public void testComputePointForSale_ReturnValue() throws InvalidPasswordException, InvalidUsernameException, InvalidTransactionIdException, UnauthorizedException {
        EZShop shop = new EZShop();
        shop.login("admin", "admin");
        assertEquals(shop.computePointsForSale(3), -1);
    }

    @Test
    public void testEndSaleTransaction() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidQuantityException, InvalidTransactionIdException, InvalidProductCodeException {
        EZShop shop = new EZShop();
        shop.login("admin", "admin");
        Integer saleId = shop.startSaleTransaction();
        assertTrue(shop.addProductToSale(saleId, "12345678901231", 5));
        assertTrue(shop.addProductToSale(saleId, "12345678901248", 4));
        assertTrue(shop.addProductToSale(saleId, "12345678901231", 3));
        assertTrue(shop.endSaleTransaction(saleId));
    }

    @Test
    public void testEndSaleTransaction_ExceptionHandling() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidQuantityException, InvalidTransactionIdException, InvalidProductCodeException {
        EZShop shop = new EZShop();
        shop.login("admin", "admin");
        Integer saleId = shop.startSaleTransaction();
        assertTrue(shop.addProductToSale(saleId, "12345678901231", 5));
        assertTrue(shop.addProductToSale(saleId, "12345678901248", 4));
        assertTrue(shop.addProductToSale(saleId, "12345678901231", 3));
        shop.logout();
        Assert.assertThrows(UnauthorizedException.class, () -> shop.endSaleTransaction(saleId));
        shop.login("admin", "admin");
        Assert.assertThrows(InvalidTransactionIdException.class, () -> shop.endSaleTransaction(-1));
        Assert.assertThrows(InvalidTransactionIdException.class, () -> shop.endSaleTransaction(0));
    }

    @Test
    public void testEndSaleTransaction_ReturnValue() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidQuantityException, InvalidTransactionIdException, InvalidProductCodeException {
        EZShop shop = new EZShop();
        shop.login("admin", "admin");
        Integer saleId = shop.startSaleTransaction();
        assertTrue(shop.addProductToSale(saleId, "12345678901231", 5));
        assertTrue(shop.addProductToSale(saleId, "12345678901248", 4));
        assertTrue(shop.addProductToSale(saleId, "12345678901231", 3));
        assertFalse(shop.endSaleTransaction(3));
        assertFalse(shop.endSaleTransaction(10));
        //Problems in DB already tested in testAddSaleTransaction() in SaleTransactionDBTestBB.java
    }

    @Test
    public void testDeleteSaleTransaction() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidQuantityException, InvalidTransactionIdException, InvalidProductCodeException {
        EZShop shop = new EZShop();
        shop.login("admin", "admin");
        Integer saleId = shop.startSaleTransaction();
        assertTrue(shop.addProductToSale(saleId, "12345678901231", 5));
        assertTrue(shop.endSaleTransaction(saleId));
        assertTrue(shop.deleteSaleTransaction(saleId));
    }

    @Test
    public void testDeleteSaleTransaction_ExceptionHandling() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidQuantityException, InvalidTransactionIdException, InvalidProductCodeException {
        EZShop shop = new EZShop();
        shop.login("admin", "admin");
        Integer saleId = shop.startSaleTransaction();
        assertTrue(shop.addProductToSale(saleId, "12345678901231", 5));
        assertTrue(shop.endSaleTransaction(saleId));
        shop.logout();
        Assert.assertThrows(UnauthorizedException.class, () -> shop.deleteReturnTransaction(saleId));
        shop.login("admin", "admin");
        Assert.assertThrows(InvalidTransactionIdException.class, () -> shop.deleteReturnTransaction(-1));
        Assert.assertThrows(InvalidTransactionIdException.class, () -> shop.deleteReturnTransaction(0));
    }

    @Test
    public void testDeleteSaleTransaction_ReturnValue() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidQuantityException, InvalidTransactionIdException, InvalidProductCodeException {
        EZShop shop = new EZShop();
        shop.login("admin", "admin");
        Integer saleId = shop.startSaleTransaction();
        assertTrue(shop.addProductToSale(saleId, "12345678901231", 5));
        assertTrue(shop.endSaleTransaction(saleId));
        assertFalse(shop.deleteSaleTransaction(3));
        assertFalse(shop.deleteSaleTransaction(40));
        //Problems in DB already tested in testDeleteSaleTransactionDB() in SaleTransactionDBTestBB.java
    }

    @Test
    public void testGetSaleTransaction() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidQuantityException, InvalidTransactionIdException, InvalidProductCodeException {
        EZShop shop = new EZShop();
        shop.login("admin", "admin");
        Integer saleId = shop.startSaleTransaction();
        assertTrue(shop.addProductToSale(saleId, "12345678901231", 5));
        assertTrue(shop.endSaleTransaction(saleId));
        SaleTransaction sale = shop.getSaleTransaction(saleId);
        assertEquals(sale.getTicketNumber(), saleId);
    }

    @Test
    public void testGetSaleTransaction_ExceptionHandling() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidQuantityException, InvalidTransactionIdException, InvalidProductCodeException {
        EZShop shop = new EZShop();
        shop.login("admin", "admin");
        Integer saleId = shop.startSaleTransaction();
        shop.logout();
        Assert.assertThrows(UnauthorizedException.class, () -> shop.getSaleTransaction(saleId));
        shop.login("admin", "admin");
        Assert.assertThrows(InvalidTransactionIdException.class, () -> shop.getSaleTransaction(-1));
        Assert.assertThrows(InvalidTransactionIdException.class, () -> shop.getSaleTransaction(0));
    }

    @Test
    public void testGetSaleTransaction_ReturnValue() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidQuantityException, InvalidTransactionIdException, InvalidProductCodeException {
        EZShop shop = new EZShop();
        shop.login("admin", "admin");
        Integer saleId = shop.startSaleTransaction();
        Integer saleIdOpenTransaction = shop.startSaleTransaction();
        assertNull(shop.getSaleTransaction(5));
        assertNull(shop.getSaleTransaction(saleIdOpenTransaction));
    }
}