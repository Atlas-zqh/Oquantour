package oquantour.service.servicehelper.strategy;

import oquantour.exception.BackTestErrorException;
import oquantour.exception.UnsupportedOperationException;
import oquantour.exception.WinnerSelectionErrorException;
import oquantour.po.StockPO;
import oquantour.po.util.SortInfo;
import oquantour.util.tools.SortUtil;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 动量策略
 * <p>
 * Created by keenan on 12/05/2017.
 */
public class Momentum extends Strategy {
    public Momentum(Map<Date, List<StockPO>> securities, int formative, int holding, Date startDate, int numOfStocks) {
        super(securities, formative, holding, startDate, numOfStocks);
    }

    /**
     * 选择赢家组合
     *
     * @param currentDate 当前调仓日的日期
     * @param dateList    日期列表（包括形成期）
     * @return 调仓日赢家组合
     */
    @Override
    protected List<StockPO> selectWinner(Date currentDate, List<Date> dateList) throws WinnerSelectionErrorException, BackTestErrorException {
        int index = dateList.indexOf(currentDate);

        System.out.println(index + "  index");

        // 得到这个持有期内数据没有缺失的股票
        List<StockPO> stockPOS = new ArrayList<>();
        try {
            stockPOS = holdingPeriodFilter(currentDate, dateList);
        } catch (UnsupportedOperationException e) {
            throw new BackTestErrorException();
        }

        System.out.println("持有期内数据没有缺失的股票有几只：" + stockPOS.size());

        List<SortInfo> sortInfos = new ArrayList<>();

        // 获得形成期的第一天和最后一天的所有股票的复权收盘价 计算收益率
        for (StockPO stockPO : stockPOS) {
            StockPO firstDayInFormative = getStockByCodeAndDay(stockPO.getStockId(), dateList.get(index - formative));
            StockPO lastDayInFormative = getStockByCodeAndDay(stockPO.getStockId(), dateList.get(index - 1));

            if (firstDayInFormative == null || lastDayInFormative == null) {
                continue;
            } else {
                double rate = lastDayInFormative.getAdjClose() / firstDayInFormative.getAdjClose() - 1;
                sortInfos.add(new SortInfo(stockPO, rate));
            }
        }

        //根据收益率从高到低排序，取前20%作为赢家组合返回
        SortUtil.sortDescending(sortInfos, sortInfo -> sortInfo.getValue());

        List<StockPO> winner = new ArrayList<>();

        for (int i = 0; i < sortInfos.size() / 5; i++) {
            winner.add(sortInfos.get(i).getStock());
        }

        return winner;
    }
}
