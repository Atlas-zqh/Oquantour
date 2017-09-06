package oquantour.action;

import com.opensymphony.xwork2.ActionSupport;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import oquantour.exception.InnerValueException;
import oquantour.exception.StockDataNotExistException;
import oquantour.po.StockPO;
import oquantour.po.StockRealTimePO;
import oquantour.po.util.ChartInfo;
import oquantour.service.PlateService;
import oquantour.service.StockService;
import oquantour.util.tools.BasicIndices;
import oquantour.util.tools.IndicesAnalysis;
import oquantour.po.util.StockInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Pxr on 2017/5/12.
 */
@Controller
public class StockAction extends ActionSupport {
    @Autowired
    private StockService stockService;
    @Autowired
    private PlateService plateService;

    public void setPlateService(PlateService plateService) {
        this.plateService = plateService;
    }

    private String stockInfo;

    private String searchResult;

    private String result;

    private String plateName;

    private Map<String, double[]> indexs;

    private String indexVal;

    private String indexName;

    public String getKLine() {
        JSONArray jsonArray = new JSONArray();
        StockInfo stock = null;
        List<ChartInfo> chartInfos = new ArrayList<>();
        JSONObject json = new JSONObject();

        try {
            stock = stockService.getStockInfo(stockInfo);

            chartInfos = stockService.getInnerValue(stockInfo);

            searchResult = "搜索成功";
            json.put("searchResult", searchResult);
        } catch (StockDataNotExistException e2) {
            searchResult = "搜索的股票不存在";
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("searchResult", searchResult);
            jsonArray.add(jsonObject1);
            result = jsonArray.toString();
            return SUCCESS;
        } catch (InnerValueException e2) {
            searchResult = e2.getMessage();
            json.put("searchResult", searchResult);
        }

        Map<String, StockRealTimePO> stringStockRealTimePOMap = stockService.getRealTimeStockInfo(stockInfo);

        List<StockPO> stockPOList = stock.getStockInfo();
        List<StockPO> weeklyInfo = stock.getWeeklyInfo();
        List<StockPO> monthlyInfo = stock.getMonthlyInfo();


        json.put("stockID", stockPOList.get(0).getStockId());
        json.put("advice", stock.getAdvice());
        json.put("estimatedOpen", stock.getEstimatedOpen());
        json.put("estimatedClose", stock.getEstimatedClose());
        json.put("estimatedHigh", stock.getEstimatedHigh());
        json.put("estimatedLow", stock.getEstimatedLow());
        json.put("estimatedAdjClose", stock.getEstimatedAdjClose());
        json.put("basicInfo", stock.getBasicInfo());
        json.put("score", stockService.getScore(stockInfo));
        json.put("实时", stringStockRealTimePOMap.get(stockInfo));
//            System.out.println(stringStockRealTimePOMap.get(stockInfo).getTrade());


        jsonArray.add(json);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (StockPO stockPO : stockPOList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("日期", simpleDateFormat.format(stockPO.getDateValue()));
            jsonObject.put("开盘价", stockPO.getOpenPrice());
            jsonObject.put("复权收盘价", stockPO.getClosePrice());//这里暂时先用收盘价
            jsonObject.put("最高价", stockPO.getHighPrice());
            jsonObject.put("最低价", stockPO.getLowPrice());
            jsonObject.put("交易量", stockPO.getVolume());
            jsonObject.put("涨跌幅", String.format("%.4f", stockPO.getChg() * 100));
            jsonObject.put("对数收益率", String.format("%.3f", stockPO.getReturnRate()));
//                jsonObject.put("板块名", stockPO.getPlate());
            jsonObject.put("5日均线", String.format("%.2f", stockPO.getMa5()));
            jsonObject.put("10日均线", String.format("%.2f", stockPO.getMa10()));
            jsonObject.put("20日均线", String.format("%.2f", stockPO.getMa20()));
            jsonObject.put("30日均线", String.format("%.2f", stockPO.getMa30()));
            jsonObject.put("60日均线", String.format("%.2f", stockPO.getMa60()));
            jsonObject.put("120日均线", String.format("%.2f", stockPO.getMa120()));
            jsonObject.put("240日均线", String.format("%.2f", stockPO.getMa240()));
            jsonObject.put("mb", stockPO.getMb());
            jsonObject.put("up", stockPO.getUp());
            jsonObject.put("dn", stockPO.getDn());
            jsonObject.put("j", stockPO.getjValue());
            jsonObject.put("d", stockPO.getdValue());
            jsonObject.put("k", stockPO.getkValue());
            jsonObject.put("dif", stockPO.getDif());
            jsonObject.put("dea", stockPO.getDea());
            jsonObject.put("macd", stockPO.getDif() - stockPO.getDea());
            jsonObject.put("adx", stockPO.getAdx());
            jsonObject.put("adxr", stockPO.getAdxr());
            jsonObject.put("+di", stockPO.getDiPlus());
            jsonObject.put("-di", stockPO.getDiMinus());
            jsonArray.add(jsonObject);
        }

        JSONObject temp1 = new JSONObject();
        temp1.put("日期", -1);
        jsonArray.add(temp1);

        for (StockPO stockPO : weeklyInfo) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("日期（周）", simpleDateFormat.format(stockPO.getDateValue()));
            jsonObject.put("开盘价（周）", stockPO.getOpenPrice());
            jsonObject.put("复权收盘价（周）", stockPO.getClosePrice());//这里暂时先用收盘价
            jsonObject.put("最高价（周）", stockPO.getHighPrice());
            jsonObject.put("最低价（周）", stockPO.getLowPrice());
            jsonObject.put("交易量（周）", stockPO.getVolume());
            jsonObject.put("5日均线", String.format("%.2f", stockPO.getMa5()));
            jsonObject.put("10日均线", String.format("%.2f", stockPO.getMa10()));
            jsonObject.put("20日均线", String.format("%.2f", stockPO.getMa20()));
            jsonObject.put("30日均线", String.format("%.2f", stockPO.getMa30()));
            jsonObject.put("60日均线", String.format("%.2f", stockPO.getMa60()));
            jsonObject.put("120日均线", String.format("%.2f", stockPO.getMa120()));
            jsonObject.put("240日均线", String.format("%.2f", stockPO.getMa240()));
            jsonArray.add(jsonObject);
        }

