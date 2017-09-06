package data.dataService;

import po.CalMeanPO;
import po.FilterStockPO;
import po.SearchPO;
import po.Stock;

import java.util.Date;
import java.util.List;

/**
 * Created by keenan on 03/03/2017.
 */
public interface StockDataService {

//    /**
//     * 根据股票名称和日期范围查询股票数据
//     *
//     * @param searchPO 要使用的属性包括"stockName","startDate","endDate"
//     * @return 搜索结果
//     */
//    List<Stock> searchStockByNameAndDateRange(SearchPO searchPO);

    /**
     * 根据股票代码和日期范围查询股票数据
     *
     * @param searchPO 要使用的属性包括"stockCode","startDate","endDate"
     * @return 搜索结果
     */
    List<Stock> searchStock(SearchPO searchPO);

    /**
     * 根据日期搜索市场中所有股票的信息
     *
     * @param searchPO 要使用的属性仅包括startDate和endDate
     *                 endDate和startDate必须相同，否则将返回一个空的列表
     * @return 搜索结果
     */
    List<Stock> searchAllStocksByDate(SearchPO searchPO);

    /**
     * 得到股票代码（无论传入的是股票名还是股票代码）
     *
     * @param stockInfoList 股票名或股票代码
     * @return 股票代码
     */
    List<String> getStockCode(List<String> stockInfoList);

    /**
     * 得到所有的股票代码和名字，格式为"名字;代码"
     *
     * @return 所有的股票代码和名字，格式为"名字;代码"
     */
    List<String> getAllStockCodeAndName();

    /**
     * 得到一段时间内所有股票信息
     * @param searchPO 开始日期，结束日期
     * @return 所有股票信息
     */
    List<List<Stock>> getAllStockInfo(SearchPO searchPO);

    /**
     * 筛选掉没有数据的股票，如果不需要形成期长度，那长度就是0
     *
     * @param stockList       股票列表
     * @param startDate       开始日期
     * @param endDate         结束日期
     * @param formativePeriod 形成期长度
     * @return 有数据的股票和没有数据的股票
     */
    FilterStockPO filterStockWithoutData(List<String> stockList, Date startDate, Date endDate, int formativePeriod);

    /**
     * 筛选掉停牌的股票
     *
     * @param stockList 股票列表
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @param formativePeriod 形成期长度
     * @return 停牌的股票和没有停牌的股票
     */
    FilterStockPO filterStockHalt(List<List<Stock>> stockList, Date startDate, Date endDate, int formativePeriod);

    /**
     * 计算均值时为了提高效率，只进行一次搜索，即搜索该股票那段时间的信息，以及前days-1天的信息
     * @param searchPO 搜索信息
     * @param days 均值类型
     * @return 为了计算均值的po
     */
    CalMeanPO getStockListForMean(SearchPO searchPO,int days);
}
