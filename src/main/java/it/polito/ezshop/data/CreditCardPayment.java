package it.polito.ezshop.data;

public class CreditCardPayment extends Payment {
	private CreditCard CC;
	
	public CreditCardPayment(double amount,CreditCard c) {
		super(amount);
		this.CC=c;
	}
	public CreditCard getCC() {
		return this.CC;
	}
	public void setCC(CreditCard c) {
		this.CC = c;
	}
	
}
