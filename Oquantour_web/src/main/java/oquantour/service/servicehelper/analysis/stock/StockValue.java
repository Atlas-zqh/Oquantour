package oquantour.service.servicehelper.analysis.stock;

import oquantour.exception.InnerValueException;
import oquantour.po.PlateinfoPO;
import oquantour.po.StockBasicInfoPO;
import oquantour.po.StockPO;
import oquantour.po.util.ChartInfo;
import oquantour.util.tools.Classifier;
import oquantour.util.tools.PlateEnum;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;

import java.sql.Date;
import java.util.*;

/**
 * 股票内在价值
 * Created by keenan on 08/06/2017.
 */
public class StockValue {

    /**
     * 获得综合评价参数
     *
     * @param stocks         股价数据
     * @param plateInfo      板块信息
     * @param stockBasicInfo 股票季度的基本面数据
     * @return 综合评价参数
     */
    public List<ChartInfo> getEvalPara(List<StockPO> stocks, Map<PlateEnum, List<PlateinfoPO>> plateInfo, Map<String, StockBasicInfoPO> stockBasicInfo, List<ChartInfo> industryPOS) throws InnerValueException {
        Calendar calendar = Calendar.getInstance();

        // 构造x矩阵
        List<Date> plateDateList = new ArrayList<>();
        plateInfo.get(PlateEnum.SH_Composite).stream().forEachOrdered(plateinfoPO -> plateDateList.add(plateinfoPO.getDateValue()));

        List<StockPO> stockPOS = modifyStockData(stocks, plateDateList);
        List<Date> stockDateList = new ArrayList<>();
        stockPOS.stream().forEachOrdered(stockPO -> stockDateList.add(stockPO.getDateValue()));

        Map<Date, StockPO> stockPOMap = Classifier.classifyStockByDate(stocks);
        Map<Date, PlateinfoPO> SH_Composite = Classifier.classifyPlateByDate(plateInfo.get(PlateEnum.SH_Composite));
        Map<Date, PlateinfoPO> SmallPlate_Composite = Classifier.classifyPlateByDate(plateInfo.get(PlateEnum.SmallPlate_Composite));
        Map<Date, PlateinfoPO> SS_300 = Classifier.classifyPlateByDate(plateInfo.get(PlateEnum.SS_300));
        Map<Date, PlateinfoPO> Gem_Composite = Classifier.classifyPlateByDate(plateInfo.get(PlateEnum.Gem_Composite));
        Map<Date, PlateinfoPO> SZ_Composite = Classifier.classifyPlateByDate(plateInfo.get(PlateEnum.SZ_Composite));
        Map<Date, PlateinfoPO> SZA_Composite = Classifier.classifyPlateByDate(plateInfo.get(PlateEnum.SZA_Composite));

        int length = stockDateList.size();
        int width = plateInfo.size() + 9 + 1;
        double[][] x = new double[length][width];

        for (int i = 0; i < length; i++) {
            Date date = stockDateList.get(i);
            calendar.setTime(date);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            String quater = year + "-" + ((month / 4) + 1);
            StockBasicInfoPO stockBasicInfoPO = stockBasicInfo.get(quater);
            if (stockBasicInfoPO == null) {
                if (month / 4 == 0) {
                    quater = (year - 1) + "-" + 4;
                } else {
                    quater = year + "-" + (month / 4);
                }

                stockBasicInfoPO = stockBasicInfo.get(quater);
            }

            if (stockBasicInfoPO == null) {
                throw new InnerValueException("\uD83D\uDE2D\uD83D\uDE2D\uD83D\uDE2D\uD83D\uDE2D</br>这只股票的基本面数据缺失</br>我们暂时无法给出价值估计</br>\uD83D\uDE2D\uD83D\uDE2D\uD83D\uDE2D\uD83D\uDE2D");
            }

            x[i][0] = Math.log(SH_Composite.get(date).getAdjClosePrice());
            x[i][1] = Math.log(SmallPlate_Composite.get(date).getAdjClosePrice());
            x[i][2] = Math.log(SS_300.get(date).getAdjClosePrice());
            x[i][3] = Math.log(Gem_Composite.get(date).getAdjClosePrice());
            x[i][4] = Math.log(SZ_Composite.get(date).getAdjClosePrice());
            x[i][5] = Math.log(SZA_Composite.get(date).getAdjClosePrice());
            x[i][6] = stockBasicInfoPO.getBusinessIncome() != null ? Math.log(stockBasicInfoPO.getBusinessIncome()) : 0.0;
            x[i][7] = stockBasicInfoPO.getGrossProfitRate() != null ? stockBasicInfoPO.getGrossProfitRate() : 0.0;
            x[i][8] = stockBasicInfoPO.getBips() != null ? stockBasicInfoPO.getBips() : 0.0;
            x[i][9] = stockBasicInfoPO.getRoe() != null ? stockBasicInfoPO.getRoe() : 0.0;
            x[i][10] = stockBasicInfoPO.getEpsg() != null ? stockBasicInfoPO.getEpsg() : 0.0;
            x[i][11] = stockBasicInfoPO.getSeg() != null ? stockBasicInfoPO.getSeg() : 0.0;
            x[i][12] = stockBasicInfoPO.getNetProfitRatio() != null ? stockBasicInfoPO.getNetProfitRatio() : 0.0;
            x[i][13] = stockBasicInfoPO.getSheqRatio() != null ? stockBasicInfoPO.getSheqRatio() : 0.0;
            x[i][14] = stockBasicInfoPO.getAdRatio() != null ? stockBasicInfoPO.getAdRatio() : 0.0;
            x[i][15] = industryPOS.get(i).getyAxis();
        }

        // 构造y矩阵
        double[] y = new double[length];
        for (int i = 0; i < y.length; i++) {
            Date date = stockDateList.get(i);
            y[i] = stockPOMap.get(date).getAdjClose();
        }

        // 线性回归
        double[] para;

        try {
            para = getEstimateParameters(y, x);
        } catch (SingularMatrixException e) {
            throw new InnerValueException("\uD83D\uDE05\uD83D\uDE05\uD83D\uDE05\uD83D\uDE05</br>这只股票数据缺失</br>我们无法提供它的股票价值评估</br>\uD83D\uDE05\uD83D\uDE05\uD83D\uDE05\uD83D\uDE05");
        } catch (MathIllegalArgumentException e) {
            throw new InnerValueException("\uD83D\uDE05\uD83D\uDE05\uD83D\uDE05\uD83D\uDE05</br>这只股票...好像最近才上市噢</br>市场数据不足，我们暂时无法给出价值估计</br>\uD83D\uDE05\uD83D\uDE05\uD83D\uDE05\uD83D\uDE05");
        }

        // 计算综合评价参数Alpha
        double[] _y = new double[length];
        List<ChartInfo> chartInfos = new ArrayList<>();
        for (int i = 0; i < stockDateList.size(); i++) {
            double pre = para[0] +
                    para[1] * x[i][0] +
                    para[2] * x[i][1] +
                    para[3] * x[i][2] +
                    para[4] * x[i][3] +
                    para[5] * x[i][4] +
                    para[6] * x[i][5] +
                    para[7] * x[i][6] +
                    para[8] * x[i][7] +
                    para[9] * x[i][8] +
                    para[10] * x[i][9] +
                    para[11] * x[i][10] +
                    para[12] * x[i][11] +
                    para[13] * x[i][12] +
                    para[14] * x[i][13] +
                    para[15] * x[i][14] +
                    para[16] * x[i][15];
            _y[i] = pre;

            chartInfos.add(new ChartInfo(stockDateList.get(i), (_y[i] - y[i]) / y[i]));
        }
        return chartInfos;
    }

    /**
     * 进行多元线性回归分析
     *
     * @param y
     * @param x
     * @return 估计参数
     */
    private double[] getEstimateParameters(double[] y, double[][] x) {
        OLSMultipleLinearRegression regression = new OLSMultipleLinearRegression();
        regression.newSampleData(y, x);
        return regression.estimateRegressionParameters();
    }

    /**
     * 删除Stock信息里日期不在dateList中的数据
     *
     * @param stockPOS
     * @param dateList
     * @return 删除结果
     */
    private List<StockPO> modifyStockData(List<StockPO> stockPOS, List<Date> dateList) {
        List<StockPO> res = new ArrayList<>(stockPOS);
        Iterator<StockPO> iterator = res.iterator();
        while (iterator.hasNext()) {
            StockPO stock = iterator.next();
            if (!dateList.contains(stock.getDateValue())) {
                iterator.remove();
            }
        }
        return res;
    }


}
