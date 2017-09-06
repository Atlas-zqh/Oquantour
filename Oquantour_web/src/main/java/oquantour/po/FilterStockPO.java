package oquantour.po;

import java.util.List;

/**
 * 此类PO为数据层将筛选后的股票返回给界面
 * Created by Pxr on 2017/3/28.
 */
public class FilterStockPO {
    //有问题的名字列表
    private List<String> stockWithProblem;
    //没问题的股票列表
    private List<List<StockPO>> stockWithoutProblem;
    //无形成期的正常股票
    private List<List<StockPO>> stockWithOutFormativePeriod;



    public List<String> getStockWithProblem() {
        return stockWithProblem;
    }

    public List<List<StockPO>> getStockWithoutProblem() {
        return stockWithoutProblem;
    }

    public FilterStockPO(List<String> stockWithProblem, List<List<StockPO>> stockWithoutProblem) {

        this.stockWithProblem = stockWithProblem;
        this.stockWithoutProblem = stockWithoutProblem;
    }

    public List<List<StockPO>> getStockWithOutFormativePeriod() {
        return stockWithOutFormativePeriod;
    }

    public FilterStockPO(List<String> stockWithProblem, List<List<StockPO>> stockWithoutProblem, List<List<StockPO>> stockWithOutFormativePeriod) {

        this.stockWithProblem = stockWithProblem;
        this.stockWithoutProblem = stockWithoutProblem;
        this.stockWithOutFormativePeriod = stockWithOutFormativePeriod;
    }
}
