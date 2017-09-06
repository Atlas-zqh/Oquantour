package oquantour.serviceTest;


import oquantour.exception.BackTestErrorException;
import oquantour.service.BackTestService;
import oquantour.po.util.BackTestResult;
import oquantour.po.util.BackTestingSingleChartInfo;
import oquantour.po.util.StrategyType;
import oquantour.po.util.BackTestInfo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by keenan on 15/05/2017.
 */
public class BackTestServiceTest {
    @Autowired
    private BackTestService backTestService;

    @Test
    public void test() {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        backTestService = (BackTestService) ctx.getBean("backTestService");

        java.util.Date date = new java.util.Date(108, 7, 20);
        Date startDate = new Date(date.getTime());

        java.util.Date date2 = new java.util.Date(109, 11, 26);
        Date endDate = new Date(date2.getTime());

        List<String> stocks = new ArrayList<>();
        for (int i = 2002; i <= 2300; i++) {
            stocks.add(String.valueOf(i));
        }

        int formartive = 5;
        int holding = 3;

        StrategyType strategyType = StrategyType.Momentum;

        BackTestInfo backTestInfo = new BackTestInfo(startDate, endDate, stocks, formartive, holding, 5, strategyType, 10, false, false, false, false);

        try {
            System.out.println("到这里！");
            BackTestResult backTestResult = backTestService.getBackTestResult(backTestInfo);
            List<BackTestingSingleChartInfo> backTestingSingleChartInfos = backTestResult.getBackTestingSingleChartInfos();

            System.out.println(backTestResult.getScore());

            double[] indices = backTestResult.getIndices();
            System.out.println(indices[0] + " " + indices[1] + " " + indices[2] + " " + indices[3]);
            System.out.println("到这里22！");
        } catch (BackTestErrorException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFindBest() {

    }
}
