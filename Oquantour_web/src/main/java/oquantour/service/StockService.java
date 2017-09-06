package oquantour.service;

import oquantour.exception.InnerValueException;
import oquantour.exception.StockDataNotExistException;
import oquantour.po.StockRealTimePO;
import oquantour.po.util.ChartInfo;
import oquantour.po.util.StockInfo;
import oquantour.util.tools.BasicIndices;

import java.util.List;
import java.util.Map;

/**
 * Created by keenan on 06/05/2017.
 */
public interface StockService {
    /**
     * 得到一只股票的数据（包括均线，投资建议，KDJ、MACD、BOLL、RSI、DMI）
     *
     * @param stockCode 股票代码
     * @return 股票信息
     */
    StockInfo getStockInfo(String stockCode) throws StockDataNotExistException;

    /**
     * 获得所有股票号和股票名
     *
     * @return 格式：  股票号;股票名
     */
    List<String> getAllStockCodeAndName();

    /**
     * 获得相似股票推荐
     *
     * @param stockCode 股票代码
     * @return 相似股票列表
     */
    List<StockRealTimePO> getRecommendedStock(String stockCode);

    /**
     * 获得股票名
     *
     * @param stockCode 股票号
     * @return 股票名
     */
    String getStockName(String stockCode);

    /**
     * 获得股票号
     *
     * @param stockName 股票名
     * @return 股票号
     */
    String getStockCode(String stockName);

    /**
     * 获得实时股票数据
     *
     * @param stockIDs
     * @return Map<StockID，StockRealTimePO></>
     */
    Map<String, StockRealTimePO> getRealTimeStockInfo(String... stockIDs);

    /**
     * 获得股票价值综合评价参数
     *
     * @param stockID 股票号
     * @return 股票价值综合评价参数
     */
    List<ChartInfo> getInnerValue(String stockID) throws InnerValueException;

    /**
     * 股票评级系数
     * 1.00~1.09强力买入；1.10~2.09买入；2.10~3.09观望；3.10~4.09适度减持；4.10~5.00卖出。
     *
     * @param stockID
     * @return 股票评级系数（股票信息不存在返回正无穷）
     */
    double getScore(String stockID);

    /**
     * 根据基本面指标选股
     *
     * @param indices 基本面指标<指标类型，取值范围>
     * @return 选出来的股票<股票号，股票名>
     */
    Map<String, String> selectStock(Map<BasicIndices, double[]> indices);


}
