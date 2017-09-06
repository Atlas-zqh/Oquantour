package data.dataImpl;

import data.dataHelper.CodeNameRelation;
import data.dataHelper.StockInfoReader;
import data.dataHelper.ThreadHelper;
import data.dataService.TransactionDateDataService;
import po.Stock;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by keenan on 07/03/2017.
 */
public class TransactionDateDataServiceImpl implements TransactionDateDataService {
    private StockInfoReader stockInfoReader;
    private CodeNameRelation codeNameRelation;
    private Map<String, String> map_code_name = null;
    private List<String> values = null;
    private StockDataServiceImpl stockDataService;

    public TransactionDateDataServiceImpl() {
        codeNameRelation = CodeNameRelation.getInstance();
        stockInfoReader = new StockInfoReader();
        stockDataService = new StockDataServiceImpl();

        if (map_code_name == null) {
            map_code_name = codeNameRelation.getCodeNameRelation();
        }
        if (values == null) {
            values = codeNameRelation.getValues();
        }
    }

    /**
     * 得到某一日的前/后n个交易日的股票数据
     *
     * @param days n的值
     * @param date 某一日
     * @return 前n个交易日（若没有，则为空）
     */
    @Override
    public List<Stock> getDaysBeforeByCode(int days, Date date, String code) {
        List<Stock> stocksByCode = stockInfoReader.readStockInfo(0, code);

        List<Stock> stockList = new ArrayList<>();

        int currentDayIndex = 0;
        for (; currentDayIndex < stocksByCode.size(); currentDayIndex++) {
            if (stocksByCode.get(currentDayIndex).getDate().equals(date)) {
                break;
            }
        }

        if (currentDayIndex + days >= stocksByCode.size() || currentDayIndex + days <= 0) {
            return null;
        } else {
//            return stocksByCode.get(i + days).getDate();
            for (int i = currentDayIndex+1; i <= currentDayIndex + days; i++) {
                stockList.add(stocksByCode.get(i));
            }
        }

        return stockList;
    }

    /**
     * 根据日期得到这个交易日和前一个交易日的数据
     * 如果这个交易日之前没有交易日，则返回的Stock[]长度为1
     * 如果这个交易日之前有交易日，则返回的Stock[]长度为2
     * 如果这一日不是交易日，或某个股票这一日没有交易数据，则返回的Stock[]长度为0
     *
     * @param date 要查询的日期
     * @return 791个股票的当前交易日和前一交易日的信息
     */
    @Override
    public List<Stock[]> getStockOfTodayAndYesterday(Date date) {

        // 参数为2、3、4、5、6时都是平均3秒内
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        List<Future<Stock[]>> resultList = new LinkedList<>();


        for (int i = 0; i < values.size(); i++) {
            Future<Stock[]> future = executorService.submit(new ThreadHelper(0, values.get(i), date));
            resultList.add(future);
        }

        List<Stock[]> result = new LinkedList<>();

        for (Future<Stock[]> future : resultList) {
            try {
                result.add(future.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } finally {
                executorService.shutdown();
            }
        }
        return result;
    }

    /**
     * 得到某只股票所有的交易日
     *
     * @param stock 股票代码/名称
     * @return 所有的交易日
     */
    @Override
    public List<Date> getAllTransactionDays(String stock) {
        List<String> stockList = new ArrayList<>();
        stockList.add(stock);
        List<Stock> stocks = stockInfoReader.readStockInfo(0, stockDataService.getStockCode(stockList).get(0));
        List<Date> dateList = new ArrayList<>();
        for (Stock stock1 : stocks) {
            dateList.add(stock1.getDate());
        }
        return dateList;
    }
}
