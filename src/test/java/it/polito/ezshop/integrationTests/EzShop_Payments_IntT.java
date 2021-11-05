package it.polito.ezshop.integrationTests;

import static org.junit.Assert.*;

import org.junit.Test;

import it.polito.ezshop.data.*;
import it.polito.ezshop.exceptions.*;

public class EzShop_Payments_IntT {

						/** FR7 **/
	
	// Receive CashPayment 
	 
	@Test
	public void testCashPaymentEXC() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezS = new EZShop();
		ezS.reset();
		assertThrows(UnauthorizedException.class,() -> ezS.receiveCashPayment(10, 10));
		
		ezS.createUser("admin", "admin", "Administrator");
		ezS.login("admin", "admin");
		assertThrows(InvalidTransactionIdException.class,() -> ezS.receiveCashPayment(null, 10));
		assertThrows(InvalidTransactionIdException.class,() -> ezS.receiveCashPayment(-4, 10));
		assertThrows(InvalidTransactionIdException.class,() -> ezS.receiveCashPayment(0, 10));
		
		assertThrows(InvalidPaymentException.class,() -> ezS.receiveCashPayment(5, -20));
		assertThrows(InvalidPaymentException.class,() -> ezS.receiveCashPayment(5, 0));
	}
	@Test
	public void testCashPaymentRet() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, UnauthorizedException, InvalidTransactionIdException, InvalidPaymentException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidQuantityException, InvalidProductIdException, InvalidLocationException {
		EZShopInterface ezS = new EZShop();
		ezS.reset();
		ezS.createUser("admin", "admin", "Administrator");
		ezS.login("admin", "admin");
		int saleId = ezS.startSaleTransaction();
		int prodId = ezS.createProductType("Eggs","234829476238",2.0,"note1");
		ezS.updatePosition(prodId, "12-A-24");
		ezS.updateQuantity(prodId, 20);
		ezS.addProductToSale(saleId,"234829476238", 5);
		ezS.endSaleTransaction(saleId);
		
		assertEquals(ezS.receiveCashPayment(15, 500),-1,0);
		assertEquals(ezS.receiveCashPayment(saleId, 5),-1,0);
		assertEquals(ezS.receiveCashPayment(saleId, 50),40,0);		
	}
	
	//Receive CreditCardPayment
	@Test
	public void testCCPaymentEXC() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezS = new EZShop();
		ezS.reset();
		assertThrows(UnauthorizedException.class,() -> ezS.receiveCreditCardPayment(10, "4485370086510891"));
		
		ezS.createUser("admin", "admin", "Administrator");
		ezS.login("admin", "admin");
		assertThrows(InvalidTransactionIdException.class,() -> ezS.receiveCreditCardPayment(null, "4485370086510891"));
		assertThrows(InvalidTransactionIdException.class,() -> ezS.receiveCreditCardPayment(-4, "4485370086510891"));
		assertThrows(InvalidTransactionIdException.class,() -> ezS.receiveCreditCardPayment(0, "4485370086510891"));
		
		assertThrows(InvalidCreditCardException.class,() -> ezS.receiveCreditCardPayment(1, null));
		assertThrows(InvalidCreditCardException.class,() -> ezS.receiveCreditCardPayment(1, ""));
		assertThrows(InvalidCreditCardException.class,() -> ezS.receiveCreditCardPayment(1, "4485370086510890"));
	}
	@Test
	public void testCCPaymentRet() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, UnauthorizedException, InvalidTransactionIdException, InvalidPaymentException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidQuantityException, InvalidCreditCardException, InvalidProductIdException, InvalidLocationException {
		EZShopInterface ezS = new EZShop();
		ezS.reset();
		ezS.createUser("admin", "admin", "Administrator");
		ezS.login("admin", "admin");
		int saleId = ezS.startSaleTransaction();
		int prodId = ezS.createProductType("Eggs","234829476238",2.0,"note1");
		ezS.updatePosition(prodId, "12-A-24");
		ezS.updateQuantity(prodId, 20);
		ezS.addProductToSale(saleId,"234829476238", 5);
		ezS.endSaleTransaction(saleId);
		
		assertFalse(ezS.receiveCreditCardPayment(15, "4485370086510891"));
		assertFalse(ezS.receiveCreditCardPayment(saleId, "4716258050958645"));	
	}
	
	// Return CashPayment 
	 
	@Test
	public void testRCashPaymentEXC() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezS = new EZShop();
		ezS.reset();
		assertThrows(UnauthorizedException.class,() -> ezS.returnCashPayment(10));
		
		ezS.createUser("admin", "admin", "Administrator");
		ezS.login("admin", "admin");
		assertThrows(InvalidTransactionIdException.class,() -> ezS.returnCashPayment(null));
		assertThrows(InvalidTransactionIdException.class,() -> ezS.returnCashPayment(-4));
		assertThrows(InvalidTransactionIdException.class,() -> ezS.returnCashPayment(0));
	}
	@Test
	public void testRCashPaymentRet() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidTransactionIdException, UnauthorizedException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidQuantityException, InvalidPaymentException, InvalidProductIdException, InvalidLocationException {
		EZShopInterface ezS = new EZShop();
		ezS.reset();
		ezS.createUser("admin", "admin", "Administrator");
		ezS.login("admin", "admin");
		int saleId = ezS.startSaleTransaction();
		int prodId = ezS.createProductType("Eggs","234829476238",2.0,"note1");
		ezS.updatePosition(prodId, "12-A-24");
		ezS.updateQuantity(prodId, 20);
		ezS.addProductToSale(saleId,"234829476238", 5);
		ezS.endSaleTransaction(saleId);
		ezS.receiveCashPayment(saleId, 50);
		
		assertEquals(ezS.returnCashPayment(10),-1,0);
		
		int rsId = ezS.startReturnTransaction(saleId);
		assertEquals(ezS.returnCashPayment(rsId),-1,0);
		
		ezS.returnProduct(rsId,"234829476238",1);
		ezS.endReturnTransaction(rsId, true);
		assertEquals(ezS.returnCashPayment(rsId),-2,0);
	}
	
	
	// Return CreditCardPayment  
	@Test
	public void testRCCPaymentEXC() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezS = new EZShop();
		ezS.reset();
		assertThrows(UnauthorizedException.class,() -> ezS.returnCreditCardPayment(10, "4485370086510891"));
		
		ezS.createUser("admin", "admin", "Administrator");
		ezS.login("admin", "admin");
		assertThrows(InvalidTransactionIdException.class,() -> ezS.returnCreditCardPayment(null, "4485370086510891"));
		assertThrows(InvalidTransactionIdException.class,() -> ezS.returnCreditCardPayment(-4, "4485370086510891"));
		assertThrows(InvalidTransactionIdException.class,() -> ezS.returnCreditCardPayment(0, "4485370086510891"));
		
		assertThrows(InvalidCreditCardException.class,() -> ezS.returnCreditCardPayment(1, null));
		assertThrows(InvalidCreditCardException.class,() -> ezS.returnCreditCardPayment(1, ""));
		assertThrows(InvalidCreditCardException.class,() -> ezS.returnCreditCardPayment(1, "4485370086510890"));
	}
	
	@Test
	public void testRCCPaymentRet() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidTransactionIdException, UnauthorizedException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidQuantityException, InvalidPaymentException, InvalidProductIdException, InvalidLocationException, InvalidCreditCardException {
		EZShopInterface ezS = new EZShop();
		ezS.reset();
		ezS.createUser("admin", "admin", "Administrator");
		ezS.login("admin", "admin");
		int saleId = ezS.startSaleTransaction();
		int prodId = ezS.createProductType("Eggs","234829476238",2.0,"note1");
		ezS.updatePosition(prodId, "12-A-24");
		ezS.updateQuantity(prodId, 20);
		ezS.addProductToSale(saleId,"234829476238", 5);
		ezS.endSaleTransaction(saleId);
		ezS.receiveCreditCardPayment(saleId,"4485370086510891");
		
		int rsId = ezS.startReturnTransaction(saleId);
		assertEquals(ezS.returnCreditCardPayment(rsId,"4485370086510891"),-1,0);
		
		ezS.returnProduct(rsId,"234829476238",1);
		ezS.endReturnTransaction(rsId, true);
		assertEquals(ezS.returnCreditCardPayment(rsId,"4485370086510891"),-2,0);
	}
}
