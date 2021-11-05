package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.*;

import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class EZShop implements EZShopInterface {

    static private HashMap<Integer, SaleTransaction> saleTransactions;
    static private HashMap<Integer, ReturnTransaction> returnTransactions;
    static private String fileCC = "creditcards.txt";
    static private HashMap<String, CreditCard> creditcards;
    static private HashMap<Integer, User> users;
    static private HashMap<Integer, Customer> customers;
    static private HashMap<String, LoyaltyCard> loyaltyCards;

    public static JDBC db;
    static private AccountBook a;
    private UserImpl loggedUser;


    static HashMap<String, ProductType> inventory;
    static HashMap<String, Product> products;
    static ArrayList<String> positions;
    static HashMap<Integer, Order> orders;


    public EZShop() {
        loggedUser = null;
        a = new AccountBook();
        inventory = new HashMap<String, ProductType>();
        products = new HashMap<String, Product>();
        positions = new ArrayList<String>();
        orders = new HashMap<Integer, Order>();
        creditcards = new HashMap<String, CreditCard>();
        CreditCard.LoadCC(fileCC, creditcards);
        users = new HashMap<Integer, User>();
        customers = new HashMap<Integer, Customer>();
        loyaltyCards = new HashMap<String, LoyaltyCard>();
        saleTransactions = new HashMap<Integer, SaleTransaction>();
        returnTransactions = new HashMap<Integer, ReturnTransaction>();
        db = new JDBC();
        //Create tables if they don't exist
        try {
            db.openConnection();
            db.createProductTypeTable();
            db.createProductTable();
            db.createOrderTable();
            db.createPositionTable();
            db.createUserTable();
            db.createLoyaltyCardTable();
            db.createCustomerTable();
            db.createBalanceTable();
            db.insertBalance();
            inventory = db.loadInventory();
            products = db.loadProducts();
            positions = db.loadPosition();
            orders = db.loadOrders();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }


        // load all data structures

        try {
            db.openConnection();
            users = db.loadUsers();
            loyaltyCards = db.loadLoyaltyCards();
            customers = db.loadCustomers(loyaltyCards);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }

        //SALE and RETURN TRANSACTION DB INIT AND MANAGEMENT
        //ADDING TABLES IF NOT EXIST AND POPULATE THEM
        try {
            db.initDB(returnTransactions, inventory, saleTransactions, creditcards);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //Update the IDGenerator in balanceOperation
        //TODO: pay attention, these three collection must be initialized before thi function call
        BalanceOperationImpl.setCounterFromDb(saleTransactions.keySet().stream().mapToInt(k -> k).max().orElse(0),
                returnTransactions.keySet().stream().mapToInt(k -> k).max().orElse(0), orders.keySet().stream().mapToInt(k -> k).max().orElse(0));
        //LOAD THE ACCOUNT BOOK
        a.LoadTransactions(orders, saleTransactions, returnTransactions);
    }

    /**
     * RESET: revert system data: see 'EZShopInterface.java' for the complete API descriptions.
     **/
    @Override
    public void reset() {
        a.resetT();
        saleTransactions.clear();
        returnTransactions.clear();
        orders.clear();
        inventory.clear();
        products.clear();
        positions.clear();
        users.clear();
        customers.clear();
        loyaltyCards.clear();
        loggedUser=null;
        try {
			CreditCard.resetCCFile(fileCC);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
        try {
            db.openConnection();
            db.resetDB();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }

    }

    /********************************************************************************************************************************************
     *                                                                                                                                          *
     *                         USERS AND RIGHTS MANAGEMENT: see 'EZShopInterface.java' for the complete API descriptions.                       *
     *                                                                                                                                          *
     ********************************************************************************************************************************************/

    @Override
    public Integer createUser(String username, String password, String role)
            throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
        if (username == null || username.isEmpty())
            throw new InvalidUsernameException();

        if (password == null || password.isEmpty())
            throw new InvalidPasswordException();

        if (role == null || role.isEmpty()
                || (!role.equals("Administrator") && !role.equals("ShopManager") && !role.equals("Cashier")))
            throw new InvalidRoleException();

        // check uniqueness of username
        for (Entry<Integer, User> e : users.entrySet()) {
            if (e.getValue().getUsername().equals(username)) {
                return -1;
            }
        }

        int id = 0;
        if (!users.isEmpty())
            id = Collections.max(users.keySet());
        id++;
        User newUser = new UserImpl(id, username, password, role);
        try {
            db.openConnection();
            db.insertUser(id, username, password, role);
        } catch (Exception e) {
            return -1;
        } finally {
            db.closeConnection();
        }
        users.put(id, newUser);


        return id;
    }

    @Override
    public boolean deleteUser(Integer id) throws InvalidUserIdException, UnauthorizedException {
        if (this.loggedUser == null || !this.loggedUser.isAdministator())
            throw new UnauthorizedException();
        if (id == null || id <= 0)
            throw new InvalidUserIdException();

        if (!users.containsKey(id))
            return false;

        try {
            db.openConnection();
            db.deleteUser(id);
        } catch (SQLException e) {
            return false;
        } finally {
            db.closeConnection();
        }
        users.remove(id);
        return true;

    }


    @Override
    public List<User> getAllUsers() throws UnauthorizedException {
        if (this.loggedUser == null || !this.loggedUser.isAdministator())
            throw new UnauthorizedException();

        return (List<User>) users.values().stream().collect(toList());
    }

    @Override
    public User getUser(Integer id) throws InvalidUserIdException, UnauthorizedException {

        if (loggedUser == null || !loggedUser.isAdministator())
            throw new UnauthorizedException();

        if (id == null || id <= 0)
            throw new InvalidUserIdException();

        if (!users.containsKey(id))
            return null;

        return users.get(id);
    }

    @Override
    public boolean updateUserRights(Integer id, String role)
            throws InvalidUserIdException, InvalidRoleException, UnauthorizedException {

        if (this.loggedUser == null || !this.loggedUser.isAdministator())
            throw new UnauthorizedException();

        if (id == null || id <= 0)
            throw new InvalidUserIdException();

        if (role == null || role.isEmpty() || (!role.equals("Administrator") && !role.equals("ShopManager") && !role.equals("Cashier")))
			throw new InvalidRoleException();

        if (!users.containsKey(id))
            return false;

        try {
            db.openConnection();
            db.updateUserRole(id, role);
        } catch (SQLException e) {
            return false;
        } finally {
            db.closeConnection();
        }

        ((UserImpl) users.get(id)).updateRole(role);

        return true;
    }

    public User getLoggedUser() {
		return this.loggedUser;
	}


    @Override
    public User login(String username, String password) throws InvalidUsernameException, InvalidPasswordException {

        if (username == null || username.isEmpty())
            throw new InvalidUsernameException();

        if (password == null || password.isEmpty())
            throw new InvalidPasswordException();

        Integer id = 0;
        for (Entry<Integer, User> e : users.entrySet()) {
            if (e.getValue().getUsername().equals(username) && e.getValue().getPassword().equals(password)) {
                id = e.getKey();
                break;
            }
        }

        if (id != 0) {
            this.loggedUser = (UserImpl) users.get(id);
            return users.get(id);
        }

        return null;
    }

    @Override
    public boolean logout() {
        if (this.loggedUser == null)
            return false;

        this.loggedUser = null;
        return true;
    }

    /********************************************************************************************************************************************
     *                                                                                                                                          *
     *               PRODUCTS AND POSITIONS MANAGEMENT: see 'EZShopInterface.java' for the complete API descriptions.                           *
     *                                                                                                                                          *
     ********************************************************************************************************************************************/

    @Override
    public Integer createProductType(String description, String productCode, double pricePerUnit, String note) throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {

        if (loggedUser == null || loggedUser.isCashier())
            throw new UnauthorizedException();

        //Double priviledges check
        if (!loggedUser.isAdministator() && !loggedUser.isShopManager())
            throw new UnauthorizedException();

        if (!ProductTypeImpl.validateDescr(description))
            throw new InvalidProductDescriptionException();

        if (!ProductTypeImpl.validateProdCode(productCode))
            throw new InvalidProductCodeException();

        if (pricePerUnit <= 0)
            throw new InvalidPricePerUnitException();

        if (inventory.containsKey(productCode))
            return -1;

        ProductType p = new ProductTypeImpl(description, productCode, pricePerUnit, note);

        try {
            db.openConnection();
            db.insertProductType(p.getId(), p.getLocation(), p.getBarCode(), p.getProductDescription(), p.getPricePerUnit(), p.getQuantity(), new Double(0), p.getNote());
        } catch (Exception e) {
            return -1;
        } finally {
            db.closeConnection();
        }


        inventory.put(productCode, p);

        return p.getId();
    }

    @Override
    public boolean updateProduct(Integer id, String newDescription, String newCode, double newPrice, String newNote) throws InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
        if (loggedUser == null || loggedUser.isCashier())
            throw new UnauthorizedException();

        //Double priviledges check
        if (!loggedUser.isAdministator() && !loggedUser.isShopManager())
            throw new UnauthorizedException();

        if (id == null || id <= 0)
            throw new InvalidProductIdException();

        if (!ProductTypeImpl.validateDescr(newDescription))
            throw new InvalidProductDescriptionException();

        if (!ProductTypeImpl.validateProdCode(newCode))
            throw new InvalidProductCodeException();

        if (newPrice <= 0)
            throw new InvalidPricePerUnitException();

        if (inventory.containsKey(newCode) && !inventory.get(newCode).getId().equals(id))
            return false;

        Optional<ProductType> prod = inventory.values().stream()
                .filter(p -> p.getId().equals(id)).findFirst();

        if (!prod.isPresent())
            return false;

        ProductType p = prod.get();

        try {
            db.openConnection();
            db.updateProductType(id, p.getLocation(), newCode, newDescription, newPrice, p.getQuantity(), new Double(0), newNote);
        } catch (Exception e) {
            return false;
        } finally {
            db.closeConnection();
        }

        //Prendo il prodotto con il vecchio barcode
        p = inventory.remove(p.getBarCode());

        p.setProductDescription(newDescription);
        p.setBarCode(newCode);
        p.setPricePerUnit(newPrice);
        p.setNote(newNote);

        inventory.put(newCode, p);

        return true;
    }

    @Override
    public boolean deleteProductType(Integer id) throws InvalidProductIdException, UnauthorizedException {
        if (loggedUser == null || loggedUser.isCashier())
            throw new UnauthorizedException();

        //Double priviledges check
        if (!loggedUser.isAdministator() && !loggedUser.isShopManager())
            throw new UnauthorizedException();

        if (id == null || id <= 0)
            throw new InvalidProductIdException();

        Optional<ProductType> prod = inventory.values().stream()
                .filter(p -> p.getId().equals(id)).findFirst();

        if (!prod.isPresent())
            return false;


        ProductType p = prod.get();

        try {
            db.openConnection();
            db.deleteProductType(id);
        } catch (Exception e) {
            return false;
        } finally {
            db.closeConnection();
        }

        //Rimuovo dall'inventario
        return inventory.remove(p.getBarCode(), p);
    }

    @Override
    public List<ProductType> getAllProductTypes() throws UnauthorizedException {
        if (loggedUser == null)
            throw new UnauthorizedException();

        //Double priviledges check
        if (!loggedUser.isAdministator() && !loggedUser.isShopManager() && !loggedUser.isCashier())
            throw new UnauthorizedException();

        return inventory.values().stream().collect(Collectors.toList());
    }

    @Override
    public ProductType getProductTypeByBarCode(String barCode) throws InvalidProductCodeException, UnauthorizedException {
        if (loggedUser == null || loggedUser.isCashier())
            throw new UnauthorizedException();

        //Double priviledges check
        if (!loggedUser.isAdministator() && !loggedUser.isShopManager())
            throw new UnauthorizedException();

        if (!ProductTypeImpl.validateProdCode(barCode))
            throw new InvalidProductCodeException();

        if (!inventory.containsKey(barCode))
            return null;

        return inventory.get(barCode);
    }

    @Override
    public List<ProductType> getProductTypesByDescription(String description) throws UnauthorizedException {
        if (loggedUser == null || loggedUser.isCashier())
            throw new UnauthorizedException();

        //Double priviledges check
        if (!loggedUser.isAdministator() && !loggedUser.isShopManager())
            throw new UnauthorizedException();

        //Null should be considered as the empty string.
        if (description == null)
            description = "";

        String descr = description;

        return inventory.values().stream()
                .filter(p -> p.getProductDescription().contains(descr))
                .collect(Collectors.toList());
    }

    @Override
    public boolean updateQuantity(Integer productId, int toBeAdded) throws InvalidProductIdException, UnauthorizedException {
        if (loggedUser == null || loggedUser.isCashier())
            throw new UnauthorizedException();

        //Double priviledges check
        if (!loggedUser.isAdministator() && !loggedUser.isShopManager())
            throw new UnauthorizedException();

        if (productId == null || productId <= 0)
            throw new InvalidProductIdException();

        Optional<ProductType> prod = inventory.values().stream()
                .filter(p -> p.getId().equals(productId)).findFirst();

        if (!prod.isPresent())
            return false;

        ProductType p = prod.get();

        if (p.getLocation() == null)
            return false;

        int qty = p.getQuantity() + toBeAdded;

        if (qty < 0)
            return false;

        try {
            db.openConnection();
            db.updateProductType(p.getId(), p.getLocation(), p.getBarCode(), p.getProductDescription(), p.getPricePerUnit(), qty, new Double(0), p.getNote());
        } catch (Exception e) {
            return false;
        } finally {
            db.closeConnection();
        }

        p.setQuantity(qty);

        inventory.replace(p.getBarCode(), p);

        return true;
    }

    @Override
    public boolean updatePosition(Integer productId, String newPos) throws InvalidProductIdException, InvalidLocationException, UnauthorizedException {
        if (loggedUser == null || loggedUser.isCashier())
            throw new UnauthorizedException();

        //Double priviledges check
        if (!loggedUser.isAdministator() && !loggedUser.isShopManager())
            throw new UnauthorizedException();

        if (productId == null || productId <= 0)
            throw new InvalidProductIdException();

        if (newPos != null && !newPos.equals("") && !Position.validatePosition(newPos))
            throw new InvalidLocationException();

        Optional<ProductType> prod = inventory.values().stream()
                .filter(p -> p.getId().equals(productId)).findFirst();

        if (!prod.isPresent())
            return false;

        ProductType p = prod.get();


        //If <newPos> is null or empty it should reset
        //the position of given product type.
        if (newPos == null || newPos.equals("")) {
            //If p has a position, I have to remove it from the array
            if (p.getLocation() != null) {
                try {
                    db.openConnection();
                    db.deletePosition(p.getLocation());
                } catch (Exception e) {
                    return false;
                } finally {
                    db.closeConnection();
                }
                positions.remove(p.getLocation());
            }

            try {
                db.openConnection();
                db.updateProductType(p.getId(), newPos, p.getBarCode(), p.getProductDescription(), p.getPricePerUnit(), p.getQuantity(), new Double(0), p.getNote());
            } catch (Exception e) {
                return false;
            } finally {
                db.closeConnection();
            }

            p.setLocation(newPos);
            inventory.replace(p.getBarCode(), p);
            return true;
        }

        if (positions.contains(newPos)) 
            return false;
        

        try {
            db.openConnection();
            db.insertPosition(newPos);
            db.updateProductType(p.getId(), newPos, p.getBarCode(), p.getProductDescription(), p.getPricePerUnit(), p.getQuantity(), new Double(0), p.getNote());
        } catch (Exception e) {
            return false;
        } finally {
            db.closeConnection();
        }

        p.setLocation(newPos);
        //Update vector
        positions.add(newPos);
        inventory.replace(p.getBarCode(), p);

        return true;
    }


    /********************************************************************************************************************************************
     *                                                                                                                                          *
     *                              ORDERS MANAGEMENT: see 'EZShopInterface.java' for the complete API descriptions.                            *
     *                                                                                                                                          *
     ********************************************************************************************************************************************/

    @Override
    public Integer issueOrder(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
        if (loggedUser == null || loggedUser.isCashier())
            throw new UnauthorizedException();

        //Double priviledges check
        if (!loggedUser.isAdministator() && !loggedUser.isShopManager())
            throw new UnauthorizedException();

        if (!ProductTypeImpl.validateProdCode(productCode))
            throw new InvalidProductCodeException();

        if (quantity <= 0)
            throw new InvalidQuantityException();

        if (pricePerUnit <= 0)
            throw new InvalidPricePerUnitException();

        if (!inventory.containsKey(productCode))
            return -1;

        OrderImpl o = new OrderImpl(productCode, quantity, pricePerUnit, "ISSUED");
        try {
            db.openConnection();
            db.insertOrder(o.getOrderId(), null, o.getPricePerUnit(), o.getQuantity(), o.getStatus(), o.getProductCode(), o.getBalance().getDate().toString(), o.getBalance().getType(), o.getBalance().getMoney(), o.getBalanceId());
        } catch (Exception e) {
            return -1;
        } finally {
            db.closeConnection();
        }
        orders.put(o.getOrderId(), o);

        return o.getOrderId();
    }

    @Override
    public Integer payOrderFor(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
        if (loggedUser == null || loggedUser.isCashier())
            throw new UnauthorizedException();

        //Double priviledges check
        if (!loggedUser.isAdministator() && !loggedUser.isShopManager())
            throw new UnauthorizedException();

        if (!ProductTypeImpl.validateProdCode(productCode))
            throw new InvalidProductCodeException();

        if (quantity <= 0)
            throw new InvalidQuantityException();

        if (pricePerUnit <= 0)
            throw new InvalidPricePerUnitException();

        if (!inventory.containsKey(productCode))
            return -1;

        double balance = computeBalance();
        double toBePayed = pricePerUnit * (double) quantity;

        //Balance not enough
        if (balance < toBePayed)
            return -1;


        OrderImpl o = new OrderImpl(productCode, quantity, pricePerUnit, "PAYED");

        try {
            db.openConnection();
            db.insertOrder(o.getOrderId(), null, o.getPricePerUnit(), o.getQuantity(), o.getStatus(), o.getProductCode(), o.getBalance().getDate().toString(), o.getBalance().getType(), o.getBalance().getMoney(), o.getBalanceId());
        } catch (Exception e) {
            return -1;
        } finally {
            db.closeConnection();
        }

        orders.put(o.getOrderId(), o);

        //Update balance
        //recordBalanceUpdate ((-1)*toBePayed);
        OrderImpl ord = (OrderImpl) o;
        a.addBalanceOperation(ord.getBalance());

        return o.getOrderId();
    }

    @Override
    public boolean payOrder(Integer orderId) throws InvalidOrderIdException, UnauthorizedException {
        if (loggedUser == null || loggedUser.isCashier())
            throw new UnauthorizedException();

        //Double priviledges check
        if (!loggedUser.isAdministator() && !loggedUser.isShopManager())
            throw new UnauthorizedException();

        if (orderId == null || orderId <= 0)
            throw new InvalidOrderIdException();

        if (!orders.containsKey(orderId))
            return false;

        Order o = orders.get(orderId);


        //false if the order does not exist or if it was not in an ISSUED/ORDERED state
        if (!o.getStatus().equals("ISSUED") && !o.getStatus().equals("ORDERED"))
            return false;

        //payed (in this case the method has no effect)
        if (o.getStatus().equals("PAYED"))
            return true;

        double balance = computeBalance();
        double toBePayed = o.getPricePerUnit() * (double) o.getQuantity();

        //Balance not enough
        if (balance < toBePayed)
            return false;

        try {
            db.openConnection();
            db.updateOrder(o.getOrderId(), null, o.getPricePerUnit(), o.getQuantity(), "PAYED", o.getProductCode());
        } catch (Exception e) {
            return false;
        } finally {
            db.closeConnection();
        }

        o.setStatus("PAYED");

        orders.replace(o.getOrderId(), o);

        //Update balance
        //recordBalanceUpdate ((-1)*toBePayed);
        OrderImpl ord = (OrderImpl) o;
        a.addBalanceOperation(ord.getBalance());

        return true;
    }

    @Override
    public boolean recordOrderArrival(Integer orderId) throws InvalidOrderIdException, UnauthorizedException, InvalidLocationException {
        if (loggedUser == null || loggedUser.isCashier())
            throw new UnauthorizedException();

        //Double priviledges check
        if (!loggedUser.isAdministator() && !loggedUser.isShopManager())
            throw new UnauthorizedException();

        if (orderId == null || orderId <= 0)
            throw new InvalidOrderIdException();

        if (!orders.containsKey(orderId))
            return false;

        Order o = orders.get(orderId);

        ProductType p = inventory.get(o.getProductCode());
        if (p.getLocation() == null)
            throw new InvalidLocationException();

        //false if the order does not exist or if it was not in an ORDERED/COMPLETED state
        //Probably is PAYED, not ORDERED
        if (!o.getStatus().equals("PAYED") && !o.getStatus().equals("COMPLETED"))
            return false;

        //COMPLETED one (in this case this method will have no effect at all).
        if (o.getStatus().equals("COMPLETED"))
            return true;

        //Update order status && quantity

        try {
            updateQuantity(p.getId(), o.getQuantity());
        } catch (InvalidProductIdException e) {
            return false;
        }

        try {
            db.openConnection();
            db.updateOrder(o.getOrderId(), null, o.getPricePerUnit(), o.getQuantity(), "COMPLETED", o.getProductCode());
        } catch (Exception e) {
            return false;
        } finally {
            db.closeConnection();
        }

        o.setStatus("COMPLETED");
        orders.replace(o.getOrderId(), o);


        return true;
    }

    @Override
    public boolean recordOrderArrivalRFID(Integer orderId, String RFIDfrom) throws InvalidOrderIdException, UnauthorizedException, 
InvalidLocationException, InvalidRFIDException {
        
    	if (loggedUser == null || loggedUser.isCashier())
            throw new UnauthorizedException();

        //Double priviledges check
        if (!loggedUser.isAdministator() && !loggedUser.isShopManager())
            throw new UnauthorizedException();

        if (orderId == null || orderId <= 0)
            throw new InvalidOrderIdException();
        
        if (!Product.validateRFID(RFIDfrom))
            throw new InvalidRFIDException();
        
        if (products.containsKey(RFIDfrom)) 
        	throw new InvalidRFIDException();
    	
        if (!orders.containsKey(orderId))
            return false;

        Order o = orders.get(orderId);

        ProductType p = inventory.get(o.getProductCode());
        if (p.getLocation() == null)
            throw new InvalidLocationException();

        //false if the order does not exist or if it was not in an ORDERED/COMPLETED state
        //Probably is PAYED, not ORDERED
        if (!o.getStatus().equals("PAYED") && !o.getStatus().equals("COMPLETED"))
            return false;

        //COMPLETED one (in this case this method will have no effect at all).
        if (o.getStatus().equals("COMPLETED"))
            return true;
        
        //New part - change 2. Generate Products and insert them in the DB
        //For every RFID generated, check if it's unique. If not, throw
        //InvalidRFIDException
        
        //Convert RFIDfrom to long
        long rfidNum=Long.parseLong(RFIDfrom);
        
        //First of all check if invalid RFID
        for (int i=0; i<o.getQuantity(); i++) {
        	//String.format
        		//0 means 0 padding
        		//12 - set width to 12
        	String newRFID = String.format("%012d", rfidNum);
        	
        	if (products.containsKey(newRFID)) 
            	throw new InvalidRFIDException();
        	
        	rfidNum++;
        	
        }
        
        //If all RFIDs are unique
        rfidNum=Long.parseLong(RFIDfrom);
        
        for (int i=0; i<o.getQuantity(); i++) {
        	
        	String newRFID = String.format("%012d", rfidNum);
        	Product RFIDprod = new Product (newRFID,o.getProductCode());
        	
        	try {
                db.openConnection();
                db.insertProduct(newRFID, o.getProductCode());
            } catch (Exception e) {
                return false;
            } finally {
                db.closeConnection();
            }
        	
        	products.put(newRFID, RFIDprod);
        	rfidNum++;
        }

        //Update order status && quantity

        try {
            updateQuantity(p.getId(), o.getQuantity());
        } catch (InvalidProductIdException e) {
            return false;
        }

        try {
            db.openConnection();
            db.updateOrder(o.getOrderId(), null, o.getPricePerUnit(), o.getQuantity(), "COMPLETED", o.getProductCode());
        } catch (Exception e) {
            return false;
        } finally {
            db.closeConnection();
        }

        o.setStatus("COMPLETED");
        orders.replace(o.getOrderId(), o);


        return true;
    }
    
    
    @Override
    public List<Order> getAllOrders() throws UnauthorizedException {
        if (loggedUser == null || loggedUser.isCashier())
            throw new UnauthorizedException();

        //Double priviledges check
        if (!loggedUser.isAdministator() && !loggedUser.isShopManager())
            throw new UnauthorizedException();

        return orders.values().stream().collect(Collectors.toList());
    }


    /********************************************************************************************************************************************
     *                                                                                                                                          *
     *              CUSTOMERS AND FIDELITY CARDS MANAGEMENT: see 'EZShopInterface.java' for the complete API descriptions.                       *
     *                                                                                                                                          *
     ********************************************************************************************************************************************/

    @Override
    public Integer defineCustomer(String customerName) throws InvalidCustomerNameException, UnauthorizedException {
        if (this.loggedUser == null || (!this.loggedUser.isAdministator() && !this.loggedUser.isShopManager()
                && !this.loggedUser.isCashier()))
            throw new UnauthorizedException();

        if (customerName == null || customerName.isEmpty())
            throw new InvalidCustomerNameException();

        // check for customerName uniqueness
        for (Entry<Integer, Customer> e : customers.entrySet()) {
            if (e.getValue().getCustomerName().equals(customerName))
                return -1;
        }

        int id = 0;
        if (!customers.isEmpty())
            id = Collections.max(customers.keySet());
        id++;
        Customer newCustomer = new CustomerImpl(id, customerName);

        try {
            db.openConnection();
            db.insertCustomer(id, customerName);
        } catch (Exception e) {
            return -1;
        } finally {
            db.closeConnection();
        }

        customers.put(id, newCustomer);
        return id;

    }


    @Override
    public boolean modifyCustomer(Integer id, String newCustomerName, String newCustomerCard)
            throws InvalidCustomerNameException, InvalidCustomerCardException, InvalidCustomerIdException,
            UnauthorizedException {

        if (this.loggedUser == null || (!this.loggedUser.isAdministator() && !this.loggedUser.isShopManager()
                && !this.loggedUser.isCashier()))
            throw new UnauthorizedException();

        if (id == null || id <= 0)
            throw new InvalidCustomerIdException();

        if (newCustomerName == null || newCustomerName.isEmpty())
            throw new InvalidCustomerNameException();

        if (newCustomerCard != null && !newCustomerCard.matches("^[0-9]{10}$") && !newCustomerCard.isEmpty())
            throw new InvalidCustomerCardException();

        if (!customers.containsKey(id))
            return false;

        // 1. card null or equals of before -> modify only name;
        // 2. card empty -> detach customerCard if already assigned;
        // 3. card valid -> if customer has already a card -> detach
        // -> card already present in loyaltycards -> attach
        // -> card not present in loyaltycards -> createCard(card) -> attach

        if (newCustomerCard == null || (customers.get(id).getCustomerCard() != null
                && customers.get(id).getCustomerCard().equals(newCustomerCard))) {
            try {
                db.openConnection();
                db.modifyCustomerName(newCustomerName, id);
                ((CustomerImpl) customers.get(id)).modifyCustomer(newCustomerName, null);
            } catch (Exception e) {
                return false;
            } finally {
                db.closeConnection();
            }
//            ((CustomerImpl) customers.get(id)).modifyCustomer(newCustomerName, null);

        } else if (newCustomerCard.isEmpty()) {
            if (customers.get(id).getCustomerCard() != null && !customers.get(id).getCustomerCard().isEmpty()) {
                try {
                    db.openConnection();
                    db.detachCustomerCard(id, customers.get(id).getCustomerCard());
                    loyaltyCards.get(customers.get(id).getCustomerCard()).detach();
                    ((CustomerImpl) customers.get(id)).detachCardFromCustomer();
                    db.modifyCustomerName(newCustomerName, id);
                    ((CustomerImpl) customers.get(id)).modifyCustomer(newCustomerName, null);

                } catch (Exception e) {
                    return false;
                } finally {
                    db.closeConnection();
                }


            }
            
            try {
                db.openConnection();
                db.modifyCustomerName(newCustomerName, id);
                ((CustomerImpl) customers.get(id)).modifyCustomer(newCustomerName, "");

            } catch (Exception e) {
                return false;
            } finally {
                db.closeConnection();
            }

        } else {
            // Customer card is valid
            // 1. detach old card if present and not the same value
            if (customers.get(id).getCustomerCard() != null && !customers.get(id).getCustomerCard().isEmpty()) {
                try {
                    db.openConnection();
                    db.detachCustomerCard(id, customers.get(id).getCustomerCard());
                    loyaltyCards.get(customers.get(id).getCustomerCard()).detach();
                    ((CustomerImpl) customers.get(id)).detachCardFromCustomer();
                    ((CustomerImpl) customers.get(id)).modifyCustomer(newCustomerName, null);
                    db.modifyCustomerName(newCustomerName, id);
                } catch (Exception e) {
                    return false;
                } finally {
                    db.closeConnection();
                }

            }

            // 2. create new card if not already present in loyaltycards and db
            if (!loyaltyCards.containsKey(newCustomerCard))
                createCard(newCustomerCard);

            // 3. attach

            try {
                db.openConnection();
                db.attachCustomerCard(id, newCustomerCard);
                loyaltyCards.get(newCustomerCard).setAssigned(true);
                ((CustomerImpl) customers.get(id)).attachCardToCustomer(loyaltyCards.get(newCustomerCard));
                ((CustomerImpl) customers.get(id)).modifyCustomer(newCustomerName, null);
                db.modifyCustomerName(newCustomerName, id);

            } catch (Exception e) {
                return false;
            } finally {
                db.closeConnection();
            }
        }

        return true;
    }



    @Override
    public boolean deleteCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {

        if (this.loggedUser == null || (!this.loggedUser.isAdministator() && !this.loggedUser.isShopManager()
                && !this.loggedUser.isCashier()))
            throw new UnauthorizedException();

        if (id == null || id <= 0)
            throw new InvalidCustomerIdException();

        if (!customers.containsKey(id))
            return false;

        try {
            db.openConnection();
            db.deleteCustomer(id);
            if (customers.get(id).getCustomerCard() != null) {
                db.deleteCustomerCard(customers.get(id).getCustomerCard());
                loyaltyCards.remove(customers.get(id).getCustomerCard());
            }
            customers.remove(id);
        } catch (Exception e) {
            return false;
        } finally {
            db.closeConnection();
        }

        return true;
    }


    @Override
    public Customer getCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {

        if (this.loggedUser == null || (!this.loggedUser.isAdministator() && !this.loggedUser.isShopManager()
                && !this.loggedUser.isCashier()))
            throw new UnauthorizedException();

        if (id == null || id <= 0)
            throw new InvalidCustomerIdException();

        if (!customers.containsKey(id))
            return null;

        return customers.get(id);
    }

    @Override
    public List<Customer> getAllCustomers() throws UnauthorizedException {

        if (this.loggedUser == null || (!this.loggedUser.isAdministator() && !this.loggedUser.isShopManager()
                && !this.loggedUser.isCashier()))
            throw new UnauthorizedException();

        return (List<Customer>) customers.values().stream().collect(toList());
    }

    @Override
    public String createCard() throws UnauthorizedException {
        if (this.loggedUser == null || (!this.loggedUser.isAdministator() && !this.loggedUser.isShopManager()
                && !this.loggedUser.isCashier()))
            throw new UnauthorizedException();

        LoyaltyCard lc = null;

        // chech among loyaltycards if one is not assigned
        if (!loyaltyCards.isEmpty()) {
            for (LoyaltyCard l : (List<LoyaltyCard>) loyaltyCards.values().stream().collect(toList())) {
                if (!l.isAssigned()) {
                    lc = l;
                    break;
                }
            }
        }

        if (lc == null) {

            String newCard = "";
            boolean check = false;
            while (!check) {

                for (int i = 0; i < 10; i++) {
                    int randomNum = (int) (Math.random() * 10); // 0 to 9
                    if (i == 0 && randomNum == 0)
                        i--;
                    else
                        newCard += randomNum;
                }

                // check if cardNumber already present
                if (loyaltyCards.containsKey(newCard))
                    newCard = "";
                else
                    check = true;
            }
            lc = new LoyaltyCard(newCard, 0, false);

            try {
                db.openConnection();
                db.insertCustomerCard(lc.getId(), lc.getPoints(), lc.isAssigned());
                loyaltyCards.put(newCard, lc);
            } catch (Exception e) {
                return "";
            } finally {
                db.closeConnection();
            }
        }
        return lc.getId();
    }

    public String createCard(String customerCard) throws UnauthorizedException, InvalidCustomerCardException {
        if (this.loggedUser == null || (!this.loggedUser.isAdministator() && !this.loggedUser.isShopManager()
                && !this.loggedUser.isCashier()))
            throw new UnauthorizedException();

        if (customerCard == null || !customerCard.matches("^[0-9]{10}$"))
            throw new InvalidCustomerCardException();

        LoyaltyCard lc = new LoyaltyCard(customerCard, 0, false);
        try {
            db.openConnection();
            db.insertCustomerCard(lc.getId(), lc.getPoints(), lc.isAssigned());
        } catch (Exception e) {
            return "";
        } finally {
            db.closeConnection();
        }
        loyaltyCards.put(customerCard, lc);

        return customerCard;
    }

    public void deleteAllCustomers() {
		customers.clear();
	}

    public void deleteAllUsers() {
		users.clear();
	}

    public void deleteAllLoyaltyCards() {
		loyaltyCards.clear();
	}


    @Override
    public boolean attachCardToCustomer(String customerCard, Integer customerId)
            throws InvalidCustomerIdException, InvalidCustomerCardException, UnauthorizedException {
        // if customer had another card before, it must be already detached and set not
        // assigned, this method just set customerCard assigned (given that it is
        // already present in db and loyaltycards) and customerCard to customer
        if (this.loggedUser == null || (!this.loggedUser.isAdministator() && !this.loggedUser.isShopManager()
                && !this.loggedUser.isCashier()))
            throw new UnauthorizedException();

        if (customerId == null || customerId <= 0)
            throw new InvalidCustomerIdException();

        if (customerCard == null || customerCard.isEmpty() || !customerCard.matches("^[0-9]{10}$"))
            throw new InvalidCustomerCardException();

        if (!customers.containsKey(customerId))
            return false;

        if (loyaltyCards.get(customerCard) != null && loyaltyCards.get(customerCard).isAssigned())
            return false;

        try {
            db.openConnection();
            db.attachCustomerCard(customerId, customerCard);
            loyaltyCards.get(customerCard).setAssigned(true);
            ((CustomerImpl) customers.get(customerId)).attachCardToCustomer(loyaltyCards.get(customerCard));
        } catch (Exception e) {
            return false;
        } finally {
            db.closeConnection();
        }

        return true;
    }


    @Override
    public boolean modifyPointsOnCard(String customerCard, int pointsToBeAdded)
            throws InvalidCustomerCardException, UnauthorizedException {

        if (this.loggedUser == null || (!this.loggedUser.isAdministator() && !this.loggedUser.isShopManager()
                && !this.loggedUser.isCashier()))
            throw new UnauthorizedException();

        if (customerCard == null || customerCard.isEmpty() || !customerCard.matches("^[0-9]{10}$"))
            throw new InvalidCustomerCardException();

        if (!loyaltyCards.containsKey(customerCard) || !loyaltyCards.get(customerCard).isAssigned())
            return false;

        Integer id = 0;
        int newPoints;

        for (Customer c : getAllCustomers()) {
            if (c.getCustomerCard().equals(customerCard)) {
                id = c.getId();
                break;
            }
        }

        if (id == 0)
            return false;

        newPoints = loyaltyCards.get(customers.get(id).getCustomerCard()).modifyPointsOnCard(pointsToBeAdded);

        if (newPoints < 0)
            return false;

        try {
            db.openConnection();
            db.modifyPoints(customerCard, newPoints);
            customers.get(id).setPoints(newPoints);
        } catch (Exception e) {
            return false;
        } finally {
            db.closeConnection();
        }

        return true;

    }

    /********************************************************************************************************************************************
     *                                                                                                                                          *
     *          SALE TRANSACTIONS AND RETURN TRANSACTIONS MANAGEMENT: see 'EZShopInterface.java' for the complete API descriptions.             *
     *                                                                                                                                          *
     ********************************************************************************************************************************************/

    @Override //OK (inconsistent witch check related to balanceId==0) !!!!!
    public Integer startSaleTransaction() throws UnauthorizedException {
        if (loggedUser == null || (!this.loggedUser.isAdministator() && !this.loggedUser.isShopManager()
                && !this.loggedUser.isCashier())) { //It can be invoked only after a user with role "Administrator", "ShopManager" or "Cashier" is logged in.
            throw new UnauthorizedException(); //@throws UnauthorizedException()
        }
        //Create new saleTransaction
        SaleTransactionImpl saleTr = new SaleTransactionImpl(0.0, "OPEN");
        saleTransactions.put(saleTr.getBalanceId(), saleTr); //Add saleTransaction to Collection
        return saleTr.getBalanceId(); // @return the id of the transaction (greater than or equal to 0)
    }

    @Override // OK
    public boolean addProductToSale(Integer transactionId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
        if (loggedUser == null || (!this.loggedUser.isAdministator() && !this.loggedUser.isShopManager()
                && !this.loggedUser.isCashier())) { // @throws UnauthorizedException if there is no logged user or if it has not the rights to perform the operation (TODO: ASSUME ONLY THESE RIGHTS, avoiding unuseful checks)
            throw new UnauthorizedException();
        }
        if (transactionId == null || transactionId <= 0) { // @throws InvalidTransactionIdException if the transaction id less than or equal to 0 or if it is null (TODO: CHECK CONSISTENCY WITH startSaleTransaction())
            throw new InvalidTransactionIdException();
        }
        if (productCode == null || productCode.equals("") || !ProductTypeImpl.validateProdCode(productCode)) {  //@throws InvalidProductCodeException if the product code is empty, null or invalid
            throw new InvalidProductCodeException();
        }
        if (amount < 0) { // @throws InvalidQuantityException if the quantity is less than 0 (TODO: check consistency add product with amount zero(?))
            throw new InvalidQuantityException();
        }
        if (!saleTransactions.containsKey(transactionId)) { //@return false if the transaction id does not identify a started and open transaction.
            return false;
        }
        //Get specific saleTransaction from Collection
        SaleTransactionImpl saleTr = (SaleTransactionImpl) saleTransactions.get(transactionId);
        if (saleTr.getStatus().equals("CLOSED") || saleTr.getStatus().equals("PAYED")) { //@return false  if the transaction id does not identify a started and open transaction.
            return false;
        }
        if (!inventory.containsKey(productCode)) { //@return false if the product code does not exist,
            return false;
        }
        if (inventory.get(productCode).getQuantity() < amount) { //@return false if the quantity of product cannot satisfy the request,
            return false;
        }
        //Add quantity to the same items if already present
        if (saleTr.getCart().containsKey(productCode)) {
            saleTr.getCart().get(productCode).addQuantity(amount);
        }
        //Else add new item with described quantity
        else {
            CartItem cartItem = new CartItem(amount, inventory.get(productCode));
            saleTr.getCart().put(productCode, cartItem);
        }
        //Updating inventory ( This method adds a product to a sale transaction decreasing the temporary amount of product available on the shelves for other customers.)
        ProductTypeImpl prod = (ProductTypeImpl) inventory.get(productCode);
        prod.setQuantity(prod.getQuantity() - amount); //Decreasing quantity
        saleTr.computeTransactionTotal(); //UPDATE TOTAL
        return true; // @return  true if the operation is successful
    }

    @Override
    public boolean addProductToSaleRFID(Integer transactionId, String RFID) throws InvalidTransactionIdException, InvalidRFIDException, InvalidQuantityException, UnauthorizedException {
        if (loggedUser == null || (!this.loggedUser.isAdministator() && !this.loggedUser.isShopManager()
                && !this.loggedUser.isCashier())) { // @throws UnauthorizedException if there is no logged user or if it has not the rights to perform the operation (TODO: ASSUME ONLY THESE RIGHTS, avoiding unuseful checks)
            throw new UnauthorizedException();
        }
        if (transactionId == null || transactionId <= 0) { // @throws InvalidTransactionIdException if the transaction id less than or equal to 0 or if it is null (TODO: CHECK CONSISTENCY WITH startSaleTransaction())
            throw new InvalidTransactionIdException();
        }
        if(RFID == null || RFID.equals("") || !Product.validateRFID(RFID)){ //  @throws InvalidRFIDException if the RFID code is empty, null or invalid
            throw new InvalidRFIDException();
        }
        if(!products.containsKey(RFID)){ //return false if the RFID does not exist,
            return false;
        }
        if (!saleTransactions.containsKey(transactionId)) { //@return false if the transaction id does not identify a started and open transaction.
            return false;
        }
        SaleTransactionImpl saleTr = (SaleTransactionImpl) saleTransactions.get(transactionId);
        if (saleTr.getStatus().equals("CLOSED") || saleTr.getStatus().equals("PAYED")) { //return false  if the transaction id does not identify a started and open transaction.
            return false;
        }
        ProductTypeImpl tmpProd = (ProductTypeImpl) inventory.get(products.get(RFID).getBarCode()); //Get the current productType
        //UPDATE INVENTORY AND PRODUCTS
        tmpProd.setQuantity(tmpProd.getQuantity() - 1); //Decrease the quantity by 1 item
        products.remove(RFID);  //Remove the RFID instance from the products collection (?)
        if(saleTr.getCart().containsKey(tmpProd.getBarCode())){ //If productType is already present in the cart, simply add the RFID ad update the quantity by 1 items
            saleTr.getCart().get(tmpProd.getBarCode()).addQuantity(1); //Add quantity
            saleTr.getCart().get(tmpProd.getBarCode()).addProdRFID(RFID);
        }
        else{ //Create new instance in the cartItem
            CartItem cartItem = new CartItem(1, inventory.get(tmpProd.getBarCode())); //Create new instance (with current amount = to 1)
            saleTr.getCart().put(tmpProd.getBarCode(), cartItem); //put new product type related to the RFID productType
            cartItem.addProdRFID(RFID); //Add RFID info into the list related to that specific product
        }
        saleTr.computeTransactionTotal(); //UPDATE TOTAL
        return true;
    }

    @Override //OK
    public boolean deleteProductFromSale(Integer transactionId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
        if (loggedUser == null || (!this.loggedUser.isAdministator() && !this.loggedUser.isShopManager()
                && !this.loggedUser.isCashier())) { // @throws UnauthorizedException if there is no logged user or if it has not the rights to perform the operation (TODO: ASSUME ONLY THESE RIGHTS, avoiding unuseful checks)
            throw new UnauthorizedException();
        }
        if (transactionId == null || transactionId <= 0) { // @throws InvalidTransactionIdException if the transaction id less than or equal to 0 or if it is null (TODO: CHECK CONSISTENCY WITH startSaleTransaction())
            throw new InvalidTransactionIdException();
        }
        if (productCode == null || productCode.equals("") || !ProductTypeImpl.validateProdCode(productCode)) { //@throws InvalidProductCodeException if the product code is empty, null or invalid
            throw new InvalidProductCodeException();
        }
        if (amount <= 0) { // @throws InvalidQuantityException if the quantity is less than 0 (TODO: check consistency delete product with amount zero(?))
            throw new InvalidQuantityException();
        }
        if (!saleTransactions.containsKey(transactionId)) { //@return false if the product code does not exist
            return false;
        }
        //Get specific saleTransaction from Collection
        SaleTransactionImpl saleTr = (SaleTransactionImpl) saleTransactions.get(transactionId);
        if (saleTr.getStatus().equals("CLOSED") || saleTr.getStatus().equals("PAYED")) { //@return false  if the transaction id does not identify a started and open transaction.
            return false;
        }
        //The transaction does not contain the inserted product
        if (!saleTr.getCart().containsKey(productCode)) {
            return false;
        }
        if (!inventory.containsKey(productCode)) { //@return false if the product code does not exist,
            return false;
        }
        if (saleTr.getCart().get(productCode).getQuantity() < amount) { //@return false if the quantity of product cannot satisfy the request,
            return false;
        }
        //If quantity is equal to one remove item reference from the cartList
        if (saleTr.getCart().get(productCode).getQuantity() == 1) {
            saleTr.getCart().remove(productCode);
        }
        //Else decrease the related quantity
        else {
            saleTr.getCart().get(productCode).decreaseQuantity(amount);
        }
        //Updating the inventory (This method deletes a product from a sale transaction increasing the temporary amount of product available on the shelves for other customers)
        inventory.get(productCode).setQuantity(inventory.get(productCode).getQuantity() + amount);
        //UPDATE TOTAL
        saleTr.computeTransactionTotal();
        return true; //@return true if the operation is successful
    }

    @Override
    public boolean deleteProductFromSaleRFID(Integer transactionId, String RFID) throws InvalidTransactionIdException, InvalidRFIDException, InvalidQuantityException, UnauthorizedException {
        if (loggedUser == null || (!this.loggedUser.isAdministator() && !this.loggedUser.isShopManager()
                && !this.loggedUser.isCashier())) { // @throws UnauthorizedException if there is no logged user or if it has not the rights to perform the operation (TODO: ASSUME ONLY THESE RIGHTS, avoiding unuseful checks)
            throw new UnauthorizedException();
        }
        if (transactionId == null || transactionId <= 0) { // @throws InvalidTransactionIdException if the transaction id less than or equal to 0 or if it is null (TODO: CHECK CONSISTENCY WITH startSaleTransaction())
            throw new InvalidTransactionIdException();
        }
        if(RFID == null || RFID.equals("") || !Product.validateRFID(RFID)){ //  @throws InvalidRFIDException if the RFID code is empty, null or invalid
            throw new InvalidRFIDException();
        }
        if (!saleTransactions.containsKey(transactionId)) { //@return false if the product code does not exist
            return false;
        }
        //Get specific saleTransaction from Collection
        SaleTransactionImpl saleTr = (SaleTransactionImpl) saleTransactions.get(transactionId);
        if (saleTr.getStatus().equals("CLOSED") || saleTr.getStatus().equals("PAYED")) { //@return false  if the transaction id does not identify a started and open transaction.
            return false;
        }
        ProductTypeImpl tmpProd = null;
        boolean presenceFlag = false;
        for(CartItem item : saleTr.getCart().values()){
            if(item.getProdRFIDs().contains(RFID)){
                presenceFlag = true;
                tmpProd = (ProductTypeImpl) item.getProduct(); // if true keep trace of ProductImpl related to that specific RFID
            }
        }
        if(!presenceFlag){ //if not present or the transactionId is wrong or product does not exist
            return false;
        }
        //ProductTypeImpl tmpProd = (ProductTypeImpl) inventory.get(products.get(RFID).getBarCode()); //Get the current productType
        //if(!saleTr.getCart().get(tmpProd.getBarCode()).getProdRFIDs().contains(RFID)){ //Cart Does not contains the specific RFID for a product
        //    return false;
        //}
        //If quantity is equal to one remove item reference from the cartList
        if (saleTr.getCart().get(tmpProd.getBarCode()).getQuantity() == 1) {
            saleTr.getCart().remove(tmpProd.getBarCode());
        }
        //Else decrease the related quantity (always 1)
        else {
            saleTr.getCart().get(tmpProd.getBarCode()).decreaseQuantity(1);
            saleTr.getCart().get(tmpProd.getBarCode()).getProdRFIDs().remove(RFID);
        }
        //Update quantity in the inventory
        products.put(RFID, new Product(RFID,tmpProd.getBarCode()));
        inventory.get(tmpProd.getBarCode()).setQuantity(inventory.get(tmpProd.getBarCode()).getQuantity() + 1);
        saleTr.computeTransactionTotal(); //UPDATE TOTAL
    	return true;
    }

    @Override //OK
    public boolean applyDiscountRateToProduct(Integer transactionId, String productCode, double discountRate) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidDiscountRateException, UnauthorizedException {
        if (loggedUser == null || (!this.loggedUser.isAdministator() && !this.loggedUser.isShopManager()
                && !this.loggedUser.isCashier())) { // @throws UnauthorizedException if there is no logged user or if it has not the rights to perform the operation (TODO: ASSUME ONLY THESE RIGHTS, avoiding unuseful checks)
            throw new UnauthorizedException();
        }
        if (transactionId == null || transactionId <= 0) { // @throws InvalidTransactionIdException if the transaction id less than or equal to 0 or if it is null (TODO: CHECK CONSISTENCY WITH startSaleTransaction())
            throw new InvalidTransactionIdException();
        }
        if (productCode == null || productCode.equals("") || !ProductTypeImpl.validateProdCode(productCode)) { //@throws InvalidProductCodeException if the product code is empty, null or invalid
            throw new InvalidProductCodeException();
        }
        if (discountRate < 0.00 || discountRate >= 1.00) { //@throws InvalidDiscountRateException if the discount rate is less than 0 or if it greater than or equal to 1.00
            throw new InvalidDiscountRateException();
        }
        if (!saleTransactions.containsKey(transactionId)) { ////@return false if the transaction does not exists
            return false;
        }
        //Get specific saleTransaction from Collection
        SaleTransactionImpl saleTr = (SaleTransactionImpl) saleTransactions.get(transactionId);
        if (saleTr.getStatus().equals("PAYED") || saleTr.getStatus().equals("CLOSED")) { //The sale transaction can be either started or closed but not already payed.
            return false;
        }
        if (!inventory.containsKey(productCode)) { //@return false if the product code does not exist,
            return false;
        }
        //The transaction does not contain the inserted product
        if (!saleTr.getCart().containsKey(productCode)) {
            return false;
        }
        saleTr.applyProdDiscount(discountRate, productCode); //Apply discount rate to transaction (implicit call for updating total)
        return true; //@return true if the operation is successful
    }

    @Override //OK
    public boolean applyDiscountRateToSale(Integer transactionId, double discountRate) throws InvalidTransactionIdException, InvalidDiscountRateException, UnauthorizedException {
        if (loggedUser == null || (!this.loggedUser.isAdministator() && !this.loggedUser.isShopManager()
                && !this.loggedUser.isCashier())) { // @throws UnauthorizedException if there is no logged user or if it has not the rights to perform the operation (TODO: ASSUME ONLY THESE RIGHTS, avoiding unuseful checks)
            throw new UnauthorizedException();
        }
        if (transactionId == null || transactionId <= 0) { // @throws InvalidTransactionIdException if the transaction id less than or equal to 0 or if it is null (TODO: CHECK CONSISTENCY WITH startSaleTransaction())
            throw new InvalidTransactionIdException();
        }
        if (discountRate < 0.00 || discountRate >= 1.00) { //@throws InvalidDiscountRateException if the discount rate is less than 0 or if it greater than or equal to 1.00
            throw new InvalidDiscountRateException();
        }
        if (!saleTransactions.containsKey(transactionId)) { //@return false if the transaction does not exists
            return false;
        }
        //Get specific saleTransaction from Collection
        SaleTransactionImpl saleTr = (SaleTransactionImpl) saleTransactions.get(transactionId);
        if (saleTr.getStatus().equals("CLOSED") || saleTr.getStatus().equals("PAYED")) { //The sale transaction can be either started or closed but not already payed.
            return false;
        }
        //Apply discount rate to transaction (implicit call for updating total)
        saleTr.setDiscountRate(discountRate);
        return true; //@return true if the operation is successful
    }

    @Override //OK
    public int computePointsForSale(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
        if (loggedUser == null || (!this.loggedUser.isAdministator() && !this.loggedUser.isShopManager()
                && !this.loggedUser.isCashier())) { // @throws UnauthorizedException if there is no logged user or if it has not the rights to perform the operation (TODO: ASSUME ONLY THESE RIGHTS, avoiding unuseful checks)
            throw new UnauthorizedException();
        }
        if (transactionId == null || transactionId <= 0) { // @throws InvalidTransactionIdException if the transaction id less than or equal to 0 or if it is null (TODO: CHECK CONSISTENCY WITH startSaleTransaction())
            throw new InvalidTransactionIdException();
        }
        if (!saleTransactions.containsKey(transactionId)) { //@return -1 if the transaction does not exists
            return -1;
        }
        //Get saleTransaction from collection
        SaleTransactionImpl saleTr = (SaleTransactionImpl) saleTransactions.get(transactionId);
        int tmp = (int) saleTr.getMoney();
        return tmp / 10; //@return the points of the sale (1 point for each 10€)
    }

    @Override //DB interfacing is required (OK)
    public boolean endSaleTransaction(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
        if (loggedUser == null || (!this.loggedUser.isAdministator() && !this.loggedUser.isShopManager()
                && !this.loggedUser.isCashier())) { // @throws UnauthorizedException if there is no logged user or if it has not the rights to perform the operation (TODO: ASSUME ONLY THESE RIGHTS, avoiding unuseful checks)
            throw new UnauthorizedException();
        }
        if (transactionId == null || transactionId <= 0) { // @throws InvalidTransactionIdException if the transaction id less than or equal to 0 or if it is null (TODO: CHECK CONSISTENCY WITH startSaleTransaction())
            throw new InvalidTransactionIdException();
        }
        if (!saleTransactions.containsKey(transactionId)) { //@return false if the transaction does not exists
            return false;
        }
        //Get saleTransaction from collection
        SaleTransactionImpl saleTr = (SaleTransactionImpl) saleTransactions.get(transactionId);
        if (saleTr.getStatus().equals("CLOSED") || saleTr.getStatus().equals("PAYED")) {  //The sale transaction can be either started or closed but not already payed.
            return false;
        }
        //UPDATING THE DB AVAILABLE QUANTITY OF EACH ITEM IN THE CART (After this operation the transaction is persisted in the system's memory.)
        for (CartItem obj : saleTr.getCart().values()) {
            //Get product
            ProductTypeImpl tmpProd = (ProductTypeImpl) obj.getProduct();
            //Update DB
            try {
                db.openConnection();
                db.updateProductType(tmpProd.getId(), tmpProd.getLocation(), tmpProd.getBarCode(), tmpProd.getProductDescription(), tmpProd.getPricePerUnit(),
                        tmpProd.getQuantity() , tmpProd.getDiscountRate(), tmpProd.getNote());
                db.closeConnection();
            } catch (SQLException e) {
                e.printStackTrace();
                return false; //@return false if there was a problem in registering the data
            }
        }
        //This method closes an opened transaction (in the collection)
        saleTr.setStatus("CLOSED");
        //UPDATE DB: this method will also update the status -> TODO: If this operation is done only in a local way different status are unuseful (?)
        try {
            db.addSaleTransactionDB(saleTr);
        } catch (SQLException e) {
            e.printStackTrace();
            return false; //@return false if there was a problem in registering the data
        }
        return true;  //@return true if the transaction was successfully closed
    }

    @Override //DB interfacing is required (OK)
    public boolean deleteSaleTransaction(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
        if (loggedUser == null || (!this.loggedUser.isAdministator() && !this.loggedUser.isShopManager()
                && !this.loggedUser.isCashier())) { // @throws UnauthorizedException if there is no logged user or if it has not the rights to perform the operation (TODO: ASSUME ONLY THESE RIGHTS, avoiding unuseful checks)
            throw new UnauthorizedException();
        }
        if (transactionId == null || transactionId <= 0) { // @throws InvalidTransactionIdException if the transaction id less than or equal to 0 or if it is null (TODO: CHECK CONSISTENCY WITH startSaleTransaction())
            throw new InvalidTransactionIdException();
        }
        if (!saleTransactions.containsKey(transactionId)) { //@return false if the transaction does not exists
            return false;
        }
        //Get saleTransaction from collection
        SaleTransactionImpl saleTr = (SaleTransactionImpl) saleTransactions.get(transactionId);
        if (saleTr.getStatus().equals("PAYED")) { //@return false if it has been payed
            return false;
        }
        //UPDATE DATABASE: This method deletes a sale transaction with given unique identifier from the system's data store.
        try {
            db.deleteSaleTransactionDB(saleTr);
        } catch (SQLException e) {
            e.printStackTrace();
            return false; //@return false if there are some problems with the db
        }
        //Remove also from collection (This method deletes a sale transaction with given unique identifier from the system's data store.)
        saleTransactions.remove(transactionId);
        for(CartItem item : saleTr.getCart().values()){
            item.getProduct().setQuantity(item.getProduct().getQuantity() + item.getQuantity()); //restore quantity productType
            for (String rfid : item.getProdRFIDs()) {
                products.put(rfid, new Product(rfid, item.getBarCode())); //re-add specific product
            }
        }
        return true; //@return ture if the transaction has been successfully deleted
    }

    @Override // OK
    public SaleTransaction getSaleTransaction(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
        if (loggedUser == null || (!this.loggedUser.isAdministator() && !this.loggedUser.isShopManager()
                && !this.loggedUser.isCashier())) { // @throws UnauthorizedException if there is no logged user or if it has not the rights to perform the operation (TODO: ASSUME ONLY THESE RIGHTS, avoiding unuseful checks)
            throw new UnauthorizedException();
        }
        if (transactionId == null || transactionId <= 0) { // @throws InvalidTransactionIdException if the transaction id less than or equal to 0 or if it is null (TODO: CHECK CONSISTENCY WITH startSaleTransaction())
            throw new InvalidTransactionIdException();
        }
        if (!saleTransactions.containsKey(transactionId)) { //@return (transaction closed), null otherwise
            return null;
        }
        //Get the transaction
        SaleTransactionImpl saleTr = (SaleTransactionImpl) saleTransactions.get(transactionId);
        if (saleTr.getStatus().equals("OPEN")) { //THE TRANSACTION MUST BE CLOSED! TODO: It is ambiguous from API (?)
            return null;
        }
        return saleTransactions.get(transactionId); //@return the transaction if it is available (transaction closed)
    }

    @Override // OK
    public Integer startReturnTransaction(Integer transactionId) throws /*InvalidTicketNumberException,*/ InvalidTransactionIdException, UnauthorizedException {
        if (loggedUser == null || (!this.loggedUser.isAdministator() && !this.loggedUser.isShopManager()
                && !this.loggedUser.isCashier())) { // @throws UnauthorizedException if there is no logged user or if it has not the rights to perform the operation (TODO: ASSUME ONLY THESE RIGHTS, avoiding unuseful checks)
            throw new UnauthorizedException();
        }
        if (transactionId == null || transactionId <= 0) { // @throws InvalidTransactionIdException if the transaction id less than or equal to 0 or if it is null (TODO: CHECK CONSISTENCY WITH startSaleTransaction())
            throw new InvalidTransactionIdException();
        }
        if (!saleTransactions.containsKey(transactionId)) { //@return -1 if the transaction is not available
            return -1;
        }
        //Get the saleTransaction
        SaleTransactionImpl saleTr = (SaleTransactionImpl) saleTransactions.get(transactionId);
        if (!saleTr.getStatus().equals("PAYED")) //TODO; assume to return -1 -> from api doc "This method starts a new return transaction for units of products that have already been sold and payed"
            return -1;
        //Create a new returnTransaction
        ReturnTransaction returnTr = new ReturnTransaction("OPEN", saleTransactions.get(transactionId));
        //Add in the collection
        returnTransactions.put(returnTr.getBalanceId(), returnTr);
        return returnTr.getBalanceId(); //@return the id of the return transaction (>= 0)
    }

    @Override // OK
    public boolean returnProduct(Integer returnId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
        if (loggedUser == null || (!this.loggedUser.isAdministator() && !this.loggedUser.isShopManager()
                && !this.loggedUser.isCashier())) { // @throws UnauthorizedException if there is no logged user or if it has not the rights to perform the operation (TODO: ASSUME ONLY THESE RIGHTS, avoiding unuseful checks)
            throw new UnauthorizedException();
        }
        if (returnId == null || returnId <= 0) { // @throws InvalidTransactionIdException if the transaction id less than or equal to 0 or if it is null (TODO: CHECK CONSISTENCY WITH startReturnTransaction())
            throw new InvalidTransactionIdException();
        }
        if (productCode == null || productCode.equals("") || !ProductTypeImpl.validateProdCode(productCode)) { //@throws InvalidProductCodeException if the product code is empty, null or invalid
            throw new InvalidProductCodeException();
        }
        if (amount < 1) { //@throws InvalidQuantityException if the quantity is less than or equal to 0
            throw new InvalidQuantityException();
        }
        if (!inventory.containsKey(productCode)) { //@return false if the the product to be returned does not exists
            return false;
        }
        if (!returnTransactions.containsKey(returnId)) { //@return false if the transaction does not exist
            return false;
        }
        //Get returnTransaction
        ReturnTransaction retTr = returnTransactions.get(returnId);
        if (!retTr.getSaleTransaction().getCart().containsKey(productCode)) { //@return false if it was not in the transaction (TODO: it is ambiguous, assume is referred to the product)
            return false;
        }
        if (amount > retTr.getSaleTransaction().getCart().get(productCode).getQuantity()) {//@return false if the amount is higher than the one in the sale transaction
            return false;
        }
        //Check if item is already in the returnCart else add new instance
        if (!retTr.getReturnedCart().containsKey(productCode)) {
            CartItem returnedItem = new CartItem(amount, inventory.get(productCode));
            retTr.getReturnedCart().put(returnedItem.getProduct().getBarCode(),returnedItem);
        }
        //Else update the quantity in the collection
        else {
            retTr.getReturnedCart().get(productCode).addQuantity(amount);
        }
        //Recompute returnValue
        retTr.computeReturnValue();
        return true; //@return  true if the operation is successful
    }

    @Override
    public boolean returnProductRFID(Integer returnId, String RFID) throws InvalidTransactionIdException, InvalidRFIDException, UnauthorizedException {
        if (loggedUser == null || (!this.loggedUser.isAdministator() && !this.loggedUser.isShopManager()
                && !this.loggedUser.isCashier())) { // @throws UnauthorizedException if there is no logged user or if it has not the rights to perform the operation (TODO: ASSUME ONLY THESE RIGHTS, avoiding unuseful checks)
            throw new UnauthorizedException();
        }
        if (returnId == null || returnId <= 0) { // @throws InvalidTransactionIdException if the transaction id less than or equal to 0 or if it is null (TODO: CHECK CONSISTENCY WITH startReturnTransaction())
            throw new InvalidTransactionIdException();
        }
        if(RFID == null || RFID.equals("") || !Product.validateRFID(RFID)){ //  @throws InvalidRFIDException if the RFID code is empty, null or invalid
            throw new InvalidRFIDException();
        }
        if (!returnTransactions.containsKey(returnId)){
            return false;
        }
//        if(!products.containsKey(RFID)){ //return false if the RFID does not exist,
//            return false;
//        }
        //Get returnTransaction
        ReturnTransaction retTr = returnTransactions.get(returnId);
        ProductTypeImpl tmpProd = null;
        boolean presenceInSaleCartFlag = false;
        for(CartItem item : retTr.getSaleTransaction().getCart().values()){
            if(item.getProdRFIDs().contains(RFID)){ //if the rfid is not present in the related saleTransaction cart means that the product is not present in the system, or the product is in another saleTransaction cart
                presenceInSaleCartFlag = true;
                tmpProd = (ProductTypeImpl) item.getProduct(); // if true keep trace of ProductImpl related to that specific RFID
            }
        }
        if(!presenceInSaleCartFlag){
            return false;
        }
//        ProductTypeImpl tmpProd = (ProductTypeImpl) inventory.get(products.get(RFID).getBarCode()); //Get the current productType
//        if(!retTr.getSaleTransaction().getCart().containsKey(tmpProd.getBarCode())){ // if the product is not in the cart of the related sale transaction
//            return false;
//        }
//        if(!retTr.getSaleTransaction().getCart().get(tmpProd.getBarCode()).getProdRFIDs().contains(RFID)){ //Check if the specific RFID is present in the saleTransaction cart
//            return false;
//        }
        //Check if item is already in the returnCart else add new instance
        if (!retTr.getReturnedCart().containsKey(tmpProd.getBarCode())) {
            CartItem returnedItem = new CartItem(1, inventory.get(tmpProd.getBarCode()));
            retTr.getReturnedCart().put(returnedItem.getProduct().getBarCode(),returnedItem);
            returnedItem.addProdRFID(RFID);
        }
        //Else update the quantity in the collection
        else {
            retTr.getReturnedCart().get(tmpProd.getBarCode()).addQuantity(1);
            retTr.getReturnedCart().get(tmpProd.getBarCode()).addProdRFID(RFID);
        }
        retTr.getReturnedCart().get(tmpProd.getBarCode()).addProdRFID(RFID);
        //Recompute returnValue
        retTr.computeReturnValue();
        return true; //@return  true if the operation is successful
    }
    
    @Override //DB interfacing is required (OK)
    public boolean endReturnTransaction(Integer returnId, boolean commit) throws InvalidTransactionIdException, UnauthorizedException {
        if (loggedUser == null || (!this.loggedUser.isAdministator() && !this.loggedUser.isShopManager()
                && !this.loggedUser.isCashier())) { // @throws UnauthorizedException if there is no logged user or if it has not the rights to perform the operation (TODO: ASSUME ONLY THESE RIGHTS, avoiding unuseful checks)
            throw new UnauthorizedException();
        }
        if (returnId == null || returnId <= 0) { // @throws InvalidTransactionIdException if the transaction id less than or equal to 0 or if it is null (TODO: CHECK CONSISTENCY WITH startReturnTransaction())
            throw new InvalidTransactionIdException();
        }
        if (!returnTransactions.containsKey(returnId)) { //if the returnId does not correspond to an active return transaction (TODO: based on system definition (commit phase) we can assume only to have a PAYED/OPEN returnTransactions)
            return false;
        }
        if(!returnTransactions.get(returnId).getStatus().equals("OPEN")){
            return false;
        }
        if (!commit) {
            //remove instance only in collection
            returnTransactions.remove(returnId); //From API description: thus the whole transaction is undone.
            return true; //@return true if the operation is successful
        }
        //Commit operation:  If committed, the return transaction must be persisted in the system's memory.
        ReturnTransaction retTr = returnTransactions.get(returnId);
        SaleTransactionImpl saleTr = (SaleTransactionImpl) saleTransactions.get(retTr.getSaleTransaction().getBalanceId());
        //Evaluate the return amount
        double totAmount = (-1) * retTr.getMoney(); //getMoney() returns a negative value(!!!)
        if (totAmount > saleTr.getPrice()) { //TODO: Ulterior check, this should not happen...
            return false;
        }
        //Update DB
        try {
            db.commitReturnTransactionDB(saleTr, retTr.getReturnedCart(), (saleTr.getPrice() - totAmount));//TODO review
            db.addReturnTransactionDB(retTr);//TODO review
        } catch (SQLException e) {
            e.printStackTrace();
            return false; //@return false if there is some problem with the db
        }
        //Re-add each related quantity of returned value to the product quantity (persistent in DB)
        for (CartItem obj : retTr.getReturnedCart().values()) {
            if (saleTr.getCart().containsKey(obj.getProduct().getBarCode())) { //TODO: ulterior check, maybe is unuseful
                ProductTypeImpl tmpProd = (ProductTypeImpl) obj.getProduct(); //Get the product
                //UPDATE DB: A closed return transaction can be committed (i.e. <commit> = true) thus it increases the product quantity available on the shelves && This method updates the transaction status (decreasing the number of units sold by the number of returned one and decreasing the final price).
                try {
                    db.openConnection();
                    db.updateProductType(tmpProd.getId(), tmpProd.getLocation(), tmpProd.getBarCode(), tmpProd.getProductDescription(), tmpProd.getPricePerUnit(),
                            (tmpProd.getQuantity() + obj.getQuantity()), tmpProd.getDiscountRate(), tmpProd.getNote());
                    db.closeConnection();
                } catch (SQLException e) {
                    e.printStackTrace();
                    return false; //@return false if there is some problem with the db
                }
                //UPDATE COLLECTIONS: A closed return transaction can be committed (i.e. <commit> = true) thus it increases the product quantity available on the shelves && This method updates the transaction status (decreasing the number of units sold by the number of returned one and decreasing the final price).
                //Update quantity in the inventory collection and in the cart of the saleTransaction object in the collection
                saleTr.getCart().get(obj.getProduct().getBarCode()).decreaseQuantity(obj.getQuantity());
                inventory.get(obj.getProduct().getBarCode()).setQuantity(inventory.get(obj.getProduct().getBarCode()).getQuantity() + obj.getQuantity());
                //Re-add to collection all the RFIDs (the db is update in the db.commitReturnTransaction() )
                for(String rfid : obj.getProdRFIDs()){
                    products.put(rfid, new Product(rfid, obj.getBarCode()));
                }
            } else {
                return false; //TODO: critical error if it occurs
            }
        }
        //Update single object (price of saleTransaction local memory)
        saleTr.setPrice(saleTr.getPrice() - totAmount);
        retTr.setStatus("CLOSED"); //The return transaction is payed the returnTransaction in the local memory
        return true; //@return true if the operation is successful
    }

    @Override //DB interfacing is required (OK)
    public boolean deleteReturnTransaction(Integer returnId) throws InvalidTransactionIdException, UnauthorizedException {
        if (loggedUser == null || (!this.loggedUser.isAdministator() && !this.loggedUser.isShopManager()
                && !this.loggedUser.isCashier())) { // @throws UnauthorizedException if there is no logged user or if it has not the rights to perform the operation (TODO: ASSUME ONLY THESE RIGHTS, avoiding unuseful checks)
            throw new UnauthorizedException();
        }
        if (returnId == null || returnId <= 0) { // @throws InvalidTransactionIdException if the transaction id less than or equal to 0 or if it is null (TODO: CHECK CONSISTENCY WITH startReturnTransaction())
            throw new InvalidTransactionIdException();
        }
        if (!returnTransactions.containsKey(returnId)) { //@return false if it doesn't exist
            return false;
        }
        //Ger returnTransaction and related saleTransaction
        ReturnTransaction retTr = returnTransactions.get(returnId);
        SaleTransactionImpl saleTr = (SaleTransactionImpl) retTr.getSaleTransaction();
        if (!retTr.getStatus().equals("CLOSED")) { //@return false if it has been payed (TODO assume no open returnTransaction in the system's collection)
            return false;
        }
        //UPDATE DB:  It affects the quantity of product sold in the connected sale transaction (and consequently its price)
        try {
            db.deleteReturnTransactionDB(retTr, saleTr);
        } catch (SQLException e) {
            e.printStackTrace();
            return false; //@return if there are some problems with the db
        }
        //Evaluate the total price to add
        double totAmount = (-1) * retTr.getMoney();//getMoney() in a returnTransaction return a negative value
        saleTr.setPrice(saleTr.getPrice() + totAmount);
        //Re-add all the returned object to the sale cart and to inventory
        for (CartItem obj : retTr.getReturnedCart().values()) {
            ProductTypeImpl tmpProd = (ProductTypeImpl) obj.getProduct();
            for(String rfid: obj.getProdRFIDs()){
                products.put(rfid, new Product(rfid, obj.getBarCode())); //Remove the RFIDs involved in the return (if a return is deleted they will miss in the Product collection) -> Product will be deleted in the DB in the db.returnDeleteTransaction().
            }
            //UPDATE DB: the quantity of product available on the shelves.
            try {
                db.openConnection();
                db.updateProductType(tmpProd.getId(), tmpProd.getLocation(), tmpProd.getBarCode(), tmpProd.getProductDescription(), tmpProd.getPricePerUnit(),
                        (tmpProd.getQuantity() - obj.getQuantity()), tmpProd.getDiscountRate(), tmpProd.getNote());
                db.closeConnection();
            } catch (SQLException e) {
                e.printStackTrace();
                return false; //@return if there are some problems with the db
            }
            //UPDATE COLLECTION: the quantity of product available on the shelves && It affects the quantity of product sold in the connected sale transaction (and consequently its price)
            saleTr.getCart().get(obj.getProduct().getBarCode()).decreaseQuantity(obj.getQuantity());
            inventory.get(obj.getProduct().getBarCode()).setQuantity(inventory.get(obj.getProduct().getBarCode()).getQuantity() + obj.getQuantity());
        }
        //Remove from collection
        returnTransactions.remove(retTr.getBalanceId());
        return true; //@return  true if the transaction has been successfully deleted
    }

    /********************************************************************************************************************************************
     *                                                                                                                                          *
     *                             PAYMENTS MANAGEMENT: see 'EZShopInterface.java' for the complete API descriptions.                           *
     *                                                                                                                                          *
     ********************************************************************************************************************************************/

    @Override
    public double receiveCashPayment(Integer ticketNumber, double cash) throws InvalidTransactionIdException, InvalidPaymentException, UnauthorizedException {
        if (this.loggedUser == null || (!this.loggedUser.isAdministator()
                && !this.loggedUser.isShopManager() && !this.loggedUser.isCashier()))
            throw new UnauthorizedException();
        if (cash <= 0)
            throw new InvalidPaymentException();
        if (ticketNumber == null || ticketNumber <= 0)
            throw new InvalidTransactionIdException();

        SaleTransactionImpl sale = (SaleTransactionImpl) saleTransactions.get(ticketNumber);
        if (sale == null)                            //DB PROBLEMS
            return -1;
        double price = sale.getPrice();
        if (price > cash)
            return -1;
        Payment tmp = new CashPayment(price, cash - price);
        sale.setPayment(tmp);
        try {
            db.saleTransactionPaymentDB(sale, tmp);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        a.addBalanceOperation(sale);
        return cash - price;
    }

    @Override
    public boolean receiveCreditCardPayment(Integer ticketNumber, String creditCard) throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {
        if (this.loggedUser == null || (!this.loggedUser.isAdministator()
                && !this.loggedUser.isShopManager() && !this.loggedUser.isCashier()))
            throw new UnauthorizedException();
        if (ticketNumber == null || ticketNumber <= 0)
            throw new InvalidTransactionIdException();
        if (creditCard == null || creditCard.isEmpty() || !CreditCard.validate(creditCard))
            throw new InvalidCreditCardException();

        SaleTransactionImpl sale = (SaleTransactionImpl) saleTransactions.get(ticketNumber);
        CreditCard c = creditcards.get(creditCard);
        if (sale == null || c == null)                            
            return false;
        double balance = c.getBalance();
        double price = sale.getPrice();
        if (balance < price)
            return false;
        Payment tmp = new CreditCardPayment(price, c);
        sale.setPayment(tmp);
        try {
            db.saleTransactionPaymentDB(sale, tmp);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        c.updateBalance(price);
        a.addBalanceOperation(sale);
        return true;
    }

    @Override
    public double returnCashPayment(Integer returnId) throws InvalidTransactionIdException, UnauthorizedException {
        if (this.loggedUser == null || (!this.loggedUser.isAdministator()
                && !this.loggedUser.isShopManager() && !this.loggedUser.isCashier()))
            throw new UnauthorizedException();
        if (returnId == null || returnId <= 0)
            throw new InvalidTransactionIdException();
        ReturnTransaction ret = returnTransactions.get(returnId);
        if (ret == null || !ret.getStatus().equals("CLOSED"))                                //DB PROBLEMS
            return -1;
        double value = ret.getMoney();
        Payment tmp = new CashPayment(value, 0);
        ret.setPayment(tmp);
        try {
            db.returnTransactionPaymentDB(ret, tmp);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        a.addBalanceOperation(ret);
        return value;
    }

    @Override
    public double returnCreditCardPayment(Integer returnId, String creditCard) throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {
        if (this.loggedUser == null || (!this.loggedUser.isAdministator()
                && !this.loggedUser.isShopManager() && !this.loggedUser.isCashier()))
            throw new UnauthorizedException();
        if (returnId == null || returnId <= 0)
            throw new InvalidTransactionIdException();
        if (creditCard == null || creditCard.isEmpty() || !CreditCard.validate(creditCard))
            throw new InvalidCreditCardException();
        CreditCard c = creditcards.get(creditCard);
        ReturnTransaction ret = returnTransactions.get(returnId);
        if (ret == null || !ret.getStatus().equals("CLOSED") || c == null)                                //ORDINE EXCP CARD E CARDREG
            return -1;
        double value = ret.getMoney();
        Payment tmp = new CreditCardPayment(value, c);
        ret.setPayment(tmp);
        try {
            db.returnTransactionPaymentDB(ret, tmp);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        a.addBalanceOperation(ret);
        c.updateBalance(value);
        return value;
    }

    /********************************************************************************************************************************************
     *                                                                                                                                          *
     *                      BALANCE OPERATIONS MANAGEMENT: see 'EZShopInterface.java' for the complete API descriptions.                        *
     *                                                                                                                                          *
     ********************************************************************************************************************************************/

    @Override
    public boolean recordBalanceUpdate(double toBeAdded) throws UnauthorizedException {
        if (this.loggedUser == null || (!this.loggedUser.isAdministator()
                && !this.loggedUser.isShopManager()))
            throw new UnauthorizedException();
        if (a.getBalance() + toBeAdded < 0)
            return false;
        else {
            if (toBeAdded >= 0)
                a.addBalanceOperation(new Credit(LocalDate.now(), "CREDIT", toBeAdded));    //BALANCE ID?????
            else
                a.addBalanceOperation(new Debit(LocalDate.now(), "DEBIT", toBeAdded));
        }
        return true;
    }

    @Override
    public List<BalanceOperation> getCreditsAndDebits(LocalDate from, LocalDate to) throws UnauthorizedException {
        if (this.loggedUser == null || (!this.loggedUser.isAdministator()
                && !this.loggedUser.isShopManager()))
            throw new UnauthorizedException();
        if (from != null && to != null && from.isAfter(to))
            return a.getByDate(to, from);
        else
            return a.getByDate(from, to);
    }

    @Override
    public double computeBalance() throws UnauthorizedException {
        if (this.loggedUser == null || (!this.loggedUser.isAdministator()
                && !this.loggedUser.isShopManager()))
            throw new UnauthorizedException();
        return a.getBalance();
    }
}
