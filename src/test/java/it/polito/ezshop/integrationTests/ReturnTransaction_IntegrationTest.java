package it.polito.ezshop.integrationTests;

import it.polito.ezshop.data.*;
import org.junit.Test;
import java.util.HashMap;
import static org.junit.Assert.*;

public class ReturnTransaction_IntegrationTest {

    @Test
    public void testGetSaleTransaction(){
        SaleTransactionImpl sale = new SaleTransactionImpl(0.00, "PAYED");
        ReturnTransaction ret = new ReturnTransaction("OPEN", sale);
        assertEquals(ret.getSaleTransaction(), sale);
    }

    @Test
    public void testGetSetReturnedCart(){
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
        SaleTransactionImpl sale = new SaleTransactionImpl(0.0,"PAYED");
        ReturnTransaction ret = new ReturnTransaction("OPEN", sale);
        ret.setReturnedCart(cart1);
        assertEquals(ret.getReturnedCart(), cart1);
    }

    @Test
    public void testGetSetStatus(){
        SaleTransactionImpl sale = new SaleTransactionImpl(0.00, "PAYED");
        ReturnTransaction ret = new ReturnTransaction("OPEN", sale);
        assertEquals("OPEN",ret.getStatus());
        ret.setStatus("PAYED");
        assertEquals("PAYED", ret.getStatus());
    }


    @Test
    public void testComputeReturnValue(){
        ProductTypeImpl prod1 = new ProductTypeImpl("prod1","12345",19.90, "Testing product");
        ProductTypeImpl prod2 = new ProductTypeImpl("prod2","12346",29.90, "Testing product");
        ProductTypeImpl prod3 = new ProductTypeImpl("prod3","12347",39.90, "Testing product");
        CartItem cartItem1 = new CartItem(10,prod1);
        CartItem cartItem2 = new CartItem(5,prod2);
        CartItem cartItem3 = new CartItem(3,prod3);
        HashMap<String,CartItem> cart1 = new HashMap <String, CartItem>();
        cart1.put(cartItem1.getBarCode(), cartItem1);
        cart1.put(cartItem2.getBarCode(), cartItem2);
        SaleTransactionImpl sale = new SaleTransactionImpl(0.0,"PAYED");
        sale.setCart(cart1);
        ReturnTransaction ret = new ReturnTransaction("OPEN", sale);
        ret.setReturnedCart(cart1);
        ret.computeReturnValue();
        // 19.90 * (1 - 0.00) * 10 = 199.00 || 19.90 * (1 - 0.20) * 10 = 159.20
        // 29.90 * (1 - 0.00) * 5 = 149.50
        // tot = 199.00 + 149.5 = 348.50 || tot = 159.20 + 149.5 = 308.70  ||  tot = (1-0.20) * 308.70 = 246.96
        assertEquals((-1)*ret.getMoney(), 348.50,0.001);
        sale.getCart().get("12345").setProductDiscount(0.20);
        ret.setReturnedCart(sale.getCart());
        ret.computeReturnValue();
        assertEquals((-1)*ret.getMoney(), 308.70,0.001);
        sale.setDiscountRate(0.20);
        ret.computeReturnValue();
        assertEquals((-1)*ret.getMoney(), 246.96,0.001);

    }

    @Test
    public void testSetGetPayment(){
        //NOTE: function will always return the extended class type (Payment) use specific method in order to get Specific payment (or use downcast)
        Payment payment = new Payment(200.00);
        CreditCard CC = new CreditCard("123455", 4000.00);
        CreditCardPayment CCpayment = new CreditCardPayment(200.00, CC);
        CashPayment cashPayment = new CashPayment(200.00, 30.50);
        SaleTransactionImpl sale = new SaleTransactionImpl(0.0,"OPEN");
        ReturnTransaction ret = new ReturnTransaction("PAYED", sale);
        //Check simple payment
        ret.setPayment(payment);
        assertEquals(ret.getPayment(),payment);
        //Check credit card payment
        ret.setPayment(CCpayment);
        assertEquals(ret.getPayment(), CCpayment);
        //Check Cash Payment
        ret.setPayment(cashPayment);
        assertEquals(ret.getPayment(), cashPayment);
    }

    @Test
    public void testIsPayed(){
        SaleTransactionImpl sale = new SaleTransactionImpl(0.00, "PAYED");
        ReturnTransaction ret = new ReturnTransaction("OPEN", sale);
        ret.setStatus("PAYED");
        Payment payment = new Payment(200.00);
        ret.setPayment(payment);
        assertTrue(ret.isPayed());
        ret.setStatus("CLOSED");
        assertFalse(ret.isPayed());
        ret.setPayment(null);
        assertFalse(ret.isPayed());
    }

    @Test
    public void testGetPaymentCreditCard(){
        SaleTransactionImpl sale = new SaleTransactionImpl(0.00, "PAYED");
        ReturnTransaction ret = new ReturnTransaction("OPEN", sale);
        ret.setStatus("PAYED");
        CreditCard cc = new CreditCard("1234567890",50.00);
        CreditCardPayment payment = new CreditCardPayment(30.00, cc);
        ret.setPayment(payment);
        assertEquals(payment,ret.getPaymentCreditCard());
    }

    @Test
    public void testGetPaymentCash(){
        SaleTransactionImpl sale = new SaleTransactionImpl(0.00, "PAYED");
        ReturnTransaction ret = new ReturnTransaction("OPEN", sale);
        ret.setStatus("PAYED");
        CashPayment payment = new CashPayment(30.00,0.00);
        ret.setPayment(payment);
        assertEquals(payment, ret.getPaymentCash());
    }

    @Test
    public void testIsCardPayment(){
        SaleTransactionImpl sale = new SaleTransactionImpl(0.00, "PAYED");
        ReturnTransaction ret = new ReturnTransaction("OPEN", sale);
        ret.setStatus("PAYED");
        Payment payment = new Payment(200.00);
        CreditCard CC = new CreditCard("123455", 4000.00);
        CreditCardPayment CCpayment = new CreditCardPayment(200.00, CC);
        CashPayment cashPayment = new CashPayment(200.00, 30.50);
        assertNull(ret.isCardPayment());
        ret.setPayment(payment);
        assertFalse(ret.isCardPayment());
        ret.setPayment(CCpayment);
        assertTrue(ret.isCardPayment());
        ret.setPayment(cashPayment);
        assertFalse(ret.isCardPayment());
    }
}
