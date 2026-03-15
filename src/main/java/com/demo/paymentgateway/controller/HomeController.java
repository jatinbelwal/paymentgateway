package com.demo.paymentgateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HomeController {

    @GetMapping("/")
    public Map<String, String> health() {
        return Map.of(
                "application", "Payment Gateway Simulation",
                "status", "RUNNING"
        );
    }
}
