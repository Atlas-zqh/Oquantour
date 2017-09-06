package oquantour.action;

import com.opensymphony.xwork2.ActionSupport;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import oquantour.exception.WrongCombinationException;
import oquantour.po.StockCombination;
import oquantour.service.PlateService;
import oquantour.service.PortfolioService;
import oquantour.po.util.ChartInfo;
import oquantour.po.util.PortfolioInfo;
import oquantour.util.tools.CalendarUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * Created by st on 2017/6/6.
 */
@Controller
public class userCombineAction extends ActionSupport {

    private String result;

    private String info;

    @Autowired
    private PortfolioService portfolioService;
    @Autowired
    private PlateService plateService;

    public void setPlateService(PlateService plateService) {
        this.plateService = plateService;
    }

    private String username;

    private String portfolioName;

    private String stocks;

    private String positions;

    public String getResult() {
        return result;
    }

    public void setProfolioService(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPortfolioName(String portfolioName) {
        this.portfolioName = portfolioName;
    }

    public void setStocks(String stocks) {
        this.stocks = stocks;
    }

    public void setPositions(String positions) {
        this.positions = positions;
    }

    public String addPortfolio() {
        JSONArray jsonArray = new JSONArray();
        try {
            List<String> stocks_list = Arrays.asList(stocks.split(";"));

//            List<Double> positions_list = new ArrayList<>(positions.split(";"));
            List<Double> doubles = Arrays.asList(Arrays.stream(positions.split(";")).map(Double::valueOf).toArray(Double[]::new));

            portfolioService.addPortfolio(username, portfolioName, stocks_list, doubles);


            info = "添加组合成功";
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("info", info);
            jsonArray.add(jsonObject);
            result = jsonArray.toString();

        } catch (WrongCombinationException e) {
            info = e.getMessage();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("info", info);
            jsonArray.add(jsonObject);
            result = jsonArray.toString();
        }

        return SUCCESS;
    }

    public String getAllPortfolios() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<Map.Entry<String, Timestamp>> allPortfolios = portfolioService.getAllPortfolios(username);

        JSONArray jsonArray = new JSONArray();
        for (Map.Entry<String, Timestamp> entry : allPortfolios) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("combinationName", entry.getKey());
            jsonObject.put("combinationTime", simpleDateFormat.format(entry.getValue()));
            System.out.println("组合：" + entry.getKey() + "  " + simpleDateFormat.format(entry.getValue()));
            jsonArray.add(jsonObject);
        }

        result = jsonArray.toString();
        return SUCCESS;
    }

    public String getPortfolio() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        PortfolioInfo portfolioInfo = portfolioService.getPortfolio(username, portfolioName);

        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("totalReturnRate", portfolioInfo.getTotalReturnRate());
        jsonObject.put("newNet", portfolioInfo.getNewNet());

        jsonArray.add(jsonObject);

        List<ChartInfo> industryDistributionChart = portfolioInfo.getIndustryDistribution();
        for (ChartInfo c : industryDistributionChart) {
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("name", c.getStrXAxis());
            jsonObject1.put("value", c.getyAxis() * 100);
            jsonArray.add(jsonObject1);
        }

        List<ChartInfo> returnRateChart = portfolioInfo.getReturnRates();
        for (ChartInfo c : returnRateChart) {
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("date", simpleDateFormat.format(c.getDateXAxis()));
            jsonObject1.put("y", c.getyAxis());
            jsonArray.add(jsonObject1);
        }

        List<StockCombination> stockCombinations = portfolioInfo.getPortfolio();
        Map<Timestamp, List<Double>> map = portfolioInfo.getPositionInfo();
        for (StockCombination stockCombination : stockCombinations) {
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("combinationName", stockCombination.getCombinationName());
            jsonObject1.put("positions", stockCombination.getPositions());
//            jsonObject1.put("prices", stockCombination.getPrices());
            jsonObject1.put("timeStamp", simpleDateFormat2.format(stockCombination.getTime()));
            jsonObject1.put("stocks", stockCombination.getStocks());

            List<Double> list = map.get(stockCombination.getTime());
            if (list != null)
                jsonObject1.put("positions2", list);
            jsonArray.add(jsonObject1);
        }

        List<ChartInfo> maxProfit = portfolioInfo.getMaxProfitStocks();
        Map<String, Double> avgPosition = portfolioInfo.getAvgPosition();
        Map<String, Double> tradeCnt = portfolioInfo.getTradeCnt();
        for (ChartInfo chartInfo : maxProfit) {
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("maxPortName", chartInfo.getStrXAxis());
            jsonObject1.put("avg", avgPosition.get(chartInfo.getStrXAxis()));
            jsonObject1.put("trade", tradeCnt.get(chartInfo.getStrXAxis()));
            jsonObject1.put("y", chartInfo.getyAxis());
            jsonArray.add(jsonObject1);
        }

        List<ChartInfo> minProfit = portfolioInfo.getMinProfitStocks();
        for (ChartInfo chartInfo : minProfit) {
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("minPortName", chartInfo.getStrXAxis());
            jsonObject1.put("avg", avgPosition.get(chartInfo.getStrXAxis()));
            jsonObject1.put("trade", tradeCnt.get(chartInfo.getStrXAxis()));
            jsonObject1.put("y", chartInfo.getyAxis());
            jsonArray.add(jsonObject1);
        }

        java.sql.Date date = new java.sql.Date(portfolioInfo.getCreateTime().getTime());

        Map<String, List<ChartInfo>> plate = plateService.getPlateReturnRates(date, CalendarUtil.getToday(),
                "上证指数", "中小板指", "创业板指", "沪深300", "深证成指", "深证Ａ指");


        addToJson(plate.get("深证Ａ指"), jsonArray, "SZADate", "SZA");
        addToJson(plate.get("中小板指"), jsonArray, "ZXDate", "ZX");
        addToJson(plate.get("创业板指"), jsonArray, "CYDate", "CY");
        addToJson(plate.get("沪深300"), jsonArray, "HSDate", "HS");
        addToJson(plate.get("深证成指"), jsonArray, "SZCDate", "SZC");
        addToJson(plate.get("上证指数"), jsonArray, "SZDate", "SZ");

//        JSONObject jsonObject1 = new JSONObject();
//        jsonObject1.put("currentStock", portfolioInfo.getCurrentCombination().getStocks());
//        jsonObject1.put("currentPosition", portfolioInfo.getCurrentCombination().getPositions());
//        jsonArray.add(jsonObject1);
        result = jsonArray.toString();
        return SUCCESS;

    }

    public String getCurrentStocks() {
        StockCombination stockCombination = portfolioService.getLatestPortfolio(username, portfolioName);
        List<String> stock = stockCombination.getStocks();
        List<Double> positions = stockCombination.getPositions();
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < stock.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("stock", stock.get(i));
            jsonObject.put("position", positions.get(i));
            jsonArray.add(jsonObject);
        }

        result = jsonArray.toString();
        return SUCCESS;
    }

    public String deletePortfolio() {
        portfolioService.deletePortfolio(username, portfolioName);
        return SUCCESS;
    }

    private void addToJson(List<ChartInfo> list, JSONArray jsonArray, String s1, String s2) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (ChartInfo chartInfo : list) {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put(s1, simpleDateFormat.format(chartInfo.getDateXAxis()));
            jsonObject.put(s2, chartInfo.getyAxis());
            jsonArray.add(jsonObject);
        }
    }


}
