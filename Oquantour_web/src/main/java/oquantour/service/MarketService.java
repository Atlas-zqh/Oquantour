package oquantour.service;

import oquantour.po.NewsPO;
import oquantour.po.TopListPO;
import oquantour.po.util.MarketTemperature;

import java.util.List;

/**
 * Created by keenan on 15/05/2017.
 */
public interface MarketService {
    /**
     * 获得市场温度
     *
     * @return 市场温度
     */
    MarketTemperature getMarketTemperature();

    /**
     * 获得新闻信息
     *
     * @return
     */
    List<NewsPO> getNewsInfo();

    /**
     * 获得龙虎榜数据
     *
     * @return
     */
    List<TopListPO> getTopListInfo();
}
