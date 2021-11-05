package it.polito.ezshop.data;

public class CashPayment extends Payment{
	private double change;

	public CashPayment(double amount,double change) {
		super(amount);
		this.change=change;
	}

	public double getChange() {
		return this.change;
	}

	public void setChange(double change) {
		this.change = change;
	}
}
