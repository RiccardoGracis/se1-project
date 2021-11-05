package it.polito.ezshop.WBTests;

import static org.junit.Assert.*;

import org.junit.Test;
import it.polito.ezshop.data.*;

public class PositionWBTests {

	@Test
	public void testGetSetAisleID() {
		Position p=new Position(1,"A",34);
		p.setAisleID(18);
		assertEquals(p.getAisleID(),18);
	}
	
	@Test
	public void testGetSetRackID() {
		Position p=new Position(1,"A",34);
		p.setRackID("ASL");
		assertEquals(p.getRackID(),"ASL");
	}
	
	@Test
	public void testGetSetLevelID() {
		Position p=new Position(1,"A",34);
		p.setLevelID(02);
		assertEquals(p.getLevelID(),2);
	}
	
	@Test
	public void testCoverageValidatePos() {
		assertFalse(Position.validatePosition("A-A-4"));
		assertFalse(Position.validatePosition("4-A-A"));
	}
}
