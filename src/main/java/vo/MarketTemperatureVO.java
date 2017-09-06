package vo;

import po.Stock;

import java.util.Date;
import java.util.List;

/**
 * 市场温度计
 * <p>
 * 内容包括但不限于：
 * 当日总交易量
 * 涨停股票数
 * 跌停股票数
 * 涨幅超过5%的股票数
 * 跌幅超过5%的股票数
 * 开盘-收盘大于5%*上一个交易日收盘价的股票个数
 * 开盘-收盘小于-5%*上一个交易日收盘价的股票个数
 * <p>
 * Created by Pxr on 2017/3/3.
 */
public class MarketTemperatureVO {
    //总交易量
    public long totalVolume;
    //涨停股票数
    public List<StockVO> limitUpStock;
    //跌停股票数
    public List<StockVO> limitDownStock;
    //涨幅超过5%的股票数
    public List<StockVO> up5perStock;
    //跌幅超过5%的股票数
    public List<StockVO> down5perStock;
    //当日大涨股
    public List<StockVO> openUp5perStock;
    //当日大跌股
    public List<StockVO> openDown5perStock;
    //当日总股票数
    public int totalNum;
    //涨幅榜
    public List<StockVO> upList;
    //跌幅榜
    public List<StockVO> downList;

    public Date date;

    public MarketTemperatureVO(long totalVolume, List<StockVO> limitUpStock, List<StockVO> limitDownStock, List<StockVO> up5perStock, List<StockVO> down5perStock, List<StockVO> openUp5perStock, List<StockVO> openDown5perStock, int totalNum, List<StockVO> upList, List<StockVO> downList) {
        this.totalVolume = totalVolume;
        this.limitUpStock = limitUpStock;
        this.limitDownStock = limitDownStock;
        this.up5perStock = up5perStock;
        this.down5perStock = down5perStock;
        this.openUp5perStock = openUp5perStock;
        this.openDown5perStock = openDown5perStock;
        this.totalNum = totalNum;
        this.upList = upList;
        this.downList = downList;
    }
}
