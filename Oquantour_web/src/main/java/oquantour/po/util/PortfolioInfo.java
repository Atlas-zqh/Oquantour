package oquantour.po.util;

import oquantour.po.StockCombination;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * 投资组合信息
 * 包括收益率等分析
 * <p>
 * Created by keenan on 03/06/2017.
 */
public class PortfolioInfo {
    /**
     * 投资组合名称
     */
    private String portfolioName;
    /**
     * 创建时间
     */
    private Timestamp createTime;
    /**
     * 最新净值
     */
    private Double newNet;
    /**
     * 总收益
     */
    private Double totalReturnRate;
    /**
     * 年化波动率
     */
    private Double annualizedVolatility;
    /**
     * 投资组合调仓详情
     */
    private List<StockCombination> portfolio;

    /**
     * 自创建组合之日起的收益率  x: getDateXAxis()
     */
    private List<ChartInfo> returnRates;
    /**
     * 最赚钱股票排行 x: getStrXAxis()
     */
    private List<ChartInfo> maxProfitStocks;
    /**
     * 最输钱股票排行 x: getStrXAxis()
     */
    private List<ChartInfo> minProfitStocks;
    /**
     * 自股票组合创建以来，所有持仓过的股票的平均仓位
     */
    private Map<String, Double> avgPosition;
    /**
     * 自股票组合创建以来，所有持仓过的股票的交易次数
     */
    private Map<String, Double> tradeCnt;
    /**
     * 行业分布  x: getStrXAxis()
     */
    private List<ChartInfo> industryDistribution;
    /**
     * 仓位变化信息
     */
    Map<Timestamp, List<Double>> positionInfo;

    public String getPortfolioName() {
        return portfolioName;
    }

    public void setPortfolioName(String portfolioName) {
        this.portfolioName = portfolioName;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Double getNewNet() {
        return newNet;
    }

    public void setNewNet(Double newNet) {
        this.newNet = newNet;
    }

    public Double getTotalReturnRate() {
        return totalReturnRate;
    }

    public void setTotalReturnRate(Double totalReturnRate) {
        this.totalReturnRate = totalReturnRate;
    }

    public List<StockCombination> getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(List<StockCombination> portfolio) {
        this.portfolio = portfolio;
    }

    public List<ChartInfo> getReturnRates() {
        return returnRates;
    }

    public void setReturnRates(List<ChartInfo> returnRates) {
        this.returnRates = returnRates;
    }

    public List<ChartInfo> getMaxProfitStocks() {
        return maxProfitStocks;
    }

    public void setMaxProfitStocks(List<ChartInfo> maxProfitStocks) {
        this.maxProfitStocks = maxProfitStocks;
    }

    public List<ChartInfo> getMinProfitStocks() {
        return minProfitStocks;
    }

    public void setMinProfitStocks(List<ChartInfo> minProfitStocks) {
        this.minProfitStocks = minProfitStocks;
    }

    public Map<String, Double> getAvgPosition() {
        return avgPosition;
    }

    public void setAvgPosition(Map<String, Double> avgPosition) {
        this.avgPosition = avgPosition;
    }

    public Map<String, Double> getTradeCnt() {
        return tradeCnt;
    }

    public void setTradeCnt(Map<String, Double> tradeCnt) {
        this.tradeCnt = tradeCnt;
    }

    public List<ChartInfo> getIndustryDistribution() {
        return industryDistribution;
    }

    public void setIndustryDistribution(List<ChartInfo> industryDistribution) {
        this.industryDistribution = industryDistribution;
    }

    public Double getAnnualizedVolatility() {
        return annualizedVolatility;
    }

    public void setAnnualizedVolatility(Double annualizedVolatility) {
        this.annualizedVolatility = annualizedVolatility;
    }

    public Map<Timestamp, List<Double>> getPositionInfo() {
        return positionInfo;
    }

    public void setPositionInfo(Map<Timestamp, List<Double>> positionInfo) {
        this.positionInfo = positionInfo;
    }

}
