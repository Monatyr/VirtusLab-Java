package com.virtuslab.internship.restApi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.virtuslab.internship.MainApp;
import com.virtuslab.internship.basket.Basket;
import com.virtuslab.internship.product.ProductDb;
import static org.junit.jupiter.api.Assertions.*;

import com.virtuslab.internship.receipt.Receipt;
import com.virtuslab.internship.receipt.ReceiptEntry;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ApiTest {

    @Test
    void CompareServerMadeReceiptWithHandMade() throws IOException {
        SpringApplication.run(MainApp.class);
        var url = new URL("http://localhost:8080/receipt");
        var con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Accept", "application/json");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoOutput(true);

        var db = new ProductDb();
        var cheese = db.getProduct("Cheese");
        var cereals = db.getProduct("Cereals");

        Basket basket = new Basket();
        basket.addProduct(cheese);
        basket.addProduct(cereals);
        basket.addProduct(cheese);

        List<ReceiptEntry> receiptEntriesV1 = new ArrayList<>();
        receiptEntriesV1.add(new ReceiptEntry(cheese, 2));
        receiptEntriesV1.add(new ReceiptEntry(cereals, 1));
        var expectedReceiptV1 = new Receipt(receiptEntriesV1);

        List<ReceiptEntry> receiptEntriesV2 = new ArrayList<>();
        receiptEntriesV2.add(new ReceiptEntry(cereals, 1));
        receiptEntriesV2.add(new ReceiptEntry(cheese, 2));
        var expectedReceiptV2 = new Receipt(receiptEntriesV2);

        var ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

        var expectedReceiptJsonV1 = ow.writeValueAsString(expectedReceiptV1).replaceAll("\\s+","");
        var expectedReceiptJsonV2 = ow.writeValueAsString(expectedReceiptV2).replaceAll("\\s+","");
        var basketContent = ow.writeValueAsString(basket);

        try(OutputStream os = con.getOutputStream()) {
            byte[] input = basketContent.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int responseCode = con.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);

        var in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        var response = new StringBuffer();

        while ((inputLine = in.readLine()) != null)
            response.append(inputLine);

        in.close();

        String stripedResponse = response.toString().replaceAll("\\s+","");

        //The server creates the receipt in a random order
        assertTrue(stripedResponse.compareTo(expectedReceiptJsonV1) == 0
                || stripedResponse.compareTo(expectedReceiptJsonV2) == 0);
    }
}
