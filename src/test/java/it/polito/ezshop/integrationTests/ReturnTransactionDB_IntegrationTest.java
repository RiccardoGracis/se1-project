package it.polito.ezshop.integrationTests;

import it.polito.ezshop.data.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import static org.junit.Assert.*;

public class ReturnTransactionDB_IntegrationTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void initDbEnvironment(){
        JDBC db=new JDBC();
        try{
            db.deleteEntireDB();
            db.initDB(null,null,null,null);
        }
        catch (SQLException e){
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
    public void testAddReturnTransactionDB() throws SQLException {
        JDBC db = new JDBC();
        SaleTransactionImpl sale = new SaleTransactionImpl(0.00, "CLOSED");
        ProductTypeImpl prod = new ProductTypeImpl("prod1", "12345", 10.00, "Testing product");
        CartItem cartItem = new CartItem(10,prod);
        ReturnTransaction ret = new ReturnTransaction("OPEN", sale);
        HashMap<String, CartItem> saleCart = new HashMap<>();
        sale.setCart(saleCart);
        HashMap<String, CartItem> retCart = new HashMap<>();
        retCart.put(cartItem.getBarCode(),cartItem);
        retCart.get(cartItem.getBarCode()).decreaseQuantity(5);
        db.addReturnTransactionDB(ret);
    }

    @Test  //Passing a null returnTransaction
    public void testAddReturnTransactionDB_ErrorHandling1() throws SQLException {
        JDBC db = new JDBC();
        SaleTransactionImpl sale = new SaleTransactionImpl(0.00, "CLOSED");
        ProductTypeImpl prod = new ProductTypeImpl("prod1", "12345", 10.00, "Testing product");
        CartItem cartItem = new CartItem(10,prod);
        ReturnTransaction ret = new ReturnTransaction("OPEN", sale);
        expectedException.expect(NullPointerException.class);
        db.addReturnTransactionDB(null);
    }

    @Test //Adding redundant returnTransaction
    public void testAddReturnTransactionDB_ErrorHandling2() throws SQLException {
        JDBC db = new JDBC();
        SaleTransactionImpl sale = new SaleTransactionImpl(0.00, "CLOSED");
        ProductTypeImpl prod = new ProductTypeImpl("prod1", "12345", 10.00, "Testing product");
        ReturnTransaction ret = new ReturnTransaction("OPEN", sale);
        db.addReturnTransactionDB(ret);
        assertThrows(SQLException.class, () -> db.addReturnTransactionDB(ret));
    }

    @Test //returnTransaction with null returnedCart
    public void testAddReturnTransactionDB_ErrorHandling3() throws SQLException {
        JDBC db = new JDBC();
        SaleTransactionImpl sale = new SaleTransactionImpl(0.00, "CLOSED");
        ReturnTransaction ret = new ReturnTransaction("OPEN", sale);
        ret.setReturnedCart(null);
        expectedException.expect(NullPointerException.class);
        db.addReturnTransactionDB(ret);
    }

    @Test
    public void testDeleteReturnTransactionDB() throws SQLException {
        JDBC db = new JDBC();
        SaleTransactionImpl sale = new SaleTransactionImpl(0.00, "CLOSED");
        ProductTypeImpl prod = new ProductTypeImpl("prod1", "12345", 10.00, "Testing product");
        CartItem cartItem = new CartItem(10,prod);
        ReturnTransaction ret = new ReturnTransaction("OPEN", sale);
        HashMap<String, CartItem> saleCart = new HashMap<>();
        sale.setCart(saleCart);
        HashMap<String, CartItem> retCart = new HashMap<>();
        retCart.put(cartItem.getBarCode(),cartItem);
        retCart.get(cartItem.getBarCode()).decreaseQuantity(5);
        db.addReturnTransactionDB(ret);
        db.deleteReturnTransactionDB(ret, sale);
    }

    @Test //null return transaction
    public void testDeleteReturnTransactionDB_ErrorHandling1() throws SQLException {
        JDBC db = new JDBC();
        SaleTransactionImpl sale = new SaleTransactionImpl(0.00, "CLOSED");
        ProductTypeImpl prod = new ProductTypeImpl("prod1", "12345", 10.00, "Testing product");
        CartItem cartItem = new CartItem(10,prod);
        ReturnTransaction ret = new ReturnTransaction("OPEN", sale);
        HashMap<String, CartItem> saleCart = new HashMap<>();
        sale.setCart(saleCart);
        HashMap<String, CartItem> retCart = new HashMap<>();
        retCart.put(cartItem.getBarCode(),cartItem);
        retCart.get(cartItem.getBarCode()).decreaseQuantity(5);
        db.addReturnTransactionDB(ret);
        expectedException.expect(NullPointerException.class);
        db.deleteReturnTransactionDB(null, sale);
    }

    @Test //null sale transaction
    public void testDeleteReturnTransactionDB_ErrorHandling2() throws SQLException {
        JDBC db = new JDBC();
        SaleTransactionImpl sale = new SaleTransactionImpl(0.00, "CLOSED");
        ProductTypeImpl prod = new ProductTypeImpl("prod1", "12345", 10.00, "Testing product");
        CartItem cartItem = new CartItem(10,prod);
        ReturnTransaction ret = new ReturnTransaction("OPEN", sale);
        HashMap<String, CartItem> saleCart = new HashMap<>();
        sale.setCart(saleCart);
        HashMap<String, CartItem> retCart = new HashMap<>();
        retCart.put(cartItem.getBarCode(),cartItem);
        retCart.get(cartItem.getBarCode()).decreaseQuantity(5);
        db.addReturnTransactionDB(ret);
        expectedException.expect(NullPointerException.class);
        db.deleteReturnTransactionDB(ret, null);
    }

    @Test
    public void testCommitReturnTransaction() throws SQLException {
        JDBC db = new JDBC();
        SaleTransactionImpl sale = new SaleTransactionImpl(0.00, "CLOSED");
        ProductTypeImpl prod = new ProductTypeImpl("prod1", "12345", 10.00, "Testing product");
        CartItem cartItem = new CartItem(10,prod);
        HashMap<String, CartItem> saleCart = new HashMap<>();
        saleCart.put(cartItem.getBarCode(),cartItem);
        sale.setCart(saleCart);
        HashMap<String, CartItem> retCart = new HashMap<>();
        retCart.put(cartItem.getBarCode(),cartItem);
        retCart.get(cartItem.getBarCode()).decreaseQuantity(5);
        db.commitReturnTransactionDB(sale,retCart,20.00);
    }

    @Test //null saleTransaction
    public void testCommitReturnTransaction_ErrorHandling1() throws SQLException {
        JDBC db = new JDBC();
        SaleTransactionImpl sale = new SaleTransactionImpl(0.00, "CLOSED");
        ProductTypeImpl prod = new ProductTypeImpl("prod1", "12345", 10.00, "Testing product");
        CartItem cartItem = new CartItem(10,prod);
        HashMap<String, CartItem> saleCart = new HashMap<>();
        saleCart.put(cartItem.getBarCode(),cartItem);
        sale.setCart(saleCart);
        HashMap<String, CartItem> retCart = new HashMap<>();
        retCart.put(cartItem.getBarCode(),cartItem);
        retCart.get(cartItem.getBarCode()).decreaseQuantity(5);
        expectedException.expect(NullPointerException.class);
        db.commitReturnTransactionDB(null,retCart,20.00);
    }

    @Test //returnedCart is null
    public void testCommitReturnTransaction_ErrorHandling2() throws SQLException {
        JDBC db = new JDBC();
        SaleTransactionImpl sale = new SaleTransactionImpl(0.00, "CLOSED");
        ProductTypeImpl prod = new ProductTypeImpl("prod1", "12345", 10.00, "Testing product");
        CartItem cartItem = new CartItem(10,prod);
        HashMap<String, CartItem> saleCart = new HashMap<>();
        saleCart.put(cartItem.getBarCode(),cartItem);
        sale.setCart(saleCart);
        HashMap<String, CartItem> retCart = new HashMap<>();
        retCart.put(cartItem.getBarCode(),cartItem);
        retCart.get(cartItem.getBarCode()).decreaseQuantity(5);
        expectedException.expect(NullPointerException.class);
        db.commitReturnTransactionDB(sale,null,20.00);
    }

    @Test // new price is null
    public void testCommitReturnTransaction_ErrorHandling3() throws SQLException {
        JDBC db = new JDBC();
        SaleTransactionImpl sale = new SaleTransactionImpl(0.00, "CLOSED");
        ProductTypeImpl prod = new ProductTypeImpl("prod1", "12345", 10.00, "Testing product");
        CartItem cartItem = new CartItem(10,prod);
        HashMap<String, CartItem> saleCart = new HashMap<>();
        saleCart.put(cartItem.getBarCode(),cartItem);
        sale.setCart(saleCart);
        HashMap<String, CartItem> retCart = new HashMap<>();
        retCart.put(cartItem.getBarCode(),cartItem);
        retCart.get(cartItem.getBarCode()).decreaseQuantity(5);
        expectedException.expect(NullPointerException.class);
        db.commitReturnTransactionDB(sale,retCart,null);

    }

    @Test //cart does not contain the product
    public void testCommitReturnTransaction_ErrorHandling4() throws SQLException {
        JDBC db = new JDBC();
        SaleTransactionImpl sale = new SaleTransactionImpl(0.00, "CLOSED");
        ProductTypeImpl prod = new ProductTypeImpl("prod1", "12345", 10.00, "Testing product");
        CartItem cartItem = new CartItem(10,prod);
        HashMap<String, CartItem> saleCart = new HashMap<>();
        sale.setCart(saleCart);
        HashMap<String, CartItem> retCart = new HashMap<>();
        retCart.put(cartItem.getBarCode(),cartItem);
        expectedException.expect(NullPointerException.class);
        db.commitReturnTransactionDB(sale,retCart,20.00);
    }

    @Test //saleCart is null
    public void testCommitReturnTransaction_ErrorHandling5() throws SQLException {
        JDBC db = new JDBC();
        SaleTransactionImpl sale = new SaleTransactionImpl(0.00, "CLOSED");
        ProductTypeImpl prod = new ProductTypeImpl("prod1", "12345", 10.00, "Testing product");
        CartItem cartItem = new CartItem(10,prod);
        HashMap<String, CartItem> saleCart = new HashMap<>();
        saleCart.put(cartItem.getBarCode(),cartItem);
        sale.setCart(null);
        HashMap<String, CartItem> retCart = new HashMap<>();
        retCart.put(cartItem.getBarCode(),cartItem);
        retCart.get(cartItem.getBarCode()).decreaseQuantity(5);
        expectedException.expect(NullPointerException.class);
        db.commitReturnTransactionDB(sale,retCart,20.00);
    }

    @Test //inconsistency between saleCart and returnCart
    public void testCommitReturnTransaction_ErrorHandling6() throws SQLException {
        JDBC db = new JDBC();
        SaleTransactionImpl sale = new SaleTransactionImpl(0.00, "CLOSED");
        ProductTypeImpl prod = new ProductTypeImpl("prod1", "12345", 10.00, "Testing product");
        ProductTypeImpl prod1 = new ProductTypeImpl("prod2", "00000", 10.00, "Testing product");
        CartItem cartItem = new CartItem(10,prod);
        CartItem cartItem1 = new CartItem(10,prod1);
        HashMap<String, CartItem> saleCart = new HashMap<>();
        saleCart.put(cartItem.getBarCode(),cartItem);
        sale.setCart(null);
        HashMap<String, CartItem> retCart = new HashMap<>();
        retCart.put(cartItem.getBarCode(),cartItem);
        retCart.put(cartItem1.getBarCode(),cartItem1);
        expectedException.expect(NullPointerException.class);
        db.commitReturnTransactionDB(sale,retCart,20.00);
    }

    @Test
    public void testReturnTransactionPaymentDB() throws SQLException {
        JDBC db = new JDBC();
        SaleTransactionImpl sale = new SaleTransactionImpl(0.0,"PAYED");
        ReturnTransaction ret = new ReturnTransaction("CLOSED", sale);
        ProductTypeImpl prod = new ProductTypeImpl("prod1", "12345", 10.00, "Testing product");
        CartItem cartItem = new CartItem(10,prod);
        HashMap<String, CartItem> retCart = new HashMap<>();
        retCart.put(cartItem.getBarCode(),cartItem);
        ret.setReturnedCart(retCart);
        ret.computeReturnValue();
        CashPayment cashP = new CashPayment(ret.getMoney()+10.00,10.00);
        db.returnTransactionPaymentDB(ret, cashP);
        CreditCard creditCard = new CreditCard("1234567890", 2000.00);
        CreditCardPayment creditCardPayment = new CreditCardPayment(sale.getPrice(),creditCard);
        db.returnTransactionPaymentDB(ret,creditCardPayment);
    }

}
