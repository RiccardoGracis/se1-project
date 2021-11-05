package it.polito.ezshop;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import it.polito.ezshop.BBTests.BlackBoxTestSuite;
import it.polito.ezshop.WBTests.WhiteBoxTestSuite;
import junit.framework.Test;
import junit.framework.TestSuite;


/**************** SUITE FOR UNIT TESTS HERE (INCLUDES BB+WB SUITES) **************/

@RunWith(Suite.class)
@Suite.SuiteClasses({BlackBoxTestSuite.class,WhiteBoxTestSuite.class})
public class TestEzShops {
	
}
