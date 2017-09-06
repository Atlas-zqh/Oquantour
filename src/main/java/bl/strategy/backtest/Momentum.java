package bl.strategy.backtest;

import bl.tools.SortUtil;
import bl.tools.utils.SortInfo;
import exception.WinnerSelectErrorException;
import po.Stock;
import vo.BackTestingInfoVO;

import java.util.*;

/**
 * 动量策略
 * <p>
 * Created by keenan on 23/03/2017.
 */
public class Momentum extends Strategy {

    public Momentum(BackTestingInfoVO backTestingInfoVO) {
        this.backTestingInfoVO = backTestingInfoVO;
        super.formativePeriod = backTestingInfoVO.formativePeriod;
    }

    /**
     * 根据收益率选出赢家组合的方法
     *
     * @param currentDate 当前日期
     * @param dateList    从Map中抽取的日期列表
     * @param map         按日期组合的股票信息
     * @return 赢家组合的股票代码列表
     */
    @Override
    protected List<String> selectWinner(Date currentDate, List<Date> dateList, Map<Date, List<Stock>> map) throws WinnerSelectErrorException {

        int index = dateList.indexOf(currentDate);

        List<SortInfo> sortInfos = new ArrayList<>();
        // 获得形成期的第一天和最后一天的所有股票的复权收盘价 计算收益率
        for (Stock stock : map.get(dateList.get(index - formativePeriod))) {

            double close1 = stock.getAdjClose();
            double close2 = getStockAdjClose(stock.getCode(), dateList, index - 1, map);
            if (close2 == -1) {
                continue;
            }
            double rate = (close2 - close1) / close1;
            sortInfos.add(new SortInfo(stock.getCode(), rate));
        }

        sortInfos = holdingPeriodFilter(sortInfos, map, dateList, currentDate, backTestingInfoVO.holdingPeriod);

        //根据收益率从高到低排序，取前20%作为赢家组合返回
        SortUtil.sortDescending(sortInfos, sortInfo -> sortInfo.getValue());

        List<String> winner = new ArrayList<>();

        for (int i = 0; i < sortInfos.size() / 5; i++) {
            winner.add(sortInfos.get(i).getStockCode());
        }

        //若赢家组合少于2只，抛出异常
        if (winner.size() < 2) {
            throw new WinnerSelectErrorException();
        }

        return winner;
    }


}
