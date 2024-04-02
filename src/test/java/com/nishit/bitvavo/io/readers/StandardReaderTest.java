package com.nishit.bitvavo.io.readers;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class StandardReaderTest {

    @Test
    public void testNextLine_OneLineInput_OneCall() {
        String testInput = "10000,B,98,25500";
        InputStream inputStream = new ByteArrayInputStream(testInput.getBytes());

        System.setIn(inputStream);

        StandardReader reader = new StandardReader();

        String actual = reader.nextLine().get();
        assertEquals(testInput, actual);
    }

    @Test
    public void testNextLine_OneLineInput_TwoCalls() {
        String testInput = "10000,B,98,25500";
        InputStream inputStream = new ByteArrayInputStream(testInput.getBytes());

        System.setIn(inputStream);

        StandardReader reader = new StandardReader();

        String firstCall = reader.nextLine().get();
        assertEquals(testInput, firstCall);

        Optional<String> secondCall = reader.nextLine();
        assertFalse(secondCall.isPresent());
    }

    @Test
    public void testNextLine_MultipleLines() {
        String testInput = "10000,B,98,25500\n10005,S,105,20000\n";
        String expectedFirstPart = "10000,B,98,25500";
        String expectedSecondPart = "10005,S,105,20000";
        InputStream inputStream = new ByteArrayInputStream(testInput.getBytes());

        System.setIn(inputStream);

        StandardReader reader = new StandardReader();

        String actualFirstPart = reader.nextLine().get();
        String actualSecondPart = reader.nextLine().get();
        assertEquals(expectedFirstPart, actualFirstPart);
        assertEquals(expectedSecondPart, actualSecondPart);
    }
}
