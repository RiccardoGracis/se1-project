package it.polito.ezshop.data;

public class Position {
	
	private int aisleID;
	private String rackID;
	private int levelID;
	
	public Position(int aisleID, String rackID, int levelID) {
		this.aisleID = aisleID;
		this.rackID = rackID;
		this.levelID = levelID;
	}
	
	public static boolean validatePosition (String position) {

        /* Here we have already checked if location is null
         * or empty. So we don't expect a null or empty value */

        if(position==null || position.equals(""))
            return false;

        String array[]=position.split("-");
        
        if(array.length!=3)
        	return false;

        try {
            Integer.parseInt(array[0]);
        }
        catch (NumberFormatException e)
        {
            return false;
        }

        try {
            Integer.parseInt(array[2]);
        }
        catch (NumberFormatException e)
        {
            return false;
        }

        //Validation of alphabetic identifier
        return array[1].matches("[a-zA-Z]+");

    }

	public int getAisleID() {
		return aisleID;
	}

	public void setAisleID(int aisleID) {
		this.aisleID = aisleID;
	}

	public String getRackID() {
		return rackID;
	}

	public void setRackID(String rackID) {
		this.rackID = rackID;
	}

	public int getLevelID() {
		return levelID;
	}

	public void setLevelID(int levelID) {
		this.levelID = levelID;
	}
	
	
	
	
	
	

}
