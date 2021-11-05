package it.polito.ezshop.data;

import java.time.LocalDate;

class Credit extends BalanceOperationImpl{
	public Credit(LocalDate date, String type, double money) {
		super( date, type, money);
	}
}
