package oquantour.util.tools;

import oquantour.po.PlateinfoPO;
import oquantour.po.StockPO;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分类器
 * Created by keenan on 09/06/2017.
 */
public class Classifier {
    public static Map<Date, StockPO> classifyStockByDate(List<StockPO> stockPOS) {
        Map<Date, StockPO> res = new HashMap<>();
        stockPOS.stream().forEach(stockPO -> res.put(stockPO.getDateValue(), stockPO));
        return res;
    }

    public static Map<Date, PlateinfoPO> classifyPlateByDate(List<PlateinfoPO> plateinfoPOS) {
        Map<Date, PlateinfoPO> res = new HashMap<>();
        plateinfoPOS.stream().forEach(plateinfoPO -> res.put(plateinfoPO.getDateValue(), plateinfoPO));
        return res;
    }
}
