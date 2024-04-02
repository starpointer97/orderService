package com.nishit.bitvavo.beans;

import java.util.List;

public class OrderBook {
    private List<OrderRow> buySideOrders;
    private List<OrderRow> sellSideOrders;

    @Override
    public String toString() {
     //TODO : Waiting for clarity from Bitvavo on how to frame the output correctly.
        return null;
    }
}
