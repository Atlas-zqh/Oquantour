package bl.strategy.backtest;

import bl.strategy.statistics.BackTestStatisticsBLHelper;
import bl.tools.Calculator;
import bl.tools.SortUtil;
import bl.tools.SplitMapElements;
import bl.tools.TransferNestedList;
import bl.tools.utils.BackTestInfo;
import bl.tools.utils.SortInfo;
import exception.FormativePeriodNotExistException;
import exception.WinnerSelectErrorException;
import vo.*;
import bl.tools.utils.StdValueInfo;
import data.dataImpl.StockDataServiceImpl;
import data.dataImpl.StockPlateDataServiceImpl;
import data.dataService.StockDataService;
import data.dataService.StockPlateDataService;
import exception.ParameterErrorException;
import exception.SelectStocksException;
import po.FilterStockPO;
import po.PlatePO;
import po.SearchPO;
import po.Stock;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 策略类
 * <p>
 * Created by keenan on 23/03/2017.
 */
public abstract class Strategy {

    // 界面传来的回测信息
    protected BackTestingInfoVO backTestingInfoVO;
    // 股票池
    protected Map<Date, List<Stock>> securities;
    // 有效的股票（包括形成期，可以回测）
    protected List<List<Stock>> validStockCodes;
    // 有效的股票（不包括形成期，可以回测）
    protected List<List<Stock>> validStockWithoutPeriod;
    // 无效的股票（没有数据不能回测）的名字
    protected List<String> invalidStockNames_Statistics;
    // 无效的股票 （停牌不能回测）的名字
    protected List<String> invalidStockNames_Halt;
    // 回测结果
    protected List<BackTestingSingleChartVO> backTestResult;
    // 数据层接口
    protected StockPlateDataService stockPlateDataService;

    protected StockDataService stockDataService;
    // 形成期
    protected int formativePeriod;
    //模拟成本资金为十万
    private final BigDecimal cost = new BigDecimal(1000000000);
    //模拟当前资金，开始时和成本相同
    private BigDecimal currentFund = cost;

    private List<WinnerInfoVO> winnerInfoVOS;

    private List<StdValueInfo> stdValueInfos;

    public Strategy() {
        stockPlateDataService = new StockPlateDataServiceImpl();
        stockDataService = new StockDataServiceImpl();
        winnerInfoVOS = new ArrayList<>();
    }


    /**
     * 重新设置持有期长度
     *
     * @param holdingPeriod 持有期长度
     */
    public void resetHoldingPeriod(int holdingPeriod) {
        this.backTestingInfoVO.holdingPeriod = holdingPeriod;
    }

    /**
     * 得到回测结果
     *
     * @return 回测结果
     */
    public List<BackTestingSingleChartVO> getBackTestResult() throws ParameterErrorException, WinnerSelectErrorException {

        currentFund = cost;
        //设置股票池
        if (securities == null) {
            set_securities();
        }

        if (stdValueInfos == null) {
            stdValueInfos = calStdValue();
        }

        List<BackTestInfo> backTestInfos = new ArrayList<>();
        List<BackTestingSingleChartVO> backTestingSingleChartVOS = new ArrayList<>();

        if (securities.size() == 0) {
            return backTestingSingleChartVOS;
        }


        List<Date> dateList = SplitMapElements.splitKeys(securities);

        Date lastDate = dateList.get(dateList.size() - 1);

        //判断用户所选的开始日期是否在设定的股票池Map中 如果不在，则选Map的第一个KeyValue作为回测的开始日期
        Date startDate = backTestingInfoVO.startDate;
        if (!dateList.contains(startDate)) {
            startDate = dateList.get(formativePeriod);
        }

        Date currentDate = startDate;

        try {
            while (!dateList.get(dateList.indexOf(currentDate) + backTestingInfoVO.holdingPeriod - 1).after(lastDate)) {
                //当前日期加上持有期在结束日期之前时，进行循环，计算策略平均收益率
                List<String> winner = selectWinner(currentDate, dateList, securities);

                addToWinnerInfo(currentDate, winner);

                backTestInfos.addAll(calReturnRate(currentDate, dateList, winner,
                        buyIntoShares(currentDate, winner, securities), securities));

                //刚好是最后一个持有期（即可以整除），则跳出循环
                if (dateList.get(dateList.indexOf(currentDate) + backTestingInfoVO.holdingPeriod - 1).equals(lastDate)) {
                    break;
                }
                currentDate = dateList.get(dateList.indexOf(currentDate) + backTestingInfoVO.holdingPeriod);
            }
        } catch (IndexOutOfBoundsException e) {
            //最后可能还剩余几天（不满一个持有期）
            List<String> winner = selectWinner(currentDate, dateList, securities);

            addToWinnerInfo(currentDate, winner);

            backTestInfos.addAll(calReturnRate(currentDate, dateList, winner,
                    buyIntoShares(currentDate, winner, securities), securities));

        } finally {
            backTestResult = combineStrategyAndBenchmark(stdValueInfos, backTestInfos);

            return backTestResult;
        }

    }

