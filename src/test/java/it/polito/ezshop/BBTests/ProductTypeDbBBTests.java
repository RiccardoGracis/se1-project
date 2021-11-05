package it.polito.ezshop.BBTests;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.Test;
import it.polito.ezshop.data.*;

public class ProductTypeDbBBTests {
	
	@Test
	public void testInsertPT() {
		JDBC db=new JDBC();
		try {
		db.deleteEntireDB();
		db.openConnection();
		db.createProductTypeTable();
		
		} 
		catch (SQLException e) {
			
		}
		
		try {
		db.insertProductType(5,"1-A-2","234829476238","ciao",10.00,0,80.0,"note1");} 
		catch (SQLException e) {
			fail();
		}
		
		db.closeConnection();
		
	}
	
	@Test
	public void testInsertPTinvDR() {
		
		JDBC db=new JDBC();
		try {
		db.deleteEntireDB();
		db.openConnection();
		db.createProductTypeTable();
		
		} 
		catch (SQLException e) {
			
		}
		
		boolean res=false;
		try {
		db.insertProductType(5,"1-A-2","234829476238","ciao",2.00,10,-1.0,"note1");}
		catch (SQLException e) {
			res=true;
		}
		assertTrue(res);
		db.closeConnection();
		
	}
	
	@Test
	public void testInsertPTinvDR2() {
		
		JDBC db=new JDBC();
		try {
		db.deleteEntireDB();
		db.openConnection();
		db.createProductTypeTable();
		
		} 
		catch (SQLException e) {
			
		}
		
		boolean res=false;
		try {
		db.insertProductType(5,"1-A-2","234829476238","ciao",2.00,10,120.0,"note1");}
		catch (SQLException e) {
			res=true;
		}
		assertTrue(res);
		db.closeConnection();
		
	}
	
	@Test
	public void testInsertPTinvQTY() {
		
		JDBC db=new JDBC();
		try {
		db.deleteEntireDB();
		db.openConnection();
		db.createProductTypeTable();
		
		} 
		catch (SQLException e) {
			
		}
		
		boolean res=false;
		try {
		db.insertProductType(5,"1-A-2","234829476238","ciao",2.00,-10,80.0,"note1");}
		catch (SQLException e) {
			res=true;
		}
		assertTrue(res);
		db.closeConnection();
		
	}
	
	@Test
	public void testInsertPTinvSP() {
		
		JDBC db=new JDBC();
		try {
		db.deleteEntireDB();
		db.openConnection();
		db.createProductTypeTable();
		
		} 
		catch (SQLException e) {
			
		}
		
		boolean res=false;
		try {
		db.insertProductType(5,"1-A-2","234829476238","ciao",-2.00,10,80.0,"note1");}
		catch (SQLException e) {
			res=true;
		}
		assertTrue(res);
		db.closeConnection();
		
	}
	
	@Test
	public void testInsertPTinvSP2() {
		
		JDBC db=new JDBC();
		try {
		db.deleteEntireDB();
		db.openConnection();
		db.createProductTypeTable();
		
		} 
		catch (SQLException e) {
			
		}
		
		boolean res=false;
		try {
		db.insertProductType(5,"1-A-2","234829476238","ciao",0.00,10,80.0,"note1");}
		catch (SQLException e) {
			res=true;
		}
		assertTrue(res);
		db.closeConnection();
		
	}
	
	@Test
	public void testInsertPTinvID() {
		
		JDBC db=new JDBC();
		try {
		db.deleteEntireDB();
		db.openConnection();
		db.createProductTypeTable();
		
		} 
		catch (SQLException e) {
			
		}
		
		boolean res=false;
		try {
		db.insertProductType(-55,"1-A-2","234829476238","ciao",10.00,10,80.0,"note1");}
		catch (SQLException e) {
			res=true;
		}
		assertTrue(res);
		db.closeConnection();
		
	}
	
	@Test
	public void testInsertPTinvID2() {
		
		JDBC db=new JDBC();
		try {
		db.deleteEntireDB();
		db.openConnection();
		db.createProductTypeTable();
		
		} 
		catch (SQLException e) {
			
		}
		
		boolean res=false;
		try {
		db.insertProductType(0,"1-A-2","234829476238","ciao",10.00,10,80.0,"note1");}
		catch (SQLException e) {
			res=true;
		}
		assertTrue(res);
		db.closeConnection();
		
	}
	
