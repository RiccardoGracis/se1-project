package it.polito.ezshop.integrationTests;

import static org.junit.Assert.*;

import org.junit.Test;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.InvalidLocationException;
import it.polito.ezshop.exceptions.InvalidOrderIdException;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidPaymentException;
import it.polito.ezshop.exceptions.InvalidPricePerUnitException;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidProductDescriptionException;
import it.polito.ezshop.exceptions.InvalidProductIdException;
import it.polito.ezshop.exceptions.InvalidQuantityException;
import it.polito.ezshop.exceptions.InvalidRFIDException;
import it.polito.ezshop.exceptions.InvalidRoleException;
import it.polito.ezshop.exceptions.InvalidTransactionIdException;
import it.polito.ezshop.exceptions.InvalidUsernameException;
import it.polito.ezshop.exceptions.UnauthorizedException;

public class EzShop_Sale_Return_RFID_IntT {

	@Test
	public void testreturnProdRFIDEXC() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, UnauthorizedException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidProductIdException, InvalidLocationException, InvalidTransactionIdException, InvalidQuantityException, InvalidPaymentException, InvalidOrderIdException, InvalidRFIDException {
		EZShopInterface ezS = new EZShop();
		ezS.reset();
		ezS.createUser("admin", "admin", "Administrator");
		
		assertThrows(UnauthorizedException.class,()->ezS.returnProductRFID(10,"0000000000"));
		ezS.login("admin", "admin");
		
		assertThrows(InvalidTransactionIdException.class,()->ezS.returnProductRFID(null,"000000000000"));
		assertThrows(InvalidTransactionIdException.class,()->ezS.returnProductRFID(0,"000000000000"));
		assertThrows(InvalidTransactionIdException.class,()->ezS.returnProductRFID(-10,"000000000000"));
		assertThrows(InvalidRFIDException.class,()->ezS.returnProductRFID(10,null));
		assertThrows(InvalidRFIDException.class,()->ezS.returnProductRFID(10,""));
		assertThrows(InvalidRFIDException.class,()->ezS.returnProductRFID(10,"00"));
		assertThrows(InvalidRFIDException.class,()->ezS.returnProductRFID(10,"0ab0"));
		
		assertFalse(ezS.returnProductRFID(10,"000000000000"));
				
	}
	@Test
	public void testreturnProdRFIDRet() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, UnauthorizedException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidProductIdException, InvalidLocationException, InvalidTransactionIdException, InvalidQuantityException, InvalidPaymentException, InvalidOrderIdException, InvalidRFIDException {
		EZShopInterface ezS = new EZShop();
		ezS.reset();
		ezS.createUser("admin", "admin", "Administrator");
		ezS.login("admin", "admin");		
		int pid = ezS.createProductType("Eggs","234829476238",2.0,"note1");
		ezS.updatePosition(pid, "33-H-12");
		ezS.recordBalanceUpdate(100.00);
		int id=ezS.issueOrder("234829476238",10,3.50);
		ezS.payOrder(id);
		ezS.recordOrderArrivalRFID(id,"000000000001");
		int saleId = ezS.startSaleTransaction();
		ezS.addProductToSaleRFID(saleId,"000000000001");
		ezS.addProductToSaleRFID(saleId,"000000000002");
		ezS.addProductToSaleRFID(saleId,"000000000004");
		ezS.endSaleTransaction(saleId);
		ezS.receiveCashPayment(saleId, 50);
		Integer retId =ezS.startReturnTransaction(saleId);
		
		assertFalse(ezS.returnProductRFID(retId,"000000000000"));
		assertTrue(ezS.returnProductRFID(retId,"000000000004"));
		assertTrue(ezS.returnProductRFID(retId,"000000000002"));
		assertFalse(ezS.returnProductRFID(retId,"000000000003"));	
	}

	@Test
	public void testAddProductToSaleRFID() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, UnauthorizedException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidProductIdException, InvalidLocationException, InvalidTransactionIdException, InvalidQuantityException, InvalidPaymentException, InvalidOrderIdException, InvalidRFIDException {
		EZShopInterface ezS = new EZShop();
		ezS.reset();
		ezS.createUser("admin", "admin", "Administrator");
		ezS.login("admin", "admin");		
		int pid = ezS.createProductType("Eggs","234829476238",2.0,"note1");
		ezS.updatePosition(pid, "33-H-12");
		ezS.recordBalanceUpdate(100.00);
		int id=ezS.issueOrder("234829476238",10,3.50);
		ezS.payOrder(id);
		ezS.recordOrderArrivalRFID(id,"000000000001");
		int saleId = ezS.startSaleTransaction();
		assertTrue(ezS.addProductToSaleRFID(saleId,"000000000001"));
		assertTrue(ezS.addProductToSaleRFID(saleId,"000000000010"));
		assertFalse(ezS.addProductToSaleRFID(saleId,"000000000011"));
		assertFalse(ezS.addProductToSaleRFID(saleId + 1,"000000000010"));
		ezS.endSaleTransaction(saleId);
		assertFalse(ezS.addProductToSaleRFID(saleId,"000000000001"));	
	}
	
