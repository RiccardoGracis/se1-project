package it.polito.ezshop.integrationTests;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import it.polito.ezshop.data.*;
import it.polito.ezshop.exceptions.*;

/***** UC1 INTEGRATION TESTS *****/

public class UC1Scenarios {
	
	/*** Scenario 1-1 ***/
	@Test
	public void testScenario1_1() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException, InvalidProductIdException, InvalidLocationException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		//Precondition starts
		ezShop.reset();
		ezShop.createUser("shm", "SHM23", "ShopManager");
		ezShop.login("shm", "SHM23");
		//Precondition ends
		
		int id=ezShop.createProductType("Eggs","234829476238",2.0,"note1");
		assertTrue("Product Type not created",id>0);
		assertNull(ezShop.getProductTypeByBarCode("234829476238").getLocation());
		boolean retval=ezShop.updatePosition(id, "12-OW-0");
		assertTrue(retval);
		
		
		//Postconditions
		assertNotNull(ezShop.getProductTypeByBarCode("234829476238"));
		assertNotNull(ezShop.getProductTypeByBarCode("234829476238").getLocation());
		assertEquals(ezShop.getProductTypeByBarCode("234829476238").getBarCode(),"234829476238");
		assertEquals(ezShop.getProductTypeByBarCode("234829476238").getLocation(),"12-OW-0");
		
	}
	
	/*** Scenario 1-2 ***/
	@Test
	public void testScenario1_2() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException, InvalidProductIdException, InvalidLocationException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		//Preconditions start
		ezShop.reset();
		ezShop.createUser("shm", "SHM23", "ShopManager");
		ezShop.login("shm", "SHM23");
		int id=ezShop.createProductType("Eggs","234829476238",2.0,"note1");
		assertTrue("Product Type not created",id>0);
		assertNull(ezShop.getProductTypeByBarCode("234829476238").getLocation());
		//Preconditions end
		ProductType p=ezShop.getProductTypeByBarCode("234829476238");
		assertNotNull(p);
		assertEquals(p.getBarCode(),"234829476238");
		
		boolean retval=ezShop.updatePosition(p.getId(), "12-OW-0");
		assertTrue(retval);
		
		//PostCondition
		assertEquals(ezShop.getProductTypeByBarCode("234829476238").getLocation(),"12-OW-0");
		assertEquals(p.getLocation(),"12-OW-0");
		
	}
	
	/*** Scenario 1-3 ***/
	@Test
	public void testScenario1_3() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException, InvalidProductIdException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		//Preconditions start
		ezShop.reset();
		ezShop.createUser("admin", "admin", "Administrator");
		ezShop.login("admin", "admin");
		int id=ezShop.createProductType("Eggs","234829476238",2.0,"note1");
		assertTrue(id>0);
		assertEquals(ezShop.getProductTypeByBarCode("234829476238").getPricePerUnit(),2.01,0.1);
		//Preconditions end
		ProductType p=ezShop.getProductTypeByBarCode("234829476238");
		assertNotNull(p);
		assertEquals(p.getBarCode(),"234829476238");
		boolean retval= ezShop.updateProduct(p.getId(),"Eggs","234829476238",40.01,"note1");
		assertTrue(retval);
		
		//PostConditions
		assertEquals(ezShop.getProductTypeByBarCode("234829476238").getPricePerUnit(),40.011,0.01);
		assertEquals(p.getPricePerUnit(),40.011,0.01);
	}

}
