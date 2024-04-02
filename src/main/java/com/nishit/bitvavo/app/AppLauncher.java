package com.nishit.bitvavo.app;

import com.nishit.bitvavo.beans.IOMode;
import com.nishit.bitvavo.beans.Order;
import com.nishit.bitvavo.beans.OrderBook;
import com.nishit.bitvavo.beans.Trade;
import com.nishit.bitvavo.io.readers.Reader;
import com.nishit.bitvavo.io.readers.ReaderFactory;
import com.nishit.bitvavo.io.writers.Writer;
import com.nishit.bitvavo.io.writers.WriterFactory;
import com.nishit.bitvavo.matcher.MatchingEngine;
import com.nishit.bitvavo.parser.CSVParser;

import java.util.List;
import java.util.Optional;

public class AppLauncher {

    public static void main(String[] args) {
        Reader inputReader = ReaderFactory.getReader(IOMode.STANDARD);
        Writer outputWriter = WriterFactory.getWriter(IOMode.STANDARD);
        MatchingEngine matchingEngine = MatchingEngine.getMatchingEngine();
        while(true){
            Optional<String> input = inputReader.nextLine();
            if(input.isPresent()){
                Order incomingOrder = CSVParser.parseCSVInput(input.get());
                List<Trade> tradeList = matchingEngine.addAndExecuteOrder(incomingOrder);
                outputWriter.writeTrades(tradeList);
            } else {
                break;
            }
        }
        OrderBook orderBook = matchingEngine.getOrderBook();
        outputWriter.writeOrderBook(orderBook);
    }
}
