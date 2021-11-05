package it.polito.ezshop.integrationTests;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Test;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidRoleException;
import it.polito.ezshop.exceptions.InvalidUsernameException;
import it.polito.ezshop.exceptions.UnauthorizedException;

public class EzShop_Balance_IntT {
						/** FR8 **/
	
	// RecordBalanceUpdate
	@Test
	public void testBalanceUpdEXC() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezS = new EZShop();
		ezS.reset();
		assertThrows(UnauthorizedException.class,() -> ezS.recordBalanceUpdate(10));
		
		ezS.createUser("C", "C", "Cashier");
		ezS.login("C", "C");
		assertThrows(UnauthorizedException.class,() -> ezS.recordBalanceUpdate(10));
	}
	@Test
	public void testBalanceUpdRet() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, UnauthorizedException {
		EZShopInterface ezS = new EZShop();
		ezS.reset();
		ezS.createUser("Admin", "Admin", "Administrator");
		ezS.login("Admin", "Admin");
		
		double x=ezS.computeBalance();
		assertTrue(ezS.recordBalanceUpdate(x+10));
		assertTrue(ezS.recordBalanceUpdate(-x-5));
		assertFalse(ezS.recordBalanceUpdate((ezS.computeBalance()+1)*(-1)));
	}
	
	// getCreditsAndDebits
	@Test
	public void testGetCredDebEXC() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezS = new EZShop();
		ezS.reset();
		assertThrows(UnauthorizedException.class,() -> ezS.getCreditsAndDebits(null,null));
		
		ezS.createUser("C", "C", "Cashier");
		ezS.login("C", "C");
		assertThrows(UnauthorizedException.class,() -> ezS.getCreditsAndDebits(null,null));
	}
	@Test
	public void testGetCredDebRet() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, UnauthorizedException {
		EZShopInterface ezS = new EZShop();
		ezS.reset();
		ezS.createUser("Admin", "Admin", "Administrator");
		ezS.login("Admin", "Admin");
		
		ezS.recordBalanceUpdate(10);
		ezS.recordBalanceUpdate(10);
		
		assertEquals(ezS.getCreditsAndDebits(null, null).size(),2,0);
		assertEquals(ezS.getCreditsAndDebits(LocalDate.now(), LocalDate.now().minusDays(1)).size(),2,0);
		assertEquals(ezS.getCreditsAndDebits(LocalDate.now().minusDays(1), LocalDate.now()).size(),2,0);
		assertEquals(ezS.getCreditsAndDebits(LocalDate.now(), null).size(),2,0);
		assertEquals(ezS.getCreditsAndDebits(null,LocalDate.now()).size(),2,0);
	}
	
	// ComputeBalance
	@Test
	public void testComputeBalanceEXC() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
		EZShopInterface ezS = new EZShop();
		ezS.reset();
		assertThrows(UnauthorizedException.class,() -> ezS.computeBalance());
		
		ezS.createUser("C", "C", "Cashier");
		ezS.login("C", "C");
		assertThrows(UnauthorizedException.class,() -> ezS.computeBalance());
	}
	@Test
	public void testComputeBalanceRet() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException, UnauthorizedException {
		EZShopInterface ezS = new EZShop();
		ezS.reset();
		ezS.createUser("Admin", "Admin", "Administrator");
		ezS.login("Admin", "Admin");
		
		double x=ezS.computeBalance();
		ezS.recordBalanceUpdate(10);
		assertEquals(ezS.computeBalance(),x+10,0);
	}
}
