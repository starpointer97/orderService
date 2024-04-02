package com.nishit.bitvavo.beans;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TestTrade {

    @Test
    public void testTrade_ToString() {
        Trade trade = new Trade("1234", "5678", 100, 1000);
        String tradeString = trade.toString();
        String expected = "trade 1234,5678,100,1000";
        assertNotNull(tradeString);
        assertEquals(expected, tradeString);
    }
}
