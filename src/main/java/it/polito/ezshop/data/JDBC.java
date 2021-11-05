package it.polito.ezshop.data;
import org.sqlite.SQLiteException;

import java.sql.Types;
import java.sql.Date;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.TreeMap;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.util.Optional.empty;

public class JDBC {

	private static String jdbcUrl;
	private Connection connection;
	//private static boolean flag = true; //false: Set connection on sql-server

	public JDBC() {
		JDBC.jdbcUrl = "jdbc:sqlite:EZShop.db";
	}
	
	public Connection openConnection() throws SQLException{
		this.connection = DriverManager.getConnection(jdbcUrl);
		return connection;
	}
	
	public boolean closeConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public HashMap<Integer, User> loadUsers() throws SQLException{
		HashMap<Integer, User> users = new HashMap<Integer, User>();

		String sql = "SELECT * FROM user";
		Statement stmt  = connection.createStatement();
		ResultSet rs    = stmt.executeQuery(sql);

		while (rs.next()) {
			int id = rs.getInt("id");
			String username = rs.getString("username");
			String password = rs.getString("password");
			String role = rs.getString("role");
			
			users.put(id, new UserImpl(id, username, password, role));
		}
		return users;
	}

	public void insertUser(int id, String username, String password, String role) throws SQLException{
		String sql = "insert into user values (?, ?, ?, ?)";
		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setInt(1, id);
		pstmt.setString(2, username);
		pstmt.setString(3, password);
		pstmt.setString(4, role);
		pstmt.executeUpdate();
	}

		public void deleteUser(int id) throws SQLException {
		String sql = "delete from user where id = ?";
		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setInt(1, id);
		pstmt.executeUpdate();
	}

	public void updateUserRole(int id, String role) throws SQLException {
		String sql = "update user set role = ? where id = ?";
		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setString(1, role);
		pstmt.setInt(2, id);
		pstmt.executeUpdate();
	}

	public HashMap<Integer, Customer> loadCustomers(HashMap<String, LoyaltyCard> loyaltyCards) throws SQLException {
		HashMap<Integer, Customer> customers = new HashMap<Integer, Customer>();

		String sql = "SELECT * FROM customer";
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);

