package it.polito.ezshop.WBTests;

import static org.junit.Assert.*;

import org.junit.Test;
import it.polito.ezshop.data.*;

public class ProductTypeImplWBTests {
	
	@Test
	public void testGetSetQuantity() {
		ProductTypeImpl p = new ProductTypeImpl("ciao","234829476238",12.00,"note");
		p.setQuantity(80);
		assertEquals(p.getQuantity(),new Integer(80));
	}
	
	@Test
	public void testGetSetLocation() {
		ProductTypeImpl p = new ProductTypeImpl("ciao","234829476238",12.00,"note");
		assertNull(p.getLocation());
		p.setLocation("80-A-9");
		assertEquals(p.getLocation(),"80-A-9");
		p.setLocation(null);
		p.setLocation("");
		assertNull(p.getLocation());
	}
	
	@Test
	public void testGetSetNote() {
		ProductTypeImpl p = new ProductTypeImpl("ciao","234829476238",12.00,"note");
		p.setNote("note2");
		assertEquals(p.getNote(),"note2");
	}
	
	@Test
	public void testGetSetProductDescription() {
		ProductTypeImpl p = new ProductTypeImpl("ciao","234829476238",12.00,"note");
		p.setProductDescription("newDescr");
		assertEquals(p.getProductDescription(),"newDescr");
	}
	
	@Test
	public void testGetSetBarCode() {
		ProductTypeImpl p = new ProductTypeImpl("ciao","234829476238",12.00,"note");
		p.setBarCode("57643947623857");
		assertEquals(p.getBarCode(),"57643947623857");
	}
	
	@Test
	public void testGetSetPricePerUnit() {
		ProductTypeImpl p = new ProductTypeImpl("ciao","234829476238",12.00,"note");
		p.setPricePerUnit(80.8);
		assertEquals(p.getPricePerUnit(),new Double(80.801),0.01);
	}
	
	@Test
	public void testGetSetId() {
		ProductTypeImpl p = new ProductTypeImpl("ciao","234829476238",12.00,"note");
		p.setId(80);
		assertEquals(p.getId(),new Integer(80));
	}
	
	@Test
	public void testGetSetDiscountRate() {
		ProductTypeImpl p = new ProductTypeImpl("ciao","234829476238",12.00,"note");
		assertEquals(p.getDiscountRate(),new Double(0.0),0.01);
		assertEquals(p.getPPUWithDiscount(),new Double(12.00),0.01);
		p.setDiscountRate(80.0);
		assertEquals(p.getDiscountRate(),new Double(80.001),0.01);
		assertEquals(p.getPPUWithDiscount(),new Double(2.40),0.01);
		p.setDiscountRate(120.0);
		assertEquals(p.getDiscountRate(),new Double(100.001),0.01);
		assertEquals(p.getPPUWithDiscount(),new Double(0.00),0.01);
	}
	
	@Test
	public void testCoverageValidateBarcode() {
		assertFalse(ProductTypeImpl.validateProdCode("aaaaaaaaaaaaa"));
	}

}
