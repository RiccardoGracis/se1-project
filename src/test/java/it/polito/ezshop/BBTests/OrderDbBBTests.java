package it.polito.ezshop.BBTests;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.Test;
import it.polito.ezshop.data.*;

public class OrderDbBBTests {
	
	@Test
	public void testInsertOrd() {
		JDBC db=new JDBC();
		try {
		db.deleteEntireDB();
		db.openConnection();
		db.createOrderTable();
		
		} 
		catch (SQLException e) {
			
		}
		
		try {
		db.insertOrder(1,null,10.0,12,"PAYED","234829476238","2021-05-15","ORDER",-25.0,2);} 
		catch (SQLException e) {
			fail();
		}
		
		db.closeConnection();
	}
	
	@Test
	public void testInsertOrdInvQT() {
		
		JDBC db=new JDBC();
		try {
		db.deleteEntireDB();
		db.openConnection();
		db.createOrderTable();
		
		} 
		catch (SQLException e) {
			
		}
		
		boolean res=false;
		try {
		db.insertOrder(1,null,10.0,-12,"PAYED","234829476238","2021-05-15","ORDER",-25.0,2);}
		catch (SQLException e) {
			res=true;
		}
		assertTrue(res);
		db.closeConnection();
		
	}
	
	@Test
	public void testInsertOrdInvQT2() {
		
		JDBC db=new JDBC();
		try {
		db.deleteEntireDB();
		db.openConnection();
		db.createOrderTable();
		
		} 
		catch (SQLException e) {
			
		}
		
		boolean res=false;
		try {
		db.insertOrder(1,null,10.0,0,"PAYED","234829476238","2021-05-15","ORDER",-25.0,2);}
		catch (SQLException e) {
			res=true;
		}
		assertTrue(res);
		db.closeConnection();
		
	}
	
	@Test
	public void testInsertOrdInvPPU() {
		
		JDBC db=new JDBC();
		try {
		db.deleteEntireDB();
		db.openConnection();
		db.createOrderTable();
		
		} 
		catch (SQLException e) {
			
		}
		
		boolean res=false;
		try {
		db.insertOrder(1,null,-10.0,10,"PAYED","234829476238","2021-05-15","ORDER",-25.0,2);}
		catch (SQLException e) {
			res=true;
		}
		assertTrue(res);
		db.closeConnection();
		
	}
	
	@Test
	public void testInsertOrdInvPPU2() {
		
		JDBC db=new JDBC();
		try {
		db.deleteEntireDB();
		db.openConnection();
		db.createOrderTable();
		
		} 
		catch (SQLException e) {
			
		}
		
		boolean res=false;
		try {
		db.insertOrder(1,null,0.0,10,"PAYED","234829476238","2021-05-15","ORDER",-25.0,2);}
		catch (SQLException e) {
			res=true;
		}
		assertTrue(res);
		db.closeConnection();
		
	}
	
	@Test
	public void testInsertOrdInvID() {
		
		JDBC db=new JDBC();
		try {
		db.deleteEntireDB();
		db.openConnection();
		db.createOrderTable();
		
		} 
		catch (SQLException e) {
			
		}
		
		boolean res=false;
		try {
		db.insertOrder(-1,null,10.0,10,"PAYED","234829476238","2021-05-15","ORDER",-25.0,2);}
		catch (SQLException e) {
			res=true;
		}
		assertTrue(res);
		db.closeConnection();
		
	}
	
	@Test
	public void testInsertOrdInvID2() {
		
		JDBC db=new JDBC();
		try {
		db.deleteEntireDB();
		db.openConnection();
		db.createOrderTable();
		
		} 
		catch (SQLException e) {
			
		}
		
		boolean res=false;
		try {
		db.insertOrder(0,null,10.0,10,"PAYED","234829476238","2021-05-15","ORDER",-25.0,2);}
		catch (SQLException e) {
			res=true;
		}
		assertTrue(res);
		db.closeConnection();
		
	}
	
	//UpdateOrder
	
	@Test
	public void testUpdateOrd() {
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
		db.updateOrder(1,null,10.0,12,"PAYED","234829476238");} 
		catch (SQLException e) {
			e.printStackTrace();
			fail();
		}
		
		db.closeConnection();
	}
	
	@Test
	public void testUpdateOrdInvQT() {
		
		JDBC db=new JDBC();
		try {
		db.deleteEntireDB();
		db.openConnection();
		db.createOrderTable();
		
		} 
		catch (SQLException e) {
			
		}
		
		boolean res=false;
		try {
		db.updateOrder(1,null,10.0,-12,"PAYED","234829476238");}
		catch (SQLException e) {
			res=true;
		}
		assertTrue(res);
		db.closeConnection();
		
	}
	
	@Test
	public void testUpdateOrdInvQT2() {
		
		JDBC db=new JDBC();
		try {
		db.deleteEntireDB();
		db.openConnection();
		db.createOrderTable();
		
		} 
		catch (SQLException e) {
			
		}
		
		boolean res=false;
		try {
		db.updateOrder(1,null,10.0,0,"PAYED","234829476238");}
		catch (SQLException e) {
			res=true;
		}
		assertTrue(res);
		db.closeConnection();
		
	}
	
	@Test
	public void testUpdateOrdInvPPU() {
		
		JDBC db=new JDBC();
		try {
		db.deleteEntireDB();
		db.openConnection();
		db.createOrderTable();
		
		} 
		catch (SQLException e) {
			
		}
		
		boolean res=false;
		try {
		db.updateOrder(1,null,-10.0,10,"PAYED","234829476238");}
		catch (SQLException e) {
			res=true;
		}
		assertTrue(res);
		db.closeConnection();
		
	}
	
	@Test
	public void testUpdateOrdInvPPU2() {
		
		JDBC db=new JDBC();
		try {
		db.deleteEntireDB();
		db.openConnection();
		db.createOrderTable();
		
		} 
		catch (SQLException e) {
			
		}
		
		boolean res=false;
		try {
		db.updateOrder(1,null,0.0,10,"PAYED","234829476238");}
		catch (SQLException e) {
			res=true;
		}
		assertTrue(res);
		db.closeConnection();
		
	}
	
	@Test
	public void testUpdateOrdInvID() {
		
		JDBC db=new JDBC();
		try {
		db.deleteEntireDB();
		db.openConnection();
		db.createOrderTable();
		
		} 
		catch (SQLException e) {
			
		}
		
		boolean res=false;
		try {
		db.updateOrder(-1,null,10.0,10,"PAYED","234829476238");}
		catch (SQLException e) {
			res=true;
		}
		assertTrue(res);
		db.closeConnection();
		
	}
	
	@Test
	public void testUpdateOrdInvID2() {
		
		JDBC db=new JDBC();
		try {
		db.deleteEntireDB();
		db.openConnection();
		db.createOrderTable();
		
		} 
		catch (SQLException e) {
			
		}
		
		boolean res=false;
		try {
		db.updateOrder(0,null,10.0,10,"PAYED","234829476238");}
		catch (SQLException e) {
			res=true;
		}
		assertTrue(res);
		db.closeConnection();
		
	}
	

}
