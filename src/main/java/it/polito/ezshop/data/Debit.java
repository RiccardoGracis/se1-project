package it.polito.ezshop.data;

import java.time.LocalDate;

public class Debit extends BalanceOperationImpl {

	public Debit( LocalDate date, String type, double money) {
		super( date, type, money);
	}

}
