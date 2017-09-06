package vo;

/**
 * 单只股票的基本信息
 * <p>
 * Created by keenan on 18/04/2017.
 */
public class StockBasicInfoVO {
    // 股票代码
    private String stockCode;
    // 股票名字
    private String stockName;
    // 开盘价
    private double open;
    // 最高价
    private double high;
    // 最低价
    private double low;
    // 收盘价
    private double close;
    // 复权收盘价
    private double adjClose;

    public StockBasicInfoVO(String stockCode, String stockName, double open, double high, double low, double close, double adjClose) {
        this.stockCode = stockCode;
        this.stockName = stockName;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.adjClose = adjClose;
    }

    public String getStockCode() {
        return stockCode;
    }

    public String getStockName() {
        return stockName;
    }

    public double getOpen() {
        return open;
    }

    public double getHigh() {
        return high;
    }

    public double getLow() {
        return low;
    }

    public double getClose() {
        return close;
    }

    public double getAdjClose() {
        return adjClose;
    }
}
