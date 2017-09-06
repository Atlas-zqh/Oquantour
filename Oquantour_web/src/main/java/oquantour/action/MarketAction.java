package oquantour.action;

import com.opensymphony.xwork2.ActionSupport;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import oquantour.po.PlateRealTimePO;
import oquantour.po.StockPO;
import oquantour.po.StockRealTimePO;
import oquantour.po.TopListPO;
import oquantour.po.util.ChartInfo;
import oquantour.service.IndustryService;
import oquantour.service.MarketService;
import oquantour.po.util.MarketTemperature;
import oquantour.service.PlateService;
import oquantour.util.tools.CalendarUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Pxr on 2017/5/23.
 */
@Controller
public class MarketAction extends ActionSupport {
    @Autowired
    private MarketService marketService;
    @Autowired
    private PlateService plateService;

    public void setPlateService(PlateService plateService) {
        this.plateService = plateService;
    }

    private String result;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");


    public String getMarketInfo() {
        JSONArray jsonArray = new JSONArray();
        //获取当日日期
        java.util.Date utilDate = new Date();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        MarketTemperature marketTemperature = marketService.getMarketTemperature();
        List<StockRealTimePO> ssUpList = marketTemperature.getSsUpList();
        List<StockRealTimePO> ssDownList = marketTemperature.getSsDownList();
        List<StockRealTimePO> szUpList = marketTemperature.getSzUpList();
        List<StockRealTimePO> szDownList = marketTemperature.getSzDownList();
        List<StockRealTimePO> ssVolumeList = marketTemperature.getSsVolumeList();
        List<StockRealTimePO> szVolumeList = marketTemperature.getSzVolumeList();

        Map<String, PlateRealTimePO> map1 = plateService.getRealTimePlateInfo();


        //获取市场温度的日期
        JSONObject json = new JSONObject();
        String date = simpleDateFormat.format(new java.util.Date(marketTemperature.getDate().getTime()));
        json.put("date", date);
        json.put("ssUpList", ssUpList);
        json.put("ssDownList", ssDownList);
        json.put("szUpList", szUpList);
        json.put("szDownList", szDownList);
        json.put("ssVolume", ssVolumeList);
        json.put("szVolume", szVolumeList);
        json.put("ssAmount", marketTemperature.getSsAmountList());
        json.put("szAmount", marketTemperature.getSzAmountList());
        json.put("热股", marketTemperature.getHotStockPOS());
        json.put("沪深300", map1.get("沪深300"));
        json.put("中小板指", map1.get("中小板指"));
        json.put("上证指数", map1.get("上证指数"));
        json.put("创业板指", map1.get("创业板指"));
        jsonArray.add(json);

        Map<String, Object> map = new HashMap<>();
        map.put("date", simpleDateFormat.format(marketTemperature.getDate()));
        map.put("down5per", marketTemperature.getDown5perStock());
        map.put("up5per", marketTemperature.getUp5perStock());
        map.put("limitUp", marketTemperature.getLimitUpStock());
        map.put("limitDown", marketTemperature.getLimitDownStock());
        map.put("openUp5per", marketTemperature.getOpenUp5perStock());
        map.put("openDown5per", marketTemperature.getOpenDown5perStock());
        map.put("totalNum", marketTemperature.getTotalNum());
        map.put("totalVolume", marketTemperature.getTotalVolume());
        map.put("temperature", marketTemperature.getTemperature());
        JSONObject jsonObject = JSONObject.fromObject(map);
        jsonArray.add(jsonObject);
        java.sql.Date today = CalendarUtil.getToday();
        java.sql.Date oneYear = CalendarUtil.getDateOneYearBefore(today);

        Map<String, List<ChartInfo>> plate = plateService.getPlateReturnRates(oneYear,
                today,
                "上证指数", "中小板指", "创业板指", "沪深300", "深证成指", "深证Ａ指");


        addToJson(plate.get("深证Ａ指"), jsonArray, "SZADate", "SZA");
        addToJson(plate.get("中小板指"), jsonArray, "ZXDate", "ZX");
        addToJson(plate.get("创业板指"), jsonArray, "CYDate", "CY");
        addToJson(plate.get("沪深300"), jsonArray, "HSDate", "HS");
        addToJson(plate.get("深证成指"), jsonArray, "SZCDate", "SZC");
        addToJson(plate.get("上证指数"), jsonArray, "SZDate", "SZ");

        result = jsonArray.toString();

        return SUCCESS;
    }

    public String getTopListInfo() {
        List<TopListPO> topListPOS = marketService.getTopListInfo();
        JSONArray jsonArray = new JSONArray();
        for (TopListPO topListPO : topListPOS) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("date", simpleDateFormat.format(topListPO.getDateValue()));
            jsonObject.put("stockID", topListPO.getStockId());
            jsonObject.put("stockName", topListPO.getStockName());
            jsonObject.put("pchange", topListPO.getPchange());
            jsonObject.put("amount", topListPO.getAmount());
            jsonObject.put("buy", topListPO.getBuy());
            jsonObject.put("bratio", topListPO.getBratio());
            jsonObject.put("sell", topListPO.getSell());
            jsonObject.put("sratio", topListPO.getSratio());
            jsonObject.put("reason", topListPO.getReason());
            jsonArray.add(jsonObject);
        }

        result = jsonArray.toString();
        return SUCCESS;
    }


    public void setMarketService(MarketService marketService) {
        this.marketService = marketService;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResult() {
        return this.result;
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
