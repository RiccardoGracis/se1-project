package it.polito.ezshop.integrationTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;

import org.junit.Test;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.InvalidCustomerCardException;
import it.polito.ezshop.exceptions.InvalidCustomerIdException;
import it.polito.ezshop.exceptions.InvalidCustomerNameException;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidRoleException;
import it.polito.ezshop.exceptions.InvalidUsernameException;
import it.polito.ezshop.exceptions.UnauthorizedException;

public class EZShop_Customers_Int {
	
	// defineCustomer
	
	@Test
	public void defineCustomerTestInvalid() throws SQLException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		((EZShop) ezShop).db.deleteEntireDB();
		((EZShop) ezShop).db.openConnection();
		((EZShop) ezShop).db.createUserTable();
		((EZShop) ezShop).db.createLoyaltyCardTable();
		((EZShop) ezShop).db.createCustomerTable();
		((EZShop) ezShop).db.closeConnection();
		((EZShop) ezShop).deleteAllUsers();
		((EZShop) ezShop).deleteAllCustomers();
		((EZShop) ezShop).deleteAllLoyaltyCards();

		ezShop.createUser("admin", "admin", "Administrator");
		assertThrows(UnauthorizedException.class, () -> ezShop.defineCustomer("c1"));
		ezShop.login("admin", "admin");
		assertThrows(InvalidCustomerNameException.class, () -> ezShop.defineCustomer(""));
		assertThrows(InvalidCustomerNameException.class, () -> ezShop.defineCustomer(null));
		
	}
	
	@Test
	public void defineCustomerTestCorrect() throws SQLException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidCustomerNameException, UnauthorizedException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		((EZShop) ezShop).db.deleteEntireDB();
		((EZShop) ezShop).db.openConnection();
		((EZShop) ezShop).db.createUserTable();
		((EZShop) ezShop).db.createLoyaltyCardTable();
		((EZShop) ezShop).db.createCustomerTable();
		((EZShop) ezShop).db.closeConnection();
		((EZShop) ezShop).deleteAllUsers();
		((EZShop) ezShop).deleteAllCustomers();
		((EZShop) ezShop).deleteAllLoyaltyCards();

		ezShop.createUser("admin", "admin", "Administrator");
		ezShop.login("admin", "admin");
		Integer a = -1;
		assertEquals(ezShop.defineCustomer("c1"), (Integer) 1);
		assertEquals(ezShop.defineCustomer("c1"), a);
		
	}
	
	// modifyCustomer
	
	@Test
	public void modifyCustomerTestInvalid() throws SQLException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		((EZShop) ezShop).db.deleteEntireDB();
		((EZShop) ezShop).db.openConnection();
		((EZShop) ezShop).db.createUserTable();
		((EZShop) ezShop).db.createLoyaltyCardTable();
		((EZShop) ezShop).db.createCustomerTable();
		((EZShop) ezShop).db.closeConnection();
		((EZShop) ezShop).deleteAllUsers();
		((EZShop) ezShop).deleteAllCustomers();
		((EZShop) ezShop).deleteAllLoyaltyCards();

		ezShop.createUser("admin", "admin", "Administrator");
		assertThrows(UnauthorizedException.class, () -> ezShop.modifyCustomer(1, "c1", ""));
		ezShop.login("admin", "admin");
		
		assertThrows(InvalidCustomerNameException.class, () -> ezShop.modifyCustomer(1, "", null));
		assertThrows(InvalidCustomerNameException.class, () -> ezShop.modifyCustomer(1, null, null));
		assertThrows(InvalidCustomerCardException.class, () -> ezShop.modifyCustomer(1, "c1", "123"));
		
	}
	
	@Test
	public void modifyCustomerTestCorrect() throws SQLException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidCustomerNameException, UnauthorizedException, InvalidCustomerCardException, InvalidCustomerIdException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		((EZShop) ezShop).db.deleteEntireDB();
		((EZShop) ezShop).db.openConnection();
		((EZShop) ezShop).db.createUserTable();
		((EZShop) ezShop).db.createLoyaltyCardTable();
		((EZShop) ezShop).db.createCustomerTable();
		((EZShop) ezShop).db.closeConnection();
		((EZShop) ezShop).deleteAllUsers();
		((EZShop) ezShop).deleteAllCustomers();
		((EZShop) ezShop).deleteAllLoyaltyCards();

		ezShop.createUser("admin", "admin", "Administrator");
		ezShop.login("admin", "admin");
		
		int id = ezShop.defineCustomer("c1");
		
		ezShop.modifyCustomer(id, "c2", null);
		assertEquals(ezShop.getCustomer(id).getCustomerName(), "c2");
		assertNull(ezShop.getCustomer(id).getCustomerCard());
		
		ezShop.modifyCustomer(id, "c2", null);
		assertEquals(ezShop.getCustomer(id).getCustomerName(), "c2");
		assertNull(ezShop.getCustomer(id).getCustomerCard());
		
		ezShop.modifyCustomer(id, "c3", "");
		assertEquals(ezShop.getCustomer(id).getCustomerName(), "c3");
		assertNull(ezShop.getCustomer(id).getCustomerCard());
		
		ezShop.modifyCustomer(id, "c3", "");
		assertEquals(ezShop.getCustomer(id).getCustomerName(), "c3");
		assertNull(ezShop.getCustomer(id).getCustomerCard());
		
		ezShop.modifyCustomer(id, "c4", "1234567890");
		assertEquals(ezShop.getCustomer(id).getCustomerName(), "c4");
		assertEquals(ezShop.getCustomer(id).getCustomerCard(), "1234567890");
		
		ezShop.modifyCustomer(id, "c4", "1234567890");
		assertEquals(ezShop.getCustomer(id).getCustomerName(), "c4");
		assertEquals(ezShop.getCustomer(id).getCustomerCard(), "1234567890");
		
		ezShop.modifyCustomer(id, "c5", null);
		assertEquals(ezShop.getCustomer(id).getCustomerName(), "c5");
		assertEquals(ezShop.getCustomer(id).getCustomerCard(), "1234567890");
		
		ezShop.modifyCustomer(id, "c6", "");
		assertEquals(ezShop.getCustomer(id).getCustomerName(), "c6");
		assertNull(ezShop.getCustomer(id).getCustomerCard());
		
		
	}
	
	
	// deleteCustomer
	
	@Test
	public void deleteCustomerTestInvalid() throws SQLException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		((EZShop) ezShop).db.deleteEntireDB();
		((EZShop) ezShop).db.openConnection();
		((EZShop) ezShop).db.createUserTable();
		((EZShop) ezShop).db.createLoyaltyCardTable();
		((EZShop) ezShop).db.createCustomerTable();
		((EZShop) ezShop).db.closeConnection();
		((EZShop) ezShop).deleteAllUsers();
		((EZShop) ezShop).deleteAllCustomers();
		((EZShop) ezShop).deleteAllLoyaltyCards();

		ezShop.createUser("admin", "admin", "Administrator");
		assertThrows(UnauthorizedException.class, () -> ezShop.deleteCustomer(1));
		ezShop.login("admin", "admin");
		assertThrows(InvalidCustomerIdException.class, () -> ezShop.deleteCustomer(0));
		assertThrows(InvalidCustomerIdException.class, () -> ezShop.deleteCustomer(null));
		
	}
	
	@Test
	public void deleteCustomerTestCorrect() throws SQLException, InvalidUsernameException, InvalidPasswordException, InvalidCustomerNameException, UnauthorizedException, InvalidCustomerIdException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		((EZShop) ezShop).db.deleteEntireDB();
		((EZShop) ezShop).db.openConnection();
		((EZShop) ezShop).db.createUserTable();
		((EZShop) ezShop).db.createLoyaltyCardTable();
		((EZShop) ezShop).db.createCustomerTable();
		((EZShop) ezShop).db.closeConnection();
		((EZShop) ezShop).deleteAllUsers();
		((EZShop) ezShop).deleteAllCustomers();
		((EZShop) ezShop).deleteAllLoyaltyCards();

		ezShop.createUser("admin", "admin", "Administrator");
		ezShop.login("admin", "admin");
		int id = ezShop.defineCustomer("c1");
		assertFalse(ezShop.deleteCustomer(id+1));
		assertTrue(ezShop.deleteCustomer(id));
		
	}
	
	// getCustomer
	@Test
	public void getCustomerTestInvalid() throws SQLException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		((EZShop) ezShop).db.deleteEntireDB();
		((EZShop) ezShop).db.openConnection();
		((EZShop) ezShop).db.createUserTable();
		((EZShop) ezShop).db.createLoyaltyCardTable();
		((EZShop) ezShop).db.createCustomerTable();
		((EZShop) ezShop).db.closeConnection();
		((EZShop) ezShop).deleteAllUsers();
		((EZShop) ezShop).deleteAllCustomers();
		((EZShop) ezShop).deleteAllLoyaltyCards();

		ezShop.createUser("admin", "admin", "Administrator");
		assertThrows(UnauthorizedException.class, () -> ezShop.getCustomer(1));
		ezShop.login("admin", "admin");
		assertThrows(InvalidCustomerIdException.class, () -> ezShop.getCustomer(0));
		assertThrows(InvalidCustomerIdException.class, () -> ezShop.getCustomer(null));
		
	}
	
	@Test
	public void getCustomerTestCorrect() throws SQLException, InvalidUsernameException, InvalidPasswordException, InvalidCustomerNameException, UnauthorizedException, InvalidCustomerIdException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		((EZShop) ezShop).db.deleteEntireDB();
		((EZShop) ezShop).db.openConnection();
		((EZShop) ezShop).db.createUserTable();
		((EZShop) ezShop).db.createLoyaltyCardTable();
		((EZShop) ezShop).db.createCustomerTable();
		((EZShop) ezShop).db.closeConnection();
		((EZShop) ezShop).deleteAllUsers();
		((EZShop) ezShop).deleteAllCustomers();
		((EZShop) ezShop).deleteAllLoyaltyCards();

		ezShop.createUser("admin", "admin", "Administrator");
		ezShop.login("admin", "admin");
		int id = ezShop.defineCustomer("c1");
		assertNull(ezShop.getCustomer(id+1));
		ezShop.getCustomer(id);
		
	}
	
	// getAllCustomers
	@Test
	public void getAllCustomersTest() throws SQLException, InvalidUsernameException, UnauthorizedException, InvalidRoleException, InvalidPasswordException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		((EZShop) ezShop).db.deleteEntireDB();
		((EZShop) ezShop).db.openConnection();
		((EZShop) ezShop).db.createUserTable();
		((EZShop) ezShop).db.createLoyaltyCardTable();
		((EZShop) ezShop).db.createCustomerTable();
		((EZShop) ezShop).db.closeConnection();
		((EZShop) ezShop).deleteAllUsers();
		((EZShop) ezShop).deleteAllCustomers();
		((EZShop) ezShop).deleteAllLoyaltyCards();

		ezShop.createUser("admin", "admin", "Administrator");
		assertThrows(UnauthorizedException.class, () -> ezShop.getAllCustomers());
		ezShop.login("admin", "admin");
		ezShop.getAllCustomers();
		
		
	}
	
	// createCard
	@Test
	public void createCardTest() throws SQLException, InvalidUsernameException, UnauthorizedException, InvalidRoleException, InvalidPasswordException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		((EZShop) ezShop).db.deleteEntireDB();
		((EZShop) ezShop).db.openConnection();
		((EZShop) ezShop).db.createUserTable();
		((EZShop) ezShop).db.createLoyaltyCardTable();
		((EZShop) ezShop).db.createCustomerTable();
		((EZShop) ezShop).db.closeConnection();
		((EZShop) ezShop).deleteAllUsers();
		((EZShop) ezShop).deleteAllCustomers();
		((EZShop) ezShop).deleteAllLoyaltyCards();

		ezShop.createUser("admin", "admin", "Administrator");
		assertThrows(UnauthorizedException.class, () -> ezShop.createCard());
		ezShop.login("admin", "admin");
		assertTrue(ezShop.createCard().matches("^[0-9]{10}$"));
		
	}
	
	// attachCardToCustomer
	@Test
	public void attachCardToCustomerTest() throws SQLException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidCustomerNameException, UnauthorizedException, InvalidCustomerIdException, InvalidCustomerCardException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		((EZShop) ezShop).db.deleteEntireDB();
		((EZShop) ezShop).db.openConnection();
		((EZShop) ezShop).db.createUserTable();
		((EZShop) ezShop).db.createLoyaltyCardTable();
		((EZShop) ezShop).db.createCustomerTable();
		((EZShop) ezShop).db.closeConnection();
		((EZShop) ezShop).deleteAllUsers();
		((EZShop) ezShop).deleteAllCustomers();
		((EZShop) ezShop).deleteAllLoyaltyCards();

		ezShop.createUser("admin", "admin", "Administrator");
		assertThrows(UnauthorizedException.class, () -> ezShop.attachCardToCustomer("", 1));
		ezShop.login("admin", "admin");
		assertThrows(InvalidCustomerCardException.class, () -> ezShop.attachCardToCustomer("", 1));
		assertThrows(InvalidCustomerCardException.class, () -> ezShop.attachCardToCustomer(null, 1));
		assertThrows(InvalidCustomerCardException.class, () -> ezShop.attachCardToCustomer("123", 1));
		
		assertThrows(InvalidCustomerIdException.class, () -> ezShop.attachCardToCustomer("1234567890", -1));
		assertThrows(InvalidCustomerIdException.class, () -> ezShop.attachCardToCustomer("1234567890", null));
		
		int id = ezShop.defineCustomer("c1");		
		((EZShop) ezShop).createCard("1234567890");
		
		assertTrue(ezShop.attachCardToCustomer("1234567890", id));
		assertFalse(ezShop.attachCardToCustomer("1234567890", id));
		assertFalse(ezShop.attachCardToCustomer("1234567890", id+1));

	}
	
	
	// modifyPointsOnCard
	
	@Test
	public void modifyPointsOnCardTest() throws SQLException, InvalidUsernameException, UnauthorizedException, InvalidRoleException, InvalidPasswordException, InvalidCustomerCardException, InvalidCustomerNameException, InvalidCustomerIdException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		((EZShop) ezShop).db.deleteEntireDB();
		((EZShop) ezShop).db.openConnection();
		((EZShop) ezShop).db.createUserTable();
		((EZShop) ezShop).db.createLoyaltyCardTable();
		((EZShop) ezShop).db.createCustomerTable();
		((EZShop) ezShop).db.closeConnection();
		((EZShop) ezShop).deleteAllUsers();
		((EZShop) ezShop).deleteAllCustomers();
		((EZShop) ezShop).deleteAllLoyaltyCards();

		ezShop.createUser("admin", "admin", "Administrator");
		assertThrows(UnauthorizedException.class, () -> ezShop.modifyPointsOnCard("1234567890", 5));
		ezShop.login("admin", "admin");
		assertThrows(InvalidCustomerCardException.class, () -> ezShop.modifyPointsOnCard("", 5));
		assertThrows(InvalidCustomerCardException.class, () -> ezShop.modifyPointsOnCard(null, 5));
		assertThrows(InvalidCustomerCardException.class, () -> ezShop.modifyPointsOnCard("123", 5));
		assertThrows(InvalidCustomerCardException.class, () -> ezShop.modifyPointsOnCard("", 5));
		
		assertFalse(ezShop.modifyPointsOnCard("1234567890", 5)); // no card with given code
		
		String newCard = ((EZShop) ezShop).createCard("1234567890");
		int id = ezShop.defineCustomer("c1");
		ezShop.attachCardToCustomer(newCard, id);
		
		assertTrue(ezShop.modifyPointsOnCard("1234567890", 5));
		assertEquals(ezShop.getCustomer(id).getPoints(), (Integer) 5);

		
	}
	
	
	

}
