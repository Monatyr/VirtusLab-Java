package com.virtuslab.internship.receipt;

import com.virtuslab.internship.basket.Basket;
import org.springframework.web.bind.annotation.*;


@RestController
public class ReceiptController {
    private final ReceiptGenerator receiptService = new ReceiptGenerator();

    @PostMapping(value="/receipt", consumes="application/json")
    public Receipt getReceipt(@RequestBody Basket basket){
        return receiptService.generate(basket);
    }
}
