package com.lp.warehouse.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lp.warehouse.domain.Deliver;
import com.lp.warehouse.domain.Goods;
import com.lp.warehouse.mapper.DeliverMapper;

import com.lp.warehouse.mapper.GoodsMapper;
import com.lp.warehouse.service.DeliverService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

/**
 * @auth lp
 */
@Service
@Transactional
public class DeliverServiceImpl extends ServiceImpl<DeliverMapper, Deliver> implements DeliverService {
    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public boolean save(Deliver entity) {
        //根据商品编号查询商品
        Goods goods = goodsMapper.selectById(entity.getGoodsid());
        goods.setNumber(goods.getNumber() - entity.getNumber());
        if (goods.getNumber() < goods.getDangernum()) {
            goods.setAvailable(0);
        }
        goodsMapper.updateById(goods);
        //保存发货信息
        return super.save(entity);
    }

    @Override
    public boolean updateById(Deliver entity) {
        //根据进货ID查询进货
        Deliver deliver = this.baseMapper.selectById(entity.getId());
        //根据商品ID查询商品信息
        Goods goods = this.goodsMapper.selectById(entity.getGoodsid());
        //库存的算法  当前库存-进货单修改之前的数量+修改之后的数量
        /*
        首先计算 物品剩余数量
如果 物品原来的数量 > 警戒值 而且 物品被设为不可用， 认为 物品被手动设置成不可用
如果 更新之后的物品数量 > 警戒值 而且 物品不是手动被设置成不可用，那么 设置物品为可用
如果 更新之后的物品数量 <= 警戒致 而且 物品现在处于可用状态， 那么 设置物品为不可用
         */
        boolean availableManualSetFlag = goods.getAvailable() == 0 && (goods.getNumber() > goods.getDangernum());
        int remaining = goods.getNumber() - deliver.getNumber() + entity.getNumber();
        if (remaining < 0) {
            throw new RuntimeException("cannot update: remaining is negative");
        }
        if (!availableManualSetFlag && remaining > goods.getDangernum()) {
            goods.setAvailable(1);
        } else if (remaining < goods.getDangernum() && goods.getAvailable() == 1) {
            goods.setAvailable(0);
        }
        goods.setNumber(remaining);
        this.goodsMapper.updateById(goods);
        //更新进货单
        return super.updateById(entity);
    }


    @Override
    public boolean removeById(Serializable id) {
        //根据发货ID查询进货
        Deliver deliver = this.baseMapper.selectById(id);
        //根据商品ID查询商品信息
        Goods goods = this.goodsMapper.selectById(deliver.getGoodsid());
        //库存的算法  当前库存-进货单数量
        goods.setNumber(goods.getNumber() - deliver.getNumber());
        if (goods.getNumber() < 0) {
            throw new RuntimeException("cannot remove: remaining is negative");
        }
        if (goods.getNumber() < goods.getDangernum()) {
            goods.setAvailable(0);
        }
        this.goodsMapper.updateById(goods);
        return super.removeById(id);
    }

}
