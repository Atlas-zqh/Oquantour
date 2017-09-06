package oquantour.service.servicehelper.analysis.stock.arima;

import java.util.Random;
import java.util.Vector;

/**
 * 自回归积分滑动平均过程 ARIMA(p,d,q)
 * <p>
 * Created by keenan on 16/05/2017.
 */
public class ARIMA {
    private double[] originalData = {};
    private Vector<double[]> armaARMAcoe = new Vector<>();
    private Vector<double[]> bestarmaARMAcoe = new Vector<>();

    /**
     * 构造函数
     *
     * @param originalData 原始时间序列数据
     */
    public ARIMA(double[] originalData) {
        this.originalData = originalData;
    }

    /**
     * 得到预测值
     *
     * @return 预测值
     */
    public double getPredictValue() {
        int[] model = this.getARIMAModel();
        double predictValue = this.predictValue(model[0], model[1]);
        // 对预测值进行反差分处理
        return (predictValue + originalData[originalData.length - 7]);
    }

    /**
     * 原始数据标准化处理：一阶差分，距离为7
     *
     * @return 差分过后的数据
     */
    private double[] difOperation() {
        double[] tempData = new double[originalData.length - 7];
        for (int i = 0; i < originalData.length - 7; i++) {
            tempData[i] = originalData[i + 7] - originalData[i];
        }
        return tempData;
    }

    /**
     * 得到ARMA模型=[p,q]
     *
     * @return ARMA模型的阶数信息
     */
    private int[] getARIMAModel() {
        double[] stdOriginalData = this.difOperation();//原始数据平稳化差分处理
        int paraType;
        // 最小的AIC可以最好地解释数据但包含最少自由参数
        double minAIC = Double.MAX_VALUE;
        int bestModelIndex = 0;
        int[][] model = new int[][]{{0, 1}, {1, 0}, {1, 1}, {0, 2}, {2, 0}, {2, 2}, {1, 2}, {2, 1}, {3, 0}, {0, 3}, {3, 1}, {1, 3}, {3, 2}, {2, 3}, {3, 3}, {4, 0}, {0, 4}, {4, 1}, {1, 4}, {4, 2}, {2, 4}, {4, 3}, {3, 4}, {4, 4}};
        // 选出AIC值最小的模型
        for (int i = 0; i < model.length; i++) {
            if (model[i][0] == 0) {
                // p
                MA ma = new MA(stdOriginalData, model[i][1]);
                // 加载模型的参数
                armaARMAcoe = ma.MAModel();
                paraType = 1;
            } else if (model[i][1] == 0) {
                // q
                AR ar = new AR(stdOriginalData, model[i][0]);
                // 加载模型的参数
                armaARMAcoe = ar.ARModel();
                paraType = 2;
            } else {
                ARMA arma = new ARMA(stdOriginalData, model[i][0], model[i][1]);
                // 加载模型的参数
                armaARMAcoe = arma.ARMAModel();
                paraType = 3;
            }

            double aic = getAIC(armaARMAcoe, stdOriginalData, paraType);
            if (aic < minAIC) {
                bestModelIndex = i;
                minAIC = aic;
                bestarmaARMAcoe = armaARMAcoe;
            }
        }

        return model[bestModelIndex];
    }

