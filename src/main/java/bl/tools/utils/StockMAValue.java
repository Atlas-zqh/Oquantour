package bl.tools.utils;

import vo.DailyAverageVO;

import java.util.List;

/**
 * 均值回归策略中会使用
 * 存放了股票号码
 * 均线类型
 * 均线的数据
 * <p>
 * Created by keenan on 05/04/2017.
 */
public class StockMAValue {

    // 股票号码
    private String stockCode;
    // 均线数据
    private List<DailyAverageVO> ma_values;

    public StockMAValue(String stockCode, List<DailyAverageVO> ma_values) {
        this.stockCode = stockCode;
        this.ma_values = ma_values;
    }

    public String getStockCode() {
        return stockCode;
    }

    public List<DailyAverageVO> getMa_values() {
        return ma_values;
    }
}
