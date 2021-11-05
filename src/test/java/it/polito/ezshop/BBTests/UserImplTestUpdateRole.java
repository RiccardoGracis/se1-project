package it.polito.ezshop.BBTests;

import static org.junit.Assert.assertThrows;

import org.junit.Test;

import it.polito.ezshop.data.UserImpl;
import it.polito.ezshop.exceptions.InvalidRoleException;

public class UserImplTestUpdateRole {

	@Test
	public void testNullRole() throws InvalidRoleException {

		UserImpl u = new UserImpl(1, "admin", "admin", "Administrator");
		assertThrows(InvalidRoleException.class, () -> { u.updateRole(null); });


	}
	
	
	@Test
	public void testRoleWrongValue() throws InvalidRoleException {

		UserImpl u = new UserImpl(1, "admin", "admin", "Administrator");
		assertThrows(InvalidRoleException.class, () -> { u.updateRole("Admin"); });
		assertThrows(InvalidRoleException.class, () -> { u.updateRole(""); });



	}
	
	@Test
	public void testRoleCorrectValue() throws InvalidRoleException {
		
		UserImpl u = new UserImpl(1, "admin", "admin", "Cashier");
		u.updateRole("Administrator");
		u.updateRole("ShopManager");
		u.updateRole("Cashier");

	}


}

