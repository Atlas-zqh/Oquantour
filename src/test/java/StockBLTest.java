import bl.stock.StockBL;
import org.junit.Before;
import org.junit.Test;
import vo.DailyAverageVO;
import vo.KLineVO;
import vo.SearchVO;
import vo.StockCompareVO;

import java.util.Date;
import java.util.List;

/**
 * stockBL的测试类
 * Created by Pxr on 2017/3/9.
 */
public class StockBLTest {
    private StockBL stockBL;

    @Before
    public void setUpBeforeClass() throws Exception {
        stockBL = new StockBL();
    }

    @Test
    public void testGetKlineInfo() {
        List<KLineVO> kLineVOList = null;
        try {
            kLineVOList = stockBL.getKLineInfo(new SearchVO("155", "",
                    new Date(2014 - 1900, 4 - 1, 28), new Date(2014 - 1900, 4 - 1, 29)));
        } catch (Exception e) {
            System.out.println("in StockBLTest, line 31: no such stock!!!");
        }
        org.junit.Assert.assertEquals(2, kLineVOList.size());
    }

    @Test
    public void testGetDailyAverageInfo() {
        List<List<DailyAverageVO>> list = stockBL.getDailyAverageInfo(new SearchVO("155", "",
                new Date(2014 - 1900, 4 - 1, 28), new Date(2014 - 1900, 4 - 1, 29)));
        org.junit.Assert.assertEquals(6, list.size());
        List<List<DailyAverageVO>> list2 = stockBL.getDailyAverageInfo(new SearchVO("155", "",
                new Date(2005 - 1900, 2 - 1, 1), new Date(2006 - 1900, 12 - 1, 29)));
        org.junit.Assert.assertEquals(6,list2.size());
    }

    @Test
    public void testCompareStock() {
        List<StockCompareVO> list = null;
        try {
            list = stockBL.compareStock(new SearchVO("155","1",
                    new Date(2014 - 1900, 4 - 1, 28), new Date(2014 - 1900, 4 - 1, 29)));
        } catch (Exception e) {
            System.out.println("in StockBLTest, line 53: no such stock!!");
        }
        org.junit.Assert.assertEquals(list.size(),2);
    }

}
