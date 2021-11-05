package it.polito.ezshop.WBTests;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.Test;
import it.polito.ezshop.data.*;

public class PositionDbWBTests {
	
	@Test
	public void testCreatePosTable() {
		JDBC db=new JDBC();
		try {
		db.deleteEntireDB();
		db.openConnection();
		db.createPositionTable();
		
		} 
		catch (SQLException e) {
			fail();
		}
		
		db.closeConnection();
	}
	
	@Test
	public void testInsertPosTable() {
		JDBC db=new JDBC();
		try {
		db.deleteEntireDB();
		db.openConnection();
		db.createPositionTable();
		
		} 
		catch (SQLException e) {
			
		}
		
		try {
			db.insertPosition("1-AB-12");
		}
		catch (SQLException e) {
			fail();
		}
		
		db.closeConnection();
	}
	
	@Test
	public void testDeletePosTable() {
		JDBC db=new JDBC();
		try {
		db.deleteEntireDB();
		db.openConnection();
		db.createPositionTable();
		db.insertPosition("1-AB-12");
		
		} 
		catch (SQLException e) {
			
		}
		
		try {
			db.deletePosition("1-AB-12");
		}
		catch (SQLException e) {
			fail();
		}
		
		db.closeConnection();
	}
	
	@Test
	public void testLoadPosTable() {
		JDBC db=new JDBC();
		try {
		db.deleteEntireDB();
		db.openConnection();
		db.createPositionTable();
		db.insertPosition("1-AB-12");
		
		} 
		catch (SQLException e) {
			
		}
		
		try {
			assertEquals(db.loadPosition().size(),1);
			assertTrue(db.loadPosition().contains("1-AB-12"));
		}
		catch (SQLException e) {
			fail();
		}
		
		db.closeConnection();
	}
	
	@Test
	public void testLoadOrderTable() {
		JDBC db=new JDBC();
		try {
		db.deleteEntireDB();
		db.openConnection();
		db.createOrderTable();
		db.insertOrder(1,null,10.0,12,"PAYED","234829476238","2021-05-15","ORDER",-25.0,2);
		
		} 
		catch (SQLException e) {
			
		}
		
		try {
			assertEquals(db.loadOrders().size(),1);
			assertTrue(db.loadOrders().containsKey(1));
		}
		catch (SQLException e) {
			fail();
		}
		
		db.closeConnection();
	}
	
	@Test
	public void testLoadInventoryTable() {
		JDBC db=new JDBC();
		try {
		db.deleteEntireDB();
		db.openConnection();
		db.createProductTypeTable();
		db.insertProductType(5,"1-A-2","234829476238","ciao",10.00,0,80.0,"note1");
		
		} 
		catch (SQLException e) {
			
		}
		
		try {
			assertEquals(db.loadInventory().size(),1);
			assertTrue(db.loadInventory().containsKey("234829476238"));
		}
		catch (SQLException e) {
			fail();
		}
		
		db.closeConnection();
	}

}
