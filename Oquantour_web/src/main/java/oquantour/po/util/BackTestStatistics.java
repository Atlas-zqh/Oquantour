package oquantour.po.util;

import oquantour.po.util.BackTestingSingleChartInfo;
import oquantour.util.tools.Calculator;

import java.util.ArrayList;
import java.util.List;

/**
 * 回测结果统计数据
 * <p>
 * Created by keenan on 08/05/2017.
 */
public class BackTestStatistics {
    // 年化收益率
    private double annualizedReturnRate;
    // 基准年化收益率
    private double stdAnnualizedReturnRate;
    // 阿尔法
    private double alpha;
    // 贝塔
    private double beta;
    // 夏普比率
    private double sharpe;
    // 信息比率
    private double informationRatio;
    // 策略收益波动率
    private double algorithmVolatility;
    // 基准收益波动率
    private double benchmarkVolatility;
    // 最大回撤
    private double maxDrawdown;

    private List<BackTestingSingleChartInfo> backTestResult;

    public BackTestStatistics() {
    }

    public BackTestStatistics(List<BackTestingSingleChartInfo> backTestingSingleChartInfos) {
        backTestResult = new ArrayList<>(backTestingSingleChartInfos);
        // 计算基本数据，计算顺序不能改
        this.annualizedReturnRate = calAnnualizedReturnRate();
        this.stdAnnualizedReturnRate = calStdAnnualizedReturnRate();
        this.beta = calBeta();
        this.algorithmVolatility = calAlgorithmVolatility();

        // 计算其他数据
        this.alpha = calAlpha();
        this.sharpe = calSharpe();
        this.informationRatio = calInformationRatio();
        this.benchmarkVolatility = calBenchmarkVolatility();
        this.maxDrawdown = calMaxDrawDown();
    }

    // getters and setters
    public double getAnnualizedReturnRate() {
        return annualizedReturnRate;
    }

    public void setAnnualizedReturnRate(double annualizedReturnRate) {
        this.annualizedReturnRate = annualizedReturnRate;
    }

    public double getStdAnnualizedReturnRate() {
        return stdAnnualizedReturnRate;
    }

