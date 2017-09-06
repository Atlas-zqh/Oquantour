package oquantour.po.util;

/**
 * Created by keenan on 31/05/2017.
 */
public class BackTestBestInfo {
    // x轴
    private double x_axis;
    // 回测统计数据
    private BackTestStatistics backTestStatistics;
    // 超额收益率
    private double extraReturnRate;
    // 策略胜率
    private double winningRate;

    public BackTestBestInfo(double x_axis, BackTestStatistics backTestStatistics, double extraReturnRate, double winningRate) {
        this.x_axis = x_axis;
        this.backTestStatistics = backTestStatistics;
        this.extraReturnRate = extraReturnRate;
        this.winningRate = winningRate;
    }

    public double getX_axis() {
        return x_axis;
    }

    public void setX_axis(double x_axis) {
        this.x_axis = x_axis;
    }

    public BackTestStatistics getBackTestStatistics() {
        return backTestStatistics;
    }

    public void setBackTestStatistics(BackTestStatistics backTestStatistics) {
        this.backTestStatistics = backTestStatistics;
    }

    public double getExtraReturnRate() {
        return extraReturnRate;
    }

    public void setExtraReturnRate(double extraReturnRate) {
        this.extraReturnRate = extraReturnRate;
    }

    public double getWinningRate() {
        return winningRate;
    }

    public void setWinningRate(double winningRate) {
        this.winningRate = winningRate;
    }
}
