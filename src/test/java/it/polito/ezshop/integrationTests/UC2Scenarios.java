package it.polito.ezshop.integrationTests;

import org.junit.Test;

import static org.junit.Assert.*;

import java.sql.SQLException;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.data.UserImpl;
import it.polito.ezshop.exceptions.*;

/*** UC2: Manage users and rights ***/
public class UC2Scenarios {

	/*** Scenario 2-1: create user and define rights ***/
	@Test
	public void testScenario2_1() throws InvalidUserIdException, InvalidPasswordException, InvalidRoleException,
			UnauthorizedException, InvalidUsernameException, SQLException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		// Preconditions
		((EZShop) ezShop).db.deleteEntireDB();
		((EZShop) ezShop).db.openConnection();
		((EZShop) ezShop).db.createUserTable();
		((EZShop) ezShop).db.closeConnection();
		((EZShop) ezShop).deleteAllUsers();
		ezShop.createUser("admin", "admin", "Administrator");
		ezShop.login("admin", "admin");

		// Steps
		int id = ezShop.createUser("u1", "pwd1", "Cashier");
		assertEquals(ezShop.getUser(id).getId(), (Integer) id);
		assertEquals(ezShop.getUser(id).getUsername(), "u1");
		assertEquals(ezShop.getUser(id).getPassword(), "pwd1");
		assertEquals(ezShop.getUser(id).getRole(), "Cashier");
		ezShop.updateUserRights(id, "ShopManager");
		assertEquals(ezShop.getUser(id).getRole(), "ShopManager");

		// Postconditions
		assertNotNull(ezShop.getUser(id));
	}

	/*** Scenario 2-2: delete user ***/
	@Test
	public void testScenario2_2() throws SQLException, InvalidUsernameException, InvalidPasswordException,
			InvalidRoleException, InvalidUserIdException, UnauthorizedException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		// Preconditions
		((EZShop) ezShop).db.deleteEntireDB();
		((EZShop) ezShop).db.openConnection();
		((EZShop) ezShop).db.createUserTable();
		((EZShop) ezShop).db.closeConnection();
		((EZShop) ezShop).deleteAllUsers();
		ezShop.createUser("admin", "admin", "Administrator");
		ezShop.login("admin", "admin");
		int id = ezShop.createUser("u1", "pwd1", "Cashier");

		// Steps
		boolean res = ezShop.deleteUser(id);

		// PostCondition
		assertTrue(res);
		assertNull(ezShop.getUser(id));

	}

	/*** Scenario 2-3: modify user rights ***/
	@Test
	public void testScenario2_3() throws SQLException, InvalidUsernameException, InvalidPasswordException,
			InvalidRoleException, InvalidUserIdException, UnauthorizedException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		// Preconditions
		((EZShop) ezShop).db.deleteEntireDB();
		((EZShop) ezShop).db.openConnection();
		((EZShop) ezShop).db.createUserTable();
		((EZShop) ezShop).db.closeConnection();
		((EZShop) ezShop).deleteAllUsers();
		ezShop.createUser("admin", "admin", "Administrator");
		ezShop.login("admin", "admin");
		int id = ezShop.createUser("u1", "pwd1", "Cashier");

		// Steps
		boolean res = ezShop.updateUserRights(id, "ShopManager");
		// PostCondition
		assertTrue(res);
		assertTrue(((UserImpl) ezShop.getUser(id)).isShopManager());

	}

}