    public void setStdAnnualizedReturnRate(double stdAnnualizedReturnRate) {
        this.stdAnnualizedReturnRate = stdAnnualizedReturnRate;
    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public double getBeta() {
        return beta;
    }

    public void setBeta(double beta) {
        this.beta = beta;
    }

    public double getSharpe() {
        return sharpe;
    }

    public void setSharpe(double sharpe) {
        this.sharpe = sharpe;
    }

    public double getInformationRatio() {
        return informationRatio;
    }

    public void setInformationRatio(double informationRatio) {
        this.informationRatio = informationRatio;
    }

    public double getAlgorithmVolatility() {
        return algorithmVolatility;
    }

    public void setAlgorithmVolatility(double algorithmVolatility) {
        this.algorithmVolatility = algorithmVolatility;
    }

    public double getBenchmarkVolatility() {
        return benchmarkVolatility;
    }

    public void setBenchmarkVolatility(double benchmarkVolatility) {
        this.benchmarkVolatility = benchmarkVolatility;
    }

    public double getMaxDrawdown() {
        return maxDrawdown;
    }

    public void setMaxDrawdown(double maxDrawdown) {
        this.maxDrawdown = maxDrawdown;
    }

    // cal values

    /**
     * 计算策略年化收益率
     *
     * @return 策略年化收益率
     */
    private double calAnnualizedReturnRate() {
        // 回测期内策略的收益率，即回测期最后一天的策略收益率
        double lastDayBackTestValue = backTestResult.get(backTestResult.size() - 1).getBackTestValue();
        // 策略执行天数
        int days = backTestResult.size();

        double annualizedReturnRate;

        annualizedReturnRate = Math.pow((1 + lastDayBackTestValue), (250.0 / days)) - 1;

        return annualizedReturnRate;
    }

    /**
     * 计算基准年化收益率
     *
     * @return 基准年化收益率
     */
    private double calStdAnnualizedReturnRate() {
        // 回测期内基准的收益率，即回测期最后一天的基准收益率
        double lastDayStdValue = backTestResult.get(backTestResult.size() - 1).getStdValue();
        // 策略执行天数
        int days = backTestResult.size();

        double stdAnnualizedReturnRate;

        stdAnnualizedReturnRate = Math.pow((1 + lastDayStdValue), (250.0 / days)) - 1;

        return stdAnnualizedReturnRate;
    }

    /**
     * 计算阿尔法值
     * Alpha是投资者获得与市场波动无关的回报，比如投资者获得了15%的回报，其基准获得了10%的回报，那么Alpha或者价值增值的部分就是5%
     *
     * @return alpha
     */
    private double calAlpha() {
        return this.annualizedReturnRate - (0.04 + this.beta * (this.stdAnnualizedReturnRate - 0.04));
    }

    /**
     * 计算贝塔值
     * 表示投资的系统性风险，反映了策略对大盘变化的敏感性。
     * 例如一个策略的Beta为1.5，则大盘涨1%的时候，策略可能涨1.5%，反之亦然；
     * 如果一个策略的Beta为-1.5，说明大盘涨1%的时候，策略可能跌1.5%，反之亦然。
     *
     * @return beta
     */
    private double calBeta() {
        // 策略每日收益
        List<Double> strategyDailyReturn = new ArrayList<>();
        // 基准每日收益
        List<Double> stdDailyReturn = new ArrayList<>();

        for (BackTestingSingleChartInfo backTestingSingleChartVO : backTestResult) {
            stdDailyReturn.add(backTestingSingleChartVO.getStdValue());
            strategyDailyReturn.add(backTestingSingleChartVO.getBackTestValue());
        }

        // 策略每日收益与基准每日收益的协方差
        double cov = Calculator.cov(stdDailyReturn, strategyDailyReturn);
        // 基准每日收益的方差
        double variance = Calculator.variance(stdDailyReturn);

        return (cov / variance);
    }

    /**
     * 计算夏普比率
     * 表示每承受一单位总风险，会产生多少的超额报酬，可以同时对策略的收益与风险进行综合考虑。
     *
     * @return 夏普比率
     */
    private double calSharpe() {
        return (this.annualizedReturnRate - 0.04) / this.algorithmVolatility;
    }

    /**
     * 计算信息比率
     * 衡量单位超额风险带来的超额收益。信息比率越大，说明该策略单位跟踪误差所获得的超额收益越高，
     * 因此，信息比率较大的策略的表现要优于信息比率较低的基准。合理的投资目标应该是在承担适度风险下，尽可能追求高信息比率。
     *
     * @return 信息比率
     */
    private double calInformationRatio() {
        // 策略与基准的每日收益差值
        List<Double> strategyMinusBenchmark = new ArrayList<>();

        backTestResult.stream().forEach(result -> strategyMinusBenchmark.add(result.getBackTestValue() - result.getStdValue()));

        double minusStdDev = Calculator.std(strategyMinusBenchmark);

        double annualizedMinusStdDev = Math.sqrt(250.0 / strategyMinusBenchmark.size()) * minusStdDev;

        return (annualizedReturnRate - stdAnnualizedReturnRate) / annualizedMinusStdDev;
    }

    /**
     * 计算策略波动率
     * 用来测量策略的风险性，波动越大代表策略风险越高。
     *
     * @return 策略波动率
     */
    private double calAlgorithmVolatility() {
        List<Double> dailyStrategyReturnRate = new ArrayList<>();

        backTestResult.stream().forEach(result -> dailyStrategyReturnRate.add(result.getBackTestValue()));

        double avg = Calculator.avg(dailyStrategyReturnRate);

        double minusSquareSum = 0;

        for (Double value : dailyStrategyReturnRate) {
            minusSquareSum += (value - avg) * (value - avg);
        }

        return Math.sqrt((250.0 / dailyStrategyReturnRate.size()) * minusSquareSum);
    }

    /**
     * 计算基准波动率
     * 用来测量基准的风险性，波动越大代表基准风险越高。
     *
     * @return 基准波动率
     */
    private double calBenchmarkVolatility() {
        List<Double> dailyBenchmarkReturnRate = new ArrayList<>();

        backTestResult.stream().forEach(result -> dailyBenchmarkReturnRate.add(result.getStdValue()));

        double avg = Calculator.avg(dailyBenchmarkReturnRate);

        double minusSquareSum = 0;

        for (Double value : dailyBenchmarkReturnRate) {
            minusSquareSum += (value - avg) * (value - avg);
        }

        return Math.sqrt((250.0 / dailyBenchmarkReturnRate.size()) * minusSquareSum);
    }

    /**
     * 计算最大回撤
     * 描述策略可能出现的最糟糕的情况，最极端可能的亏损情况。
     * 具体计算方法为 max(1 - 策略当日价值 / 当日之前虚拟账户最高价值)
     *
     * @return 最大回撤
     */
    @SuppressWarnings("Duplicates")
    private double calMaxDrawDown() {
        List<Double> dailyStrategyReturnRate = new ArrayList<>();
        List<Double> possibleResults = new ArrayList<>();

        for (BackTestingSingleChartInfo backTestingSingleChartVO : backTestResult) {
            dailyStrategyReturnRate.add(backTestingSingleChartVO.getBackTestValue());
        }

        for (int currentPos = 0; currentPos < dailyStrategyReturnRate.size(); currentPos++) {
            double max = Double.MIN_VALUE;

            for (int j = 0; j < currentPos; j++) {
                if (dailyStrategyReturnRate.get(j) > max) {
                    max = dailyStrategyReturnRate.get(j);
                }
            }

            possibleResults.add(1.0 - ((1.0 + dailyStrategyReturnRate.get(currentPos)) / (1.0 + max)));
        }

        return Calculator.max(possibleResults);
    }

}
