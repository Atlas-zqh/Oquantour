package vo;

import bl.tools.StrategyType;

import java.util.Date;
import java.util.List;

/**
 * 回测信息
 * <p>
 * Created by keenan on 23/03/2017.
 */
public class BackTestingInfoVO {
    // 回测开始日期
    public Date startDate;
    // 回测结束日期
    public Date endDate;
    // 是否显示所有股票的回测结果（如果为true，板块名称为""，多只股票信息stocks.size()=0
    public boolean showAll;
    // 股票板块名称(opt.)
    public String stockPlateName;
    // 要回测的多只股票名称或代码(opt.)
    public List<String> stocks;
    // 形成期
    public int formativePeriod;
    // 持有期
    public int holdingPeriod;
    // 几日均线
    public int ma_length;
    // 策略类型
    public StrategyType strategyType;
    // 最大持仓股票数目
    public int maxholdingStocks;

    /**
     * 动量策略
     */
    public BackTestingInfoVO(Date startDate, Date endDate, boolean showAll, String stockPlateName, List<String> stocks, int formativePeriod, int holdingPeriod, StrategyType strategyType) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.showAll = showAll;
        this.stockPlateName = stockPlateName;
        this.stocks = stocks;
        this.formativePeriod = formativePeriod;
        this.holdingPeriod = holdingPeriod;
        this.strategyType = strategyType;
    }

    /**
     * 均值回归
     */
    public BackTestingInfoVO(Date startDate, Date endDate, boolean showAll, String stockPlateName, int ma_length, List<String> stocks, int holdingPeriod, StrategyType strategyType, int maxholdingStocks) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.showAll = showAll;
        this.stockPlateName = stockPlateName;
        this.stocks = stocks;
        this.holdingPeriod = holdingPeriod;
        this.ma_length = ma_length;
        this.strategyType = strategyType;
        this.maxholdingStocks = maxholdingStocks;
    }
}
