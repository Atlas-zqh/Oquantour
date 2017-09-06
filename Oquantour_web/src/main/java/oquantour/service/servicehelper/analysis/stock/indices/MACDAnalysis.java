package oquantour.service.servicehelper.analysis.stock.indices;

import oquantour.po.StockPO;

import java.sql.Date;
import java.util.*;

/**
 * Created by keenan on 07/06/2017.
 */
public class MACDAnalysis {
    private static final String goldenCross = "黄金交叉;买进", deathCross = "死亡交叉;卖出";

    public static List<Map<Date, String>> getSignal(List<StockPO> data) {
        Map<Date, String> map_buy = new HashMap<>();
        Map<Date, String> map_sell = new HashMap<>();
        for (int i = 1; i < data.size(); i++) {
            StockPO pre = data.get(i - 1);
            StockPO cur = data.get(i);
            // DIF上穿DEA 金叉
            if (pre.getDif() < pre.getDea() && cur.getDif() >= cur.getDea()) {
                map_buy.put(cur.getDateValue(), goldenCross);
            } else if (pre.getDif() > pre.getDea() && cur.getDif() <= cur.getDea()) {
                // 白线下穿黄线 死叉
                map_sell.put(cur.getDateValue(), deathCross);
            }
        }
        List<Map<Date, String>> maps = new ArrayList<>();
        maps.add(map_buy);
        maps.add(map_sell);
        return maps;
    }
}
