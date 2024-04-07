package com.nishit.bitvavo.utils;

public class OrderValidator {

    public static boolean isOrderParamsValid(String orderId, String side, String price, String quantity){
        return validateOrderId(orderId) && validateSide(side) && validatePrice(price) && validateQuantity(quantity);
    }

    /*
   Making sure that the orderId is a valid numeric.
    */
    private static boolean validateOrderId(String orderId){
        try{
            Long.parseLong(orderId);
            return true;
        } catch(NumberFormatException nfe){
            throw new IllegalArgumentException("Order Id has to be numeric");
        }
    }

    /*
    Validating that the value is 'B' or 'S'
     */
    private static boolean validateSide(String side){
        if("B".equals(side) || "S".equals(side)){
            return true;
        } else {
            throw new IllegalArgumentException("Side has to be either B (Buy) or S (Sell)");
        }
    }

    /*
    Validate the price is positive and less than or equal to 999999
     */
    private static boolean validatePrice(String price){
        Integer priceInt;
        try{
            priceInt = Integer.parseInt(price);
        } catch(NumberFormatException nfe){
            throw new IllegalArgumentException("Value of price provided is not parsable to integer. - " + price);
        }
        if(priceInt > 0 && priceInt <= 999999){
            return true;
        } else {
            throw new IllegalArgumentException(" Price has to be greater than 0 and less than 999999");
        }
    }

    /*
    Validate quantity is positive and less than or equal to 999,999,999
     */
    private static boolean validateQuantity(String quantity){
        Integer quantityInt;
        try{
            quantityInt = Integer.parseInt(quantity);
        } catch(NumberFormatException nfe){
            throw new IllegalArgumentException("Value of price provided is not parsable to integer. - " + quantity);
        }
        if(quantityInt > 0 && quantityInt <= 999999999){
            return true;
        } else {
            throw new IllegalArgumentException("Quantity has to be greater than 0 and less than 999999999");
        }
    }
}
