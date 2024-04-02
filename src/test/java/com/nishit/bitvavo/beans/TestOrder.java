package com.nishit.bitvavo.beans;

import org.junit.Test;

public class TestOrder {

    @Test
    public void createOrder_allValidInput(){
        new Order("1234", "B", 100, 1000);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createOrder_InvalidOrderId(){
        new Order("ABC1234", "B", 100, 1000);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createOrder_InvalidSide(){
        new Order("1234", "N", 100, 1000);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createOrder_InvalidPrice(){
        new Order("1234", "B", -100, 1000);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createOrder_InvalidQuantity(){
        new Order("1234", "B", 100, 1000000000);
    }

}
