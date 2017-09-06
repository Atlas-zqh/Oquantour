package oquantour.util.tools;

import java.sql.Date;
import java.util.Calendar;

/**
 * Created by keenan on 11/06/2017.
 */
public class CalendarUtil {
    /**
     * 获得今天的日期
     *
     * @return 今日日期
     */
    public static Date getToday() {
        Date today = new Date(System.currentTimeMillis());
        return today;
    }

    /**
     * 获得某个日期一年前的日期
     *
     * @param date 日期
     * @return 一年前日期
     */
    public static Date getDateOneYearBefore(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);
        calendar.set(year - 1, month, day);
        long one = calendar.getTimeInMillis();
        Date oneYear = new java.sql.Date(one);
        return oneYear;
    }
}
