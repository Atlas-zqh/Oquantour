package oquantour.service.serviceImpl;

import oquantour.data.dao.StockDao;
import oquantour.exception.BackTestErrorException;
import oquantour.exception.FormativeNotExistsException;
import oquantour.exception.StockLessThanOneHundredException;
import oquantour.exception.UnsupportedOperationException;
import oquantour.po.StockPO;
import oquantour.po.util.*;
import oquantour.service.BackTestService;
import oquantour.service.servicehelper.analysis.backtest.BackTestAnalysis;
import oquantour.service.servicehelper.strategy.*;
import oquantour.util.tools.CombinationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by keenan on 09/05/2017.
 */
@Transactional
@Service
public class BackTestServiceImpl implements BackTestService {
    @Autowired
    private StockDao stockDao;

    @Override
    public BackTestResult getBackTestResult(BackTestInfo backTestInfo) throws BackTestErrorException {
        Map<Date, List<StockPO>> securities = getSecurities(backTestInfo);

        if (securities == null) {
            throw new BackTestErrorException("\uD83D\uDE14\uD83D\uDE14\uD83D\uDE14\uD83D\uDE14</br>这段时间有效回测股票数量不足100只</br>选满再试试吧\uD83D\uDE14\uD83D\uDE14\uD83D\uDE14\uD83D\uDE14");
        }

        StrategyType strategyType = backTestInfo.getStrategyType();

        Strategy strategy;
        switch (strategyType) {
            case Momentum:
                strategy = new Momentum(securities, backTestInfo.getFormativePeriod(), backTestInfo.getHoldingPeriod(), backTestInfo.getStartDate(), backTestInfo.getStocks().size());
                break;
            case MeanReversion:
                strategy = new MeanReversion(securities, backTestInfo.getHoldingPeriod(), backTestInfo.getStartDate(), backTestInfo.getStocks().size(), backTestInfo.getMa_length(), backTestInfo.getMaxholdingStocks());
                break;
            case SmallMarketValueWheeled:
                strategy = new SmallMarketValWheeled(securities, backTestInfo.getFormativePeriod(), backTestInfo.getHoldingPeriod(), backTestInfo.getStartDate(), backTestInfo.getStocks().size(), backTestInfo.getMaxholdingStocks());
                break;
            default:
                strategy = new DIY_Strategy(securities, backTestInfo.getHoldingPeriod(), backTestInfo.getStartDate(), backTestInfo.getStocks().size(), backTestInfo.getPriceIndicesRange(), backTestInfo.getPriceIndicesWeight(), backTestInfo.getMaxholdingStocks());
        }


        try {
            List<ChartInfo> backTestReturnRate = strategy.getBackTestReturnRate();
            List<ChartInfo> benchmarkReturnRate = strategy.getBenchmarkReturnRate();
            List<BackTestingSingleChartInfo> backTestingSingleChartInfos = CombinationHelper.combinedReturnRate(backTestReturnRate, benchmarkReturnRate);
            List<Double> returnRateDistribution = BackTestAnalysis.getReturnRateDistribution(backTestReturnRate, backTestInfo.getHoldingPeriod());
            BackTestStatistics backTestStatistics = BackTestAnalysis.getBackTestStatistics(backTestingSingleChartInfos);
            List<BackTestWinner> winners = strategy.getDailyWinners();
            double score = BackTestAnalysis.getStrategyScore(backTestStatistics, backTestingSingleChartInfos, winners);
            double[] indices = BackTestAnalysis.getIndexes(backTestStatistics, backTestingSingleChartInfos, winners);

            BackTestResult backTestResult = new BackTestResult(backTestingSingleChartInfos, backTestStatistics, winners, returnRateDistribution, score, indices);

            List<BackTestBestInfo> bestHolding = findBestHolding(backTestInfo, securities);
            List<BackTestBestInfo> bestHoldingNum = findBestHoldingNum(backTestInfo, securities);

            backTestResult.setBestHolding(bestHolding);
            backTestResult.setBestHoldingNum(bestHoldingNum);

            return backTestResult;
        } catch (UnsupportedOperationException e) {
            throw new BackTestErrorException("\uD83D\uDE32\uD83D\uDE32\uD83D\uDE32\uD83D\uDE32</br>哎呀，好像哪里算错了</br>换些条件再来一次吧</br>\uD83D\uDE32\uD83D\uDE32\uD83D\uDE32\uD83D\uDE32");
        }
    }

