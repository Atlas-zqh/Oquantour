package bl.strategy.statistics;

import bl.tools.Calculator;
import vo.BackTestingSingleChartVO;
import vo.StrategyStatisticsVO;

import java.util.*;

/**
 * 获得统计数据
 * <p>
 * Created by keenan on 28/03/2017.
 */
public class BackTestStatisticsBLHelper {
    private List<BackTestingSingleChartVO> backTestResult;

    private double annualizedReturnRate;

    private double stdAnnualizedReturnRate;

    private double beta;

    private double algorithmVolatility;

    public BackTestStatisticsBLHelper(List<BackTestingSingleChartVO> backTestResult) {
        this.backTestResult = backTestResult;
        calBasicElements();
    }

    /**
     * 计算基本的四个数据：年化收益率、基准年化收益率、贝塔、策略波动率
     */
    private void calBasicElements() {
        annualizedReturnRate = getAnnualizedReturnRate();
        stdAnnualizedReturnRate = getStdAnnualizedReturnRate();
        beta = getBeta();
        algorithmVolatility = getAlgorithmVolatility();
    }

    /**
     * 得到回测统计数据
     *
     * @return
     */
    public StrategyStatisticsVO getStatistics() {
        // 年化收益率
        double annualizedReturnRate = this.annualizedReturnRate;
        // 基准年化收益率
        double stdAnnualizedReturnRate = this.stdAnnualizedReturnRate;
        // 阿尔法
        double alpha = getAlpha();
        // 贝塔
        double beta = this.beta;
        // 夏普比率
        double sharpe = getSharpe();
        // 信息比率
        double informationRatio = getInformationRatio();
        // 策略收益波动率
        double algorithmVolatility = this.algorithmVolatility;
        // 基准收益波动率
        double benchmarkVolatility = getBenchmarkVolatility();
        // 最大回撤
        double maxDrawdown = getMaxDrawdown();

        return new StrategyStatisticsVO(annualizedReturnRate, stdAnnualizedReturnRate, alpha, beta, sharpe,
                informationRatio, algorithmVolatility, benchmarkVolatility, maxDrawdown);
    }

    /**
     * 得到阿尔法值
     * Alpha是投资者获得与市场波动无关的回报，比如投资者获得了15%的回报，其基准获得了10%的回报，那么Alpha或者价值增值的部分就是5%
     *
     * @return alpha
     */
    private double getAlpha() {
        return this.annualizedReturnRate - (0.04 + this.beta * (this.stdAnnualizedReturnRate - 0.04));
    }

    /**
     * 得到贝塔值
     * 表示投资的系统性风险，反映了策略对大盘变化的敏感性。
     * 例如一个策略的Beta为1.5，则大盘涨1%的时候，策略可能涨1.5%，反之亦然；
     * 如果一个策略的Beta为-1.5，说明大盘涨1%的时候，策略可能跌1.5%，反之亦然。
     *
     * @return beta
     */
    private double getBeta() {
        // 策略每日收益
        List<Double> strategyDailyReturn = new ArrayList<>();
        // 基准每日收益
        List<Double> stdDailyReturn = new ArrayList<>();
//
//        System.out.println("****************** Statistics *******************");
        for (BackTestingSingleChartVO backTestingSingleChartVO : backTestResult) {
            stdDailyReturn.add(backTestingSingleChartVO.stdValue);
            strategyDailyReturn.add(backTestingSingleChartVO.backTestValue);
//            System.out.println("zqh3                      benchmark: " + backTestingSingleChartVO.stdValue);
//            System.out.println("zqh3                      back test: " + backTestingSingleChartVO.backTestValue);
        }

        // 策略每日收益与基准每日收益的协方差
        double cov = Calculator.cov(stdDailyReturn, strategyDailyReturn);
        // 基准每日收益的方差
        double variance = Calculator.variance(stdDailyReturn);

        return (cov / variance);
    }

    /**
     * 得到夏普比率
     * 表示每承受一单位总风险，会产生多少的超额报酬，可以同时对策略的收益与风险进行综合考虑。
     *
     * @return 夏普比率
     */
    private double getSharpe() {
        return (this.annualizedReturnRate - 0.04) / this.algorithmVolatility;
    }

