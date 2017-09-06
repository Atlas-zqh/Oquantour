package oquantour.service.servicehelper.strategy;

import oquantour.exception.BackTestErrorException;
import oquantour.exception.UnsupportedOperationException;
import oquantour.exception.WinnerSelectionErrorException;
import oquantour.po.StockPO;
import oquantour.po.util.BackTestWinner;
import oquantour.po.util.ChartInfo;
import oquantour.util.tools.Calculator;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.*;

/**
 * 在serviceImpl中处理股票池，策略类只负责根据股票池和回测条件进行计算，返回回测结果
 * <p>
 * 策略类需要的参数为：股票池数据（以Map形式，包括了形成期的数据），形成期长度，持有期长度，最大持仓股票数
 * <p>
 * Created by keenan on 10/05/2017.
 */

public abstract class Strategy {
    // 股票池
    protected Map<Date, List<StockPO>> securities;
    // 形成期
    protected int formative;
    // 持有期
    protected int holding;
    // 回测开始日期
    protected Date startDate;
    // 参与回测的股票数量
    protected int numOfStocks;
    // 每个调仓日的赢家组合
    protected List<BackTestWinner> dailyWinners;
    // 收益率分布
    protected List<Double> returnRateDistribution;
    // 策略收益率
    protected List<ChartInfo> backTestReturnRate;
    // 基准收益率
    protected List<ChartInfo> benchmarkReturnRate;
    // 初始成本资金为一千万
    private final BigDecimal initialFund = new BigDecimal(10000000);
    // 模拟当前资金，开始时和成本资金相同
    private BigDecimal currentMoney = initialFund;
    // 调仓完成后的余额，起始为0

    public Strategy(Map<Date, List<StockPO>> securities, int formative, int holding, Date startDate, int numOfStocks) {
        this.securities = securities;
        this.formative = formative;
        this.holding = holding;
        this.startDate = startDate;
        this.numOfStocks = numOfStocks;
        dailyWinners = new ArrayList<>();
        returnRateDistribution = new ArrayList<>();
        backTestReturnRate = new ArrayList<>();
        benchmarkReturnRate = new ArrayList<>();
    }

    public void setSecurities(Map<Date, List<StockPO>> securities) {
        this.securities = securities;
    }

    public void setFormative(int formative) {
        this.formative = formative;
    }

    public void setHolding(int holding) {
        this.holding = holding;
    }

