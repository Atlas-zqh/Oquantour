package data.dataImpl;

import data.dataHelper.CodeNameRelation;
import data.dataHelper.StockInfoReader;
import data.dataService.StockDataService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import po.CalMeanPO;
import po.FilterStockPO;
import po.SearchPO;
import po.Stock;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by keenan on 04/03/2017.
 */
public class StockDataServiceImpl implements StockDataService {
    private List<Stock> stockList;

    // 储存股票号和股票名称的map，在根据姓名搜索时，将名字转换为股票号
    private Map<String, String> map_code_name = null;

    private StockInfoReader stockInfoReader;

    private CodeNameRelation codeNameRelation;

    private Map<String, String> map_name_code = null;

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yy");

    public StockDataServiceImpl() {
        stockList = new ArrayList<>();
        stockInfoReader = new StockInfoReader();

        if (codeNameRelation == null) {
            codeNameRelation = CodeNameRelation.getInstance();
        }

        if (map_name_code == null) {
            map_name_code = codeNameRelation.getNameCodeRelation();
        }

        if (map_code_name == null) {
            map_code_name = codeNameRelation.getCodeNameRelation();
        }
    }

//    /**
//     * 根据股票名称和日期范围查询股票数据
//     *
//     * @param searchPO 要使用的属性包括"stockName","startDate","endDate"
//     * @return 搜索结果
//     */
//    public List<Stock> searchStockByNameAndDateRange(SearchPO searchPO) {
//        String code = map_code_name.get(searchPO.getStockInfo());
//
//        if (code == null) {
//            return new ArrayList<>();
//        }
//
//        SearchPO newPo = new SearchPO(code, searchPO.getStartDate(), searchPO.getEndDate());
//
//        return searchStock(newPo);
//    }


    /**
     * 根据股票代码和日期范围查询股票数据
     *
     * @param searchPO 要使用的属性包括"stockCode","startDate","endDate"
     * @return 搜索结果
     */
    public List<Stock> searchStock(SearchPO searchPO) {
        List<String> stockCode = new ArrayList<>();
        stockCode.add(searchPO.getStockInfo());
        List<Stock> possibleStocks = stockInfoReader.readStockInfo(0, getStockCode(stockCode).get(0));
        List<Stock> stocksMatch = new ArrayList<>();
        for (int i = 0; i < possibleStocks.size(); i++) {
            Stock stock = possibleStocks.get(i);
            Date thisDate = stock.getDate();
            Date startDate = searchPO.getStartDate();
            Date endDate = searchPO.getEndDate();

            if (thisDate.equals(startDate) || thisDate.equals(endDate) || (thisDate.after(startDate) && thisDate.before(endDate))) {
                stocksMatch.add(stock);
            }
        }
        Collections.reverse(stocksMatch);
        return stocksMatch;
    }

    /**
     * 根据日期搜索市场中所有股票的信息
     *
     * @param searchPO 要使用的属性仅包括startDate和endDate
     *                 endDate和startDate必须相同，否则将返回一个空的列表
     * @return 搜索结果
     */
    public List<Stock> searchAllStocksByDate(SearchPO searchPO) {
        ArrayList<Stock> stocksFound = new ArrayList<>();

        if (!searchPO.getStartDate().equals(searchPO.getEndDate())) {
            return stocksFound;
        }

        Date searchDate = searchPO.getEndDate();

        List<Stock> stocksByYear = stockInfoReader.readStockInfo(1, String.valueOf(searchDate.getYear()));

        if (stocksByYear.isEmpty()) {
            return stocksFound;
        }

        for (Stock stock : stocksByYear) {
            if (stock.getDate().equals(searchDate)) {
                stocksFound.add(stock);
            }
        }

        return stocksFound;
    }

