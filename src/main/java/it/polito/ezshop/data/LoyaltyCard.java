package it.polito.ezshop.data;

public class LoyaltyCard {

	private String id;
	private int points;
	private boolean assigned;
	
	public LoyaltyCard(String id, int points, boolean assigned) {
		this.id = id;
		this.points = points;
		this.assigned = assigned;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public boolean isAssigned() {
		return assigned;
	}

	public void setAssigned(boolean assigned) {
		this.assigned = assigned;
	}
	
	public int modifyPointsOnCard(int pointsToBeAdded) {
		if(points + pointsToBeAdded < 0 )
			return -1;
		points += pointsToBeAdded;
		return points;
	}

	public void detach() {
		this.points = 0;
		this.assigned = false;
	}

	

}
