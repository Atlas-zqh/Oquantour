package oquantour.data.daoImpl;

import oquantour.data.dao.BasicDao;
import oquantour.data.datagetter.NewsInfoGetter;
import oquantour.data.datagetter.RealTimeInfoGetter;
import oquantour.data.datagetter.TopListInfoGetter;
import oquantour.po.*;
import oquantour.po.util.MarketTemperature;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Date;
import java.util.*;

/**
 * Created by island on 2017/6/4.
 */
@Transactional(readOnly = false)
@Repository
public class BasicStockDaoImpl implements BasicDao {
    //使用Spring注入HibernateTemplate的一个实例
    private HibernateTemplate hibernateTemplate;

    public HibernateTemplate getHibernateTemplate() {
        return hibernateTemplate;
    }

    @Resource(name = "hibernateTemplate")
    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    /**
     * 根据股票ID获得股票基本信息
     *
     * @param code
     * @return
     */
    @Override
    public Map<String, StockInfoPO> getStockInfo(String... code) {
        Map<String, StockInfoPO> stocks = new HashMap<>();
        for (int i = 0; i < code.length; i++) {
            List<StockInfoPO> stockInfoPOS = (List<StockInfoPO>) this.getHibernateTemplate().find("from oquantour.po.StockInfoPO where stockId = ?", code[i]);
            if (stockInfoPOS.size() > 0)
                stocks.put(code[i], stockInfoPOS.get(0));
        }
        return stocks;
    }

