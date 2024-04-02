package com.nishit.bitvavo.io.writers;

import com.nishit.bitvavo.beans.IOMode;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class WriterFactoryTest {

    @Test
    public void testWriterFactory_returnStandardWriter(){
        Writer writer = WriterFactory.getWriter(IOMode.STANDARD);
        assertTrue(writer instanceof StandardWriter);
    }

    @Test (expected = IllegalStateException.class)
    public void testWriterFactory_otherMode(){
        WriterFactory.getWriter(IOMode.FILE);
    }
}
