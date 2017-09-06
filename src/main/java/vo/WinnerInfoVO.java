package vo;

import java.util.Date;
import java.util.List;

/**
 * 赢家股票信息
 * <p>
 * Created by keenan on 18/04/2017.
 */
public class WinnerInfoVO {
    private Date changeDate;

    private List<StockBasicInfoVO> winners;

    public WinnerInfoVO(Date changeDate, List<StockBasicInfoVO> winners) {
        this.changeDate = changeDate;
        this.winners = winners;
    }

    public Date getChangeDate() {
        return changeDate;
    }

    public List<StockBasicInfoVO> getWinners() {
        return winners;
    }
}
