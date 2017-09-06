package oquantour.service.servicehelper.analysis.portfolio;

import oquantour.po.StockCombination;
import oquantour.util.tools.Calculator;
import oquantour.po.util.ChartInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 投资组合分析
 * Created by keenan on 05/06/2017.
 */
public class PortfolioAnalysis {
    public static final String cnt = "累计交易次数", avg = "平均仓位";
    /**
     * 获得年化收益波动率
     *
     * @param chartInfos 收益率
     * @return 年化收益波动率
     */
    public Double getVolatility(List<ChartInfo> chartInfos) {
        List<Double> dailyStrategyReturnRate = new ArrayList<>();
        chartInfos.stream().forEach(result -> dailyStrategyReturnRate.add(result.getyAxis()));
        double avg = Calculator.avg(dailyStrategyReturnRate);
        double minusSquareSum = 0;
        for (Double value : dailyStrategyReturnRate) {
            minusSquareSum += (value - avg) * (value - avg);
        }
        return Math.sqrt((250.0 / dailyStrategyReturnRate.size()) * minusSquareSum);
    }

    /**
     * 获得平均仓位
     *
     * @param portfolios 投资组合
     * @return 每个股票的平均仓位(avg), 累计交易次数(cnt)
     */
    public Map<String, Map<String, Double>> getAvgPosition(List<StockCombination> portfolios) {
        Map<String, Double> map = new HashMap<>();
        Map<String, Double> cntMap = new HashMap<>();

        for (StockCombination portfolio : portfolios) {
            List<String> stockIds = portfolio.getStocks();
            List<Double> positions = portfolio.getPositions();
            for (int i = 0; i < stockIds.size(); i++) {
                String s = stockIds.get(i);
                Double p = positions.get(i);
                if (map.get(s) == null) {
                    map.put(s, p);
                } else {
                    Double tmp = map.get(s);
                    tmp += p;
                    map.put(s, tmp);
                }
                if (cntMap.get(s) == null) {
                    cntMap.put(s, 1.0);
                } else {
                    Double tmp = cntMap.get(s);
                    tmp += 1.0;
                    cntMap.put(s, tmp);
                }
            }
        }
        for (Map.Entry<String, Double> entry : cntMap.entrySet()) {
            Double sum = map.get(entry.getKey());
            sum /= entry.getValue();
            map.put(entry.getKey(), sum);
        }
        Map<String, Map<String, Double>> result = new HashMap<>();
        result.put(cnt, cntMap);
        result.put(avg, map);
        return result;
    }

}
