package com.nishit.bitvavo.utils;

import com.nishit.bitvavo.beans.BuyOrSell;
import com.nishit.bitvavo.beans.Order;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import static com.nishit.bitvavo.constants.Constants.BLANK_SPACE;
import static com.nishit.bitvavo.constants.Constants.PRICE_SIZE_LIMIT;
import static com.nishit.bitvavo.constants.Constants.QUANTITY_SIZE_LIMIT;

public class OrderBookUtils {

    public static String formatNumberToCommaAndSize(int number, int size) {
        // Format number with commas
        String formattedNumberWithCommas = String.format("%,d", number);

        // Pad the formatted number to ensure it's of size 'size'
        String paddedNumber = String.format("%" + size + "s", formattedNumberWithCommas);

        return paddedNumber;
    }

    public static String formatNumberToSize(int number, int size) {

        // Pad the formatted number to ensure it's of size 'size'
        String paddedNumber = String.format("%" + size + "s", number);

        return paddedNumber;
    }

    public static String createEmptyStringOfSize(){
        return String.format("%19s", "").substring(0, 19);
    }

    public static List<Order> createListFromQueue(PriorityQueue<Order> queue){
        List<Order> list = new LinkedList<>();
        while(!queue.isEmpty()){
            list.add(queue.remove());
        }
        return list;
    }

    public static String createRecordFromOrder(Order order, BuyOrSell buyOrSell){
        StringBuilder builder = new StringBuilder();
        switch (buyOrSell){
            case B:
                builder
                    .append(OrderBookUtils.formatNumberToCommaAndSize(order.getQuantity(), QUANTITY_SIZE_LIMIT)) // Formatting to use commas and fixed length of 11 digits
                    .append(BLANK_SPACE)
                    .append(OrderBookUtils.formatNumberToSize(order.getPrice(), PRICE_SIZE_LIMIT)) // Formatting to fixed length of 6 digits without commas
                    .append(BLANK_SPACE);
                break;
            case S:
                builder
                    .append(BLANK_SPACE)
                    .append(OrderBookUtils.formatNumberToSize(order.getPrice(), PRICE_SIZE_LIMIT)) // Formatting to fixed length of 6 digits without commas
                    .append(BLANK_SPACE)
                    .append(OrderBookUtils.formatNumberToCommaAndSize(order.getQuantity(), QUANTITY_SIZE_LIMIT)); // Formatting to use commas and fixed length of 11 digits
                break;
        }
        return builder.toString();
    }

}
