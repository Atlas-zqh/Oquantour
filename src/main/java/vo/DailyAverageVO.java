package vo;

import java.util.Date;

/**
 * 计算日均线
 * Created by Pxr on 2017/3/3.
 */
public class DailyAverageVO {
    //均值
    public double average;
    //日期
    public Date date;

    public DailyAverageVO(double average, Date date) {
        this.average = average;
        this.date = date;
    }
}
