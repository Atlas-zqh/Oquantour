package bl.strategy;

import exception.FormativePeriodNotExistException;
import exception.ParameterErrorException;
import exception.SelectStocksException;
import exception.WinnerSelectErrorException;
import vo.BackTestingInfoVO;
import vo.BackTestingJudgeVO;
import vo.WinningRateVO;

import java.util.List;

/**
 * 超额收益率和策略胜率
 * <p>
 * 参数说明:
 * 与回测所用的数据基本相同
 * 两种策略都需要的数据为:
 * 回测区间; 策略类型; 回测股票;
 * <p>
 * 如果是动量策略，还需要形成期或者持有期（只需调用相应的方法，数据的提取由逻辑层处理）、
 * 如果是均值回归，还需要选择均线（5日、10日、20日）
 * a首先，用户要选择一个回测区间。
 * <p>
 * Created by keenan on 07/04/2017.
 */
public interface WinningRateBLService {
    /**
     * 得到超额收益率和策略胜率（固定形成期）
     *
     * @param backTestingInfoVO
     * @return 超额收益率和策略胜率
     */
    List<WinningRateVO> getWinningRate_FixedFormative(BackTestingInfoVO backTestingInfoVO) throws SelectStocksException, FormativePeriodNotExistException, ParameterErrorException, WinnerSelectErrorException;

    /**
     * 得到超额收益率和策略胜率（固定持有期）
     * <p>
     * 若策略为均值回归，该方法返回空的List
     *
     * @param backTestingInfoVO
     * @return
     */
    List<WinningRateVO> getWinningRate_FixedHolding(BackTestingInfoVO backTestingInfoVO) throws SelectStocksException, FormativePeriodNotExistException, ParameterErrorException, WinnerSelectErrorException;

}
