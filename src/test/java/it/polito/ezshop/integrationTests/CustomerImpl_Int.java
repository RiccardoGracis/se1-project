package it.polito.ezshop.integrationTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import it.polito.ezshop.data.CustomerImpl;
import it.polito.ezshop.data.LoyaltyCard;
import it.polito.ezshop.exceptions.InvalidCustomerCardException;
import it.polito.ezshop.exceptions.InvalidCustomerNameException;

public class CustomerImpl_Int {
	
	@Test
	public void testGetSetId() {
		CustomerImpl c = new CustomerImpl(1, "c1");
		c.setId(2);
		assertEquals(c.getId(), (Integer) 2);
	}
	
	@Test
	public void testGetSetCustomerName() {
		CustomerImpl c = new CustomerImpl(1, "c1");
		c.setCustomerName("c2");
		assertEquals(c.getCustomerName(), "c2");
	}
	
	@Test
	public void testGetSetCustomerCard() {
		CustomerImpl c = new CustomerImpl(1, "c1");
		assertNull(c.getCustomerCard());
		String newLc = "1234567890";
		c.setCustomerCard(newLc);
		assertEquals(c.getCustomerCard(), newLc);
	}
	
	@Test
	public void testGetSetPoints() {
		CustomerImpl c = new CustomerImpl(1, "c1");
		String newLc = "1234567890";
		c.setCustomerCard(newLc);
		c.setPoints(50);
		assertEquals(c.getPoints(), (Integer) 50);
	}
	
	@Test
	public void testModifyCustomer() throws InvalidCustomerNameException, InvalidCustomerCardException {
	
		CustomerImpl c = new CustomerImpl(1, "c1");
		assertThrows(InvalidCustomerNameException.class, () -> {c.modifyCustomer(null, null);});
		assertThrows(InvalidCustomerNameException.class, () -> {c.modifyCustomer("", null);});
		assertThrows(InvalidCustomerCardException.class, () -> {c.modifyCustomer("c1", "123");});
		c.modifyCustomer("c1", "");
		assertNull(c.getCustomerCard());
		c.modifyCustomer("c2", "1234567890");
		assertEquals(c.getCustomerCard(), "1234567890");
		assertEquals(c.getCustomerName(), "c2");
	}
	
	@Test
	public void testAttachCardToCustomer() {
		CustomerImpl c = new CustomerImpl(1, "c1");
		String newLc = "1234567890";
		LoyaltyCard lc = new LoyaltyCard(newLc, 0, false);
		c.attachCardToCustomer(lc);
		assertEquals(c.getCustomerCard(), newLc);
		assertEquals(c.getPoints(), (Integer) 0);
		assertTrue(lc.isAssigned());
	}
	
	@Test
	public void testDetachCardFromCustomer() {
		CustomerImpl c = new CustomerImpl(1, "c1");
		String newLc = "1234567890";
		LoyaltyCard lc = new LoyaltyCard(newLc, 0, false);
		c.attachCardToCustomer(lc);
		c.detachCardFromCustomer();
		assertNull(c.getCustomerCard());
	}

}