        JSONObject temp2 = new JSONObject();
        temp2.put("日期（周）", -1);
        jsonArray.add(temp2);

        for (StockPO stockPO : monthlyInfo) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("日期（月）", simpleDateFormat.format(stockPO.getDateValue()));
            jsonObject.put("开盘价（月）", stockPO.getOpenPrice());
            jsonObject.put("复权收盘价（月）", stockPO.getClosePrice());//这里暂时先用收盘价
            jsonObject.put("最高价（月）", stockPO.getHighPrice());
            jsonObject.put("最低价（月）", stockPO.getLowPrice());
            jsonObject.put("交易量（月）", stockPO.getVolume());
            jsonObject.put("5日均线", String.format("%.2f", stockPO.getMa5()));
            jsonObject.put("10日均线", String.format("%.2f", stockPO.getMa10()));
            jsonObject.put("20日均线", String.format("%.2f", stockPO.getMa20()));
            jsonObject.put("30日均线", String.format("%.2f", stockPO.getMa30()));
            jsonObject.put("60日均线", String.format("%.2f", stockPO.getMa60()));
            jsonObject.put("120日均线", String.format("%.2f", stockPO.getMa120()));
            jsonObject.put("240日均线", String.format("%.2f", stockPO.getMa240()));
            jsonArray.add(jsonObject);
        }
        Map<Date, String> kdjAnaBuy = stock.getBuyPoints().get(IndicesAnalysis.KDJ);
        Map<Date, String> kdjAnaSell = stock.getSellPoints().get(IndicesAnalysis.KDJ);
        Map<Date, String> bollAnaBuy = stock.getBuyPoints().get(IndicesAnalysis.BOLL);
        Map<Date, String> bollAnaSell = stock.getSellPoints().get(IndicesAnalysis.BOLL);
        Map<Date, String> macdAnaBuy = stock.getBuyPoints().get(IndicesAnalysis.MACD);
        Map<Date, String> macdAnaSell = stock.getSellPoints().get(IndicesAnalysis.MACD);
        Map<Date, String> dmiAnaBuy = stock.getBuyPoints().get(IndicesAnalysis.DMI);
        Map<Date, String> dmiAnaSell = stock.getSellPoints().get(IndicesAnalysis.DMI);

        for (Map.Entry<Date, String> entry : kdjAnaBuy.entrySet()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("kdjDate", simpleDateFormat.format(entry.getKey()));
            jsonObject.put("kdjAna", entry.getValue());
            jsonArray.add(jsonObject);
        }
        for (Map.Entry<Date, String> entry : kdjAnaSell.entrySet()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("kdjDate", simpleDateFormat.format(entry.getKey()));
            jsonObject.put("kdjAna", entry.getValue());
            jsonArray.add(jsonObject);
        }

        for (Map.Entry<Date, String> entry : bollAnaBuy.entrySet()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("bollDate", simpleDateFormat.format(entry.getKey()));
            jsonObject.put("bollAna", entry.getValue());
            jsonArray.add(jsonObject);
        }
        for (Map.Entry<Date, String> entry : bollAnaSell.entrySet()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("bollDate", simpleDateFormat.format(entry.getKey()));
            jsonObject.put("bollAna", entry.getValue());
            jsonArray.add(jsonObject);
        }

        for (Map.Entry<Date, String> entry : macdAnaBuy.entrySet()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("macdDate", simpleDateFormat.format(entry.getKey()));
            jsonObject.put("macdAna", entry.getValue());
            jsonArray.add(jsonObject);
        }
        for (Map.Entry<Date, String> entry : macdAnaSell.entrySet()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("macdDate", simpleDateFormat.format(entry.getKey()));
            jsonObject.put("macdAna", entry.getValue());
            jsonArray.add(jsonObject);
        }

        for (Map.Entry<Date, String> entry : dmiAnaBuy.entrySet()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("dmiDate", simpleDateFormat.format(entry.getKey()));
            jsonObject.put("dmiAna", entry.getValue());
            jsonArray.add(jsonObject);
        }
        for (Map.Entry<Date, String> entry : dmiAnaSell.entrySet()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("dmiDate", simpleDateFormat.format(entry.getKey()));
            jsonObject.put("dmiAna", entry.getValue());
            jsonArray.add(jsonObject);
        }

        if (!chartInfos.isEmpty()) {
            for (ChartInfo chartInfo : chartInfos) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("xAxis", simpleDateFormat.format(chartInfo.getDateXAxis()));
                jsonObject.put("value", chartInfo.getyAxis());
                jsonArray.add(jsonObject);
            }
        }

        Set<Date> dates = new TreeSet<>();
        dates.addAll(macdAnaBuy.keySet());
        dates.addAll(macdAnaSell.keySet());
        dates.addAll(kdjAnaBuy.keySet());
        dates.addAll(kdjAnaSell.keySet());
        dates.addAll(bollAnaBuy.keySet());
        dates.addAll(bollAnaSell.keySet());
        dates.addAll(dmiAnaBuy.keySet());
        dates.addAll(dmiAnaSell.keySet());
        List<Date> datesList = new ArrayList<>(dates);

        List<Integer> kdjList = new ArrayList<>();
        List<Integer> bollList = new ArrayList<>();
        List<Integer> macdList = new ArrayList<>();
        List<Integer> dmiList = new ArrayList<>();
        for (Date date : datesList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("allDate", simpleDateFormat.format(date));
            jsonArray.add(jsonObject);
            addToList(kdjList, kdjAnaBuy, kdjAnaSell, date);
            addToList(bollList, bollAnaBuy, bollAnaSell, date);
            addToList(macdList, macdAnaBuy, macdAnaSell, date);
            addToList(dmiList, dmiAnaBuy, dmiAnaSell, date);
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("kdjList", kdjList);
        jsonObject.put("bollList", bollList);
        jsonObject.put("macdList", macdList);
        jsonObject.put("dmiList", dmiList);
        jsonArray.add(jsonObject);


        result = jsonArray.toString();

        return SUCCESS;
    }

    public String getAllStockNameAndCode() {
        List<String> stringList = stockService.getAllStockCodeAndName();
        Map<String, Object> map = new HashMap<>();

        map.put("stockNameAndCode", stringList);

        JSONObject jsonObject = JSONObject.fromObject(map);

        result = jsonObject.toString();

        return SUCCESS;
    }

    public String getRecommendedStock() {
        List<StockRealTimePO> stringList = stockService.getRecommendedStock(stockInfo);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("recommend", stringList);

        result = jsonObject.toString();

        return SUCCESS;
    }

    public String getStockByPlate() {
        List<String> strings = plateService.getStockByPlate(plateName);

        Map<String, Object> map = new HashMap<>();

        map.put("stockInPlate", strings);

        JSONObject jsonObject = JSONObject.fromObject(map);

        result = jsonObject.toString();
        return SUCCESS;
    }

    private void addToList(List<Integer> list, Map<Date, String> map1, Map<Date, String> map2, Date date) {
        if (map1.get(date) != null) {
            list.add(1);
        } else if (map2.get(date) != null) {
            list.add(-1);
        } else {
            list.add(0);
        }
    }

    public String selectStock() {
        Map<BasicIndices, double[]> map = new HashMap<>();
        String[] indexName_array = new String[0];
        double[] indexVal_array = new double[0];

        if (indexName.length() > 0 && indexVal.length() > 0) {
            indexName_array = indexName.split(";");
            indexVal_array = Arrays.stream(indexVal.split(";")).mapToDouble(Double::parseDouble).toArray();
        }

        int j = 0;
        for (int i = 0; i < indexName_array.length; i++) {
            double[] value = new double[2];
            value[0] = indexVal_array[j];
            j++;
            value[1] = indexVal_array[j];
            j++;
            System.out.println("st                                               " + value[0] + ";" + value[1]);
            map.put(BasicIndices.getIndex(indexName_array[i]), value);
        }

        System.out.println("map的size" + map.size());

        Map<String, String> stock = stockService.selectStock(map);

        System.out.println("stock的size" + stock.size());

        List<String> stockID = new ArrayList<>(stock.keySet());
        List<String> stockName = new ArrayList<>();
        for (String s : stockID) {
            stockName.add(stock.get(s));
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("stockID", stockID);
        jsonObject.put("stockName", stockName);

        result = jsonObject.toString();

        return SUCCESS;

    }

    public void setStockService(StockService stockService) {
        this.stockService = stockService;
    }

    public void setStockInfo(String stockInfo) {
        this.stockInfo = stockInfo;
    }

    public void setSearchResult(String searchResult) {
        this.searchResult = searchResult;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void setPlateName(String plateName) {
        this.plateName = plateName;
    }

    public void setIndexs(Map<String, double[]> index) {
        this.indexs = index;
    }

    public void setIndexVal(String indexVal) {
        this.indexVal = indexVal;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }
}