		while (rs.next()) {
			int id = rs.getInt("id");
			String name = rs.getString("name");
			String loyaltyCard = rs.getString("loyaltycard");

			CustomerImpl c = new CustomerImpl(id, name);

			if (loyaltyCard != null) {
				c.attachCardToCustomer(loyaltyCards.get(loyaltyCard));
			}

			customers.put(id, c);

		}
		return customers;
	}

	public void insertCustomer(int id, String name) throws SQLException {
		String sql = "insert into customer (id, name) values (?, ?)";
		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setInt(1, id);
		pstmt.setString(2, name);
		pstmt.executeUpdate();
	}

	public void modifyCustomer(String newCustomerName, String newCustomerCard, Integer id) throws SQLException{		
		String sql;
		PreparedStatement pstmt;

		// card can be empty or valid
		if (!newCustomerCard.isEmpty()) {
			sql = "insert into loyaltycard values (?, 0, true)";
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, newCustomerCard);
			pstmt.executeUpdate();
		}
		sql = "update customer set name = ?, loyaltycard = ? where id = ?";
		pstmt = connection.prepareStatement(sql);
		pstmt.setString(1, newCustomerName);
		pstmt.setString(2, newCustomerCard);
		pstmt.setInt(3, id);
		pstmt.executeUpdate();
	}

	public void modifyCustomerName(String newCustomerName, Integer id)
			throws SQLException {

		String sql = "update customer set name = ? where id = ?";
		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setString(1, newCustomerName);
		pstmt.setInt(2, id);
		pstmt.executeUpdate();
	}

	public void deleteCustomer(int id) throws SQLException {
		String sql = "delete from customer where id = ?";
		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setInt(1, id);
		pstmt.executeUpdate();
	}

	public HashMap<String, LoyaltyCard> loadLoyaltyCards() throws SQLException {
		HashMap<String, LoyaltyCard> loyaltyCards = new HashMap<String, LoyaltyCard>();

		String sql = "SELECT * FROM loyaltycard";
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);

		while (rs.next()) {
			String id = rs.getString("id");
			Integer points = rs.getInt("points");
			boolean assigned = rs.getBoolean("assigned");

			loyaltyCards.put(id, new LoyaltyCard(id, points, assigned));
		}
		return loyaltyCards;
	}

	public void insertCustomerCard(String id, int points, boolean assigned) throws SQLException {
		String sql = "insert into loyaltycard (id, points, assigned ) values (?,?,?)";
		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setString(1, id);
		pstmt.setInt(2, points);
		pstmt.setBoolean(3, assigned);
		pstmt.executeUpdate();

	}

	public void attachCustomerCard(Integer customerId, String customerCard) throws SQLException {
		String sql = "update loyaltycard set assigned = true where id = ?";
		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setString(1, customerCard);
		pstmt.executeUpdate();
		sql = "update customer set loyaltycard = ? where id = ?";
		pstmt = connection.prepareStatement(sql);
		pstmt.setString(1, customerCard);
		pstmt.setInt(2, customerId);
		pstmt.executeUpdate();

	}

	public void detachCustomerCard(Integer customerId, String customerCard) throws SQLException {
		String sql = "update loyaltycard set assigned = false, points = 0 where id = ?";
		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setString(1, customerCard);
		pstmt.executeUpdate();
		sql = "update customer set loyaltycard = null where id = ?";
		pstmt = connection.prepareStatement(sql);
		pstmt.setInt(1, customerId);
		pstmt.executeUpdate();

	}
	
	public void deleteCustomerCard(String customerCard) throws SQLException {
		String sql = "delete from loyaltycard where id = ?";
		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setString(1, customerCard);
		pstmt.executeUpdate();
		pstmt.executeUpdate();
	}

	public void modifyPoints(String customerCard, Integer points) throws SQLException {
		String sql = "update loyaltycard set points = ? where id = ?";
		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setInt(1, points);
		pstmt.setString(2, customerCard);
		pstmt.executeUpdate();
	}

	public void createUserTable() throws SQLException {
		String sql="CREATE TABLE IF NOT EXISTS user (\n"
				+ "    id       INTEGER PRIMARY KEY,\n"
				+ "    username STRING  UNIQUE,\n"
				+ "    password STRING,\n"
				+ "    role     STRING\n"
				+ ");";
		Statement stmt  = connection.createStatement();
		stmt.executeUpdate(sql);
	}
	
	public void createCustomerTable() throws SQLException {
		String sql="CREATE TABLE IF NOT EXISTS customer (\n"
				+ "    id          INTEGER PRIMARY KEY,\n"
				+ "    name        STRING  UNIQUE,\n"
				+ "    loyaltycard STRING  REFERENCES loyaltycard (id) \n"
				+ ");";
		Statement stmt  = connection.createStatement();
		stmt.executeUpdate(sql);
	}
	
	public void createLoyaltyCardTable() throws SQLException {
		String sql="CREATE TABLE IF NOT EXISTS loyaltycard (\n"
				+ "    id       STRING  PRIMARY KEY,\n"
				+ "    points   INTEGER,\n"
				+ "    assigned BOOLEAN\n"
				+ ");";
		Statement stmt  = connection.createStatement();
		stmt.executeUpdate(sql);
	}

	
	//This method creates productTypeTable if it not exists
	public void createProductTypeTable() throws SQLException {
		String sql="CREATE TABLE IF NOT EXISTS `ProductType` (" +
				"  `id` INT UNIQUE NOT NULL," + 
				"  `position` varchar(30) NULL," + 
				"  `barcode` varchar(15) UNIQUE  NOT NULL," + 
				"  `description`  varchar(150) NOT NULL," + 
				"  `sellprice` REAL  NOT NULL default 0," + 
				"  `quantity`  INT NOT NULL default 0," + 
				"  `discountrate` REAL NOT NULL default 0," + 
				"  `note`  varchar(150)," +
				"   PRIMARY KEY  (`id`)" +  
				")";
		Statement stmt  = connection.createStatement();
		stmt.executeUpdate(sql);
	}
	
	//This method creates productTable if it not exists
		public void createProductTable() throws SQLException {
			String sql="CREATE TABLE IF NOT EXISTS `Product` (" +
					"  `rfid` varchar(14) UNIQUE NOT NULL," + 
					"  `barcode` varchar(15) NOT NULL REFERENCES ProductType (barcode)," + 
					"   PRIMARY KEY  (`rfid`)" +  
					")";
			Statement stmt  = connection.createStatement();
			stmt.executeUpdate(sql);
		}
		
	//This method creates orderTable if it not exists
	public void createOrderTable() throws SQLException {
		String sql="CREATE TABLE IF NOT EXISTS `Orders` (" +
				"  `id` INT UNIQUE NOT NULL," + 
				"  `supplier` varchar(30) NULL," + 
				"  `priceperunit` REAL  NOT NULL default 0," + 
				"  `quantity`  INT NOT NULL default 0," + 
				"  `status`  varchar(15) NOT NULL," +
				"  `productcode` varchar(15) NOT NULL," +
				"  `date`  varchar(30) NOT NULL," +
				"  `type`  varchar(15) NOT NULL," +
				"  `money` REAL  NOT NULL default 0," +
				"  `balanceid` INT UNIQUE NOT NULL," +
				"   PRIMARY KEY  (`id`)" +  
				")";
		Statement stmt  = connection.createStatement();
		stmt.executeUpdate(sql);
	}
	
	//This method creates positionTable if it not exists
	public void createPositionTable() throws SQLException {
		String sql="CREATE TABLE IF NOT EXISTS `Position` (" +
				"  `pos` varchar(30) UNIQUE NOT NULL," + 
				"   PRIMARY KEY  (`pos`)" +  
				")";
		Statement stmt  = connection.createStatement();
		stmt.executeUpdate(sql);
	}
	
	public HashMap <String,ProductType> loadInventory() throws SQLException{
		HashMap <String,ProductType> inventory = new HashMap <String,ProductType>();

		String sql = "SELECT * FROM ProductType";
		Statement stmt  = connection.createStatement();
		ResultSet rs    = stmt.executeQuery(sql);

		while (rs.next()) {
			int id = rs.getInt("id");
			String position = rs.getString("position");
			String barcode = rs.getString("barcode");
			String description = rs.getString("description");
			Double sellprice= rs.getDouble("sellprice");
			int quantity = rs.getInt("quantity");
			//No discount in interface of productType
			Double discountrate= rs.getDouble("discountrate");
			String note = rs.getString("note");
			
			ProductTypeImpl p = new ProductTypeImpl(description,barcode,sellprice,note);
			p.setQuantity(quantity);
			p.setId(id);
			p.setLocation(position);
			p.setDiscountRate(discountrate);
			
			inventory.put(barcode, p);
		}
		return inventory;
	}
	
	public HashMap <String,Product> loadProducts() throws SQLException{
		HashMap <String,Product> products = new HashMap <String,Product>();

		String sql = "SELECT * FROM Product";
		Statement stmt  = connection.createStatement();
		ResultSet rs    = stmt.executeQuery(sql);

		while (rs.next()) {
			String RFID = rs.getString("rfid");
			String barcode = rs.getString("barcode");
			
			Product p = new Product(RFID,barcode);
			
			products.put(RFID, p);
		}
		return products;
	}
	
	public ArrayList<String> loadPosition() throws SQLException{
		ArrayList<String> positions = new ArrayList<String> ();

		String sql = "SELECT * FROM Position";
		Statement stmt  = connection.createStatement();
		ResultSet rs    = stmt.executeQuery(sql);

		while (rs.next()) {
			String position = rs.getString("pos");
		
			positions.add(position);
		}
		return positions;
	}
	
	public HashMap <Integer, Order> loadOrders() throws SQLException{
		HashMap <Integer, Order> orders = new HashMap <Integer, Order>();

		String sql = "SELECT * FROM Orders";
		Statement stmt  = connection.createStatement();
		ResultSet rs    = stmt.executeQuery(sql);

		while (rs.next()) {
			int id = rs.getInt("id");
			//String supplier = rs.getString("supplier");
			Double priceperunit= rs.getDouble("priceperunit");
			int quantity = rs.getInt("quantity");
			String status = rs.getString("status");
			String productcode = rs.getString("productcode");
			String date = rs.getString("date");
			String type = rs.getString("type");
			Double money= rs.getDouble("money");
			int balanceid = rs.getInt("balanceid");
			
			
			OrderImpl o = new OrderImpl(productcode,quantity,priceperunit,status);
			o.setOrderId(id);
			//No setter for supplier since we don't manage it
			Debit d = o.getBalance();
			d.setDate(LocalDate.parse(date));
			d.setType(type);
			d.setMoney(money);
			d.setBalanceId(balanceid);
			o.setBalance(d);
			
			orders.put(id, o);
		}
		return orders;
	}
	
	public void insertProductType(int id, String position,String barcode, String description,Double sellprice, int quantity, Double discountrate,String note) throws SQLException{
		if(id<=0 || quantity<0 || discountrate<0 || discountrate > 100 || sellprice<=0)
			throw new SQLException("Error in params");
		String sql = "INSERT INTO ProductType(id,position,barcode,description,sellprice,quantity,discountrate,note)"+
				" values (?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setInt(1, id);
		pstmt.setString(2, position);
		pstmt.setString(3, barcode);
		pstmt.setString(4, description);
		pstmt.setDouble(5, sellprice);
		pstmt.setInt(6, quantity);
		pstmt.setDouble(7, discountrate);
		pstmt.setString(8, note);
		pstmt.executeUpdate();
	}
	
	public void insertProduct(String rfid,String barcode) throws SQLException{
		
		String sql = "INSERT INTO Product(rfid,barcode)"+
				" values (?, ?)";
		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setString(1, rfid);
		pstmt.setString(2, barcode);
		pstmt.executeUpdate();
	}
	
	public void insertOrder(int id, String supplier,Double priceperunit, int quantity, String status
			,String productcode,String date, String type, Double money, int balanceid) throws SQLException{
		if(id<=0||priceperunit<=0||quantity<=0)
			throw new SQLException("Error in params");
		String sql = "INSERT INTO Orders(id,supplier,priceperunit,quantity,status,productcode,date,type,money,		balanceid)"+
				" values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setInt(1, id);
		pstmt.setString(2, supplier);
		pstmt.setDouble(3, priceperunit);
		pstmt.setInt(4, quantity);
		pstmt.setString(5, status);
		pstmt.setString(6, productcode);
		pstmt.setString(7, date);
		pstmt.setString(8, type);
		pstmt.setDouble(9, money);
		pstmt.setInt(10, balanceid);
		pstmt.executeUpdate();
	}
	
	public void insertPosition(String pos) throws SQLException {
		String sql = "INSERT INTO Position(pos)"+
				" values (?)";
		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setString(1, pos);
		pstmt.executeUpdate();
	}
	
	public void updateProductType(int id, String newPosition,String newBarcode, String newDescription,Double newSellprice, int newQuantity, Double newDiscountrate,String newNote) throws SQLException{
		
		if(id<=0 || newQuantity<0 || newDiscountrate<0 || newDiscountrate > 100 || newSellprice<=0)
			throw new SQLException("Error in params");
		
		String sql = "UPDATE ProductType SET position=?,barcode=?,description=?,sellprice=?,quantity=?,discountrate=?,note=? WHERE id=?";
		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setString(1, newPosition);
		pstmt.setString(2, newBarcode);
		pstmt.setString(3, newDescription);
		pstmt.setDouble(4, newSellprice);
		pstmt.setInt(5, newQuantity);
		pstmt.setDouble(6, newDiscountrate);
		pstmt.setString(7, newNote);
		pstmt.setInt(8, id);
		pstmt.executeUpdate();
	}
	
	public void updateOrder(int id, String newSupplier,Double newPriceperunit, int newQuantity, String newStatus,String newProductcode) throws SQLException{
		if(id<=0||newPriceperunit<=0||newQuantity<=0)
			throw new SQLException("Error in params");
		
		String sql = "UPDATE Orders SET supplier=?,priceperunit=?,quantity=?,status=?,productcode=? WHERE id=?";
		PreparedStatement pstmt = connection.prepareStatement(sql);
		
		pstmt.setString(1, newSupplier);
		pstmt.setDouble(2, newPriceperunit);
		pstmt.setInt(3, newQuantity);
		pstmt.setString(4, newStatus);
		pstmt.setString(5, newProductcode);
		pstmt.setInt(6, id);
		pstmt.executeUpdate();
	}
	
	public void deleteProductType(int id) throws SQLException{
		String sql = "DELETE FROM ProductType WHERE id = ?";
		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setInt(1, id);
		pstmt.executeUpdate();
	}
	
	public void deleteProduct(String rfid) throws SQLException{
		String sql = "DELETE FROM Product WHERE rfid = ?";
		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setString(1, rfid);
		pstmt.executeUpdate();
	}
	
	public void deleteOrder(int id) throws SQLException{
		String sql = "DELETE FROM Orders WHERE id=?";
		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setInt(1, id);
		pstmt.executeUpdate();
	}
	
	public void deletePosition(String pos) throws SQLException{
		String sql = "DELETE FROM Position WHERE pos=?";
		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setString(1, pos);
		pstmt.executeUpdate();
	}


	//SALE AND RETURN DATABASE MANAGEMENT
	//Tested
	public void loadSaleTransactionDB(HashMap<Integer, SaleTransaction> sales, HashMap<String, ProductType> inv, HashMap<String, CreditCard> CCs) throws SQLException{
		String sql = "SELECT * FROM saleTransaction";
		String sql1 = "SELECT * FROM cart WHERE balanceId=";
		this.connection = openConnection();
		Statement stmt = connection.createStatement();
		ResultSet res = stmt.executeQuery(sql);
		while(res.next()){
			SaleTransactionImpl tmp = new SaleTransactionImpl(res.getDouble("discountRate"), res.getString("status"));
			tmp.setDate(res.getDate("date").toLocalDate());
			tmp.setMoney(res.getDouble("money"));
			tmp.setBalanceId(res.getInt("balanceId"));
			if(tmp.getStatus().equals("CLOSED")){
				tmp.setPayment(null);
			}
			else {
				if (res.getObject("CreditCardId") == null) {
					CashPayment p = new CashPayment(tmp.getMoney(), res.getDouble("change"));
					tmp.setPayment(p);
				} else {
					CreditCardPayment p = new CreditCardPayment(tmp.getMoney(), CCs.get(res.getString("CreditCardId")));
					tmp.setPayment(p);
				}
			}
			//load cart based on inventory
			String newSql = sql1+tmp.getBalanceId();
			Statement stmt1 = connection.createStatement();
			ResultSet res1 = stmt1.executeQuery(newSql);
			HashMap<String,CartItem> tmpCart = new HashMap<String, CartItem>();
			while(res1.next()) {
				CartItem tmpCartItem = new CartItem(res1.getInt("quantity"), inv.get(res1.getString("barCode")));
				tmpCartItem.setProductDiscount(res1.getDouble("productDiscount"));
				tmpCart.put(tmpCartItem.getProduct().getBarCode(),tmpCartItem);
			}
			tmp.setCart(tmpCart);
			sales.put(res.getInt("balanceId"),tmp);
		}
		closeConnection();
	}

	public void loadRFIDProdIntoSaleAndReturnTransactions(HashMap<Integer, SaleTransaction> sales, HashMap<Integer,ReturnTransaction> returns) throws SQLException {
		String sql = "SELECT * FROM cartRFID";
		this.connection = openConnection();
		Statement stmt = connection.createStatement();
		ResultSet res = stmt.executeQuery(sql);
		while(res.next()){
			if(sales.containsKey(res.getInt("balanceId"))){ //If the balanceId is a saleTransaction load into sales
				SaleTransactionImpl tmp = (SaleTransactionImpl) sales.get(res.getInt("balanceId"));
				tmp.getCart().get(res.getString("barCode")).addProdRFID(res.getString("rfid"));
			}
			else{ //else the balanceId is related to a return transaction
				returns.get(res.getInt("balanceId")).getReturnedCart().get(res.getString("barCode")).addProdRFID(res.getString("rfid"));
			}
		}
		closeConnection();
	}


	//Tested
	public void loadReturnTransaction(HashMap<Integer, ReturnTransaction> returns, HashMap<String, ProductType> inv, HashMap<Integer, SaleTransaction> sales, HashMap<String, CreditCard> CCs) throws SQLException {
		String sql = "SELECT * FROM returnTransaction";
		String sql1 = "SELECT * FROM cart WHERE balanceId=?";
		this.connection = openConnection();
		Statement stmt = connection.createStatement();
		ResultSet res = stmt.executeQuery(sql);
		while(res.next()){
			ReturnTransaction tmp = new ReturnTransaction(res.getString("status"), sales.get(res.getInt("saleTransactionID")));
			tmp.setDate(res.getDate("date").toLocalDate());
			tmp.setMoney(res.getDouble("money"));
			tmp.setBalanceId(res.getInt("balanceId"));
			if(tmp.getStatus().equals("CLOSED")){
				tmp.setPayment(null);
			}
			else {
				if (res.getObject("CreditCardId") == null) {
					CashPayment p = new CashPayment(tmp.getMoney(), res.getDouble("change"));
					tmp.setPayment(p);
				} else {
					CreditCardPayment p = new CreditCardPayment(tmp.getMoney(), CCs.get(res.getString("CreditCardId")));
					tmp.setPayment(p);
				}
			}
			//load cart based on inventory
			PreparedStatement stmt1 = connection.prepareStatement(sql1);
			stmt1.setInt(1, tmp.getBalanceId());
			ResultSet res1 = stmt1.executeQuery();
			HashMap<String,CartItem> tmpCart = new HashMap<String, CartItem>();
			while(res1.next()) {
				CartItem tmpCartItem = new CartItem(res1.getInt("quantity"), inv.get(res1.getString("barCode")));
				tmpCartItem.setProductDiscount(res1.getDouble("productDiscount"));
				tmpCart.put(tmpCartItem.getProduct().getBarCode(),tmpCartItem);
			}
			tmp.setReturnedCart(tmpCart);
			returns.put(res.getInt("balanceId"), tmp);
		}
		closeConnection();
	}
	//Tested, Change1 edit
	public void commitReturnTransactionDB(SaleTransaction s, HashMap<String,CartItem> returnedCart, Double newPrice) throws SQLException {
		SaleTransactionImpl tmp = (SaleTransactionImpl) s;
		//Re-edit the transaction price
		String sql = "UPDATE saleTransaction "+
				"SET balanceId=?,discountRate=?,status=?,date=?,money=?,creditCardId=?,fiedelityCardId=?,change=? "
				+ "WHERE balanceId=?";
		String sql1 = "DELETE FROM cart WHERE balanceId=? AND barCode=?";
		String sql2 = "UPDATE cart SET balanceId=?, barCode=?, quantity=?, productDiscount=? WHERE balanceId=? AND barCode=? ";
		String sql3 = "DELETE FROM cartRFID WHERE balanceId=? AND barCode=? AND rfid = ?";
		this.connection = openConnection();
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setInt(1,tmp.getBalanceId());
		preparedStatement.setInt(9,tmp.getBalanceId());
		preparedStatement.setDouble(2,tmp.getDiscountRate());
		preparedStatement.setString(3,tmp.getStatus());
		//TODO check notation of Date sql type is timestamp
		preparedStatement.setDate(4, Date.valueOf(tmp.getDate()));
		preparedStatement.setDouble(5,newPrice);
		//TODO: is fiedelityCard needed in the db?
		preparedStatement.setNull(7, Types.NULL);
		if (tmp.getPayment() ==  null) {
			preparedStatement.setNull(8, Types.NULL);
			preparedStatement.setNull(6, Types.NULL);
		}
		else {
			if (tmp.isCardPayment()){
				preparedStatement.setString(6, tmp.getPaymentCreditCard().getCC().getNumber());
				preparedStatement.setNull(8, Types.NULL);
			}
			else{
				preparedStatement.setNull(6, Types.NULL);
				preparedStatement.setDouble(8, tmp.getPaymentCash().getChange());
			}
		}
		preparedStatement.executeUpdate();
		for(CartItem item : returnedCart.values()){
			PreparedStatement pstmt;
			if ((int)tmp.getCart().get(item.getProduct().getBarCode()).getQuantity() == (int)item.getQuantity()){ //Are equals -> I need to delete the item in cart DB (TODO see warnings '==' or 'equals()')
				pstmt = connection.prepareStatement(sql1);
				pstmt.setInt(1,tmp.getBalanceId());
				pstmt.setString(2, item.getProduct().getBarCode());
			}
			else{//I have only to modify the product quantity in cart DB
				pstmt = connection.prepareStatement(sql2);
				pstmt.setInt(1, tmp.getBalanceId());
				pstmt.setString(2, item.getProduct().getBarCode());
				pstmt.setInt(5, tmp.getBalanceId());
				pstmt.setString(6, item.getProduct().getBarCode());
				pstmt.setInt(3, (tmp.getCart().get(item.getProduct().getBarCode()).getQuantity()) - item.getQuantity());
				pstmt.setDouble(4, item.getProductDiscount());
			}
			pstmt.executeUpdate();
			preparedStatement = connection.prepareStatement(sql3);
			preparedStatement.setInt(1, s.getTicketNumber());
			preparedStatement.setString(2, item.getBarCode());
			for(int i = 0; i < item.getProdRFIDs().size(); i++){
				preparedStatement.setString(3, item.getProdRFIDs().get(i));
				preparedStatement.executeUpdate();
				this.insertProduct(item.getProdRFIDs().get(i), item.getBarCode()); //Re-add returned RFIDs into product DB
			}
		}
		closeConnection();
	}

	//Tested, Change1 edit
	public void addSaleTransactionDB(SaleTransaction s) throws SQLException{
		SaleTransactionImpl tmp = (SaleTransactionImpl) s;
		String sql = "INSERT INTO saleTransaction(balanceId, discountRate, status, date, money, creditCardId, fiedelitycardId, change) VALUES(?,?,?,?,?,?,?,?)";
		String sql1 = "INSERT INTO cart(balanceId, barCode, quantity, productDiscount) VALUES(?,?,?,?)";
		String sql2 = "INSERT INTO cartRFID(balanceId, barCode, rfid) VALUES(?, ?, ?)";
		this.connection = openConnection();
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setInt(1,tmp.getBalanceId());
		preparedStatement.setDouble(2,tmp.getDiscountRate());
		preparedStatement.setString(3,tmp.getStatus());
		//TODO check notation of Date sql type is timestamp
		preparedStatement.setDate(4, Date.valueOf(tmp.getDate()));
		preparedStatement.setDouble(5,tmp.getMoney());
		preparedStatement.setNull(6,Types.NULL);
		preparedStatement.setNull(8,Types.NULL);
		//TODO: is fiedelityCard needed in the db?
		preparedStatement.setNull(7, Types.NULL);
		preparedStatement.executeUpdate();
		//Add the transaction cart items into DB
		for (CartItem item : tmp.getCart().values()) {
			preparedStatement = connection.prepareStatement(sql1);
			preparedStatement.setInt(1,tmp.getBalanceId());
			preparedStatement.setString(2, item.getProduct().getBarCode());
			preparedStatement.setInt(3, item.getQuantity());
			preparedStatement.setDouble(4, item.getProductDiscount());
			preparedStatement.executeUpdate();
			//store all the RFID for a specific productType
			for(int i = 0; i < item.getProdRFIDs().size(); i++){
				preparedStatement = connection.prepareStatement(sql2);
				preparedStatement.setInt(1, tmp.getBalanceId());
				preparedStatement.setString(2, item.getBarCode());
				preparedStatement.setString(3, item.getProdRFIDs().get(i));
				preparedStatement.executeUpdate();
				this.deleteProduct(item.getProdRFIDs().get(i)); //delete RFIDs also from DB when a saleTransaction is committed
			}
		}
		closeConnection();
	}
	//Tested, Change1 edit
	public void addReturnTransactionDB(ReturnTransaction r) throws SQLException{
		String sql = "INSERT INTO returnTransaction(balanceId, status, saleTransactionID, date, money, creditCardId, change) VALUES(?,?,?,?,?,?,?)";
		String sql1 = "INSERT INTO cart(balanceId, barCode, quantity, productDiscount) VALUES(?,?,?,?)";
		String sql2 = "INSERT INTO cartRFID(balanceId, barCode, rfid) VALUES(?, ?, ?)";
		this.connection = openConnection();
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setInt(1,r.getBalanceId());
		preparedStatement.setString(2,r.getStatus());
		//TODO check notation of Date sql type is timestamp
		preparedStatement.setInt(3, r.getSaleTransaction().getBalanceId());
		preparedStatement.setDate(4, Date.valueOf(r.getDate()));
		preparedStatement.setDouble(5,r.getMoney());
		if(!r.isPayed()){
			preparedStatement.setNull(6,Types.NULL);
			preparedStatement.setNull(7,Types.NULL);
		}
		else {
			if (r.isCardPayment()) { //CARD PAYMENT
				preparedStatement.setString(6, r.getPaymentCreditCard().getCC().getNumber());
				preparedStatement.setNull(7, Types.NULL);
			}
			else { //CASH PAYMENT
					preparedStatement.setNull(6, Types.NULL);
					preparedStatement.setDouble(7, r.getPaymentCash().getChange());
				}
		}
		preparedStatement.setNull(7, Types.NULL);
		preparedStatement.setNull(6, Types.NULL);

		preparedStatement.executeUpdate();

		//Add return Transaction cart item into DB
		for (CartItem item : r.getReturnedCart().values()) {
			preparedStatement = connection.prepareStatement(sql1);
			preparedStatement.setInt(1,r.getBalanceId());
			preparedStatement.setString(2, item.getProduct().getBarCode());
			preparedStatement.setInt(3, item.getQuantity());
			preparedStatement.setDouble(4, item.getProductDiscount());
			preparedStatement.executeUpdate();
			//store all the RFID for a specific productType
			for(int i = 0; i < item.getProdRFIDs().size(); i++){
				preparedStatement = connection.prepareStatement(sql2);
				preparedStatement.setInt(1, r.getBalanceId());
				preparedStatement.setString(2, item.getBarCode());
				preparedStatement.setString(3, item.getProdRFIDs().get(i));
				preparedStatement.executeUpdate();
			}
		}
		closeConnection();
	}

	//Tested, Change1 edit
	public void deleteSaleTransactionDB(SaleTransaction s) throws  SQLException{
		SaleTransactionImpl tmp = (SaleTransactionImpl) s;
		String sql = "DELETE FROM saleTransaction WHERE balanceId=?";
		String sql1 = "DELETE FROM cart WHERE balanceId=?";
		String sql2 = "DELETE FROM cartRFID WHERE balanceId=?";
		this.connection = openConnection();
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setInt(1,tmp.getBalanceId());
		preparedStatement.executeUpdate();
		preparedStatement = connection.prepareStatement(sql1);
		preparedStatement.setInt(1,tmp.getBalanceId());
		preparedStatement.executeUpdate();
		preparedStatement = connection.prepareStatement(sql2);
		preparedStatement.setInt(1,tmp.getBalanceId());
		preparedStatement.executeUpdate();
		closeConnection();
	}
	//Tested, Change1 edit
	public void deleteReturnTransactionDB(ReturnTransaction r, SaleTransaction s) throws SQLException{
		String sql = "DELETE FROM returnTransaction WHERE balanceId=?";
		String sql1 = "DELETE FROM cart WHERE balanceId=?";
		String sql2 = "UPDATE saleTransaction "+
				"SET balanceId=?,discountRate=?,status=?,date=?,money=?,creditCardId=?,fiedelityCardId=?,change=? "
				+ "WHERE balanceId=?";
		String sql3 = "INSERT INTO cart(balanceId, productId, quantity, productDiscount) VALUES(?,?,?,?)";
		String sql4 = "UPDATE cart SET balanceId=?, barCode=?, quantity=?, productDiscount=? WHERE balanceId=? AND barCode=?";
		String sql5 = "UPDATE cartRFID SET balanceId=?, barCode=?, rfid=? WHERE balanceId=? AND barCode=? AND rfid=?";
		SaleTransactionImpl tmp = (SaleTransactionImpl) s;
		//Update the sale Transaction
		this.connection = openConnection();
		PreparedStatement preparedStatement = connection.prepareStatement(sql2);
		preparedStatement.setInt(1,tmp.getBalanceId());
		preparedStatement.setInt(9,tmp.getBalanceId());
		preparedStatement.setDouble(2,tmp.getDiscountRate());
		preparedStatement.setString(3,tmp.getStatus());
		//TODO check notation of Date sql type is timestamp
		preparedStatement.setDate(4, Date.valueOf(tmp.getDate()));
		preparedStatement.setDouble(5, (tmp.getMoney() + (-1)*r.getMoney()));
		//TODO: is fiedelityCard needed in the db?
		preparedStatement.setNull(7, Types.NULL);
		if (tmp.getPayment() != null) {
			if (tmp.isCardPayment()){ //TODO: testing isCardPayment return could be null
				preparedStatement.setString(6, tmp.getPaymentCreditCard().getCC().getNumber());
				preparedStatement.setNull(8,Types.NULL);
			}
			else{
				preparedStatement.setNull(6, Types.NULL);
				preparedStatement.setDouble(8,tmp.getPaymentCash().getChange());
			}
		}
		else{
			preparedStatement.setNull(6, Types.NULL);
			preparedStatement.setNull(8, Types.NULL);
		}
		preparedStatement.executeUpdate();
		//DELETE all items related to the returnTransaction in cart
		preparedStatement = connection.prepareStatement(sql1);
		preparedStatement.setInt(1, r.getBalanceId());
		preparedStatement.executeUpdate();
		//Re-add the item to the sale transaction
		for(CartItem returned : r.getReturnedCart().values()){
			if (!tmp.getCart().containsKey(returned.getProduct().getBarCode())){ //Item not present i need to re-add it
				preparedStatement = connection.prepareStatement(sql3);
				preparedStatement.setInt(1, tmp.getBalanceId());
				preparedStatement.setString(2, returned.getProduct().getBarCode());
				preparedStatement.setInt(3, returned.getQuantity());
				preparedStatement.setDouble(4, returned.getProductDiscount());
			}
			else{ //Item already present -> just update quantity
				preparedStatement = connection.prepareStatement(sql4);
				preparedStatement.setInt(1, tmp.getBalanceId());
				preparedStatement.setInt(5, tmp.getBalanceId());
				preparedStatement.setString(2, returned.getProduct().getBarCode());
				preparedStatement.setString(6, returned.getProduct().getBarCode());
				preparedStatement.setInt(3, tmp.getCart().get(returned.getProduct().getBarCode()).getQuantity() + returned.getQuantity());
				preparedStatement.setDouble(4, returned.getProductDiscount());
			}
			//Execute statement
			preparedStatement.executeUpdate();
			//Update RFIDs
			preparedStatement = connection.prepareStatement(sql5);
			preparedStatement.setInt(1, s.getTicketNumber());
			preparedStatement.setInt(4, r.getBalanceId());
			preparedStatement.setString(2, returned.getBarCode());
			preparedStatement.setString(5, returned.getBarCode());
			for(int i = 0; i < returned.getProdRFIDs().size(); i++){
				preparedStatement.setString(3, returned.getProdRFIDs().get(i));
				preparedStatement.setString(6, returned.getProdRFIDs().get(i));
				preparedStatement.executeUpdate();
				this.deleteProduct(returned.getProdRFIDs().get(i));
			}
		}
		//Finally, delete returnTransaction into returnTransaction DB
		preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setInt(1, r.getBalanceId());
		preparedStatement.executeUpdate();
		closeConnection();
	}
	//Tested
	public void saleTransactionPaymentDB(SaleTransaction s, Payment p) throws SQLException {
		String sql = "UPDATE saleTransaction "+
				"SET balanceId=?,discountRate=?,status=?,date=?,money=?,creditCardId=?,fiedelityCardId=?,change=? "
				+ "WHERE balanceId=?";
		SaleTransactionImpl tmp = (SaleTransactionImpl) s;
		this.connection = openConnection();
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setInt(1,tmp.getBalanceId());
		preparedStatement.setInt(9,tmp.getBalanceId());
		preparedStatement.setDouble(2,tmp.getDiscountRate());
		preparedStatement.setString(3,"PAYED");
		//TODO check notation of Date sql type is timestamp
		preparedStatement.setDate(4, Date.valueOf(tmp.getDate()));
		preparedStatement.setDouble(5,tmp.getMoney());
		//TODO: is fiedelityCard needed in the db?
		preparedStatement.setNull(7, Types.NULL);
		if (p instanceof CreditCardPayment){
			CreditCardPayment tmpPayment = (CreditCardPayment) p;
			preparedStatement.setNull(8, Types.NULL);
			preparedStatement.setString(6, tmpPayment.getCC().getNumber());
		}
		else{
			CashPayment tmpPayment = (CashPayment) p;
			preparedStatement.setNull(6, Types.NULL);
			preparedStatement.setDouble(8,tmpPayment.getChange());
		}
		preparedStatement.executeUpdate();
		closeConnection();
	}
	//Tested
	public void returnTransactionPaymentDB(ReturnTransaction r, Payment p) throws SQLException{
		String sql = "UPDATE returnTransaction "+
				"SET balanceId=?,status=?,saleTransactionId=?,date=?,money=?,creditCardId=?,change=? "
				+ "WHERE balanceId=?";
		this.connection = openConnection();
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		preparedStatement.setInt(1,r.getBalanceId());
		preparedStatement.setInt(3,r.getSaleTransaction().getBalanceId());
		preparedStatement.setInt(8,r.getBalanceId());
		preparedStatement.setString(2,"PAYED");
		//TODO check notation of Date sql type is timestamp
		preparedStatement.setDate(4, Date.valueOf(r.getDate()));
		preparedStatement.setDouble(5,r.getMoney());
		if (p instanceof CreditCardPayment){
			CreditCardPayment tmpPayment = (CreditCardPayment) p;
			preparedStatement.setNull(7, Types.NULL);
			preparedStatement.setString(6, tmpPayment.getCC().getNumber());
		}
		else{
			CashPayment tmpPayment = (CashPayment) p;
			preparedStatement.setNull(6, Types.NULL);
			preparedStatement.setDouble(7,tmpPayment.getChange());
		}
		preparedStatement.executeUpdate();
		closeConnection();
	}

	//Tested
	public void initDB(HashMap<Integer, ReturnTransaction> returns, HashMap<String, ProductType> inv, HashMap<Integer, SaleTransaction> sales, HashMap<String, CreditCard> CCs) throws SQLException{
		String sqlSaleTransaction = "CREATE TABLE IF NOT EXISTS `saleTransaction` (\n" +
				"  `balanceId` bigint NOT NULL,\n" +
				"  `discountRate` double NOT NULL,\n" +
				"  `status` varchar(10) NOT NULL,\n" +
				"  `date` date NOT NULL,\n" +
				"  `money` double NOT NULL,\n" +
				"  `creditCardId` varchar(16) DEFAULT NULL,\n" +
				"  `fiedelityCardId` int DEFAULT NULL,\n" +
				"  `change` double DEFAULT NULL,\n" +
				"  PRIMARY KEY (`balanceId`)\n" +
				")";

		String sqlReturnTransaction = "CREATE TABLE IF NOT EXISTS`returnTransaction` (\n" +
				"  `balanceId` bigint NOT NULL,\n" +
				"  `status` varchar(10) NOT NULL,\n" +
				"  `saleTransactionID` bigint NOT NULL,\n" +
				"  `date` date NOT NULL,\n" +
				"  `money` double NOT NULL,\n" +
				"  `creditCardId` varchar(16) DEFAULT NULL,\n" +
				"  `change` double DEFAULT NULL,\n" +
				"  PRIMARY KEY (`balanceId`)\n" +
				")";

		String sqlCart = "CREATE TABLE IF NOT EXISTS `cart` (\n" +
				"  `balanceId` bigint NOT NULL,\n" +
				"  `barCode` varchar(15) NOT NULL,\n" +
				"  `quantity` int NOT NULL,\n" +
				"  `productDiscount` double NOT NULL," +
				"  FOREIGN KEY (balanceId) REFERENCES saleTransaction(balanceId)\n" +
				"  FOREIGN KEY (barCode) REFERENCES ProductType(barcode)\n" +
				")";

		String sqlcartRFID = "CREATE TABLE IF NOT EXISTS `cartRFID` (\n" +
				"  `balanceId` bigint NOT NULL,\n" +
				"  `barCode` varchar(15) NOT NULL,\n" +
				"  `rfid` varchar(14) NOT NULL,\n" +
				"  FOREIGN KEY (balanceId) REFERENCES saleTransaction(balanceId)\n" +
				"  FOREIGN KEY (rfid) REFERENCES Product(rfid)\n" +
				")";
		//CREATE TABLES
		this.connection = openConnection();
		Statement stmt  = connection.createStatement();
		stmt.executeUpdate(sqlSaleTransaction);
		stmt.executeUpdate(sqlReturnTransaction);
		stmt.executeUpdate(sqlCart);
		stmt.executeUpdate(sqlcartRFID);
		closeConnection();
		if (sales == null || inv == null || returns == null || CCs == null)
			return;
		//TODO: IF COLLECTIONS ARE NOT LOCALLY CREATED RETURN (USED FOR TEST PURPOSE THEN ASSUME COLLECTION MUST BE ALWAYS CREATED BEFORE INIT THEM FROM DB)
		loadSaleTransactionDB(sales,inv,CCs);
		loadReturnTransaction(returns,inv,sales,CCs);
		loadRFIDProdIntoSaleAndReturnTransactions(sales, returns);
	}

	void resetDB() throws SQLException{
		String sql="DELETE FROM ";
		String[] tables= {"Orders","Position","ProductType","returnTransaction","saleTransaction","cart","Balance","loyaltycard", "customer", "user","Product", "cartRFID"};
		for(String t : tables) {
			String sqlE = sql + t;
			Statement stmt = connection.createStatement();
			stmt.execute(sqlE);
		}
	}
	//Tested
	public void deleteEntireDB() throws SQLException{
		String[] tables= {"Orders","Position","ProductType","returnTransaction","saleTransaction","cart", "loyaltycard", "customer", "user","Balance","Product", "cartRFID"};
		this.connection=openConnection();
		for(String t : tables) {
			String sql = "DROP TABLE IF EXISTS " + t;
			Statement stmt = connection.createStatement();
			stmt.execute(sql);
		}
		closeConnection();
	}
	public void createBalanceTable() throws SQLException{
		String sql="CREATE TABLE IF NOT EXISTS Balance (balanceTot REAL NOT NULL default 0)";
		Statement stmt  = connection.createStatement();
		stmt.executeUpdate(sql);
	}
	public void insertBalance() throws SQLException{
		String sql="SELECT COUNT(*) FROM Balance";
		Statement stmt = connection.createStatement();
		ResultSet res = stmt.executeQuery(sql);
		res.next();
		if(res.getInt("COUNT(*)")!=0) 
			return;
		String sqlA="INSERT INTO Balance(balanceTot) VALUES(0.00)";
		Statement stmt2 = connection.createStatement();
		stmt2.execute(sqlA);
						
	}
	public void updateBalance(double balance)throws SQLException{
		String sql = "UPDATE Balance SET balanceTot=?";
		PreparedStatement pstmt = connection.prepareStatement(sql);
		pstmt.setDouble(1, balance);
		pstmt.executeUpdate();
	}
	public double loadBalance() throws SQLException{
		String sql="SELECT * FROM Balance";
		Statement stmt  = connection.createStatement();
		ResultSet rs    = stmt.executeQuery(sql);
		rs.next();
		return rs.getDouble(1);
	}
}
