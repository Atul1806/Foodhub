package com.foodhub.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api")
public class HealthController {

    @GetMapping("/health")
    @ResponseBody
    public Map<String, String> helloGFG() {
        Map<String, String> response = new HashMap<>();
        response.put("Status","Up");
        response.put("service", "foodhub");
        return response;
    }
}
