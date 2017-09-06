package bl.stock;

import bl.tools.Calculator;
import bl.tools.ComparatorChg;
import data.dataImpl.StockDataServiceImpl;
import data.dataImpl.StockPlateDataServiceImpl;
import data.dataImpl.TransactionDateDataServiceImpl;
import exception.BothStockNotExistException;
import exception.FirstStockNotExistException;
import exception.NotTransactionDayException;
import exception.SecondStockNotExistException;
import po.CalMeanPO;
import po.SearchPO;
import po.Stock;
import vo.*;

import java.text.DecimalFormat;
import java.util.*;

/**
 * 实现bl层的接口
 * Created by Pxr on 2017/3/3.
 */
public class StockBL implements StockBLService {
    //数据层的实现
    private StockDataServiceImpl stockDataService;
    private TransactionDateDataServiceImpl transactionDateDataService;
    private StockPlateDataServiceImpl stockPlateDataService;

    private MarketTemperatureVO marketTemperatureVO = null;

    //构造函数
    public StockBL() {
        stockDataService = new StockDataServiceImpl();
        transactionDateDataService = new TransactionDateDataServiceImpl();
        stockPlateDataService = new StockPlateDataServiceImpl();
    }

    /**
     * 得到画k线所需要的信息
     *
     * @param searchVO 搜索对象
     * @return 画k线所需要的vo对象
     */
    public List<KLineVO> getKLineInfo(SearchVO searchVO) throws FirstStockNotExistException {
        //调用私有方法，搜索股票
        List<Stock> stockList = stockDataService.searchStock(new SearchPO(searchVO.stock1, searchVO.startDate, searchVO.endDate));
        //若用户未搜索到股票
        if (stockList.isEmpty()) {
            throw new FirstStockNotExistException();//抛出异常
        } else {//否则，循环添加list
            List<KLineVO> kLineVOList = new ArrayList<>();
            stockList.stream().forEach(stock -> kLineVOList.add(new KLineVO(stock)));
            return kLineVOList;
        }
    }

    /**
     * 得到画均线所需要的信息
     *
     * @param searchVO 搜索对象
     * @return 画5天，10天，30天，60天，120天和240天均线所需要的信息的list组成的list
     */
    public List<List<DailyAverageVO>> getDailyAverageInfo(SearchVO searchVO) {
        List<List<DailyAverageVO>> lists = new ArrayList<>();
        int a[] = {//均线类型
                5, 10, 30, 60, 120, 240
        };
        for (int i : a) {
            if (getDailyAverage(searchVO, i) != null && !getDailyAverage(searchVO, i).isEmpty())
                lists.add(getDailyAverage(searchVO, i));
        }
        return lists;
    }

    /**
     * 比较两种股票的信息
     * 包括但不局限于这两只股票这段时间的最低值、最高
     * 值、涨幅/跌幅、每天的收盘价和对数收益率、对数收益率方差。
     *
     * @param searchVO 搜索对象
     * @return 比较的两只股票的数据对象组成的list
     */
    public List<StockCompareVO> compareStock(SearchVO searchVO) throws FirstStockNotExistException, SecondStockNotExistException, BothStockNotExistException {
        List<Stock> list1 = stockDataService.searchStock(new SearchPO(searchVO.stock1, searchVO.startDate, searchVO.endDate));
        List<Stock> list2 = stockDataService.searchStock(new SearchPO(searchVO.stock2, searchVO.startDate, searchVO.endDate));
        if (list1.isEmpty() && list2.isEmpty()) {
            throw new BothStockNotExistException();
        } else if (list1.isEmpty()) {
            throw new FirstStockNotExistException();
        } else if (list2.isEmpty()) {
            throw new SecondStockNotExistException();
        }
        double max1 = calMax(list1);//最大值
        double max2 = calMax(list2);
        double min1 = calMin(list1);//最小值
        double min2 = calMin(list2);

        List<Double> closeList1 = getClose(list1);
        List<Double> closeList2 = getClose(list2);

        List<Double> logReturnList1 = calLogReturn(list1);
        List<Double> logReturnList2 = calLogReturn(list2);

        double logReturnVariance1 = calLogReturnVariance(logReturnList1);//对数收益率方差
        double logReturnVariance2 = calLogReturnVariance(logReturnList2);

        List<Date> dateList1 = getDate(list1);
        List<Date> dateList2 = getDate(list2);

        List<StockCompareVO> compareVOS = new ArrayList<>();

        compareVOS.add(new StockCompareVO(max1, min1, logReturnVariance1, list1.get(0).getName(), list1.get(0).getCode(), calChg(list1), closeList1, dateList1, logReturnList1));
        compareVOS.add(new StockCompareVO(max2, min2, logReturnVariance2, list2.get(0).getName(), list2.get(0).getCode(), calChg(list2), closeList2, dateList2, logReturnList2));

        return compareVOS;
    }

