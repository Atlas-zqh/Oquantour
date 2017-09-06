package oquantour.service.serviceImpl;

import oquantour.data.dao.BasicDao;
import oquantour.data.dao.IndustryDao;
import oquantour.data.dao.PlateDao;
import oquantour.data.dao.StockDao;
import oquantour.exception.InnerValueException;
import oquantour.exception.StockDataNotExistException;
import oquantour.exception.UnsupportedOperationException;
import oquantour.po.*;
import oquantour.po.util.ChartInfo;
import oquantour.po.util.Edge;
import oquantour.po.util.SortInfo;
import oquantour.po.util.StockInfo;
import oquantour.service.StockService;
import oquantour.service.servicehelper.analysis.industry.IndustryRelativity;
import oquantour.service.servicehelper.analysis.stock.StockPrediction;
import oquantour.service.servicehelper.analysis.stock.StockScore;
import oquantour.service.servicehelper.analysis.stock.StockValue;
import oquantour.service.servicehelper.analysis.stock.indices.*;
import oquantour.util.tools.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by keenan on 07/05/2017.
 */
@Service
@Transactional
public class StockServiceImpl implements StockService {
    @Autowired
    private StockDao stockDao;
    @Autowired
    private PlateDao plateDao;
    @Autowired
    private BasicDao basicDao;
    @Autowired
    private IndustryDao industryDao;

    private StockPrediction stockPrediction = new StockPrediction();

    private AdjAnalysis adjAnalysis = new AdjAnalysis();

    private Map<String, String> codeName = new HashMap<>();

    private Map<String, String> nameCode = new HashMap<>();

    private List<String> allStockCodeAndName = new ArrayList<>();

    private StockCache stockCache = new StockCache();

    private final String MONTHLY = "MONTHLY", WEEKLY = "WEEKLY";

    /**
     * 得到一只股票的数据（包括均线，投资建议，下一个交易日的股价预测）
     *
     * @param stockCode 股票代码
     * @return 股票信息
     */
    @Override
    @SuppressWarnings("Duplicates")
    public StockInfo getStockInfo(String stockCode) throws StockDataNotExistException {
        List<StockPO> stocks = stockCache.getStockData(stockCode);
        if (stocks == null || stocks.isEmpty()) {
            throw new StockDataNotExistException(stockCode);
        }

        String advice = adjAnalysis.getInvestmentSuggestion(stocks);
        MapUtil mapUtil = getAnalysis(stocks);

        List<Double> allOpen = new ArrayList<>();
        List<Double> allClose = new ArrayList<>();
        List<Double> allLow = new ArrayList<>();
        List<Double> allHigh = new ArrayList<>();
        List<Double> allAdjClose = new ArrayList<>();

        for (StockPO stockPO : stocks) {
            allOpen.add(stockPO.getOpenPrice());
            allClose.add(stockPO.getClosePrice());
            allLow.add(stockPO.getLowPrice());
            allHigh.add(stockPO.getHighPrice());
            allAdjClose.add(stockPO.getAdjClose());
        }

        double estimatedOpen = 0;
        double estimatedClose = 0;
        double estimatedLow = 0;
        double estimatedHigh = 0;
        double estimatedAdjClose = 0;

        try {
            ExecutorService pool = Executors.newFixedThreadPool(5);
            EstimateThread open = new EstimateThread(allOpen);
            EstimateThread close = new EstimateThread(allClose);
            EstimateThread high = new EstimateThread(allHigh);
            EstimateThread low = new EstimateThread(allLow);
            EstimateThread adjClose = new EstimateThread(allAdjClose);

            Future<Double> f1 = pool.submit(open);
            Future<Double> f2 = pool.submit(close);
            Future<Double> f3 = pool.submit(high);
            Future<Double> f4 = pool.submit(low);
            Future<Double> f5 = pool.submit(adjClose);

            estimatedOpen = f1.get();
            estimatedClose = f2.get();
            estimatedHigh = f3.get();
            estimatedLow = f4.get();
            estimatedAdjClose = f5.get();
        } catch (InterruptedException e) {
            estimatedOpen = -1;
            estimatedClose = -1;
            estimatedHigh = -1;
            estimatedLow = -1;
            estimatedAdjClose = -1;
        } catch (ExecutionException e) {
            estimatedOpen = -1;
            estimatedClose = -1;
            estimatedHigh = -1;
            estimatedLow = -1;
            estimatedAdjClose = -1;
        } finally {
            List<StockPO> weekly = generateData(stocks, WEEKLY);
            List<StockPO> monthly = generateData(stocks, MONTHLY);
            Map<String, StockInfoPO> map = basicDao.getStockInfo(stockCode);
            StockInfoPO basicInfo = map.get(stockCode);

            return new StockInfo(stocks, weekly, monthly, advice, estimatedOpen, estimatedClose, estimatedLow, estimatedHigh, estimatedAdjClose, basicInfo, mapUtil.getMap_buy(), mapUtil.getMap_sell());
        }
    }


