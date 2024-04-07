package com.nishit.bitvavo.io.writers;

import com.nishit.bitvavo.beans.OrderBook;
import com.nishit.bitvavo.beans.Trade;

import java.util.List;

import static com.nishit.bitvavo.constants.Constants.NEW_LINE;


public class StandardWriter implements Writer {

    @Override
    public String writeOutOrderBook(OrderBook orderBook) {
        return orderBook.toString();
    }

    @Override
    public String writeTrades(List<Trade> tradeList) {
        StringBuilder tradesOutputBuilder = new StringBuilder();
        for(Trade trade : tradeList){
            tradesOutputBuilder.append(trade.toString()).append(NEW_LINE);
        }
        return tradesOutputBuilder.toString();
    }
}
