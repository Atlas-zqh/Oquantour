package oquantour.action;

import com.opensymphony.xwork2.ActionSupport;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import oquantour.exception.BackTestErrorException;
import oquantour.po.StockPO;
import oquantour.po.util.*;
import oquantour.service.BackTestService;
import oquantour.service.StockService;
import oquantour.util.tools.BasicIndices;
import oquantour.util.tools.PriceIndices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static oquantour.po.util.StrategyType.*;

/**
 * Created by Pxr on 2017/5/16.
 */
@Controller
public class BackTestAction extends ActionSupport {
    // 回测开始日期
    private String startDate;
    // 回测结束日期
    private String endDate;
    // 要回测的多只股票代码
    private String stocks;
    // 形成期
    private int formativePeriod;
    // 持有期
    private int holdingPeriod;
    // 几日均线
    private int ma_length;
    // 策略类型
    private String strategyType;
    // 最大持仓股票数目
    private int maxholdingStocks;
    // 过滤ST股票
    private boolean filter_ST;
    // 过滤无数据股票
    private boolean filter_NoData;
    // 过滤停牌股票
    private boolean filter_Suspension;
    @Autowired
    private StockService stockService;
    //回测种类flag，flag=0表示为经典策略，flag=1表示为DIY策略
    private int flag;
    //表示是否是区间选股，若是区间选股则值为1，若是权重选股值为0
    private int isRangeActive;
    // 是否忽略100只约束
    private boolean ignore_100;
    @Autowired
    private BackTestService backTestService;

    private String info;

    private String result;

    private String indexName;

    private String indexVal;

