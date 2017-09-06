package oquantour.service.servicehelper.analysis.stock.arima;

import java.util.Vector;

/**
 * 移动平均过程 MA(q)
 * <p>
 * Created by keenan on 16/05/2017.
 */
public class MA {
    private double[] stdoriginalData = {};
    private int q;
    private ARMAMath armamath = new ARMAMath();

    /**
     * MA模型
     *
     * @param stdoriginalData 预处理过后的数据
     * @param q               q为MA模型阶数
     */
    protected MA(double[] stdoriginalData, int q) {
        this.stdoriginalData = stdoriginalData;
        this.q = q;
    }

    /**
     * 返回MA模型参数
     *
     * @return
     */
    protected Vector<double[]> MAModel() {
        Vector<double[]> v = new Vector<>();
        v.add(armamath.getMApara(armamath.autoCorGrma(stdoriginalData, q), q));
        return v;//拿到MA模型里面的参数值
    }
}
