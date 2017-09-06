package bl.stock;

import exception.BothStockNotExistException;
import exception.FirstStockNotExistException;
import exception.NotTransactionDayException;
import exception.SecondStockNotExistException;
import vo.*;

import java.util.Date;
import java.util.List;

/**
 * bl层的股票模块，为数据层提供接口
 * Created by Pxr on 2017/3/3.
 */
public interface StockBLService {
    //得到某种股票某一段时间内k线图所需的信息
    List<KLineVO> getKLineInfo(SearchVO searchVO) throws FirstStockNotExistException;

    //得到某种股票均图所需的信息
    List<List<DailyAverageVO>> getDailyAverageInfo(SearchVO searchVO);

    //比较两种股票在某一段时间内的信息
    List<StockCompareVO> compareStock(SearchVO searchVO) throws FirstStockNotExistException, SecondStockNotExistException, BothStockNotExistException;

    //得到某一天市场信息
    MarketTemperatureVO getMarketInfo(SearchVO searchVO) throws NotTransactionDayException;

    //
    double getTemperature(MarketTemperatureVO marketTemperatureVO) throws NotTransactionDayException;

    //根据股票代码和日期判断该日是否为该股票的交易日
    boolean isTransactionDay(String stockCode, Date date);

    //得到所有的股票代码和名字，格式为"名字;代码"
    List<String> getAllStockCodeAndName();

    //得到主板所有股票的股票代码和名字，格式为"名字;代码"
    List<String> getAllStockCodeAndNameInPlate(String plate);

    //得到板块中的市场信息
    MarketTemperatureVO getMarketInfoInPlate(String plateName, Date date) throws NotTransactionDayException;
}
