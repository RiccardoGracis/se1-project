package it.polito.ezshop.integrationTests;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;

import org.junit.Test;

import it.polito.ezshop.data.CreditCard;
import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.EZShopInterface;

public class CreditCard_IntT {

	@Test
	public void testLoadCC() throws IOException {
		HashMap<String,CreditCard> cc = new HashMap<String,CreditCard>();
		CreditCard.resetCCFile("creditcards.txt");
		CreditCard.LoadCC("creditcards.txt", cc);
		assertFalse(cc.isEmpty());
		assertEquals(cc.size(),3);
		assertEquals(cc.get("4485370086510891").getBalance(),150,0);
		assertEquals(cc.get("5100293991053009").getBalance(),10,0);
		assertEquals(cc.get("4716258050958645").getBalance(),0,0);
	}
	@Test
	public void testUpdateCC() throws IOException {
		HashMap<String,CreditCard> cc = new HashMap<String,CreditCard>();
		CreditCard.resetCCFile("creditcards.txt");
		CreditCard.LoadCC("creditcards.txt", cc);
		
		cc.get("4716258050958645").updateBalance(-15);
		assertEquals(cc.get("4716258050958645").getBalance(),15,0);
		
		cc.clear();
		CreditCard.LoadCC("creditcards.txt", cc);
		assertEquals(cc.get("4716258050958645").getBalance(),15,0);
		
		cc.get("4716258050958645").updateBalance(15);		//TO LEAVE FILE UNCHANGED
	}
}
