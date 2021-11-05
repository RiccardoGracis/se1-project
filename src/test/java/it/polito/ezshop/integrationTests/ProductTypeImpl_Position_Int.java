package it.polito.ezshop.integrationTests;

import static org.junit.Assert.*;

import org.junit.Test;

import it.polito.ezshop.data.*;

public class ProductTypeImpl_Position_Int {
	
	@Test
	public void testInitialNullPos() {
		ProductType p = new ProductTypeImpl("Eggs","234829476238",2.0,"note1");
		assertNull(p.getLocation());
	}
	
	//Position is validated in API, so we don't need to check its validity in this
	//Integration level
	
	@Test
	public void testCorrectPos() {
		ProductType p = new ProductTypeImpl("Eggs","234829476238",2.0,"note1");
		p.setLocation("12-AB-7");
		assertNotNull(p.getLocation());
		assertEquals(p.getLocation(),"12-AB-7");
	}
	
	@Test
	public void testResetPos1() {
		ProductType p = new ProductTypeImpl("Eggs","234829476238",2.0,"note1");
		p.setLocation("12-AB-7");
		assertNotNull(p.getLocation());
		assertEquals(p.getLocation(),"12-AB-7");
		p.setLocation("");
		assertNull(p.getLocation());
	}
	
	@Test
	public void testResetPos2() {
		ProductType p = new ProductTypeImpl("Eggs","234829476238",2.0,"note1");
		p.setLocation("12-AB-7");
		assertNotNull(p.getLocation());
		assertEquals(p.getLocation(),"12-AB-7");
		p.setLocation(null);
		assertNull(p.getLocation());
	}
	
	@Test
	public void testChangePos() {
		ProductType p = new ProductTypeImpl("Eggs","234829476238",2.0,"note1");
		p.setLocation("12-AB-7");
		assertNotNull(p.getLocation());
		assertEquals(p.getLocation(),"12-AB-7");
		p.setLocation("1-CC-15");
		assertNotNull(p.getLocation());
		assertEquals(p.getLocation(),"1-CC-15");
	}

}
