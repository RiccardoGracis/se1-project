package it.polito.ezshop.data;

import java.time.LocalDate;

public class OrderImpl implements Order {

	static private Integer idCount = 1;
	private Integer id;
	private String supplier;
	private double pricePerUnit;
	private int quantity;
	private String status;
	private String productCode;
	private Debit b;




	public OrderImpl(String productCode, int quantity, double pricePerUnit, String status) {
		//Debit constructor
		this.b=new Debit(LocalDate.now(),"ORDER",(-1)*(quantity*pricePerUnit));
		this.id=idCount;
		idCount++;
		this.productCode = productCode;
		this.quantity = quantity;
		this.pricePerUnit = pricePerUnit;
		if(status.equals("ORDERED") || status.equals("PAYED") || status.equals("ISSUED") || status.equals("COMPLETED"))
			this.status=status;
		//No supplier management
		this.supplier = null;
	}

	@Override
	public String getProductCode() {
		return productCode;
	}

	@Override
	public void setProductCode(String productCode) {
		this.productCode=productCode;

	}

	@Override
	public double getPricePerUnit() {

		return pricePerUnit;
	}

	@Override
	public void setPricePerUnit(double pricePerUnit) {
		this.pricePerUnit = pricePerUnit;

	}

	@Override
	public int getQuantity() {
		return quantity;
	}

	@Override
	public void setQuantity(int quantity) {
		this.quantity=quantity;

	}

	@Override
	public String getStatus() {

		return status;
	}

	@Override
	public void setStatus(String status) {
		if(status.equals("ORDERED") || status.equals("PAYED") || status.equals("ISSUED") || status.equals("COMPLETED"))
			this.status=status;

	}

	@Override
	public Integer getOrderId() {

		return id;
	}

	@Override
	public void setOrderId(Integer orderId) {
		this.id=orderId;

	}

	@Override
	public Integer getBalanceId() {
		return b.getBalanceId();
	}

	@Override
	public void setBalanceId(Integer balanceId) {
		b.setBalanceId(balanceId);

	}

	public Debit getBalance() {
		return b;
	}

	public void setBalance(Debit b) {
		this.b = b;
	}
	
	

}

