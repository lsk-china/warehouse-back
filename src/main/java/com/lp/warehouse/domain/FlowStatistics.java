package com.lp.warehouse.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlowStatistics implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    @TableField("goodsname")
    private String goodsName;

    @TableField("provider_name")
    private String providerName;

    private Double price;

    @TableField("total_cost")
    private Double totalCost;

    @TableField("total_income")
    private Double totalIncome;

    @TableField("total_profit")
    private Double totalProfit;
}
