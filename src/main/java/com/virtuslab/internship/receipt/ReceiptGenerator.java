package com.virtuslab.internship.receipt;

import com.virtuslab.internship.basket.Basket;
import com.virtuslab.internship.discount.Discount;
import com.virtuslab.internship.discount.FifteenPercentDiscount;
import com.virtuslab.internship.discount.TenPercentDiscount;
import com.virtuslab.internship.product.Product;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReceiptGenerator {

    public Receipt generate(Basket basket){
        Map<Product, Integer> productsCount = new HashMap<>();
        basket.getProducts().forEach(product -> {
            Integer productAmount = productsCount.get(product);
            productsCount.put(product, productAmount == null ? 1 : productAmount+1);
        });

        List<ReceiptEntry> receiptEntries = new ArrayList<>();
        productsCount.forEach((product, count) -> {
            receiptEntries.add(new ReceiptEntry(product, count));
        });

        Receipt receipt = new Receipt(receiptEntries);
        List<Discount> discounts = List.of(new FifteenPercentDiscount(), new TenPercentDiscount());

        for(Discount discount: discounts){
            receipt = discount.apply(receipt);
        }

        return receipt;
    }
}
