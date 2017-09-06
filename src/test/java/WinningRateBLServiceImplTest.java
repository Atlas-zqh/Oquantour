import bl.strategy.BackTestingBLService;
import bl.strategy.BackTestingBLServiceImpl;
import bl.strategy.WinningRateBLService;
import bl.strategy.WinningRateBLServiceImpl;
import bl.tools.StrategyType;
import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Before;
import vo.BackTestingInfoVO;
import vo.WinningRateVO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * WinningRateBLServiceImpl Tester.
 *
 * @author <keenan>
 * @version 1.0
 * @since <pre>04/17/2017</pre>
 */
public class WinningRateBLServiceImplTest {
    private WinningRateBLService winningRateBLService;

    @Before
    public void setUpBeforeClass() throws Exception {
        winningRateBLService = new WinningRateBLServiceImpl();
    }

    @org.junit.Test
    public void testGetWinningRate_FixedFormative() throws Exception {
        BackTestingInfoVO backTestingInfoVO = new BackTestingInfoVO(new Date(2010 - 1900, 10 - 1, 15), new Date(2010 - 1900, 12 - 1, 15),
                false, "创业板", new ArrayList<>(), 5, 5, StrategyType.MOMENTUM);

        List<WinningRateVO> winningRateVOS = winningRateBLService.getWinningRate_FixedFormative(backTestingInfoVO);
        Assert.assertNotEquals(new ArrayList<>(), winningRateVOS);
    }

    @org.junit.Test
    public void testGetWinningRate_FixedHolding() throws Exception {
        BackTestingInfoVO backTestingInfoVO = new BackTestingInfoVO(new Date(2010 - 1900, 10 - 1, 15), new Date(2010 - 1900, 12 - 1, 15),
                false, "创业板", new ArrayList<>(), 5, 5, StrategyType.MOMENTUM);

        List<WinningRateVO> winningRateVOS = winningRateBLService.getWinningRate_FixedHolding(backTestingInfoVO);
        Assert.assertNotEquals(new ArrayList<>(), winningRateVOS);
    }

}
