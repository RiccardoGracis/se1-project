package it.polito.ezshop.WBTests;

import static org.junit.Assert.*;

import org.junit.Test;

import it.polito.ezshop.data.CashPayment;

public class CashPaymentWBTests {
	@Test
	public void testGetSetChange() {
		CashPayment cp = new CashPayment(10.0,0.50);
		assertEquals(cp.getChange(),0.50,0);
		cp.setChange(1.00);
		assertEquals(cp.getChange(),1.00,0);
	}
}
