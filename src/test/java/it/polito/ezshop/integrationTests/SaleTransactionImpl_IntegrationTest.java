package it.polito.ezshop.integrationTests;

import it.polito.ezshop.data.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import static org.junit.Assert.*;

public class SaleTransactionImpl_IntegrationTest {

    @Test
    public void testGetSetCart(){
        ProductTypeImpl prod1 = new ProductTypeImpl("prod1","12345",19.90, "Testing product");
        ProductTypeImpl prod2 = new ProductTypeImpl("prod2","12346",29.90, "Testing product");
        ProductTypeImpl prod3 = new ProductTypeImpl("prod3","12347",39.90, "Testing product");
        CartItem cartItem1 = new CartItem(10,prod1);
        CartItem cartItem2 = new CartItem(5,prod2);
        CartItem cartItem3 = new CartItem(3,prod3);
        HashMap<String,CartItem> cart1 = new HashMap <String, CartItem>();
        cart1.put(cartItem1.getBarCode(), cartItem1);
        cart1.put(cartItem2.getBarCode(), cartItem2);
        cart1.put(cartItem3.getBarCode(), cartItem3);
        SaleTransactionImpl sale = new SaleTransactionImpl(0.0,"OPEN");
        assertTrue(sale.getCart().values().isEmpty());
        sale.setCart(cart1);
        assertEquals(sale.getCart(),cart1);
        assertEquals(sale.getCart(),cart1);
    }

    @Test
    public void testApplyProdDiscount(){
        ProductTypeImpl prod1 = new ProductTypeImpl("prod1","12345",19.90, "Testing product");
        ProductTypeImpl prod2 = new ProductTypeImpl("prod2","12346",29.90, "Testing product");
        CartItem cartItem1 = new CartItem(10,prod1);
        CartItem cartItem2 = new CartItem(5,prod2);
        HashMap<String,CartItem> cart1 = new HashMap <String, CartItem>();
        cart1.put(cartItem1.getBarCode(), cartItem1);
        cart1.put(cartItem2.getBarCode(), cartItem2);
        SaleTransactionImpl sale = new SaleTransactionImpl(0.0,"OPEN");
        sale.setCart(cart1);
        sale.applyProdDiscount(0.20, "12345");
        // 19.90 * (1 - 0.20) * 10 = 159.20
        // 29.90 * (1 - 0.00) * 5 = 149.50
        // tot = 159.2 + 149.5 = 308.70
        assertEquals(sale.getPrice(), 308.70, 0.001);
    }

    @Test
    public void testComputeTransactionTotal(){
        ProductTypeImpl prod1 = new ProductTypeImpl("prod1","12345",19.90, "Testing product");
        ProductTypeImpl prod2 = new ProductTypeImpl("prod2","12346",29.90, "Testing product");
        CartItem cartItem1 = new CartItem(10,prod1);
        CartItem cartItem2 = new CartItem(5,prod2);
        HashMap<String,CartItem> cart1 = new HashMap <String, CartItem>();
        cart1.put(cartItem1.getBarCode(), cartItem1);
        cart1.put(cartItem2.getBarCode(), cartItem2);
        SaleTransactionImpl sale = new SaleTransactionImpl(0.0,"OPEN");
        sale.setCart(cart1);
        sale.applyProdDiscount(0.20, "12345");
        sale.setDiscountRate(0.20);
        // 19.90 * (1 - 0.20) * 10 = 159.20
        // 29.90 * (1 - 0.00) * 5 = 149.50
        // Subtot = 159.2 + 149.5 = 308.70 -> tot = (1 - 0.20) * subtot = 246.96
        sale.computeTransactionTotal();
        assertEquals(sale.getPrice(), 246.96, 0.001);
    }

    @Test
    public void testSetGetPayment(){
        //NOTE: function will always return the extended class type (Payment) use specific method in order to get Specific payment (or use downcast)
        Payment payment = new Payment(200.00);
        CreditCard CC = new CreditCard("123455", 4000.00);
        CreditCardPayment CCpayment = new CreditCardPayment(200.00, CC);
        CashPayment cashPayment = new CashPayment(200.00, 30.50);
        SaleTransactionImpl sale = new SaleTransactionImpl(0.0,"PAYED");
        //Check simple payment
        sale.setPayment(payment);
        assertEquals(sale.getPayment(),payment);
        //Check credit card payment
        sale.setPayment(CCpayment);
        assertEquals(sale.getPayment(), CCpayment);
        //Check Cash Payment
        sale.setPayment(cashPayment);
        assertEquals(sale.getPayment(), cashPayment);
    }

