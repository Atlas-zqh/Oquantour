package po;

import java.util.List;

/**
 * Created by Pxr on 2017/4/14.
 */
public class CalMeanPO {
    private List<Stock> stockList;

    private List<Stock> stockBeforeList;

    public CalMeanPO(List<Stock> stockList, List<Stock> stockBeforeList) {
        this.stockList = stockList;
        this.stockBeforeList = stockBeforeList;
    }

    public List<Stock> getStockList() {
        return stockList;
    }

    public List<Stock> getStockBeforeList() {
        return stockBeforeList;
    }
}
