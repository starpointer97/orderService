package com.nishit.bitvavo.beans;

import lombok.Getter;

@Getter
public class Trade {
    private final String aggressOrderId; //orderId that triggered the match
    private final String restingOrderId; //orderId of existing order in the orderBook
    private final Integer price;
    private final Integer quantity;

    public Trade(String aggressOrderId, String restingOrderId, Integer price, Integer quantity){
        this.aggressOrderId = aggressOrderId;
        this.restingOrderId = restingOrderId;
        this.price = price;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("trade").append(" ").append(aggressOrderId).append(",").append(restingOrderId).append(",").append(price).append(",").append(quantity);
        return builder.toString();
    }
}