    @Test
    public void testIsCardPayment(){
        Payment payment = new Payment(200.00);
        CreditCard CC = new CreditCard("123455", 4000.00);
        CreditCardPayment CCpayment = new CreditCardPayment(200.00, CC);
        CashPayment cashPayment = new CashPayment(200.00, 30.50);
        SaleTransactionImpl sale = new SaleTransactionImpl(0.0,"OPEN");
        assertNull(sale.isCardPayment());
        sale.setPayment(payment);
        assertFalse(sale.isCardPayment());
        sale.setPayment(CCpayment);
        assertTrue(sale.isCardPayment());
        sale.setPayment(cashPayment);
        assertFalse(sale.isCardPayment());
    }

    @Test
    public void testGetPaymentCreditCard(){
        Payment payment = new Payment(200.00);
        CreditCard CC = new CreditCard("123455", 4000.00);
        CreditCardPayment CCpayment = new CreditCardPayment(200.00, CC);
        CashPayment cashPayment = new CashPayment(200.00, 30.50);
        SaleTransactionImpl sale = new SaleTransactionImpl(0.0,"OPEN");
        //No payment is inserted (optional is empty)
        assertNull(sale.getPaymentCreditCard());
        //Payment (extended by class of CashPayment and CreditCardPayment -> wrong class)
        sale.setPayment(payment);
        assertNull(sale.getPaymentCreditCard());
        //CashPayment (wrong class)
        sale.setPayment(cashPayment);
        assertNull(sale.getPaymentCreditCard());
        //CreditCard Payment (correct class)
        sale.setPayment(CCpayment);
        assertEquals(sale.getPaymentCreditCard(), CCpayment);
    }

    @Test
    public void testGetPaymentCash(){
        Payment payment = new Payment(200.00);
        CreditCard CC = new CreditCard("123455", 4000.00);
        CreditCardPayment CCpayment = new CreditCardPayment(200.00, CC);
        CashPayment cashPayment = new CashPayment(200.00, 30.50);
        SaleTransactionImpl sale = new SaleTransactionImpl(0.0,"OPEN");
        //No payment is inserted (optional is empty)
        assertNull(sale.getPaymentCreditCard());
        //Payment (extended by class of CashPayment and CreditCardPayment -> wrong class)
        sale.setPayment(payment);
        assertNull(sale.getPaymentCash());
        //CashPayment (wrong class)
        sale.setPayment(CCpayment);
        assertNull(sale.getPaymentCash());
        //CreditCard Payment (correct class)
        sale.setPayment(cashPayment);
        assertEquals(sale.getPaymentCash(), cashPayment);
    }

    @Test
    public void testSetGetEntries(){
        ProductTypeImpl prod1 = new ProductTypeImpl("prod1","12345",19.90, "Testing product");
        ProductTypeImpl prod2 = new ProductTypeImpl("prod2","12346",29.90, "Testing product");
        ProductTypeImpl prod3 = new ProductTypeImpl("prod3","12347",39.90, "Testing product");
        CartItem cartItem1 = new CartItem(10,prod1);
        CartItem cartItem2 = new CartItem(5,prod2);
        CartItem cartItem3 = new CartItem(3,prod3);
        HashMap<String,CartItem> cart1 = new HashMap <String, CartItem>();
        cart1.put(cartItem1.getBarCode(), cartItem1);
        cart1.put(cartItem2.getBarCode(), cartItem2);
        cart1.put(cartItem3.getBarCode(), cartItem3);
        SaleTransactionImpl sale = new SaleTransactionImpl(0.00,"OPEN");
        sale.setCart(cart1);
        assertEquals(sale.getEntries(), new ArrayList<>(cart1.values()));
        sale.setEntries(sale.getEntries());
        assertEquals(sale.getCart(),cart1);
    }


}
