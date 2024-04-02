package com.nishit.bitvavo.io.readers;

import com.nishit.bitvavo.beans.IOMode;

public class ReaderFactory {
    public static Reader getReader(IOMode inputMode){
        switch (inputMode){
            case STANDARD:
                return new StandardReader();
            default:
                throw new IllegalStateException("No other mode is supported currently");
        }
    }
}
