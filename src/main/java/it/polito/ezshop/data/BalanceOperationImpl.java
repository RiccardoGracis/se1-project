package it.polito.ezshop.data;

import java.time.LocalDate;

public class BalanceOperationImpl implements BalanceOperation {
	private int BalanceId;
	private LocalDate Date;
	private String Type;
	private double Money;
	private static int idCounter=0;
	
	public BalanceOperationImpl( LocalDate date, String type, double money) {
		this.BalanceId = ++idCounter;
		this.Date = date;
		this.Type = type;
		this.Money = money;
	}

	@Override
	public int getBalanceId() {
		return this.BalanceId;
	}

	@Override
	public void setBalanceId(int balanceId) {
		this.BalanceId=balanceId;
	}

	@Override
	public LocalDate getDate() {
		return this.Date;
	}

	@Override
	public void setDate(LocalDate date) {
		this.Date=date;
	}

	@Override
	public double getMoney() {
		return this.Money;
	}

	@Override
	public void setMoney(double money) {
		this.Money=money;
	}

	@Override
	public String getType() {
		return this.Type;
	}

	@Override
	public void setType(String type) {
		this.Type=type;
	}

	//Added by Riccardo in order to interface correctly with DB.
	public static void setCounterFromDb(Integer saleMaxId, Integer returnMaxId, Integer orderMaxId){
		idCounter = Integer.max(Integer.max(saleMaxId, returnMaxId),orderMaxId);
	}

}
