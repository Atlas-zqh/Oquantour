package oquantour.service.servicehelper.strategy;

import oquantour.exception.BackTestErrorException;
import oquantour.exception.UnsupportedOperationException;
import oquantour.exception.WinnerSelectionErrorException;
import oquantour.po.StockPO;
import oquantour.po.util.SortInfo;
import oquantour.util.tools.PriceIndices;
import oquantour.util.tools.SortUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by keenan on 16/05/2017.
 */
public class DIY_Strategy extends Strategy {
    private Map<PriceIndices, double[]> priceIndicesRange;

    private Map<PriceIndices, Double> priceIndicesWeight;

    private int maxHoldingNum;

    public DIY_Strategy(Map<Date, List<StockPO>> securities, int holding, Date startDate, int numOfStocks, Map<PriceIndices, double[]> priceIndicesRange, Map<PriceIndices, Double> priceIndicesWeight, int maxHoldingNum) {
        super(securities, 0, holding, startDate, numOfStocks);
        this.priceIndicesRange = priceIndicesRange;
        this.priceIndicesWeight = priceIndicesWeight;
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
        // 得到这个持有期内数据没有缺失的股票
        List<StockPO> stockPOS = new ArrayList<>();
        try {
            stockPOS = holdingPeriodFilter(currentDate, dateList);
        } catch (UnsupportedOperationException e) {
            throw new BackTestErrorException();
        }

        if (priceIndicesRange == null || priceIndicesRange.isEmpty()) {
            return weightFilter(stockPOS);
        } else if (priceIndicesWeight == null || priceIndicesWeight.isEmpty()) {
            return rangeFilter(stockPOS);
        }
        return null;
    }

    /**
     * 按照区间方式进行调仓
     *
     * @return 赢家组合
     */
    private List<StockPO> rangeFilter(List<StockPO> stockPOS) {
        List<SortInfo> sortInfos = new ArrayList<>();
        for (StockPO stockPO : stockPOS) {
            int inCnt = 0;
            for (Map.Entry<PriceIndices, double[]> entry : priceIndicesRange.entrySet()) {
                double down = entry.getValue()[0];
                double up = entry.getValue()[1];
                double value = getValue(stockPO, entry.getKey());
                if (value >= down && value <= up) {
                    inCnt++;
                }
            }
            sortInfos.add(new SortInfo(stockPO, inCnt));
        }
        SortUtil.sortDescending(sortInfos, sortInfo -> sortInfo.getValue());
        int maxSize = (sortInfos.size() < maxHoldingNum) ? sortInfos.size() : maxHoldingNum;
        List<StockPO> winner = new ArrayList<>();
        for (int i = 0; i < maxSize; i++) {
            winner.add(sortInfos.get(i).getStock());
        }
        return winner;
    }

    /**
     * 按照权重方式进行调仓
     *
     * @return 赢家组合
     */
    private List<StockPO> weightFilter(List<StockPO> stockPOS) {
        List<SortInfo> sortInfos = new ArrayList<>();
        for (StockPO stockPO : stockPOS) {
            Double score = 0.0;
            for (Map.Entry<PriceIndices, Double> entry : priceIndicesWeight.entrySet()) {
                score += getValue(stockPO, entry.getKey()) * entry.getValue();
            }
            sortInfos.add(new SortInfo(stockPO, score));
        }
        SortUtil.sortDescending(sortInfos, sortInfo -> sortInfo.getValue());
        int maxSize = (sortInfos.size() < maxHoldingNum) ? sortInfos.size() : maxHoldingNum;
        List<StockPO> winner = new ArrayList<>();
        for (int i = 0; i < maxSize; i++) {
            winner.add(sortInfos.get(i).getStock());
        }
        return winner;
    }

    /**
     * 获得对应指标的值
     *
     * @param stockPO    股票
     * @param priceIndex 技术指标枚举类型
     * @return 对应指标值
     */
    private Double getValue(StockPO stockPO, PriceIndices priceIndex) {
        try {
            Class clazz = stockPO.getClass();
            String methodName = "get" + priceIndex.toString();
            Method method = clazz.getMethod(methodName);
            Double value = (Double) method.invoke(stockPO);
            return value;
        } catch (NoSuchMethodException e1) {
            e1.printStackTrace();
            return 0.0;
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
            return 0.0;
        } catch (InvocationTargetException e3) {
            return 0.0;
        }
    }
}
