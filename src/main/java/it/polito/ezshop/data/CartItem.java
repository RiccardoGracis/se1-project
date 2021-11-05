package it.polito.ezshop.data;

import java.util.ArrayList;

public class CartItem implements TicketEntry{
    private Integer quantity;
    private final ProductType product;
    private double productDiscount;
    private ArrayList<String> prodRFIDs;

    public CartItem(Integer quantity, ProductType product){
        this.product=product;
        this.quantity=quantity;
        this.productDiscount = 0.0;
        this.prodRFIDs = new ArrayList<String>();
    }
    //Tested
    public void decreaseQuantity(Integer amount){
        if (amount < 0)
            return;
        if (this.quantity > amount)
            this.quantity-=amount;
        else
            this.quantity=0;
    }
    //Tested
    public void addQuantity(Integer quantity) {
        if (quantity > 0)
            this.quantity += quantity;
    }
    //Tested
    public Integer getQuantity() {
        return this.quantity;
    }
    //Tested
    public ProductType getProduct() {
        return this.product;
    }
    //Tested
    public double getProductDiscount(){
        return productDiscount;
    }
    //Tested
    public void setProductDiscount(double productDiscount) {
        this.productDiscount = productDiscount;
    }
    //Tested
    @Override
    public String getBarCode() {
        return this.product.getBarCode();
    }
    //Tested
    @Override
    public void setBarCode(String barCode) {
        this.product.setBarCode(barCode);
    }
    //Tested
    @Override
    public String getProductDescription() {
        return this.product.getProductDescription();
    }
    //Tested
    @Override
    public void setProductDescription(String productDescription) {
        this.product.setProductDescription(productDescription);
    }
    //Tested
    @Override
    public int getAmount() {
        return this.getQuantity();
    }
    //Tested
    @Override
    public void setAmount(int amount) {
        this.quantity=amount;
    }
    //Tested
    @Override
    public double getPricePerUnit() {
        return this.product.getPricePerUnit();
    }
    //Tested
    @Override
    public void setPricePerUnit(double pricePerUnit) {
        this.product.setPricePerUnit(pricePerUnit);
    }
    //Tested
    @Override
    public double getDiscountRate() {
        return this.getProductDiscount();
    }
    //Tested
    @Override
    public void setDiscountRate(double discountRate) {
        this.setProductDiscount(discountRate);
    }

    public ArrayList<String> getProdRFIDs(){
        return this.prodRFIDs;
    }

    public void addProdRFID(String rfid){
        if(this.prodRFIDs.contains(rfid))
            return;
        this.prodRFIDs.add(rfid);
    }
}

