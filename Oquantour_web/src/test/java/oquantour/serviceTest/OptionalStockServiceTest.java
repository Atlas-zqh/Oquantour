package oquantour.serviceTest;

import oquantour.BaseTest;
import oquantour.service.OptionalStockService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by keenan on 25/05/2017.
 */
public class OptionalStockServiceTest extends BaseTest {
    @Autowired
    private OptionalStockService optionalStockService;

    @Test
    public void testAddOptionalStock() {
        String username = "ljr";
        String stockID = "2";

        optionalStockService.addOptionalStock(username, stockID);
    }

    @Test
    public void test() {
        String username = "ljr";
        String stockID = "3";

        String stockID2 = "4";
        String stockID3 = "5";
        String stockID4 = "6";

        System.out.println(optionalStockService.getAllOptionalStock(username));
//        System.out.println(optionalStockService.isInOptionalStock(username, "2"));
//        optionalStockService.addOptionalStock(username, stockID);
//        optionalStockService.addOptionalStock(username, stockID2);
//        optionalStockService.addOptionalStock(username, stockID3);
//        System.out.println(optionalStockService.getAllOptionalStock(username).size());
//
//        optionalStockService.addOptionalStock(username, stockID4);
//
//        optionalStockService.deleteOptionalStock(username, stockID);
//        System.out.println(optionalStockService.isInOptionalStock(username, stockID));
//        optionalStockService.deleteOptionalStock(username, stockID2);
//        optionalStockService.deleteOptionalStock(username, stockID3);
//        System.out.println(optionalStockService.getAllOptionalStock(username));
    }
}
