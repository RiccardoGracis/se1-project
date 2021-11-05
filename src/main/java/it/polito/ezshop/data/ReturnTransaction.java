package it.polito.ezshop.data;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Optional;

public class ReturnTransaction extends Debit{
    private HashMap<String,CartItem> returnedCart;
    private String status;
    private Optional<Payment> p;
    private SaleTransaction saleTransaction;

    public ReturnTransaction(String status, SaleTransaction saleTransaction) {
        super(LocalDate.now(),"RETURN",0.0);
        this.p = Optional.empty();
        this.returnedCart = new HashMap<String, CartItem>();
        this.status = status;
        this.saleTransaction = saleTransaction;
    }
    //Tested
    public SaleTransactionImpl getSaleTransaction(){
        return (SaleTransactionImpl) this.saleTransaction;
    }
    //Tested
    public HashMap<String, CartItem> getReturnedCart(){
        return returnedCart;
    }
    //Tested
    public void setReturnedCart(HashMap<String, CartItem> returnedCart) {
        this.returnedCart = returnedCart;
    }
    //Tested
    public void setStatus(String status) {
        this.status = status;
    }
    //Tested
    public String getStatus() {
        return status;
    }
    //Tested
    public void computeReturnValue(){
        double subTot = this.returnedCart.values().stream().mapToDouble(obj -> (1 - obj.getProductDiscount()) * obj.getProduct().getPricePerUnit() * obj.getQuantity()).sum();
        //Check if it is correct!!!
        this.setMoney((-1)*(subTot * (1 - this.saleTransaction.getDiscountRate())));
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
    public Payment getPayment(){
        return this.p.orElse(null);
    }
    //Tested
    public boolean isPayed(){
        return this.p.isPresent() && this.status.equals("PAYED");
    }
    //Tested
    public Boolean isCardPayment() {
        //if status is PAYED -> p is present
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
}
