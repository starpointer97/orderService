package com.nishit.bitvavo.io.writers;

import com.nishit.bitvavo.beans.OrderBook;
import com.nishit.bitvavo.beans.Trade;

import java.util.List;

public interface Writer {

    String writeOutOrderBook(OrderBook orderBook);

    String writeTrades(List<Trade> tradeList);
}
