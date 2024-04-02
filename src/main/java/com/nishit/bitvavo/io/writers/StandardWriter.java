package com.nishit.bitvavo.io.writers;

import com.nishit.bitvavo.beans.OrderBook;
import com.nishit.bitvavo.beans.Trade;

import java.util.List;

public class StandardWriter implements Writer {

    @Override
    public void writeOrderBook(OrderBook orderBook) {
        //TODO : Implement after getting clarity from Bitvavo on output format.
    }

    @Override
    public void writeTrades(List<Trade> tradeList) {
        for(Trade trade : tradeList){
            System.out.println(trade);
        }

    }
}
