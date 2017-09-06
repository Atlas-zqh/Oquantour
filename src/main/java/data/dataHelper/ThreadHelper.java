package data.dataHelper;

import po.Stock;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by keenan on 12/03/2017.
 */
public class ThreadHelper implements Callable<Stock[]> {
    private StockInfoReader stockInfoReader = new StockInfoReader();

    private Stock[] stocks = new Stock[0];

    private int flag;

    private String mark;

    private Date date;

    public ThreadHelper(int flag, String mark, Date date) {
        this.flag = flag;
        this.mark = mark;
        this.date = date;
    }

    /**
     * 线程调用的方法
     *
     * @return Stock数组
     */
    @Override
    public Stock[] call() {
        List<Stock> stockList = stockInfoReader.readStockInfo(flag, mark);

        int length = stockList.size();

        for (int i = 0; i < length; i++) {
            if (stockList.get(i).getDate().equals(date)) {
                if (i < length - 1) {
                    stocks = new Stock[2];
                    stocks[0] = stockList.get(i + 1);
                    stocks[1] = stockList.get(i);
                    break;
                } else {
                    stocks = new Stock[1];
                    stocks[0] = stockList.get(i);
                    break;
                }
            }
        }

        return stocks;
    }

}
