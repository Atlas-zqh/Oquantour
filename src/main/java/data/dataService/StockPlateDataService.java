package data.dataService;

import po.PlatePO;
import po.SearchPO;
import po.Stock;

import java.util.List;

/**
 * Created by Pxr on 2017/3/23.
 */
public interface StockPlateDataService {
    /**
     * 根据板块名称，获得该段时间内存在数据的股票
     * @param searchPO 板块名，开始时间，结束时间
     * @return 股票代码
     */
    public List<List<Stock>> getStockInStockPlate(SearchPO searchPO);

    /**
     * 根据股票信息获得它所在的板块
     * @param stockInfo 股票名或代码
     * @return 板块名
     */
    public String getStockPlate(String stockInfo);

    /**
     * 得到板块中的所有股票 代码+名称
     * @param plateName 板块名
     * @return 板块中的所有股票
     */
    public List<String> getAllStockInfoInPlate(String plateName);

    /**
     * 获得板块一段时间内每个交易日的收益率
     * @param searchPO 板块名，开始日期，结束日期
     * @return 每个交易日的收益率与日期
     */
    public List<PlatePO> getPlateReturnRate(SearchPO searchPO);

}
