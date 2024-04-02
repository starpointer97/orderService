package com.nishit.bitvavo.beans;

public class Order {

    public static final Integer NUMBER_OF_FIELDS_IN_THIS_CLASS = 5; //change this count depending on number of fields
    private final String orderId;
    private final BuyOrSell side;
    private final Integer price;
    private final Integer quantity;

    public Order(String orderId, String side, Integer price, Integer quantity){
        if(validateInputs(orderId, side, price, quantity)){
            this.orderId = orderId;
            this.side = BuyOrSell.valueOf(side);
            this.price = price;
            this.quantity = quantity;
        } else {
            throw new IllegalArgumentException("Provided inputs are invalid."
                + "orderId needs to be a number. Provided is " + orderId +
                " side needs to be either B or S. Provided is " + side +
                " price needs to be > 0 and  <= 999999. Provided is " + price +
                " quantity needs to be  > 0 and <= 999999999. Provided is " + quantity);
        }
    }

    private boolean validateInputs(String orderId, String side, Integer price, Integer quantity){
        return validateOrderId(orderId) && validateSide(side) && validatePrice(price) && validateQuantity(quantity);
    }

    /*
    Making sure that the orderId is a valid numeric.
     */
    private boolean validateOrderId(String orderId){
        try{
            Long.parseLong(orderId);
            return true;
        } catch(NumberFormatException nfe){
            return false;
        }
    }

    /*
    Validating that the value is 'B' or 'S'
     */
    private boolean validateSide(String side){
        return ("B".equals(side) || "S".equals(side));
    }

    /*
    Validate the price is positive and less than or equal to 999999
     */
    private boolean validatePrice(Integer price){
        return ( price > 0 && price <= 999999);
    }

    /*
    Validate quantity is positive and less than or equal to 999,999,999
     */
    private boolean validateQuantity(Integer quantity){
        return (quantity > 0 && quantity <= 999999999);
    }
}
