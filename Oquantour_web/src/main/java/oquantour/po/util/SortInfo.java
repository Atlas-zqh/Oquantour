package oquantour.po.util;

import oquantour.po.StockPO;

/**
 * Created by keenan on 12/05/2017.
 */
public class SortInfo {
    private String stockCode;

    private StockPO stock;

    private double value;

    public SortInfo(StockPO stock, double value) {
        this.stock = stock;
        this.value = value;
    }

    public SortInfo(String stockCode, double value) {
        this.stockCode = stockCode;
        this.value = value;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public StockPO getStock() {
        return stock;
    }

    public void setStock(StockPO stock) {
        this.stock = stock;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
