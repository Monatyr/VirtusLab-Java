package com.virtuslab.internship.discount;

import com.virtuslab.internship.product.Product;
import com.virtuslab.internship.receipt.Receipt;
import com.virtuslab.internship.receipt.ReceiptEntry;


public class FifteenPercentDiscount extends Discount{
    public static String NAME = "FifteenPercentDiscount";

    @Override
    protected boolean shouldApply(Receipt receipt) {
        return receipt.entries().stream()
                .filter(receiptEntry -> receiptEntry.product().type().equals(Product.Type.GRAINS))
                .map(ReceiptEntry::quantity)
                .mapToInt(Integer::intValue)
                .sum() >= 3;
    }

    @Override
    protected double getPercentage() {
        return 0.85;
    }

    @Override
    protected String getNAME() { return NAME; }
}
