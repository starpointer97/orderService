package com.nishit.bitvavo.matcher;

import com.nishit.bitvavo.beans.Order;
import com.nishit.bitvavo.beans.OrderBook;
import com.nishit.bitvavo.beans.Trade;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class TestMatchingEngine {

    private MatchingEngine matchingEngine = MatchingEngine.getMatchingEngine();

    @Before
    public void initialize(){
        MatchingEngine.clearMatchingEngine();
    }
    @Test
    public void testMatchingEngine_MultipleBuyOrdersForSamePrice_MaintainOrdering() {
        Order order1 = new Order("1235", "B", 100, 1000);
        Order order2 = new Order("1234", "B", 100, 1000);
        matchingEngine.addAndExecuteOrder(order1);
        matchingEngine.addAndExecuteOrder(order2);
        assertEquals(2, matchingEngine.buySideOrders.size());
        assertEquals(0, matchingEngine.sellSideOrders.size());
        Order actualOrder1 = matchingEngine.buySideOrders.poll();
        Order actualOrder2 = matchingEngine.buySideOrders.poll();

        assertNotNull(actualOrder1);
        assertEquals(order1.getOrderId(), actualOrder1.getOrderId());

        assertNotNull(actualOrder2);
        assertEquals(order2.getOrderId(), actualOrder2.getOrderId());
    }

    @Test
    public void testMatchingEngine_MultipleSellOrdersForSamePrice_MaintainOrdering() {
        Order order1 = new Order("1235", "S", 100, 1000);
        Order order2 = new Order("1234", "S", 100, 1000);
        matchingEngine.addAndExecuteOrder(order1);
        matchingEngine.addAndExecuteOrder(order2);
        assertEquals(0, matchingEngine.buySideOrders.size());
        assertEquals(2, matchingEngine.sellSideOrders.size());
        Order actualOrder1 = matchingEngine.sellSideOrders.poll();
        Order actualOrder2 = matchingEngine.sellSideOrders.poll();

        assertNotNull(actualOrder1);
        assertEquals(order1.getOrderId(), actualOrder1.getOrderId());

        assertNotNull(actualOrder2);
        assertEquals(order2.getOrderId(), actualOrder2.getOrderId());
    }

    @Test
    public void testMatchingEngine_NoOrderPlaced(){
        OrderBook orderBook = matchingEngine.getOrderBook();

        assertNotNull(orderBook);
        assertEquals(0, orderBook.getBuySideOrders().size());
        assertEquals(0, orderBook.getSellSideOrders().size());
    }

    @Test
    public void testMatchingEngine_addOneBuyOrder_Successful(){
        Order order = new Order("1234", "B", 100, 1000);
        List<Trade> tradeList = matchingEngine.addAndExecuteOrder(order);
        OrderBook orderBook = matchingEngine.getOrderBook();

        assertEquals(0, tradeList.size());
        assertEquals(0, orderBook.getSellSideOrders().size());
        assertEquals(1, orderBook.getBuySideOrders().size());
    }

    @Test
    public void testMatchingEngine_addOneSellOrder_Successful(){
        Order order = new Order("1234", "S", 100, 1000);
        List<Trade> tradeList = matchingEngine.addAndExecuteOrder(order);
        OrderBook orderBook = matchingEngine.getOrderBook();

        assertEquals(0, tradeList.size());
        assertEquals(1, orderBook.getSellSideOrders().size());
        assertEquals(0, orderBook.getBuySideOrders().size());
    }

    @Test
    public void testMatchingEngine_addOneBuyOrderOneSellOrder_NoMatch(){
        Order buyOrder = new Order("1", "B", 95, 1000);
        Order sellOrder = new Order("2", "S", 100, 1000);
        List<Trade> tradeListAfterBuy = matchingEngine.addAndExecuteOrder(buyOrder);
        OrderBook orderBook = matchingEngine.getOrderBook();

        assertEquals(0, tradeListAfterBuy.size());
        assertEquals(0, orderBook.getSellSideOrders().size());
        assertEquals(1, orderBook.getBuySideOrders().size());

        List<Trade> tradeListAfterSell = matchingEngine.addAndExecuteOrder(sellOrder);

        assertEquals(0, tradeListAfterSell.size());
        assertEquals(1, orderBook.getSellSideOrders().size());
        assertEquals(1, orderBook.getBuySideOrders().size());
    }

    @Test
    public void testMatchingEngine_addOneBuyOrderOneSellOrder_Match(){
        Order buyOrder = new Order("1", "B", 100, 1000);
        Order sellOrder = new Order("2", "S", 100, 1000);
        List<Trade> tradeListAfterBuy = matchingEngine.addAndExecuteOrder(buyOrder);
        OrderBook orderBook = matchingEngine.getOrderBook();

        assertEquals(0, tradeListAfterBuy.size());
        assertEquals(0, orderBook.getSellSideOrders().size());
        assertEquals(1, orderBook.getBuySideOrders().size());

        List<Trade> tradeListAfterSell = matchingEngine.addAndExecuteOrder(sellOrder);

        assertEquals(1, tradeListAfterSell.size());
        assertEquals(0, orderBook.getSellSideOrders().size());
        assertEquals(0, orderBook.getBuySideOrders().size());

        Trade trade = tradeListAfterSell.get(0);
        assertNotNull(trade);
        assertEquals("2", trade.getAggressOrderId());
        assertEquals("1", trade.getRestingOrderId());
        assertEquals(Integer.valueOf(100), trade.getPrice());
        assertEquals(Integer.valueOf(1000), trade.getQuantity());
    }

    @Test
    public void testMatchingEngine_addMultipleBuyOrderOneSellOrder_Match(){
        Order order1 = new Order("1", "B", 100, 1000);
        Order order2 = new Order("2", "B", 101, 500);
        Order order3 = new Order("3", "S", 100, 1800);

        List<Trade> tradeListAfterBuy1 = matchingEngine.addAndExecuteOrder(order1);
        OrderBook orderBook1 = matchingEngine.getOrderBook();

        assertEquals(0, tradeListAfterBuy1.size());
        assertEquals(0, orderBook1.getSellSideOrders().size());
        assertEquals(1, orderBook1.getBuySideOrders().size());

        List<Trade> tradeListAfterBuy2 = matchingEngine.addAndExecuteOrder(order2);
        OrderBook orderBook2 = matchingEngine.getOrderBook();

        assertEquals(0, tradeListAfterBuy2.size());
        assertEquals(0, orderBook2.getSellSideOrders().size());
        assertEquals(2, orderBook2.getBuySideOrders().size());

        List<Trade> tradeListAfterSell1 = matchingEngine.addAndExecuteOrder(order3);
        OrderBook orderBook3 = matchingEngine.getOrderBook();
        assertEquals(2, tradeListAfterSell1.size());
        Trade trade1 = tradeListAfterSell1.get(0);
        assertNotNull(trade1);

        assertEquals("3", trade1.getAggressOrderId());
        assertEquals("2", trade1.getRestingOrderId());
        assertEquals(Integer.valueOf(101), trade1.getPrice());
        assertEquals(Integer.valueOf(500), trade1.getQuantity());

        Trade trade2 = tradeListAfterSell1.get(1);
        assertEquals("3", trade2.getAggressOrderId());
        assertEquals("1", trade2.getRestingOrderId());
        assertEquals(Integer.valueOf(100), trade2.getPrice());
        assertEquals(Integer.valueOf(1000), trade2.getQuantity());

        assertEquals(0, orderBook3.getBuySideOrders().size());
        assertEquals(1, orderBook3.getSellSideOrders().size());
        assertEquals(Integer.valueOf(300), orderBook3.getSellSideOrders().peek().getQuantity());
    }

    @Test
    public void testMatchingEngine_Input1(){
        Order order1 = new Order("10000", "B", 98, 25500);
        Order order2 = new Order("10005", "S", 105, 20000);
        Order order3 = new Order("10001", "S", 100, 500);
        Order order4 = new Order("10002", "S", 100, 10000);
        Order order5 = new Order("10003", "B", 99, 50000);
        Order order6 = new Order("10004", "S", 103, 100);

        List<Trade> tradeList1 = matchingEngine.addAndExecuteOrder(order1);
        OrderBook orderBook1 = matchingEngine.getOrderBook();

        assertEquals(0, tradeList1.size());
        assertEquals(1, orderBook1.getBuySideOrders().size());
        assertEquals(0, orderBook1.getSellSideOrders().size());

        List<Trade> tradeList2 = matchingEngine.addAndExecuteOrder(order2);
        OrderBook orderBook2 = matchingEngine.getOrderBook();

        assertEquals(0, tradeList2.size());
        assertEquals(1, orderBook2.getBuySideOrders().size());
        assertEquals(1, orderBook2.getSellSideOrders().size());

        List<Trade> tradeList3 = matchingEngine.addAndExecuteOrder(order3);
        OrderBook orderBook3 = matchingEngine.getOrderBook();

        assertEquals(0, tradeList3.size());
        assertEquals(1, orderBook3.getBuySideOrders().size());
        assertEquals(2, orderBook3.getSellSideOrders().size());

        List<Trade> tradeList4 = matchingEngine.addAndExecuteOrder(order4);
        OrderBook orderBook4 = matchingEngine.getOrderBook();

        assertEquals(0, tradeList4.size());
        assertEquals(1, orderBook4.getBuySideOrders().size());
        assertEquals(3, orderBook4.getSellSideOrders().size());

        List<Trade> tradeList5 = matchingEngine.addAndExecuteOrder(order5);
        OrderBook orderBook5 = matchingEngine.getOrderBook();

        assertEquals(0, tradeList5.size());
        assertEquals(2, orderBook5.getBuySideOrders().size());
        assertEquals(3, orderBook5.getSellSideOrders().size());

        List<Trade> tradeList6 = matchingEngine.addAndExecuteOrder(order6);
        OrderBook orderBook6 = matchingEngine.getOrderBook();

        assertEquals(0, tradeList6.size());
        assertEquals(2, orderBook6.getBuySideOrders().size());
        assertEquals(4, orderBook6.getSellSideOrders().size());
    }

    @Test
    public void testMatchingEngine_Input2(){
        Order order1 = new Order("10000", "B", 98, 25500);
        Order order2 = new Order("10005", "S", 105, 20000);
        Order order3 = new Order("10001", "S", 100, 500);
        Order order4 = new Order("10002", "S", 100, 10000);
        Order order5 = new Order("10003", "B", 99, 50000);
        Order order6 = new Order("10004", "S", 103, 100);
        Order order7 = new Order("10006", "B", 105, 16000);

        List<Trade> tradeList1 = matchingEngine.addAndExecuteOrder(order1);
        OrderBook orderBook1 = matchingEngine.getOrderBook();

        assertEquals(0, tradeList1.size());
        assertEquals(1, orderBook1.getBuySideOrders().size());
        assertEquals(0, orderBook1.getSellSideOrders().size());

        List<Trade> tradeList2 = matchingEngine.addAndExecuteOrder(order2);
        OrderBook orderBook2 = matchingEngine.getOrderBook();

        assertEquals(0, tradeList2.size());
        assertEquals(1, orderBook2.getBuySideOrders().size());
        assertEquals(1, orderBook2.getSellSideOrders().size());

        List<Trade> tradeList3 = matchingEngine.addAndExecuteOrder(order3);
        OrderBook orderBook3 = matchingEngine.getOrderBook();

        assertEquals(0, tradeList3.size());
        assertEquals(1, orderBook3.getBuySideOrders().size());
        assertEquals(2, orderBook3.getSellSideOrders().size());

        List<Trade> tradeList4 = matchingEngine.addAndExecuteOrder(order4);
        OrderBook orderBook4 = matchingEngine.getOrderBook();

        assertEquals(0, tradeList4.size());
        assertEquals(1, orderBook4.getBuySideOrders().size());
        assertEquals(3, orderBook4.getSellSideOrders().size());

        List<Trade> tradeList5 = matchingEngine.addAndExecuteOrder(order5);
        OrderBook orderBook5 = matchingEngine.getOrderBook();

        assertEquals(0, tradeList5.size());
        assertEquals(2, orderBook5.getBuySideOrders().size());
        assertEquals(3, orderBook5.getSellSideOrders().size());

        List<Trade> tradeList6 = matchingEngine.addAndExecuteOrder(order6);
        OrderBook orderBook6 = matchingEngine.getOrderBook();

        assertEquals(0, tradeList6.size());
        assertEquals(2, orderBook6.getBuySideOrders().size());
        assertEquals(4, orderBook6.getSellSideOrders().size());

        List<Trade> tradeList7 = matchingEngine.addAndExecuteOrder(order7);
        OrderBook orderBook7 = matchingEngine.getOrderBook();

        assertEquals(4, tradeList7.size());
        Trade trade1 = tradeList7.get(0);
        Trade trade2 = tradeList7.get(1);
        Trade trade3 = tradeList7.get(2);
        Trade trade4 = tradeList7.get(3);

        assertNotNull(trade1);
        assertNotNull(trade2);
        assertNotNull(trade3);
        assertNotNull(trade4);

        assertEquals("trade 10006,10001,100,500", trade1.toString());
        assertEquals("trade 10006,10002,100,10000", trade2.toString());
        assertEquals("trade 10006,10004,103,100", trade3.toString());
        assertEquals("trade 10006,10005,105,5400", trade4.toString());
        assertEquals(2, orderBook7.getBuySideOrders().size());
        assertEquals(1, orderBook7.getSellSideOrders().size());

        Order sellSideRemainingOrder = orderBook7.getSellSideOrders().peek();
        assertNotNull(sellSideRemainingOrder);
        assertEquals(Integer.valueOf(105), sellSideRemainingOrder.getPrice());
        assertEquals(Integer.valueOf(14600), sellSideRemainingOrder.getQuantity());


    }
}
