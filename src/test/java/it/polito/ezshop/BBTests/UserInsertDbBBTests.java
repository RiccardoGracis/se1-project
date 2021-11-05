package it.polito.ezshop.BBTests;

import static org.junit.Assert.assertThrows;

import java.sql.SQLException;

import org.junit.Test;

import it.polito.ezshop.data.JDBC;

public class UserInsertDbBBTests {
	
	@Test
	public void testInsertUserExistingId() throws SQLException {
		JDBC db = new JDBC();
		db.deleteEntireDB();
		db.openConnection();
		db.createUserTable();
		db.insertUser(1, "u1", "pwd1", "Administrator");
		assertThrows(SQLException.class, () -> {
			db.insertUser(1, "u2", "pwd1", "Administrator");
		});
	}
	
	@Test
	public void testInsertUserExistingUsername() throws SQLException {
		JDBC db = new JDBC();
		db.deleteEntireDB();
		db.openConnection();
		db.createUserTable();
		db.insertUser(1, "u1", "pwd1", "Administrator");
		assertThrows(SQLException.class, () -> {
			db.insertUser(2, "u1", "pwd1", "Administrator");
		});
	}
	
	@Test
	public void testInsertUserCorrect() throws SQLException {
		JDBC db = new JDBC();
		db.deleteEntireDB();
		db.openConnection();
		db.createUserTable();
		db.insertUser(1, "u1", "pwd1", "Administrator");
	}


}
