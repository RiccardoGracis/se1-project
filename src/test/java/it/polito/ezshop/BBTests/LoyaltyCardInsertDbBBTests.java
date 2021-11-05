package it.polito.ezshop.BBTests;

import static org.junit.Assert.assertThrows;

import java.sql.SQLException;

import org.junit.Test;

import it.polito.ezshop.data.JDBC;

public class LoyaltyCardInsertDbBBTests {
	
	@Test
	public void testInsertLoyaltyCardExisting() throws SQLException {
		JDBC db = new JDBC();
		db.deleteEntireDB();
		db.openConnection();
		db.createLoyaltyCardTable();
		db.insertCustomerCard("1234567890", 0, false);
		assertThrows(SQLException.class, () -> {
			db.insertCustomerCard("1234567890", 0, false);
		});
	}
	

	@Test
	public void testInsertLoyaltyCardCorrect() throws SQLException {
		JDBC db = new JDBC();
		db.deleteEntireDB();
		db.openConnection();
		db.createLoyaltyCardTable();
		db.insertCustomerCard("1234567890", 0, false);
	}

}
