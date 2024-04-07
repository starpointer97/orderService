package com.nishit.bitvavo.beans;

import com.nishit.bitvavo.utils.OrderValidator;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
@Builder
public class Order {

    public static final Integer NUMBER_OF_FIELDS_IN_THIS_CLASS = 7; //change this count depending on number of fields
    public static final Integer NUMBER_OF_MEMBER_FIELDS_IN_INPUT = 4; //insertionTimeStamp and static fields
    private final String orderId;
    private final BuyOrSell side;
    private final Integer price;
    private final Integer quantity;
    private Long insertionTimestamp;

    public Order(String orderId, String side, String price, String quantity){
        OrderValidator.isOrderParamsValid(orderId, side, price, quantity);
        this.orderId = orderId;
        this.side = BuyOrSell.valueOf(side);
        this.price = Integer.parseInt(price);
        this.quantity = Integer.parseInt(quantity);
        this.insertionTimestamp = System.nanoTime();
    }

    public Order(String orderId, BuyOrSell side, Integer price, Integer quantity, Long insertionTimestamp){
        this.orderId = orderId;
        this.side = side;
        this.price = price;
        this.quantity = quantity;
        this.insertionTimestamp = insertionTimestamp;
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Order order = (Order) o;
        return Objects.equals(orderId, order.orderId) && side == order.side && Objects.equals(price, order.price)
            && Objects.equals(quantity, order.quantity);
    }

    @Override public int hashCode() {
        return Objects.hash(orderId, side, price, quantity);
    }
}
