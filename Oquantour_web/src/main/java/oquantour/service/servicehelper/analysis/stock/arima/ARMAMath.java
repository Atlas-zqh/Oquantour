package oquantour.service.servicehelper.analysis.stock.arima;

import Jama.Matrix;

/**
 * Created by keenan on 16/05/2017.
 */
public class ARMAMath {
    /**
     * 均值
     *
     * @param dataArray
     * @return
     */
    protected double avgData(double[] dataArray) {
        return this.sumData(dataArray) / dataArray.length;
    }

    /**
     * 和
     * @param dataArray
     * @return
     */
    protected double sumData(double[] dataArray) {
        double sumData = 0;
        for (int i = 0; i < dataArray.length; i++) {
            sumData += dataArray[i];
        }
        return sumData;
    }

    /**
     * 标准化后的方差
     * @param dataArray
     * @return
     */
    protected double varerrData(double[] dataArray) {
        double variance = 0;
        double avgsumData = this.avgData(dataArray);

        for (int i = 0; i < dataArray.length; i++) {
            dataArray[i] -= avgsumData;
            variance += dataArray[i] * dataArray[i];
        }
        return variance / dataArray.length;//variance error;
    }

    /**
     * 计算自相关的函数 Tho(k)=Grma(k)/Grma(0)
     *
     * @param dataArray 数列
     * @param order     阶数
     * @return
     */
    protected double[] autoCorData(double[] dataArray, int order) {
        double[] autoCor = new double[order + 1];
        double varData = this.varerrData(dataArray);//标准化过后的方差

        for (int i = 0; i <= order; i++) {
            autoCor[i] = 0;
            for (int j = 0; j < dataArray.length - i; j++) {
                autoCor[i] += dataArray[j + i] * dataArray[j];
            }
            autoCor[i] /= dataArray.length;
            autoCor[i] /= varData;
        }
        return autoCor;
    }

    /**
     * Grma
     *
     * @param dataArray
     * @param order
     * @return 序列的自相关系数
     */
    protected double[] autoCorGrma(double[] dataArray, int order) {
        double[] autoCor = new double[order + 1];
        for (int i = 0; i <= order; i++) {
            autoCor[i] = 0;
            for (int j = 0; j < dataArray.length - i; j++) {
                autoCor[i] += dataArray[j + i] * dataArray[j];
            }
            autoCor[i] /= (dataArray.length - i);
        }
        return autoCor;
    }

    /**
     * 解MA模型的参数
     *
     * @param autoCorData
     * @param q
     * @return
     */
    protected double[] getMApara(double[] autoCorData, int q) {
        double[] maPara = new double[q + 1];//第一个存放噪声参数，后面q个存放ma参数sigma2,ma1,ma2...
        double[] tempmaPara = maPara;
        double temp = 0;
        boolean iterationFlag = true;
        //解方程组
        //迭代法解方程组
        maPara[0] = 1;
        while (iterationFlag) {
            for (int i = 1; i < maPara.length; i++) {
                temp += maPara[i] * maPara[i];
            }
            tempmaPara[0] = autoCorData[0] / (1 + temp);

            for (int i = 1; i < maPara.length; i++) {
                temp = 0;
                for (int j = 1; j < maPara.length - i; j++) {
                    temp += maPara[j] * maPara[j + i];
                }
                tempmaPara[i] = -(autoCorData[i] / maPara[0] - temp);
            }
            iterationFlag = false;
            for (int i = 0; i < maPara.length; i++) {
                if (maPara[i] != tempmaPara[i]) {
                    iterationFlag = true;
                    break;
                }
            }
            maPara = tempmaPara;
        }

        return maPara;
    }

    /**
     * 计算自回归系数
     *
     * @param dataArray
     * @param p
     * @param q
     * @return
     */
    protected double[] parcorrCompute(double[] dataArray, int p, int q) {
        double[][] toplizeArray = new double[p][p];//p阶toplize矩阵；

        double[] atuocorr = this.autoCorData(dataArray, p + q);//返回p+q阶的自相关函数
        double[] autocorrF = this.autoCorGrma(dataArray, p + q);//返回p+q阶的自相关系数数
        for (int i = 1; i <= p; i++) {
            int k = 1;
            for (int j = i - 1; j > 0; j--) {
                toplizeArray[i - 1][j - 1] = atuocorr[q + k++];
            }
            toplizeArray[i - 1][i - 1] = atuocorr[q];
            int kk = 1;
            for (int j = i; j < p; j++) {
                toplizeArray[i - 1][j] = atuocorr[q + kk++];
            }
        }

        Matrix toplizeMatrix = new Matrix(toplizeArray);//由二位数组转换成二维矩阵
        Matrix toplizeMatrixInverse = toplizeMatrix.inverse();//矩阵求逆运算

        double[] temp = new double[p];
        for (int i = 1; i <= p; i++) {
            temp[i - 1] = atuocorr[q + i];
        }

        Matrix autocorrMatrix = new Matrix(temp, p);
        Matrix parautocorDataMatrix = toplizeMatrixInverse.times(autocorrMatrix); //  [Fi]=[toplize]x[autocorr]';
        //矩阵计算结果应该是按照[a b c] 列向量存储的

        double[] result = new double[parautocorDataMatrix.getRowDimension() + 1];
        for (int i = 0; i < parautocorDataMatrix.getRowDimension(); i++) {
            result[i] = parautocorDataMatrix.get(i, 0);
        }

        // 估算sigma2
        double sum2 = 0;
        for (int i = 0; i < p; i++)
            for (int j = 0; j < p; j++) {
                sum2 += result[i] * result[j] * autocorrF[Math.abs(i - j)];
            }
        result[result.length - 1] = autocorrF[0] - sum2; // result数组最后一个存储干扰估计值

        return result;
    }

}