    /**
     * 得到股票代码（无论传入的是股票名还是股票代码）
     *
     * @param stockInfoList 股票名或股票代码
     * @return 股票代码
     */
    @Override
    public List<String> getStockCode(List<String> stockInfoList) {
        // 股票号码的正则表达式
        String stockCodeCriteria = "^[0-9]*$";
        // 转化后的股票号码
        List<String> codeList = new ArrayList<>();

        for (String s : stockInfoList) {
            if (s.matches(stockCodeCriteria)) {
                codeList.add(s);
            } else {
                String code = map_code_name.get(s);
                if (code == null) {
                    continue;
                }
                codeList.add(map_code_name.get(s));
            }
        }
        return codeList;
    }

    public String getStockCode(String stockInfo) {
        String stockCodeCriteria = "^[0-9]*$";
        if (stockInfo.matches(stockCodeCriteria)) {
            return stockInfo;
        } else {
            String code = map_code_name.get(stockInfo);
            if (code == null) {
                return null;
            }
            return (map_code_name.get(stockInfo));
        }
    }

    /**
     * 得到所有的股票代码和名字，格式为"名字;代码"
     *
     * @return 所有的股票代码和名字，格式为"名字;代码"
     */
    public List<String> getAllStockCodeAndName() {
        BufferedReader bufferedReader = null;
        String temp = System.getProperty("user.dir") + "/datasource/allStocks.txt";
//        String temp = String.valueOf(getClass().getResource("/"));
        File allStocks = new File(temp);
//        System.out.println("=============================="+temp+"===============================");
        List<String> strings = new ArrayList<>();
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(allStocks)));
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                strings.add(str);
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strings;
    }

    /**
     * 筛选掉没有数据的股票，如果不需要形成期长度，那长度就是0，如果不需要均值日期长度，那长度就是0
     *
     * @param stockList       股票列表
     * @param startDate       开始日期
     * @param endDate         结束日期
     * @param formativePeriod 形成期长度
     * @return 有数据的股票和没有数据的股票
     */
    public FilterStockPO filterStockWithoutData(List<String> stockList, Date startDate, Date endDate, int formativePeriod) {
        List<String> stockWithProblem = new ArrayList<>();
        List<List<Stock>> stockWithoutProblem = new ArrayList<>();
        List<Date> transactionDateList = stockInfoReader.readTransactionDays();
        Date formativeStartDay = transactionDateList.get(transactionDateList.indexOf(getTransactionDateAfterStartDate(startDate)) + formativePeriod);//形成期开始的日期
        if (formativeStartDay == null) {
            return null;
        } else {
            Map<String, List<Stock>> map = stockInfoReader.readManyStocks(stockList, formativeStartDay, endDate);
            for (String s : map.keySet()) {
                if (map.get(s).size() > formativePeriod) {
                    stockWithoutProblem.add(map.get(s));
                } else {
                    stockWithProblem.add(s + ";" + map_name_code.get(s));
                }
            }
            return new FilterStockPO(stockWithProblem, stockWithoutProblem);
        }
    }

    /**
     * 筛选掉停牌的股票
     *
     * @param stockList       股票列表
     * @param startDate       开始日期
     * @param endDate         结束日期
     * @param formativePeriod 形成期长度
     * @return 停牌的股票和没有停牌的股票
     */
    public FilterStockPO filterStockHalt(List<List<Stock>> stockList, Date startDate, Date endDate, int formativePeriod) {
        List<String> stockWithProblem = new ArrayList<>();
        List<List<Stock>> stockWithoutProblem = new ArrayList<>();
//        List<Date> transactionDateList = stockInfoReader.readTransactionDays();
//        Date formativeStartDay = transactionDateList.get(transactionDateList.indexOf(getTransactionDateAfterStartDate(startDate)) + formativePeriod);
//        int transactionDays = 0;
//        for (Date date : transactionDateList) {//目的是得出开始日期到结束日期之间交易日的长度
//
//            if ((date.equals(endDate) || date.before(endDate)) && (date.equals(formativeStartDay) || date.after(formativeStartDay))) {
//                transactionDays++;
//            }
//        }
//        for (List<Stock> stocks : stockList) {
//            if (stocks.size() > 0) {
////                System.out.println(stocks.size()+" "+stocks.get(0).getName()+" "+stocks.get(0).getDate());
//                if (stocks.size() == transactionDays)
//                    stockWithoutProblem.add(stocks);
//                else
//                    stockWithProblem.add(stocks.get(0).getCode()+";"+stocks.get(0).getName());
//            }
//        }
        List<List<Stock>> stockWithOutFormativePeriod = new ArrayList<>();

        for (List<Stock> stocks : stockList) {
            boolean isHalt = false;
            for (Stock stock : stocks) {
                if (stock.getVolume() == 0) {
                    stockWithProblem.add(stock.getCode() + ";" + stock.getName());
                    isHalt = true;
                    break;
                }
            }
            if (!isHalt) {
                stockWithoutProblem.add(stocks);
                List<Stock> temp = new ArrayList<>();
                for (Stock s : stocks) {
                    if (!s.getDate().before(startDate))
                        temp.add(s);
                }
                stockWithOutFormativePeriod.add(temp);
            }

        }

        return new FilterStockPO(stockWithProblem, stockWithoutProblem, stockWithOutFormativePeriod);
    }

    /**
     * 获得开始日期后的第一个交易日
     *
     * @param startDate 开始日期
     * @return 开始日期后的第一个交易日
     */
    private Date getTransactionDateAfterStartDate(Date startDate) {
        List<Date> transactionDateList = stockInfoReader.readTransactionDays();
        for (int i = 0; i < transactionDateList.size() - 1; i++) {
            if (startDate.after(transactionDateList.get(i + 1)) && startDate.before(transactionDateList.get(i)))
                return transactionDateList.get(i);
            else if (startDate.equals(transactionDateList.get(i)))
                return startDate;
        }
        return null;
    }

    /**
     * 得到一段时间内所有股票信息
     *
     * @param searchPO 开始日期，结束日期
     * @return 所有股票信息
     */
    public List<List<Stock>> getAllStockInfo(SearchPO searchPO) {
        List<List<Stock>> allStockList = new ArrayList<>();
        List<String> allCodeAndName = getAllStockCodeAndName();
        for (String str : allCodeAndName) {
            allStockList.add(searchStock(new SearchPO(str.split(";")[0], searchPO.getStartDate(), searchPO.getEndDate())));
        }
        return allStockList;
    }

    public CalMeanPO getStockListForMean(SearchPO searchPO, int days) {
        if (getStockCode(searchPO.getStockInfo()) == null) {
            return new CalMeanPO(new ArrayList<>(), new ArrayList<>());
        }
        String code = getStockCode(searchPO.getStockInfo());
        File f = new File(System.getProperty("user.dir") + "/datasource/stocks/" + code.substring(0, 1) + "/" + code + ".txt");
        try {
            List<String> strings = FileUtils.readLines(f);
            List<Stock> stockList = new ArrayList<>();
            List<Stock> beforeStockList = new ArrayList<>();
            int startIndex = 0;
            for (int i = 0, size = strings.size(); i < size; i++) {
                String[] s = StringUtils.split(strings.get(i), ";");
                Date date = simpleDateFormat.parse(s[1]);
                if (date.before(searchPO.getStartDate())) {
                    startIndex = i;
                    break;
                } else if (!date.after(searchPO.getEndDate())) {
                    stockList.add(new Stock(strings.get(i), date));
                }
            }
            Collections.reverse(stockList);
            if (startIndex != 0) {
                for (int j = startIndex, size = strings.size(); j < startIndex + days - 1 && j < size; j++) {
                    beforeStockList.add(new Stock(strings.get(j), simpleDateFormat.parse(StringUtils.split(strings.get(j), ";")[1])));
                }
            }
            Collections.reverse(beforeStockList);
            return new CalMeanPO(stockList, beforeStockList);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException p) {
            p.printStackTrace();
        }
        return null;
    }
}
