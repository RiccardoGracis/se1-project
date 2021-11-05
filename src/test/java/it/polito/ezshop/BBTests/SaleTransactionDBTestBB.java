package it.polito.ezshop.BBTests;

import static org.junit.Assert.*;

import it.polito.ezshop.data.JDBC;
import it.polito.ezshop.data.SaleTransactionImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.sqlite.SQLiteException;

import java.sql.SQLException;
import java.util.Arrays;

public class SaleTransactionDBTestBB {
    @Before
    public void initDbEnvironment(){
        JDBC db=new JDBC();
        try{ //Delete previous data
            db.deleteEntireDB();
        }
        catch (SQLException e){
            fail(e.getErrorCode() + "\n\t" + Arrays.toString(e.getStackTrace()));
        }
       try { //Re-create tables
           db.initDB(null,null,null,null);
       } catch (SQLException e) {
           fail(e.getErrorCode() + "\n\t" + Arrays.toString(e.getStackTrace()));
       }
   }
   
    @Test //Correct execution
    public void testAddSaleTransaction(){
        SaleTransactionImpl s = new SaleTransactionImpl(0.0,"CLOSED");
        JDBC db=new JDBC();
        try {
            db.addSaleTransactionDB(s);
        } catch (SQLException e) {
            fail(e.getErrorCode() + "\n\t" + Arrays.toString(e.getStackTrace()));
        }
    }

    @Test //Add sale to non existent table
    public void testAddSaleTransaction_Error01(){
        SaleTransactionImpl s = new SaleTransactionImpl(0.0,"CLOSED");
        JDBC db=new JDBC();
        try{
            db.deleteEntireDB();
        }
        catch (SQLException e){
            fail(e.getErrorCode() + "\n\t" + Arrays.toString(e.getStackTrace()));
        }
        Assert.assertThrows(SQLException.class, () -> db.addSaleTransactionDB(s));
    }

    @Test //Add redundant sale to existent table
    public void testAddSaleTransaction_Error02(){
        SaleTransactionImpl s = new SaleTransactionImpl(0.0,"CLOSED");
        SaleTransactionImpl sRedundant = new SaleTransactionImpl(0.0,"CLOSED");
        sRedundant.setBalanceId(s.getBalanceId());
        JDBC db=new JDBC();
        try {
            db.addSaleTransactionDB(s);
        } catch (SQLException e) {
           fail(e.getErrorCode() + "\n\t" + Arrays.toString(e.getStackTrace()));
        }
        Assert.assertThrows(SQLException.class, () -> db.addSaleTransactionDB(sRedundant));
    }

    @Test //Add null sale to existent table
    public void testAddSaleTransaction_Error03(){
        JDBC db=new JDBC();
        Assert.assertThrows(java.lang.NullPointerException.class, () -> db.addSaleTransactionDB(null));
    }

    @Test //Correct execution
    public void testDeleteSaleTransactionDB(){
        SaleTransactionImpl s = new SaleTransactionImpl(0.0,"CLOSED");
        JDBC db=new JDBC();
        try {
            db.deleteSaleTransactionDB(s);
        } catch (SQLException e) {
            fail(e.getErrorCode() + "\n\t" + Arrays.toString(e.getStackTrace()));
        }
    }

    @Test //Delete from an nonexistent table
    public void testDeleteSaleTransactionDB_Error01(){
        SaleTransactionImpl s = new SaleTransactionImpl(0.0,"CLOSED");
        JDBC db=new JDBC();
        try{
            db.deleteEntireDB();
        }
        catch (SQLException e){
            fail(e.getErrorCode() + "\n\t" + Arrays.toString(e.getStackTrace()));
        }
        Assert.assertThrows(SQLException.class, () -> db.deleteSaleTransactionDB(s));
    }

    @Test //Delete nonexistent saleTransaction (no exception are arisen)
    public void testDeleteSaleTransactionDB_Error02(){
        SaleTransactionImpl s = new SaleTransactionImpl(0.0,"CLOSED");
        JDBC db=new JDBC();
        try {
            db.addSaleTransactionDB(s);
        } catch (SQLException e) {
            fail(e.getErrorCode() + "\n\t" + Arrays.toString(e.getStackTrace()));
        }
        s.setBalanceId(s.getBalanceId() + 1);
        try {
            db.deleteSaleTransactionDB(s);
        } catch (SQLException e) {
            fail(e.getErrorCode() + "\n\t" + Arrays.toString(e.getStackTrace()));
        }
    }

    @Test //Delete null saleTransaction
    public void testDeleteSaleTransactionDB_Error03(){
        JDBC db=new JDBC();
        Assert.assertThrows(java.lang.NullPointerException.class, () -> db.deleteSaleTransactionDB(null));
    }
}
