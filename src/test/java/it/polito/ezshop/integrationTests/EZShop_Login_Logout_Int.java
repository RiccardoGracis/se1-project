package it.polito.ezshop.integrationTests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;

import org.junit.Test;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidRoleException;
import it.polito.ezshop.exceptions.InvalidUsernameException;

public class EZShop_Login_Logout_Int {
	
	// Login Tests
	
		@Test
		public void loginTestInvalid() throws SQLException, InvalidUsernameException, InvalidPasswordException {
			EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
			((EZShop) ezShop).db.deleteEntireDB();
			((EZShop) ezShop).db.openConnection();
			((EZShop) ezShop).db.createUserTable();
			((EZShop) ezShop).db.closeConnection();
			((EZShop) ezShop).deleteAllUsers();
			assertThrows(InvalidUsernameException.class, () -> ezShop.login("", "admin"));
			assertThrows(InvalidUsernameException.class, () -> ezShop.login(null, "admin"));
			assertThrows(InvalidPasswordException.class, () -> ezShop.login("admin", ""));
			assertThrows(InvalidPasswordException.class, () -> ezShop.login("admin", null));			
		}
		
		@Test
		public void loginTestCorrect() throws SQLException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
			EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
			((EZShop) ezShop).db.deleteEntireDB();
			((EZShop) ezShop).db.openConnection();
			((EZShop) ezShop).db.createUserTable();
			((EZShop) ezShop).db.closeConnection();
			((EZShop) ezShop).deleteAllUsers();
			ezShop.createUser("admin", "admin", "Administrator");
			ezShop.login("admin", "admin");			
		}
		
		// logout test
		@Test
		public void logoutTestInvalid() throws SQLException {
			EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
			((EZShop) ezShop).db.deleteEntireDB();
			((EZShop) ezShop).db.openConnection();
			((EZShop) ezShop).db.createUserTable();
			((EZShop) ezShop).db.closeConnection();
			((EZShop) ezShop).deleteAllUsers();
			assertFalse(ezShop.logout());			
		}
		
		@Test
		public void logoutTestCorrect() throws SQLException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
			EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
			((EZShop) ezShop).db.deleteEntireDB();
			((EZShop) ezShop).db.openConnection();
			((EZShop) ezShop).db.createUserTable();
			((EZShop) ezShop).db.closeConnection();
			((EZShop) ezShop).deleteAllUsers();
			ezShop.createUser("admin", "admin", "Administrator");
			ezShop.login("admin", "admin");
			assertTrue(ezShop.logout());			
		}
		
		

}
