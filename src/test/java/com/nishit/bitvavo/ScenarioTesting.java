package com.nishit.bitvavo;

import com.nishit.bitvavo.beans.Order;
import com.nishit.bitvavo.beans.OrderBook;
import com.nishit.bitvavo.beans.Trade;
import com.nishit.bitvavo.matcher.MatchingEngine;
import com.nishit.bitvavo.utils.MD5Parser;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ScenarioTesting {



    private MatchingEngine matchingEngine = MatchingEngine.getMatchingEngine();

    @Test
    public void test_Input1(){
        Order order1 = new Order("10000", "B", "98", "25500");
        Order order2 = new Order("10005", "S", "105", "20000");
        Order order3 = new Order("10001", "S", "100", "500");
        Order order4 = new Order("10002", "S", "100", "10000");
        Order order5 = new Order("10003", "B", "99", "50000");
        Order order6 = new Order("10004", "S", "103", "100");

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

        String orderBook = matchingEngine.getOrderBook().toString();
        String md5Output = MD5Parser.getMD5Hash(orderBook);
        String expectedMD5Output = "8ff13aad3e61429bfb5ce0857e846567";
        assertEquals(expectedMD5Output, md5Output);
    }

    @Test
    public void test_Input2(){

    }

}
