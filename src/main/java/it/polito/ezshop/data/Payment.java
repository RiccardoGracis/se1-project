package it.polito.ezshop.data;

public class Payment {
	private double Amount;
	
	public Payment(double amount) {
		this.Amount=amount;
	}
	
	public double getAmount() {
		return this.Amount;
	}
	
	public void setAmount(double amount) {
		this.Amount=amount;
	}
}
