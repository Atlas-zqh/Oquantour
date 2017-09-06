package bl.strategy;

import exception.FormativePeriodNotExistException;
import exception.ParameterErrorException;
import exception.SelectStocksException;
import exception.WinnerSelectErrorException;
import vo.*;

import java.util.List;

/**
 * 供界面调用的接口类
 * Created by st on 2017/3/27.
 */
public interface BackTestingBLService {

    /**
     * 得到回测结果
     *
     * @return 回测结果
     */
    List<BackTestingSingleChartVO> getBackTestResult() throws ParameterErrorException, WinnerSelectErrorException;

    /**
     * 得到回测统计数据
     *
     * @return 回测统计数据
     */
    StrategyStatisticsVO getBackTestStatistics();

    /**
     * 判断能否回测
     *
     * @return 能否回测的结果
     */
    BackTestingJudgeVO canBackTest() throws SelectStocksException, FormativePeriodNotExistException;

    /**
     * 设置回测信息
     *
     * @param backTestingInfoVO
     */
    void setBackTestingInfo(BackTestingInfoVO backTestingInfoVO);

    /**
     * 获得收益率分布直方图的数据（由界面按需自己处理）
     *
     * @return 收益率分布数据
     */
    List<Double> getReturnRateDistribution();

    /**
     * 获得每个调仓日赢家组合的信息
     *
     * @return 赢家组合的信息
     */
    List<WinnerInfoVO> getWinnersInfo();
}
