package vo;

/**
 * 回测统计量
 * <p>
 * Created by keenan on 23/03/2017.
 */
public class StrategyStatisticsVO {
    // 年化收益率
    public double annualizedReturnRate;
    // 基准年化收益率
    public double stdAnnualizedReturnRate;
    // 阿尔法
    public double alpha;
    // 贝塔
    public double beta;
    // 夏普比率
    public double sharpe;
    // 信息比率
    public double informationRatio;
    // 策略收益波动率
    public double algorithmVolatility;
    // 基准收益波动率
    public double benchmarkVolatility;
    // 最大回撤
    public double maxDrawdown;

    public StrategyStatisticsVO(){

    }

    public StrategyStatisticsVO(double annualizedReturnRate, double stdAnnualizedReturnRate, double alpha, double beta, double sharpe,
                                double informationRatio, double algorithmVolatility, double benchmarkVolatility, double maxDrawdown) {
        this.annualizedReturnRate = annualizedReturnRate;
        this.stdAnnualizedReturnRate = stdAnnualizedReturnRate;
        this.alpha = alpha;
        this.beta = beta;
        this.sharpe = sharpe;
        this.informationRatio = informationRatio;
        this.algorithmVolatility = algorithmVolatility;
        this.benchmarkVolatility = benchmarkVolatility;
        this.maxDrawdown = maxDrawdown;
    }
}
