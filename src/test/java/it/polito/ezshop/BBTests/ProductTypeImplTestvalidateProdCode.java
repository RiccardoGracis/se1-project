package it.polito.ezshop.BBTests;

import static org.junit.Assert.*;

import org.junit.Test;
import it.polito.ezshop.data.*;

public class ProductTypeImplTestvalidateProdCode {
	
	@Test
	public void testBarCode12() {
		assertTrue(ProductTypeImpl.validateProdCode("234829476238"));
	}
	
	@Test
	public void testBarCode14() {
		assertTrue(ProductTypeImpl.validateProdCode("57643947623857"));
	}
	
	@Test
	public void testBarCodeInvalid() {
		assertFalse(ProductTypeImpl.validateProdCode("1234567891023"));
	}
	
	@Test
	public void testBarCodeAlpha() {
		assertFalse(ProductTypeImpl.validateProdCode("90sf"));
	}
	
	@Test
	public void testBarCodeEmpty() {
		assertFalse(ProductTypeImpl.validateProdCode(""));
	}
	
	@Test
	public void testBarCodeLong() {
		assertFalse(ProductTypeImpl.validateProdCode("098765432123456"));
	}
	
	@Test
	public void testBarCodeNull() {
		assertFalse(ProductTypeImpl.validateProdCode(null));
	}
	

}
