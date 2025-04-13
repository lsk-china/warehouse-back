package com.lp.warehouse.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lp.sys.common.DataGridView;
import com.lp.warehouse.service.StatisticsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController()
@RequestMapping("/statistics")
public class StatisticsController {
    @Resource
    private StatisticsService statisticsService;

    @GetMapping("/flow")
    public DataGridView flow(@RequestParam Integer page, @RequestParam Integer limit) {
        return statisticsService.getFlowStatistics(page, limit);
    }
}
