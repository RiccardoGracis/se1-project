package it.polito.ezshop.BBTests;

import static org.junit.Assert.*;

import org.junit.Test;
import it.polito.ezshop.data.*;

public class ProductTypeImplTestvalidateDescr {

	@Test
	public void testValidDescr() {
		String s1="ciao";
		assertTrue(ProductTypeImpl.validateDescr(s1));
	}
	
	@Test
	public void testEmptyDescr() {
		String s2="";
		assertFalse(ProductTypeImpl.validateDescr(s2));
	}
	
	@Test
	public void testNullDescr() {
		assertFalse(ProductTypeImpl.validateDescr(null));
	}
}
