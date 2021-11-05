package it.polito.ezshop.WBTests;

import org.junit.Test;
import it.polito.ezshop.data.*;
import java.time.LocalDate;

import static org.junit.Assert.*;

public class SaleTransactionImplTestWB {
    @Test
    public void getSetDiscountRate() {
        SaleTransactionImpl sale = new SaleTransactionImpl(0.0, "OPEN");
        sale.setDiscountRate(0.20);
        assertEquals(sale.getDiscountRate(), 0.20,0.0001);
    }

    @Test
    public void getSetPrice() {
        SaleTransactionImpl sale = new SaleTransactionImpl(0.0, "OPEN");
        sale.setPrice(120.10);
        assertEquals(sale.getPrice(),120.10,0.0001);
    }

    @Test
    public void getSetStatus() {
        SaleTransactionImpl sale = new SaleTransactionImpl(0.0, "OPEN");
        sale.setStatus("CLOSED");
        sale.setStatus("PAYED");
        assertEquals(sale.getStatus(),"PAYED");
    }

    @Test
    public void getSetTicketNumber(){
        SaleTransactionImpl sale = new SaleTransactionImpl(0.0, "OPEN");
        sale.setTicketNumber(1234);
        assertEquals((int) sale.getTicketNumber(),1234);
    }

    @Test
    public void getSetDate(){
        SaleTransactionImpl sale = new SaleTransactionImpl(0.0, "OPEN");
        sale.setDate(LocalDate.now());
        assertEquals(sale.getDate(),LocalDate.now());
    }

    @Test
    public void getSetType(){
        SaleTransactionImpl sale = new SaleTransactionImpl(0.0, "OPEN");
        sale.setType("ANOTHER TYPE");
        assertEquals(sale.getType(), "ANOTHER TYPE");
    }

    @Test
    public void computeEmptyCartTotal(){
        SaleTransactionImpl sale = new SaleTransactionImpl(0.0, "OPEN");
        sale.computeTransactionTotal();
        assertEquals(sale.getPrice(),0.00,0.0001);
    }

    @Test
    public void getEmptyCart(){
        SaleTransactionImpl sale = new SaleTransactionImpl(0.0, "OPEN");
        assertTrue(sale.getCart().isEmpty());
    }

    @Test
    public void getEmptyPayment(){
        SaleTransactionImpl sale = new SaleTransactionImpl(0.0, "OPEN");
        assertNull(sale.isCardPayment());
        assertNull(sale.getPaymentCash());
        assertNull(sale.getPaymentCreditCard());
    }
}