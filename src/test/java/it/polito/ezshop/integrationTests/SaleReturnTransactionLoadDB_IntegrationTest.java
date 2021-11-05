package it.polito.ezshop.integrationTests;

import it.polito.ezshop.data.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runners.model.MultipleFailureException;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.Assert.fail;

public class SaleReturnTransactionLoadDB_IntegrationTest {

    @Before
    public void initEnvironment() throws SQLException {
        JDBC db = new JDBC();
        //3 different saleTransaction (one cash-payed, one cc-payed and one pendent)
        SaleTransactionImpl sale = new SaleTransactionImpl(0.00, "CLOSED"); //1
        SaleTransactionImpl saleCashP = new SaleTransactionImpl(0.00, "PAYED"); //2
        SaleTransactionImpl saleCreditCardP = new SaleTransactionImpl(0.00, "PAYED"); //3
        ReturnTransaction ret1 = new ReturnTransaction("PAYED", saleCashP); //4
        ReturnTransaction ret2 = new ReturnTransaction("CLOSED", saleCreditCardP); //5
        ReturnTransaction ret3 = new ReturnTransaction("PAYED", saleCreditCardP); //6
        ProductTypeImpl prod = new ProductTypeImpl("prod1", "12345", 10.00, "Testing product");
        CartItem cartItem = new CartItem(10, prod);
        HashMap<String, CartItem> saleCart = new HashMap<>();
        saleCart.put(cartItem.getBarCode(), cartItem);
        //Setting the 3 cart with recompute price
        sale.setCart(saleCart);
        saleCashP.setCart(saleCart);
        saleCreditCardP.setCart(saleCart);
        sale.computeTransactionTotal();
        saleCashP.computeTransactionTotal();
        saleCreditCardP.computeTransactionTotal();
        ret1.setReturnedCart(saleCart);
        ret2.setReturnedCart(saleCart);
        ret1.computeReturnValue();
        ret2.computeReturnValue();
        ret3.setReturnedCart(saleCart);
        ret3.computeReturnValue();
        //Different type of Payment
        CashPayment cashPayment = new CashPayment(saleCashP.getPrice() + 10.00, 10.00);
        CreditCard creditCard = new CreditCard("1234567890", 2000.00);
        CreditCardPayment creditCardPayment = new CreditCardPayment(saleCreditCardP.getPrice(), creditCard);
        try {
            db.deleteEntireDB();
            db.initDB(null, null, null, null);
            //Add 3 sales and 2 return to DB
            db.addSaleTransactionDB(sale);
            db.addSaleTransactionDB(saleCashP);
            db.addSaleTransactionDB(saleCreditCardP);
            db.addReturnTransactionDB(ret1);
            db.addReturnTransactionDB(ret2);
            db.addReturnTransactionDB(ret3);
            //Set payment if needed
            db.saleTransactionPaymentDB(saleCashP, cashPayment);
            db.saleTransactionPaymentDB(saleCreditCardP, creditCardPayment);
            db.returnTransactionPaymentDB(ret1, cashPayment);
            db.returnTransactionPaymentDB(ret3, creditCardPayment);
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
    public void testLoadSaleTransaction() throws SQLException {
        JDBC db = new JDBC();
        ProductTypeImpl prod = new ProductTypeImpl("prod1", "12345", 10.00, "Testing product");
        CreditCard creditCard = new CreditCard("1234567890", 2000.00);
        HashMap<Integer, SaleTransaction> sales = new HashMap<Integer, SaleTransaction>();
        HashMap<String, ProductType> inv = new HashMap<String, ProductType>();
        HashMap<String, CreditCard> CCs = new HashMap<String, CreditCard>();
        CCs.put(creditCard.getNumber(), creditCard);
        inv.put(prod.getBarCode(), prod);
        db.loadSaleTransactionDB(sales, inv, CCs);
        assertEquals(sales.size(), 3);
        assertEquals(sales.values().stream().map(sale -> (SaleTransactionImpl) sale).filter(s -> s.getStatus().equals("PAYED")).count(), 2);
        assertEquals(sales.values().stream().map(s -> (SaleTransactionImpl) s).filter(s -> !s.getStatus().equals("PAYED")).count(), 1);
    }

    @Test
    public void testLoadSaleTransaction_ErrorHandling1() throws SQLException {
        JDBC db = new JDBC();
        ProductTypeImpl prod = new ProductTypeImpl("prod1", "12345", 10.00, "Testing product");
        CreditCard creditCard = new CreditCard("1234567890", 2000.00);
        HashMap<Integer, SaleTransaction> sales = new HashMap<Integer, SaleTransaction>();
        HashMap<String, ProductType> inv = new HashMap<String, ProductType>();
        HashMap<String, CreditCard> CCs = new HashMap<String, CreditCard>();
        CCs.put(creditCard.getNumber(), creditCard);
        inv.put(prod.getBarCode(), prod);
        assertThrows(NullPointerException.class, () ->db.loadSaleTransactionDB(sales,null,CCs));
        db.closeConnection();
    }

     @Test
    public void testLoadSaleTransaction_ErrorHandling3() throws SQLException {
        JDBC db = new JDBC();
        ProductTypeImpl prod = new ProductTypeImpl("prod1", "12345", 10.00, "Testing product");
        CreditCard creditCard = new CreditCard("1234567890", 2000.00);
        HashMap<Integer, SaleTransaction> sales = new HashMap<Integer, SaleTransaction>();
        HashMap<String, ProductType> inv = new HashMap<String, ProductType>();
        HashMap<String, CreditCard> CCs = new HashMap<String, CreditCard>();
        CCs.put(creditCard.getNumber(), creditCard);
        inv.put(prod.getBarCode(), prod);
         assertThrows(NullPointerException.class, () ->db.loadSaleTransactionDB(null,inv,CCs));
         db.closeConnection();
    }

    @Test
    public void testLoadSaleTransaction_ErrorHandling2() throws SQLException {
        JDBC db = new JDBC();
        ProductTypeImpl prod = new ProductTypeImpl("prod1", "12345", 10.00, "Testing product");
        CreditCard creditCard = new CreditCard("1234567890", 2000.00);
        HashMap<Integer, SaleTransaction> sales = new HashMap<Integer, SaleTransaction>();
        HashMap<String, ProductType> inv = new HashMap<String, ProductType>();
        HashMap<String, CreditCard> CCs = new HashMap<String, CreditCard>();
        CCs.put(creditCard.getNumber(), creditCard);
        inv.put(prod.getBarCode(), prod);
        assertThrows(NullPointerException.class, () ->db.loadSaleTransactionDB(sales,null,CCs));
        db.closeConnection();
    }


    @Test
    public void testLoadReturnTransaction() throws SQLException {
        JDBC db = new JDBC();
        ProductTypeImpl prod = new ProductTypeImpl("prod1", "12345", 10.00, "Testing product");
        CreditCard creditCard = new CreditCard("1234567890", 2000.00);
        HashMap<Integer, SaleTransaction> sales = new HashMap<Integer, SaleTransaction>();
        HashMap<String, ProductType> inv = new HashMap<String, ProductType>();
        HashMap<String, CreditCard> CCs = new HashMap<String, CreditCard>();
        CCs.put(creditCard.getNumber(), creditCard);
        inv.put(prod.getBarCode(), prod);
        db.loadSaleTransactionDB(sales, inv, CCs);
        HashMap<Integer, ReturnTransaction> returns = new HashMap<Integer, ReturnTransaction>();
        db.loadReturnTransaction(returns,inv,sales,CCs);
        assertEquals(returns.values().stream().filter(ret -> ret.getStatus().equals("PAYED")).count(), 2);
        assertEquals(returns.values().stream().filter(ret -> ret.getStatus().equals("CLOSED")).count(), 1);
    }

    @Test
    public void testLoadReturnTransaction_HandlingError1() throws SQLException {
        JDBC db = new JDBC();
        ProductTypeImpl prod = new ProductTypeImpl("prod1", "12345", 10.00, "Testing product");
        CreditCard creditCard = new CreditCard("1234567890", 2000.00);
        HashMap<Integer, SaleTransaction> sales = new HashMap<Integer, SaleTransaction>();
        HashMap<String, ProductType> inv = new HashMap<String, ProductType>();
        HashMap<String, CreditCard> CCs = new HashMap<String, CreditCard>();
        CCs.put(creditCard.getNumber(), creditCard);
        inv.put(prod.getBarCode(), prod);
        db.loadSaleTransactionDB(sales, inv, CCs);
        HashMap<Integer, ReturnTransaction> returns = new HashMap<Integer, ReturnTransaction>();
        assertThrows(NullPointerException.class, () ->db.loadReturnTransaction(null,inv,sales,CCs));
        db.closeConnection();
    }

    @Test
    public void testLoadReturnTransaction_HandlingError2() throws SQLException {
        JDBC db = new JDBC();
        ProductTypeImpl prod = new ProductTypeImpl("prod1", "12345", 10.00, "Testing product");
        CreditCard creditCard = new CreditCard("1234567890", 2000.00);
        HashMap<Integer, SaleTransaction> sales = new HashMap<Integer, SaleTransaction>();
        HashMap<String, ProductType> inv = new HashMap<String, ProductType>();
        HashMap<String, CreditCard> CCs = new HashMap<String, CreditCard>();
        CCs.put(creditCard.getNumber(), creditCard);
        inv.put(prod.getBarCode(), prod);
        db.loadSaleTransactionDB(sales, inv, CCs);
        HashMap<Integer, ReturnTransaction> returns = new HashMap<Integer, ReturnTransaction>();
        assertThrows(NullPointerException.class, () ->db.loadReturnTransaction(returns,null,sales,CCs));
        db.closeConnection();
    }

    @Test
    public void testLoadReturnTransaction_HandlingError3() throws SQLException {
        JDBC db = new JDBC();
        ProductTypeImpl prod = new ProductTypeImpl("prod1", "12345", 10.00, "Testing product");
        CreditCard creditCard = new CreditCard("1234567890", 2000.00);
        HashMap<Integer, SaleTransaction> sales = new HashMap<Integer, SaleTransaction>();
        HashMap<String, ProductType> inv = new HashMap<String, ProductType>();
        HashMap<String, CreditCard> CCs = new HashMap<String, CreditCard>();
        CCs.put(creditCard.getNumber(), creditCard);
        inv.put(prod.getBarCode(), prod);
        db.loadSaleTransactionDB(sales, inv, CCs);
        HashMap<Integer, ReturnTransaction> returns = new HashMap<Integer, ReturnTransaction>();
        assertThrows(NullPointerException.class, () ->db.loadReturnTransaction(returns,inv,null,CCs));
        db.closeConnection();
    }

    @Test
    public void testLoadReturnTransaction_HandlingError4() throws SQLException {
        JDBC db = new JDBC();
        ProductTypeImpl prod = new ProductTypeImpl("prod1", "12345", 10.00, "Testing product");
        CreditCard creditCard = new CreditCard("1234567890", 2000.00);
        HashMap<Integer, SaleTransaction> sales = new HashMap<Integer, SaleTransaction>();
        HashMap<String, ProductType> inv = new HashMap<String, ProductType>();
        HashMap<String, CreditCard> CCs = new HashMap<String, CreditCard>();
        CCs.put(creditCard.getNumber(), creditCard);
        inv.put(prod.getBarCode(), prod);
        db.loadSaleTransactionDB(sales, inv, CCs);
        HashMap<Integer, ReturnTransaction> returns = new HashMap<Integer, ReturnTransaction>();
        assertThrows(NullPointerException.class, () ->db.loadReturnTransaction(returns,inv,sales,null));
        db.closeConnection();
    }
}
