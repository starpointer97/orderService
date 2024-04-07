package com.nishit.bitvavo.beans;

import com.nishit.bitvavo.utils.OrderBookUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import static com.nishit.bitvavo.constants.Constants.*;

@AllArgsConstructor
@Getter
public class OrderBook {
    private PriorityQueue<Order> buySideOrders;
    private PriorityQueue<Order> sellSideOrders;


    @Override
    public String toString() {
        PriorityQueue<Order> copyOfBuySide = new PriorityQueue<>(buySideOrders);
        PriorityQueue<Order> copyOfSellSide = new PriorityQueue<>(sellSideOrders);

        List<Order> buySideOrders = OrderBookUtils.createListFromQueue(copyOfBuySide);
        List<Order> sellSideOrders = OrderBookUtils.createListFromQueue(copyOfSellSide);

        int minSize = Math.min(buySideOrders.size(), sellSideOrders.size());

        StringBuilder orderBookBuilder = new StringBuilder();
        for (int i = 0; i < minSize; i++) {
            orderBookBuilder.append(OrderBookUtils.createRecordFromOrder(buySideOrders.get(i), BuyOrSell.B)) //BuyOrder converted to the given format
                .append(ORDER_BOOK_DELIMITER).append(OrderBookUtils.createRecordFromOrder(sellSideOrders.get(i), BuyOrSell.S)) // SellOrder converted to the given format
                .append(NEW_LINE);
        }

        if (buySideOrders.size() == sellSideOrders.size()) {
            return orderBookBuilder.toString();
        }

        if (buySideOrders.size() > sellSideOrders.size()) {
            for (int i = minSize; i < buySideOrders.size(); i++) {
                orderBookBuilder.append(OrderBookUtils.createRecordFromOrder(buySideOrders.get(i), BuyOrSell.B)).append(ORDER_BOOK_DELIMITER).append(OrderBookUtils.createEmptyStringOfSize());
                orderBookBuilder.append(NEW_LINE);
            }
        }
        else {
            for (int i = minSize; i < sellSideOrders.size(); i++) {
                orderBookBuilder.append(OrderBookUtils.createEmptyStringOfSize()).append(ORDER_BOOK_DELIMITER)
                    .append(OrderBookUtils.createRecordFromOrder(sellSideOrders.get(i), BuyOrSell.S)).append(NEW_LINE);
            }
        }

        return orderBookBuilder.toString();
    }
}