    /**
     * 获得市场温度计所需要的信息
     * 若那天不存在，抛出异常
     *
     * @param searchVO 日期
     * @return 市场温度计
     */
    public MarketTemperatureVO getMarketInfo(SearchVO searchVO) throws NotTransactionDayException {
        List<Stock> list = stockDataService.searchAllStocksByDate(new SearchPO("", searchVO.startDate, searchVO.startDate));
        if (list.isEmpty()) {
            throw new NotTransactionDayException();
        } else {
            List<StockVO> upList = new ArrayList<>();//涨的股票列表
            List<StockVO> downList = new ArrayList<>();//跌的股票列表
            List<StockVO> limitUpStock = new ArrayList<>();//涨停股票列表
            List<StockVO> limitDownStock = new ArrayList<>();//跌停股票列表
            List<StockVO> up5perStock = new ArrayList<>();//涨幅超过5%的股票
            List<StockVO> down5perStock = new ArrayList<>();//跌幅超过5%的股票
            List<StockVO> openUp5perStock = new ArrayList<>();
            List<StockVO> openDown5perStock = new ArrayList<>();

            long totalVolume = 0;//总交易量

            for (Stock stock : list) {
                totalVolume += stock.getVolume();
            }

            List<Stock[]> yesterdayAndTodayStock = transactionDateDataService.getStockOfTodayAndYesterday(searchVO.startDate);

            for (Stock[] s : yesterdayAndTodayStock) {//此处调用数据层的方法，返回一个list,其中每一项是一个stock数组，第一个是上一个交易日，第二个是今日

                if (s.length != 2) {//如果数组的长度不是2，跳过此轮
                    continue;
                }
                if (s[1].getVolume() == 0) {
                    continue;
                }

                double chg;//涨／跌幅

                chg = (s[1].getAdjClose() - s[0].getAdjClose()) / s[0].getAdjClose();
                if (chg > 10.5 / 100 || chg < -10.5 / 100) {
                    continue;
                }

                if (Math.abs(s[1].getAdjClose() - s[0].getAdjClose()) * 100 / s[0].getAdjClose() < (10 - 0.01 * 100 / s[0].getAdjClose())) {
                    continue;
                }

                //开盘-收盘大于5%*上一个交易日收盘价的股票个数
                if ((s[1].getOpen() - s[1].getClose()) > 0.05 * s[0].getClose()) {
                    openUp5perStock.add(new StockVO(s[1], chg));
                } else if ((s[1].getOpen() - s[1].getClose()) < -0.05 * s[0].getClose()) {
                    openDown5perStock.add(new StockVO(s[1], chg));
                }

                if (chg > 0.05) {//涨幅超过5%
                    up5perStock.add(new StockVO(s[1], chg));
                }
                if (chg < -0.05) {//跌幅超过5%
                    down5perStock.add(new StockVO(s[1], chg));
                }
                boolean isSTStock = false;
                if (s[1].getName().contains("ST")) {
                    isSTStock = true;
                }

                if (isSTStock && chg > 0.05) {
                    limitUpStock.add(new StockVO(s[1], chg));
                } else if (isSTStock && chg < -0.05) {
                    limitDownStock.add(new StockVO(s[1], chg));
                } else if (chg >= 0.1) {//涨停，即涨幅超过10%
                    limitUpStock.add(new StockVO(s[1], chg));
                } else if (chg <= -0.1) {//跌停，即跌幅超过10%
                    limitDownStock.add(new StockVO(s[1], chg));
                }

                if (chg > 0) {
                    upList.add(new StockVO(s[1], chg));
                }
                if (chg < 0) {
                    downList.add(new StockVO(s[1], chg));
                }
            }
            ComparatorChg comparatorChg = new ComparatorChg();
            upList.sort(comparatorChg);
            downList.sort(comparatorChg);
            Collections.reverse(downList);


            marketTemperatureVO = new MarketTemperatureVO(totalVolume, limitUpStock, limitDownStock, up5perStock, down5perStock, openUp5perStock, openDown5perStock, list.size(), upList, downList);

            marketTemperatureVO.date = searchVO.startDate;

            return marketTemperatureVO;
        }
    }

