package com.lp.sale.service.impl;

import com.lp.sale.service.SaleService;
import com.lp.sale.util.StatusCode;
import com.lp.sys.common.WebUtils;
import com.lp.sys.domain.User;
import com.lp.warehouse.domain.Goods;
import com.lp.warehouse.domain.Outport;
import com.lp.warehouse.mapper.GoodsMapper;
import com.lp.warehouse.mapper.OutportMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class SaleServiceImpl implements SaleService {
    @Resource
    private GoodsMapper goodsMapper;

    @Resource
    private OutportMapper outportMapper;

    @Override
    public void saleItem(Integer goodsId) {
        saleBatch(goodsId, 1);
    }

    @Override
    public void saleBatch(Integer goodsId, Integer amount) {
        Goods goods = goodsMapper.selectById(goodsId);
        if (goods == null) {
            throw new StatusCode(404, "Goods Not Found");
        }
        if (goods.getAvailable() != 1) {
            throw new StatusCode(400, "Not available");
        }
        if (goods.getNumber() < amount) {
            throw new StatusCode(400, "Not enough number");
        }
        goods.setNumber(goods.getNumber() - amount);
        goodsMapper.updateById(goods);
        User user = (User) WebUtils.getSession().getAttribute("user");
        Outport outport = new Outport();
        outport.setGoodsid(goodsId);
        outport.setNumber(amount);
        outport.setOutputtime(new Date());
        outport.setOutportprice(goods.getPrice());
        outport.setOperateperson(user.getName());
        outportMapper.insert(outport);
    }
}
