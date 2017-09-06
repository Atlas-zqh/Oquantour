package oquantour.service.servicehelper.analysis.stock.indices;

import oquantour.po.StockPO;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;

import java.util.ArrayList;
import java.util.List;

/**
 * 股票行情分析
 * <p>
 * Created by keenan on 16/05/2017.
 */
public class AdjAnalysis {
    /**
     * 得到股票投资建议
     *
     * @param stocks 股票信息
     * @return 投资建议
     */
    public String getInvestmentSuggestion(List<StockPO> stocks) {
        // 倒数一个波浪
        int last_index = 0;
        // 倒数第二个波浪
        int snd2last_index = 0;
        // 波浪计数
        int cnt = 0;
        // 寻找最后两个波浪点的日期
        for (int i = stocks.size() - 2; i >= 1; i--) {
            StockPO current = stocks.get(i);
            StockPO right = stocks.get(i + 1);
            StockPO left = stocks.get(i - 1);

            if ((right.getAdjClose() > current.getAdjClose() && left.getAdjClose() > current.getAdjClose()) || (right.getAdjClose() < current.getAdjClose() && left.getAdjClose() < current.getAdjClose())) {
                if (cnt == 0) {
                    last_index = i;
                    cnt++;
                    continue;
                } else if (cnt == 1) {
                    snd2last_index = i;
                    break;
                }
            }
        }

        // 特殊情况
        if (Math.abs(last_index - snd2last_index) < 2) {
            return "该股近几日股价波动较大，建议进一步持股观望。";
        }

        // 非线性回归拟合 - 抛物线
        int t = 1;
        WeightedObservedPoints adjCloseFunc = new WeightedObservedPoints();
        WeightedObservedPoints volumeFunc = new WeightedObservedPoints();

        for (int j = snd2last_index; j < stocks.size(); j++) {
            adjCloseFunc.add(t, stocks.get(j).getAdjClose());
            volumeFunc.add(t, stocks.get(j).getVolume());
            t++;
        }

        double[] adjCloseFunc_coeff = polynomialCurve(adjCloseFunc);
        double[] volumeFunc_coeff = polynomialCurve(volumeFunc);

        double a = adjCloseFunc_coeff[2];
        double b = adjCloseFunc_coeff[1];
        double c = adjCloseFunc_coeff[0];

        double r = volumeFunc_coeff[2];
        double p = volumeFunc_coeff[1];
        double q = volumeFunc_coeff[0];

        List<Double> s = new ArrayList<>();
        List<Double> up = new ArrayList<>();// 分子

        // 计算股票的成交量对价格的回归函数的弹性
        for (int j = 1; j <= stocks.size() - snd2last_index; j++) {
            up.add((2 * a * r * j * j * j + (2 * b * r + q * p) * j * j + (2 * r * c + p * b) * j + p * c));
            s.add((2 * a * r * j * j * j + (2 * b * r + q * p) * j * j + (2 * r * c + p * b) * j + p * c) /
                    (2 * a * r * j * j * j) + (2 * a * p + b * r) * j * j + (2 * a * q + b * p) * j + b * q);
        }

        final String[] adviceOptions = {
                "股票处于价升量增阶段，价格在上升过程中受到了成交量的强有力配合和支持，投资者宜买进而不宜卖出，特别对于空仓投资者来说，选股时在同等条件下应先选择弹性最大的股票买入。",
                "股票处于价跌量减阶段，市场杀跌动力不足，持股的投资者不宜再杀跌，空仓的投资者应该主动补仓。",
                "股票处于价升量增阶段，但价格在上升过程中缺乏足够的成交量的配合和支持，许多获利筹码没有得到充分释放，此时该股的投资风险较大，股价未来可能将出现平盘整理或回调整理，建议投资者持币观望。",
                "股票处于价跌量减阶段，市场杀跌动力较大，该股在近期内很难有较强劲的反弹行情，空仓投资者应多看少动。",
                "股票处于价升量减阶段，该股的大多数筹码被庄家或机构大户所锁定，股价的无量空涨孕育了极大的投资风险，如果一有风吹草动或庄家出调，该股深幅回调势在必然，尽管该股在近日内有凌厉的涨势，投资者也不宜为贪图一时的近利而去冒不必要的危险。",
                "股票处于价跌量增阶段，这种量价背离现象说明，做空者出货坚决，市场杀跌动力较大，如无实质性利好消息出台，该股后期的行情将继续下行，不断向下寻求支撑。"
        };

        String advice = "";
        // 涨跌情况判断
        List<Integer> statuses = new ArrayList<>();
        // 生成投资建议
        int status = -1;
        for (int i = snd2last_index; i < stocks.size(); i++) {
            int index = i - snd2last_index;
            if (s.get(index) > 1) {
                if (up.get(index) > 0) {
                    status = 0;
                } else if (up.get(index) < 0) {
                    status = 1;
                }
            } else if (s.get(index) > 0 && s.get(index) < 1) {
                if (up.get(index) > 0) {
                    status = 2;
                } else if (up.get(index) < 0) {
                    status = 3;
                }
            } else if (s.get(index) < 0) {
                if (up.get(index) < 0) {
                    status = 4;
                } else if (up.get(index) > 0) {
                    status = 5;
                }
            }

            statuses.add(status);
        }

        int currentNum = statuses.get(0);
        int currentStart = snd2last_index;
        int currentEnd = snd2last_index;
        for (int i = snd2last_index + 1; i < stocks.size(); i++) {
            int index = i - snd2last_index;

            if (statuses.get(index) == currentNum) {
                currentEnd++;
                continue;
            } else {
                currentNum = statuses.get(index - 1);
                advice += stocks.get(currentStart).getDateValue() + "到" + stocks.get(currentEnd).getDateValue() + adviceOptions[currentNum] + "\n";
                currentNum = statuses.get(index);
                currentStart = i;
                currentEnd++;
            }

        }

        advice += stocks.get(currentStart).getDateValue() + "到" + stocks.get(currentEnd).getDateValue() + adviceOptions[currentNum] + "\n";
        return advice;
    }

    /**
     * 获得抛物线回归拟合的参数 (0:指数为0, 1:指数为1, 2:指数为2)
     *
     * @param weightedObservedPoints 原始点集
     * @return 拟合结果参数
     */
    private double[] polynomialCurve(WeightedObservedPoints weightedObservedPoints) {
        final PolynomialCurveFitter fitter = PolynomialCurveFitter.create(2);

        return fitter.fit(weightedObservedPoints.toList());
    }

}