    /**
     * 获得所有股票号和股票名
     *
     * @return 格式：  股票号;股票名
     */
    @Override
    public List<String> getAllStockCodeAndName() {
        if (allStockCodeAndName.isEmpty()) {
            allStockCodeAndName = stockDao.getAllStockCodeAndName();
        }
        return allStockCodeAndName;
    }

    /**
     * 获得相似股票推荐
     *
     * @param stockCode 股票代码
     * @return 相似股票列表
     */
    @Override
    @SuppressWarnings("Duplicates")
    public List<StockRealTimePO> getRecommendedStock(String stockCode) {
        // 这只股票的行业
        String industry = industryDao.getIndustryOfStock(stockCode).get(stockCode);
        // 所有的行业
        List<String> list = industryDao.getAllIndustries();
        // 获得行业最强关系队列
        String[] industries = list.toArray(new String[1]);
        Date today = CalendarUtil.getToday();
        Date oneYear = CalendarUtil.getDateOneYearBefore(today);
        List<List<Double>> rr = new ArrayList<>();
        Map<String, List<ChartInfo>> chartInfos = new HashMap<>();
        for (String s : industries) {
            List<ChartInfo> chartInfo = industryDao.getIndustryReturnRate(s, oneYear, today);
            chartInfos.put(s, chartInfo);
            List<Double> doubles = new ArrayList<>();
            chartInfo.stream().forEachOrdered(chartInfo1 -> doubles.add(chartInfo1.getyAxis()));
            rr.add(doubles);
        }
        PriorityQueue<Edge> edges = new IndustryRelativity(list, rr).getRelativity();
        // 找到与当前的行业有最强关系的行业
        List<SortInfo> maxIndustry = new ArrayList<>();
        for (Edge edge : edges) {
            if (edge.getIndustryA_Name().equals(industry)) {
                maxIndustry.add(new SortInfo(edge.getIndustryB_Name(), edge.getWeight()));
            } else if (edge.getIndustryB_Name().equals(industry)) {
                maxIndustry.add(new SortInfo(edge.getIndustryA_Name(), edge.getWeight()));
            } else {
                continue;
            }
        }
        // 按照相关性排序
        SortUtil.sortDescending(maxIndustry, sortInfo -> sortInfo.getValue());
        // 取出前3个行业
        List<String> maxIndustries = new ArrayList<>();
        int len = maxIndustry.size() < 3 ? maxIndustry.size() : 3;
        for (int i = 0; i < len; i++) {
            maxIndustries.add(maxIndustry.get(i).getStockCode());
        }
        // 取出前3个行业内的所有股票
        List<String> candidates = new ArrayList<>();
        for (String s : maxIndustries) {
            candidates.addAll(industryDao.getStockIDsInIndustry(s));
        }
        Map<String, List<StockPO>> stocks = new HashMap<>();
        // 取出候选股票的数据
        candidates.stream().forEach(s -> stocks.put(s, stockDao.searchStock(s, oneYear, today)));
        // 取出自己的数据
        List<StockPO> thisStock = stockDao.searchStock(stockCode, oneYear, today);
        List<SortInfo> sortInfos = new ArrayList<>();

        for (Map.Entry<String, List<StockPO>> entry : stocks.entrySet()) {
            // 同一只股票
            if (entry.getKey().equals(stockCode)) {
                continue;
            }

            // 数据量不同
            List<StockPO> tmpStock = entry.getValue();
            List<StockPO> tmpThisStock = new ArrayList<>(thisStock);
            if (tmpStock.size() != thisStock.size()) {
                // 规整数据量
                try {
                    formatData(tmpStock, tmpThisStock);
                } catch (UnsupportedOperationException e) {
                    continue;
                }
            }

            List<Double> thisLogReturnRate = getLogReturnRate(tmpThisStock);
            List<Double> tmpLogReturnRate = getLogReturnRate(tmpStock);
            double cov = Calculator.cov(thisLogReturnRate, tmpLogReturnRate);
            double relativity = cov / Math.sqrt(Calculator.variance(thisLogReturnRate) * Calculator.variance(tmpLogReturnRate));
            sortInfos.add(new SortInfo(entry.getKey(), relativity));
        }

        SortUtil.sortDescending(sortInfos, sortInfo -> sortInfo.getValue());

        List<String> recommendedStocks = new ArrayList<>();

        int maxLength = sortInfos.size() < 5 ? sortInfos.size() : 5;
        for (int i = 0; i < maxLength; i++) {
            recommendedStocks.add(sortInfos.get(i).getStockCode());
        }

        String[] ss = recommendedStocks.toArray(new String[0]);

        Map<String, StockRealTimePO> stockRealTimePOMap = stockDao.getRealTimeStockInfo(ss);

        List<StockRealTimePO> lista = new ArrayList<>(stockRealTimePOMap.values());
        return lista;
    }

