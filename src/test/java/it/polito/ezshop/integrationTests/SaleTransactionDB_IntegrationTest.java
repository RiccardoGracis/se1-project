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

public class SaleTransactionDB_IntegrationTest {
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
    public void testSaleTransactionPaymentDB() throws SQLException {
        JDBC db = new JDBC();
        SaleTransactionImpl sale = new SaleTransactionImpl(0.00, "CLOSED");
        ProductTypeImpl prod = new ProductTypeImpl("prod1", "12345", 10.00, "Testing product");
        CartItem cartItem = new CartItem(10,prod);
        HashMap<String, CartItem> saleCart = new HashMap<>();
        saleCart.put(cartItem.getBarCode(),cartItem);
        sale.setCart(saleCart);
        sale.computeTransactionTotal();
        CreditCard creditCard = new CreditCard("1234567890", 2000.00);
        CashPayment cashPayment = new CashPayment(sale.getPrice(),0.00);
        CreditCardPayment creditCardPayment = new CreditCardPayment(sale.getPrice(),creditCard);
        //Correct Execution with different payment
        db.addSaleTransactionDB(sale);
        db.saleTransactionPaymentDB(sale, cashPayment);
        db.saleTransactionPaymentDB(sale,creditCardPayment);
    }

    @Test
    public void testSaleTransactionPaymentDB_ErrorHandling1() throws SQLException {
        JDBC db = new JDBC();
        SaleTransactionImpl sale = new SaleTransactionImpl(0.00, "CLOSED");
        ProductTypeImpl prod = new ProductTypeImpl("prod1", "12345", 10.00, "Testing product");
        CartItem cartItem = new CartItem(10,prod);
        HashMap<String, CartItem> saleCart = new HashMap<>();
        saleCart.put(cartItem.getBarCode(),cartItem);
        sale.setCart(saleCart);
        sale.computeTransactionTotal();
        db.addSaleTransactionDB(sale);
        expectedException.expect(NullPointerException.class);
        db.saleTransactionPaymentDB(sale, null);
    }

    @Test
    public void testSaleTransactionPaymentDB_ErrorHandling2() throws SQLException{
        JDBC db = new JDBC();
        SaleTransactionImpl sale = new SaleTransactionImpl(0.00, "CLOSED");
        ProductTypeImpl prod = new ProductTypeImpl("prod1", "12345", 10.00, "Testing product");
        CartItem cartItem = new CartItem(10,prod);
        HashMap<String, CartItem> saleCart = new HashMap<>();
        saleCart.put(cartItem.getBarCode(),cartItem);
        sale.setCart(saleCart);
        sale.computeTransactionTotal();
        CashPayment cashPayment = new CashPayment(sale.getPrice(),0.00);
        db.addSaleTransactionDB(sale);
        expectedException.expect(NullPointerException.class);
        db.saleTransactionPaymentDB(null, cashPayment);
    }

    @Test
    public void testSaleTransactionPaymentDB_ErrorHandling3() throws SQLException{
        JDBC db = new JDBC();
        SaleTransactionImpl sale = new SaleTransactionImpl(0.00, "CLOSED");
        ProductTypeImpl prod = new ProductTypeImpl("prod1", "12345", 10.00, "Testing product");
        CartItem cartItem = new CartItem(10,prod);
        HashMap<String, CartItem> saleCart = new HashMap<>();
        saleCart.put(cartItem.getBarCode(),cartItem);
        sale.setCart(saleCart);
        sale.computeTransactionTotal();
        CashPayment cashPayment = new CashPayment(sale.getPrice(),0.00);
        db.addSaleTransactionDB(sale);
        expectedException.expect(NullPointerException.class);
        db.saleTransactionPaymentDB(null, cashPayment);
    }

    @Test
    public void testSaleTransactionPaymentDB_ErrorHandling4() throws SQLException{
        JDBC db = new JDBC();
        SaleTransactionImpl sale = new SaleTransactionImpl(0.00, "CLOSED");
        ProductTypeImpl prod = new ProductTypeImpl("prod1", "12345", 10.00, "Testing product");
        CartItem cartItem = new CartItem(10,prod);
        HashMap<String, CartItem> saleCart = new HashMap<>();
        saleCart.put(cartItem.getBarCode(),cartItem);
        sale.setCart(saleCart);
        sale.computeTransactionTotal();
        CreditCard creditCard = new CreditCard("1234567890", 2000.00);
        CreditCardPayment creditCardPayment = new CreditCardPayment(sale.getPrice(),creditCard);
        db.deleteEntireDB();
        assertThrows(SQLException.class, () -> db.saleTransactionPaymentDB(sale,creditCardPayment));
    }
}
