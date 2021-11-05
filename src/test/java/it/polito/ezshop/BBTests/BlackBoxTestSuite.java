package it.polito.ezshop.BBTests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import junit.framework.Test;
import junit.framework.TestSuite;

@RunWith(Suite.class)
@Suite.SuiteClasses({PositionTestvalidatePosition.class, ProductTypeImplTestvalidateProdCode.class,
	ProductTypeImplTestvalidateDescr.class, ProductTypeDbBBTests.class, OrderDbBBTests.class, UserImplTestUpdateRole.class, SaleTransactionDBTestBB.class,
		CreditCardBBTests.class, UserInsertDbBBTests.class, LoyaltyCardInsertDbBBTests.class})
public class BlackBoxTestSuite {

}