    public int getHolding() {
        return holding;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setNumOfStocks(int numOfStocks) {
        this.numOfStocks = numOfStocks;
    }

    /**
     * 得到策略收益率
     * <p>
     * 调仓日开盘前调仓
     *
     * @return 策略收益率
     */
    public List<ChartInfo> getBackTestReturnRate() throws BackTestErrorException {
        if (!backTestReturnRate.isEmpty()) {
            backTestReturnRate = new ArrayList<>();
        }

        List<Date> dateList = new ArrayList<>(securities.keySet());

        Date lastDate = dateList.get(dateList.size() - 1);

        // 回测开始日期（形成期后的第一天）
        Date actualStartDate = dateList.get(formative);

        Date currentDate = actualStartDate;

        int cur_index = formative;

        try {
            while (!dateList.get(cur_index + holding - 1).after(lastDate)) {
                // 如果不是第一天，如果是调仓日，先把持仓股票卖出，计算收益率
                // 如果不是调仓日，只要计算收益率
                // 得到赢家组合
                // 买入，得到份额
                // 下一个调仓日
                List<StockPO> winners = selectWinner(currentDate, dateList);

                BackTestWinner afterBuyingInfo = buyIntoShare(winners, currentDate);

                calBackTestReturnRate(afterBuyingInfo, dateList, currentDate);

                dailyWinners.add(afterBuyingInfo);

                if (dateList.get(cur_index + holding - 1).equals(lastDate)) {
                    break;
                }

                cur_index += holding;
                currentDate = dateList.get(cur_index);
            }
        } catch (IndexOutOfBoundsException e) {
            List<StockPO> winners = selectWinner(currentDate, dateList);

            BackTestWinner afterBuyingInfo = buyIntoShare(winners, currentDate);

            calBackTestReturnRate(afterBuyingInfo, dateList, currentDate);
            dailyWinners.add(afterBuyingInfo);
        } catch (BackTestErrorException e) {
            throw e;
        } finally {
            return backTestReturnRate;
        }
    }

    /**
     * 计算策略收益率
     * 并加入策略收益率结果中
     *
     * @param winner      当前调仓日的赢家组合
     * @param dateList    日期列表
     *                    包含形成期
     * @param currentDate 当前日期
     */
    private void calBackTestReturnRate(BackTestWinner winner, List<Date> dateList, Date currentDate) {

        Date actualStartDate = dateList.get(formative);
        int i = 0;
        if (actualStartDate == currentDate) {
            backTestReturnRate.add(new ChartInfo(currentDate, 0));
            i = 1;
        }

        int index = dateList.indexOf(currentDate);
        Date lastDate = dateList.get(dateList.size() - 1);

        BigDecimal totalFund;

        String lastRemainingStr;

        for (; i < holding; i++) {
            // 调仓日的初始余额为上一个调仓日调仓完后的余额
            lastRemainingStr = (i == 0) ? String.valueOf(dailyWinners.get(dailyWinners.size() - 1).getDailyRemainings()) : String.valueOf(winner.getDailyRemainings());

            if (index + i >= dateList.size()) {
                break;
            }

            // 如果日期超过了用户所选日期的最后一天，则跳出循环
            else if (dateList.get(index + i).after(lastDate)) {
                break;
            }

            totalFund = new BigDecimal(lastRemainingStr);

            List<StockPO> currentDayStock = securities.get(dateList.get(index + i));

            for (Map.Entry<StockPO, Integer> stock : winner.getShares().entrySet()) {
                StockPO correspondingStock = currentDayStock
                        .stream()
                        .filter(stockPO -> stockPO.getStockId().equals(stock.getKey().getStockId()))
                        .findFirst()
                        .get();
                if (null == correspondingStock) {
                    continue;
                } else {
                    // 得到赢家组合中的股票的当日复权收盘价，乘以持股数即为当前资金数
                    BigDecimal closeOfCurrentDate = new BigDecimal(String.valueOf(correspondingStock.getAdjClose()));
                    BigDecimal stockNum = new BigDecimal(String.valueOf(stock.getValue()));
                    closeOfCurrentDate = closeOfCurrentDate.multiply(stockNum);
                    totalFund = totalFund.add(closeOfCurrentDate);
                }
            }

            // 计算累计收益率（始终和最初的成本比较）
            currentMoney = totalFund;

            BigDecimal bigDecimal = currentMoney.subtract(initialFund);
            double rate = bigDecimal.divide(initialFund).doubleValue();

            backTestReturnRate.add(new ChartInfo(dateList.get(index + i), rate));
        }
    }

    /**
     * 得到基准收益率
     *
     * @return 基准收益率
     */
    public List<ChartInfo> getBenchmarkReturnRate() {
        if (!benchmarkReturnRate.isEmpty()) {
            return benchmarkReturnRate;
        }
        // 在原股票池中删除形成期的数据
        Map<Date, List<StockPO>> stockDataWithoutPeriod = getSecuritiesWithoutPeriodInfo(securities, startDate, formative);

        // 找到每个股票的第一天的数据
        Map<String, StockPO> firstDayStocks = getFirstTransactionData(stockDataWithoutPeriod, numOfStocks);

        List<Double> total = new ArrayList<>();
        for (Map.Entry<Date, List<StockPO>> entry : stockDataWithoutPeriod.entrySet()) {
            entry.getValue()
                    .stream()
                    .forEach(stock -> total.add((stock.getAdjClose() / firstDayStocks.get(stock.getStockId()).getAdjClose()) - 1));

            benchmarkReturnRate.add(new ChartInfo(entry.getKey(), Calculator.avg(total)));

            total.clear();
        }

        return benchmarkReturnRate;
    }

    /**
     * 得到调仓日赢家组合
     *
     * @return 调仓日赢家组合
     * @throws UnsupportedOperationException 非法操作
     */
    public List<BackTestWinner> getDailyWinners() throws UnsupportedOperationException {
        if (backTestReturnRate.isEmpty()) {
            throw new UnsupportedOperationException();
        }

        return dailyWinners;
    }

    /**
     * 选择赢家组合
     *
     * @param currentDate 当前调仓日的日期
     * @param dateList    日期列表（包括形成期）
     * @return 调仓日赢家组合
     */
    protected abstract List<StockPO> selectWinner(Date currentDate, List<Date> dateList) throws BackTestErrorException, WinnerSelectionErrorException;

    /**
     * 调仓日买入股票
     * <p>
     * 每日买入股票以整数股买入，记录余额和每个股票的买入股数
     *
     * @param winners     当日赢家组合
     * @param currentDate 当前调仓日期
     * @return 当前调仓日完成调仓后的信息
     */
    private BackTestWinner buyIntoShare(List<StockPO> winners, Date currentDate) {
        Map<StockPO, Integer> shares = new HashMap<>();

        double eachMoney = currentMoney.doubleValue() / winners.size();

        BigDecimal remainings = new BigDecimal("0");

        for (StockPO stock : winners) {
            int share = (int) Math.floor(eachMoney / stock.getAdjClose());
            shares.put(stock, share);

            BigDecimal eachMoneyBigDecimal = new BigDecimal(String.valueOf(eachMoney));
            BigDecimal priceBigDecimal = new BigDecimal(String.valueOf(stock.getAdjClose()));
            BigDecimal shareBigDecimal = new BigDecimal(String.valueOf(share));

            BigDecimal investment = shareBigDecimal.multiply(priceBigDecimal);
            BigDecimal remain = eachMoneyBigDecimal.subtract(investment);

            remainings = remainings.add(remain);
        }

        BackTestWinner backTestWinner = new BackTestWinner(currentDate, shares, remainings.doubleValue());

        return backTestWinner;
    }

    /**
     * 剔除股票池中形成期的数据
     *
     * @param securities 原来的股票池
     * @param startDate  回测开始日期
     * @param formative  形成期长度
     * @return 剔除形成期数据后的股票池
     */
    private Map<Date, List<StockPO>> getSecuritiesWithoutPeriodInfo(Map<Date, List<StockPO>> securities, Date startDate, int formative) {
        Map<Date, List<StockPO>> result = new TreeMap<>(securities);

        Set<Date> dateSet = new HashSet<>(result.keySet());

        int cnt = 0;
        for (Date date : dateSet) {
            if (cnt == formative) {
                break;
            }

            if (date.before(startDate)) {
                result.remove(date);
                cnt++;
            }
        }

        return result;
    }

    /**
     * 得到每个股票的第一天的数据
     *
     * @param securitiesWithoutPeriod 剔除了形成期的股票池
     * @param numOfStocks             参与回测的股票数量
     * @return 每个股票第一天的数据
     */
    private Map<String, StockPO> getFirstTransactionData(Map<Date, List<StockPO>> securitiesWithoutPeriod, int numOfStocks) {
        Map<Date, List<StockPO>> all = new TreeMap<>(securitiesWithoutPeriod);
        Map<String, StockPO> firstDay = new HashMap<>();

        // 记录已经找到第一天数据的股票数量
        int cnt = 0;
        for (Map.Entry<Date, List<StockPO>> entry : all.entrySet()) {
            // 全都找到了
            if (cnt == numOfStocks) {
                break;
            }

            for (StockPO stock : entry.getValue()) {
                // 已经找到过这一只的了
                if (firstDay.containsKey(stock.getStockId())) {
                    continue;
                } else {
                    firstDay.put(stock.getStockId(), stock);
                    cnt++;
                }
            }
        }

        return firstDay;
    }

    /**
     * 得到持有期内都有数据的股票
     *
     * @param date     当前日期
     * @param dateList 日期列表（包含形成期）
     * @return 持有期内都有数据的股票
     */
    protected List<StockPO> holdingPeriodFilter(Date date, List<Date> dateList) throws UnsupportedOperationException {
        int index = dateList.indexOf(date);

        List<StockPO> currentDayInfo = securities.get(date);

        List<List<StockPO>> lists = new ArrayList<>();

        for (int offset = 0; offset < holding; offset++) {
            if (offset + index >= dateList.size()) {
                break;
            }
            lists.add(securities.get(dateList.get(offset + index)));
        }

        Map<StockPO, Integer> map = new HashMap<>();
        currentDayInfo.forEach(stock -> map.put(stock, 0));

        for (List<StockPO> list : lists) {
            for (StockPO stockPO : list) {
                if (map.get(stockPO) == null) {
                    map.put(stockPO, 0);
                } else {
                    map.put(stockPO, map.get(stockPO).intValue() + 1);
                }
            }
        }

        List<StockPO> result = new ArrayList<>();

        int size = dateList.size() - index;

        if (size >= holding) {
            size = holding;
        }

        for (Map.Entry<StockPO, Integer> entry : map.entrySet()) {
            if (entry.getValue() == size) {
                result.add(entry.getKey());
            }
        }

        if (result.size() == 0) {
            throw new UnsupportedOperationException();
        }
        return result;
    }

    /**
     * 得到某一股票某一日的股票信息
     *
     * @param stockCode 股票代码
     * @param date      日期
     * @return 股票
     */
    protected StockPO getStockByCodeAndDay(String stockCode, Date date) {
        List<StockPO> stocks = securities.get(date);
        if (stocks == null) {
            return null;
        }
        StockPO stock = stocks.stream().filter(s -> s.getStockId().equals(stockCode)).findFirst().orElse(null);
        return stock;
    }
}
