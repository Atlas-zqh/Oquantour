package oquantour.action;

import com.opensymphony.xwork2.ActionSupport;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import oquantour.po.StockPO;
import oquantour.po.StockRealTimePO;
import oquantour.service.OptionalStockService;
import oquantour.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Pxr on 2017/5/23.
 */
@Controller
public class OptionalStockAction extends ActionSupport {
    @Autowired
    private OptionalStockService optionalStockService;
    @Autowired
    private StockService stockService;

    private String account;

    private String stockCode;

    private String result;

    public String addOptionalStock() {
        String addOptionalStockResult;

        optionalStockService.addOptionalStock(account, stockCode);

        addOptionalStockResult = "addSuccess";

        Map<String, Object> map = new HashMap<>();
        map.put("addOptionalStockInfo", addOptionalStockResult);

        JSONObject jsonObject = JSONObject.fromObject(map);
        result = jsonObject.toString();

//        addOptionalStockResult = "addSuccess";

//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("addOptionalStockResult", addOptionalStockResult);

//        result = jsonObject.toString();

        return SUCCESS;
    }

    public String deleteOptionalStock() {
        String deleteOptionalStockResult;

        optionalStockService.deleteOptionalStock(account, stockCode);

        deleteOptionalStockResult = "deleteSuccess";

        Map<String, Object> map = new HashMap<>();
        map.put("deleteOptionalStockInfo", deleteOptionalStockResult);

        JSONObject jsonObject = JSONObject.fromObject(map);
        result = jsonObject.toString();

        return SUCCESS;
    }

    public String getAllOptionalStock() {

        System.out.println("ST: "+account);
        List<String> stockList = optionalStockService.getAllOptionalStock(account);
        String[] s = stockList.toArray(new String[1]);
        System.out.println("stockID的size" + s.length);

        Map<String, StockRealTimePO> map = stockService.getRealTimeStockInfo(s);
        System.out.println("map的size"+map.size());

        JSONArray jsonArray = new JSONArray();

        for (Map.Entry<String, StockRealTimePO> entry : map.entrySet()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("stockID", entry.getKey());
            System.out.println("自选股:" + entry.getKey());
            jsonObject.put("stockName", entry.getValue().getStockName());
            jsonObject.put("stockTrade", entry.getValue().getTrade());
            jsonObject.put("stockChange", entry.getValue().getChangepercent());
            jsonArray.add(jsonObject);
        }

        result = jsonArray.toString();
        return SUCCESS;
    }

    public void setOptionalStockService(OptionalStockService optionalStockService) {
        this.optionalStockService = optionalStockService;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setStockService(StockService stockService) {
        this.stockService = stockService;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }
}
