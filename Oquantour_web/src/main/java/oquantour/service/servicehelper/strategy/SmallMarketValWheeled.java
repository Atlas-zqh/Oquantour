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
 * 小市值轮动策略
 * <p>
 * 每隔若干个交易日，等金额持有市值排名最小的前几只股票
 * <p>
 * Created by keenan on 13/05/2017.
 */
public class SmallMarketValWheeled extends Strategy {
    private int maxHoldingNum;

    public SmallMarketValWheeled(Map<Date, List<StockPO>> securities, int formative, int holding, Date startDate, int numOfStocks, int maxHoldingNum) {
        super(securities, formative, holding, startDate, numOfStocks);
        this.maxHoldingNum = maxHoldingNum;
    }

    public void setMaxHoldingNum(int maxHoldingNum) {
        this.maxHoldingNum = maxHoldingNum;
    }

    /**
     * 选择赢家组合
     *
     * @param currentDate 当前调仓日的日期
     * @param dateList    日期列表（包括形成期）
     * @return 调仓日赢家组合
     */
    @Override
    protected List<StockPO> selectWinner(Date currentDate, List<Date> dateList) throws BackTestErrorException, WinnerSelectionErrorException {
        List<StockPO> stockPOS = new ArrayList<>();
        try {
            stockPOS = holdingPeriodFilter(currentDate, dateList);
        } catch (UnsupportedOperationException e) {
            throw new BackTestErrorException();
        }
        List<SortInfo> sortInfos = new ArrayList<>();
        for (StockPO stock : stockPOS) {
            sortInfos.add(new SortInfo(stock, stock.getAdjClose() * stock.getVolume()));
        }

        // 等金额持有市值排名最小的前几只股票
        SortUtil.sort(sortInfos, sortInfo -> sortInfo.getValue());

        int maxSize = (sortInfos.size() < maxHoldingNum) ? sortInfos.size() : maxHoldingNum;

        List<StockPO> winner = new ArrayList<>();

        for (int i = 0; i < maxSize; i++) {
            winner.add(sortInfos.get(i).getStock());
        }

        return winner;
    }
}
