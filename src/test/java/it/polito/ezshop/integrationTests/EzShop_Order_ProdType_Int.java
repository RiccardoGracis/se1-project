package it.polito.ezshop.integrationTests;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import it.polito.ezshop.data.*;
import it.polito.ezshop.exceptions.*;

public class EzShop_Order_ProdType_Int {
	
	/*** API issueOrder ***/
	
	@Test
	public void testIssueUnauthorized() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.createUser("cashier", "cm32", "Cashier");
		assertThrows(UnauthorizedException.class,
				()->{ezShop.issueOrder("234829476238",10,2.50);
		});
		ezShop.login("cashier", "cm32");
		assertThrows(UnauthorizedException.class,
				()->{ezShop.issueOrder("234829476238",10,2.50);
		});
	}
	
	@Test
	public void testIssueInvalidPPU() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.reset();
		ezShop.createUser("admin", "admin", "Administrator");
		ezShop.login("admin", "admin");
		//Negative
		assertThrows(InvalidPricePerUnitException.class,
				()->{ezShop.issueOrder("234829476238",10,-2.50);
		});
		
		//0
		assertThrows(InvalidPricePerUnitException.class,
				()->{ezShop.issueOrder("234829476238",10,0);
		});
	}
	
	@Test
	public void testIssueInvalidQTY() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.reset();
		ezShop.createUser("admin", "admin", "Administrator");
		ezShop.login("admin", "admin");
		//Negative
		assertThrows(InvalidQuantityException.class,
				()->{ezShop.issueOrder("234829476238",-10,2.50);
		});
		
		//0
		assertThrows(InvalidQuantityException.class,
				()->{ezShop.issueOrder("234829476238",0,20.0);
		});
	}
	
	@Test
	public void testIssueInvalidProductCode() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.reset();
		ezShop.createUser("shm", "SHM23", "ShopManager");
		ezShop.login("shm", "SHM23");
		assertThrows(InvalidProductCodeException.class,
				()->{ezShop.issueOrder("",2,3.50);
		});
		assertThrows(InvalidProductCodeException.class,
				()->{ezShop.issueOrder(null,2,3.50);
		});
		//Less than 12 digits
		assertThrows(InvalidProductCodeException.class,
				()->{ezShop.issueOrder("123456",2,3.50);
		});
		//More than 14
		assertThrows(InvalidProductCodeException.class,
				()->{ezShop.issueOrder("12345678901234567",2,3.50);
		});
		//NaN barcode
		assertThrows(InvalidProductCodeException.class,
				()->{ezShop.issueOrder("123456789123a",2,3.50);
		});
		assertThrows(InvalidProductCodeException.class,
				()->{ezShop.issueOrder("123HIske232444",2,3.50);
		});
		//Invalid
		assertThrows(InvalidProductCodeException.class,
				()->{ezShop.issueOrder("123456789123",2,3.50);
		});
	}
	
	@Test
	public void testIssueInvalid() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.reset();
		ezShop.createUser("shm", "SHM23", "ShopManager");
		ezShop.login("shm", "SHM23");
		//Product do not exists
		int retval=ezShop.issueOrder("234829476238",10,2.50);
		assertEquals(retval,-1);
	}
	
	@Test
	public void testIssueValid() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException, InvalidProductDescriptionException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.reset();
		ezShop.createUser("admin", "admin", "Administrator");
		ezShop.login("admin", "admin");
		int retval=ezShop.createProductType("Eggs","234829476238",2.0,"note1");
		assertTrue(retval>0);
		retval=ezShop.issueOrder("234829476238",10,2.50);
		assertTrue(retval>0);
		retval=ezShop.issueOrder("234829476238",10,20.50);
		assertTrue(retval>0);
	}
	
	/*** API payOrderFor ***/
	
	@Test
	public void testPayOrderForUnauthorized() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.createUser("cashier", "cm32", "Cashier");
		assertThrows(UnauthorizedException.class,
				()->{ezShop.payOrderFor("234829476238",10,2.50);
		});
		ezShop.login("cashier", "cm32");
		assertThrows(UnauthorizedException.class,
				()->{ezShop.payOrderFor("234829476238",10,2.50);
		});
	}
	
	@Test
	public void testPayOrderForInvalidPPU() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.reset();
		ezShop.createUser("admin", "admin", "Administrator");
		ezShop.login("admin", "admin");
		//Negative
		assertThrows(InvalidPricePerUnitException.class,
				()->{ezShop.payOrderFor("234829476238",10,-2.50);
		});
		
		//0
		assertThrows(InvalidPricePerUnitException.class,
				()->{ezShop.payOrderFor("234829476238",10,0);
		});
	}
	
	@Test
	public void testPayOrderForInvalidQTY() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.reset();
		ezShop.createUser("admin", "admin", "Administrator");
		ezShop.login("admin", "admin");
		//Negative
		assertThrows(InvalidQuantityException.class,
				()->{ezShop.payOrderFor("234829476238",-10,2.50);
		});
		
		//0
		assertThrows(InvalidQuantityException.class,
				()->{ezShop.payOrderFor("234829476238",0,20.0);
		});
	}
	
	@Test
	public void testPayOrderForInvalidProductCode() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.reset();
		ezShop.createUser("shm", "SHM23", "ShopManager");
		ezShop.login("shm", "SHM23");
		assertThrows(InvalidProductCodeException.class,
				()->{ezShop.payOrderFor("",2,3.50);
		});
		assertThrows(InvalidProductCodeException.class,
				()->{ezShop.payOrderFor(null,2,3.50);
		});
		//Less than 12 digits
		assertThrows(InvalidProductCodeException.class,
				()->{ezShop.payOrderFor("123456",2,3.50);
		});
		//More than 14
		assertThrows(InvalidProductCodeException.class,
				()->{ezShop.payOrderFor("12345678901234567",2,3.50);
		});
		//NaN barcode
		assertThrows(InvalidProductCodeException.class,
				()->{ezShop.payOrderFor("123456789123a",2,3.50);
		});
		assertThrows(InvalidProductCodeException.class,
				()->{ezShop.payOrderFor("123HIske232444",2,3.50);
		});
		//Invalid
		assertThrows(InvalidProductCodeException.class,
				()->{ezShop.payOrderFor("123456789123",2,3.50);
		});
	}
	
	@Test
	public void testPayOrderForInvalid() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException, InvalidProductDescriptionException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.reset();
		ezShop.createUser("shm", "SHM23", "ShopManager");
		ezShop.login("shm", "SHM23");
		//Product do not exists
		int retval=ezShop.payOrderFor("234829476238",10,2.50);
		assertEquals(retval,-1);
		retval=ezShop.createProductType("Eggs","234829476238",2.0,"note1");
		assertTrue(retval>0);
		//Not enough money in balance
		retval=ezShop.payOrderFor("234829476238",10,2.50);
		assertEquals(retval,-1);
		
	}
	
	@Test
	public void testPayOrderForValid() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException, InvalidProductDescriptionException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.reset();
		ezShop.createUser("shm", "SHM23", "ShopManager");
		ezShop.login("shm", "SHM23");
		int retval=ezShop.createProductType("Eggs","234829476238",2.0,"note1");
		assertTrue(retval>0);
		
		ezShop.recordBalanceUpdate(25.00);
		assertEquals(ezShop.computeBalance(),25.00,0.1);
		
		retval=ezShop.payOrderFor("234829476238",10,2.50);
		assertTrue(retval>0);
		
		assertEquals(ezShop.computeBalance(),0.00,0.1);
		
	}
	
	/*** API payOrder ***/
	@Test
	public void testPayOrderUnauthorized() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.createUser("cashier", "cm32", "Cashier");
		assertThrows(UnauthorizedException.class,
				()->{ezShop.payOrder(10);
		});
		ezShop.login("cashier", "cm32");
		assertThrows(UnauthorizedException.class,
				()->{ezShop.payOrder(10);
		});
	}
	
	@Test
	public void testPayOrderInvalidOrderId() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.reset();
		ezShop.createUser("shm", "SHM23", "ShopManager");
		ezShop.login("shm", "SHM23");
		assertThrows(InvalidOrderIdException.class,
				()->{ezShop.payOrder(0);
		});
		assertThrows(InvalidOrderIdException.class,
				()->{ezShop.payOrder(-10);
		});
		assertThrows(InvalidOrderIdException.class,
				()->{ezShop.payOrder(null);
		});
	}
	
	@Test
	public void testPayOrderFalse() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidOrderIdException, UnauthorizedException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidQuantityException, InvalidProductIdException, InvalidLocationException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.reset();
		ezShop.createUser("admin", "admin", "Administrator");
		ezShop.login("admin", "admin");
		//Order do not exists
		boolean retval=ezShop.payOrder(10);
		assertFalse(retval);
		
		int pid=ezShop.createProductType("Eggs","234829476238",2.0,"note1");
		assertTrue(pid>0);
		
		ezShop.recordBalanceUpdate(25.00);
		assertEquals(ezShop.computeBalance(),25.00,0.1);
		
		int id=ezShop.payOrderFor("234829476238",10,2.50);
		assertTrue(id>0);
		
		ezShop.recordBalanceUpdate(25.00);
		assertEquals(ezShop.computeBalance(),25.00,0.1);
		
		//Already PAYED
		retval=ezShop.payOrder(id);
		assertFalse(retval);
		
		//Already COMPLETED
		retval=ezShop.updatePosition(pid, "12-OW-0");
		assertTrue(retval);
		
		retval=ezShop.recordOrderArrival(id);
		assertTrue(retval);
		
		retval=ezShop.payOrder(id);
		assertFalse(retval);
		
	}
	
	@Test
	public void testPayOrderTrue() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidOrderIdException, UnauthorizedException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidQuantityException, InvalidProductIdException, InvalidLocationException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.reset();
		ezShop.createUser("admin", "admin", "Administrator");
		ezShop.login("admin", "admin");
		
		
		int pid=ezShop.createProductType("Eggs","234829476238",2.0,"note1");
		assertTrue(pid>0);
		
		int id=ezShop.issueOrder("234829476238",10,2.50);
		assertTrue(id>0);
		
		ezShop.recordBalanceUpdate(25.00);
		assertEquals(ezShop.computeBalance(),25.00,0.1);
		
		boolean retval=ezShop.payOrder(id);
		assertTrue(retval);
		
	}
	
	/*** API recordOrderArrival  ***/
	@Test
	public void testRecordOrderUnauthorized() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.createUser("cashier", "cm32", "Cashier");
		assertThrows(UnauthorizedException.class,
				()->{ezShop.recordOrderArrival(10);
		});
		ezShop.login("cashier", "cm32");
		assertThrows(UnauthorizedException.class,
				()->{ezShop.recordOrderArrival(10);
		});
	}
	
	@Test
	public void testRecordOrderInvalidOrderId() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.reset();
		ezShop.createUser("shm", "SHM23", "ShopManager");
		ezShop.login("shm", "SHM23");
		assertThrows(InvalidOrderIdException.class,
				()->{ezShop.recordOrderArrival(0);
		});
		assertThrows(InvalidOrderIdException.class,
				()->{ezShop.recordOrderArrival(-10);
		});
		assertThrows(InvalidOrderIdException.class,
				()->{ezShop.recordOrderArrival(null);
		});
	}
	
	@Test
	public void testRecordOrderInvalidLocation() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException, InvalidQuantityException, InvalidOrderIdException, InvalidLocationException, InvalidProductIdException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.reset();
		ezShop.createUser("admin", "admin", "Administrator");
		ezShop.login("admin", "admin");
		
		int pid=ezShop.createProductType("Eggs","234829476238",2.0,"note1");
		assertTrue(pid>0);
		
		ezShop.recordBalanceUpdate(25.00);
		assertEquals(ezShop.computeBalance(),25.00,0.1);
		
		//Issue and Pay Order
		
		int oid=ezShop.payOrderFor("234829476238",10,2.50);
		assertTrue(oid>0);
		
		//No location
		assertThrows(InvalidLocationException.class,
				()->{ezShop.recordOrderArrival(oid);
		});
		
		
		
		int id=ezShop.issueOrder("234829476238",10,3.50);
		assertTrue(id>0);
		
		//Assign and remove location
		assertTrue(ezShop.updatePosition(pid, "33-H-12"));
		assertTrue(ezShop.updatePosition(pid, ""));
		
		ezShop.recordBalanceUpdate(40.00);
		assertEquals(ezShop.computeBalance(),40.00,0.1);
		
		//Pay other Order
		assertTrue(ezShop.payOrder(id));
		
		//No location
				assertThrows(InvalidLocationException.class,
						()->{ezShop.recordOrderArrival(id);
				});
		
	}
	
	@Test
	public void testRecordOrderFalse() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException, InvalidProductIdException, InvalidLocationException, InvalidQuantityException, InvalidOrderIdException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.reset();
		ezShop.createUser("shm", "SHM23", "ShopManager");
		ezShop.login("shm", "SHM23");
		
		int pid=ezShop.createProductType("Eggs","234829476238",2.0,"note1");
		assertTrue(pid>0);
		
		assertTrue(ezShop.updatePosition(pid, "33-H-12"));
		
		int id=ezShop.issueOrder("234829476238",10,3.50);
		assertTrue(id>0);
		
		//Order in ISSUED state
		assertFalse(ezShop.recordOrderArrival(id));
		
		//Order do not exists
		assertFalse(ezShop.recordOrderArrival(id+1));
		
		ezShop.recordBalanceUpdate(40.00);
		assertEquals(ezShop.computeBalance(),40.00,0.1);
		
		//Pay Order
		assertTrue(ezShop.payOrder(id));
		
		assertTrue(ezShop.recordOrderArrival(id));
		
		
	}
	
	@Test
	public void testRecordOrderTrue() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidProductIdException, InvalidLocationException, UnauthorizedException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidQuantityException, InvalidOrderIdException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.reset();
		ezShop.createUser("shm", "SHM23", "ShopManager");
		ezShop.login("shm", "SHM23");
		
		int pid=ezShop.createProductType("Eggs","234829476238",2.0,"note1");
		assertTrue(pid>0);
		
		assertTrue(ezShop.updatePosition(pid, "33-H-12"));
		
		//Assert QTY is 0
		assertEquals(ezShop.getProductTypeByBarCode("234829476238").getQuantity(),Integer.valueOf(0));
		
		int id=ezShop.issueOrder("234829476238",10,3.50);
		assertTrue(id>0);
		
		ezShop.recordBalanceUpdate(40.00);
		assertEquals(ezShop.computeBalance(),40.00,0.1);
		
		//Pay Order
		assertTrue(ezShop.payOrder(id));
		
		assertTrue(ezShop.recordOrderArrival(id));
		
		//Assert QTY is 10
		assertEquals(ezShop.getProductTypeByBarCode("234829476238").getQuantity(),Integer.valueOf(10));
	
		ezShop.recordBalanceUpdate(20.00);
		assertEquals(ezShop.computeBalance(),25.00,0.1);
		
		//Issue and Pay Order
		
		int oid=ezShop.payOrderFor("234829476238",20,1.25);
		assertTrue(oid>0);
		
		assertTrue(ezShop.recordOrderArrival(oid));
		
		//Assert QTY is 30
		assertEquals(ezShop.getProductTypeByBarCode("234829476238").getQuantity(),Integer.valueOf(30));
		
		//Order in COMPLETED State, nothing changes
		assertTrue(ezShop.recordOrderArrival(oid));
		
	}
	
	/*** API recordOrderArrivalRFID   ***/
	@Test
	public void testRecordOrderRFIDEXC() throws InvalidOrderIdException,InvalidRFIDException,InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException, InvalidQuantityException, InvalidProductIdException, InvalidLocationException {
		EZShopInterface ezShop = new EZShop();
		ezShop.reset();
		ezShop.createUser("cashier", "cm32", "Cashier");
		ezShop.createUser("shm", "SHM23", "ShopManager");
		assertThrows(UnauthorizedException.class,()->ezShop.recordOrderArrivalRFID(10,"000000000000"));
		ezShop.login("cashier", "cm32");
		assertThrows(UnauthorizedException.class,()->ezShop.recordOrderArrivalRFID(10,"000000000000"));
		
		ezShop.logout();
		ezShop.login("shm", "SHM23");
		assertThrows(InvalidOrderIdException.class,()->ezShop.recordOrderArrivalRFID(null,"000000000000"));
		assertThrows(InvalidOrderIdException.class,()->ezShop.recordOrderArrivalRFID(0,"000000000000"));
		assertThrows(InvalidOrderIdException.class,()->ezShop.recordOrderArrivalRFID(-10,"000000000000"));
		
		assertThrows(InvalidRFIDException.class,()->ezShop.recordOrderArrivalRFID(10,"0"));
		
		ezShop.createProductType("Eggs","234829476238",2.0,"note1");
		ezShop.recordBalanceUpdate(25.00);
		int oid=ezShop.payOrderFor("234829476238",10,2.50);
	
		assertThrows(InvalidLocationException.class,()->ezShop.recordOrderArrivalRFID(oid,"000000000001"));
		
		ezShop.reset();
		ezShop.createUser("shm", "SHM23", "ShopManager");
		ezShop.login("shm", "SHM23");
		int pid=ezShop.createProductType("Eggs","234829476238",2.0,"note1");
		ezShop.updatePosition(pid, "33-H-12");
		ezShop.recordBalanceUpdate(100.00);
		int id=ezShop.issueOrder("234829476238",10,3.50);
		ezShop.payOrder(id);
		
		assertTrue(ezShop.recordOrderArrivalRFID(id,"000000000001"));
		
		int id2=ezShop.issueOrder("234829476238",1,3.50);
		ezShop.payOrder(id2);
		
		assertThrows(InvalidRFIDException.class,()->ezShop.recordOrderArrivalRFID(id2,"000000000001"));
		assertThrows(InvalidRFIDException.class,()->ezShop.recordOrderArrivalRFID(id2,"000000000005"));
	}
	@Test
	public void testRecordOrderRFIDReturn() throws InvalidOrderIdException,InvalidRFIDException,InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException, InvalidQuantityException, InvalidProductIdException, InvalidLocationException {
		EZShopInterface ezShop = new EZShop();
		ezShop.reset();
		ezShop.createUser("shm", "SHM23", "ShopManager");
		ezShop.login("shm", "SHM23");
		
		assertFalse(ezShop.recordOrderArrivalRFID(20,"000000000001"));
		
		int pid = ezShop.createProductType("Eggs","234829476238",2.0,"note1");
		ezShop.updatePosition(pid, "33-H-12");
		ezShop.recordBalanceUpdate(100.00);
		int id=ezShop.issueOrder("234829476238",10,3.50);
		
		assertFalse(ezShop.recordOrderArrivalRFID(id,"000000000001"));
		
		ezShop.payOrder(id);
		ezShop.recordOrderArrivalRFID(id,"000000000001");
		assertTrue(ezShop.recordOrderArrival(id));
		
		
	}
	
	/*** API getAllOrders ***/
	@Test
	public void testGetAllUnauthorized() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.createUser("cashier", "cm32", "Cashier");
		assertThrows(UnauthorizedException.class,
				()->{ezShop.getAllOrders();
		});
		ezShop.login("cashier", "cm32");
		assertThrows(UnauthorizedException.class,
				()->{ezShop.getAllOrders();
		});
	}
	
	@Test
	public void testGetAllEmpty() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, UnauthorizedException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.reset();
		ezShop.createUser("shm", "SHM23", "ShopManager");
		ezShop.login("shm", "SHM23");
		List<Order> list = ezShop.getAllOrders();
		assertNotNull(list);
		assertTrue(list.isEmpty());
		assertEquals(list.size(),0);
	}
	
	@Test
	public void testGetAllValid() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, UnauthorizedException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidProductIdException, InvalidLocationException, InvalidQuantityException, InvalidOrderIdException {
		EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
		ezShop.reset();
		ezShop.createUser("admin", "admin", "Administrator");
		ezShop.login("admin", "admin");
		
		int pid=ezShop.createProductType("Eggs","234829476238",2.0,"note1");
		assertTrue(pid>0);
		
		assertTrue(ezShop.updatePosition(pid, "33-H-12"));
		
		int id=ezShop.issueOrder("234829476238",10,3.50);
		assertTrue(id>0);
		
		List<Order> list = ezShop.getAllOrders();
		assertNotNull(list);
		assertFalse(list.isEmpty());
		assertEquals(list.size(),1);
		
		ezShop.recordBalanceUpdate(25.00);
		assertEquals(ezShop.computeBalance(),25.00,0.1);
		
		//Issue and Pay Order
		
		int oid=ezShop.payOrderFor("234829476238",20,1.25);
		assertTrue(oid>0);
		
		list = ezShop.getAllOrders();
		assertNotNull(list);
		assertFalse(list.isEmpty());
		assertEquals(list.size(),2);
		
		assertTrue(ezShop.recordOrderArrival(oid));
		
		ezShop.recordBalanceUpdate(25.00);
		assertEquals(ezShop.computeBalance(),25.00,0.1);
		
		//Issue and Pay Order
		
		int oid2=ezShop.payOrderFor("234829476238",10,1.70);
		assertTrue(oid2>0);
		
		list = ezShop.getAllOrders();
		assertNotNull(list);
		assertFalse(list.isEmpty());
		assertEquals(list.size(),3);
		
		ezShop.recordBalanceUpdate(27.00);
		assertEquals(ezShop.computeBalance(),35.00,0.1);
		
		assertTrue(ezShop.payOrder(id));
		
		list = ezShop.getAllOrders();
		assertNotNull(list);
		assertFalse(list.isEmpty());
		assertEquals(list.size(),3);
		
		
	}
	
	
	
	

}
