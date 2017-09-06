package vo;

import java.util.Date;
import java.util.List;

/**
 * 比较的两只股票数据
 * 包括但不局限于这两只股票这段时间的最低值、最高
 * 值、涨幅/跌幅、每天的收盘价和对数收益率、对数收益率方差。
 * Created by Pxr on 2017/3/3.
 */
public class StockCompareVO {
    //最高价
    public double high;
    //最低价
    public double low;
    //一段时间对数收益率方差
    public double logReturnVariance;
    //股票名
    public String stockName;
    //股票号
    public String stockCode;
    //涨跌幅
    public double chg;
    //
    public List<Double> closeList;
    //
    public List<Date> dateList;
    //
    public List<Double> logReturnList;
//    //关盘价的最大最小
//    public double maxClose;
//    public double minClose;
//    //对数收益率的最大最小
//    public double maxLogReturn;
//    public double minLogReturn;


    public StockCompareVO(double high, double low, double logReturnVariance, String stockName, String stockCode, double chg, List<Double> closeList, List<Date> dateList, List<Double> logReturnList) {
        this.high = high;
        this.low = low;
        this.logReturnVariance = logReturnVariance;
        this.stockName = stockName;
        this.stockCode = stockCode;
        this.chg = chg;
        this.closeList = closeList;
        this.dateList = dateList;
        this.logReturnList = logReturnList;
    }
}
