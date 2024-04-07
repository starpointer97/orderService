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
        return (inputArray.length == Order.NUMBER_OF_MEMBER_FIELDS_IN_INPUT);
    }

    private static Order createOrder(final String[] inputArray){
        return new Order(inputArray[0], inputArray[1], inputArray[2], inputArray[3]);
    }
}
