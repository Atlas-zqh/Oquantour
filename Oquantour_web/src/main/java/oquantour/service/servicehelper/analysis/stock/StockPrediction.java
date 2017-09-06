package oquantour.service.servicehelper.analysis.stock;

import oquantour.service.servicehelper.analysis.stock.arima.ARIMA;
import oquantour.util.tools.Calculator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by keenan on 16/05/2017.
 */
public class StockPrediction {
    /**
     * 得到预测值
     *
     * @param originData 起始数据（按时间顺序排序：过去 -> 现在）
     * @return 预测值
     */
    public double getNextPrediction(List<Double> originData) {

        double[] lastDataArray = new double[originData.size()];
        for (int i = 0; i < originData.size() - 1; i++) {
            lastDataArray[i] = originData.get(i);
        }
        ARIMA arima = new ARIMA(lastDataArray);
        List<Double> possibleValues = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            possibleValues.add(arima.getPredictValue());
        }
        return Calculator.avg(possibleValues);
    }
}
