package it.polito.ezshop.WBTests;

import java.sql.SQLException;

import org.junit.Test;

import it.polito.ezshop.data.JDBC;

public class LoyaltyCardDbWBTests {
	
	@Test
	public void testCreateLoyaltyCardTable() throws SQLException {
		JDBC db=new JDBC();
		db.openConnection();
		db.createLoyaltyCardTable();
	}
	
	@Test
	public void testDeleteLoyaltyCard() throws SQLException {
		JDBC db = new JDBC();
		db.openConnection();
		db.createLoyaltyCardTable();
		db.deleteCustomerCard("1234567890");
	}
		
	@Test
	public void testModifyPointsLoyaltyCard() throws SQLException {
		JDBC db = new JDBC();
		db.deleteEntireDB();
		db.openConnection();
		db.createLoyaltyCardTable();
		db.insertCustomerCard("1234567890", 0, false);
		db.modifyPoints("1234567890", 50);
	}
	
	@Test
	public void testLoadLoyaltyCards() throws SQLException {
		JDBC db = new JDBC();
		db.deleteEntireDB();
		db.openConnection();
		db.createLoyaltyCardTable();
		db.insertCustomerCard("1234567890", 0, false);
		db.insertCustomerCard("1234567891", 0, false);
		db.loadLoyaltyCards();
		
	}

}
