package it.polito.ezshop.integrationTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.sql.SQLException;

import org.junit.Test;

import it.polito.ezshop.data.CustomerImpl;
import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.InvalidCustomerCardException;
import it.polito.ezshop.exceptions.InvalidCustomerIdException;
import it.polito.ezshop.exceptions.InvalidCustomerNameException;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidRoleException;
import it.polito.ezshop.exceptions.InvalidUsernameException;
import it.polito.ezshop.exceptions.UnauthorizedException;

/*** UC4: Manage Customers and Cards ***/
public class UC4Scenarios {
	
	/*** Scenario 4-1: create customer record ***/
	@Test
	public void testScenario4_1() throws SQLException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidCustomerNameException, UnauthorizedException, InvalidCustomerIdException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		// Preconditions
		((EZShop) ezShop).db.deleteEntireDB();
		((EZShop) ezShop).db.openConnection();
		((EZShop) ezShop).db.createCustomerTable();
		((EZShop) ezShop).db.createUserTable();
		((EZShop) ezShop).db.closeConnection();
		((EZShop) ezShop).deleteAllCustomers();
		((EZShop) ezShop).deleteAllUsers();
		ezShop.createUser("admin", "admin", "Administrator");
		ezShop.login("admin", "admin");

		// Steps
		int id = ezShop.defineCustomer("c1");

		// Postconditions
		assertNotNull(ezShop.getCustomer(id));
	}
	
	/*** Scenario 4-2: attach loyalty card to customer ***/
	@Test
	public void testScenario4_2() throws SQLException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidCustomerNameException, UnauthorizedException, InvalidCustomerIdException, InvalidCustomerCardException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		// Preconditions
		((EZShop) ezShop).db.deleteEntireDB();
		((EZShop) ezShop).db.openConnection();
		((EZShop) ezShop).db.createLoyaltyCardTable();
		((EZShop) ezShop).db.createCustomerTable();
		((EZShop) ezShop).db.createUserTable();
		((EZShop) ezShop).db.closeConnection();
		((EZShop) ezShop).deleteAllCustomers();
		((EZShop) ezShop).deleteAllUsers();
		((EZShop) ezShop).deleteAllLoyaltyCards();
		ezShop.createUser("admin", "admin", "Administrator");
		ezShop.login("admin", "admin");
		int id = ezShop.defineCustomer("c1");

		// Steps
		String newCard = ezShop.createCard();
		ezShop.attachCardToCustomer(newCard, id);
		
		// Postconditions
		assertEquals(ezShop.getCustomer(id).getCustomerCard(), newCard);
		assertEquals(ezShop.getCustomer(id).getPoints(), (Integer) 0);
		
	}
	
	/*** Scenario 4-3: detach loyalty card from customer  ***/
	@Test
	public void testScenario4_3() throws SQLException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidCustomerNameException, UnauthorizedException, InvalidCustomerIdException, InvalidCustomerCardException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		// Preconditions
		((EZShop) ezShop).db.deleteEntireDB();
		((EZShop) ezShop).db.openConnection();
		((EZShop) ezShop).db.createLoyaltyCardTable();
		((EZShop) ezShop).db.createCustomerTable();
		((EZShop) ezShop).db.createUserTable();
		((EZShop) ezShop).db.closeConnection();
		((EZShop) ezShop).deleteAllCustomers();
		((EZShop) ezShop).deleteAllUsers();
		((EZShop) ezShop).deleteAllLoyaltyCards();
		ezShop.createUser("admin", "admin", "Administrator");
		ezShop.login("admin", "admin");
		int id = ezShop.defineCustomer("c1");
		String newCard = ezShop.createCard();
		ezShop.attachCardToCustomer(newCard, id);

		// Steps
		((CustomerImpl) ezShop.getCustomer(id)).detachCardFromCustomer();
		
		// Postconditions
		assertNull(ezShop.getCustomer(id).getCustomerCard());
		
	}
	
	/*** Scenario 4-4: update customer record  ***/
	@Test
	public void testScenario4_4() throws SQLException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidCustomerNameException, UnauthorizedException, InvalidCustomerIdException, InvalidCustomerCardException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		// Preconditions
		((EZShop) ezShop).db.deleteEntireDB();
		((EZShop) ezShop).db.openConnection();
		((EZShop) ezShop).db.createLoyaltyCardTable();
		((EZShop) ezShop).db.createCustomerTable();
		((EZShop) ezShop).db.createUserTable();
		((EZShop) ezShop).db.closeConnection();
		((EZShop) ezShop).deleteAllCustomers();
		((EZShop) ezShop).deleteAllUsers();
		((EZShop) ezShop).deleteAllLoyaltyCards();
		ezShop.createUser("admin", "admin", "Administrator");
		ezShop.login("admin", "admin");
		int id = ezShop.defineCustomer("c1");
		String newCard = ezShop.createCard();
		ezShop.attachCardToCustomer(newCard, id);

		// Steps
		ezShop.modifyCustomer(id, "c2", null);
		
		// Postconditions
		assertEquals(ezShop.getCustomer(id).getCustomerCard(), newCard);
		assertEquals(ezShop.getCustomer(id).getCustomerName(), "c2");
		
//		ezShop.modifyCustomer(id, "c2", "");
//		assertNull(ezShop.getCustomer(id).getCustomerCard());
//		
//		ezShop.modifyCustomer(id, "c2", "1234567890");
//		assertEquals(ezShop.getCustomer(id).getCustomerCard(), "1234567890");
		
	}

}
