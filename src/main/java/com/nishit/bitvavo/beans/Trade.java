package com.nishit.bitvavo.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Trade {
    private final String aggressOrderId; //orderId that triggered the match
    private final String restingOrderId; //orderId of existing order in the orderBook
    private final Integer price;
    private final Integer quantity;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("trade").append(" ").append(aggressOrderId).append(",").append(restingOrderId).append(",").append(price).append(",").append(quantity);
        return builder.toString();
    }
}