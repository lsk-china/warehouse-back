package com.lp.warehouse.service.impl;

import com.lp.sys.common.DataGridView;
import com.lp.warehouse.domain.FlowStatistics;
import com.lp.warehouse.domain.PurchaseStatistics;
import com.lp.warehouse.domain.SaleStatistics;
import com.lp.warehouse.mapper.StatisticsMapper;
import com.lp.warehouse.service.StatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Resource
    private StatisticsMapper statisticsMapper;

    @Override
    public List<PurchaseStatistics> getPurchaseStatistics(Integer page, Integer limit) {
        List<PurchaseStatistics> purchaseStatistics = statisticsMapper.getPurchaseStatistics(page, limit);
        purchaseStatistics  = purchaseStatistics
                .stream()
                .peek(record -> {
                    double totalPrice = record.getNumber() * record.getInportprice();
                    record.setTotalPrice(round(totalPrice, 2));
                })
                .collect(Collectors.toList());
        return purchaseStatistics;
    }

    @Override
    public List<SaleStatistics> getSaleStatistics(Integer page, Integer limit) {
        List<SaleStatistics> saleStatistics = statisticsMapper.getSaleStatistics(page, limit);
        saleStatistics  = saleStatistics
                .stream()
                .peek(record -> {
                    double totalPrice = record.getNumber() * record.getOutportprice();
                    record.setTotalPrice(round(totalPrice, 2));
                })
                .collect(Collectors.toList());
        return saleStatistics;
    }

    @Override
    public DataGridView getFlowStatistics(Integer page, Integer limit) {
        List<FlowStatistics> flowStatistics = statisticsMapper.getFlowStatistics(page, limit);
        Long total = statisticsMapper.goodsCount();
        DataGridView dataGridView = new DataGridView();
        dataGridView.setCount(total);
        dataGridView.setData(flowStatistics);
        return dataGridView;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
