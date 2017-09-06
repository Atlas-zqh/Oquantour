package po;

import java.util.List;

/**
 * 此类PO为数据层将筛选后的股票返回给界面
 * Created by Pxr on 2017/3/28.
 */
public class FilterStockPO {
    //有问题的名字列表
    private List<String> stockWithProblem;
    //没问题的股票列表
    private List<List<Stock>> stockWithoutProblem;
    //无形成期的正常股票
    private List<List<Stock>> stockWithOutFormativePeriod;



    public List<String> getStockWithProblem() {
        return stockWithProblem;
    }

    public List<List<Stock>> getStockWithoutProblem() {
        return stockWithoutProblem;
    }

    public FilterStockPO(List<String> stockWithProblem, List<List<Stock>> stockWithoutProblem) {

        this.stockWithProblem = stockWithProblem;
        this.stockWithoutProblem = stockWithoutProblem;
    }

    public List<List<Stock>> getStockWithOutFormativePeriod() {
        return stockWithOutFormativePeriod;
    }

    public FilterStockPO(List<String> stockWithProblem, List<List<Stock>> stockWithoutProblem, List<List<Stock>> stockWithOutFormativePeriod) {

        this.stockWithProblem = stockWithProblem;
        this.stockWithoutProblem = stockWithoutProblem;
        this.stockWithOutFormativePeriod = stockWithOutFormativePeriod;
    }
}
