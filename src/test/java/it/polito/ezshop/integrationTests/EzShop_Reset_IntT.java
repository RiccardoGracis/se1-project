package it.polito.ezshop.integrationTests;

import static org.junit.Assert.*;

import org.junit.Test;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.InvalidCustomerCardException;
import it.polito.ezshop.exceptions.InvalidCustomerIdException;
import it.polito.ezshop.exceptions.InvalidCustomerNameException;
import it.polito.ezshop.exceptions.InvalidLocationException;
import it.polito.ezshop.exceptions.InvalidOrderIdException;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidPaymentException;
import it.polito.ezshop.exceptions.InvalidPricePerUnitException;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidProductDescriptionException;
import it.polito.ezshop.exceptions.InvalidProductIdException;
import it.polito.ezshop.exceptions.InvalidQuantityException;
import it.polito.ezshop.exceptions.InvalidRoleException;
import it.polito.ezshop.exceptions.InvalidTransactionIdException;
import it.polito.ezshop.exceptions.InvalidUsernameException;
import it.polito.ezshop.exceptions.UnauthorizedException;

public class EzShop_Reset_IntT {

	@Test
	public void testReset() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, InvalidTransactionIdException, UnauthorizedException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidQuantityException, InvalidPaymentException, InvalidProductIdException, InvalidLocationException, InvalidOrderIdException, InvalidCustomerNameException, InvalidCustomerIdException, InvalidCustomerCardException {
		EZShopInterface ezS = new EZShop();
		ezS.reset();
		ezS.createUser("admin", "admin", "Administrator");
		ezS.login("admin", "admin");
		ezS.createUser("c", "c", "Cashier");
		int saleId = ezS.startSaleTransaction();
		int prodId = ezS.createProductType("Eggs","234829476238",2.0,"note1");
		ezS.recordBalanceUpdate(100.00);
		ezS.updatePosition(prodId, "12-A-24");
		ezS.updateQuantity(prodId, 20);
		ezS.addProductToSale(saleId,"234829476238", 5);
		ezS.endSaleTransaction(saleId);
		ezS.receiveCashPayment(saleId, 50);
		int id = ezS.issueOrder("234829476238",10,2.50);
		ezS.payOrder(id);
		ezS.defineCustomer("c1");
		ezS.defineCustomer("c2");
		((EZShop) ezS).createCard("1234567890");
		ezS.attachCardToCustomer("1234567890", id);
		
		ezS.reset();
		
		ezS.createUser("admin", "admin", "Administrator");
		ezS.login("admin", "admin");									
		assertEquals(ezS.getAllUsers().size(),1,0);
		assertEquals(ezS.getAllCustomers().size(),0,0);
		assertEquals(ezS.computeBalance(),0,0);
		assertTrue(ezS.getAllOrders().isEmpty());
		assertTrue(ezS.getAllProductTypes().isEmpty());
		assertTrue(ezS.getCreditsAndDebits(null, null).isEmpty());
	}

}
