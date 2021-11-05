package it.polito.ezshop;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import it.polito.ezshop.integrationTests.*;
import junit.framework.Test;
import junit.framework.TestSuite;

/**************** SUITE FOR INTEGRATION TESTS HERE **************/

@RunWith(Suite.class)
@Suite.SuiteClasses({
		CartItem_IntegrationTest.class,ReturnTransaction_IntegrationTest.class,ReturnTransactionDB_IntegrationTest.class,ReturnTransactionIntegrationTest.class,SaleReturnTransactionLoadDB_IntegrationTest.class,
		SaleTransactionDB_IntegrationTest.class,SaleTransactionImpl_IntegrationTest.class,SaleTransactionIntegrationTest.class,
		AccountBook_IntT.class,CreditCard_IntT.class,ProductTypeImpl_Position_Int.class,CustomerDb_Int.class, CustomerImpl_Int.class,
		EzShop_Order_ProdType_Int.class,EzShop_Payments_IntT.class,EzShop_ProdType_Position_Int.class,EzShop_ProductTypeImpl_Int.class,EzShop_Balance_IntT.class,EzShop_Reset_IntT.class,EZShop_Customers_Int.class,
		EZShop_Login_Logout_Int.class,EZShop_Users_Int.class,EzShop_Sale_Return_RFID_IntT.class,
		UC3Scenarios.class,UC1Scenarios.class,UC2Scenarios.class,UC4Scenarios.class,UC5Scenarios.class,UC6Scenarios.class,UC8Scenarios.class	
})
public class TestEzShopIntegrationSuite {

}
