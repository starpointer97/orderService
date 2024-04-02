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
            return executeOrder(order, buySideOrders, sellSideOrders);
        } else {
            return executeOrder(order, sellSideOrders, buySideOrders);
        }
    }

    private boolean isPriceValid(Order myOrder, Order oppositeOrder){
        switch (myOrder.getSide()){
            case B:
                return (oppositeOrder.getPrice() <= myOrder.getPrice());
            case S:
                return oppositeOrder.getPrice() >= myOrder.getPrice();
            default:
                throw new IllegalStateException("Order Side can either be B(Buy) or S(Sell)");
        }
    }

    /**
     * For buy side order, validate if sell side has a resting order with price <= orderPrice
     * For sell side order, validate if sell side has a resting order with price >= orderPrice
     * @param order
     */
    private List<Trade> executeOrder(Order order, PriorityQueue<Order> mySide, PriorityQueue<Order> oppositeSide){
        List<Trade> tradesList = new LinkedList<>();
        Order oppositeSideTopOrder = oppositeSide.peek();
        if(oppositeSideTopOrder != null && isPriceValid(order, oppositeSideTopOrder)){
            Order restingOrder = oppositeSide.poll();
            Integer restingOrderQuantity = restingOrder.getQuantity();
            Integer quantityToFill = order.getQuantity();
            if(restingOrderQuantity > quantityToFill){
                Integer remainingQuantityInRestingOrder = restingOrderQuantity - quantityToFill;
                Order remainingQuantityRestingOrder = new Order(restingOrder.getOrderId(), restingOrder.getSide().name(), restingOrder.getPrice(), remainingQuantityInRestingOrder);
                oppositeSide.add(remainingQuantityRestingOrder);
                Trade executedTrade = new Trade(order.getOrderId(), restingOrder.getOrderId(), restingOrder.getPrice(), quantityToFill);
                tradesList.add(executedTrade);
                return tradesList;
            } else if (restingOrderQuantity < quantityToFill){
                Integer remainingQuantityInAggressOrder = quantityToFill - restingOrderQuantity;
                Order remainingQuantityAggressOrder = new Order(order.getOrderId(), order.getSide().name(), order.getPrice(), remainingQuantityInAggressOrder);
                Trade executedTrade = new Trade(order.getOrderId(), restingOrder.getOrderId(), restingOrder.getPrice(), restingOrderQuantity);
                tradesList.add(executedTrade);
                tradesList.addAll(executeOrder(remainingQuantityAggressOrder, mySide, oppositeSide));
                return tradesList;
            } else {
                Trade executedTrade = new Trade(order.getOrderId(), restingOrder.getOrderId(), restingOrder.getPrice(), order.getQuantity());
                tradesList.add(executedTrade);
                return tradesList;
            }
        } else {
            mySide.add(order);
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
