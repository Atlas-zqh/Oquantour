package oquantour.service.servicehelper.analysis.stock.indices;

import oquantour.po.StockPO;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by keenan on 07/06/2017.
 */
public class BOLLAnalysis {
    private static final String shortBuy = "短线买入", longBuy = "中长线买入", shortSell = "短线卖出", longSell = "中线卖出";

    public static List<Map<Date, String>> getSignal(List<StockPO> data) {
        Map<Date, String> map_buy = new HashMap<>();
        Map<Date, String> map_sell = new HashMap<>();
        for (int i = 1; i < data.size(); i++) {
            StockPO pre = data.get(i - 1);
            StockPO cur = data.get(i);
            if (pre.getMb() == -1 || pre.getUp() == -1 || pre.getDn() == -1) {
                continue;
            }
            //当美国线从布林线的中轨线以下、向上突破布林线中轨线时，预示着股价的强势特征开始出现，股价将上涨，投资者应以中长线买入股票为主。
            if (pre.getHighPrice() < pre.getMb() && cur.getHighPrice() >= cur.getMb()) {
                map_buy.put(cur.getDateValue(), longBuy);
            } else if (pre.getLowPrice() > pre.getMb() && pre.getHighPrice() < pre.getUp() && cur.getHighPrice() >= cur.getUp()) {//当美国线从布林线的中轨线以上、向上突破布林线上轨时，预示着股价的强势特征已经确立，股价将可能短线大涨，投资者应以持股待涨或短线买入为主。
                map_buy.put(cur.getDateValue(), shortBuy);
            } else if (pre.getHighPrice() < pre.getDn() && cur.getHighPrice() >= cur.getDn()) {//当美国线从布林线下轨下方、向上突破布林线下轨时，预示着股价的短期行情可能回暖，投资者可以及时适量买进股票，作短线反弹行情。
                map_buy.put(cur.getDateValue(), shortBuy);
            } else if (pre.getHighPrice() > pre.getUp() && cur.getHighPrice() <= cur.getUp()) {//当美国线在布林线上方向上运动了一段时间后，如果美国线的运动方向开始掉头向下，投资者应格外小心，一旦美国线掉头向下并突破布林线上轨时，预示着股价短期的强势行情可能结束，股价短期内将大跌，投资者应及时短线卖出股票、离场观望。
                map_sell.put(cur.getDateValue(), shortSell);
            } else if (pre.getLowPrice() > pre.getMb() && cur.getLowPrice() <= cur.getMb()) { //当美国线从布林线中轨上方、向下突破布林线的中轨时，预示着股价前期的强势行情已经结束，股价的中期下跌趋势已经形成，投资者应中线及时卖出股票。
                map_sell.put(cur.getDateValue(), longSell);
            }
        }
        List<Map<Date, String>> maps = new ArrayList<>();
        maps.add(map_buy);
        maps.add(map_sell);
        return maps;
    }
}
