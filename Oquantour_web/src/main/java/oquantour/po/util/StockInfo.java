package oquantour.po.util;

import oquantour.po.StockInfoPO;
import oquantour.po.StockPO;
import oquantour.util.tools.IndicesAnalysis;

import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by keenan on 16/05/2017.
 */
public class StockInfo {
    /**
     * 全部的股票信息
     */
    private List<StockPO> stockInfo;
    /**
     * 周k数据
     */
    private List<StockPO> weeklyInfo;
    /**
     * 月k数据
     */
    private List<StockPO> monthlyInfo;
    /**
     * 投资建议
     */
    private String advice;
    /**
     * 预测开盘价
     */
    private double estimatedOpen;
    /**
     * 预测收盘价
     */
    private double estimatedClose;
    /**
     * 预测最低价
     */
    private double estimatedLow;
    /**
     * 预测最高价
     */
    private double estimatedHigh;
    /**
     * 预测复权收盘价
     */
    private double estimatedAdjClose;
    /**
     * 基本面数据
     */
    private StockInfoPO basicInfo;
    /**
     * 指标分析（仅买入点）   key=IndicesAnalysis.*** , value={(key:日期 value:买入信号)}
     */
    private Map<IndicesAnalysis, Map<java.sql.Date, String>> buyPoints;
    /**
     * 指标分析（仅卖出点）   key=IndicesAnalysis.*** , value={(key:日期 value:卖出信号)}
     */
    private Map<IndicesAnalysis, Map<java.sql.Date, String>> sellPoints;

    public StockInfo(List<StockPO> stockInfo, List<StockPO> weeklyInfo, List<StockPO> monthlyInfo, String advice, double estimatedOpen, double estimatedClose, double estimatedLow, double estimatedHigh, double estimatedAdjClose, StockInfoPO basicInfo, Map<IndicesAnalysis, Map<java.sql.Date, String>> buyPoints, Map<IndicesAnalysis, Map<java.sql.Date, String>> sellPoints) {
        this.stockInfo = stockInfo;
        this.weeklyInfo = weeklyInfo;
        this.monthlyInfo = monthlyInfo;
        this.advice = advice;
        this.estimatedOpen = estimatedOpen;
        this.estimatedClose = estimatedClose;
        this.estimatedLow = estimatedLow;
        this.estimatedHigh = estimatedHigh;
        this.estimatedAdjClose = estimatedAdjClose;
        this.basicInfo = basicInfo;
        this.buyPoints = buyPoints;
        this.sellPoints = sellPoints;
    }

    public List<StockPO> getStockInfo() {
        return stockInfo;
    }

    public void setStockInfo(List<StockPO> stockInfo) {
        this.stockInfo = stockInfo;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }

    public double getEstimatedOpen() {
        return estimatedOpen;
    }

    public void setEstimatedOpen(double estimatedOpen) {
        this.estimatedOpen = estimatedOpen;
    }

    public double getEstimatedClose() {
        return estimatedClose;
    }

    public void setEstimatedClose(double estimatedClose) {
        this.estimatedClose = estimatedClose;
    }

    public double getEstimatedLow() {
        return estimatedLow;
    }

    public void setEstimatedLow(double estimatedLow) {
        this.estimatedLow = estimatedLow;
    }

    public double getEstimatedHigh() {
        return estimatedHigh;
    }

    public void setEstimatedHigh(double estimatedHigh) {
        this.estimatedHigh = estimatedHigh;
    }

    public double getEstimatedAdjClose() {
        return estimatedAdjClose;
    }

    public void setEstimatedAdjClose(double estimatedAdjClose) {
        this.estimatedAdjClose = estimatedAdjClose;
    }

    public List<StockPO> getWeeklyInfo() {
        return weeklyInfo;
    }

    public void setWeeklyInfo(List<StockPO> weeklyInfo) {
        this.weeklyInfo = weeklyInfo;
    }

    public List<StockPO> getMonthlyInfo() {
        return monthlyInfo;
    }

    public void setMonthlyInfo(List<StockPO> monthlyInfo) {
        this.monthlyInfo = monthlyInfo;
    }

    public StockInfoPO getBasicInfo() {
        return basicInfo;
    }

    public void setBasicInfo(StockInfoPO basicInfo) {
        this.basicInfo = basicInfo;
    }

    public Map<IndicesAnalysis, Map<Date, String>> getBuyPoints() {
        return buyPoints;
    }

    public void setBuyPoints(Map<IndicesAnalysis, Map<Date, String>> buyPoints) {
        this.buyPoints = buyPoints;
    }

    public Map<IndicesAnalysis, Map<Date, String>> getSellPoints() {
        return sellPoints;
    }

    public void setSellPoints(Map<IndicesAnalysis, Map<Date, String>> sellPoints) {
        this.sellPoints = sellPoints;
    }
}