    /**
     * 根据一定指标选出赢家组合的方法
     *
     * @param currentDate 当前日期
     * @param dateList    从Map中抽取的日期列表
     * @param map         按日期组合的股票信息
     * @return 赢家组合的股票代码列表
     */
    protected abstract List<String> selectWinner(Date currentDate, List<Date> dateList, Map<Date, List<Stock>> map) throws WinnerSelectErrorException;

    /**
     * 买入赢家组合后，计算持有期内平均收益率的方法
     *
     * @param currentDate 当前日期
     * @param dateList    从map中分离出来的日期列表（便于一一对应）
     * @param winner      已选出的赢家组合
     * @param map         以日期组合的股票信息
     * @return List<BackTestInfo>，日期和收益率组成的BackTestInfo对象
     */
    protected List<BackTestInfo> calReturnRate(Date currentDate, List<Date> dateList, List<String> winner, List<Double> numOfShares, Map<Date, List<Stock>> map) {

        int index = dateList.indexOf(currentDate);
        Date lastDate = dateList.get(dateList.size() - 1);
        BigDecimal totalFund;
        List<BackTestInfo> backTestInfos = new ArrayList<>();
        for (int i = 0; i < backTestingInfoVO.holdingPeriod; i++) {
            if (index + i >= dateList.size()) {
                break;
            }
            //如果日期超过了用户所选日期的最后一天，则跳出循环
            else if (dateList.get(index + i).after(lastDate)) {
                break;
            }

            //每天先把总数清零
            totalFund = BigDecimal.valueOf(0);

            for (String code : winner) {
                for (Stock stock : map.get(dateList.get(index + i))) {
                    if (stock.getCode().equals(code)) {
                        //得到赢家组合中的股票的当日复权收盘价，乘以持股数即为当前资金数
                        BigDecimal closeOfCurrentDate = new BigDecimal(stock.getAdjClose());
                        BigDecimal stockNum = new BigDecimal(numOfShares.get(winner.indexOf(code)));
                        totalFund = totalFund.add(closeOfCurrentDate.multiply(stockNum));
                        break;
                    }
                }
            }
            //计算累计收益率（始终和最初的成本比较）
            currentFund = totalFund;
            BigDecimal bigDecimal = currentFund.subtract(cost);
            double rate = bigDecimal.divide(cost).doubleValue();

            backTestInfos.add(new BackTestInfo(dateList.get(index + i), rate));

        }

        return backTestInfos;

    }

    /**
     * 得到回测统计数据
     *
     * @return 回测统计数据
     */
    public StrategyStatisticsVO getBackTestStatistics() {
        BackTestStatisticsBLHelper backTestStatisticsBLHelper = new BackTestStatisticsBLHelper(backTestResult);

        return backTestStatisticsBLHelper.getStatistics();
    }

    /**
     * 能否回测
     *
     * @return 能否回测的判断结果
     */
    public BackTestingJudgeVO canBackTest() throws SelectStocksException, FormativePeriodNotExistException {

        List<String> stockCodes = getSecurityCodes();

        removeStocksWithoutStatistics(stockCodes, formativePeriod);

        if (backTestingInfoVO.stockPlateName.equals("")) {
            //如果是自选股，必须大于100只
            //如果筛选掉没有数据的股票后，总数量小于100只，则不能回测
            if (validStockCodes.size() < 100) {
                return new BackTestingJudgeVO(false, TransferNestedList.transfer(validStockCodes), invalidStockNames_Statistics, new ArrayList<>());

            } else {
                //总数量大于100只，则继续筛选掉有停牌情况的股票
                removeHaltedStocks(validStockCodes, formativePeriod);
                if (validStockCodes.size() < 100) {
                    return new BackTestingJudgeVO(false, TransferNestedList.transfer(validStockCodes), invalidStockNames_Statistics, invalidStockNames_Halt);
                } else {
                    return new BackTestingJudgeVO(true, TransferNestedList.transfer(validStockCodes), invalidStockNames_Statistics, invalidStockNames_Halt);
                }
            }
        } else {
            //如果是板块
            removeHaltedStocks(validStockCodes, formativePeriod);
            return new BackTestingJudgeVO(true, TransferNestedList.transfer(validStockCodes), invalidStockNames_Statistics, invalidStockNames_Halt);
        }

    }

