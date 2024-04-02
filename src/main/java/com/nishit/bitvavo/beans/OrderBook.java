package com.nishit.bitvavo.beans;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.PriorityQueue;

@AllArgsConstructor
@Getter
public class OrderBook {
    private PriorityQueue<Order> buySideOrders;
    private PriorityQueue<Order> sellSideOrders;


    @Override
    public String toString() {
     //TODO : Waiting for clarity from Bitvavo on how to frame the output correctly.
        return null;
    }
}
