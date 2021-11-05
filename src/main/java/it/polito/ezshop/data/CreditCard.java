package it.polito.ezshop.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class CreditCard {
	private String number;
	private boolean valid;
	private double balance;

	public CreditCard(String number,double balance) {
		this.number = number;
		this.valid=validate(number);
		this.balance=balance;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public void updateBalance(double balance) {		
		this.balance -= balance;
		updateCC("creditcards.txt",this.number,this.balance);
	}
	public String getNumber() {
		return this.number;
	}
	public void setNumber(String number) {
		this.number = number;
		this.valid=validate(number);
	}
	public boolean isValid() {
		return valid;
	}
	public static boolean validate(String number)	{
		// Luhn Alg
		int nDigits = number.length();
		int nSum = 0;
		boolean isSecond = false;
		if(nDigits<13 || nDigits>19)
			return false;
		for (int i = nDigits - 1; i >= 0; i--)
		{
			int d = number.charAt(i) - '0';
			if (isSecond == true)
				d = d * 2;
			nSum += d / 10;
			nSum += d % 10;

			isSecond = !isSecond;
		}
		return (nSum % 10 == 0);
	}
	
	//TEST
	public static void LoadCC(String path, HashMap<String,CreditCard> cc) {
		try {
			FileReader f = new FileReader(path);
			BufferedReader b = new BufferedReader(f);
			String line,args[];
			while((line=b.readLine())!=null){
				if(line.startsWith("#")) 
					continue;
				args=line.split(";");
				cc.put(args[0],new CreditCard(args[0],Double.valueOf(args[1])));	
			}
			f.close();
			b.close();
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void updateCC(String file, String creditcard, double total){
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line, newline,content = "";
			while((line = br.readLine())!= null){
				if(line.startsWith("#")) {
					content=content+line+"\n";
					continue;
				}
				newline = line;
				String l[] = line.split(";");
				if(l[0].equalsIgnoreCase(creditcard)) 
					newline= new String(l[0] + ";"+ String.valueOf(total) );
				content=content+ newline+ "\n";
			}
			br.close();
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			bw.write(content);
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void resetCCFile(String file) throws IOException {
		String content="#Do not delete the lines preceded by an \"#\" and do not modify the first three credit cards\r\n"
				+ "#since they will be used in the acceptance tests.\r\n"
				+ "#The lines preceded by an \"#\" must be ignored.\r\n"
				+ "#Here you can add all the credit card numbers you need with their balance. The format MUST be :\r\n"
				+ "#<creditCardNumber>;<balance>\r\n"
				+ "4485370086510891;150.00\r\n"
				+ "5100293991053009;10.00\r\n"
				+ "4716258050958645;0.00";
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));
		bw.write(content);
		bw.close();
	}
}

