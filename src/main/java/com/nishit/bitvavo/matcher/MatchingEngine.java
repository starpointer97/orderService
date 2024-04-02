package com.nishit.bitvavo.matcher;

import com.google.common.annotations.VisibleForTesting;
import com.nishit.bitvavo.beans.BuyOrSell;
import com.nishit.bitvavo.beans.Order;
import com.nishit.bitvavo.beans.OrderBook;
import com.nishit.bitvavo.beans.Trade;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public class MatchingEngine {

    private static final MatchingEngine MATCHING_ENGINE = new MatchingEngine();

    @VisibleForTesting
    PriorityQueue<Order> buySideOrders;
    @VisibleForTesting
    PriorityQueue<Order> sellSideOrders;

    @VisibleForTesting
    MatchingEngine(){
        Comparator<Order> buySideComparator = Comparator.comparing(Order::getPrice).reversed().thenComparing(Order::getOrderId);
        Comparator<Order> sellSideComparator = Comparator.comparing(Order::getPrice).thenComparing(Order::getOrderId);

        buySideOrders = new PriorityQueue<>(buySideComparator);
        sellSideOrders = new PriorityQueue<>(sellSideComparator);
    }

    public OrderBook getOrderBook(){
        return new OrderBook(buySideOrders, sellSideOrders);
    }

    public List<Trade> addAndExecuteOrder(Order order){
        if(BuyOrSell.B.equals(order.getSide())){
            return executeBuyOrder(order);
        } else {
            return executeSellOrder(order);
        }
    }

    /**
     * For buy side order, validate if sell side has a resting order with price <= orderPrice
     * @param order
     */
    private List<Trade> executeBuyOrder(Order order){
        List<Trade> tradesList = new LinkedList<>();
        Order sellSideTopOrder = sellSideOrders.peek();
        if(sellSideTopOrder != null && sellSideTopOrder.getPrice() <= order.getPrice()){
            Order restingOrder = sellSideOrders.poll();
            Integer restingOrderQuantity = restingOrder.getQuantity();
            Integer quantityToFill = order.getQuantity();
            if(restingOrderQuantity > quantityToFill){
                Integer remainingQuantityInRestingOrder = restingOrderQuantity - quantityToFill;
                Order remainingQuantityRestingOrder = new Order(restingOrder.getOrderId(), restingOrder.getSide().name(), restingOrder.getPrice(), remainingQuantityInRestingOrder);
                sellSideOrders.add(remainingQuantityRestingOrder);
                Trade executedTrade = new Trade(order.getOrderId(), restingOrder.getOrderId(), restingOrder.getPrice(), quantityToFill);
                tradesList.add(executedTrade);
                return tradesList;
            } else if (restingOrderQuantity < quantityToFill){
                Integer remainingQuantityInAggressOrder = quantityToFill - restingOrderQuantity;
                Order remainingQuantityAggressOrder = new Order(order.getOrderId(), order.getSide().name(), order.getPrice(), remainingQuantityInAggressOrder);
                Trade executedTrade = new Trade(order.getOrderId(), restingOrder.getOrderId(), restingOrder.getPrice(), restingOrderQuantity);
                tradesList.add(executedTrade);
                tradesList.addAll(executeBuyOrder(remainingQuantityAggressOrder));
                return tradesList;
            } else {
                Trade executedTrade = new Trade(order.getOrderId(), restingOrder.getOrderId(), restingOrder.getPrice(), order.getQuantity());
                tradesList.add(executedTrade);
                return tradesList;
            }
        } else {
            buySideOrders.add(order);
            return tradesList;
        }
    }

    private List<Trade> executeSellOrder(Order order){
        List<Trade> tradesList = new LinkedList<>();
        Order buySideTopOrder = buySideOrders.peek();
        if(buySideTopOrder != null && buySideTopOrder.getPrice() >= order.getPrice()){
            Order restingOrder = buySideOrders.poll();
            Integer restingOrderQuantity = restingOrder.getQuantity();
            Integer quantityToFill = order.getQuantity();
            if(restingOrderQuantity > quantityToFill){
                Integer remainingQuantityInRestingOrder = restingOrderQuantity - quantityToFill;
                Order remainingQuantityRestingOrder = new Order(restingOrder.getOrderId(), restingOrder.getSide().name(), restingOrder.getPrice(), remainingQuantityInRestingOrder);
                buySideOrders.add(remainingQuantityRestingOrder);
                Trade executedTrade = new Trade(order.getOrderId(), restingOrder.getOrderId(), restingOrder.getPrice(), quantityToFill);
                tradesList.add(executedTrade);
                return tradesList;
            } else if (restingOrderQuantity < quantityToFill){
                Integer remainingQuantityInAggressOrder = quantityToFill - restingOrderQuantity;
                Order remainingQuantityAggressOrder = new Order(order.getOrderId(), order.getSide().name(), order.getPrice(), remainingQuantityInAggressOrder);
                Trade executedTrade = new Trade(order.getOrderId(), restingOrder.getOrderId(), restingOrder.getPrice(), restingOrderQuantity);
                tradesList.add(executedTrade);
                tradesList.addAll(executeSellOrder(remainingQuantityAggressOrder));
                return tradesList;
            } else {
                Trade executedTrade = new Trade(order.getOrderId(), restingOrder.getOrderId(), restingOrder.getPrice(), order.getQuantity());
                tradesList.add(executedTrade);
                return tradesList;
            }
        } else {
            sellSideOrders.add(order);
            return tradesList;
        }
    }

    public static MatchingEngine getMatchingEngine(){
        return MATCHING_ENGINE;
    }

    public static void clearMatchingEngine(){
        MATCHING_ENGINE.sellSideOrders.clear();
        MATCHING_ENGINE.buySideOrders.clear();
    }

}
