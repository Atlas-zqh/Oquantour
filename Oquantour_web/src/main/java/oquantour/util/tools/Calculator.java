package oquantour.util.tools;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;


/**
 * 计算均值、方差、标准差
 * <p>
 * Created by keenan on 24/03/2017.
 */
public final class Calculator {

    /**
     * 计算总和
     *
     * @param list
     * @return 总和
     */
    public static final double sum(List<Double> list) {
        DoubleSummaryStatistics summaryStatistics = list.stream().mapToDouble((x) -> x).summaryStatistics();
        return summaryStatistics.getSum();
    }

    /**
     * 计算算数平均值
     *
     * @param list
     * @return 算数平均值
     */
    public static final double avg(List<Double> list) {
        DoubleSummaryStatistics summaryStatistics = list.stream().mapToDouble((x) -> x).summaryStatistics();
        return summaryStatistics.getAverage();
    }

    /**
     * 计算标准差
     *
     * @param list
     * @return 标准差
     */
    public static final double std(List<Double> list) {
        return Math.sqrt(variance(list));
    }

    /**
     * 计算方差
     *
     * @param list
     * @return 方差
     */
    public static final double variance(List<Double> list) {
        double avg = avg(list);
        double allSquare = 0;

        for (Double d : list) {
            allSquare += (d - avg) * (d - avg);
        }

        return allSquare / list.size();
    }

    /**
     * 计算最大值
     *
     * @param list
     * @return 最大值
     */
    public static final double max(List<Double> list) {
        DoubleSummaryStatistics summaryStatistics = list.stream().mapToDouble((x) -> x).summaryStatistics();
        return summaryStatistics.getMax();
    }

    /**
     * 计算最小值
     *
     * @param list
     * @return 最小值
     */
    public static final double min(List<Double> list) {
        DoubleSummaryStatistics summaryStatistics = list.stream().mapToDouble((x) -> x).summaryStatistics();
        return summaryStatistics.getMin();
    }

    /**
     * 求两个List对应位置的乘积
     *
     * @param operand1 第一个list
     * @param operand2 第二个list
     * @return 计算结果（如果两个list长度不相等，返回的list.size()=0）
     */
    public static final List<Double> listMultiply(List<Double> operand1, List<Double> operand2) {
        List<Double> result = new ArrayList<>();

        if (operand1.size() != operand2.size()) {
            return result;
        }

        for (int i = 0; i < operand1.size(); i++) {
            result.add(operand1.get(i) * operand2.get(i));
        }

        return result;
    }

    /**
     * 两组数据的协方差
     *
     * @param operand1 第一组数据
     * @param operand2 第二组数据
     * @return 两组数据的协方差
     */
    public static final double cov(List<Double> operand1, List<Double> operand2) {
        List<Double> multiplyResult = listMultiply(operand1, operand2);

        double eXY = avg(multiplyResult);
        double eX = avg(operand1);
        double eY = avg(operand2);

        return eXY - (eX * eY);
    }

    /**
     * 两组数据的相关系数
     *
     * @param a
     * @param b
     * @return 相关系数
     */
    public static final double relative(List<Double> a, List<Double> b) {
        return cov(a, b) / (std(a) * std(b));
    }

}
