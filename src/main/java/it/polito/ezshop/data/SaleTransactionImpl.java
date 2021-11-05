package it.polito.ezshop.data;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class SaleTransactionImpl extends Credit implements SaleTransaction{
    private Double discountRate;
    private String status;
    //private Optional<LoyaltyCard> loyaltyCard;
    private HashMap<String,CartItem> cart;
    private Optional<Payment> p;

    //Tested
    public SaleTransactionImpl(Double discountRate, String status) {
        super(LocalDate.now(), "TRANSACTION", 0.0);
        //loyaltyCard = Optional.empty();
        p = Optional.empty();
        this.discountRate = discountRate;
        this.status=status;
        this.cart = new HashMap<String, CartItem>();
    } 

    @Override //tested
    public Integer getTicketNumber() {
        return this.getBalanceId();
    }

    @Override //tested
    public void setTicketNumber(Integer ticketNumber) {this.setBalanceId(ticketNumber); }

    @Override
    public List<TicketEntry> getEntries() {
        return new ArrayList<>(this.getCart().values());
    }

    @Override
    public void setEntries(List<TicketEntry> entries) {
        HashMap<String, CartItem> map = new HashMap<>();
        for (TicketEntry obj : entries) {
            if (map.put(obj.getBarCode(), (CartItem) obj) != null) {
                throw new IllegalStateException("Duplicate key");
            }
        }
        this.setCart(map);
        this.computeTransactionTotal();
    }

    @Override //Tested
    public double getDiscountRate() {
        return this.discountRate;
    }

    @Override //Tested
    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
        computeTransactionTotal();
    }

    @Override //Tested
    public double getPrice() {
        return this.getMoney();
    }

    @Override //Tested
    public void setPrice(double price) {
        this.setMoney(price);
    }
    //Tested
    public HashMap<String, CartItem> getCart(){
        return this.cart;
    }
    //Tested
    public String getStatus(){
        return this.status;
    }
    //Tested
    public void applyProdDiscount(Double discountRate, String productCode){
       cart.get(productCode).setProductDiscount(discountRate);
        computeTransactionTotal(); //IMPLICIT
    }
    //Tested
    public void setStatus(String newStatus){
            this.status=newStatus;
    }
    //Tested
    public void computeTransactionTotal(){
        double subTot = this.cart.values().stream().mapToDouble(obj -> (1 - obj.getProductDiscount()) * obj.getProduct().getPricePerUnit() * obj.getQuantity()).sum();
        this.setMoney(subTot * (1 - this.discountRate));
    }
    //Tested
    public void setPayment(Payment p){
    	if (p==null) {
            this.p = Optional.empty();
            return;
        }
        this.p = Optional.of(p);
        this.status="PAYED";
    }
    //Tested
    public Boolean isCardPayment() {
        if (this.p.isPresent())
            return p.get() instanceof CreditCardPayment;
        else
            return null;
    }
    //Tested
    public CreditCardPayment getPaymentCreditCard(){
        if (this.p.isPresent() && this.p.get() instanceof CreditCardPayment)
            return (CreditCardPayment) p.get();
        return null;
    }
    //Tested
    public CashPayment getPaymentCash() {
        if (this.p.isPresent() && this.p.get() instanceof CashPayment)
            return (CashPayment) p.get();
        return null;
    }
    //Tested
    public void setCart(HashMap<String, CartItem> cart){
        this.cart = cart;
    }
    //Tested
    public Payment getPayment(){
        return this.p.orElse(null);
    }
}
