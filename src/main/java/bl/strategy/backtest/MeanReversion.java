package bl.strategy.backtest;

import bl.stock.StockBL;
import bl.tools.Calculator;
import bl.tools.SortUtil;
import bl.tools.utils.SortInfo;
import bl.tools.utils.StockMAValue;
import exception.WinnerSelectErrorException;
import po.Stock;
import vo.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 均值回归策略
 * <p>
 * Created by keenan on 23/03/2017.
 */
public class MeanReversion extends Strategy {
    // 最大持仓股票数
    private int maxHoldingStocks;

    private StockBL stockBL;

    private List<StockMAValue> maValues;

    /**
     * 子类的构造方法
     */
    public MeanReversion(BackTestingInfoVO backTestingInfoVO) {
        this.backTestingInfoVO = backTestingInfoVO;
        stockBL = new StockBL();
        super.formativePeriod = backTestingInfoVO.ma_length;
        this.maxHoldingStocks = backTestingInfoVO.maxholdingStocks;
    }

    /**
     * 获得均线数据
     *
     * @return 每一个股票的均线数据
     */
    private List<StockMAValue> getMAValues() {
        // 回测开始日期和结束日期
        Date startDate = backTestingInfoVO.startDate;
        Date endDate = backTestingInfoVO.endDate;
        ArrayList<StockMAValue> stockMAValues = new ArrayList<>();

        if (validStockCodes == null || validStockCodes.isEmpty()) {
            return stockMAValues;
        }
        for (List<Stock> stocks : validStockCodes) {

            // 当前处理的股票代码
            String code = stocks.get(0).getCode();

            // 封装均线数据（股票代码、均线类型和均线值一一对应）

            List<DailyAverageVO> dailyAverageVOS = stockBL.getDailyAverage(new SearchVO(code, "", startDate, endDate), formativePeriod);

            // 如果没有均线数据，就下一个
            if (dailyAverageVOS.isEmpty() || dailyAverageVOS == null) {
                continue;
            }
            stockMAValues.add(new StockMAValue(code, dailyAverageVOS));
        }
        return stockMAValues;
    }

    /**
     * 得到前n日收盘价的标准差
     *
     * @param stockCode   当前这只股票
     * @param currentDate 当前日
     * @param dateList    从map中提取出来的日期列表，包括了第一个形成期
     * @param map         股票数据
     * @return 前n日收盘价的标准差（如果前n日都没有数据，则返回-1）
     */
    private double getNStdDev(String stockCode, Date currentDate, List<Date> dateList, Map<Date, List<Stock>> map) {
        int index = dateList.indexOf(currentDate);
        // 前N日的所有复权收盘价
        List<Double> adjCloses = new ArrayList<>();

        for (int offset = 1; offset <= formativePeriod; offset++) {
            double adjClose = getStockAdjClose(stockCode, dateList, index - offset, map);
            // 等于-1说明不存在该股票当日的数据
            if ((-1) == adjClose) {
                continue;
            }
            adjCloses.add(adjClose);
        }

        if (0 == adjCloses.size()) {
            return -1;
        }

        return Calculator.std(adjCloses);
    }

    /**
     * 根据与均值的偏离度选出赢家组合的方法
     *
     * @param currentDate 当前日期
     * @param dateList    从Map中抽取的日期列表
     * @param map         按日期组合的股票信息
     * @return 赢家组合的股票代码列表
     */
    @Override
    protected List<String> selectWinner(Date currentDate, List<Date> dateList, Map<Date, List<Stock>> map) throws WinnerSelectErrorException {
        List<StockMAValue> stockMAValues;

        if (null == maValues) {
            maValues = this.getMAValues();
        }

        stockMAValues = maValues;

        // 当前调仓日的所有股票信息
        List<Stock> currentDateStockInfo = map.get(currentDate);

        List<SortInfo> sortInfos = new ArrayList<>();

        // 将所有股票的数据封装到SortInfo(包含了股票号码和当日该股票的偏离度)
        for (int i = 0, size = currentDateStockInfo.size(); i < size; i++) {
            Stock stock = currentDateStockInfo.get(i);

            double stdDev_pastN = getNStdDev(stock.getCode(), currentDate, dateList, map);

            if (((-1) == stdDev_pastN) || (0 == stdDev_pastN)) {
                continue;
            }

            // 判断得到的是不是当前处理的这只股票的均线数据
            StockMAValue currentStockMAValue = stockMAValues
                    .stream()
                    .filter(stockMAValue -> stockMAValue.getStockCode().equals(stock.getCode()))
                    .findFirst()
                    .orElse(null);

            if (null == currentStockMAValue) {
                continue;
            }

            // 找到当前调仓日的均线数据
            DailyAverageVO currentDailyAverageVO = currentStockMAValue.getMa_values()
                    .stream()
                    .filter(dailyAverageVO -> dailyAverageVO.date.equals(currentDate))
                    .findFirst()
                    .orElse(null);

            if (null == currentDailyAverageVO) {
                continue;
            }

            // 偏离度 = （均线的价格 - 现在的股价）／ 均线的价格
            sortInfos.add(new SortInfo(stock.getCode(), (currentDailyAverageVO.average - stock.getAdjClose()) / stdDev_pastN));
        }

        // 找到偏离度最高的k只股票(k = 最大持仓股票数)
        // 偏离度数值越小，则说明越有可能回归
        // 我们要选择的是当前股价低于均线最多的几只股票
        sortInfos = holdingPeriodFilter(sortInfos, map, dateList, currentDate, backTestingInfoVO.holdingPeriod);

        // 只选择偏离度为正的股票
        sortInfos = sortInfos.stream().filter(sortInfo -> sortInfo.getValue() >= 0).collect(Collectors.toList());

        SortUtil.sortDescending(sortInfos, sortInfo -> sortInfo.getValue());

        List<String> winner = new ArrayList<>();

        int maxSize = (sortInfos.size() < maxHoldingStocks) ? sortInfos.size() : maxHoldingStocks;

        for (int i = 0; i < maxSize; i++) {
            winner.add(sortInfos.get(i).getStockCode());
        }

        //若赢家组合为0，抛出异常
        if (winner.size() < 1) {
            throw new WinnerSelectErrorException();
        }

        return winner;

    }
}