    @Override
    public MarketTemperatureVO getMarketInfoInPlate(String plateName, Date date) throws NotTransactionDayException {
        MarketTemperatureVO temp = marketTemperatureVO;
        if (plateName.equals("市场") && date.equals(temp.date)) {
            return marketTemperatureVO;
        } else if (!date.equals(marketTemperatureVO.date) && plateName.equals("市场")) {
            marketTemperatureVO = getMarketInfo(new SearchVO(date));
            return marketTemperatureVO;
        } else if (!date.equals(temp.date) && !plateName.equals("市场")) {
            marketTemperatureVO = getMarketInfo(new SearchVO(date));
        }
        List<String> strings = new ArrayList<>();
        for (String s : getAllStockCodeAndNameInPlate(plateName)) {
            strings.add(s.split(";")[0]);
        }
        List<StockVO> upList = filterStockInPlate(strings, marketTemperatureVO.upList);//涨的股票列表
        List<StockVO> downList = filterStockInPlate(strings, marketTemperatureVO.downList);//跌的股票列表
        List<StockVO> limitUpStock = filterStockInPlate(strings, marketTemperatureVO.limitUpStock);//涨停股票列表
        List<StockVO> limitDownStock = filterStockInPlate(strings, marketTemperatureVO.limitDownStock);//跌停股票列表
        List<StockVO> up5perStock = filterStockInPlate(strings, marketTemperatureVO.up5perStock);//涨幅超过5%的股票
        List<StockVO> down5perStock = filterStockInPlate(strings, marketTemperatureVO.down5perStock);//跌幅超过5%的股票
        List<StockVO> openUp5perStock = filterStockInPlate(strings, marketTemperatureVO.openUp5perStock);
        List<StockVO> openDown5perStock = filterStockInPlate(strings, marketTemperatureVO.openDown5perStock);

        long totalVolume = 0;
        for (StockVO stockVO : upList) {
            totalVolume += stockVO.volume;
        }
        for (StockVO stockVO : downList) {
            totalVolume += stockVO.volume;
        }
        return new MarketTemperatureVO(totalVolume, limitUpStock, limitDownStock, up5perStock, down5perStock, openUp5perStock, openDown5perStock, upList.size() + downList.size(), upList, downList);
    }

    private List<StockVO> filterStockInPlate(List<String> strings, List<StockVO> stockVOS) {
        List<StockVO> stockVOS1 = new ArrayList<>();
        for (StockVO stockVO : stockVOS) {
            if (strings.contains(stockVO.code)) {
                stockVOS1.add(stockVO);
            }
        }
        return stockVOS1;
    }

    /**
     * 为了画温度计，设计算法类比市场温度
     *
     * @param marketTemperatureVO 当日市场信息
     * @return 温度
     */
    public double getTemperature(MarketTemperatureVO marketTemperatureVO) throws NotTransactionDayException {
        if (marketTemperatureVO.totalNum == 0) {
            throw new NotTransactionDayException();
        } else {
            double upPercent = marketTemperatureVO.upList.size() / marketTemperatureVO.totalNum;//当日上涨股票占总股票的百分比
            double downPercent = marketTemperatureVO.downList.size() / marketTemperatureVO.totalNum;
            double temperature = 0.0;
            temperature += (marketTemperatureVO.limitUpStock.size() - marketTemperatureVO.limitDownStock.size()) * 0.5;//温度加上当日涨停股票的数量，减去跌停股票的数量
            temperature += (upPercent - downPercent) * 0.3;
            temperature += (marketTemperatureVO.up5perStock.size() - marketTemperatureVO.down5perStock.size()) * 0.5;
            DecimalFormat decimalFormat = new DecimalFormat("#.0");

            return Double.parseDouble(decimalFormat.format(temperature));
        }
    }


    /**
     * 获得某一种时间的均线
     *
     * @param searchVO 搜索信息
     * @param days     时间长度
     * @return 某种时间均线的list
     */
    public List<DailyAverageVO> getDailyAverage(SearchVO searchVO, int days) {
        //调用私有方法，搜索股票
        CalMeanPO calMeanPO = stockDataService.getStockListForMean(new SearchPO(searchVO.stock1, searchVO.startDate, searchVO.endDate), days);
        List<Stock> stockList = calMeanPO.getStockList();
        //若用户未搜索到股票
        if (stockList.isEmpty()) {
            return new ArrayList<>();
        } else {
            List<Stock> beforeStock = calMeanPO.getStockBeforeList();//获得前days-1天的数据
            List<DailyAverageVO> list = new ArrayList<>();
            if (!beforeStock.isEmpty()) {//若存在前days个交易日，从搜索的第一个交易日开始放入前days个交易日的list
                beforeStock.addAll(stockList);

                if (days <= beforeStock.size()) {//要满足交易日的长度大于days
                    for (int i = days - 1, size = beforeStock.size(); i < size; i++) {//从第一个交易日开始计算
                        list.add(new DailyAverageVO(calAverage(beforeStock, i, days), beforeStock.get(i).getDate()));
                    }
                }
            } else {//若不存在前days个交易日，则从第days个交易日开始计算
                if (days <= stockList.size()) {//要满足交易日的长度大于days
                    for (int i = days - 1, size = stockList.size(); i < size; i++) {
                        list.add(new DailyAverageVO(calAverage(stockList, i, days), stockList.get(i).getDate()));
                    }
                }
            }

            return list;
        }
    }

