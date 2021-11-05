package it.polito.ezshop.WBTests;

import static org.junit.Assert.*;

import org.junit.Test;
import it.polito.ezshop.data.*;

public class OrderImplWBTests {
	
	@Test
	public void testGetSetProductCode() {
		OrderImpl o = new OrderImpl("57643947623857",10,1.20,"ISSUED");
		o.setProductCode("234829476238");
		assertEquals(o.getProductCode(),"234829476238");
	}
	
	@Test
	public void testGetSetPricePerUnit() {
		OrderImpl o = new OrderImpl("57643947623857",10,1.20,"PAYED");
		o.setPricePerUnit(1.85);
		assertEquals(o.getPricePerUnit(),new Double(1.851),0.01);
	}
	
	@Test
	public void testGetSetQuantity() {
		OrderImpl o = new OrderImpl("57643947623857",10,1.20,"COMPLETED");
		o.setQuantity(18);
		assertEquals(o.getQuantity(),18);
	}
	
	@Test
	public void testGetSetStatus() {
		OrderImpl o = new OrderImpl("57643947623857",10,1.20,"");
		o.setStatus("Ciao");
		o.setStatus("PAYED");
		o.setStatus("ISSUED");
		o.setStatus("COMPLETED");
		o.setStatus("ORDERED");
		assertEquals(o.getStatus(),"ORDERED");
	}
	
	@Test
	public void testGetSetOrderId() {
		OrderImpl o = new OrderImpl("57643947623857",10,1.20,"ORDERED");
		o.setOrderId(18);
		assertEquals(o.getOrderId(),new Integer(18));
	}
	
	//ToBe completed after the fix
	@Test
	public void testGetSetBalanceId() {
		OrderImpl o = new OrderImpl("57643947623857",10,1.20,"ORDERED");
		o.setBalanceId(18);
		assertEquals(o.getBalanceId(),new Integer(18));
	}

	@Test
	public void testGetSetType() {
		OrderImpl o = new OrderImpl("57643947623857",10,1.20,"ORDERED");
		o.getBalance().setType("ORDER");
		assertEquals(o.getBalance().getType(),"ORDER");
	}
	
	@Test
	public void testGetSetMoney() {
		OrderImpl o = new OrderImpl("57643947623857",10,1.20,"ORDERED");
		o.getBalance().setMoney(2.67);
		assertEquals(o.getBalance().getMoney(),new Double(2.672),0.01);
	}

}