    /**
     * 设置股票池
     */
    protected void set_securities() {

        List<List<Stock>> mySecurity = validStockCodes;

        securities = reCombine(mySecurity);
    }


    /**
     * 获得基准收益率
     *
     * @return 基准收益率
     */
    protected List<StdValueInfo> calStdValue() {
        if (backTestingInfoVO.stockPlateName != "") {
            return this.plateStdValue(backTestingInfoVO.stockPlateName, backTestingInfoVO.startDate, backTestingInfoVO.endDate);
        }

        List<StdValueInfo> stdValueInfos = new ArrayList<>();

        if (securities.size() == 0) {
            return stdValueInfos;
        }

        // 在原股票池中删除形成期的数据
        Map<Date, List<Stock>> testDays = reCombine(validStockWithoutPeriod);


        // 找到每个股票的第一天的数据
        List<Stock> firstDayStocks = new ArrayList<>();

        validStockWithoutPeriod.stream().forEach(stocks -> firstDayStocks.add(stocks.get(stocks.size() - 1)));

        for (Map.Entry<Date, List<Stock>> dateListEntry : testDays.entrySet()) {
            List<Double> total = new ArrayList<>();

            dateListEntry.getValue()
                    .stream()
                    .filter(stock -> firstDayStocks.contains(stock))
                    .forEach(stock -> total.add((stock.getAdjClose() / firstDayStocks.get(firstDayStocks.indexOf(stock)).getAdjClose()) - 1));

            stdValueInfos.add(new StdValueInfo(dateListEntry.getKey(), Calculator.avg(total)));
        }

        return stdValueInfos;
    }

    /**
     * 根据界面选择的股票范围获得相应股票代码列表
     *
     * @return 相应股票代码的列表
     */
    protected List<String> getSecurityCodes() throws SelectStocksException {
        List<String> stockCodes = new ArrayList<>();

        if (backTestingInfoVO.showAll && backTestingInfoVO.stockPlateName.equals("")
                && backTestingInfoVO.stocks.size() == 0) {
            //获得改短时间内所有股票的代码
            List<String> temp = stockDataService.getAllStockCodeAndName();
            for (String s : temp) {
                stockCodes.add(s.split(";")[1]);
            }
        } else if (!backTestingInfoVO.stockPlateName.equals("")
                && backTestingInfoVO.stocks.size() == 0) {
            //根据模块获得股票
            List<String> temp = stockPlateDataService.getAllStockInfoInPlate(backTestingInfoVO.stockPlateName);
            for (String s : temp) {
                stockCodes.add(s.split(";")[0]);
            }
        } else if (backTestingInfoVO.stockPlateName.equals("")
                && backTestingInfoVO.stocks.size() != 0) {
            //用户自选股票
            for (String stockCode : backTestingInfoVO.stocks) {
                stockCodes.add(stockCode);
            }
        } else {
            throw new SelectStocksException();
        }

        return stockCodes;
    }

    /**
     * 剔除没有数据的股票
     *
     * @param stockCodes
     * @param formativePeriod
     */
    protected void removeStocksWithoutStatistics(List<String> stockCodes, int formativePeriod) throws FormativePeriodNotExistException {
        FilterStockPO filterStockPO = stockDataService.filterStockWithoutData(stockCodes, backTestingInfoVO.startDate,
                backTestingInfoVO.endDate, formativePeriod);

        // 不存在形成期的数据
        if (filterStockPO == null) {
            throw new FormativePeriodNotExistException("No valid information in formative period.");
        }

        validStockCodes = filterStockPO.getStockWithoutProblem();
        invalidStockNames_Statistics = filterStockPO.getStockWithProblem();

    }

    /**
     * 剔除有停牌情况的股票
     * （先剔除没有数据的股票）
     *
     * @param stocks
     */
    protected void removeHaltedStocks(List<List<Stock>> stocks, int formativePeriod) {
        FilterStockPO filterStockPO = stockDataService.filterStockHalt(stocks, backTestingInfoVO.startDate,
                backTestingInfoVO.endDate, formativePeriod);

        validStockCodes = filterStockPO.getStockWithoutProblem();
        invalidStockNames_Halt = filterStockPO.getStockWithProblem();
        validStockWithoutPeriod = filterStockPO.getStockWithOutFormativePeriod();
    }

