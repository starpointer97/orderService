package com.nishit.bitvavo.io.writers;

import com.nishit.bitvavo.beans.OrderBook;

public interface Writer {

    public void writeOrderBook(OrderBook orderBook);
}
