package it.polito.ezshop.WBTests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import it.polito.ezshop.data.LoyaltyCard;

public class LoyaltyCardWBTests {

	@Test
	public void testGetSetId() {
		LoyaltyCard lc = new LoyaltyCard("1234567890", 0, false);
		lc.setId("1111111111");
		assertEquals(lc.getId(), "1111111111");
	}
	
	@Test
	public void testGetSetPoints() {
		LoyaltyCard lc = new LoyaltyCard("1234567890", 0, false);
		lc.setPoints(50);
		assertEquals(lc.getPoints(), 50);
	}
	
	@Test
	public void testGetSetAssigned() {
		LoyaltyCard lc = new LoyaltyCard("1234567890", 0, false);
		lc.setAssigned(true);
		assertEquals(lc.isAssigned(), true);
	}
	
	@Test
	public void testDetach() {
		LoyaltyCard lc = new LoyaltyCard("1234567890", 5, true);
		lc.detach();
		assertEquals(lc.isAssigned(), false);
		assertEquals(lc.getPoints(), 0);
	}
	
	@Test
	public void testModifyPointsOnCard() {
		LoyaltyCard lc = new LoyaltyCard("1234567890", 0, true);
		assertEquals(lc.modifyPointsOnCard(-1), -1);
		assertEquals(lc.modifyPointsOnCard(10), 10);
	}

	

}
