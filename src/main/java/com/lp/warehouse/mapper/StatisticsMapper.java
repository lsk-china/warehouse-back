package com.lp.warehouse.mapper;

import com.lp.warehouse.domain.FlowStatistics;
import com.lp.warehouse.domain.PurchaseStatistics;
import com.lp.warehouse.domain.SaleStatistics;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StatisticsMapper {
    @Select("select bus_inport.id, bus_inport.paytype, bus_inport.inporttime, bus_inport.inportprice, bus_inport.operateperson, bus_inport.number, bus_inport.remark, (select bus_goods.goodsname from bus_goods where id=bus_inport.goodsid) as goods_name, (select bus_provider.providername from bus_provider where bus_provider.id = bus_inport.providerid) as provider_name from bus_inport limit #{offset},#{limit}")
    List<PurchaseStatistics> doGetPurchaseStatistics(Integer offset, Integer limit);

    @Select("select bus_outport.id, bus_outport.paytype, bus_outport.outputtime, bus_outport.operateperson, bus_outport.outportprice, bus_outport.remark, bus_outport.number, (select bus_goods.goodsname from bus_goods where id=bus_outport.goodsid) as goods_name, (select bus_provider.providername from bus_provider where bus_provider.id = (select bus_goods.providerid from bus_goods where bus_goods.id=bus_outport.goodsid)) as provider_name from bus_outport limit #{offset},#{limit}")
    List<SaleStatistics> doGetSaleStatistics(Integer offset, Integer limit);

    @Select("select\n" +
            "    bus_goods.id,\n" +
            "    bus_goods.goodsname,\n" +
            "    (select bus_provider.providername from bus_provider where bus_provider.id=bus_goods.providerid) as provider_name,\n" +
            "    bus_goods.price,\n" +
            "    ifnull(\n" +
            "            (select sum(bus_inport.inportprice * bus_inport.number) from bus_inport where bus_inport.goodsid=bus_goods.id),\n" +
            "            0.0\n" +
            "    ) as total_cost,\n" +
            "    ifnull(\n" +
            "            (select sum(bus_deliver.deliverprice * bus_deliver.number) from bus_deliver where bus_deliver.goodsid=bus_goods.id),\n" +
            "            0.0\n" +
            "    ) as total_income,\n" +
            "    (select total_income - total_cost) as total_profit\n" +
            "from bus_goods limit #{offset},#{limit}")
    List<FlowStatistics> doGetFlowStatistics(Integer offset, Integer limit);

    @Select("select count(id) from bus_goods")
    Long goodsCount();

    default List<SaleStatistics> getSaleStatistics(Integer page, Integer limit) {
        int offset = (page - 1) * limit - 1;
        offset = Math.max(offset, 0);
        return doGetSaleStatistics(offset, limit);
    }

    default List<PurchaseStatistics> getPurchaseStatistics(Integer page, Integer limit) {
        int offset = (page - 1) * limit - 1;
        offset = Math.max(offset, 0);
        return doGetPurchaseStatistics(offset, limit);
    }

    default List<FlowStatistics> getFlowStatistics(Integer page, Integer limit) {
        int offset = (page - 1) * limit - 1;
        offset = Math.max(offset, 0);
        return doGetFlowStatistics(offset, limit);
    }


}
