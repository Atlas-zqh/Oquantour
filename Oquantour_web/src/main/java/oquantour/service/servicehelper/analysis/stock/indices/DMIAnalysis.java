package oquantour.service.servicehelper.analysis.stock.indices;

import oquantour.po.StockPO;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by keenan on 08/06/2017.
 */
public class DMIAnalysis {
    private static final String buy = "买入", sell = "卖出";

    public static List<Map<Date, String>> getSignal(List<StockPO> data) {
        Map<Date, String> map_buy = new HashMap<>();
        Map<Date, String> map_sell = new HashMap<>();
        for (int i = 1; i < data.size(); i++) {
            StockPO pre = data.get(i - 1);
            StockPO cur = data.get(i);
            if (cur.getAdx() < cur.getDiMinus() && cur.getAdx() < 20) {
                continue;
            } else if (cur.getAdxr() > 20 && cur.getAdxr() < 25) {
                continue;
            } else if (pre.getDiPlus() < pre.getDiMinus() && cur.getDiPlus() >= cur.getDiMinus()) {
                map_buy.put(cur.getDateValue(), buy);
            } else if (pre.getDiMinus() < pre.getDiPlus() && cur.getDiMinus() >= cur.getDiPlus()) {
                map_sell.put(cur.getDateValue(), sell);
            }
        }

        List<Map<Date, String>> maps = new ArrayList<>();
        maps.add(map_buy);
        maps.add(map_sell);
        return maps;
    }
}
