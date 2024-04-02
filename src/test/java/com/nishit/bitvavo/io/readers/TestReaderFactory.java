package com.nishit.bitvavo.io.readers;

import com.nishit.bitvavo.beans.IOMode;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class TestReaderFactory {

    @Test
    public void testReaderFactory_returnStandardReader(){
        Reader reader = ReaderFactory.getReader(IOMode.STANDARD);
        assertTrue(reader instanceof StandardReader);
    }

    @Test (expected = IllegalStateException.class)
    public void testReaderFactory_otherMode(){
        ReaderFactory.getReader(IOMode.FILE);
    }
}
