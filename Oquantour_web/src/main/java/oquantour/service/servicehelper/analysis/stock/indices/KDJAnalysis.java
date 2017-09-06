package oquantour.service.servicehelper.analysis.stock.indices;

import oquantour.po.StockPO;

import java.sql.Date;
import java.util.*;

/**
 * Created by keenan on 03/06/2017.
 */
public class KDJAnalysis {
    private static final String goldenCross = "黄金交叉;买进", deathCross = "死亡交叉;卖出";

    public static List<Map<Date, String>> getSignal(List<StockPO> data) {
        Map<Date, String> map_buy = new HashMap<>();
        Map<Date, String> map_sell = new HashMap<>();
        for (int i = 1; i < data.size(); i++) {
            StockPO pre = data.get(i - 1);
            StockPO cur = data.get(i);
            if (pre.getdValue() < 35 && pre.getkValue() < 35 && cur.getdValue() < 35 && cur.getkValue() < 35 && pre.getkValue() < pre.getdValue() && cur.getkValue() >= cur.getdValue()) {
                map_buy.put(cur.getDateValue(), goldenCross);
            } else if (pre.getkValue() > 65 && pre.getdValue() > 65 && cur.getdValue() > 65 && cur.getkValue() > 65 && pre.getkValue() > pre.getdValue() && cur.getkValue() <= cur.getdValue()) {
                map_sell.put(cur.getDateValue(), deathCross);
            } else {
                continue;
            }
        }
        List<Map<Date, String>> maps = new ArrayList<>();
        maps.add(map_buy);
        maps.add(map_sell);
        return maps;
    }
}
