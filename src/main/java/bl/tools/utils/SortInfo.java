package bl.tools.utils;

/**
 * Created by st on 2017/3/25.
 * 策略中，选出赢家组合时会用到的信息类，用于排序
 */
public class SortInfo {

    //股票代码
    private String stockCode;

    private double value;

    public SortInfo(String stockCode, double value) {
        this.stockCode = stockCode;
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public String getStockCode() {
        return stockCode;
    }
}
