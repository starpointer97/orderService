package com.nishit.bitvavo.beans;

public class OrderRow {
    private Integer quantity;
    private Integer price;
    private BuyOrSell side;

    public OrderRow(Integer quantity, Integer price, String side){
        this.quantity = quantity;
        this.price = price;
        this.side = BuyOrSell.valueOf(side);
    }
}
