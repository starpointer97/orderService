package com.nishit.bitvavo.io.writers;

import com.nishit.bitvavo.beans.IOMode;

public class WriterFactory {
    public static Writer getWriter(IOMode outputMode){
        switch (outputMode){
            case STANDARD:
                return new StandardWriter();
            default:
                throw new IllegalStateException("No other mode is supported currently");
        }
    }
}
