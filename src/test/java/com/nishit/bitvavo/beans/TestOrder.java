package com.nishit.bitvavo.beans;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
public class TestOrder {

    @Test
    public void createOrder_allValidInput(){
        Order order = new Order("1234", "B", "100", "1000");
        Class<?> classObj = order.getClass();
        Field[] fields = classObj.getDeclaredFields();
        Assert.assertEquals(Order.NUMBER_OF_FIELDS_IN_THIS_CLASS, (Integer)fields.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createOrder_InvalidOrderId(){
        new Order("ABC1234", "B", "100", "1000");
    }

    @Test(expected = IllegalArgumentException.class)
    public void createOrder_InvalidSide(){
        new Order("1234", "N", "100", "1000");
    }

    @Test(expected = IllegalArgumentException.class)
    public void createOrder_InvalidPrice(){
        new Order("1234", "B", "-100", "1000");
    }

    @Test(expected = IllegalArgumentException.class)
    public void createOrder_InvalidQuantity(){
        new Order("1234", "B", "100", "1000000000");
    }

}
