package com.nishit.bitvavo.app;

import com.nishit.bitvavo.beans.IOMode;
import com.nishit.bitvavo.beans.Order;
import com.nishit.bitvavo.io.readers.Reader;
import com.nishit.bitvavo.io.readers.ReaderFactory;
import com.nishit.bitvavo.io.writers.Writer;
import com.nishit.bitvavo.io.writers.WriterFactory;
import com.nishit.bitvavo.parser.CSVParser;

import java.util.Optional;

public class AppLauncher {

    public static void main(String[] args) {
        Reader inputReader = ReaderFactory.getReader(IOMode.STANDARD);
        while(true){
            Optional<String> input = inputReader.nextLine();
            if(input.isPresent()){
                Order incomingOrder = CSVParser.parseCSVInput(input.get());

            } else {
                break;
            }
        }
        Writer outputWriter = WriterFactory.getWriter(IOMode.STANDARD);



    }
}