	//UpdateProductType
	@Test
	public void testUpdatePT() {
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
		db.updateProductType(5,"1-A-2","234829476238","ciaoa",10.00,0,80.0,"note1");} 
		catch (SQLException e) {
			e.printStackTrace();
			fail();
		}
		
		db.closeConnection();
		
	}
	
	
	@Test
	public void testUpdatePTinvDR() {
		
		JDBC db=new JDBC();
		try {
		db.deleteEntireDB();
		db.openConnection();
		db.createProductTypeTable();
		
		} 
		catch (SQLException e) {
			
		}
		
		boolean res=false;
		try {
		db.updateProductType(5,"1-A-2","234829476238","ciao",2.00,10,-1.0,"note1");}
		catch (SQLException e) {
			res=true;
		}
		assertTrue(res);
		db.closeConnection();
		
	}
	
	@Test
	public void testUpdatePTinvDR2() {
		
		JDBC db=new JDBC();
		try {
		db.deleteEntireDB();
		db.openConnection();
		db.createProductTypeTable();
		
		} 
		catch (SQLException e) {
			
		}
		
		boolean res=false;
		try {
		db.updateProductType(5,"1-A-2","234829476238","ciao",2.00,10,120.0,"note1");}
		catch (SQLException e) {
			res=true;
		}
		assertTrue(res);
		db.closeConnection();
		
	}
	
	@Test
	public void testUpdatePTinvQTY() {
		
		JDBC db=new JDBC();
		try {
		db.deleteEntireDB();
		db.openConnection();
		db.createProductTypeTable();
		
		} 
		catch (SQLException e) {
			
		}
		
		boolean res=false;
		try {
		db.updateProductType(5,"1-A-2","234829476238","ciao",2.00,-10,80.0,"note1");}
		catch (SQLException e) {
			res=true;
		}
		assertTrue(res);
		db.closeConnection();
		
	}
	
	@Test
	public void testUpdatePTinvSP() {
		
		JDBC db=new JDBC();
		try {
		db.deleteEntireDB();
		db.openConnection();
		db.createProductTypeTable();
		
		} 
		catch (SQLException e) {
			
		}
		
		boolean res=false;
		try {
		db.updateProductType(5,"1-A-2","234829476238","ciao",-2.00,10,80.0,"note1");}
		catch (SQLException e) {
			res=true;
		}
		assertTrue(res);
		db.closeConnection();
		
	}
	
	@Test
	public void testUpdatePTinvSP2() {
		
		JDBC db=new JDBC();
		try {
		db.deleteEntireDB();
		db.openConnection();
		db.createProductTypeTable();
		
		} 
		catch (SQLException e) {
			
		}
		
		boolean res=false;
		try {
		db.updateProductType(5,"1-A-2","234829476238","ciao",0.00,10,80.0,"note1");}
		catch (SQLException e) {
			res=true;
		}
		assertTrue(res);
		db.closeConnection();
		
	}
	
	@Test
	public void testUpdatePTinvID() {
		
		JDBC db=new JDBC();
		try {
		db.deleteEntireDB();
		db.openConnection();
		db.createProductTypeTable();
		
		} 
		catch (SQLException e) {
			
		}
		
		boolean res=false;
		try {
		db.updateProductType(-55,"1-A-2","234829476238","ciao",10.00,10,80.0,"note1");}
		catch (SQLException e) {
			res=true;
		}
		assertTrue(res);
		db.closeConnection();
		
	}
	
	@Test
	public void testUpdatePTinvID2() {
		
		JDBC db=new JDBC();
		try {
		db.deleteEntireDB();
		db.openConnection();
		db.createProductTypeTable();
		
		} 
		catch (SQLException e) {
			
		}
		
		boolean res=false;
		try {
		db.updateProductType(0,"1-A-2","234829476238","ciao",10.00,10,80.0,"note1");}
		catch (SQLException e) {
			res=true;
		}
		assertTrue(res);
		db.closeConnection();
		
	}

}