    public void setStockService(StockService stockService) {
        this.stockService = stockService;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getResult() {
        return result;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setStocks(String stocks) {
        this.stocks = stocks;
    }

    public void setFormativePeriod(int formativePeriod) {
        this.formativePeriod = formativePeriod;
    }

    public void setHoldingPeriod(int holdingPeriod) {
        this.holdingPeriod = holdingPeriod;
    }

    public void setMa_length(int ma_length) {
        this.ma_length = ma_length;
    }

    public void setStrategyType(String strategyType) {
        this.strategyType = strategyType;
    }

    public void setMaxholdingStocks(int maxholdingStocks) {
        this.maxholdingStocks = maxholdingStocks;
    }

    public void setFilter_ST(boolean filter_ST) {
        this.filter_ST = filter_ST;
    }

    public void setFilter_NoData(boolean filter_NoData) {
        this.filter_NoData = filter_NoData;
    }

    public void setFilter_Suspension(boolean filter_Suspension) {
        this.filter_Suspension = filter_Suspension;
    }

    public void setIgnore_100(boolean ignore_100) {
        this.ignore_100 = ignore_100;
    }

    public void setBackTestService(BackTestService backTestService) {
        this.backTestService = backTestService;
    }

    public void setIndexVal(String indexVal) {
        this.indexVal = indexVal;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public void setIsRangeActive(int isRangeActive) {
        this.isRangeActive = isRangeActive;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setResult(String result) {
        this.result = result;
    }


    public String backTest() {
        JSONArray jsonArray = new JSONArray();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            BackTestInfo backTestInfo = generateBackTestInfo();

            BackTestResult backTestResult = backTestService.getBackTestResult(backTestInfo);
            info = "回测成功";

            JSONObject resultJson = new JSONObject();
            resultJson.put("info", info);
            jsonArray.add(resultJson);

            for (BackTestingSingleChartInfo backTestingSingleChartInfo : backTestResult.getBackTestingSingleChartInfos()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("date", simpleDateFormat.format(backTestingSingleChartInfo.getDate()));
                jsonObject.put("backTestValue", String.format("%.3f", backTestingSingleChartInfo.getBackTestValue() * 100));
                jsonObject.put("stdValue", String.format("%.3f", backTestingSingleChartInfo.getStdValue() * 100));
                jsonArray.add(jsonObject);
            }

            JSONObject statisticJson = new JSONObject();
            statisticJson.put("backTestStatistics", backTestResult.getBackTestStatistics());
            jsonArray.add(statisticJson);

            JSONObject returnRateJson = new JSONObject();
            returnRateJson.put("returnRateDistribution", backTestResult.getReturnRateDistribution());
            jsonArray.add(returnRateJson);

            List<BackTestWinner> dailyWinner = backTestResult.getDailyWinners();

            for (BackTestWinner backTestWinner : dailyWinner) {
                Map<StockPO, Integer> shares = backTestWinner.getShares();
                for (Map.Entry<StockPO, Integer> entry : shares.entrySet()) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("winnerDate", simpleDateFormat.format(backTestWinner.getDate()));
//                    jsonObject.put("stockName", stockService.getStockName(entry.getKey().getStockId()));
                    jsonObject.put("stockCode", entry.getKey().getStockId());
                    jsonObject.put("stockClose", String.format("%.2f", entry.getKey().getClosePrice()));
                    jsonObject.put("stockOpen", String.format("%.2f", entry.getKey().getOpenPrice()));
                    jsonObject.put("stockHigh", String.format("%.2f", entry.getKey().getHighPrice()));
                    jsonObject.put("stockLow", String.format("%.2f", entry.getKey().getLowPrice()));
                    jsonObject.put("stockAdjClose", String.format("%.2f", entry.getKey().getAdjClose()));
//                    jsonObject.put("stockChg", String.format("%.2f", entry.getKey().getChg()*100));
                    jsonObject.put("share", entry.getValue());
                    jsonArray.add(jsonObject);
                }
//                jsonObject.put("shares", backTestWinner.getShares());
            }
            List<BackTestBestInfo> backTestBestInfos = backTestResult.getBestHolding();

            double[] doubles = backTestResult.getIndices();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("抗风险能力", doubles[0]);
            jsonObject.put("稳定性", doubles[1]);
            jsonObject.put("盈利能力", doubles[2]);
            jsonObject.put("持股分散度", doubles[3]);
            jsonObject.put("评分", backTestResult.getScore());
            jsonObject.put("最佳持有期", backTestBestInfos);
            List<BackTestBestInfo> list = backTestResult.getBestHoldingNum();

            if (!list.isEmpty()) {
                jsonObject.put("最佳持仓数", list);
            }
            jsonArray.add(jsonObject);


            result = jsonArray.toString();

        } catch (BackTestErrorException e) {
            info = e.getMessage();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("info", info);
            jsonArray.add(jsonObject);
            result = jsonArray.toString();
        }
        return SUCCESS;
    }

    private BackTestInfo generateBackTestInfo() {

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

            java.sql.Date startDate_sql = new java.sql.Date(simpleDateFormat.parse(startDate).getTime());
            java.sql.Date endDate_sql = new java.sql.Date(simpleDateFormat.parse(endDate).getTime());
            String[] s = stocks.split(";");
            List<String> stocksList = new ArrayList<>();
            for (int i = 0; i < s.length; i++) {
                stocksList.add(s[i]);
            }

            if (flag == 0) {
                StrategyType strategy = null;
                switch (strategyType) {
                    case "动量策略":
                        strategy = Momentum;
                        break;
                    case "均值回归":
                        strategy = MeanReversion;
                        break;
                    case "小市值轮动策略":
                        strategy = SmallMarketValueWheeled;
                        break;
                    default:
                        break;
                }
                return new BackTestInfo(startDate_sql, endDate_sql, stocksList, formativePeriod, holdingPeriod, ma_length, strategy,
                        maxholdingStocks, filter_ST, filter_NoData, filter_Suspension, ignore_100);
            } else if (flag == 1) {
                StrategyType strategyType = DIY;

                if (isRangeActive == 1) {
                    //是区间选股
                    Map<PriceIndices, double[]> map = new HashMap<>();
                    String[] indexName_array = indexName.split(";");
                    double[] indexVal_array = Arrays.stream(indexVal.split(";")).mapToDouble(Double::parseDouble).toArray();

                    for (int i = 0; i < indexName_array.length; i++) {
                        double[] value = new double[2];
                        value[0] = indexVal_array[i];
                        value[1] = indexVal_array[i + 1];
                        map.put(PriceIndices.getIndex(indexName_array[i]), value);
                    }
                    return new BackTestInfo(startDate_sql, endDate_sql, stocksList, holdingPeriod, strategyType, maxholdingStocks, filter_ST, filter_NoData, filter_Suspension, false, map, Collections.EMPTY_MAP);
                } else {
                    Map<PriceIndices, Double> map = new HashMap<>();
                    String[] indexName_array = indexName.split(";");
                    double[] indexVal_array = Arrays.stream(indexVal.split(";")).mapToDouble(Double::parseDouble).toArray();
                    for (int i = 0; i < indexName_array.length; i++) {
                        map.put(PriceIndices.getIndex(indexName_array[i]), indexVal_array[i]);
                    }
                    return new BackTestInfo(startDate_sql, endDate_sql, stocksList, holdingPeriod, strategyType, maxholdingStocks, filter_ST, filter_NoData, filter_Suspension, false, Collections.EMPTY_MAP, map);
                }
            }
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        return null;
    }


}