    /**
     * 股票代码分 -> 按日期分（排序好）
     *
     * @param securities
     * @return
     */
    private Map<Date, List<Stock>> reCombine(List<List<Stock>> securities) {

        // 使用TreeMap，在加入时自动完成排序
        Map<Date, List<Stock>> dateListMap = new TreeMap<>();
        // 将股票按照日期重新组合List
        for (List<Stock> stocks : securities) {
            for (Stock stock : stocks) {

                Date thisDate = stock.getDate();

                if (dateListMap.containsKey(thisDate)) {
                    dateListMap.get(thisDate).add(stock);
                } else {
                    List<Stock> newDateStock = new ArrayList<>();
                    newDateStock.add(stock);
                    dateListMap.put(thisDate, newDateStock);
                }
            }
        }

        return dateListMap;
    }

    /**
     * 得到板块收益率
     *
     * @param plateName 板块名称
     * @return 板块基准收益率
     */
    private List<StdValueInfo> plateStdValue(String plateName, Date startDate, Date endDate) {
        List<PlatePO> platePOList = stockPlateDataService.getPlateReturnRate(new SearchPO(startDate, endDate, plateName));

        List<StdValueInfo> stdValueInfos = new ArrayList<>();

        platePOList.stream().forEach(platePO -> stdValueInfos.add(new StdValueInfo(platePO)));

        SortUtil.sort(stdValueInfos, stdValueInfo -> stdValueInfo.getDate());

        return stdValueInfos;
    }

    /**
     * 将策略收益率和基准收益率合并成画图用的回测信息
     *
     * @param stdValueInfos 策略收益率
     * @param backTestInfos 基准收益率
     * @return 回测信息
     * @throws ParameterErrorException
     */
    protected List<BackTestingSingleChartVO> combineStrategyAndBenchmark(List<StdValueInfo> stdValueInfos, List<BackTestInfo> backTestInfos) throws ParameterErrorException {

        if (stdValueInfos.size() != backTestInfos.size()) {
            throw new ParameterErrorException("Benchmark return rate and back test return rate don't match!");
        }

        List<BackTestingSingleChartVO> backTestingSingleChartVOS = new ArrayList<>();

        for (int i = 0; i < stdValueInfos.size(); i++) {
            StdValueInfo stdValueInfo = stdValueInfos.get(i);
            BackTestInfo backTestInfo = backTestInfos.get(i);

            if (!stdValueInfo.getDate().equals(backTestInfo.getDate())) {
                throw new ParameterErrorException("Data in benchmark results and back test results don't match!");
            }

            backTestingSingleChartVOS.add(new BackTestingSingleChartVO(stdValueInfo.getDate(), backTestInfo.getValue(), stdValueInfo.getValue()));
        }

        SortUtil.sort(backTestingSingleChartVOS, backTestingSingleChartVO -> backTestingSingleChartVO.date);

        return backTestingSingleChartVOS;
    }

    /**
     * 模拟买入股票的方法
     * 买入的股数 = 当前资金数根据股票数量等分（等权重买入），再除以当前复权收盘价
     *
     * @param currentDate 当前日期（应为某个持有期的第一天）
     * @param winner      已选出的赢家组合
     * @param map         以日期组合的股票信息
     * @return 赢家组合中每只股票的买入股数（大小应与赢家组合相等）
     */
    private List<Double> buyIntoShares(Date currentDate, List<String> winner, Map<Date, List<Stock>> map) {
        List<Double> numOfShares = new ArrayList<>();

        for (String code : winner) {
            for (Stock stock : map.get(currentDate)) {
                if (stock.getCode().equals(code)) {
                    numOfShares.add(currentFund.doubleValue() / winner.size() / stock.getAdjClose());
                }
            }
        }

        return numOfShares;
    }

    /**
     * 在map中获得某只股票某日的复权收盘价的方法
     *
     * @param stockCode 要搜索的股票代码
     * @param dateList  从map中分离出来的日期列表
     * @param index     当前日期在日期列表中的索引号
     * @param map       以日期组合的股票信息
     * @return 此股票的复权收盘价（如果不存在该股票当日的复权收盘价，返回值为-1）
     */
    protected double getStockAdjClose(String stockCode, List<Date> dateList, int index, Map<Date, List<Stock>> map) {
        List<Stock> stockList = map.get(dateList.get(index));
        Stock stock = stockList.stream().filter(s -> s.getCode().equals(stockCode)).findFirst().orElse(null);
        double adjClose = (stock == null) ? -1 : stock.getAdjClose();
        return adjClose;
    }

