package it.polito.ezshop.BBTests;

import static org.junit.Assert.*;

import org.junit.Test;

import it.polito.ezshop.data.CreditCard;

public class CreditCardBBTests {

	@Test
	public void testCCValid() {
		assertTrue(CreditCard.validate("4485370086510891"));
	}
	@Test
	public void testCCnotValid() {
		assertFalse(CreditCard.validate("79927398712"));
	}
}
