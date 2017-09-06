package oquantour.data.datahelper;

import oquantour.data.dao.PlateDao;
import oquantour.data.dao.StockDao;
import oquantour.data.dao.UserDao;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by island on 2017/6/9.
 */
public class UpdateDataHelper {
    static ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
    static UserDao userDao;
    static StockDao stockDao;
    static PlateDao plateDao;

    public static void main(String[] args){
        timer1();
        timer3();
    }

    public static void timer1(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 00); // 控制时
        calendar.set(Calendar.MINUTE, 00);    // 控制分
        calendar.set(Calendar.SECOND, 0);    // 控制秒
        Date time = calendar.getTime();     // 得出执行任务的时间,此处为今天的00：00：00

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
//                System.out.println(new Date().getTime()+"-------定时任务1--------");
                stockDao = (StockDao)ctx.getBean("stockDao");
                stockDao.updateRealTimeStockInfo();
                System.out.println("update realtimestock success");
            }
        }, time, 1000 * 60 * 2);
    }

    public static void timer2(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 00); // 控制时
        calendar.set(Calendar.MINUTE, 00);    // 控制分
        calendar.set(Calendar.SECOND, 0);    // 控制秒
        Date time = calendar.getTime();     // 得出执行任务的时间,此处为今天的00：00：00

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
//                System.out.println(new Date().getTime()+"-------定时任务1--------");
                stockDao = (StockDao)ctx.getBean("stockDao");
//                stockDao.updateDailyStockInfo();
            }
        }, time, 1000 * 60 * 60 * 24);
    }

    public static void timer3(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 00); // 控制时
        calendar.set(Calendar.MINUTE, 00);    // 控制分
        calendar.set(Calendar.SECOND, 0);    // 控制秒
        Date time = calendar.getTime();     // 得出执行任务的时间,此处为今天的00：00：00

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
//                System.out.println(new Date().getTime()+"-------定时任务1--------");
                plateDao = (PlateDao)ctx.getBean("plateDao");
                plateDao.updateRealTimePlateInfo();
                System.out.println("update realtime plate success");
            }
        }, time, 1000 * 60 );
    }
}
