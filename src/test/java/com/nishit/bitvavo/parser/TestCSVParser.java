package com.nishit.bitvavo.parser;

import com.nishit.bitvavo.beans.BuyOrSell;
import com.nishit.bitvavo.beans.Order;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestCSVParser {

    @Test
    public void testParseInput_ValidatedSuccessfully(){
        String input = "10000,B,98,25500";
        Order actualOrder = CSVParser.parseCSVInput(input);
        Order expectedOrder = new Order("10000", BuyOrSell.B.name(),98, 25500);
        assertNotNull(actualOrder);
        assertEquals(expectedOrder, actualOrder);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInput_ValidatedUnSuccessfully(){
        String input = "10000,B,98,25500,XYZ";
        CSVParser.parseCSVInput(input);
    }
}
