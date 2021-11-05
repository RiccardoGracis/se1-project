package it.polito.ezshop.data;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class AccountBook {
	private double balance;
	private TreeMap<LocalDate,ArrayList<BalanceOperation>> Transactions;

	public AccountBook() {
		balance=0.00;
		Transactions= new TreeMap<LocalDate,ArrayList<BalanceOperation>>();
	}

	public double getBalance() {
		return this.balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public List<BalanceOperation> getByDate(LocalDate from,LocalDate to){
		if(from==null && to==null)
			return Transactions.values().stream()
					.flatMap(Collection::stream).collect(Collectors.toList());
		else if(from!=null && to==null)
			return Transactions.subMap(from, true, Transactions.lastKey(), true).values().stream()
					.flatMap(Collection::stream).collect(Collectors.toList());
		else if(from==null && to!=null)
			return Transactions.subMap(Transactions.firstKey(), true, to, true).values().stream()
					.flatMap(Collection::stream).collect(Collectors.toList());
		else
			return Transactions.subMap(from,true, to,true).values().stream()
					.flatMap(Collection::stream).collect(Collectors.toList());
	}
	public void updateBalance(double value) {																	//TODO UNIFICARE
		this.balance+=value;
		try {
			EZShop.db.openConnection();
			EZShop.db.updateBalance(this.balance);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			EZShop.db.closeConnection();
		}
		
	}
	public void addBalanceOperation(BalanceOperation op) {
		LocalDate date=op.getDate();
		if(Transactions.containsKey(date))
			Transactions.get(date).add(op);
		else
			Transactions.put(date, new ArrayList<BalanceOperation>(Arrays.asList(op)));
		updateBalance(op.getMoney());
	}
	public void LoadTransactions(HashMap <Integer,Order> orders,HashMap<Integer, SaleTransaction> saleTransactions,HashMap<Integer, ReturnTransaction> returnTransactions) {
		orders.values().stream().
			filter(x -> x.getStatus().equals("PAYED")).forEach(x-> addBalanceOperation( ((OrderImpl)x).getBalance() ) );
		saleTransactions.values().stream().
			filter(x -> (((SaleTransactionImpl) x)).getStatus().equals("CLOSED")).forEach(x->addBalanceOperation((SaleTransactionImpl) x));
		returnTransactions.values().stream().
			filter(x -> x.getStatus().equals("CLOSED") ).forEach(x->addBalanceOperation(x));
		double b=0;
		try {
			EZShop.db.openConnection();
			b = EZShop.db.loadBalance();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			EZShop.db.closeConnection();
		}
		setBalance(b);
	}
	public void resetT() {
		Transactions.clear();
		setBalance(0);
	}
}
