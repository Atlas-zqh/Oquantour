package oquantour.service.servicehelper.analysis.stock.arima;

import java.util.Vector;

/**
 * 自回归过程 AR(p)
 * <p>
 * Created by keenan on 16/05/2017.
 */
public class AR {
    private double[] stdoriginalData = {};
    private int p;
    private ARMAMath armamath = new ARMAMath();

    /**
     * AR模型
     *
     * @param stdoriginalData
     * @param p               //p为MA模型阶数
     */
    protected AR(double[] stdoriginalData, int p) {
        this.stdoriginalData = stdoriginalData;
        this.p = p;
    }

    /**
     * 返回AR模型参数
     *
     * @return
     */
    protected Vector<double[]> ARModel() {
        Vector<double[]> v = new Vector<>();
        v.add(armamath.parcorrCompute(stdoriginalData, p, 0));
        return v;//得到了自回归系数
    }
}