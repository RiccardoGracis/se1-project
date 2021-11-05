package it.polito.ezshop.integrationTests;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.Test;

import it.polito.ezshop.data.AccountBook;
import it.polito.ezshop.data.BalanceOperation;
import it.polito.ezshop.data.BalanceOperationImpl;
import it.polito.ezshop.data.EZShopInterface;

public class AccountBook_IntT {

	@Test
	public void testAddBalanceOp_UpdateBalance_GetByDate() {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.reset();
		AccountBook a = new AccountBook();
		List<BalanceOperation> l;
		BalanceOperationImpl b = new BalanceOperationImpl(LocalDate.now(),"CREDIT",10);
		BalanceOperationImpl b2 = new BalanceOperationImpl(LocalDate.now().minusDays(1),"DEBIT",-20);
		BalanceOperationImpl b3 = new BalanceOperationImpl(LocalDate.now().plusDays(2),"CREDIT",10);
		BalanceOperationImpl b4 = new BalanceOperationImpl(LocalDate.now(),"CREDIT",15);
		
		
		a.addBalanceOperation(b);
		assertEquals(a.getBalance(),10,0);
		assertNotEquals(a.getBalance(),-10,0);
		
		a.addBalanceOperation(b2);
		assertEquals(a.getBalance(),-10,0);
		
		a.addBalanceOperation(b3);
		a.addBalanceOperation(b4);
		l=a.getByDate(null, null);
		assertEquals(l.size(),4);
		l=a.getByDate(null, LocalDate.now());
		assertEquals(l.size(),3);
		l=a.getByDate(LocalDate.now().plusDays(1), null);
		assertEquals(l.size(),1);
		l=a.getByDate(LocalDate.now().minusDays(3), LocalDate.now().plusDays(1));
		assertEquals(l.size(),3);
	}
	@Test
	public void testReset() {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.reset();
		AccountBook a = new AccountBook();
		BalanceOperationImpl b = new BalanceOperationImpl(LocalDate.now(),"CREDIT",10);
		BalanceOperationImpl b2 = new BalanceOperationImpl(LocalDate.now().minusDays(1),"DEBIT",-20);
		a.addBalanceOperation(b);		
		a.addBalanceOperation(b2);
		a.resetT();
		assertTrue(a.getByDate(null, null).isEmpty());
		assertEquals(a.getBalance(),0,0);

	}

}
