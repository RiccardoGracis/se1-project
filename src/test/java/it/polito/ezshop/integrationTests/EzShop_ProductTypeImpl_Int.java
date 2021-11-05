package it.polito.ezshop.integrationTests;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import it.polito.ezshop.data.*;
import it.polito.ezshop.exceptions.*;

public class EzShop_ProductTypeImpl_Int {
	
				/**FR3**/
	
	/***** TEST API CREATE PRODUCT TYPE  ****/
	
	@Test
	public void testUnauthorized() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.createUser("cashier", "cm32", "Cashier");
		assertThrows(UnauthorizedException.class,
				()->{ezShop.createProductType("Eggs","234829476238",2.0,"note1");
		});
		ezShop.login("cashier", "cm32");
		assertThrows(UnauthorizedException.class,
				()->{ezShop.createProductType("Eggs","234829476238",2.0,"note1");
		});
	}
	
	@Test
	public void testInvalidPricePerUnit() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.createUser("admin", "admin", "Administrator");
		ezShop.login("admin", "admin");
		assertThrows(InvalidPricePerUnitException.class,
				()->{ezShop.createProductType("Eggs","234829476238",-0.01,"note1");
		});
		assertThrows(InvalidPricePerUnitException.class,
				()->{ezShop.createProductType("Eggs","234829476238",0,"note1");
		});
		assertThrows(InvalidPricePerUnitException.class,
				()->{ezShop.createProductType("Eggs","234829476238",-10.97,"note1");
		});
		
	}
	
	@Test
	public void testInvalidProductCode() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.createUser("admin", "admin", "Administrator");
		ezShop.login("admin", "admin");
		assertThrows(InvalidProductCodeException.class,
				()->{ezShop.createProductType("Eggs","",2.0,"note1");
		});
		assertThrows(InvalidProductCodeException.class,
				()->{ezShop.createProductType("Eggs",null,2.0,"note1");
		});
		//Less than 12 digits
		assertThrows(InvalidProductCodeException.class,
				()->{ezShop.createProductType("Eggs","123456",2.0,"note1");
		});
		//More than 14
		assertThrows(InvalidProductCodeException.class,
				()->{ezShop.createProductType("Eggs","12345678901234567",2.0,"note1");
		});
		//NaN barcode
		assertThrows(InvalidProductCodeException.class,
				()->{ezShop.createProductType("Eggs","123456789123a",2.0,"note1");
		});
		assertThrows(InvalidProductCodeException.class,
				()->{ezShop.createProductType("Eggs","123HIske232444",2.0,"note1");
		});
		//Invalid
		assertThrows(InvalidProductCodeException.class,
				()->{ezShop.createProductType("Eggs","123456789123",2.0,"note1");
		});
		
	}
	
	@Test
	public void testInvalidProductDescription() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.createUser("shm", "SHM23", "ShopManager");
		ezShop.login("shm", "SHM23");
		
		assertThrows(InvalidProductDescriptionException.class,
				()->{ezShop.createProductType("","234829476238",2.01,"note1");
		});
		
		assertThrows(InvalidProductDescriptionException.class,
				()->{ezShop.createProductType(null,"234829476238",2.01,"note1");
		});
		
	}
	
	@Test
	public void testNegRetValue() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.reset();
		ezShop.createUser("shm", "SHM23", "ShopManager");
		ezShop.login("shm", "SHM23");
		int retval=ezShop.createProductType("Eggs","234829476238",2.0,"note1");
		assertTrue(retval>0);
		retval=ezShop.createProductType("Flowers","234829476238",5.0,"note2");
		assertEquals(retval,-1);
		
	}
	
	@Test
	public void testCorrectValue() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.reset();
		ezShop.createUser("shm", "SHM23", "ShopManager");
		ezShop.login("shm", "SHM23");
		int retval=ezShop.createProductType("Eggs","234829476238",2.0,"note1");
		assertTrue(retval>0);
		
	}

	/***** TEST API updateProduct  ****/
	
	@Test
	public void testUpdUnauthorized() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.createUser("cashier", "cm32", "Cashier");
		assertThrows(UnauthorizedException.class,
				()->{ezShop.updateProduct(2,"Eggs","234829476238",2.0,"note1");
		});
		ezShop.login("cashier", "cm32");
		assertThrows(UnauthorizedException.class,
				()->{ezShop.updateProduct(2,"Eggs","234829476238",2.0,"note1");
		});
	}
	
	@Test
	public void testUpdInvalidPricePerUnit() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.createUser("admin", "admin", "Administrator");
		ezShop.login("admin", "admin");
		assertThrows(InvalidPricePerUnitException.class,
				()->{ezShop.updateProduct(2,"Eggs","234829476238",-0.01,"note1");
		});
		assertThrows(InvalidPricePerUnitException.class,
				()->{ezShop.updateProduct(2,"Eggs","234829476238",0,"note1");
		});
		assertThrows(InvalidPricePerUnitException.class,
				()->{ezShop.updateProduct(2,"Eggs","234829476238",-10.97,"note1");
		});
		
	}
	
	@Test
	public void testUpdInvalidProductCode() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.createUser("admin", "admin", "Administrator");
		ezShop.login("admin", "admin");
		assertThrows(InvalidProductCodeException.class,
				()->{ezShop.updateProduct(2,"Eggs","",2.0,"note1");
		});
		assertThrows(InvalidProductCodeException.class,
				()->{ezShop.updateProduct(2,"Eggs",null,2.0,"note1");
		});
		//Less than 12 digits
		assertThrows(InvalidProductCodeException.class,
				()->{ezShop.updateProduct(2,"Eggs","123456",2.0,"note1");
		});
		//More than 14
		assertThrows(InvalidProductCodeException.class,
				()->{ezShop.updateProduct(2,"Eggs","12345678901234567",2.0,"note1");
		});
		//NaN barcode
		assertThrows(InvalidProductCodeException.class,
				()->{ezShop.updateProduct(2,"Eggs","123456789123a",2.0,"note1");
		});
		assertThrows(InvalidProductCodeException.class,
				()->{ezShop.updateProduct(2,"Eggs","123HIske232444",2.0,"note1");
		});
		//Invalid
		assertThrows(InvalidProductCodeException.class,
				()->{ezShop.updateProduct(2,"Eggs","123456789123",2.0,"note1");
		});
		
	}
	
	@Test
	public void testUpdInvalidProductDescription() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.createUser("shm", "SHM23", "ShopManager");
		ezShop.login("shm", "SHM23");
		
		assertThrows(InvalidProductDescriptionException.class,
				()->{ezShop.updateProduct(2,"","234829476238",2.01,"note1");
		});
		
		assertThrows(InvalidProductDescriptionException.class,
				()->{ezShop.updateProduct(2,null,"234829476238",2.01,"note1");
		});
		
	}
	
	@Test
	public void testUpdInvalidProductId() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.createUser("admin", "admin", "Administrator");
		ezShop.login("admin", "admin");
		assertThrows(InvalidProductIdException.class,
				()->{ezShop.updateProduct(-2,"Eggs","234829476238",2.01,"note1");
		});
		
		assertThrows(InvalidProductIdException.class,
				()->{ezShop.updateProduct(0,"Eggs","234829476238",2.01,"note1");
		});
		
		assertThrows(InvalidProductIdException.class,
				()->{ezShop.updateProduct(null,"Eggs","234829476238",2.01,"note1");
		});
		
	}
	
	@Test
	public void testUpdFalse() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.reset();
		ezShop.createUser("admin", "admin", "Administrator");
		ezShop.login("admin", "admin");
		//No product with this id
		boolean retval= ezShop.updateProduct(5,"Eggs","234829476238",2.01,"note1");
		assertFalse(retval);
		int id=ezShop.createProductType("Eggs","234829476238",2.0,"note1");
		ezShop.createProductType("Water","57643947623857",4.0,"note2");
		//Another product has the same barcode
		retval= ezShop.updateProduct(5,"Eggs","57643947623857",2.01,"note1");
		assertFalse(retval);
		//No product with this id
		retval= ezShop.updateProduct(180,"Eggs","234829476238",2.01,"note1");
		assertFalse(retval);
	}
	
	@Test
	public void testUpdTrue() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException, InvalidProductIdException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.reset();
		ezShop.createUser("admin", "admin", "Administrator");
		ezShop.login("admin", "admin");
		int id=ezShop.createProductType("Eggs","234829476238",2.0,"note1");
		boolean retval= ezShop.updateProduct(id,"Eggs","234829476238",2.01,"note1");
		assertTrue(retval);
	}
	
	/****** API deleteProductType  **********/
	@Test
	public void testDeleteUnauthorized() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.createUser("cashier", "cm32", "Cashier");
		assertThrows(UnauthorizedException.class,
				()->{ezShop.deleteProductType(2);
		});
		ezShop.login("cashier", "cm32");
		assertThrows(UnauthorizedException.class,
				()->{ezShop.deleteProductType(2);
		});
	}
	
	@Test
	public void testDeleteInvalidProductId() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.createUser("admin", "admin", "Administrator");
		ezShop.login("admin", "admin");
		assertThrows(InvalidProductIdException.class,
				()->{ezShop.deleteProductType(-2);
		});
		
		assertThrows(InvalidProductIdException.class,
				()->{ezShop.deleteProductType(0);
		});
		
		assertThrows(InvalidProductIdException.class,
				()->{ezShop.deleteProductType(null);
		});
		
	}
	
	@Test
	public void testDeleteFalse() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidProductIdException, UnauthorizedException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.reset();
		ezShop.createUser("admin", "admin", "Administrator");
		ezShop.login("admin", "admin");
		//Id not existing
		boolean retval = ezShop.deleteProductType(20);
		assertFalse(retval);
		ezShop.createProductType("Eggs","234829476238",2.0,"note1");
		retval = ezShop.deleteProductType(20);
		assertFalse(retval);
	}
	
	@Test
	public void testDeleteTrue() throws InvalidProductIdException, UnauthorizedException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.reset();
		ezShop.createUser("shm", "SHM23", "ShopManager");
		ezShop.login("shm", "SHM23");
		int id=ezShop.createProductType("Eggs","234829476238",2.0,"note1");
		assertTrue("Product Type not created",id>0);
		boolean retval = ezShop.deleteProductType(id);
		assertTrue(retval);
		
	}
	
	/**** API getAllProductTypes  ******/
	@Test
	public void testGetAllUnauthorized () throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		
		assertThrows(UnauthorizedException.class,
				()->{ezShop.getAllProductTypes();
		});
	}
	
	@Test
	public void testGetAllEmpty () throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, UnauthorizedException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.reset();
		ezShop.createUser("shm", "SHM23", "ShopManager");
		ezShop.login("shm", "SHM23");
		List<ProductType> list= ezShop.getAllProductTypes();
		assertTrue(list.isEmpty());
	}
	
	@Test
	public void testGetAllSuccess () throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, UnauthorizedException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.reset();
		ezShop.createUser("admin", "admin", "Administrator");
		ezShop.login("admin", "admin");
		int id=ezShop.createProductType("Eggs","234829476238",2.0,"note1");
		assertTrue("Product Type not created",id>0);
		List<ProductType> list= ezShop.getAllProductTypes();
		assertEquals(list.size(),1);
		id=ezShop.createProductType("Water","57643947623857",4.0,"note2");
		assertTrue("Product Type not created",id>0);
		list= ezShop.getAllProductTypes();
		assertEquals(list.size(),2);
	}
	
	/******** getProductTypeByBarCode *******/
	@Test
	public void testByCodeUnauthorized () throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.createUser("cashier", "cm32", "Cashier");
		assertThrows(UnauthorizedException.class,
				()->{ezShop.getProductTypeByBarCode("57643947623857");
		});
		ezShop.login("cashier", "cm32");
		assertThrows(UnauthorizedException.class,
				()->{ezShop.getProductTypeByBarCode("57643947623857");
		});
	}
	
	@Test
	public void testByCodeInvalidProductCode() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.createUser("admin", "admin", "Administrator");
		ezShop.login("admin", "admin");
		assertThrows(InvalidProductCodeException.class,
				()->{ezShop.getProductTypeByBarCode("");
		});
		assertThrows(InvalidProductCodeException.class,
				()->{ezShop.getProductTypeByBarCode(null);
		});
		//Less than 12 digits
		assertThrows(InvalidProductCodeException.class,
				()->{ezShop.getProductTypeByBarCode("123456");
		});
		//More than 14
		assertThrows(InvalidProductCodeException.class,
				()->{ezShop.getProductTypeByBarCode("12345678901234567");
		});
		//NaN barcode
		assertThrows(InvalidProductCodeException.class,
				()->{ezShop.getProductTypeByBarCode("123456789123a");
		});
		assertThrows(InvalidProductCodeException.class,
				()->{ezShop.getProductTypeByBarCode("123HIske232444");
		});
		//Invalid
		assertThrows(InvalidProductCodeException.class,
				()->{ezShop.getProductTypeByBarCode("123456789123");
		});
		
	}
	
	@Test
	public void testByCodeNull() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidProductCodeException, UnauthorizedException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.reset();
		ezShop.createUser("shm", "SHM23", "ShopManager");
		ezShop.login("shm", "SHM23");
		ProductType p =ezShop.getProductTypeByBarCode("234829476238");
		assertNull(p);
	}
	
	@Test
	public void testByCodeSuccess() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidProductCodeException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.reset();
		ezShop.createUser("admin", "admin", "Administrator");
		ezShop.login("admin", "admin");
		int id=ezShop.createProductType("Eggs","234829476238",2.0,"note1");
		assertTrue("Product Type not created",id>0);
		id=ezShop.createProductType("Water","57643947623857",4.0,"note2");
		assertTrue("Product Type not created",id>0);
		ProductType p =ezShop.getProductTypeByBarCode("234829476238");
		assertNotNull(p);
		assertEquals(p.getBarCode(),"234829476238");
		p =ezShop.getProductTypeByBarCode("57643947623857");
		assertNotNull(p);
		assertEquals(p.getBarCode(),"57643947623857");
	}
	
	/******* getProductTypesByDescription ******/
	@Test
	public void testByDescriptionUnauthorized () throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.createUser("cashier", "cm32", "Cashier");
		assertThrows(UnauthorizedException.class,
				()->{ezShop.getProductTypesByDescription("Eggs");
		});
		ezShop.login("cashier", "cm32");
		assertThrows(UnauthorizedException.class,
				()->{ezShop.getProductTypesByDescription("Eggs");
		});
	}
	
	@Test
	public void testGetByCodeEmpty () throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, UnauthorizedException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.reset();
		ezShop.createUser("shm", "SHM23", "ShopManager");
		ezShop.login("shm", "SHM23");
		List<ProductType> list= ezShop.getProductTypesByDescription("");
		assertTrue(list.isEmpty());
		list= ezShop.getProductTypesByDescription(null);
		assertTrue(list.isEmpty());
		list= ezShop.getProductTypesByDescription("Eggs");
		assertTrue(list.isEmpty());
	}
	
	@Test
	public void testGetByCodeSuccess () throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, UnauthorizedException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.reset();
		ezShop.createUser("admin", "admin", "Administrator");
		ezShop.login("admin", "admin");
		int id=ezShop.createProductType("Still Water","234829476238",2.0,"note1");
		assertTrue("Product Type not created",id>0);
		id=ezShop.createProductType("Mineral Water","57643947623857",4.0,"note2");
		assertTrue("Product Type not created",id>0);
		
		List<ProductType> list= ezShop.getProductTypesByDescription("Water");
		assertFalse(list.isEmpty());
		assertEquals(list.size(),2);
		list= ezShop.getProductTypesByDescription("nera");
		assertFalse(list.isEmpty());
		assertEquals(list.size(),1);
		list= ezShop.getProductTypesByDescription("ral");
		assertFalse(list.isEmpty());
		assertEquals(list.size(),1);
		//Begin of string
		list= ezShop.getProductTypesByDescription("M");
		assertFalse(list.isEmpty());
		assertEquals(list.size(),1);
	}

}
