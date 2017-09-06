import bl.strategy.BackTestingBLService;
import bl.strategy.BackTestingBLServiceImpl;
import bl.tools.StrategyType;
import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Before;
import vo.BackTestingInfoVO;
import vo.BackTestingJudgeVO;
import vo.BackTestingSingleChartVO;
import vo.StrategyStatisticsVO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * BackTestingBLServiceImpl Tester.
 *
 * @author <keenan>
 * @version 1.0
 * @since <pre>04/17/2017</pre>
 */
public class BackTestingBLServiceImplTest {
    private BackTestingBLService backTestingBLService;

    @Before
    public void setUpBeforeClass() throws Exception {
        backTestingBLService = BackTestingBLServiceImpl.getInstance();
    }

    @org.junit.Test
    public void testGetBackTestResult() throws Exception {
        BackTestingInfoVO backTestingInfoVO = new BackTestingInfoVO(new Date(2010 - 1900, 10 - 1, 15), new Date(2010 - 1900, 12 - 1, 15),
                false, "创业板", new ArrayList<>(), 5, 5, StrategyType.MOMENTUM);
        backTestingBLService.setBackTestingInfo(backTestingInfoVO);
        backTestingBLService.canBackTest();
        List<BackTestingSingleChartVO> backTestingSingleChartVOS = backTestingBLService.getBackTestResult();

        Assert.assertNotEquals(new ArrayList<>(), backTestingSingleChartVOS);
    }

    @org.junit.Test
    public void testGetBackTestStatistics() throws Exception {
        BackTestingInfoVO backTestingInfoVO = new BackTestingInfoVO(new Date(2010 - 1900, 10 - 1, 15), new Date(2010 - 1900, 12 - 1, 15),
                false, "创业板", new ArrayList<>(), 5, 5, StrategyType.MOMENTUM);
        backTestingBLService.setBackTestingInfo(backTestingInfoVO);
        backTestingBLService.canBackTest();
        backTestingBLService.getBackTestResult();
        StrategyStatisticsVO strategyStatisticsVO = backTestingBLService.getBackTestStatistics();

        Assert.assertNotEquals(null, strategyStatisticsVO);
    }

    @org.junit.Test
    public void testSetBackTestingInfo() throws Exception {
        BackTestingInfoVO backTestingInfoVO = new BackTestingInfoVO(new Date(2010 - 1900, 10 - 1, 15), new Date(2010 - 1900, 12 - 1, 15),
                false, "创业板", new ArrayList<>(), 5, 5, StrategyType.MOMENTUM);
        backTestingBLService.setBackTestingInfo(backTestingInfoVO);
        BackTestingJudgeVO backTestingJudgeVO = backTestingBLService.canBackTest();
        Assert.assertEquals(true, backTestingJudgeVO.getCanBackTest());
    }

    @org.junit.Test
    public void testGetReturnRateDistribution() throws Exception {
        BackTestingInfoVO backTestingInfoVO = new BackTestingInfoVO(new Date(2010 - 1900, 10 - 1, 15), new Date(2010 - 1900, 12 - 1, 15),
                false, "创业板", new ArrayList<>(), 5, 5, StrategyType.MOMENTUM);
        backTestingBLService.setBackTestingInfo(backTestingInfoVO);
        BackTestingJudgeVO backTestingJudgeVO = backTestingBLService.canBackTest();
        backTestingBLService.getBackTestResult();
        List<Double> doubles = backTestingBLService.getReturnRateDistribution();
        Assert.assertNotEquals(new ArrayList<>(), doubles);
    }

}
