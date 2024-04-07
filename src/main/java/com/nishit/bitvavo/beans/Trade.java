package com.nishit.bitvavo.beans;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import static com.nishit.bitvavo.constants.Constants.TRADE_WORD;
import static com.nishit.bitvavo.constants.Constants.TRADE_DELIMITER;
import static com.nishit.bitvavo.constants.Constants.BLANK_SPACE;

@Getter
@Builder
@AllArgsConstructor
public class Trade {
    private final String aggressOrderId; //orderId that triggered the match
    private final String restingOrderId; //orderId of existing order in the orderBook
    private final Integer price;
    private final Integer quantity;

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(TRADE_WORD).append(BLANK_SPACE).append(aggressOrderId).append(TRADE_DELIMITER).append(restingOrderId).append(TRADE_DELIMITER).append(price).append(TRADE_DELIMITER).append(quantity);
        return builder.toString();
    }
}