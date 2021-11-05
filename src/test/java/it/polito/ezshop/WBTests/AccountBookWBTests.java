package it.polito.ezshop.WBTests;

import static org.junit.Assert.*;
import it.polito.ezshop.data.*;
import org.junit.Test;

public class AccountBookWBTests {

	@Test
	public void testGetBalance() {
		AccountBook a = new AccountBook();
		a.setBalance(150.53);
		assertEquals(a.getBalance(),150.53,0);	
	}
}
