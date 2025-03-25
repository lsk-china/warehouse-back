package com.lp.sale.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sale")
public class RedirectController {
    @RequestMapping("/toSale")
    public String toSale() {
        return "sale/sale";
    }
}