    /**
     * 获得股票名
     *
     * @param stockCode 股票号
     * @return 股票名
     */
    @Override
    public String getStockName(String stockCode) {
        if (codeName.isEmpty()) {
            List<String> allCodeName = stockDao.getAllStockCodeAndName();

            for (String s : allCodeName) {
                String[] strs = s.split(";");
                codeName.put(strs[0], strs[1]);
            }
        }
        return codeName.get(stockCode);
    }

    /**
     * 获得实时股票数据
     *
     * @param stockIDs
     * @return Map<StockID，StockRealTimePO></>
     */
    @Override
    public Map<String, StockRealTimePO> getRealTimeStockInfo(String... stockIDs) {
        Map<String, StockRealTimePO> stockRealTimePOS = stockDao.getRealTimeStockInfo(stockIDs);
        return stockRealTimePOS;
    }

    /**
     * 获得股票价值综合评价参数
     *
     * @param stockID 股票号
     * @return 股票价值综合评价参数
     */
    @Override
    public List<ChartInfo> getInnerValue(String stockID) throws InnerValueException {
        Date today = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);
        calendar.set(year - 1, month, day);
        long one = calendar.getTimeInMillis();
        java.sql.Date oneYear = new java.sql.Date(one);
        // 股票数据
        List<StockPO> stocks = stockCache.getStockData(stockID);
        // 六大指数数据
        //SH_Composite, SmallPlate_Composite, Gem_Composite, SS_300, SZ_Composite, SZA_Composite
        List<PlateinfoPO> SH_Composite = plateDao.getPlateInfo("上证指数", oneYear, today);
        List<PlateinfoPO> SmallPlate_Composite = plateDao.getPlateInfo("中小板指", oneYear, today);
        List<PlateinfoPO> Gem_Composite = plateDao.getPlateInfo("创业板指", oneYear, today);
        List<PlateinfoPO> SS_300 = plateDao.getPlateInfo("沪深300", oneYear, today);
        List<PlateinfoPO> SZ_Composite = plateDao.getPlateInfo("深证成指", oneYear, today);
        List<PlateinfoPO> SZA_Composite = plateDao.getPlateInfo("深证Ａ指", oneYear, today);
        String industry = industryDao.getIndustryOfStock(stockID).get(stockID);
        List<ChartInfo> industryPOS = industryDao.getIndustryReturnRate(industry, oneYear, today);