    /**
     * 得到策略年化收益率
     *
     * @return 策略年化收益率
     */
    private double getAnnualizedReturnRate() {
        // 回测期内策略的收益率，即回测期最后一天的策略收益率
        double lastDayBackTestValue = backTestResult.get(backTestResult.size() - 1).backTestValue;
        // 策略执行天数
        int days = backTestResult.size();

        double annualizedReturnRate;

        annualizedReturnRate = Math.pow((1 + lastDayBackTestValue), (250.0 / days)) - 1;

        return annualizedReturnRate;
    }

    /**
     * 得到基准年化收益率
     *
     * @return 基准年化收益率
     */
    private double getStdAnnualizedReturnRate() {
        // 回测期内基准的收益率，即回测期最后一天的基准收益率
        double lastDayStdValue = backTestResult.get(backTestResult.size() - 1).stdValue;
        // 策略执行天数
        int days = backTestResult.size();

        double stdAnnualizedReturnRate;

        stdAnnualizedReturnRate = Math.pow((1 + lastDayStdValue), (250.0 / days)) - 1;

        return stdAnnualizedReturnRate;
    }

    /**
     * 得到信息比率
     * 衡量单位超额风险带来的超额收益。信息比率越大，说明该策略单位跟踪误差所获得的超额收益越高，
     * 因此，信息比率较大的策略的表现要优于信息比率较低的基准。合理的投资目标应该是在承担适度风险下，尽可能追求高信息比率。
     *
     * @return 信息比率
     */
    private double getInformationRatio() {
        // 策略与基准的每日收益差值
        List<Double> strategyMinusBenchmark = new ArrayList<>();

        backTestResult.stream().forEach(result -> strategyMinusBenchmark.add(result.backTestValue - result.stdValue));

        double minusStdDev = Calculator.std(strategyMinusBenchmark);

        double annualizedMinusStdDev = Math.sqrt(250.0 / strategyMinusBenchmark.size()) * minusStdDev;

        return (annualizedReturnRate - stdAnnualizedReturnRate) / annualizedMinusStdDev;
    }

    /**
     * 得到策略波动率
     * 用来测量策略的风险性，波动越大代表策略风险越高。
     *
     * @return 策略波动率
     */
    private double getAlgorithmVolatility() {
        List<Double> dailyStrategyReturnRate = new ArrayList<>();

        backTestResult.stream().forEach(result -> dailyStrategyReturnRate.add(result.backTestValue));

        double avg = Calculator.avg(dailyStrategyReturnRate);

        double minusSquareSum = 0;

        for (Double value : dailyStrategyReturnRate) {
            minusSquareSum += (value - avg) * (value - avg);
        }

        return Math.sqrt((250.0 / dailyStrategyReturnRate.size()) * minusSquareSum);
    }

    /**
     * 得到基准波动率
     * 用来测量基准的风险性，波动越大代表基准风险越高。
     *
     * @return 基准波动率
     */
    private double getBenchmarkVolatility() {
        List<Double> dailyBenchmarkReturnRate = new ArrayList<>();

        backTestResult.stream().forEach(result -> dailyBenchmarkReturnRate.add(result.stdValue));

        double avg = Calculator.avg(dailyBenchmarkReturnRate);

        double minusSquareSum = 0;

        for (Double value : dailyBenchmarkReturnRate) {
            minusSquareSum += (value - avg) * (value - avg);
        }

        return Math.sqrt((250.0 / dailyBenchmarkReturnRate.size()) * minusSquareSum);
    }

    /**
     * 得到最大回撤
     * 描述策略可能出现的最糟糕的情况，最极端可能的亏损情况。
     * 具体计算方法为 max(1 - 策略当日价值 / 当日之前虚拟账户最高价值)
     *
     * @return 最大回撤
     */
    private double getMaxDrawdown() {
        List<Double> dailyStrategyReturnRate = new ArrayList<>();
        List<Double> possibleResults = new ArrayList<>();

        for (BackTestingSingleChartVO backTestingSingleChartVO : backTestResult) {
            dailyStrategyReturnRate.add(backTestingSingleChartVO.backTestValue);
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
