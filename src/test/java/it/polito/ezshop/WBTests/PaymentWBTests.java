package it.polito.ezshop.WBTests;

import static org.junit.Assert.*;

import org.junit.Test;

import it.polito.ezshop.data.Payment;

public class PaymentWBTests {
	@Test
	public void testGetSetAmount() {
		Payment p = new Payment(10.0);
		assertEquals(p.getAmount(),10.0,0);
		p.setAmount(15.00);
		assertEquals(p.getAmount(),15.00,0);
	}
}
