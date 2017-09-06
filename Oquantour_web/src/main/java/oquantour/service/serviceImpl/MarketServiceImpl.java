package oquantour.service.serviceImpl;

import oquantour.data.dao.BasicDao;
import oquantour.po.NewsPO;
import oquantour.po.TopListPO;
import oquantour.po.util.MarketTemperature;
import oquantour.service.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by keenan on 15/05/2017.
 */
@Service
@Transactional
public class MarketServiceImpl implements MarketService {
    @Autowired
    private BasicDao basicDao;

    /**
     * 获得市场温度计
     *
     * @return 市场温度
     */
    @Override
    public MarketTemperature getMarketTemperature() {
        return basicDao.getMarketTemperature();
    }

    /**
     * 获得新闻信息
     *
     * @return
     */
    @Override
    public List<NewsPO> getNewsInfo() {
        return basicDao.getNewsInfo();
    }

    /**
     * 获得龙虎榜数据
     *
     * @return
     */
    @Override
    public List<TopListPO> getTopListInfo() {
        return basicDao.getTopListInfo();
    }
}
