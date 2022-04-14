package com.virtuslab.internship.discount;

import com.virtuslab.internship.receipt.Receipt;

import java.math.BigDecimal;

public class TenPercentDiscount extends Discount{

    public static String NAME = "TenPercentDiscount";

    @Override
    protected boolean shouldApply(Receipt receipt) {
        return receipt.totalPrice().compareTo(BigDecimal.valueOf(50)) >= 0;
    }

    @Override
    protected double getPercentage() {
        return 0.9;
    }

    @Override
    protected String getNAME(){ return NAME; }
}