    /**
     * 获得热股
     *
     * @return
     */
    @Override
    public List<StockRealTimePO> getHotStocks() {
        RealTimeInfoGetter realTimeInfoGetter = new RealTimeInfoGetter();
        Map<String, StockRealTimePO> stocks = realTimeInfoGetter.getRealTimeStock(new ArrayList<>());
        List<StockRealTimePO> changePercents = (List<StockRealTimePO>) this.getHibernateTemplate().find("from oquantour.po.StockRealTimePO order by changepercent desc");
        List<StockRealTimePO> volumes = (List<StockRealTimePO>) this.getHibernateTemplate().find("from oquantour.po.StockRealTimePO order by volume desc ");

        List<StockRealTimePO> stockRealTimePOS = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            stockRealTimePOS.add(changePercents.get(i));
        }
        return stockRealTimePOS;
    }

    /**
     * 添加股票基本数据
     *
     * @param stockBasicInfoPO
     */
    @Override
    public void addBasicInfo(StockBasicInfoPO stockBasicInfoPO) {
        this.getHibernateTemplate().saveOrUpdate(stockBasicInfoPO);
    }

    /**
     * 根据股票ID和季度获得股票基本面信息
     *
     * @param year
     * @param quarter
     * @param code
     * @return
     */
    @Override
    public Map<String, StockBasicInfoPO> getStockBasicInfo(int year, int quarter, String... code) {
        String time = year + "-" + quarter;
        Map<String, StockBasicInfoPO> result = new HashMap<>();
        for (int i = 0; i < code.length; i++) {
            List<StockBasicInfoPO> stockBasicInfoPOS = (List<StockBasicInfoPO>) this.getHibernateTemplate().find("from oquantour.po.StockBasicInfoPO where quarterOfYear = ? and stockId = ?", new Object[]{time, code[i]});
            if (stockBasicInfoPOS.size() > 0)
                result.put(code[i], stockBasicInfoPOS.get(0));
        }
        return result;
    }

    private void evictStockRealTimePO(List<StockRealTimePO> stockRealTimePOS){
        for(int i = 0; i < stockRealTimePOS.size(); i++){
            this.getHibernateTemplate().evict(stockRealTimePOS.get(i));
        }
    }

    /**
     * 获得市场温度
     *
     * @return
     */
    @Override
    public MarketTemperature getMarketTemperature() {
        MarketTemperature marketTemperature = new MarketTemperature();
        System.out.println(System.currentTimeMillis());
        List<Long> totalVolume = (List<Long>) this.getHibernateTemplate().find("select sum(volume) from oquantour.po.StockRealTimePO");
        if (totalVolume.size() > 0)
            marketTemperature.setTotalVolume(totalVolume.get(0));
        List<Long> limitUpStock = (List<Long>) this.getHibernateTemplate().find("select count(*) from oquantour.po.StockRealTimePO where (stockName like '%ST%' and changepercent >= 0.05) or (stockName not like '%ST%' and changepercent >= 0.1) ");
        if (limitUpStock.size() > 0)
            marketTemperature.setLimitUpStock(limitUpStock.get(0));
        List<Long> limitDownStock = (List<Long>) this.getHibernateTemplate().find("select count(*) from oquantour.po.StockRealTimePO where (stockName like '%ST%' and changepercent <= -0.05) or (stockName not like '%ST%' and changepercent <= -0.1) ");
        if (limitDownStock.size() > 0)
            marketTemperature.setLimitDownStock(limitDownStock.get(0));
        List<Long> up5perStock = (List<Long>) this.getHibernateTemplate().find("select count(*) from oquantour.po.StockRealTimePO where changepercent >= 0.05");
        if (up5perStock.size() > 0)
            marketTemperature.setUp5perStock(up5perStock.get(0));
        List<Long> down5perStock = (List<Long>) this.getHibernateTemplate().find("select count(*) from oquantour.po.StockRealTimePO where changepercent <= -0.05");
        if (down5perStock.size() > 0)
            marketTemperature.setDown5perStock(down5perStock.get(0));
        List<Long> openUp5perStock = (List<Long>) this.getHibernateTemplate().find("select count(*) from oquantour.po.StockRealTimePO where ((trade - settlement) / settlement) >= 0.05");
        if (openUp5perStock.size() > 0)
            marketTemperature.setOpenUp5perStock(openUp5perStock.get(0));
        List<Long> openDown5perStock = (List<Long>) this.getHibernateTemplate().find("select count(*) from oquantour.po.StockRealTimePO where ((trade - settlement) / settlement) <= -0.05");
        if (openDown5perStock.size() > 0)
            marketTemperature.setOpenDown5perStock(openDown5perStock.get(0));
        List<Long> totalNum = (List<Long>) this.getHibernateTemplate().find("select count(*) from oquantour.po.StockRealTimePO");
        if (totalNum.size() > 0)
            marketTemperature.setTotalNum(totalNum.get(0));
        List<StockRealTimePO> upList = (List<StockRealTimePO>) this.getHibernateTemplate().find("from oquantour.po.StockRealTimePO where changepercent > 0 order by changepercent desc");
        evictStockRealTimePO(upList);
        marketTemperature.setUpList(upList);
        List<StockRealTimePO> downList = (List<StockRealTimePO>) this.getHibernateTemplate().find("from oquantour.po.StockRealTimePO where changepercent < 0 order by changepercent");
        evictStockRealTimePO(downList);
        marketTemperature.setDownList(downList);

        List<StockRealTimePO> szUpList = (List<StockRealTimePO>) this.getHibernateTemplate().find("from oquantour.po.StockRealTimePO where (stockId like '00%' or stockId like '200%' or stockId like '300%') and changepercent > 0 order by changepercent desc");
        evictStockRealTimePO(szUpList);
        marketTemperature.setSzUpList(szUpList);

        List<StockRealTimePO> ssUpList = (List<StockRealTimePO>) this.getHibernateTemplate().find("from oquantour.po.StockRealTimePO where stockId like '600%' and changepercent > 0 order by changepercent desc");
        evictStockRealTimePO(ssUpList);
        marketTemperature.setSsUpList(ssUpList);

        List<StockRealTimePO> szDownList = (List<StockRealTimePO>) this.getHibernateTemplate().find("from oquantour.po.StockRealTimePO where (stockId like '00%' or stockId like '200%' or stockId like '300%') and changepercent < 0 order by changepercent");
        evictStockRealTimePO(szDownList);
        marketTemperature.setSzDownList(szDownList);

        List<StockRealTimePO> ssDownList = (List<StockRealTimePO>) this.getHibernateTemplate().find("from oquantour.po.StockRealTimePO where stockId like '600%' and changepercent < 0 order by changepercent");
        evictStockRealTimePO(ssDownList);
        marketTemperature.setSsDownList(ssDownList);

        List<StockRealTimePO> szVolumeList = (List<StockRealTimePO>) this.getHibernateTemplate().find("from oquantour.po.StockRealTimePO where (stockId like '00%' or stockId like '200%' or stockId like '300%') order by volume desc");
        evictStockRealTimePO(szVolumeList);
        marketTemperature.setSzVolumeList(szVolumeList);

        List<StockRealTimePO> ssVolumeList = (List<StockRealTimePO>) this.getHibernateTemplate().find("from oquantour.po.StockRealTimePO where stockId like '600%' order by volume desc");
        evictStockRealTimePO(ssVolumeList);
        marketTemperature.setSsVolumeList(ssVolumeList);

        List<StockRealTimePO> szAmountList = (List<StockRealTimePO>) this.getHibernateTemplate().find("from oquantour.po.StockRealTimePO where (stockId like '00%' or stockId like '200%' or stockId like '300%') order by amount desc");
        evictStockRealTimePO(szAmountList);
        marketTemperature.setSzAmountList(szAmountList);

        List<StockRealTimePO> ssAmountList = (List<StockRealTimePO>) this.getHibernateTemplate().find("from oquantour.po.StockRealTimePO where stockId like '600%' order by amount desc");
        evictStockRealTimePO(ssAmountList);
        marketTemperature.setSsAmountList(ssAmountList);

        Date date = new Date(new java.util.Date().getTime());
        marketTemperature.setDate(date);

        marketTemperature.setTemperature(marketTemperature.calTemperature());

        List<StockRealTimePO> volumeList = (List<StockRealTimePO>) this.getHibernateTemplate().find("from oquantour.po.StockRealTimePO order by volume desc ");
        List<StockRealTimePO> amountList = (List<StockRealTimePO>) this.getHibernateTemplate().find("from oquantour.po.StockRealTimePO order by amount desc ");
        evictStockRealTimePO(volumeList);
        evictStockRealTimePO(amountList);

        List<Long> totalAmount = (List<Long>) this.getHibernateTemplate().find("select sum(amount) from oquantour.po.StockRealTimePO");
        System.out.println(System.currentTimeMillis());

        Long volume = totalVolume.get(0);
        Long amount = totalAmount.get(0);
        Map<String, HotStockPO> map = new HashMap<>();
        for (int i = 0; i < upList.size(); i++) {
            HotStockPO hotStockPO = new HotStockPO();
            hotStockPO.setStockRealTimePO(upList.get(i));
            hotStockPO.setStockID(upList.get(i).getStockId());
            hotStockPO.setChangeRank(i + 1);
            hotStockPO.setChangeRate(upList.get(i).getChangepercent() * 1200);
            map.put(upList.get(i).getStockId(), hotStockPO);
        }

        int j = 1;
        for (int i = 0; i < volumeList.size(); i++) {
//            System.out.println(volumeList.get(i).getVolume());
            if (map.get(volumeList.get(i).getStockId()) != null) {
                map.get(volumeList.get(i).getStockId()).setVolumeRank(j);
                map.get(volumeList.get(i).getStockId()).setVolumeRate(((volumeList.get(i).getVolume().doubleValue() * 1000) / volume.doubleValue()));
                j++;
            }
        }

        j = 1;
        for (int i = 0; i < amountList.size(); i++) {
            if (map.get(amountList.get(i).getStockId()) != null) {
                map.get(amountList.get(i).getStockId()).setAmountRank(j);
                map.get(amountList.get(i).getStockId()).setAmountRate(((amountList.get(i).getAmount().doubleValue() * 10000) / amount.doubleValue()));
                j++;
            }
        }

        for (String s : map.keySet()) {
            map.get(s).calComprehensiveRate();
        }


        List<Map.Entry<String, HotStockPO>> infoIds =
                new ArrayList<Map.Entry<String, HotStockPO>>(map.entrySet());

        Collections.sort(infoIds, new Comparator<Map.Entry<String, HotStockPO>>() {
            public int compare(Map.Entry<String, HotStockPO> o1, Map.Entry<String, HotStockPO> o2) {
                double dif = (o1.getValue().getComprehensiveRate()) - (o2.getValue().getComprehensiveRate());
                if (dif > 0)
                    return 1;
                if (dif == 0)
                    return 0;
                if (dif < 0)
                    return -1;
                //return (o2.getValue() - o1.getValue());
                return 0;
            }
        });

        List<HotStockPO> hotStockPOList = new ArrayList<>();
        for (int i = infoIds.size() - 1; i >= 0; i--) {
            HotStockPO hotStockPO = infoIds.get(i).getValue();
            hotStockPO.setComprehensiveRank(infoIds.size() - i);
            hotStockPOList.add(hotStockPO);
//            System.out.println(hotStockPO.getStockID() + " " + hotStockPO.getChangeRank() + ":" + hotStockPO.getStockRealTimePO().getChangepercent() + " " + hotStockPO.getAmountRank()  + ":" + hotStockPO.getStockRealTimePO().getAmount() + " " + hotStockPO.getVolumeRank()  + ":" + hotStockPO.getStockRealTimePO().getVolume() + " " + hotStockPO.getComprehensiveRate());
        }
        marketTemperature.setHotStockPOS(hotStockPOList);

        /*
        System.out.println(marketTemperature.getTotalVolume());
        System.out.println(marketTemperature.getDate());
        System.out.println(marketTemperature.getDown5perStock());
        System.out.println(marketTemperature.getLimitDownStock());
        System.out.println(marketTemperature.getLimitUpStock());
        System.out.println(marketTemperature.getOpenDown5perStock());
        System.out.println(marketTemperature.getOpenUp5perStock());
        System.out.println(marketTemperature.getDownList().size());
        System.out.println(marketTemperature.getUpList().size());
        System.out.println(marketTemperature.getSsAmountList().size());
        System.out.println(marketTemperature.getSsDownList().size());
        System.out.println(marketTemperature.getSsUpList().size());
        System.out.println(marketTemperature.getSsVolumeList().size());
        System.out.println(marketTemperature.getSzAmountList().size());
        System.out.println(marketTemperature.getSzDownList().size());
        System.out.println(marketTemperature.getSzUpList().size());
        System.out.println(marketTemperature.getSzVolumeList().size());
*/
        return marketTemperature;
    }

    @Override
    public void getWidth() {
        List<String> args = new ArrayList<>();
        args.add("esp");
        args.add("bvps");
        args.add("roe");
        args.add("netProfits");
        args.add("profitsYoy");
        args.add("netProfitRatio");
        args.add("grossProfitRate");
        args.add("businessIncome");
        args.add("bips");
        args.add("arturnover");
        args.add("inventoryTurnover");
        args.add("inventoryDays");
        args.add("currentassetTurnover");
        args.add("currentassetDays");
        args.add("arturndays");
        args.add("mbrg");
        args.add("nprg");
        args.add("nav");
        args.add("targ");
        args.add("epsg");
        args.add("seg");
        args.add("currentRatio");
        args.add("quickRatio");
        args.add("icRatio");
        args.add("sheqRatio");
        args.add("adRatio");
        args.add("cashRatio");
        args.add("cfSales");
        args.add("rateOfReturn");
        args.add("cfNm");
        args.add("cfLiabilities");
        args.add("cashflowRatio");

        for(int i = 0 ; i < args.size(); i++) {
            List<Double> l1 = (List<Double>) this.getHibernateTemplate().find("select max(" +
                    args.get(i) +
                    ") from oquantour.po.StockBasicInfoPO");
            List<Double> l2 = (List<Double>) this.getHibernateTemplate().find("select min(" +
                    args.get(i) +
                    ") from oquantour.po.StockBasicInfoPO");
            System.out.println(args.get(i) + " max:" + l1.get(0) + ", min:" + l2.get(0));
        }

    }

    /**
     * 添加新闻信息
     */
    @Transactional(readOnly = false)
    @Override
    public void addNewsInfo() {
        NewsInfoGetter newsInfoGetter = new NewsInfoGetter();
        List<NewsPO> newsPOS = newsInfoGetter.getNews();
        for(int i = 0; i < newsPOS.size(); i++) {
            NewsPO newsPO = new NewsPO();
            newsPO.setTitle(newsPOS.get(i).getTitle());
            newsPO.setDateValue(newsPOS.get(i).getDateValue());
            newsPO.setUrl(newsPOS.get(i).getUrl());
            newsPO.setContent(newsPOS.get(i).getContent());
            System.out.println(newsPOS.get(i).getTitle());
            System.out.println(newsPOS.get(i).getDateValue());
            List<NewsPO> newsPOS1 = (List<NewsPO>) this.getHibernateTemplate().find("from oquantour.po.NewsPO where title = ? and dateValue = ?", new Object[]{newsPOS.get(i).getTitle(), newsPOS.get(i).getDateValue()});
            if(newsPOS1.size() == 0)
                this.getHibernateTemplate().saveOrUpdate(newsPO);
        }
    }

    /**
     * 获得新闻信息
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public List<NewsPO> getNewsInfo() {
        List<NewsPO> result = new ArrayList<>();
        List<NewsPO> newsPOS = (List<NewsPO>) this.getHibernateTemplate().find("from oquantour.po.NewsPO order by dateValue desc ");
        for(int i = 0; i < newsPOS.size(); i++){
            this.getHibernateTemplate().evict(newsPOS.get(i));
            if(i < 20)
                result.add(newsPOS.get(i));
            System.out.println(newsPOS.get(i).getDateValue());
        }
        return result;
    }

    @Transactional(readOnly = false)
    @Override
    public void addTopList(Date date) {
        TopListInfoGetter topListInfoGetter = new TopListInfoGetter();
        List<TopListPO> listPOS = topListInfoGetter.getTopList(date);
        for(int i = 0; i < listPOS.size(); i++){
            System.out.println(listPOS.get(i).getStockId());
            List<TopListPO> topListPOS = (List<TopListPO>)this.getHibernateTemplate().find("from oquantour.po.TopListPO where stockId = ? and dateValue = ? and reason = ?", new Object[]{listPOS.get(i).getStockId(), listPOS.get(i).getDateValue(), listPOS.get(i).getReason()});
            if(topListPOS.size() > 0){
                this.getHibernateTemplate().evict(topListPOS.get(0));
            }else {
                this.getHibernateTemplate().saveOrUpdate(listPOS.get(i));
            }
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<TopListPO> getTopListInfo() {
        List<TopListPO> result = new ArrayList<>();
        List<Date> dates = (List<Date>)this.getHibernateTemplate().find("select max(dateValue) from oquantour.po.TopListPO");
        if(dates.size() > 0) {
            Date date = dates.get(0);
            List<TopListPO> topListPOS = (List<TopListPO>)this.getHibernateTemplate().find("from oquantour.po.TopListPO where dateValue = ?", date);
            for(int i = 0; i < topListPOS.size(); i++){
                TopListPO topListPO = topListPOS.get(i);
                this.getHibernateTemplate().evict(topListPO);
                result.add(topListPO);
            }
        }
        return result;
    }
}