    /**
     * 获得股票池
     *
     * @param backTestInfo 回测信息
     * @return 股票池
     */
    private Map<Date, List<StockPO>> getSecurities(BackTestInfo backTestInfo) {
        try {
            System.out.println("筛股票11");
            Map<Date, List<StockPO>> securities = stockDao.filterStock(backTestInfo.getStartDate(), backTestInfo.getEndDate(), backTestInfo.getStocks(), backTestInfo.getFormativePeriod(), backTestInfo.isFilter_ST(), backTestInfo.isFilter_NoData(), backTestInfo.isFilter_Suspension(), backTestInfo.isIgnore_100());
            System.out.println("筛股票22");
            return securities;
        } catch (FormativeNotExistsException e) {
            return null;
        } catch (StockLessThanOneHundredException e) {
            return null;
        }
    }

    /**
     * 寻找最佳持有期（其余数据固定）
     *
     * @param backTestInfo 回测信息
     */
    @SuppressWarnings("Duplicates")
    private List<BackTestBestInfo> findBestHolding(BackTestInfo backTestInfo, Map<Date, List<StockPO>> securities) throws BackTestErrorException {
        if (securities == null) {
            throw new BackTestErrorException("该段时间有效回测股票数量不足");
        }

        Strategy strategy;
        switch (backTestInfo.getStrategyType()) {
            case Momentum:
                strategy = new Momentum(securities, backTestInfo.getFormativePeriod(), 2, backTestInfo.getStartDate(), backTestInfo.getStocks().size());
                break;
            case MeanReversion:
                strategy = new MeanReversion(securities, 2, backTestInfo.getStartDate(), backTestInfo.getStocks().size(), backTestInfo.getMa_length(), backTestInfo.getMaxholdingStocks());
                break;
            case SmallMarketValueWheeled:
                strategy = new SmallMarketValWheeled(securities, backTestInfo.getFormativePeriod(), 2, backTestInfo.getStartDate(), backTestInfo.getStocks().size(), backTestInfo.getMaxholdingStocks());
                break;
            default:
                strategy = new DIY_Strategy(securities, 2, backTestInfo.getStartDate(), backTestInfo.getStocks().size(), backTestInfo.getPriceIndicesRange(), backTestInfo.getPriceIndicesWeight(), backTestInfo.getMaxholdingStocks());
        }

        List<BackTestBestInfo> backTestBestInfos = new ArrayList<>();

        try {
            BackTestBestInfo backTestBestInfo = encapsulateBestInfo(strategy, 2);

            backTestBestInfos.add(backTestBestInfo);

            for (int holding = 4; holding <= 40; holding += 2) {
                strategy.setHolding(holding);

                System.out.println(strategy.getHolding());
                BackTestBestInfo backTestBestInfo1 = encapsulateBestInfo(strategy, holding);
                backTestBestInfos.add(backTestBestInfo1);
            }
        } catch (UnsupportedOperationException e) {
            throw new BackTestErrorException("好像哪里出错了，换批股票再来一次吧");
        }

        return backTestBestInfos;
    }

