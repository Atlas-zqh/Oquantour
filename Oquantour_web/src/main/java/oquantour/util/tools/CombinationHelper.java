package oquantour.util.tools;

import oquantour.exception.UnsupportedOperationException;
import oquantour.po.util.BackTestingSingleChartInfo;
import oquantour.po.util.ChartInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by keenan on 11/05/2017.
 */
public class CombinationHelper {
    /**
     * 将回测收益率和基准收益率合并
     *
     * @param backTest  策略收益率
     * @param benchmark 基准收益率
     * @return 合并成BackTestingSingleChartInfo
     * @throws UnsupportedOperationException 非法操作
     */
    public static List<BackTestingSingleChartInfo> combinedReturnRate(List<ChartInfo> backTest, List<ChartInfo> benchmark) throws UnsupportedOperationException {
        if (backTest.isEmpty() || backTest == null || benchmark.isEmpty() || backTest == null) {
            System.out.println("ConbinationHelper 跑出了Unsupported异常！！！！！！！！！！！！！！！！！！！！！！！！");
            throw new UnsupportedOperationException();
        }

        if (backTest.size() != benchmark.size()) {
            System.out.println(backTest.size() + "   " + benchmark.size());
            throw new UnsupportedOperationException();
        }

        List<BackTestingSingleChartInfo> result = new ArrayList<>();

        for (int i = 0; i < backTest.size(); i++) {
            result.add(new BackTestingSingleChartInfo(backTest.get(i), benchmark.get(i)));
        }

        return result;
    }
}
