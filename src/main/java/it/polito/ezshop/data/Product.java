package it.polito.ezshop.data;

public class Product {

	private String RFID;
	private String barCode;
	
	public Product(String RFID, String barCode) {
		
		this.RFID = RFID;
		this.barCode = barCode;
	}
	
	public static boolean validateRFID (String RFID) {
		
		
        if(RFID == null || RFID.equals("") )
            return false;

        if(RFID.length()!=12)
            return false;

        if (!RFID.matches("[0-9]+"))
        	return false;
		
		return true;
	}

	public String getRFID() {
		return RFID;
	}

	public String getBarCode() {
		return barCode;
	}
	
	
	
	
}
