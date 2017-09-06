package oquantour.po.util;

import java.sql.Date;

/**
 * Created by keenan on 07/05/2017.
 */
public class BackTestingSingleChartInfo {
    // 日期
    private Date date;
    // 策略累积收益率
    private double backTestValue;
    // 基准累积收益率
    private double stdValue;

    public BackTestingSingleChartInfo() {
    }

    public BackTestingSingleChartInfo(Date date, double backTestValue, double stdValue) {
        this.date = date;
        this.backTestValue = backTestValue;
        this.stdValue = stdValue;
    }

    public BackTestingSingleChartInfo(ChartInfo backTest, ChartInfo benchmark) {
        this.date = backTest.getDateXAxis();
        this.backTestValue = backTest.getyAxis();
        this.stdValue = benchmark.getyAxis();
    }

    public Date getDate() {
        return date;
    }

    public double getBackTestValue() {
        return backTestValue;
    }

    public double getStdValue() {
        return stdValue;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setBackTestValue(double backTestValue) {
        this.backTestValue = backTestValue;
    }

    public void setStdValue(double stdValue) {
        this.stdValue = stdValue;
    }
}
