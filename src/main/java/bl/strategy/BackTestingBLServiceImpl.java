package bl.strategy;

import bl.strategy.backtest.MeanReversion;
import bl.strategy.backtest.Momentum;
import bl.strategy.backtest.Strategy;
import exception.FormativePeriodNotExistException;
import exception.ParameterErrorException;
import exception.SelectStocksException;
import exception.WinnerSelectErrorException;
import vo.*;

import java.util.List;

/**
 * Created by st on 2017/3/27.
 * <p>
 * Modified by keenan on 2017/4/4
 */
public class BackTestingBLServiceImpl implements BackTestingBLService {
    private Strategy strategy;

    private BackTestingInfoVO backTestingInfoVO;

    private static BackTestingBLService backTestingBLService;

    private BackTestingBLServiceImpl() {
    }

    public static BackTestingBLService getInstance() {
        if (backTestingBLService == null) {
            backTestingBLService = new BackTestingBLServiceImpl();
        }

        return backTestingBLService;
    }

    /**
     * 根据界面选择的策略类型，返回回测结果
     *
     * @return
     */
    @Override
    public List<BackTestingSingleChartVO> getBackTestResult() throws ParameterErrorException, WinnerSelectErrorException {
        return strategy.getBackTestResult();
    }

    /**
     * 根据界面传入的策略类型，返回回测统计数据
     *
     * @return
     */
    @Override
    public StrategyStatisticsVO getBackTestStatistics() {
        return strategy.getBackTestStatistics();
    }

    /**
     * 判断能否回测
     *
     * @return 能否回测的结果
     */
    @Override
    public BackTestingJudgeVO canBackTest() throws SelectStocksException, FormativePeriodNotExistException {
        return strategy.canBackTest();
    }

    /**
     * 设置回测信息
     *
     * @param backTestingInfoVO
     */
    @Override
    public void setBackTestingInfo(BackTestingInfoVO backTestingInfoVO) {
        this.backTestingInfoVO = backTestingInfoVO;

        switch (backTestingInfoVO.strategyType) {
            case MOMENTUM:
                strategy = new Momentum(backTestingInfoVO);
                break;
            case MEANREVERSION:
                strategy = new MeanReversion(backTestingInfoVO);
                break;
        }
    }

    /**
     * 获得收益率分布直方图的数据（由界面按需自己处理）
     *
     * @return 收益率分布数据
     */
    @Override
    public List<Double> getReturnRateDistribution() {
        return strategy.getReturnRateDistribution();
    }

    /**
     * 获得每个调仓日赢家组合的信息
     *
     * @return 赢家组合的信息
     */
    @Override
    public List<WinnerInfoVO> getWinnersInfo() {
        return strategy.getWinnerInfo();
    }
}
