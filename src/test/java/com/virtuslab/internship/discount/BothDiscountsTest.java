package com.virtuslab.internship.discount;

import com.virtuslab.internship.product.ProductDb;
import com.virtuslab.internship.receipt.Receipt;
import com.virtuslab.internship.receipt.ReceiptEntry;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BothDiscountsTest {

    @Test
    void shouldApplyBothDiscounts(){ //at least 3 grain products and the total price after the 15% discount is >= 50
        //Given
        var productDb = new ProductDb();
        var cheese = productDb.getProduct("Cheese");
        var bread = productDb.getProduct("Bread");
        var cereals = productDb.getProduct("Cereals");
        List<ReceiptEntry> receiptEntries = new ArrayList<>();
        receiptEntries.add(new ReceiptEntry(cheese, 2));
        receiptEntries.add(new ReceiptEntry(bread, 2));
        receiptEntries.add(new ReceiptEntry(cereals, 1));

        var receipt = new Receipt(receiptEntries);
        var fifteenPercentDiscount = new FifteenPercentDiscount();
        var tenPercentDiscount = new TenPercentDiscount();
        var expectedTotalPrice = cheese.price().multiply(BigDecimal.valueOf(2)).add(bread.price().multiply(BigDecimal.valueOf(2))).add(cereals.price()).multiply(BigDecimal.valueOf(0.85)).multiply(BigDecimal.valueOf(0.9));

        // When
        var receiptAfterOneDiscount = fifteenPercentDiscount.apply(receipt);
        var receiptAfterBothDiscounts = tenPercentDiscount.apply(receiptAfterOneDiscount);

        // Then
        assertEquals(expectedTotalPrice, receiptAfterBothDiscounts.totalPrice());
        assertEquals(2, receiptAfterBothDiscounts.discounts().size());
    }

    @Test
    void shouldOnlyApply15PercentButNot10(){ //at least 3 grain products but the total price after the 15% discount < 50
        var productDb = new ProductDb();
        var cheese = productDb.getProduct("Cheese");
        var bread = productDb.getProduct("Bread");
        var cereals = productDb.getProduct("Cereals");
        List<ReceiptEntry> receiptEntries = new ArrayList<>();
        receiptEntries.add(new ReceiptEntry(cheese, 1));
        receiptEntries.add(new ReceiptEntry(bread, 2));
        receiptEntries.add(new ReceiptEntry(cereals, 1));

        var receipt = new Receipt(receiptEntries);
        var fifteenPercentDiscount = new FifteenPercentDiscount();
        var tenPercentDiscount = new TenPercentDiscount();
        var expectedTotalPrice = cheese.price().add(bread.price().multiply(BigDecimal.valueOf(2))).add(cereals.price()).multiply(BigDecimal.valueOf(0.85));

        // When
        var receiptAfterOneDiscount = fifteenPercentDiscount.apply(receipt);
        var receiptAfterBothDiscounts = tenPercentDiscount.apply(receiptAfterOneDiscount);

        // Then
        assertEquals(expectedTotalPrice, receiptAfterBothDiscounts.totalPrice());
        assertEquals(1, receiptAfterBothDiscounts.discounts().size());
    }

    @Test
    void shouldOnlyApply10PercentButNot15(){ //total price >= 50 but less than 3 grain products in the basket
        var productDb = new ProductDb();
        var cheese = productDb.getProduct("Cheese");
        var bread = productDb.getProduct("Bread");
        var cereals = productDb.getProduct("Cereals");
        List<ReceiptEntry> receiptEntries = new ArrayList<>();
        receiptEntries.add(new ReceiptEntry(cheese, 2));
        receiptEntries.add(new ReceiptEntry(bread, 1));
        receiptEntries.add(new ReceiptEntry(cereals, 1));

        var receipt = new Receipt(receiptEntries);
        var fifteenPercentDiscount = new FifteenPercentDiscount();
        var tenPercentDiscount = new TenPercentDiscount();
        var expectedTotalPrice = cheese.price().multiply(BigDecimal.valueOf(2)).add(bread.price()).add(cereals.price()).multiply(BigDecimal.valueOf(0.9));

        // When
        var receiptAfterOneDiscount = fifteenPercentDiscount.apply(receipt);
        var receiptAfterBothDiscounts = tenPercentDiscount.apply(receiptAfterOneDiscount);

        // Then
        assertEquals(expectedTotalPrice, receiptAfterBothDiscounts.totalPrice());
        assertEquals(1, receiptAfterBothDiscounts.discounts().size());
    }

    @Test
    void shouldNotApplyAnyDiscount(){ //less than 3 grain products in the basket and the total price < 50
        var productDb = new ProductDb();
        var cheese = productDb.getProduct("Cheese");
        var bread = productDb.getProduct("Bread");
        var cereals = productDb.getProduct("Cereals");
        List<ReceiptEntry> receiptEntries = new ArrayList<>();
        receiptEntries.add(new ReceiptEntry(cheese, 1));
        receiptEntries.add(new ReceiptEntry(bread, 1));
        receiptEntries.add(new ReceiptEntry(cereals, 1));

        var receipt = new Receipt(receiptEntries);
        var fifteenPercentDiscount = new FifteenPercentDiscount();
        var tenPercentDiscount = new TenPercentDiscount();
        var expectedTotalPrice = cheese.price().add(bread.price()).add(cereals.price());

        // When
        var receiptAfterOneDiscount = fifteenPercentDiscount.apply(receipt);
        var receiptAfterBothDiscounts = tenPercentDiscount.apply(receiptAfterOneDiscount);

        // Then
        assertEquals(expectedTotalPrice, receiptAfterBothDiscounts.totalPrice());
        assertEquals(0, receiptAfterBothDiscounts.discounts().size());
    }
}
