package bl.strategy;

import bl.strategy.backtest.MeanReversion;
import bl.strategy.backtest.Momentum;
import bl.strategy.backtest.Strategy;
import bl.tools.StrategyType;
import exception.FormativePeriodNotExistException;
import exception.ParameterErrorException;
import exception.SelectStocksException;
import exception.WinnerSelectErrorException;
import vo.BackTestingInfoVO;
import vo.BackTestingJudgeVO;
import vo.BackTestingSingleChartVO;
import vo.WinningRateVO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by keenan on 11/04/2017.
 */
public class WinningRateBLServiceImpl implements WinningRateBLService {
    private Strategy strategy;

    /**
     * 得到超额收益率和策略胜率（固定形成期）
     * <p>
     * 持有期范围为2～60中的偶数
     *
     * @param backTestingInfoVO
     * @return 超额收益率和策略胜率
     */
    @Override
    public List<WinningRateVO> getWinningRate_FixedFormative(BackTestingInfoVO backTestingInfoVO) throws SelectStocksException, FormativePeriodNotExistException, ParameterErrorException, WinnerSelectErrorException {
        List<WinningRateVO> result = new ArrayList<>();
//        List<BackTestingSingleChartVO> backTestResult=new ArrayList<>();
        // 从第一次的回测中获取所有日期列表
        // 默认第一次回测持有期为 2
        backTestingInfoVO.holdingPeriod = 2;

        StrategyType strategyType = backTestingInfoVO.strategyType;


        strategy = (strategyType == StrategyType.MEANREVERSION) ? new MeanReversion(backTestingInfoVO) : new Momentum(backTestingInfoVO);

        // 判断能否回测（如果不能，则不会把它的数据放到数据中）
        // 同时，这一步也把validStockCode设置好，于是可以获取日期列表
        BackTestingJudgeVO backTestingJudgeVO = strategy.canBackTest();
        // 得到最大持有期长度
        int maxHoldingPeriod = strategy.getDateList().size();
        // 最大为60天
        if (maxHoldingPeriod >= 60) {
            maxHoldingPeriod = 60;
        }

        // 回过头去判断 持有期为2的时候，是否可以回测
        if (backTestingJudgeVO.getCanBackTest()) {
            result.add(encapsulateWinningRate(strategy.getBackTestResult(), 2));
        }


        // 设置持有期
        for (int i = 4; i <= maxHoldingPeriod; i += 2) {
            strategy.resetHoldingPeriod(i);
            result.add(encapsulateWinningRate(strategy.getBackTestResult(), i));
        }

        return result;
    }

    /**
     * 得到超额收益率和策略胜率（固定持有期）
     * <p>
     * 若策略为均值回归，该方法返回空的List
     * <p>
     * 形成期范围为 1～30
     *
     * @param backTestingInfoVO
     * @return 固定持有期的超额收益率和策略胜率（如果是均值回归进行该测试，则返回空的list）
     */
    @Override
    public List<WinningRateVO> getWinningRate_FixedHolding(BackTestingInfoVO backTestingInfoVO) throws SelectStocksException, FormativePeriodNotExistException, ParameterErrorException, WinnerSelectErrorException {
        List<WinningRateVO> result = new ArrayList<>();

        StrategyType strategyType = backTestingInfoVO.strategyType;
        if (strategyType.equals(StrategyType.MEANREVERSION)) {
            return result;
        }

        // 形成期最多30天
        for (int i = 2; i <= 30; i += 2) {
            strategy = null;

            backTestingInfoVO.formativePeriod = i;
            strategy = new Momentum(backTestingInfoVO);

            BackTestingJudgeVO backTestingJudgeVO = strategy.canBackTest();

            if (backTestingJudgeVO.getCanBackTest()) {
                result.add(encapsulateWinningRate(strategy.getBackTestResult(), i));
            } else {
                throw new SelectStocksException();
            }
        }
        return result;
    }

    /**
     * 将回测结果封装成返回给界面的VO
     *
     * @param backTestResult 回测结果
     * @param parameter      参数：持有期(或形成期)
     * @return 封装结果
     */
    private WinningRateVO encapsulateWinningRate(List<BackTestingSingleChartVO> backTestResult, int parameter) {
        return new WinningRateVO(parameter, getExtraReturnRate(backTestResult), getWinningRate());
    }

    /**
     * 计算超额收益率
     *
     * @param backTestResult 回测结果
     * @return 超额收益率
     */
    private double getExtraReturnRate(List<BackTestingSingleChartVO> backTestResult) {
        BackTestingSingleChartVO last = backTestResult.get(backTestResult.size() - 1);
        // 策略收益率-基准收益率
        return (last.backTestValue - last.stdValue);
    }

    /**
     * 计算策略胜率
     *
     * @return 策略胜率
     */
    private double getWinningRate() {
        List<Double> returnRateDistribution = strategy.getReturnRateDistribution();

        long winningTimes = returnRateDistribution.stream().filter(s -> (s > 0)).count();

        return (double) winningTimes / returnRateDistribution.size();
    }

}