    /**
     * 寻找最佳持仓股票数
     *
     * @param backTestInfo 回测信息
     */
    private List<BackTestBestInfo> findBestHoldingNum(BackTestInfo backTestInfo, Map<Date, List<StockPO>> securities) throws BackTestErrorException {
        if (securities == null) {
            throw new BackTestErrorException("该段时间有效回测股票数量不足");
        }

        Strategy strategy;
        switch (backTestInfo.getStrategyType()) {
            case Momentum:
                return Collections.EMPTY_LIST;
            case MeanReversion:
                strategy = new MeanReversion(securities, backTestInfo.getHoldingPeriod(), backTestInfo.getStartDate(), backTestInfo.getStocks().size(), backTestInfo.getMa_length(), 5);
                break;
            case SmallMarketValueWheeled:
                strategy = new SmallMarketValWheeled(securities, backTestInfo.getFormativePeriod(), backTestInfo.getHoldingPeriod(), backTestInfo.getStartDate(), backTestInfo.getStocks().size(), 5);
                break;
            default:
                strategy = new DIY_Strategy(securities, backTestInfo.getHoldingPeriod(), backTestInfo.getStartDate(), backTestInfo.getStocks().size(), backTestInfo.getPriceIndicesRange(), backTestInfo.getPriceIndicesWeight(), 5);
        }

        List<BackTestBestInfo> backTestBestInfos = new ArrayList<>();

        try {
            BackTestBestInfo backTestBestInfo = encapsulateBestInfo(strategy, 5);

            backTestBestInfos.add(backTestBestInfo);

            for (int maxHolding = 10; maxHolding <= 80; maxHolding += 5) {
                strategy.setHolding(maxHolding);
                System.out.println(strategy.getHolding());
                BackTestBestInfo backTestBestInfo1 = encapsulateBestInfo(strategy, maxHolding);
                backTestBestInfos.add(backTestBestInfo1);
            }
        } catch (UnsupportedOperationException e) {
            throw new BackTestErrorException("好像哪里出错了，换批股票再来一次吧");
        }

        return backTestBestInfos;
    }

    /**
     * 封装最佳数据
     *
     * @param strategy 策略
     * @return 最佳数据
     */
    private BackTestBestInfo encapsulateBestInfo(Strategy strategy, double x_axis) throws UnsupportedOperationException, BackTestErrorException {
        List<ChartInfo> backTestReturnRate = strategy.getBackTestReturnRate();

        List<ChartInfo> benchmarkReturnRate = strategy.getBenchmarkReturnRate();
        List<BackTestingSingleChartInfo> backTestingSingleChartInfos = CombinationHelper.combinedReturnRate(backTestReturnRate, benchmarkReturnRate);
        List<Double> returnRateDistribution = BackTestAnalysis.getReturnRateDistribution(backTestReturnRate, 2);
        BackTestStatistics backTestStatistics = BackTestAnalysis.getBackTestStatistics(backTestingSingleChartInfos);
        double winningRate = getWinningRate(returnRateDistribution);
        double extraReturnRate = getExtraReturnRate(backTestingSingleChartInfos);


        System.out.println(x_axis + " " + backTestReturnRate.size() + " " + winningRate + " " + extraReturnRate);
        return new BackTestBestInfo(x_axis, backTestStatistics, extraReturnRate, winningRate);
    }

    /**
     * 计算策略胜率
     *
     * @param returnRateDistribution 收益率分布
     * @return 胜率
     */
    private double getWinningRate(List<Double> returnRateDistribution) {
        long winningTimes = returnRateDistribution.stream().filter(s -> (s > 0)).count();
        return (double) winningTimes / returnRateDistribution.size();
    }

    /**
     * 计算超额收益率
     *
     * @param backTestingSingleChartInfos 回测结果
     * @return 超额收益率
     */
    private double getExtraReturnRate(List<BackTestingSingleChartInfo> backTestingSingleChartInfos) {
        BackTestingSingleChartInfo backTestingSingleChartInfo = backTestingSingleChartInfos.get(backTestingSingleChartInfos.size() - 1);
        return backTestingSingleChartInfo.getBackTestValue() - backTestingSingleChartInfo.getStdValue();
    }
}
