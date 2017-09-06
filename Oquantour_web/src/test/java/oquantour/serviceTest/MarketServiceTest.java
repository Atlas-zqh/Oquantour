package oquantour.serviceTest;

import oquantour.BaseTest;
import oquantour.service.MarketService;
import oquantour.po.util.MarketTemperature;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Date;

/**
 * Created by keenan on 23/05/2017.
 */
public class MarketServiceTest extends BaseTest {
    @Autowired
    private MarketService marketService;

//    @Test
//    public void testGetMarketTemperature() {
//        String str = "2010-08-05";
//
////        MarketTemperature marketTemperature = marketService.getMarketTemperature(Date.valueOf(str));
//
//        System.out.println(marketTemperature.getDown5perStock());
//        System.out.println(marketTemperature.getLimitDownStock());
//        System.out.println(marketTemperature.getOpenDown5perStock());
//
//
//    }
}