    /**
     * 计算一个list中某一天的均值
     *
     * @param list  股票信息组成的list
     * @param start 开始的位置
     * @param days  多少天的均值
     * @return 均值
     */
    private double calAverage(List<Stock> list, int start, int days) {
        List<Double> dbList = new ArrayList<>();

        for (int i = start, j = start - days; i > j; i--) {
            dbList.add(list.get((i)).getClose());
        }
        return Calculator.avg(dbList);
    }

    /**
     * 计算股票list中股价的最大值
     *
     * @param list 股票列表
     * @return 最大值
     */
    private double calMax(List<Stock> list) {
        List<Double> dbList = new ArrayList<>();

        list.stream().forEach(stock -> dbList.add(stock.getHigh()));

        return Calculator.max(dbList);
    }

    /**
     * 计算股票list中股价的最小值
     *
     * @param list 股票列表
     * @return 最小值
     */
    private double calMin(List<Stock> list) {
        List<Double> dbList = new ArrayList<>();

        list.stream().forEach(stock -> dbList.add(stock.getLow()));

        return Calculator.min(dbList);
    }

    /**
     * 计算某一只股票的涨幅／跌幅 （跌幅为负的涨幅），最后一天的第一天的比较
     *
     * @param list 股票列表
     * @return 涨幅
     */
    private double calChg(List<Stock> list) {
        return (list.get(list.size() - 1).getAdjClose() - list.get(0).getAdjClose()) / list.get(0).getAdjClose();
    }

    /**
     * 计算每日的对数收益率,如果上一个交易日不存在，则返回0.0
     *
     * @param list 股票列表
     * @return 对数收益率
     */
    private List<Double> calLogReturn(List<Stock> list) {
        List<Double> doubleList = new ArrayList<>();
        if (!getBeforeStock(1, list.get(0)).isEmpty()) {
            Stock yesterdayStock = getBeforeStock(1, list.get(0)).get(0);//第一天前一个交易日的股票信息
            doubleList.add(Math.log(list.get(0).getAdjClose() / yesterdayStock.getAdjClose()));
        } else
            doubleList.add(0.0);
        for (int i = 1; i < list.size(); i++) {
            doubleList.add(Math.log(list.get(i).getAdjClose() / list.get(i - 1).getAdjClose()));
        }
        return doubleList;
    }


    /**
     * 计算对数收益率的方差
     *
     * @param list 对数list
     * @return 对数收益率方差
     */
    private double calLogReturnVariance(List<Double> list) {
        return Calculator.variance(list);
    }

    /**
     * 获得某个交易日之前的股票
     *
     * @param days  交易日数量
     * @param stock 股票信息
     * @return 那一段时间的股票
     */
    private List<Stock> getBeforeStock(int days, Stock stock) {

        List<Stock> stocks = transactionDateDataService.getDaysBeforeByCode(days, stock.getDate(), stock.getCode());

        return (stocks == null) ? new ArrayList<>() : stocks;
    }


    /**
     * 得到每日的收盘价
     *
     * @param list 股票列表
     * @return 每日的关盘价
     */
    private List<Double> getClose(List<Stock> list) {
        List<Double> doubleList = new ArrayList<>();
        list.stream().forEach(stock -> doubleList.add(stock.getClose()));
        return doubleList;
    }

    /**
     * 得到股票每个交易日的日期
     *
     * @param list 股票列表
     * @return 每个交易日的日期
     */
    private List<Date> getDate(List<Stock> list) {
        List<Date> dateList = new ArrayList<>();
        list.stream().forEach(stock -> dateList.add(stock.getDate()));
        return dateList;
    }

    /**
     * 根据股票代码和日期判断该日是否为该股票的交易日
     *
     * @param stockCode
     * @param date
     * @return 判断结果
     */
    public boolean isTransactionDay(String stockCode, Date date) {
        List<Date> dateList = transactionDateDataService.getAllTransactionDays(stockCode);
        return dateList.contains(date);
    }

    /**
     * 得到所有的股票代码和名字，格式为"名字;代码"
     *
     * @return 所有的股票代码和名字，格式为"名字;代码"
     */
    @Override
    public List<String> getAllStockCodeAndName() {
        return stockDataService.getAllStockCodeAndName();
    }

    /**
     * 根据板块名获得该板块中的所有股票的代码和名字
     *
     * @param plate 板块名
     * @return 该板块中的所有股票的代码和名字
     */
    public List<String> getAllStockCodeAndNameInPlate(String plate) {
        return stockPlateDataService.getAllStockInfoInPlate(plate);
    }

}
