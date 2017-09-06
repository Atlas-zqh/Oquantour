package oquantour.action;

import com.opensymphony.xwork2.ActionSupport;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import oquantour.po.StockRealTimePO;
import oquantour.po.util.ChartInfo;
import oquantour.service.IndustryService;
import oquantour.po.util.Edge;
import oquantour.po.util.RelationGraph;
import oquantour.util.tools.CalendarUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Created by Pxr on 2017/6/6.
 */
@Controller
public class IndustryAction extends ActionSupport {
    @Autowired
    private IndustryService industryService;

    private String result;

    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public String getStockInIndustry() {
        JSONArray jsonArray = new JSONArray();
        Map<String, StockRealTimePO> map = industryService.getIndustryStocks(name);

        JSONObject jsonObject = JSONObject.fromObject(map);

        jsonArray.add(jsonObject);

        List<String> list = industryService.getAllIndustriesName();
        Date today = CalendarUtil.getToday();
        Date oneYear = CalendarUtil.getDateOneYearBefore(today);
        Map<String, List<ChartInfo>> map2 = industryService.getAllIndustryReturnRate(
                list, oneYear, today);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        List<ChartInfo> chartInfos = map2.get(name);
        for (ChartInfo chartInfo : chartInfos) {
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("date", simpleDateFormat.format(chartInfo.getDateXAxis()));
            jsonObject1.put("value", chartInfo.getyAxis());
            jsonArray.add(jsonObject1);
        }
        result = jsonArray.toString();
        return SUCCESS;
    }

    public String getIndustryTree() {
        RelationGraph relationGraph = industryService.getIndustryTree();
        PriorityQueue<Edge> priorityQueue = relationGraph.getEdges();
        JSONArray jsonArray = new JSONArray();
        for (String s : relationGraph.getIndustries()) {
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("id", s);
            jsonArray.add(jsonObject1);
        }
        while (priorityQueue.peek() != null) {
            Edge edge = priorityQueue.poll();
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("edge", edge);
            jsonArray.add(jsonObject);
        }

        result = jsonArray.toString();


        return SUCCESS;
    }

    public String getAllIndustryName() {
        List<String> list = industryService.getAllIndustriesName();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list", list);

        result = jsonObject.toString();
        return SUCCESS;
    }


    public void setIndustryService(IndustryService industryService) {
        this.industryService = industryService;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }
}
