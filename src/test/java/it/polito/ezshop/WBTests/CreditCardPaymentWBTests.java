package it.polito.ezshop.WBTests;

import static org.junit.Assert.*;

import org.junit.Test;

import it.polito.ezshop.data.CreditCard;
import it.polito.ezshop.data.CreditCardPayment;

public class CreditCardPaymentWBTests {

	@Test
	public void testGetSetCC() {
		CreditCard c = new CreditCard("4485370086510891",10);
		CreditCardPayment cp = new CreditCardPayment(5,c);
		assertTrue(cp.getCC()==c);
		CreditCard c2 = new CreditCard("4485370086510892",15);
		cp.setCC(c2);
		assertTrue(cp.getCC()==c2);
	}

}
