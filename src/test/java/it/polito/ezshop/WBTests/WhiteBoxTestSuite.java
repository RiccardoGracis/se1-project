package it.polito.ezshop.WBTests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import junit.framework.Test;
import junit.framework.TestSuite;

@RunWith(Suite.class)
@Suite.SuiteClasses({PositionWBTests.class, ProductTypeImplWBTests.class,
	OrderImplWBTests.class,PositionDbWBTests.class,BalanceOperationImplWBTests.class,
	CashPaymentWBTests.class,CreditCardWBTests.class,PaymentWBTests.class,AccountBookWBTests.class, LoyaltyCardWBTests.class, LoyaltyCardDbWBTests.class, UserImplWBTests.class, UserDbWBTests.class,
	SaleTransactionImplTestWB.class, CreditCardWBTests.class,CreditCardPaymentWBTests.class,BalanceDBTests.class,ProductWBTests.class})


public class WhiteBoxTestSuite {

}