	@Test
	public void testAddProductToSaleRFIDEXC() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, UnauthorizedException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidProductIdException, InvalidLocationException, InvalidTransactionIdException, InvalidQuantityException, InvalidPaymentException, InvalidOrderIdException, InvalidRFIDException {
		EZShopInterface ezS = new EZShop();
		ezS.reset();
		ezS.createUser("admin", "admin", "Administrator");
		
		assertThrows(UnauthorizedException.class,()->ezS.addProductToSaleRFID(10,"0000000000"));
		ezS.login("admin", "admin");
		
		assertThrows(InvalidTransactionIdException.class,()->ezS.addProductToSaleRFID(null,"000000000000"));
		assertThrows(InvalidTransactionIdException.class,()->ezS.addProductToSaleRFID(0,"000000000000"));
		assertThrows(InvalidTransactionIdException.class,()->ezS.addProductToSaleRFID(-1,"000000000000"));
		assertThrows(InvalidRFIDException.class,()->ezS.addProductToSaleRFID(1,null));
		assertThrows(InvalidRFIDException.class,()->ezS.addProductToSaleRFID(1,""));
		assertThrows(InvalidRFIDException.class,()->ezS.addProductToSaleRFID(1,"00"));
		assertThrows(InvalidRFIDException.class,()->ezS.addProductToSaleRFID(1,"0ab0"));
		assertThrows(InvalidRFIDException.class,()->ezS.addProductToSaleRFID(1,"aaaaaaaaaaaa"));
		assertThrows(InvalidRFIDException.class,()->ezS.addProductToSaleRFID(1,"1aaaaaaaaaa1"));
		assertThrows(InvalidRFIDException.class,()->ezS.addProductToSaleRFID(1,"1234567890123"));


	}
	
	@Test
	public void testDeleteProductFromSaleRFID() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, UnauthorizedException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidProductIdException, InvalidLocationException, InvalidTransactionIdException, InvalidQuantityException, InvalidPaymentException, InvalidOrderIdException, InvalidRFIDException {
		EZShopInterface ezS = new EZShop();
		ezS.reset();
		ezS.createUser("admin", "admin", "Administrator");
		ezS.login("admin", "admin");		
		int pid = ezS.createProductType("Eggs","234829476238",2.0,"note1");
		ezS.updatePosition(pid, "33-H-12");
		ezS.recordBalanceUpdate(100.00);
		int id=ezS.issueOrder("234829476238",10,3.50);
		ezS.payOrder(id);
		ezS.recordOrderArrivalRFID(id,"000000000001");
		int saleId = ezS.startSaleTransaction();
		ezS.addProductToSaleRFID(saleId,"000000000001");
		assertTrue(ezS.deleteProductFromSaleRFID(saleId,"000000000001"));
		assertFalse(ezS.deleteProductFromSaleRFID(saleId,"000000000001")); //issues
		assertFalse(ezS.deleteProductFromSaleRFID(saleId,"000000000011"));
		assertFalse(ezS.deleteProductFromSaleRFID(saleId + 1,"000000000010"));
		ezS.endSaleTransaction(saleId);
		assertFalse(ezS.deleteProductFromSaleRFID(saleId,"000000000001"));	

	}
	
	@Test
	public void testDeleteProductFromSaleRFIDEXC() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, UnauthorizedException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidProductIdException, InvalidLocationException, InvalidTransactionIdException, InvalidQuantityException, InvalidPaymentException, InvalidOrderIdException, InvalidRFIDException {
		EZShopInterface ezS = new EZShop();
		ezS.reset();
		ezS.createUser("admin", "admin", "Administrator");

		assertThrows(UnauthorizedException.class, () -> ezS.deleteProductFromSaleRFID(10, "0000000000"));
		ezS.login("admin", "admin");

		assertThrows(InvalidTransactionIdException.class, () -> ezS.deleteProductFromSaleRFID(null, "000000000000"));
		assertThrows(InvalidTransactionIdException.class, () -> ezS.deleteProductFromSaleRFID(0, "000000000000"));
		assertThrows(InvalidTransactionIdException.class, () -> ezS.deleteProductFromSaleRFID(-1, "000000000000"));
		assertThrows(InvalidRFIDException.class, () -> ezS.deleteProductFromSaleRFID(1, null));
		assertThrows(InvalidRFIDException.class, () -> ezS.deleteProductFromSaleRFID(1, ""));
		assertThrows(InvalidRFIDException.class, () -> ezS.deleteProductFromSaleRFID(1, "00"));
		assertThrows(InvalidRFIDException.class, () -> ezS.deleteProductFromSaleRFID(1, "0ab0"));
		assertThrows(InvalidRFIDException.class, () -> ezS.deleteProductFromSaleRFID(1, "aaaaaaaaaaaa"));
		assertThrows(InvalidRFIDException.class, () -> ezS.deleteProductFromSaleRFID(1, "1aaaaaaaaaa1"));
		assertThrows(InvalidRFIDException.class, () -> ezS.deleteProductFromSaleRFID(1, "1234567890123"));

	}
	

}
