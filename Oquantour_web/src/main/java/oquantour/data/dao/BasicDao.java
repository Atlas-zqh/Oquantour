package oquantour.data.dao;

import oquantour.po.*;
import oquantour.po.util.MarketTemperature;

import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by island on 2017/6/4.
 */
public interface BasicDao {
    /**
     * 根据股票ID获得股票基本信息
     * @param code
     * @return
     */
    Map<String, StockInfoPO> getStockInfo(String... code);

    /**
     * 获得热股
     * @return
     */
    List<StockRealTimePO> getHotStocks();

    /**
     * 添加股票基本数据
     * @param stockBasicInfoPO
     */
    void addBasicInfo(StockBasicInfoPO stockBasicInfoPO);

    /**
     * 根据股票ID和季度获得股票基本面信息
     * @param year
     * @param quarter
     * @param code
     * @return
     */
    Map<String, StockBasicInfoPO> getStockBasicInfo(int year, int quarter, String... code);

    /**
     * 获得市场温度
     * @return
     */
    MarketTemperature getMarketTemperature();

    /**
     * 获得基本面数据每个因子的范围
     */
    void getWidth();

    /**
     * 添加新闻信息
     */
    void addNewsInfo();

    /**
     * 获得新闻信息
     * @return
     */
    List<NewsPO> getNewsInfo();

    /**
     * 添加龙虎榜数据
     * @param date
     */
    void addTopList(Date date);

    /**
     * 获得龙虎榜数据
     * @return
     */
    List<TopListPO> getTopListInfo();
}
