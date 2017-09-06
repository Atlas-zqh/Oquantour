package oquantour.service.servicehelper.analysis.stock;

import oquantour.po.StockBasicInfoPO;
import oquantour.po.StockPO;

/**
 * 股票评级
 * 股票评级系数：1.00~1.09强力买入；1.10~2.09买入；2.10~3.09观望；3.10~4.09适度减持；4.10~5.00卖出
 * Created by keenan on 10/06/2017.
 */
public class StockScore {
    public double getScore(StockPO stockPO, StockBasicInfoPO basicInfoPOS) {
        // 股价波动性
        double volatility = ((stockPO.getHighPrice() - stockPO.getLowPrice()) / stockPO.getLowPrice()) * 20;
        // 获利能力
        double net_profit_ratio = basicInfoPOS.getNetProfitRatio() == null ? 0.0 : basicInfoPOS.getNetProfitRatio();
        double profitability = (net_profit_ratio / 100) * 40;
        // 股票市场性
        double currentasset_turnover = basicInfoPOS.getCurrentassetTurnover() == null ? 0.0 : basicInfoPOS.getCurrentassetTurnover();
        double market_value = currentasset_turnover * 15;
        // 营运能力
        double adratio = basicInfoPOS.getAdRatio() == null ? 0.0 : basicInfoPOS.getAdRatio();
        double run_value = (adratio / 100) * 15;
        // 短期偿债能力
        double current_ratio = basicInfoPOS.getCurrentRatio() == null ? 0.0 : basicInfoPOS.getCurrentRatio();
        double debt_value = (current_ratio / 100) * 5;
        // 财务结构
        double sheq_ratio = basicInfoPOS.getSheqRatio() == null ? 0.0 : basicInfoPOS.getSheqRatio();
        double finance_value = (sheq_ratio / 100) * 5;

        return volatility + profitability + market_value + run_value + debt_value + finance_value;
    }
}
