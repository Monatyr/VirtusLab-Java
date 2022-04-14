package com.virtuslab.internship.discount;

import com.virtuslab.internship.receipt.Receipt;

import java.math.BigDecimal;

abstract public class Discount {

    public Receipt apply(Receipt receipt){
        if (shouldApply(receipt)) {
            var totalPrice = receipt.totalPrice().multiply(BigDecimal.valueOf(getPercentage()));
            var discounts = receipt.discounts();
            discounts.add(getNAME());
            return new Receipt(receipt.entries(), discounts, totalPrice);
        }
        return receipt;
    };

    protected abstract boolean shouldApply(Receipt receipt);

    protected abstract double getPercentage();

    protected  abstract String getNAME();
}
