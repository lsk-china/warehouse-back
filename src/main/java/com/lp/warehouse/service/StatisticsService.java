package com.lp.warehouse.service;

import com.lp.sys.common.DataGridView;
import com.lp.warehouse.domain.FlowStatistics;
import com.lp.warehouse.domain.PurchaseStatistics;
import com.lp.warehouse.domain.SaleStatistics;

import java.util.List;

public interface StatisticsService {
    List<PurchaseStatistics> getPurchaseStatistics(Integer page, Integer limit);
    List<SaleStatistics> getSaleStatistics(Integer page, Integer limit);
    DataGridView getFlowStatistics(Integer page, Integer limit);
}
