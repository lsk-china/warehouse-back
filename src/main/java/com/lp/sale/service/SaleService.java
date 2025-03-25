package com.lp.sale.service;

import com.lp.warehouse.domain.Goods;

public interface SaleService {
    void saleItem(Integer goodsId);
    void saleBatch(Integer goodsId, Integer amount);
}
