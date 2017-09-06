package oquantour.service.servicehelper.analysis.stock.arima;

import java.util.Vector;

/**
 * 自回归移动平均过程 ARMA(p,q)
 * <p>
 * Created by keenan on 16/05/2017.
 */
public class ARMA {
    private double[] stdoriginalData = {};
    private int p;
    private int q;
    private ARMAMath armamath = new ARMAMath();

    /**
     * ARMA模型
     *
     * @param stdoriginalData
     * @param p,q             p,q为MA模型阶数
     */
    protected ARMA(double[] stdoriginalData, int p, int q) {
        this.stdoriginalData = stdoriginalData;
        this.p = p;
        this.q = q;
    }

    protected Vector<double[]> ARMAModel() {

        double[] arcoe = armamath.parcorrCompute(stdoriginalData, p, q);

        double[] autocorData = getautocorofMA(p, q, stdoriginalData, arcoe);

        double[] macoe = armamath.getMApara(autocorData, q);//得到MA模型里面的参数值

        Vector<double[]> v = new Vector<>();
        v.add(arcoe);
        v.add(macoe);
        return v;
    }

    /**
     * 得到MA的自相关系数
     *
     * @param p
     * @param q
     * @param stdoriginalData
     * @return
     */
    protected double[] getautocorofMA(int p, int q, double[] stdoriginalData, double[] autoRegress) {
        int temp = 0;
        double[] errArray = new double[stdoriginalData.length - p];
        int count = 0;
        for (int i = p; i < stdoriginalData.length; i++) {
            temp = 0;
            for (int j = 1; j <= p; j++)
                temp += stdoriginalData[i - j] * autoRegress[j - 1];
            errArray[count++] = stdoriginalData[i] - temp;//保存估计残差序列
        }
        return armamath.autoCorGrma(errArray, q);
    }

}
