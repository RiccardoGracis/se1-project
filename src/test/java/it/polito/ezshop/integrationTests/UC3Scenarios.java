package it.polito.ezshop.integrationTests;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import it.polito.ezshop.data.*;
import it.polito.ezshop.exceptions.*;

/***** UC3 INTEGRATION TESTS *****/

public class UC3Scenarios {
	
	/*** Scenario 3-1 ***/
	@Test
	public void testScenario3_1() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException, InvalidProductDescriptionException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		//Precondition starts
		ezShop.reset();
		ezShop.createUser("michael92", "SM57bcd!?", "ShopManager");
		ezShop.login("michael92", "SM57bcd!?");
		int retval=ezShop.createProductType("Eggs","234829476238",2.0,"note1");
		assertTrue(retval>0);
		//Balance 0 and qty 0
		assertEquals(ezShop.computeBalance(),0.00,0.1);
		assertEquals(ezShop.getProductTypeByBarCode("234829476238").getQuantity(),Integer.valueOf(0));
		//Precondition ends
		
		
		retval=ezShop.issueOrder("234829476238",10,2.50);
		assertTrue(retval>0);
		
		////PostConditions
		assertEquals(ezShop.computeBalance(),0.00,0.1);
		assertEquals(ezShop.getProductTypeByBarCode("234829476238").getQuantity(),Integer.valueOf(0));
		
	}
	
	/*** Scenario 3-2 ***/
	@Test
	public void testScenario3_2() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidOrderIdException, UnauthorizedException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidQuantityException, InvalidProductIdException, InvalidLocationException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		//Precondition starts
		ezShop.reset();
		ezShop.createUser("michael92", "SM57bcd!?", "ShopManager");
		ezShop.login("michael92", "SM57bcd!?");
		
		
		int pid=ezShop.createProductType("Eggs","234829476238",2.0,"note1");
		assertTrue(pid>0);
		
		int id=ezShop.issueOrder("234829476238",10,2.50);
		assertTrue(id>0);
		
		ezShop.recordBalanceUpdate(25.00);
		assertEquals(ezShop.computeBalance(),25.00,0.1);
		//Precondition ends
		
		
		boolean retval=ezShop.payOrder(id);
		assertTrue(retval);
		
		//PostConditions
		assertEquals(ezShop.computeBalance(),0.00,0.1);
		assertEquals(ezShop.getProductTypeByBarCode("234829476238").getQuantity(),Integer.valueOf(0));
		
	}
	
	/*** Scenario 3-3 ***/
	@Test
	public void testScenario3_3() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidProductIdException, InvalidLocationException, UnauthorizedException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidQuantityException, InvalidOrderIdException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		//Precondition starts
		ezShop.reset();
		ezShop.createUser("shm", "SHM23", "ShopManager");
		ezShop.login("shm", "SHM23");
		
		int pid=ezShop.createProductType("Eggs","234829476238",2.0,"note1");
		assertTrue(pid>0);
		//System.out.println(ezShop.updatePosition(pid, "33-H-12"));
		assertTrue(ezShop.updatePosition(pid, "33-H-12"));
		
		//Assert QTY is 0
		assertEquals(ezShop.getProductTypeByBarCode("234829476238").getQuantity(),Integer.valueOf(0));
		
		int id=ezShop.issueOrder("234829476238",10,3.50);
		assertTrue(id>0);
		
		ezShop.recordBalanceUpdate(40.00);
		assertEquals(ezShop.computeBalance(),40.00,0.1);
		
		//Pay Order
		assertTrue(ezShop.payOrder(id));
		//Preconditions end
		
		assertTrue(ezShop.recordOrderArrival(id));
		
		//PostConditions
		//Assert QTY is 10
		assertEquals(ezShop.getProductTypeByBarCode("234829476238").getQuantity(),Integer.valueOf(10));
		
	}
}
