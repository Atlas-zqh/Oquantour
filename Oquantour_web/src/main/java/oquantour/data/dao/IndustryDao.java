package oquantour.data.dao;

import oquantour.po.StockRealTimePO;
import oquantour.po.util.ChartInfo;

import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by island on 2017/6/6.
 */
public interface IndustryDao {
    /**
     * 获得行业信息
     * @param plateName
     * @param startDate
     * @param endDate
     * @return
     */
    List<ChartInfo> getIndustryReturnRate(String plateName, Date startDate, Date endDate);

    /**
     * 获得所有行业名
     * @return
     */
    List<String> getAllIndustries();

    /**
     * 获得股票所在的板块名
     * @param stockIDs
     * @return Map<股票名, 行业名></>
     */
    Map<String, String> getIndustryOfStock(String... stockIDs);

    /**
     * 获得行业中所有股票及其实时数据
     * @param industryName
     * @return
     */
    Map<String, StockRealTimePO> getStocksInIndustry(String industryName);

    /**
     * 获得行业中所有股票的ID
     * @param industryName
     * @return
     */
    List<String> getStockIDsInIndustry(String industryName);

    /**
     * 添加行业信息
     * @param industry
     */
    void addIndustryInfo(String industry);
}
