package com.nishit.bitvavo.io.writers;

import com.nishit.bitvavo.beans.OrderBook;
import com.nishit.bitvavo.beans.Trade;

import java.util.List;

public interface Writer {

    public void writeOrderBook(OrderBook orderBook);

    public void writeTrades(List<Trade> tradeList);
}
