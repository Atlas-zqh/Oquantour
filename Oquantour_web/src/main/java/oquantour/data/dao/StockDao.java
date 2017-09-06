package oquantour.data.dao;

import oquantour.exception.FormativeNotExistsException;
import oquantour.exception.StockLessThanOneHundredException;
import oquantour.exception.WrongCombinationException;
import oquantour.po.*;
import oquantour.util.tools.BasicIndices;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by island on 5/3/17.
 */
public interface StockDao {

    /**
     * 根据股票代码和日期范围查询股票数据
     *
     * @param stockCode
     * @param startDate
     * @param endDate
     * @return
     */
    List<StockPO> searchStock(String stockCode, Date startDate, Date endDate);

    /**
     * 获得实时股票数据
     *
     * @param stockIDs
     * @return Map<StockID，StockRealTimePO></>
     */
    Map<String, StockRealTimePO> getRealTimeStockInfo(String... stockIDs);

    /**
     * 更新股票实时数据
     */
    void updateRealTimeStockInfo();

    /**
     * 根据股票代码查询股票数据
     *
     * @param stockCode
     * @return
     */
    List<StockPO> searchStock(String stockCode);

    /**
     * 根据日期搜索市场中所有股票的信息
     *
     * @param startDate
     * @param endDate
     * @return
     */
    Map<String, List<StockPO>> searchAllStocksByDate(Date startDate, Date endDate);

    /**
     * 得到股票代码;股票名称（无论传入的是股票名还是股票代码）
     *
     * @param stockInfoList 股票名或股票代码
     * @return 股票代码；股票名称
     */
    List<String> getStockCode(String... stockInfoList);

    /**
     * 得到所有的股票代码和名字，格式为"名字;代码"
     *
     * @return 所有的股票代码和名字，格式为"名字;代码"
     */
    List<String> getAllStockCodeAndName();

    /**
     * 根据股票号和日期获得前n个交易日的股票信息
     *
     * @param date
     * @param num
     * @param stockCode
     * @return
     */
    Map<String, List<StockPO>> getStocksOfPreDates(Date date, int num, String... stockCode);

    /**
     * 获得某交易日前num日的所有股票数据
     *
     * @param date
     * @param num
     * @return
     */
    Map<String, List<StockPO>> getStocksOfPreDates(Date date, int num);

    /**
     * 根据传入的日期判断该日是否为该股票的交易日
     *
     * @param date
     * @param stockCode
     * @return
     */
    boolean isTransactionDay(Date date, String stockCode);

    /**
     * 判断该日是否为交易日
     *
     * @param date
     * @return
     */
    boolean isTransactionDay(Date date);

    /**
     * 过滤股票
     *
     * @param startDate
     * @param endDate
     * @param stocks
     * @param formativePeriod
     * @param filter_ST
     * @param filter_NoData
     * @param filter_Suspension
     * @return
     */
    Map<Date, List<StockPO>> filterStock(Date startDate, Date endDate, List<String> stocks, int formativePeriod, boolean filter_ST, boolean filter_NoData, boolean filter_Suspension, boolean largerThan100) throws FormativeNotExistsException, StockLessThanOneHundredException;

    /**
     * 自选策略筛选股票
     *
     * @param map
     * @return <股票名, 股票号>
     */
    Map<String, String> filterStock(Map<BasicIndices, double[]> map);

    /**
     * 获得一只股票所有交易日的信息
     *
     * @param stockID
     * @return
     */
    List<StockPO> getAllInfoOfOneStock(String stockID);

    /**
     * 获得某行业在某段时间内所有的股票数据
     *
     * @param industry
     * @param startDate
     * @param endDate
     * @return
     */
    Map<String, List<StockPO>> getStockInfoByIndustryAndDate(String industry, Date startDate, Date endDate);

    /**
     * 增加股票基本数据
     *
     * @param stockInfoPO
     */
    void addStockInfo(StockInfoPO stockInfoPO);

    /**
     * 每日更新股票信息
     */
    void updateDailyStockInfo(String date);

    /**
     * 更新股票信息
     *
     * @param stockPO
     */
    public void update(StockPO stockPO);

    /**
     * 删除股票信息
     *
     * @param stockPO
     */
    public void delete(StockPO stockPO);

    /**
     * 保存股票信息
     *
     * @param stockPO
     */
    public void add(StockPO stockPO);

    /**
     * 筛选无数据股票
     */
    public void filterAllStocks();

    /**
     * 获得所有股票的数据
     */
    public void getAllStockInfos();
}
