package oquantour.service.servicehelper.analysis.backtest;

import oquantour.exception.UnsupportedOperationException;
import oquantour.po.StockPO;
import oquantour.po.util.BackTestStatistics;
import oquantour.po.util.BackTestWinner;
import oquantour.po.util.BackTestingSingleChartInfo;
import oquantour.po.util.ChartInfo;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 回测结果分析
 * <p>
 * Created by keenan on 16/05/2017.
 */
public class BackTestAnalysis {
    /**
     * 得到收益率分布
     *
     * @return 收益率分布
     * @throws UnsupportedOperationException 非法操作
     */
    public static List<Double> getReturnRateDistribution(List<ChartInfo> backTestReturnRate, int holding) throws UnsupportedOperationException {
        List<Double> returnRateDistribution = new ArrayList<>();
        if (backTestReturnRate.isEmpty()) {
            throw new UnsupportedOperationException();
        }

        //循环计算每个持有期的收益率
        int offset = 0;
        for (; offset + holding <= backTestReturnRate.size(); offset += holding) {
            double startValue = backTestReturnRate.get(offset).getyAxis();
            double endValue = backTestReturnRate.get(offset + holding - 1).getyAxis();
            returnRateDistribution.add((endValue - startValue) / (1 + startValue));
        }

        if (offset != backTestReturnRate.size() && offset + holding > backTestReturnRate.size()) {
            double startValue = backTestReturnRate.get(offset).getyAxis();
            double endValue = backTestReturnRate.get(backTestReturnRate.size() - 1).getyAxis();

            returnRateDistribution.add((endValue - startValue) / (1 + startValue));
        }

        return returnRateDistribution;
    }

    /**
     * 得到回测统计数据
     *
     * @return 回测统计数据
     */
    public static BackTestStatistics getBackTestStatistics(List<BackTestingSingleChartInfo> backTestingSingleChartInfos) {

        BackTestStatistics backTestStatistics = new BackTestStatistics(backTestingSingleChartInfos);

        return backTestStatistics;
    }

    /**
     * 策略评分
     * 评分规则：起始为1000分
     *
     * @param backTestStatistics 回测数据
     * @param backTestResult     回测结果（策略收益率和基准收益率）
     * @param backTestWinners    赢家组合
     * @return 策略评分
     */
    @SuppressWarnings("Duplicates")
    public static double getStrategyScore(BackTestStatistics backTestStatistics, List<BackTestingSingleChartInfo> backTestResult, List<BackTestWinner> backTestWinners) {
        double init = 1000.00;
        int win = 0;
        int lose = 0;

        for (BackTestingSingleChartInfo backTestingSingleChartInfo : backTestResult) {
            if (backTestingSingleChartInfo.getBackTestValue() > backTestingSingleChartInfo.getStdValue()) {
                win++;
            } else if (backTestingSingleChartInfo.getBackTestValue() < backTestingSingleChartInfo.getStdValue()) {
                lose++;
            }
        }

        init += (win - lose) * 1;

        init += backTestStatistics.getSharpe() * 30;

        init -= (backTestStatistics.getAlgorithmVolatility() - backTestStatistics.getBenchmarkVolatility()) * 20;

        init -= (backTestStatistics.getMaxDrawdown()) * 10;

        DecimalFormat decimalFormat = new DecimalFormat("#.0");
        return Double.valueOf(decimalFormat.format(init));
    }

    /**
     * 风险指标（雷达图）: 都投影到 [0, 1]
     *
     * @param backTestStatistics 回测统计数据
     * @param backTestResult     回测结果（策略收益率和基准收益率）
     * @param backTestWinners    赢家组合
     * @return 风险指标 0:抗风险能力 1:稳定性 2:盈利能力 3:持股分散度
     */
    @SuppressWarnings("Duplicates")
    public static double[] getIndexes(BackTestStatistics backTestStatistics, List<BackTestingSingleChartInfo> backTestResult, List<BackTestWinner> backTestWinners) {
        double[] indices = new double[4];
        // 抗风险能力：反映组合最大回撤的大小。最大回撤用来描述买入组合后可能出现的最糟糕收益情况，是一个重要的风险指标。
        double antiRisk = backTestStatistics.getMaxDrawdown();
        indices[0] = antiRisk;

        // 稳定性：反映组合收益是否稳定。净值变化波动越小的组合，在该项指标上分值越高。
        double stability;
        if (backTestStatistics.getAlgorithmVolatility() >= 1) {
            stability = 1;
        } else if (backTestStatistics.getAlgorithmVolatility() <= 0) {
            stability = 0;
        } else {
            stability = backTestStatistics.getAlgorithmVolatility();
        }
        indices[1] = stability;

        // 盈利能力：反映组合的赚钱能力，组合收益率越高，该项分值越高。
        double profitability;
        double win = 0;
        double lose = 0;
        for (BackTestingSingleChartInfo backTestingSingleChartInfo : backTestResult) {
            if (backTestingSingleChartInfo.getBackTestValue() > backTestingSingleChartInfo.getStdValue()) {
                win++;
            } else if (backTestingSingleChartInfo.getBackTestValue() < backTestingSingleChartInfo.getStdValue()) {
                lose++;
            }
        }
        profitability = (win) / (win + lose);
        indices[2] = profitability;
        // 持股分散度：考核组合持股是否分散，持股越分散，该项分值越高。
        double divertion;
        Map<String, Integer> map = new HashMap<>();
        for (BackTestWinner backTestWinner : backTestWinners) {
            for (StockPO stock : backTestWinner.getShares().keySet()) {
                if (map.get(stock.getStockId()) == null) {
                    map.put(stock.getStockId(), 0);
                } else {
                    int val = map.get(stock.getStockId());
                    map.put(stock.getStockId(), val + 1);
                }
            }
        }

        double size = 0;
        for (Integer integer : map.values()) {
            size += integer;
        }
        size = size / map.values().size();

        double dot = 0;
        for (String s : map.keySet()) {
            if (map.get(s) <= size) {
                dot++;
            }
        }

        divertion = dot / map.keySet().size();

        indices[3] = divertion;

        return indices;
    }
}
