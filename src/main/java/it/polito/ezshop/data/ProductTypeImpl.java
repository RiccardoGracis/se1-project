package it.polito.ezshop.data;

import java.util.Optional;

public class ProductTypeImpl implements ProductType {
	
	static private Integer idCount = 1;
	private Integer id;
	private Optional<Position> position;
	private String barCode;
	private String description;
	private double sellPrice;
	private int quantity;
	private double discountRate;
	private String notes;
	
	
	
	public ProductTypeImpl(String description, String barCode, double sellPrice, String notes) {
		
		this.id=idCount;
		idCount++;
		this.description = description;
		this.barCode = barCode;
		this.sellPrice = sellPrice;
		this.notes = notes;
		this.position = Optional.empty();
		this.quantity = 0;
		this.discountRate = 0;
	}
	
	public static boolean validateDescr (String description)
    {
        if(description == null || description.equals(""))
            return false;
        return true;
    }
	
	public static boolean validateProdCode (String gtin)
    {
		//Pre-validation
        if(gtin == null || gtin.equals("") )
            return false;

        if(gtin.length()<12 || gtin.length()>14)
            return false;

        if (!gtin.matches("[0-9]+"))
        	return false;
        
        //Validation from https://github.com/TheGeekExplorer/GTIN-Validation/blob/master/Java/CheckGTIN.java
        
        /* Check length of barcode for validity via the checkdigit calculation
         * We split the barcode into its constituent digits, offset them into the GTIN
         * calculation tuple (x1, x3, x1, x3, x1, etc, etc), multiply the digits and add
         * them together, then modulo them on 10, and you get the calculated check digit.
         * For more information see GS1 website: https://www.gs1.org/check-digit-calculator
         * @param string gtin
         * @return bool */
        
        int[] CheckDigitArray = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        int[] gtinMaths       = {3, 1, 3, 1, 3, 1, 3, 1, 3, 1, 3, 1, 3, 1, 3, 1, 3};
        String[] BarcodeArray = gtin.split("(?!^)");
        int gtinLength = gtin.length();
        int modifier = (17 - (gtinLength - 1));
        int gtinCheckDigit = Integer.parseInt(gtin.substring(gtinLength - 1));
        int tmpCheckDigit = 0;
        int tmpCheckSum = 0;
        int tmpMath = 0;
        int i=0;
        int ii=0;
        
        // Run through and put digits into multiplication table
        for (i=0; i < (gtinLength - 1); i++) {
            CheckDigitArray[modifier + i] = Integer.parseInt(BarcodeArray[i]);  // Add barcode digits to Multiplication Table
        }
        
        // Calculate "Sum" of barcode digits
        for (ii=modifier; ii < 17; ii++) {
            tmpCheckSum += (CheckDigitArray[ii] * gtinMaths[ii]);
        }
        
        // Difference from Rounded-Up-To-Nearest-10 - Fianl Check Digit Calculation
        tmpCheckDigit = (int) ((Math.ceil((float) tmpCheckSum / (float) 10) * 10) - tmpCheckSum);
        
        // Check if last digit is same as calculated check digit
        if (gtinCheckDigit == tmpCheckDigit)
            return true;
        return false;
    }
	
	
	@Override
	public Integer getQuantity() {
		
		return quantity;
	}

	@Override
	public void setQuantity(Integer quantity) {
		
		this.quantity = quantity;

	}

	@Override
	public String getLocation() {
		if(position.isPresent())
		{
			Position p= position.get();
			return p.getAisleID()+"-"+p.getRackID()+"-"+p.getLevelID();
		}
		return null;
	}

	@Override
	public void setLocation(String location) {
		
		//Reset location
		if(location==null || location.equals(""))
		{
			position = Optional.empty();
		}
		else {
		String array[]=location.split("-");
		int AisleID = Integer.parseInt(array[0]);
		String RackID = array[1];
		int LevelID = Integer.parseInt(array[2]);
		
		Position p = new Position(AisleID,RackID,LevelID);
		
		position = Optional.of(p);
		}

	}

	@Override
	public String getNote() {
		
		return notes;
	}

	@Override
	public void setNote(String note) {
		
		this.notes = note;

	}

	@Override
	public String getProductDescription() {
		
		return description;
	}

	@Override
	public void setProductDescription(String productDescription) {
		
		this.description=productDescription;

	}

	@Override
	public String getBarCode() {
		
		return barCode;
	}

	@Override
	public void setBarCode(String barCode) {
		
		this.barCode=barCode;

	}

	@Override
	public Double getPricePerUnit() {
		
		return sellPrice;
	}

	@Override
	public void setPricePerUnit(Double pricePerUnit) {
		
		this.sellPrice = pricePerUnit;

	}

	@Override
	public Integer getId() {
		
		return id;
	}

	@Override
	public void setId(Integer id) {
		
		this.id=id;
	}

	public double getDiscountRate() {
		return discountRate;
	}


	public void setDiscountRate(double discountRate) {
		if(discountRate>100)
			this.discountRate=100;
		else
			this.discountRate = discountRate;
	}


	public Double getPPUWithDiscount() {
		
		return sellPrice*(100-discountRate)/(double)100;
	}

}
