package it.polito.ezshop.WBTests;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

import java.sql.SQLException;

import org.junit.Test;

import it.polito.ezshop.data.JDBC;
import it.polito.ezshop.data.LoyaltyCard;
import it.polito.ezshop.data.UserImpl;

public class UserDbWBTests {
	
	@Test
	public void testCreateUserTable() throws SQLException {
		JDBC db=new JDBC();
		db.deleteEntireDB();
		db.openConnection();
		db.createUserTable();
	}
	
	@Test
	public void testDeleteUser() throws SQLException {
		JDBC db = new JDBC();
		db.deleteEntireDB();
		db.openConnection();
		db.createUserTable();
		db.insertUser(0, "u1", "pwd1", "Administrator");
		db.deleteUser(0);
	}
	
	@Test
	public void testLoadUsers() throws SQLException {
		JDBC db = new JDBC();
		db.deleteEntireDB();
		db.openConnection();
		db.createUserTable();
		db.insertUser(1, "u1", "pwd1", "Administrator");
		db.insertUser(2, "u2", "pwd2", "ShopManager");
		db.loadUsers();
		
	}
	
	@Test
	public void testUpdateUserRole() throws SQLException {
		JDBC db = new JDBC();
		db.deleteEntireDB();
		db.openConnection();
		db.createUserTable();
		db.insertUser(1, "u1", "pwd1", "Administrator");
		db.updateUserRole(1, "ShopManager");
	}

}
