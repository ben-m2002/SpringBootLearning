package com.ben.learning;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

@RestController
public class FirstController {

    Map<String, Vector<Order>> orderMap = new TreeMap<>();

    @GetMapping("/order")
    @ResponseStatus(HttpStatus.FOUND)
    public Order[] getOrders(
            @RequestParam String customerName
    ){
        Order[] returnArr = new Order[orderMap.get(customerName).size()];
        for (int i = 0; i < orderMap.get(customerName).size(); i++) {
            returnArr[i] = orderMap.get(customerName).get(i);
        }
        return returnArr;
    }

    @PostMapping("/post-order")
    public String postOrder(
            @RequestBody Order order
    ) {
        orderMap.computeIfAbsent(order.getCustomerName(), k -> new Vector<>());
        orderMap.get(order.getCustomerName()).add(order);
        return "Post request made with message: " + order.toString();
    }
}
