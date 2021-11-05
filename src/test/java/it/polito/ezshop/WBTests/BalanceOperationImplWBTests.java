package it.polito.ezshop.WBTests;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Test;

import it.polito.ezshop.data.BalanceOperationImpl;

public class BalanceOperationImplWBTests {
	@Test
	public void testGetSetBalanceId() {
		BalanceOperationImpl b = new BalanceOperationImpl(LocalDate.now(),"CREDIT",10.50);
		b.setBalanceId(150);
		assertEquals(b.getBalanceId(),150);
	}
	@Test
	public void testGetSetDate() {
		LocalDate d = LocalDate.now();
		BalanceOperationImpl b = new BalanceOperationImpl(d,"CREDIT",10.50);
		assertEquals(b.getDate(), d);
		LocalDate d2=d.minusDays(1);
		b.setDate(d2);
		assertEquals(b.getDate(),d2);
	}
	@Test
	public void testGetSetMoney() {
		BalanceOperationImpl b = new BalanceOperationImpl(LocalDate.now(),"CREDIT",11.00);
		double x=15.00;
		assertEquals(b.getMoney(),11.00,0);
		b.setMoney(x);
		assertEquals(b.getMoney(),x,0);
	}
	@Test
	public void testGetSetType() {
		BalanceOperationImpl b = new BalanceOperationImpl(LocalDate.now(),"CREDIT",11.00);
		assertEquals(b.getType(),"CREDIT");
		b.setType("DEBIT");
		assertEquals(b.getType(),"DEBIT");
	}
	@Test
	public void testSetCounter() {
		BalanceOperationImpl.setCounterFromDb(1, 0, 5);
		BalanceOperationImpl b = new BalanceOperationImpl(LocalDate.now(),"CREDIT",11.00);
		assertEquals(6,b.getBalanceId());
	}

}
