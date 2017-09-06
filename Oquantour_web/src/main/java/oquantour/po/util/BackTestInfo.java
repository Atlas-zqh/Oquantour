package oquantour.po.util;

import oquantour.util.tools.PriceIndices;

import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 * 回测板块时，选择忽略100只约束，并将板块内所有股票号转为股票代码List传入
 * <p>
 * Created by keenan on 07/05/2017.
 */
public class BackTestInfo {

    // 回测开始日期
    private Date startDate;
    // 回测结束日期
    private Date endDate;
    // 要回测的多只股票代码
    private List<String> stocks;
    // 形成期
    private int formativePeriod;
    // 持有期
    private int holdingPeriod;
    // 几日均线
    private int ma_length;
    // 策略类型
    private StrategyType strategyType;
    // 最大持仓股票数目
    private int maxholdingStocks;
    // 过滤ST股票
    private boolean filter_ST;
    // 过滤无数据股票
    private boolean filter_NoData;
    // 过滤停牌股票
    private boolean filter_Suspension;
    // 是否忽略100只约束
    private boolean ignore_100;

    private Map<PriceIndices, double[]> priceIndicesRange;

    private Map<PriceIndices, Double> priceIndicesWeight;

    public BackTestInfo() {
    }

    public BackTestInfo(Date startDate, Date endDate, List<String> stocks, int formativePeriod, int holdingPeriod, int ma_length, StrategyType strategyType, int maxholdingStocks, boolean filter_ST, boolean filter_NoData, boolean filter_Suspension, boolean ignore_100) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.stocks = stocks;
        this.formativePeriod = formativePeriod;
        this.holdingPeriod = holdingPeriod;
        this.ma_length = ma_length;
        this.strategyType = strategyType;
        this.maxholdingStocks = maxholdingStocks;
        this.filter_ST = filter_ST;
        this.filter_NoData = filter_NoData;
        this.filter_Suspension = filter_Suspension;
        this.ignore_100 = ignore_100;
    }

    public BackTestInfo(Date startDate, Date endDate, List<String> stocks, int holdingPeriod, StrategyType strategyType, int maxholdingStocks, boolean filter_ST, boolean filter_NoData, boolean filter_Suspension, boolean ignore_100, Map<PriceIndices, double[]> priceIndicesRange, Map<PriceIndices, Double> priceIndicesWeight) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.stocks = stocks;
        this.holdingPeriod = holdingPeriod;
        this.strategyType = strategyType;
        this.maxholdingStocks = maxholdingStocks;
        this.filter_ST = filter_ST;
        this.filter_NoData = filter_NoData;
        this.filter_Suspension = filter_Suspension;
        this.ignore_100 = ignore_100;
        this.priceIndicesRange = priceIndicesRange;
        this.priceIndicesWeight = priceIndicesWeight;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setStocks(List<String> stocks) {
        this.stocks = stocks;
    }

    public void setFormativePeriod(int formativePeriod) {
        this.formativePeriod = formativePeriod;
    }

    public void setHoldingPeriod(int holdingPeriod) {
        this.holdingPeriod = holdingPeriod;
    }

    public void setMa_length(int ma_length) {
        this.ma_length = ma_length;
    }

    public void setStrategyType(StrategyType strategyType) {
        this.strategyType = strategyType;
    }

    public void setMaxholdingStocks(int maxholdingStocks) {
        this.maxholdingStocks = maxholdingStocks;
    }

    public void setFilter_ST(boolean filter_ST) {
        this.filter_ST = filter_ST;
    }

    public void setFilter_NoData(boolean filter_NoData) {
        this.filter_NoData = filter_NoData;
    }

    public void setFilter_Suspension(boolean filter_Suspension) {
        this.filter_Suspension = filter_Suspension;
    }

    public boolean isIgnore_100() {
        return ignore_100;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public List<String> getStocks() {
        return stocks;
    }

    public int getFormativePeriod() {
        return formativePeriod;
    }

    public int getHoldingPeriod() {
        return holdingPeriod;
    }

    public int getMa_length() {
        return ma_length;
    }

    public StrategyType getStrategyType() {
        return strategyType;
    }

    public int getMaxholdingStocks() {
        return maxholdingStocks;
    }

    public boolean isFilter_ST() {
        return filter_ST;
    }

    public boolean isFilter_NoData() {
        return filter_NoData;
    }

    public boolean isFilter_Suspension() {
        return filter_Suspension;
    }

    public void setIgnore_100(boolean ignore_100) {
        this.ignore_100 = ignore_100;
    }

    public Map<PriceIndices, double[]> getPriceIndicesRange() {
        return priceIndicesRange;
    }

    public void setPriceIndicesRange(Map<PriceIndices, double[]> priceIndicesRange) {
        this.priceIndicesRange = priceIndicesRange;
    }

    public Map<PriceIndices, Double> getPriceIndicesWeight() {
        return priceIndicesWeight;
    }

    public void setPriceIndicesWeight(Map<PriceIndices, Double> priceIndicesWeight) {
        this.priceIndicesWeight = priceIndicesWeight;
    }
}
