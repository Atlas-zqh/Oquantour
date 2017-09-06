package vo;

import java.util.Date;

/**
 * 用于绘制策略和基准的累计收益率比较图
 * <p>
 * Created by keenan on 23/03/2017.
 */
public class BackTestingSingleChartVO {
    // 日期
    public Date date;
    // 策略累积收益率
    public double backTestValue;
    // 基准累积收益率
    public double stdValue;

    public BackTestingSingleChartVO(Date date, double backTestValue, double stdValue) {
        this.date = date;
        this.backTestValue = backTestValue;
        this.stdValue = stdValue;
    }


}
