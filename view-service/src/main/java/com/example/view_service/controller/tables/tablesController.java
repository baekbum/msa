package com.example.view_service.controller.tables;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/tables")
public class tablesController {

    @GetMapping("users")
    public String users() {
        return "/tables/users";
    }

    @GetMapping("orders")
    public String orders() {
        return "/tables/orders";
    }

    @GetMapping("catalogs")
    public String catalogs() {
        return "/tables/catalogs";
    }
}
