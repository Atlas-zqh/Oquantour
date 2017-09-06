package oquantour.serviceTest;

import oquantour.BaseTest;
import oquantour.po.StockCombination;
import oquantour.service.PortfolioService;
import oquantour.po.util.ChartInfo;
import oquantour.po.util.PortfolioInfo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by keenan on 05/06/2017.
 */
public class PortfolioTest extends BaseTest {
    @Autowired
    private PortfolioService portfolioService;

//    @Test
//    public void testCalReturnRate() {
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        try {
//            List<String> stocks1 = new ArrayList<>();
//            List<Double> positions1 = new ArrayList<>();
//            java.util.Date date11 = df.parse("2017-5-22 16:10:38.00");
//            String time1 = df.format(date11);
//            Timestamp ts1 = Timestamp.valueOf(time1);
//            stocks1.add("000001");
//            stocks1.add("000002");
//            stocks1.add("000010");
//            stocks1.add("000005");
//            positions1.add(0.20);
//            positions1.add(0.20);
//            positions1.add(0.30);
//            positions1.add(0.15);
//            StockCombination stockCombination1 = new StockCombination(stocks1, positions1, ts1);
//
//            List<String> stocks2 = new ArrayList<>();
//            List<Double> positions2 = new ArrayList<>();
//            java.util.Date date2 = df.parse("2017-5-27 16:10:38.00");
//            String time2 = df.format(date2);
//            Timestamp ts2 = Timestamp.valueOf(time2);
//            stocks2.add("000006");
//            stocks2.add("000002");
//            stocks2.add("000007");
//            stocks2.add("000005");
//            positions2.add(0.20);
//            positions2.add(0.20);
//            positions2.add(0.30);
//            positions2.add(0.15);
//            StockCombination stockCombination2 = new StockCombination(stocks2, positions2, ts2);
//
//            List<String> stocks3 = new ArrayList<>();
//            List<Double> positions3 = new ArrayList<>();
//            java.util.Date date3 = df.parse("2017-6-2 16:10:38.00");
//            String time = df.format(date3);
//            Timestamp ts3 = Timestamp.valueOf(time);
//            stocks3.add("000006");
//            stocks3.add("000008");
//            stocks3.add("000010");
//            stocks3.add("000005");
//            positions3.add(0.24);
//            positions3.add(0.20);
//            positions3.add(0.16);
//            positions3.add(0.15);
//            StockCombination stockCombination3 = new StockCombination(stocks3, positions3, ts3);
//
//            List<StockCombination> stockCombinations = new ArrayList<>();
//            stockCombinations.add(stockCombination1);
//            stockCombinations.add(stockCombination2);
//            stockCombinations.add(stockCombination3);
//
//            Map<String, List<ChartInfo>> chartInfos = portfolioService.getReturnRate(stockCombinations);
//
//            System.out.println(chartInfos.isEmpty());
//            for (ChartInfo chartInfo : chartInfos.get("收益率")) {
//                System.out.println(chartInfo.getDateXAxis() + " :: " + chartInfo.getyAxis());
//            }
//
//            for (ChartInfo chartInfo : chartInfos.get("个股收益率贡献")) {
//                System.out.println(chartInfo.getStrXAxis() + " :: " + chartInfo.getyAxis());
//            }
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//    }

    @Test
    public void testPortfolioInfo() {
        String username = "qky";
        String portfolioName = "2017.06.08伪造测试用组合";

        PortfolioInfo portfolioInfo = portfolioService.getPortfolio(username, portfolioName);

        List<ChartInfo> chartInfos = portfolioInfo.getIndustryDistribution();

        for (ChartInfo chartInfo : chartInfos) {
            System.out.println(chartInfo.getStrXAxis() + " " + chartInfo.getyAxis());
        }
    }

//    @Test
//    public void testModify(){
//        String username="qky";
//        String portfolioName="2017.06.08伪造测试用组合";
//
//        //000005;000004;000001;000002;
//        List<String> stocks=new ArrayList<>();
//        stocks.add("000005");
//        stocks.add("000001");
//        stocks.add("000016");
//        stocks.add("000017");
//        List<Double> positions=new ArrayList<>();
//        positions.add(0.18);
//        positions.add(0.39);
//        positions.add(0.15);
//        positions.add(0.15);
//        try{
//            portfolioService.modifyPortfolio(username,portfolioName,stocks,positions);
//        }catch (WrongCombinationException e){
//            e.printStackTrace();
//        }
//
//    }
}
