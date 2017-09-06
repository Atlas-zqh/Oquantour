package oquantour.po.util;

import java.sql.Date;

/**
 * 画图时要用的
 * <p>
 * x轴和y轴对应的值
 * <p>
 * Created by keenan on 07/05/2017.
 */
public class ChartInfo {
    // X轴
    private double xAxis;
    // Date X轴
    private Date dateXAxis;
    // String x轴
    private String strXAxis;
    // Y轴
    private double yAxis;

    public ChartInfo() {
    }

    public ChartInfo(Date dateXAxis, double yAxis) {
        this.dateXAxis = dateXAxis;
        this.yAxis = yAxis;
    }

    public ChartInfo(double xAxis, double yAxis) {
        this.xAxis = xAxis;
        this.yAxis = yAxis;
    }

    public ChartInfo(String strXAxis, double yAxis) {
        this.strXAxis = strXAxis;
        this.yAxis = yAxis;
    }

    public String getStrXAxis() {
        return strXAxis;
    }

    public void setStrXAxis(String strXAxis) {
        this.strXAxis = strXAxis;
    }

    public void setxAxis(double xAxis) {
        this.xAxis = xAxis;
    }

    public void setDateXAxis(Date dateXAxis) {
        this.dateXAxis = dateXAxis;
    }

    public void setyAxis(double yAxis) {
        this.yAxis = yAxis;
    }

    public double getxAxis() {
        return xAxis;
    }

    public Date getDateXAxis() {
        return dateXAxis;
    }

    public double getyAxis() {
        return yAxis;
    }


}
