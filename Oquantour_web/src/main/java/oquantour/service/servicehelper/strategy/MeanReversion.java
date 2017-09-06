package oquantour.service.servicehelper.strategy;

import oquantour.exception.BackTestErrorException;
import oquantour.exception.UnsupportedOperationException;
import oquantour.exception.WinnerSelectionErrorException;
import oquantour.po.StockPO;
import oquantour.po.util.SortInfo;
import oquantour.util.tools.Calculator;
import oquantour.util.tools.SortUtil;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 均值回归策略
 * <p>
 * Created by keenan on 12/05/2017.
 */
public class MeanReversion extends Strategy {
    // 均线类型
    private int maType;
    // 最大持仓股票数
    private int maxHoldingNum;

    public MeanReversion(Map<Date, List<StockPO>> securities, int holding, Date startDate, int numOfStocks, int maType, int maxHoldingNum) {
        super(securities, maType, holding, startDate, numOfStocks);
        this.maType = maType;
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
        if (maType != 5 && maType != 10 && maType != 20 && maType != 30 && maType != 60 && maType != 120 && maType != 240) {
            throw new BackTestErrorException();
        }

        // 得到这个持有期内数据没有缺失的股票
        List<StockPO> stockPOS;
        try {
            stockPOS = holdingPeriodFilter(currentDate, dateList);
        } catch (UnsupportedOperationException e) {
            throw new BackTestErrorException();
        }

        List<SortInfo> sortInfos = new ArrayList<>();
        for (StockPO stock : stockPOS) {
            double stdDev_pastN = getNStdDev(stock, currentDate, dateList);

            if (((-1) == stdDev_pastN) || (0 == stdDev_pastN)) {
                continue;
            }

            // 赋值均线值
            double maValue = 0;
            switch (maType) {
                case 5:
                    maValue = stock.getMa5();
                    break;
                case 10:
                    maValue = stock.getMa10();
                    break;
                case 20:
                    maValue = stock.getMa20();
                    break;
                case 30:
                    maValue = stock.getMa30();
                    break;
                case 60:
                    maValue = stock.getMa60();
                    break;
                case 120:
                    maValue = stock.getMa120();
                    break;
                case 240:
                    maValue = stock.getMa240();
                    break;
            }

            // 偏离度 = （均线的价格 - 现在的股价）／ 均线的价格
            sortInfos.add(new SortInfo(stock, (maValue - stock.getAdjClose()) / stdDev_pastN));
        }
        // 找到偏离度最高的k只股票(k = 最大持仓股票数)
        // 偏离度数值越小，则说明越有可能回归
        // 我们要选择的是当前股价低于均线最多的几只股票

        SortUtil.sortDescending(sortInfos, sortInfo -> sortInfo.getValue());

        int maxSize = (sortInfos.size() < maxHoldingNum) ? sortInfos.size() : maxHoldingNum;

        List<StockPO> winner = new ArrayList<>();

        for (int i = 0; i < maxSize; i++) {
            winner.add(sortInfos.get(i).getStock());
        }

        return winner;
    }

    /**
     * 得到前n日收盘价的标准差
     *
     * @param stock       当前这只股票
     * @param currentDate 当前日
     * @param dateList    从map中提取出来的日期列表，包括了第一个形成期
     * @return 前n日收盘价的标准差（如果前n日都没有数据，则返回-1)
     */
    private double getNStdDev(StockPO stock, Date currentDate, List<Date> dateList) {
        int index = dateList.indexOf(currentDate);
        // 前N日的所有复权收盘价
        List<Double> adjCloses = new ArrayList<>();

        for (int offset = 1; offset <= maType; offset++) {
            StockPO stockPO = getStockByCodeAndDay(stock.getStockId(), dateList.get(index - offset));

            if (null == stockPO) {
                continue;
            }

            adjCloses.add(stockPO.getAdjClose());
        }

        if (0 == adjCloses.size()) {
            return -1;
        }

        return Calculator.std(adjCloses);
    }
}
