package it.polito.ezshop.WBTests;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.Test;

import it.polito.ezshop.data.JDBC;

public class BalanceDBTests {

	@Test
	public void testCreateBalTable() {
		JDBC db=new JDBC();
		try {
		db.deleteEntireDB();
		db.openConnection();
		db.createBalanceTable();
		
		} 
		catch (SQLException e) {
			fail();
		}
		
		db.closeConnection();
	}
	
	@Test
	public void testInsertBalTable() {
		JDBC db=new JDBC();
		try {
		db.deleteEntireDB();
		db.openConnection();
		db.createBalanceTable();
		
		} 
		catch (SQLException e) {
			
		}
		
		try {
			db.insertBalance();
		}
		catch (SQLException e) {
			fail();
		}
		
		db.closeConnection();
	}
	
	@Test
	public void testUpdateBalTable() {
		JDBC db=new JDBC();
		try {
		db.deleteEntireDB();
		db.openConnection();
		db.createBalanceTable();
		db.insertBalance();
		} 
		catch (SQLException e) {
			
		}
		
		try {
			db.updateBalance(10);
		}
		catch (SQLException e) {
			fail();
		}
		
		db.closeConnection();
	}
	@Test
	public void testLoadBal() {
		JDBC db=new JDBC();
		try {
		db.deleteEntireDB();
		db.openConnection();
		db.createBalanceTable();
		db.insertBalance();
		db.updateBalance(10);
		} 
		catch (SQLException e) {
			
		}
		
		try {
			assertEquals(db.loadBalance(),10,0);
		}
		catch (SQLException e) {
			fail();
		}
		
		db.closeConnection();
	}

}