    /**
     * 用于筛选已排好序的股票代码
     * 如果之后的持有期中没有该股票的数据
     * 则它无法成为赢家组合中的一部分
     *
     * @param sortInfos     已经排好序的股票信息
     * @param map           按日期组合的股票信息
     * @param dateList      从Map中抽取的日期列表
     * @param startDate     持有期的开始日期
     * @param holdingPeriod 持有期长度
     * @return 有效的股票代码列表
     */
    protected List<SortInfo> holdingPeriodFilter(List<SortInfo> sortInfos, Map<Date, List<Stock>> map, List<Date> dateList,
                                                 Date startDate, int holdingPeriod) {
        Set<String> result = sortInfos.stream().collect(Collectors.groupingBy(SortInfo::getStockCode)).keySet();

        int index = dateList.indexOf(startDate);

        Set<String> temp = result;
        Set<String> retainResult = null;


        // 找出map中 当前天的有数据的股票的代码
        for (int offset = 0; offset < holdingPeriod; offset++) {
            if (index + offset >= dateList.size()) {
                break;
            }
            List<Stock> stockList = map.get(dateList.get(index + offset));
            Set<String> strings = new HashSet<>();
            stockList.stream().forEach(s -> strings.add(s.getCode()));

            retainResult = new HashSet<>();

            for (String s : strings) {
                if (temp.contains(s)) {
                    retainResult.add(s);
                }
            }

            temp = retainResult;

        }

        List<SortInfo> sortInfoResult = new ArrayList<>();

        for (String s : retainResult) {
            SortInfo sortInfo = sortInfos.stream().filter(sortInfo1 -> s.equals(sortInfo1.getStockCode())).findFirst().orElse(null);
            if (sortInfo != null) {
                sortInfoResult.add(sortInfo);
            }
        }
        return sortInfoResult;
    }

    /**
     * 获得收益率分布直方图的数据（由界面按需自己处理）
     *
     * @return 收益率分布数据
     */
    public List<Double> getReturnRateDistribution() {
        List<Double> returnRateDistribution = new ArrayList<>();
        int holdingPeriod = backTestingInfoVO.holdingPeriod;

        //循环计算每个持有期的收益率
        int offset = 0;
        for (; offset + holdingPeriod <= backTestResult.size(); offset += holdingPeriod) {
            double startValue = backTestResult.get(offset).backTestValue;
            double endValue = backTestResult.get(offset + holdingPeriod - 1).backTestValue;
            returnRateDistribution.add((endValue - startValue) / (1 + startValue));
        }

        if (offset != backTestResult.size() && offset + holdingPeriod > backTestResult.size()) {
            double startValue = backTestResult.get(offset).backTestValue;
            double endValue = backTestResult.get(backTestResult.size() - 1).backTestValue;

            returnRateDistribution.add((endValue - startValue) / (1 + startValue));
        }
        return returnRateDistribution;
    }

    /**
     * 获得所有可回测的日期列表（不算上持有期）
     *
     * @return 可回测的日期列表
     */
    public List<Date> getDateList() {
        List<List<Stock>> allSecurities = new ArrayList<>(validStockCodes);

        Map<Date, List<Stock>> dateListMap = reCombine(allSecurities);

        Date startDate = backTestingInfoVO.startDate;

        Iterator<Map.Entry<Date, List<Stock>>> iterator = dateListMap.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<Date, List<Stock>> entry = iterator.next();
            Date currentDate = entry.getKey();

            if (currentDate.before(startDate)) {
                iterator.remove();
                continue;
            }

            if (currentDate.equals(startDate) || currentDate.after(startDate)) {
                break;
            }
        }

        return SplitMapElements.splitKeys(dateListMap);
    }

    /**
     * 得到每个调仓日的赢家信息
     *
     * @return 赢家信息
     */
    public List<WinnerInfoVO> getWinnerInfo() {
        return this.winnerInfoVOS;
    }

    /**
     * 向赢家信息VO中增加赢家组合信息
     *
     * @param currentDate 当前调仓日
     * @param winners     赢家组合的信息
     */
    private void addToWinnerInfo(Date currentDate, List<String> winners) {
        List<Stock> stocks = securities.get(currentDate);
        List<StockBasicInfoVO> stockBasicInfoVOS = new ArrayList<>();

        for (Stock stock : stocks) {
            if (winners.contains(stock.getCode())) {
                stockBasicInfoVOS.add(new StockBasicInfoVO(stock.getCode(), stock.getName(), stock.getOpen(), stock.getHigh(), stock.getLow(), stock.getClose(), stock.getAdjClose()));
            }
        }

        winnerInfoVOS.add(new WinnerInfoVO(currentDate, stockBasicInfoVOS));
    }

}
