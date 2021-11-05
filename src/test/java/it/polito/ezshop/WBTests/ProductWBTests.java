package it.polito.ezshop.WBTests;

import static org.junit.Assert.*;

import org.junit.Test;

import it.polito.ezshop.data.Product;

public class ProductWBTests {

	@Test
	public void testGetRFID() {
		Product p = new Product("0123456789","234829476238");
		assertEquals(p.getRFID(),"0123456789");
	}
	@Test
	public void testGetBarcode() {
		Product p = new Product("0123456789","234829476238");
		assertEquals(p.getBarCode(),"234829476238");
	}
	@Test
	public void testValidateRFID() {
		assertEquals(Product.validateRFID(""),false);
		assertEquals(Product.validateRFID("000123456789"),true);
		assertEquals(Product.validateRFID("012345678"),false);
		assertEquals(Product.validateRFID("01234567890"),false);
		assertEquals(Product.validateRFID("012a456789"),false);
		assertEquals(Product.validateRFID("0123456,89"),false);	
		assertTrue(Product.validateRFID("000000000001"));
	}

}
