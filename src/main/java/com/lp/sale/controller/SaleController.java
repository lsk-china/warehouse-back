package com.lp.sale.controller;

import com.lp.sale.service.SaleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/sale")
public class SaleController {

    @Resource
    private SaleService saleService;

    @GetMapping("/item")
    public Object saleItem(Integer goodsId) {
        saleService.saleItem(goodsId);
        return "Success";
    }

    @GetMapping("/batch")
    public Object saleBatch(Integer goodsId, Integer amount) {
        saleService.saleBatch(goodsId, amount);
        return "Success";
    }
}
