package bl.tools;

import po.Stock;

import java.util.ArrayList;
import java.util.List;

/**
 * 把List<List<Stock>>中的股票代码信息提取出来
 * Created by st on 2017/3/31.
 */
public class TransferNestedList {

    /**
     * 把List<List<Stock>>中的股票代码信息提取出来
     *
     * @param stocks
     * @return 股票代码列表
     */
    public static List<String> transfer(List<List<Stock>> stocks) {
        List<String> stockCodes = new ArrayList<>();
        for (List<Stock> stock : stocks) {
            stockCodes.add(stock.get(0).getCode());
        }
        return stockCodes;
    }
}
