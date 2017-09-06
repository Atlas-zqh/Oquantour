package oquantour.po.util;

import org.apache.poi.ss.formula.functions.T;

/**
 * Created by island on 5/10/17.
 */
public class StockNameInfo {
    //股票ID
    private String stockID;
    //股票名称
    private String stockName;

    public StockNameInfo(String stockID, String stockName) {
        this.stockID = stockID;
        this.stockName = stockName;
    }

    public String getStockID() {
        return stockID;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockID(String stockID) {
        this.stockID = stockID;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
