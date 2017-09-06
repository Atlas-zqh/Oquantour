package vo;

import po.Stock;

import java.util.Date;

/**
 * 画k线所需要的参数
 * 开盘闭盘，日期，最高最低，成交量
 * Created by Pxr on 2017/3/3.
 */
public class KLineVO {
    //开盘时的价格
    public double open;
    //关盘时的价格
    public double close;
    //当日的最高价
    public double high;
    //当日的最低价
    public double low;
    //日期
    public Date date;
    //股票名
    public String stockName;
    //股票号
    public String stockCode;
    //构造函数

    public KLineVO(Stock stock) {
        this.open = stock.getOpen();
        this.close = stock.getClose();
        this.high = stock.getHigh();
        this.low = stock.getLow();
        this.date = stock.getDate();
        this.stockName = stock.getName();
        this.stockCode = stock.getCode();
    }
}
