package it.polito.ezshop.integrationTests;

import static org.junit.Assert.*;
import java.sql.SQLException;

import org.junit.Test;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.data.User;
import it.polito.ezshop.exceptions.*;

public class UC5Scenarios {
	
	/*** Scenario 5-1: login ***/
	@Test
	public void testScenario5_1() throws SQLException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidCustomerIdException, UnauthorizedException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		// Preconditions
		((EZShop) ezShop).db.deleteEntireDB();
		((EZShop) ezShop).db.openConnection();
		((EZShop) ezShop).db.createUserTable();
		((EZShop) ezShop).db.closeConnection();
		((EZShop) ezShop).deleteAllUsers();
		int id = ezShop.createUser("admin", "pass", "Administrator");

		// Steps
		User u = ezShop.login("admin", "pass");

		// Postconditions
		assertEquals(u.getId(), ((EZShop) ezShop).getLoggedUser().getId());
		assertEquals(u.getUsername(), ((EZShop) ezShop).getLoggedUser().getUsername());
		assertEquals(u.getPassword(), ((EZShop) ezShop).getLoggedUser().getPassword());
		assertEquals(u.getRole(), ((EZShop) ezShop).getLoggedUser().getRole());

	}
	
	/*** Scenario 5-2: logout ***/
	@Test
	public void testScenario5_2() throws SQLException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidCustomerIdException, UnauthorizedException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		// Preconditions
		((EZShop) ezShop).db.deleteEntireDB();
		((EZShop) ezShop).db.openConnection();
		((EZShop) ezShop).db.createUserTable();
		((EZShop) ezShop).db.closeConnection();
		((EZShop) ezShop).deleteAllUsers();
		int id = ezShop.createUser("admin", "pass", "Administrator");
		User u = ezShop.login("admin", "pass");


		// Steps
		boolean res = ezShop.logout();

		// Postconditions
		assertTrue(res);
		assertNull(((EZShop) ezShop).getLoggedUser());
	
	}

}
