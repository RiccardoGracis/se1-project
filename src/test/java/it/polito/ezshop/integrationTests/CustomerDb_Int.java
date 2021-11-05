package it.polito.ezshop.integrationTests;

import static org.junit.Assert.assertThrows;

import java.sql.SQLException;

import org.junit.Test;

import it.polito.ezshop.data.JDBC;

public class CustomerDb_Int {
	
	@Test
	public void testCreateCustomerTable() throws SQLException {
		JDBC db=new JDBC();
		db.deleteEntireDB();
		db.openConnection();
		db.createCustomerTable();
	}

	@Test
	public void testInsertCustomerExistingId() throws SQLException {
		JDBC db = new JDBC();
		db.deleteEntireDB();
		db.openConnection();
		db.createCustomerTable();
		db.insertCustomer(1, "c1");
		assertThrows(SQLException.class, () -> {
			db.insertCustomer(1, "c2");
		});
	}

	@Test
	public void testInsertCustomerCorrect() throws SQLException {
		JDBC db = new JDBC();
		db.deleteEntireDB();
		db.openConnection();
		db.createCustomerTable();
		db.insertCustomer(1, "c1");
	}
	
	@Test
	public void testModifyCustomerEmptyLoyaltyCard() throws SQLException {
		JDBC db = new JDBC();
		db.deleteEntireDB();
		db.openConnection();
		db.createCustomerTable();
		db.insertCustomer(1, "c1");
		db.modifyCustomer("c2", "", 1);
	}
	
	@Test
	public void testModifyCustomerValidLoyaltyCard() throws SQLException {
		JDBC db = new JDBC();
		db.deleteEntireDB();
		db.openConnection();
		db.createCustomerTable();
		db.createLoyaltyCardTable();
		db.insertCustomer(1, "c1");
		db.modifyCustomer("c2", "1234567890", 1);
	}
	
	 

	@Test
	public void testModifyCustomerName() throws SQLException {
		JDBC db = new JDBC();
		db.deleteEntireDB();
		db.openConnection();
		db.createCustomerTable();
		db.insertCustomer(1, "c1");
		db.modifyCustomerName("c2", 1);
	}
	
	@Test
	public void testDeleteCustomer() throws SQLException {
		JDBC db = new JDBC();
		db.deleteEntireDB();
		db.openConnection();
		db.createCustomerTable();
		db.insertCustomer(1, "c1");
		db.deleteCustomer(1);
	}
	
	@Test
	public void testLoadCustomers() throws SQLException {
		JDBC db = new JDBC();
		db.deleteEntireDB();
		db.openConnection();
		db.createCustomerTable();
		db.createLoyaltyCardTable();
		db.insertCustomer(1, "c1");
		db.insertCustomer(2, "c2");
		db.loadCustomers(db.loadLoyaltyCards());
		
	}

}
