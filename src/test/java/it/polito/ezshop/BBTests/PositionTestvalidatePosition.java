package it.polito.ezshop.BBTests;

import static org.junit.Assert.*;

import org.junit.Test;
import it.polito.ezshop.data.*;

public class PositionTestvalidatePosition {
	@Test
	public void testPositionValid() {
		assertTrue(Position.validatePosition("12-AB-8903"));
	}
	
	@Test
	public void testPositionInvalid() {
		assertFalse(Position.validatePosition("12AB-8903"));
	}
	
	@Test
	public void testPositionNoDelim() {
		assertFalse(Position.validatePosition("AB12B"));
	}
	
	@Test
	public void testPositionNull() {
		assertFalse(Position.validatePosition(null));
	}
	
	@Test
	public void testPositionEmpty() {
		assertFalse(Position.validatePosition(""));
	}
}
