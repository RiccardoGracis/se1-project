package it.polito.ezshop.integrationTests;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.Test;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.*;

public class EZShop_Users_Int {
	
	// Create User Tests
	
	@Test
	public void createUserTestUsernameInvalid() throws SQLException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		((EZShop) ezShop).db.deleteEntireDB();
		((EZShop) ezShop).db.openConnection();
		((EZShop) ezShop).db.createUserTable();
		((EZShop) ezShop).db.closeConnection();
		((EZShop) ezShop).deleteAllUsers();
		assertThrows(InvalidUsernameException.class, () -> ezShop.createUser("", "admin", "Administrator"));
		assertThrows(InvalidUsernameException.class, () -> ezShop.createUser(null, "admin", "Administrator"));
		
	}
	
	@Test
	public void createUserTestPasswordInvalid() throws SQLException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		((EZShop) ezShop).db.deleteEntireDB();
		((EZShop) ezShop).db.openConnection();
		((EZShop) ezShop).db.createUserTable();
		((EZShop) ezShop).db.closeConnection();
		((EZShop) ezShop).deleteAllUsers();
		assertThrows(InvalidPasswordException.class, () -> ezShop.createUser("admin", "", "Administrator"));
		assertThrows(InvalidPasswordException.class, () -> ezShop.createUser("admin", null, "Administrator"));
		
	}
	
	@Test
	public void createUserTestRoleInvalid() throws SQLException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		((EZShop) ezShop).db.deleteEntireDB();
		((EZShop) ezShop).db.openConnection();
		((EZShop) ezShop).db.createUserTable();
		((EZShop) ezShop).db.closeConnection();
		((EZShop) ezShop).deleteAllUsers();
		assertThrows(InvalidRoleException.class, () -> ezShop.createUser("admin", "admin", ""));
		assertThrows(InvalidRoleException.class, () -> ezShop.createUser("admin", "admin", null));
		assertThrows(InvalidRoleException.class, () -> ezShop.createUser("admin", "admin", "admin"));

		
	}
	
	@Test
	public void createUserTestUsernameAlreadyExisting() throws SQLException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		((EZShop) ezShop).db.deleteEntireDB();
		((EZShop) ezShop).db.openConnection();
		((EZShop) ezShop).db.createUserTable();
		((EZShop) ezShop).db.closeConnection();
		((EZShop) ezShop).deleteAllUsers();
		ezShop.createUser("admin", "admin", "Administrator");
		Integer ret = -1;
		assertEquals(ezShop.createUser("admin", "admin", "Administrator"), ret);
		
	}
	
	@Test
	public void createUserTestCorrect() throws SQLException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		((EZShop) ezShop).db.deleteEntireDB();
		((EZShop) ezShop).db.openConnection();
		((EZShop) ezShop).db.createUserTable();
		((EZShop) ezShop).db.closeConnection();
		((EZShop) ezShop).deleteAllUsers();
		ezShop.createUser("admin", "admin", "Administrator");
		
	}
	
	// Delete user tests
	
	@Test
	public void deleteUserTestUnauthorizedOperation() throws SQLException, InvalidUserIdException, UnauthorizedException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		((EZShop) ezShop).db.deleteEntireDB();
		((EZShop) ezShop).db.openConnection();
		((EZShop) ezShop).db.createUserTable();
		((EZShop) ezShop).db.closeConnection();
		((EZShop) ezShop).deleteAllUsers();
		assertThrows(UnauthorizedException.class, () -> ezShop.deleteUser(-1));
		ezShop.createUser("u1", "u1", "Cashier");
		ezShop.login("u1", "u1");
		assertThrows(UnauthorizedException.class, () -> ezShop.deleteUser(-1));
		
	}
	
	@Test
	public void deleteUserTestInvalidId() throws SQLException, InvalidUserIdException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		((EZShop) ezShop).db.deleteEntireDB();
		((EZShop) ezShop).db.openConnection();
		((EZShop) ezShop).db.createUserTable();
		((EZShop) ezShop).db.closeConnection();
		((EZShop) ezShop).deleteAllUsers();
		ezShop.createUser("admin", "admin", "Administrator");
		ezShop.login("admin", "admin");
		assertThrows(InvalidUserIdException.class, () -> ezShop.deleteUser(0));
		assertThrows(InvalidUserIdException.class, () -> ezShop.deleteUser(null));
		
	}
	
	@Test
	public void deleteUserTestCorrect() throws SQLException, InvalidUserIdException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException, UnauthorizedException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		((EZShop) ezShop).db.deleteEntireDB();
		((EZShop) ezShop).db.openConnection();
		((EZShop) ezShop).db.createUserTable();
		((EZShop) ezShop).db.closeConnection();
		((EZShop) ezShop).deleteAllUsers();
		int id = ezShop.createUser("admin", "admin", "Administrator");
		ezShop.login("admin", "admin");
		assertFalse(ezShop.deleteUser(5));
		assertTrue(ezShop.deleteUser(id));
		
	}	
	
	// getAllUsers
	
	@Test
	public void getAllUsersTestUnauthorizedOperation() throws UnauthorizedException, SQLException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		((EZShop) ezShop).db.deleteEntireDB();
		((EZShop) ezShop).db.openConnection();
		((EZShop) ezShop).db.createUserTable();
		((EZShop) ezShop).db.closeConnection();
		((EZShop) ezShop).deleteAllUsers();
		assertThrows(UnauthorizedException.class, () -> ezShop.getAllUsers());
		ezShop.createUser("u1", "u1", "Cashier");
		ezShop.login("u1", "u1");
		assertThrows(UnauthorizedException.class, () -> ezShop.getAllUsers());
		
	}
	
	@Test
	public void getAllUsersTestCorrect() throws UnauthorizedException, SQLException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		((EZShop) ezShop).db.deleteEntireDB();
		((EZShop) ezShop).db.openConnection();
		((EZShop) ezShop).db.createUserTable();
		((EZShop) ezShop).db.closeConnection();
		((EZShop) ezShop).deleteAllUsers();
		ezShop.createUser("admin", "admin", "Administrator");
		ezShop.login("admin", "admin");
		ezShop.getAllUsers();
	}
	
	// getUser
	
	@Test
	public void getUserTestInvalid() throws UnauthorizedException, SQLException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidUserIdException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		((EZShop) ezShop).db.deleteEntireDB();
		((EZShop) ezShop).db.openConnection();
		((EZShop) ezShop).db.createUserTable();
		((EZShop) ezShop).db.closeConnection();
		((EZShop) ezShop).deleteAllUsers();
		assertThrows(UnauthorizedException.class, () -> ezShop.getUser(1));
		ezShop.createUser("u", "u", "Cashier");
		ezShop.login("u", "u");
		assertThrows(UnauthorizedException.class, () -> ezShop.getUser(1));
		ezShop.logout();
		ezShop.createUser("admin", "admin", "Administrator");
		ezShop.login("admin", "admin");
		assertThrows(InvalidUserIdException.class, () -> ezShop.getUser(-1));
		assertThrows(InvalidUserIdException.class, () -> ezShop.getUser(null));
	}
	
	@Test
	public void getUserTestCorrect() throws UnauthorizedException, SQLException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidUserIdException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		((EZShop) ezShop).db.deleteEntireDB();
		((EZShop) ezShop).db.openConnection();
		((EZShop) ezShop).db.createUserTable();
		((EZShop) ezShop).db.closeConnection();
		((EZShop) ezShop).deleteAllUsers();
		ezShop.createUser("admin", "admin", "Administrator");
		ezShop.login("admin", "admin");
		assertNull(ezShop.getUser(2));
		ezShop.getUser(1);
	}
	
	// updateUserRights
	
	@Test
	public void updateUserRightsTestInvalid() throws UnauthorizedException, SQLException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidUserIdException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		((EZShop) ezShop).db.deleteEntireDB();
		((EZShop) ezShop).db.openConnection();
		((EZShop) ezShop).db.createUserTable();
		((EZShop) ezShop).db.closeConnection();
		((EZShop) ezShop).deleteAllUsers();
		ezShop.createUser("admin", "admin", "Administrator");
		assertThrows(UnauthorizedException.class, () -> ezShop.getUser(1));
		ezShop.login("admin", "admin");
		assertThrows(InvalidUserIdException.class, () -> ezShop.updateUserRights(-1, null));
		assertThrows(InvalidUserIdException.class, () -> ezShop.updateUserRights(null, null));
		assertThrows(InvalidRoleException.class, () -> ezShop.updateUserRights(1, null));
		assertThrows(InvalidRoleException.class, () -> ezShop.updateUserRights(1, ""));
		assertThrows(InvalidRoleException.class, () -> ezShop.updateUserRights(1, "admin"));
		
		
	}

	@Test
	public void updateUserRightsTestCorrect() throws UnauthorizedException, SQLException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidUserIdException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		((EZShop) ezShop).db.deleteEntireDB();
		((EZShop) ezShop).db.openConnection();
		((EZShop) ezShop).db.createUserTable();
		((EZShop) ezShop).db.closeConnection();
		((EZShop) ezShop).deleteAllUsers();
		ezShop.createUser("admin", "admin", "Administrator");
		ezShop.login("admin", "admin");
		
		assertFalse(ezShop.updateUserRights(2, "ShopManager"));
		assertTrue(ezShop.updateUserRights(1, "ShopManager"));
		
	}


		
		

	
	
	
}
