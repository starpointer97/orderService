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

    private Order createOrder(Order order, Integer remainingQuantity){
        return Order.builder().orderId(order.getOrderId())
            .side(order.getSide())
            .price(order.getPrice())
            .quantity(remainingQuantity)
            .build();
    }

    private Trade createTrade(Order order, Order restingOrder, Integer quantityToFill){
        return Trade.builder().aggressOrderId(order.getOrderId())
            .restingOrderId(restingOrder.getOrderId())
            .price(restingOrder.getPrice())
            .quantity(quantityToFill)
            .build();
    }

    /**
     * For buy side order, validate if sell side has a resting order with price <= orderPrice
     * For sell side order, validate if sell side has a resting order with price >= orderPrice
     */
    private List<Trade> executeOrder(Order order, PriorityQueue<Order> mySideOrderBook, PriorityQueue<Order> oppositeSideOrderBook){
        List<Trade> tradesList = new LinkedList<>();
        Order oppositeSideTopOrder = oppositeSideOrderBook.peek();
        if(oppositeSideTopOrder != null && isPriceValid(order, oppositeSideTopOrder)){
            Order restingOrder = oppositeSideOrderBook.poll();
            Integer quantityAvailableInRestingOrder = restingOrder.getQuantity();
            Integer quantityToFillInIncomingOrder = order.getQuantity();
            if(quantityAvailableInRestingOrder > quantityToFillInIncomingOrder){
                Integer remainingQuantityInRestingOrder = quantityAvailableInRestingOrder - quantityToFillInIncomingOrder;
                Order remainingQuantityRestingOrder = createOrder(restingOrder, remainingQuantityInRestingOrder);

                oppositeSideOrderBook.add(remainingQuantityRestingOrder);
                Trade executedTrade = createTrade(order, restingOrder, quantityToFillInIncomingOrder);
                tradesList.add(executedTrade);
                return tradesList;
            } else if (quantityAvailableInRestingOrder < quantityToFillInIncomingOrder){
                Integer remainingQuantityInAggressOrder = quantityToFillInIncomingOrder - quantityAvailableInRestingOrder;
                Order remainingQuantityAggressOrder = createOrder(order, remainingQuantityInAggressOrder);

                Trade executedTrade = createTrade(order, restingOrder, quantityAvailableInRestingOrder);
                tradesList.add(executedTrade);
                tradesList.addAll(executeOrder(remainingQuantityAggressOrder, mySideOrderBook, oppositeSideOrderBook));
                return tradesList;
            } else {
                Trade executedTrade = createTrade(order, restingOrder, quantityToFillInIncomingOrder);
                tradesList.add(executedTrade);
                return tradesList;
            }
        } else {
            mySideOrderBook.add(order);
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
