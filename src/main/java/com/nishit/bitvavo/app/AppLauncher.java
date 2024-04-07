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
import com.nishit.bitvavo.utils.MD5Parser;

import java.util.List;
import java.util.Optional;

public class AppLauncher {
    public static void main(String[] args) {
        Reader inputReader = ReaderFactory.getReader(IOMode.STANDARD);
        Writer outputWriter = WriterFactory.getWriter(IOMode.STANDARD);
        MatchingEngine matchingEngine = MatchingEngine.getMatchingEngine();
        StringBuilder outputBuilder = new StringBuilder();
        while (true) {
            Optional<String> input = inputReader.nextLine();
            if (input.isPresent()) {
                Order incomingOrder = CSVParser.parseCSVInput(input.get());
                List<Trade> tradeList = matchingEngine.addAndExecuteOrder(incomingOrder);
                String tradesForThisAggressingOrder = outputWriter.writeTrades(tradeList);
                outputBuilder.append(tradesForThisAggressingOrder);
                System.out.print(tradesForThisAggressingOrder);
            }
            else {
                break;
            }
        }
        inputReader.closeStream();
        OrderBook orderBook = matchingEngine.getOrderBook();
        String orderBookAfterAllTrades = outputWriter.writeOutOrderBook(orderBook);
        outputBuilder.append(orderBookAfterAllTrades);
        System.out.println(orderBookAfterAllTrades);

        System.out.println(MD5Parser.getMD5Hash(outputBuilder.toString()));
    }
}
