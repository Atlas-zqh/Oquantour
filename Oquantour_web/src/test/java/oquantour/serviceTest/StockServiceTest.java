package oquantour.serviceTest;

import oquantour.BaseTest;
import oquantour.exception.StockDataNotExistException;
import oquantour.po.StockPO;
import oquantour.po.StockRealTimePO;
import oquantour.service.StockService;
import oquantour.service.servicehelper.analysis.stock.indices.kAnalysis;
import oquantour.util.tools.IndicesAnalysis;
import oquantour.po.util.StockInfo;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;
import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * Created by keenan on 13/05/2017.
 */
public class StockServiceTest extends BaseTest {
    @Autowired
    private StockService stockService;

    @Test
    public void testSearchStock() {

        try {
            StockInfo stockInfo = stockService.getStockInfo("000037");

            System.out.println(stockInfo.getStockInfo().get(stockInfo.getStockInfo().size() - 1));
            System.out.println(stockInfo.getAdvice());
            System.out.println(stockInfo.getEstimatedAdjClose());
            System.out.println(stockInfo.getEstimatedOpen());
            System.out.println(stockInfo.getEstimatedClose());
            System.out.println(stockInfo.getEstimatedLow());
            System.out.println(stockInfo.getEstimatedHigh());

            Map<IndicesAnalysis, Map<Date, String>> mapMap = stockInfo.getSellPoints();
            Map<Date, String> dmi = mapMap.get(IndicesAnalysis.KDJ);
            for (Map.Entry<Date, String> entry : dmi.entrySet()) {
                System.out.println(entry.getKey() + " " + entry.getValue());
            }

            StockPO stockPO = stockInfo.getStockInfo().get(15);

            Date date = stockPO.getDateValue();
            Calendar calendar = Calendar.getInstance();

            calendar.setTime(date);
//            System.out.println(calendar.get(Calendar.MONTH));
//
//            System.out.println(stockPO.getDateValue());

        } catch (StockDataNotExistException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testPrediction() {

    }

    @Test
    public void testRegression() {
        final WeightedObservedPoints obs = new WeightedObservedPoints();
        obs.add(-2, 4);
        obs.add(-1, 1);
        obs.add(-0.5, 0.25);
        obs.add(0, 0);
        obs.add(1, 1);
        obs.add(2, 4);
        obs.add(3, 9);

        // Instantiate a third-degree polynomial fitter.
        final PolynomialCurveFitter fitter = PolynomialCurveFitter.create(2);

        // Retrieve fitted parameters (coefficients of the polynomial function).
        final double[] coeff = fitter.fit(obs.toList());
        for (double c : coeff) {
            System.out.println(c);
        }
    }

    @Test
    public void testRecommendStock() {
        String code = "000002";

        List<StockRealTimePO> rs = stockService.getRecommendedStock(code);

        System.out.println(rs.get(0).getChangepercent());
    }

    @Test
    public void testKAnalysis() {
        try {
            List<String> allNamesAndCodes = stockService.getAllStockCodeAndName();

            List<String> allCodes = new ArrayList<>();

            for (String s : allNamesAndCodes) {
                String[] strings = s.split(";");
                allCodes.add(strings[0]);
            }

            for (int i = 20; i < allCodes.size(); i++) {
                String s = allCodes.get(i);
                StockInfo stockInfo = stockService.getStockInfo(s);

                kAnalysis kAnalysis = new kAnalysis();
                long start = System.currentTimeMillis();
                String result = kAnalysis.kCombineAnalysis(stockInfo.getStockInfo());

                long end = System.currentTimeMillis();

                System.out.println(s);
                System.out.println(result);
            }
        } catch (StockDataNotExistException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMonthly() {
        try {
            StockInfo stockInfo = stockService.getStockInfo("000004");

            List<StockPO> result = stockInfo.getMonthlyInfo();


            for (StockPO stockPO : result) {
                System.out.println(stockPO.getStockId());
                System.out.println(stockPO.getDateValue());
                System.out.println(stockPO.getHighPrice());
                System.out.println(stockPO.getLowPrice());
                System.out.println(stockPO.getOpenPrice());
                System.out.println(stockPO.getClosePrice());
                System.out.println(stockPO.getMa5());
                System.out.println(stockPO.getMa10());
                System.out.println(stockPO.getMa20());
                System.out.println(stockPO.getMa30());
                System.out.println(stockPO.getMa60());
                System.out.println(stockPO.getMa120());
                System.out.println(stockPO.getMa240());
                System.out.println("=======");
            }
        } catch (StockDataNotExistException e) {
            e.printStackTrace();
        }
    }

//    @Test
//    public void testPlate() {
//        String plateName = "主板";
//
//        List<String> strings = .getStockByPlate(plateName);
//        System.out.println(strings == null);
//
//        for (String s : strings) {
//            System.out.println(s);
//        }
//    }

    @Test
    public void testMultipleRegression() {
        OLSMultipleLinearRegression regression = new OLSMultipleLinearRegression();
        double[] y = new double[]{11.0, 12.0, 13.0, 14.0, 15.0, 16.0};
        double[][] x = new double[6][];
        x[0] = new double[]{0, 0, 0, 0, 0};
        x[1] = new double[]{2.0, 0, 0, 0, 0};
        x[2] = new double[]{0, 3.0, 0, 0, 0};
        x[3] = new double[]{0, 0, 4.0, 0, 0};
        x[4] = new double[]{0, 0, 0, 5.0, 0};
        x[5] = new double[]{0, 0, 0, 0, 6.0};

        regression.newSampleData(y, x);
        double beta[] = regression.estimateRegressionParameters();
        double[] residuals = regression.estimateResiduals();//残余方差
        double[][] parametersVariance = regression.estimateRegressionParametersVariance();
        double regressandVariance = regression.estimateRegressandVariance();
        double rSquared = regression.calculateRSquared();//R回归方差
        double sigma = regression.estimateRegressionStandardError();//标准差
        System.out.println(regression.estimateErrorVariance());
        regression.estimateRegressandVariance();


        for (int i = 0; i < beta.length; i++) {
            System.out.print(beta[i] + "    ");
        }
        System.out.println();
        for (int i = 0; i < residuals.length; i++) {
            System.out.print(residuals[i] + "     ");
        }
        System.out.println();
        System.out.println(parametersVariance);
        System.out.println(regressandVariance);
        System.out.println(rSquared);
        System.out.println(sigma);

        for (int i = 0; i < 6; i++) {
            System.out.println();
        }
        ;
    }

//    @Test
//    public void testInnerValue() {
//        stockService.getInnerValue("000001");
//    }

    @Test
    public void testScore() {
        System.out.println("000008: " + stockService.getScore("000008"));
        System.out.println("=================================================");
        System.out.println("000009: " + stockService.getScore("000009"));
        System.out.println("=================================================");
        System.out.println("000010: " + stockService.getScore("000010"));
        System.out.println("=================================================");
        System.out.println("000011: " + stockService.getScore("000011"));
        System.out.println("=================================================");
        System.out.println("000012: " + stockService.getScore("000012"));
        System.out.println("=================================================");
        System.out.println("000014: " + stockService.getScore("000014"));
        System.out.println("=================================================");
        System.out.println("000016: " + stockService.getScore("000016"));
        System.out.println("=================================================");
        System.out.println("000017: " + stockService.getScore("000017"));
        System.out.println("=================================================");
        System.out.println("000018: " + stockService.getScore("000018"));
        System.out.println("=================================================");
        System.out.println("000019: " + stockService.getScore("000019"));
        System.out.println("=================================================");
        System.out.println("000020: " + stockService.getScore("000020"));
        System.out.println("=================================================");
        System.out.println("000021: " + stockService.getScore("000021"));
        System.out.println("=================================================");
        System.out.println("000022: " + stockService.getScore("000022"));
        System.out.println("=================================================");
        System.out.println("000023: " + stockService.getScore("000023"));
        System.out.println("=================================================");
        System.out.println("000024: " + stockService.getScore("000024"));
        System.out.println("=================================================");
        System.out.println("000333: " + stockService.getScore("000333"));
        System.out.println("=================================================");
    }
}
