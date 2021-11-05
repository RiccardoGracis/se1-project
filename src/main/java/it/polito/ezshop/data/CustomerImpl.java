package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.InvalidCustomerCardException;
import it.polito.ezshop.exceptions.InvalidCustomerNameException;


public class CustomerImpl implements Customer {

	private Integer id;
	private String customerName;
	private LoyaltyCard loyaltyCard;

	public CustomerImpl(Integer id, String customerName) {
		this.id = id;
		this.customerName = customerName;
	}

	@Override
	public String getCustomerName() {
		return this.customerName;
	}

	@Override
	public void setCustomerName(String customerName) {
		this.customerName = customerName;

	}

	@Override
	public String getCustomerCard() {
		if (loyaltyCard == null) 
			return null;
		return this.loyaltyCard.getId();
	}

	@Override
	public void setCustomerCard(String customerCard) {
		this.loyaltyCard = new LoyaltyCard(customerCard, 0, true);
	}

	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public Integer getPoints() {
		if (loyaltyCard == null) 
			return 0;
		return this.loyaltyCard.getPoints();
	}

	@Override
	public void setPoints(Integer points) {
		if(this.loyaltyCard != null)
			this.loyaltyCard.setPoints(points);
	}

	public void modifyCustomer(String newCustomerName, String newCustomerCard) throws InvalidCustomerNameException, InvalidCustomerCardException {

		if (newCustomerName == null || newCustomerName.isEmpty())
			throw new InvalidCustomerNameException();
		
		if (newCustomerCard != null) {
			if(!newCustomerCard.matches("^[0-9]{10}$") && !newCustomerCard.isEmpty())
				throw new InvalidCustomerCardException();
			if (!newCustomerCard.isEmpty())
				this.setCustomerCard(newCustomerCard);
			if (newCustomerCard.isEmpty())
				this.setCustomerCard(null);
		}
		this.setCustomerName(newCustomerName);
		
	}

	public void attachCardToCustomer(LoyaltyCard loyaltyCard) {
		if (loyaltyCard != null)
			loyaltyCard.setAssigned(true);
		this.loyaltyCard = loyaltyCard;	}

	public void detachCardFromCustomer() {
		this.loyaltyCard = null;
	}

}
