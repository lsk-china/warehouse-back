package com.lp.sale.service;

public interface SaleService {
    void saleItem(Integer goodsId);
    void saleBatch(Integer goodsId, Integer amount);
}
