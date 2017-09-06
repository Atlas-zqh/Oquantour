package oquantour.po.util;

import java.util.List;

/**
 * Created by keenan on 08/05/2017.
 */
public class BackTestResult {
    // 基准收益率和策略收益率对比
    private List<BackTestingSingleChartInfo> backTestingSingleChartInfos;

    // 回测统计数据
    private BackTestStatistics backTestStatistics;

    // 每个调仓日的持仓股票详情
    private List<BackTestWinner> dailyWinners;

    // 收益率分布
    private List<Double> returnRateDistribution;

    // 策略评分
    private double score;

    // 风险指标
    private double[] indices;

    // 最佳持有期结果
    private List<BackTestBestInfo> bestHolding;

    // 最佳持仓数结果
    private List<BackTestBestInfo> bestHoldingNum;

    public BackTestResult() {
    }

    public BackTestResult(List<BackTestingSingleChartInfo> backTestingSingleChartInfos, BackTestStatistics backTestStatistics, List<BackTestWinner> dailyWinners, List<Double> returnRateDistribution, double score, double[] indices) {
        this.backTestingSingleChartInfos = backTestingSingleChartInfos;
        this.backTestStatistics = backTestStatistics;
        this.dailyWinners = dailyWinners;
        this.returnRateDistribution = returnRateDistribution;
        this.score = score;
        this.indices = indices;
    }

    public List<BackTestingSingleChartInfo> getBackTestingSingleChartInfos() {
        return backTestingSingleChartInfos;
    }

    public void setBackTestingSingleChartInfos(List<BackTestingSingleChartInfo> backTestingSingleChartInfos) {
        this.backTestingSingleChartInfos = backTestingSingleChartInfos;
    }

    public BackTestStatistics getBackTestStatistics() {
        return backTestStatistics;
    }

    public void setBackTestStatistics(BackTestStatistics backTestStatistics) {
        this.backTestStatistics = backTestStatistics;
    }

    public List<BackTestWinner> getDailyWinners() {
        return dailyWinners;
    }

    public void setDailyWinners(List<BackTestWinner> dailyWinners) {
        this.dailyWinners = dailyWinners;
    }

    public List<Double> getReturnRateDistribution() {
        return returnRateDistribution;
    }

    public void setReturnRateDistribution(List<Double> returnRateDistribution) {
        this.returnRateDistribution = returnRateDistribution;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public double[] getIndices() {
        return indices;
    }

    public void setIndices(double[] indices) {
        this.indices = indices;
    }
    //    private List<Double> calReturnRateDistribution(List<BackTestingSingleChartInfo> backTestingSingleChartInfos, int holding) {
//        List<Double> returnRateDistribution = new ArrayList<>();
//        //循环计算每个持有期的收益率
//        int offset = 0;
//        for (; offset + holding <= backTestingSingleChartInfos.size(); offset += holding) {
//            double startValue = backTestingSingleChartInfos.get(offset).getBackTestValue();
//            double endValue = backTestingSingleChartInfos.get(offset + holding - 1).getBackTestValue();
//            returnRateDistribution.add((endValue - startValue) / (1 + startValue));
//        }
//
//        if (offset != backTestingSingleChartInfos.size() && offset + holding > backTestingSingleChartInfos.size()) {
//            double startValue = backTestingSingleChartInfos.get(offset).getBackTestValue();
//            double endValue = backTestingSingleChartInfos.get(backTestingSingleChartInfos.size() - 1).getBackTestValue();
//
//            returnRateDistribution.add((endValue - startValue) / (1 + startValue));
//        }
//        return returnRateDistribution;
//    }


    public List<BackTestBestInfo> getBestHolding() {
        return bestHolding;
    }

    public void setBestHolding(List<BackTestBestInfo> bestHolding) {
        this.bestHolding = bestHolding;
    }

    public List<BackTestBestInfo> getBestHoldingNum() {
        return bestHoldingNum;
    }

    public void setBestHoldingNum(List<BackTestBestInfo> bestHoldingNum) {
        this.bestHoldingNum = bestHoldingNum;
    }
}
