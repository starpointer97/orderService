package com.nishit.bitvavo.io.writers;

import com.nishit.bitvavo.beans.Order;
import com.nishit.bitvavo.beans.OrderBook;
import com.nishit.bitvavo.matcher.MatchingEngine;
import com.nishit.bitvavo.utils.MD5Parser;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.PriorityQueue;

import static org.junit.Assert.assertEquals;

public class TestStandardWriter {

    StandardWriter writer = new StandardWriter();
    MatchingEngine matchingEngine = MatchingEngine.getMatchingEngine();

    Order order1 = new Order("10000", "B", "98", "25500");
    Order order2 = new Order("10005", "S", "105", "20000");
    Order order3 = new Order("10001", "S", "100", "500");
    Order order4 = new Order("10002", "S", "100", "10000");
    Order order5 = new Order("10003", "B", "99", "50000");
    Order order6 = new Order("10004", "S", "103", "100");
    Order order7 = new Order("10006", "B", "105", "16000");

    @Before
    public void setup(){
        MatchingEngine.clearMatchingEngine();
    }

    @Test
    public void testWriteToStandardOutput_NoTrades_OrderBookEmpty(){
        String tradesOutput = writer.writeTrades(new LinkedList<>());
        assertEquals("", tradesOutput);

        String orderBookOutput = writer.writeOutOrderBook(new OrderBook(new PriorityQueue<>(), new PriorityQueue<>()));
        assertEquals("", orderBookOutput);
    }

    @Test
    public void testWriteToStandardOutput_NoTrades_OrderBookPresent(){
        matchingEngine.addAndExecuteOrder(order1);
        matchingEngine.addAndExecuteOrder(order2);
        matchingEngine.addAndExecuteOrder(order3);
        matchingEngine.addAndExecuteOrder(order4);
        matchingEngine.addAndExecuteOrder(order5);
        matchingEngine.addAndExecuteOrder(order6);

        String md5HashExpected = "8ff13aad3e61429bfb5ce0857e846567";
        assertEquals(md5HashExpected, MD5Parser.getMD5Hash(writer.writeOutOrderBook(matchingEngine.getOrderBook())));
    }

    @Test
    public void testWriteToStandardOutput_TradesExecuted_OrderBookPresent() {

        String md5HashExpected = "ce8e7e5ab26ab5a7db6b7d30759cf02e";
        StringBuilder outputBuilder = new StringBuilder();

        outputBuilder.append(writer.writeTrades(matchingEngine.addAndExecuteOrder(order1)));
        outputBuilder.append(writer.writeTrades(matchingEngine.addAndExecuteOrder(order2)));
        outputBuilder.append(writer.writeTrades(matchingEngine.addAndExecuteOrder(order3)));
        outputBuilder.append(writer.writeTrades(matchingEngine.addAndExecuteOrder(order4)));
        outputBuilder.append(writer.writeTrades(matchingEngine.addAndExecuteOrder(order5)));
        outputBuilder.append(writer.writeTrades(matchingEngine.addAndExecuteOrder(order6)));
        outputBuilder.append(writer.writeTrades(matchingEngine.addAndExecuteOrder(order7)));

        outputBuilder.append(writer.writeOutOrderBook(matchingEngine.getOrderBook()));
        assertEquals(md5HashExpected, MD5Parser.getMD5Hash(outputBuilder.toString()));
    }

    @Test
    public void testWriteToStandardOutput_TradesExecuted_OrderBookEmpty() {
        StringBuilder outputBuilder = new StringBuilder();
        String md5HashExpected = "67a38bed9a12b07b8d1990b15702fa1c";


        matchingEngine.addAndExecuteOrder(order7);

        outputBuilder.append(writer.writeTrades(matchingEngine.addAndExecuteOrder(order2)));
        outputBuilder.append(writer.writeTrades(matchingEngine.addAndExecuteOrder(order7)));

        outputBuilder.append(writer.writeOutOrderBook(matchingEngine.getOrderBook()));

        assertEquals(md5HashExpected, MD5Parser.getMD5Hash(outputBuilder.toString()));
    }
}
