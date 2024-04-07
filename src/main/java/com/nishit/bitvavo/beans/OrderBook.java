package com.nishit.bitvavo.beans;

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

        List<Order> buySideOrders = createListFromQueue(copyOfBuySide);
        List<Order> sellSideOrders = createListFromQueue(copyOfSellSide);

        int minSize = Math.min(buySideOrders.size(), sellSideOrders.size());

        StringBuilder orderBookBuilder = new StringBuilder();
        for(int i = 0 ; i < minSize ; i++){
            orderBookBuilder.append(createRecordFromOrder(buySideOrders.get(i), BuyOrSell.B)) //BuyOrder converted to the given format
                            .append(ORDER_BOOK_DELIMITER)
                            .append(createRecordFromOrder(sellSideOrders.get(i), BuyOrSell.S)) // SellOrder converted to the given format
                            .append(NEW_LINE);
        }

        if(buySideOrders.size() == sellSideOrders.size()){
            return orderBookBuilder.toString();
        }

        if(buySideOrders.size() > sellSideOrders.size()){
            for(int i = minSize ; i < buySideOrders.size() ; i++){
                orderBookBuilder.append(createRecordFromOrder(buySideOrders.get(i), BuyOrSell.B))
                                .append(ORDER_BOOK_DELIMITER)
                                .append(createEmptyStringOfSize());
                orderBookBuilder.append(NEW_LINE);
            }
        } else {
            for(int i = minSize ; i < sellSideOrders.size() ; i++){
                orderBookBuilder.append(createEmptyStringOfSize())
                                .append(ORDER_BOOK_DELIMITER)
                                .append(createRecordFromOrder(sellSideOrders.get(i), BuyOrSell.S))
                                .append(NEW_LINE);
            }
        }

        return orderBookBuilder.toString();
    }
    private String createEmptyStringOfSize(){
        return String.format("%19s", "").substring(0, 19);
    }

    private List<Order> createListFromQueue(PriorityQueue<Order> queue){
        List<Order> list = new LinkedList<>();
        while(!queue.isEmpty()){
            list.add(queue.remove());
        }
        return list;
    }

    private String createRecordFromOrder(Order order, BuyOrSell buyOrSell){
        StringBuilder builder = new StringBuilder();
        switch (buyOrSell){
            case B:
                builder
                    .append(formatNumberToCommaAndSize(order.getQuantity(), QUANTITY_SIZE_LIMIT)) // Formatting to use commas and fixed length of 11 digits
                    .append(BLANK_SPACE)
                    .append(formatNumberToSize(order.getPrice(), PRICE_SIZE_LIMIT)) // Formatting to fixed length of 6 digits without commas
                    .append(BLANK_SPACE);
                break;
            case S:
                builder
                    .append(BLANK_SPACE)
                    .append(formatNumberToSize(order.getPrice(), PRICE_SIZE_LIMIT)) // Formatting to fixed length of 6 digits without commas
                    .append(BLANK_SPACE)
                    .append(formatNumberToCommaAndSize(order.getQuantity(), QUANTITY_SIZE_LIMIT)); // Formatting to use commas and fixed length of 11 digits
                break;
        }
        return builder.toString();
    }

    private String formatNumberToCommaAndSize(int number, int size) {
        // Format number with commas
        String formattedNumberWithCommas = String.format("%,d", number);

        // Pad the formatted number to ensure it's of size 'size'
        String paddedNumber = String.format("%" + size + "s", formattedNumberWithCommas);

        return paddedNumber;
    }

    private String formatNumberToSize(int number, int size) {

        // Pad the formatted number to ensure it's of size 'size'
        String paddedNumber = String.format("%" + size + "s", number);

        return paddedNumber;
    }
}
