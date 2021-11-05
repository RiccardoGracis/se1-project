package it.polito.ezshop.WBTests;

import static org.junit.Assert.*;

import org.junit.Test;

import it.polito.ezshop.data.CreditCard;

public class CreditCardWBTests {

	@Test
	public void testGetSetBalance() {
		CreditCard c = new CreditCard("79927398713",10);
		assertEquals(c.getBalance(),10,0);
		c.setBalance(15.50);
		assertEquals(c.getBalance(),15.50,0);
		//UPDATE?
	}
	@Test
	public void testGetSetNumber_Validation() {
		CreditCard c = new CreditCard("4485370086510891",10);
		assertEquals(c.getNumber(),"4485370086510891");
		assertTrue(c.isValid());
		c.setNumber("79927398712");
		assertEquals(c.getNumber(),"79927398712");
		assertFalse(c.isValid());
		//UPDATE?
	}
	@Test
	public void testValidator() {
		assertFalse(CreditCard.validate("10"));
		assertFalse(CreditCard.validate(""));
		assertFalse(CreditCard.validate("12345678901234567890"));
		assertTrue(CreditCard.validate("4485370086510891"));
		assertFalse(CreditCard.validate("4485370086510890"));		
	}
}
