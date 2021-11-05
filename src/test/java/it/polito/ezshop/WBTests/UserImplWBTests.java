package it.polito.ezshop.WBTests;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.Test;
import it.polito.ezshop.data.*;

public class UserImplWBTests {
	
	@Test
	public void testGetSetId() {
		UserImpl u = new UserImpl(1, "u1", "pwd", "Administrator");
		u.setId(2);
		assertEquals(u.getId(), (Integer) 2);
	}
	
	@Test
	public void testGetSetUsername() {
		UserImpl u = new UserImpl(1, "u1", "pwd", "Administrator");
		u.setUsername("u2");
		assertEquals(u.getUsername(),"u2");
	}
	
	@Test
	public void testGetSetPassword() {
		UserImpl u = new UserImpl(1, "u1", "pwd", "Administrator");
		u.setPassword("pwd2");
		assertEquals(u.getPassword(),"pwd2");
	}
	
	@Test
	public void testGetSetRole() {
		UserImpl u = new UserImpl(1, "u1", "pwd", "Administrator");
		u.setRole("ShopManager");
		assertEquals(u.getRole(),"ShopManager");
	}
	
	@Test
	public void testCurrentRole() {
		UserImpl u = new UserImpl(1, "u1", "pwd", "Administrator");
		assertTrue(u.isAdministator());
		u.setRole("ShopManager");
		assertTrue(u.isShopManager());
		u.setRole("Cashier");
		assertTrue(u.isCashier());
	}


}

