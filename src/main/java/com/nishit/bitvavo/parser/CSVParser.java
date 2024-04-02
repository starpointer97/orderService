package com.nishit.bitvavo.parser;

import com.nishit.bitvavo.beans.Order;

public class CSVParser {

    public static Order parseCSVInput(String input){
        String[] inputArray = input.split(",");
        if(validateOrderInputSize(inputArray)){
            return createOrder(inputArray);
        } else {
            throw new IllegalArgumentException("Number of fields in the input do not match number of fields in the constructor");
        }
    }

    private static boolean validateOrderInputSize(String[] inputArray){
        return (inputArray.length == Order.NUMBER_OF_FIELDS_IN_THIS_CLASS - 1);
    }

    private static Order createOrder(final String[] inputArray){
        return new Order(inputArray[0], inputArray[1], Integer.parseInt(inputArray[2]), Integer.parseInt(inputArray[3]));
    }
}