        Map<PlateEnum, List<PlateinfoPO>> indices = new HashMap<>();
        indices.put(PlateEnum.SH_Composite, SH_Composite);
        indices.put(PlateEnum.SmallPlate_Composite, SmallPlate_Composite);
        indices.put(PlateEnum.Gem_Composite, Gem_Composite);
        indices.put(PlateEnum.SS_300, SS_300);
        indices.put(PlateEnum.SZ_Composite, SZ_Composite);
        indices.put(PlateEnum.SZA_Composite, SZA_Composite);

        // 按照季度的基本面
        Map<String, StockBasicInfoPO> stockBasicInfo = new HashMap<>();
        int s = ((month + 1) / 4) + 1;
        for (int y = year - 1; y <= year; y++) {
            for (; s <= 4; s++) {
                Map<String, StockBasicInfoPO> stockBasicInfoPOMap = basicDao.getStockBasicInfo(y, s, stockID);
                if (stockBasicInfoPOMap == null || stockBasicInfoPOMap.isEmpty()) {
                    break;
                }
                stockBasicInfo.put(String.valueOf(y) + "-" + String.valueOf(s), stockBasicInfoPOMap.get(stockID));
            }
            s = 1;
        }

        return new StockValue().getEvalPara(stocks, indices, stockBasicInfo, industryPOS);
    }

    /**
     * 股票评级系数
     * 1.00~1.09强力买入；1.10~2.09买入；2.10~3.09观望；3.10~4.09适度减持；4.10~5.00卖出。
     *
     * @param stockID
     * @return 股票评级系数（股票信息不存在返回正无穷）
     */
    public double getScore(String stockID) {
        List<StockPO> stocks = stockCache.getStockData(stockID);

        StockBasicInfoPO stockBasicInfoPO = basicDao.getStockBasicInfo(2017, 1, stockID).get(stockID);

        if (stocks.size() == 0 || stockBasicInfoPO == null) {
            return Double.MAX_VALUE;
        }

        return new StockScore().getScore(stocks.get(stocks.size() - 1), stockBasicInfoPO);
    }

    /**
     * 根据基本面指标选股
     *
     * @param indices 基本面指标<指标类型，取值范围>
     * @return 选出来的股票<股票号，股票名>
     */
    @Override
    public Map<String, String> selectStock(Map<BasicIndices, double[]> indices) {
        return stockDao.filterStock(indices);
    }

    /**
     * 获得股票号
     *
     * @param stockName 股票名
     * @return 股票号
     */
    @Override
    public String getStockCode(String stockName) {
        if (nameCode.isEmpty()) {
            List<String> allCodeName = stockDao.getAllStockCodeAndName();

            for (String s : allCodeName) {
                String[] strs = s.split(";");
                nameCode.put(strs[1], strs[0]);
            }
        }
        return codeName.get(stockName);
    }

    /**
     * 提取对数收益率
     *
     * @param stocks 股票数据
     * @return 对数收益率
     */
    private List<Double> getLogReturnRate(List<StockPO> stocks) {
        List<Double> logReturnRate = new ArrayList<>();
        stocks.stream().forEach(stockPO -> logReturnRate.add(stockPO.getReturnRate()));
        return logReturnRate;
    }

    /**
     * 根据日期取两组股票数据的交集，结果保留在原来的List中
     *
     * @param tmpStock
     * @param tmpThisStock
     */
    private void formatData(List<StockPO> tmpStock, List<StockPO> tmpThisStock) throws UnsupportedOperationException {
        if (tmpStock.isEmpty() || tmpThisStock.isEmpty()) {
            throw new UnsupportedOperationException();
        }
        List<StockPO> shortOne = (tmpStock.size() < tmpThisStock.size()) ? tmpStock : tmpThisStock;

        List<StockPO> longOne = (tmpStock.size() > tmpThisStock.size()) ? tmpStock : tmpThisStock;

        Iterator<StockPO> shortIter = shortOne.iterator();
        Iterator<StockPO> longIter = longOne.iterator();

        StockPO s = shortIter.next();
        StockPO l = longIter.next();

        while (shortIter.hasNext() && longIter.hasNext()) {
            if (s.getDateValue().equals(l.getDateValue())) {
                s = shortIter.next();
                l = longIter.next();
                continue;
            } else if (s.getDateValue().before(l.getDateValue())) {
                shortIter.remove();
                s = shortIter.next();
                continue;
            } else {
                longIter.remove();
                l = longIter.next();
                continue;
            }
        }

        if (!s.getDateValue().equals(l.getDateValue())) {
            shortIter.remove();
            longIter.remove();
        }

        while (shortIter.hasNext()) {
            shortIter.next();
            shortIter.remove();
        }

        while (longIter.hasNext()) {
            longIter.next();
            longIter.remove();
        }
    }

    /**
     * 得到周K或月K数据
     *
     * @param dailyInfo 日K数据
     * @return 经转化的周K或月K数据
     */
    private List<StockPO> generateData(List<StockPO> dailyInfo, String type) {
        List<StockPO> result = new ArrayList<>();

        Map<String, List<StockPO>> listMap;
        if (type == MONTHLY) {
            listMap = classifyStocksByMonth(dailyInfo);
        } else {
            listMap = classifyStocksByWeek(dailyInfo);
        }

        for (Map.Entry<String, List<StockPO>> entry : listMap.entrySet()) {
            List<StockPO> stockPOS = entry.getValue();
            // 第一天的开盘价
            double open = stockPOS.get(0).getOpenPrice();
            // 最后一天的收盘价
            double close = stockPOS.get(stockPOS.size() - 1).getClosePrice();
            // 最高价
            double high = getHigh(stockPOS);
            // 最低价
            double low = getLow(stockPOS);

            double volume = getVolume(stockPOS);

            StockPO stockPO = new StockPO(stockPOS.get(0).getStockId(), stockPOS.get(0).getDateValue(), close, high, low, open, volume);

            result.add(stockPO);
        }

        // 根据日期排序
        SortUtil.sort(result, stockPO -> stockPO.getDateValue());
        // 设置均线
        setMA5(result);
        setMA10(result);
        setMA20(result);
        setMA30(result);
        setMA60(result);
        setMA120(result);
        setMA240(result);

        return result;
    }

    /**
     * @param dailyInfo
     * @return
     */
    private Map<String, List<StockPO>> classifyStocksByWeek(List<StockPO> dailyInfo) {
        Calendar calendar = Calendar.getInstance();
        Map<String, List<StockPO>> listMap = new HashMap<>();

        for (StockPO stockPO : dailyInfo) {
            calendar.setTime(stockPO.getDateValue());
            String dateStr = "" + calendar.getWeekYear() + "-" + calendar.get(Calendar.WEEK_OF_YEAR) + "";

            List<StockPO> stockPOS;
            if (listMap.get(dateStr) == null) {
                stockPOS = new ArrayList<>();
            } else {
                stockPOS = listMap.get(dateStr);
            }
            stockPOS.add(stockPO);
            listMap.put(dateStr, stockPOS);
        }

        return listMap;
    }

    /**
     * @param dailyInfo
     * @return
     */
    private Map<String, List<StockPO>> classifyStocksByMonth(List<StockPO> dailyInfo) {
        Calendar calendar = Calendar.getInstance();
        Map<String, List<StockPO>> listMap = new HashMap<>();

        // 按月进行分类
        for (StockPO stockPO : dailyInfo) {
            calendar.setTime(stockPO.getDateValue());
            String dateStr = "" + calendar.get(Calendar.YEAR) + "-" + calendar.get(Calendar.MONTH) + "";

            List<StockPO> stockPOS;
            if (listMap.get(dateStr) == null) {
                stockPOS = new ArrayList<>();
            } else {
                stockPOS = listMap.get(dateStr);
            }
            stockPOS.add(stockPO);
            listMap.put(dateStr, stockPOS);
        }

        return listMap;
    }

    /**
     * 得到最高价
     *
     * @param stocks
     * @return 最高价
     */
    private double getHigh(List<StockPO> stocks) {
        double high = Double.MIN_VALUE;
        for (StockPO stockPO : stocks) {
            if (stockPO.getHighPrice() > high) {
                high = stockPO.getHighPrice();
            }
        }
        return high;
    }

    /**
     * 得到最低价
     *
     * @param stocks
     * @return 最低价
     */
    private double getLow(List<StockPO> stocks) {
        double low = Double.MAX_VALUE;
        for (StockPO stockPO : stocks) {
            if (stockPO.getLowPrice() < low) {
                low = stockPO.getLowPrice();
            }
        }
        return low;
    }

    private double getVolume(List<StockPO> stocks) {
        double total = 0;
        for (StockPO stockPO : stocks) {
            total += stockPO.getVolume();
        }
        return total;
    }

    private void setMA5(List<StockPO> stocks) {
        try {
            for (int i = 0; i <= (5 - 2); i++) {
                stocks.get(i).setMa5(-1.0);
            }
        } catch (IndexOutOfBoundsException e) {
            return;
        }
        int index = 5 - 1;
        while (index < stocks.size()) {
            double sum = 0;
            for (int i = index - (5 - 1); i <= index; i++) {
                sum += stocks.get(i).getClosePrice();
            }
            stocks.get(index).setMa5(sum / 5.0);
            index++;
        }
    }

    private void setMA10(List<StockPO> stocks) {
        try {
            for (int i = 0; i <= (10 - 2); i++) {
                stocks.get(i).setMa10(-1.0);
            }
        } catch (IndexOutOfBoundsException e) {
            return;
        }
        int index = 10 - 1;
        while (index < stocks.size()) {
            double sum = 0;
            for (int i = index - (10 - 1); i <= index; i++) {
                sum += stocks.get(i).getClosePrice();
            }
            stocks.get(index).setMa10(sum / 10.0);
            index++;
        }
    }

    private void setMA20(List<StockPO> stocks) {
        try {
            for (int i = 0; i <= (20 - 2); i++) {
                stocks.get(i).setMa20(-1.0);
            }
        } catch (IndexOutOfBoundsException e) {
            return;
        }
        int index = 20 - 1;
        while (index < stocks.size()) {
            double sum = 0;
            for (int i = index - (20 - 1); i <= index; i++) {
                sum += stocks.get(i).getClosePrice();
            }
            stocks.get(index).setMa20(sum / 20.0);
            index++;
        }
    }

    private void setMA30(List<StockPO> stocks) {
        try {
            for (int i = 0; i <= (30 - 2); i++) {
                stocks.get(i).setMa30(-1.0);
            }
        } catch (IndexOutOfBoundsException e) {
            return;
        }
        int index = 30 - 1;
        while (index < stocks.size()) {
            double sum = 0;
            for (int i = index - (30 - 1); i <= index; i++) {
                sum += stocks.get(i).getClosePrice();
            }
            stocks.get(index).setMa30(sum / 30.0);
            index++;
        }
    }

    private void setMA60(List<StockPO> stocks) {
        try {
            for (int i = 0; i <= (60 - 2); i++) {
                stocks.get(i).setMa60(-1.0);
            }
        } catch (IndexOutOfBoundsException e) {
            return;
        }
        int index = 60 - 1;
        while (index < stocks.size()) {
            double sum = 0;
            for (int i = index - (60 - 1); i <= index; i++) {
                sum += stocks.get(i).getClosePrice();
            }
            stocks.get(index).setMa60(sum / 60.0);
            index++;
        }
    }

    private void setMA120(List<StockPO> stocks) {
        try {
            for (int i = 0; i <= (120 - 2); i++) {
                stocks.get(i).setMa120(-1.0);
            }
        } catch (IndexOutOfBoundsException e) {
            return;
        }
        int index = 120 - 1;
        while (index < stocks.size()) {
            double sum = 0;
            for (int i = index - (120 - 1); i <= index; i++) {
                sum += stocks.get(i).getClosePrice();
            }
            stocks.get(index).setMa120(sum / 120.0);
            index++;
        }
    }

    private void setMA240(List<StockPO> stocks) {
        try {
            for (int i = 0; i <= (240 - 2); i++) {
                stocks.get(i).setMa240(-1.0);
            }
        } catch (IndexOutOfBoundsException e) {
            return;
        }
        int index = 240 - 1;
        while (index < stocks.size()) {
            double sum = 0;
            for (int i = index - (240 - 1); i <= index; i++) {
                sum += stocks.get(i).getClosePrice();
            }
            stocks.get(index).setMa240(sum / 240.0);
            index++;
        }
    }

    private MapUtil getAnalysis(List<StockPO> data) {
        Map<IndicesAnalysis, Map<java.sql.Date, String>> mapMap_buy = new HashMap<>();
        Map<IndicesAnalysis, Map<java.sql.Date, String>> mapMap_sell = new HashMap<>();

        List<Map<Date, String>> boll = BOLLAnalysis.getSignal(data);
        List<Map<Date, String>> dmi = DMIAnalysis.getSignal(data);
        List<Map<Date, String>> kdj = KDJAnalysis.getSignal(data);
        List<Map<Date, String>> macd = MACDAnalysis.getSignal(data);

        mapMap_buy.put(IndicesAnalysis.BOLL, boll.get(0));
        mapMap_buy.put(IndicesAnalysis.DMI, dmi.get(0));
        mapMap_buy.put(IndicesAnalysis.KDJ, kdj.get(0));
        mapMap_buy.put(IndicesAnalysis.MACD, macd.get(0));

        mapMap_sell.put(IndicesAnalysis.BOLL, boll.get(1));
        mapMap_sell.put(IndicesAnalysis.DMI, dmi.get(1));
        mapMap_sell.put(IndicesAnalysis.KDJ, kdj.get(1));
        mapMap_sell.put(IndicesAnalysis.MACD, macd.get(1));

        return new MapUtil(mapMap_buy, mapMap_sell);
    }

    /**
     * 帮助传递多个Map参数
     */
    private class MapUtil {
        private Map<IndicesAnalysis, Map<java.sql.Date, String>> map_buy;

        private Map<IndicesAnalysis, Map<java.sql.Date, String>> map_sell;

        public MapUtil(Map<IndicesAnalysis, Map<Date, String>> map_buy, Map<IndicesAnalysis, Map<Date, String>> map_sell) {
            this.map_buy = map_buy;
            this.map_sell = map_sell;
        }

        public Map<IndicesAnalysis, Map<Date, String>> getMap_buy() {
            return map_buy;
        }

        public void setMap_buy(Map<IndicesAnalysis, Map<Date, String>> map_buy) {
            this.map_buy = map_buy;
        }

        public Map<IndicesAnalysis, Map<Date, String>> getMap_sell() {
            return map_sell;
        }

        public void setMap_sell(Map<IndicesAnalysis, Map<Date, String>> map_sell) {
            this.map_sell = map_sell;
        }
    }

    /**
     * 采取FIFO算法的股票信息缓存区
     */
    private class StockCache {
        private List<String> stocks;

        private Map<String, List<StockPO>> data;

        public StockCache() {
            stocks = new ArrayList<>();
            data = new HashMap<>();
        }

        public List<StockPO> getStockData(String stockID) {
            if (stocks.contains(stockID)) {
                return data.get(stockID);
            } else {
                if (stocks.size() < 20) {
                    stocks.add(stockID);
                    List<StockPO> stock = stockDao.getAllInfoOfOneStock(stockID);
                    data.put(stockID, stock);
                    return stock;
                } else {
                    String s = stocks.remove(0);
                    data.remove(s);
                    stocks.add(stockID);
                    List<StockPO> stock = stockDao.getAllInfoOfOneStock(stockID);
                    data.put(stockID, stock);
                    return stock;
                }
            }
        }
    }

    private class EstimateThread implements Callable<Double> {
        List<Double> data;

        public EstimateThread(List<Double> data) {
            this.data = data;
        }

        @Override
        public Double call() throws Exception {
            return stockPrediction.getNextPrediction(data);
        }
    }

}
