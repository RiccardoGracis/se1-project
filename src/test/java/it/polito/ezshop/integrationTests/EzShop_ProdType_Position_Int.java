package it.polito.ezshop.integrationTests;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import it.polito.ezshop.data.*;
import it.polito.ezshop.exceptions.*;

public class EzShop_ProdType_Position_Int {
	
				/**FR4**/
	
	/***** TEST API updatePosition  ****/
	@Test
	public void testUpdPosUnauthorized() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.createUser("cashier", "cm32", "Cashier");
		assertThrows(UnauthorizedException.class,
				()->{ezShop.updatePosition(2,"3-BC-12");
		});
		ezShop.login("cashier", "cm32");
		assertThrows(UnauthorizedException.class,
				()->{ezShop.updatePosition(2,"3-BC-12");
		});
	}
	
	@Test
	public void testUpdPosInvalidProductId() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.createUser("admin", "admin", "Administrator");
		ezShop.login("admin", "admin");
		assertThrows(InvalidProductIdException.class,
				()->{ezShop.updatePosition(-2,"3-BC-12");
		});
		
		assertThrows(InvalidProductIdException.class,
				()->{ezShop.updatePosition(0,"3-BC-12");
		});
		
		assertThrows(InvalidProductIdException.class,
				()->{ezShop.updatePosition(null,"3-BC-12");
		});
		
	}
	
	@Test
	public void testUpdPosInvalidLocation() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.reset();
		ezShop.createUser("admin", "admin", "Administrator");
		ezShop.login("admin", "admin");
		//Bad aisle
		assertThrows(InvalidLocationException.class,
				()->{ezShop.updatePosition(3,"3a-BC-12");
		});
		//Bad rack
		assertThrows(InvalidLocationException.class,
				()->{ezShop.updatePosition(4,"3-BC2-12");
		});
		//Bad level
		assertThrows(InvalidLocationException.class,
				()->{ezShop.updatePosition(6,"3-BC-12a");
		});
		//BadFormat1
		assertThrows(InvalidLocationException.class,
				()->{ezShop.updatePosition(6,"3BC-12");
		});
		//BadFormat2
		assertThrows(InvalidLocationException.class,
				()->{ezShop.updatePosition(6,"3-BC12");
		});
		//BadFormat3
		assertThrows(InvalidLocationException.class,
				()->{ezShop.updatePosition(6,"3BC12");
		});
		//All Numbers
		assertThrows(InvalidLocationException.class,
				()->{ezShop.updatePosition(6,"3-11-12");
		});
	}
	
	@Test
	public void testUpdPosTrue () throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException, InvalidProductIdException, InvalidLocationException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.reset();
		ezShop.createUser("shm", "SHM23", "ShopManager");
		ezShop.login("shm", "SHM23");
		int id=ezShop.createProductType("Eggs","234829476238",2.0,"note1");
		assertTrue("Product Type not created",id>0);
		assertNull(ezShop.getProductTypeByBarCode("234829476238").getLocation());
		boolean retval=ezShop.updatePosition(id, "12-OW-0");
		assertTrue(retval);
		assertEquals(ezShop.getProductTypeByBarCode("234829476238").getLocation(),"12-OW-0");
		retval=ezShop.updatePosition(id, "");
		assertTrue(retval);
		assertNull(ezShop.getProductTypeByBarCode("234829476238").getLocation());
		retval=ezShop.updatePosition(id, "12-OW-0");
		assertTrue(retval);
		assertNotNull(ezShop.getProductTypeByBarCode("234829476238").getLocation());
		retval=ezShop.updatePosition(id, null);
		assertTrue(retval);
		assertNull(ezShop.getProductTypeByBarCode("234829476238").getLocation());
	}
	
	@Test
	public void testUpdPosFalse() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidProductIdException, InvalidLocationException, UnauthorizedException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.reset();
		ezShop.createUser("shm", "SHM23", "ShopManager");
		ezShop.login("shm", "SHM23");
		//Product Not Exists
		boolean retval=ezShop.updatePosition(13, "12-OW-0");
		assertFalse(retval);
		
		
		int id1=ezShop.createProductType("Still Water","234829476238",2.0,"note1");
		assertTrue("Product Type not created",id1>0);
		
		retval=ezShop.updatePosition(id1, "63-C-99");
		assertTrue(retval);
		
		int id2=ezShop.createProductType("Mineral Water","57643947623857",4.0,"note2");
		assertTrue("Product Type not created",id2>0);
		
		//Position already assigned
		retval=ezShop.updatePosition(id2, "63-C-99");
		assertFalse(retval);
		
		//Clear Position id1
		retval=ezShop.updatePosition(id1, "");
		assertTrue(retval);
		
		//Now update should have success
		retval=ezShop.updatePosition(id2, "63-C-99");
		assertTrue(retval);
	}
	
	/***** TEST API updateQuantity  ****/
	@Test
	public void testUpdQTYUnauthorized () throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.createUser("cashier", "cm32", "Cashier");
		assertThrows(UnauthorizedException.class,
				()->{ezShop.updateQuantity(2,10);
		});
		ezShop.login("cashier", "cm32");
		assertThrows(UnauthorizedException.class,
				()->{ezShop.updateQuantity(2,10);
		});
		
	}
	
	@Test
	public void testUpdQTYInvalidProductId() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.createUser("admin", "admin", "Administrator");
		ezShop.login("admin", "admin");
		assertThrows(InvalidProductIdException.class,
				()->{ezShop.updateQuantity(-2,10);
		});
		
		assertThrows(InvalidProductIdException.class,
				()->{ezShop.updateQuantity(0,10);
		});
		
		assertThrows(InvalidProductIdException.class,
				()->{ezShop.updateQuantity(null,10);
		});
		
	}
	
	@Test
	public void testUpdQTYFalse() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException, InvalidProductIdException, InvalidLocationException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.reset();
		ezShop.createUser("shm", "SHM23", "ShopManager");
		ezShop.login("shm", "SHM23");
		int id=ezShop.createProductType("Eggs","234829476238",2.0,"note1");
		assertTrue("Product Type not created",id>0);
		
		//No location
		boolean retval=ezShop.updateQuantity(id, 10);
		assertFalse(retval);
		
		retval=ezShop.updatePosition(id, "12-OW-0");
		assertTrue(retval);
		//No product with this id
		retval=ezShop.updateQuantity(id+1, 12);
		assertFalse(retval);
		//<toBeAdded> is negative and the resulting amount would be negative too
		retval=ezShop.updateQuantity(id, -1);
		assertFalse(retval);
		
		assertEquals(ezShop.getProductTypeByBarCode("234829476238").getQuantity(),Integer.valueOf(0));
		retval=ezShop.updateQuantity(id, 10);
		assertTrue(retval);
		
		retval=ezShop.updateQuantity(id, -11);
		assertFalse(retval);
	}
	
	@Test
	public void testUpdQTYTrue() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException, InvalidProductIdException, InvalidLocationException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.reset();
		ezShop.createUser("shm", "SHM23", "ShopManager");
		ezShop.login("shm", "SHM23");
		int id=ezShop.createProductType("Eggs","234829476238",2.0,"note1");
		assertTrue("Product Type not created",id>0);
		
		boolean retval=ezShop.updatePosition(id, "12-OW-0");
		assertTrue(retval);
		
		
		assertEquals(ezShop.getProductTypeByBarCode("234829476238").getQuantity(),Integer.valueOf(0));
		retval=ezShop.updateQuantity(id, 10);
		assertTrue(retval);
		
		retval=ezShop.updateQuantity(id, -1);
		assertTrue(retval);
		
		retval=ezShop.updateQuantity(id, -9);
		assertTrue(retval);
	}
	
	

}