    /**
     * 计算AIC
     *
     * @param para            装载模型的参数信息
     * @param stdoriginalData 预处理过后的原始数据
     * @param type            1：MA；2：AR；3：ARMA
     * @return 模型的AIC值
     */
    private double getAIC(Vector<double[]> para, double[] stdoriginalData, int type) {
        double temp;
        double temp2;
        double sumErr = 0;
        int p;  // ar1,ar2,...,sig2
        int q = 0;  // sig2,ma1,ma2...
        int n = stdoriginalData.length;
        Random random = new Random();

        if (type == 1) {
            double[] maPara = para.get(0);
            q = maPara.length;
            double[] err = new double[q];  //error(t),error(t-1),error(t-2)...
            for (int k = q - 1; k < n; k++) {
                temp = 0;

                for (int i = 1; i < q; i++) {
                    temp += maPara[i] * err[i];
                }
                //产生各个时刻的噪声
                for (int j = q - 1; j > 0; j--) {
                    err[j] = err[j - 1];
                }
                err[0] = random.nextGaussian() * Math.sqrt(maPara[0]);
                //估计的方差之和
                sumErr += (stdoriginalData[k] - (temp)) * (stdoriginalData[k] - (temp));
            }
            return (n - (q - 1)) * Math.log(sumErr / (n - (q - 1))) + (q + 1) * 2;
        } else if (type == 2) {
            double[] arPara = para.get(0);
            p = arPara.length;
            for (int k = p - 1; k < n; k++) {
                temp = 0;
                for (int i = 0; i < p - 1; i++) {
                    temp += arPara[i] * stdoriginalData[k - i - 1];
                }
                //估计的方差之和
                sumErr += (stdoriginalData[k] - temp) * (stdoriginalData[k] - temp);
            }
            return (n - (q - 1)) * Math.log(sumErr / (n - (q - 1))) + (p + 1) * 2;
        } else {
            double[] arPara = para.get(0);
            double[] maPara = para.get(1);
            p = arPara.length;
            q = maPara.length;
            double[] err = new double[q];  //error(t),error(t-1),error(t-2)...

            for (int k = p - 1; k < n; k++) {
                temp = 0;
                temp2 = 0;
                for (int i = 0; i < p - 1; i++) {
                    temp += arPara[i] * stdoriginalData[k - i - 1];
                }
                for (int i = 1; i < q; i++) {
                    temp2 += maPara[i] * err[i];
                }
                //产生各个时刻的噪声
                for (int j = q - 1; j > 0; j--) {
                    err[j] = err[j - 1];
                }
                err[0] = random.nextGaussian() * Math.sqrt(maPara[0]);
                //估计的方差之和
                sumErr += (stdoriginalData[k] - (temp2 + temp)) * (stdoriginalData[k] - (temp2 + temp));
            }
            return (n - (q - 1)) * Math.log(sumErr / (n - (q - 1))) + (p + q) * 2;
        }
    }


    /**
     * 进行一步预测
     *
     * @param p ARMA模型的AR的阶数
     * @param q ARMA模型的MA的阶数
     * @return 预测值
     */
    private double predictValue(int p, int q) {
        double predict;
        double[] stdoriginalData = this.difOperation();
        int n = stdoriginalData.length;
        double temp = 0, temp2 = 0;
        double[] err = new double[q + 1];

        Random random = new Random();
        if (p == 0) {
            double[] maPara = bestarmaARMAcoe.get(0);
            for (int k = q; k < n; k++) {
                temp = 0;
                for (int i = 1; i <= q; i++) {
                    temp += maPara[i] * err[i];
                }
                //产生各个时刻的噪声
                for (int j = q; j > 0; j--) {
                    err[j] = err[j - 1];
                }
                err[0] = random.nextGaussian() * Math.sqrt(maPara[0]);
            }
            predict = (int) (temp);
        } else if (q == 0) {
            double[] arPara = bestarmaARMAcoe.get(0);
            for (int k = p; k < n; k++) {
                temp = 0;
                for (int i = 0; i < p; i++) {
                    temp += arPara[i] * stdoriginalData[k - i - 1];
                }
            }
            predict = (int) (temp);
        } else {

            double[] arPara = bestarmaARMAcoe.get(0);
            double[] maPara = bestarmaARMAcoe.get(1);
            err = new double[q + 1];  //error(t),error(t-1),error(t-2)...
            for (int k = p; k < n; k++) {
                temp = 0;
                temp2 = 0;
                for (int i = 0; i < p; i++) {
                    temp += arPara[i] * stdoriginalData[k - i - 1];
                }
                for (int i = 1; i <= q; i++) {
                    temp2 += maPara[i] * err[i];
                }
                //产生各个时刻的噪声
                for (int j = q; j > 0; j--) {
                    err[j] = err[j - 1];
                }
                err[0] = random.nextGaussian() * Math.sqrt(maPara[0]);
            }
            predict = (temp2 + temp);
        }
        return predict;
    }
}
