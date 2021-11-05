package it.polito.ezshop.integrationTests;

import it.polito.ezshop.data.*;
import org.junit.Test;
import java.util.HashMap;
import static org.junit.Assert.*;

public class CartItem_IntegrationTest {

    @Test
    public void testAddDecreaseQuantity(){
        ProductTypeImpl prod = new ProductTypeImpl("prod1","12345",19.90, "Testing product");
        CartItem cartItem = new CartItem(10,prod);
        cartItem.addQuantity(5);
        assertEquals((int)cartItem.getQuantity(), 15);
        cartItem.addQuantity(-5);
        assertEquals((int)cartItem.getQuantity(), 15);
        cartItem.addQuantity(0);
        assertEquals((int)cartItem.getQuantity(), 15);
        cartItem.decreaseQuantity(3);
        assertEquals((int)cartItem.getQuantity(), 12);
        cartItem.decreaseQuantity(30);
        assertEquals((int)cartItem.getQuantity(), 0);
    }

    @Test
    public void testGetQuantity(){
        ProductTypeImpl prod = new ProductTypeImpl("prod1","12345",19.90, "Testing product");
        CartItem cartItem = new CartItem(10,prod);
        cartItem.addQuantity(10);
        assertEquals((int)cartItem.getQuantity(), 20);
        cartItem.decreaseQuantity(5);
        assertEquals((int) cartItem.getQuantity(),15);
    }

    @Test
    public void testGetProduct(){
        ProductTypeImpl prod = new ProductTypeImpl("prod1","12345",19.90, "Testing product");
        CartItem cartItem = new CartItem(10,prod);
        assertEquals(cartItem.getProduct(),prod);
    }

    @Test
    public void testGetSetProductDiscount(){
        ProductTypeImpl prod = new ProductTypeImpl("prod1","12345",19.90, "Testing product");
        CartItem cartItem = new CartItem(10,prod);
        cartItem.setProductDiscount(0.20);
        assertEquals(cartItem.getProductDiscount(), 0.20,0.001);
    }

    @Test
    public void testGetSetBarCode(){
        ProductTypeImpl prod = new ProductTypeImpl("prod1","12345",19.90, "Testing product");
        CartItem cartItem = new CartItem(10,prod);
        assertEquals(cartItem.getBarCode(),"12345");
        cartItem.setBarCode("14674");//This could lead to inconsistency among products remember to check DB in order to avoid collisions
        assertEquals(cartItem.getBarCode(),"14674");
    }

    @Test
    public void testGetSetProductDescription(){
        ProductTypeImpl prod = new ProductTypeImpl("prod1","12345",19.90, "Testing product");
        CartItem cartItem = new CartItem(10,prod);
        assertEquals(cartItem.getProductDescription(),"prod1");
        cartItem.setProductDescription("New Testing Product");
        assertEquals(cartItem.getProductDescription(), "New Testing Product");
    }

    @Test
    public void testGetSetAmount(){
        ProductTypeImpl prod = new ProductTypeImpl("prod1","12345",19.90, "Testing product");
        CartItem cartItem = new CartItem(10,prod);
        assertEquals(cartItem.getAmount(),10);
        cartItem.setAmount(20);
        assertEquals(cartItem.getAmount(),20);
    }
    
    @Test
    public void testGetSetPricePerUnit(){
        ProductTypeImpl prod = new ProductTypeImpl("prod1","12345",19.90, "Testing product");
        CartItem cartItem = new CartItem(10,prod);
        assertEquals(cartItem.getPricePerUnit(), 19.90, 0.001);
        cartItem.setPricePerUnit(10.00);
        assertEquals(cartItem.getPricePerUnit(), 10.00, 0.001);
    }

    @Test
    public void testGetSetDiscountRate(){
        ProductTypeImpl prod = new ProductTypeImpl("prod1","12345",19.90, "Testing product");
        CartItem cartItem = new CartItem(10,prod);
        assertEquals(cartItem.getDiscountRate(), 0.00, 0.001);
        cartItem.setDiscountRate(0.25);
        assertEquals(cartItem.getDiscountRate(), 0.25, 0.001);
    }
}
