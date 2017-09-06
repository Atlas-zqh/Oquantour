package oquantour.data.daoImpl;

import oquantour.data.dao.PlateDao;
import oquantour.data.datagetter.RealTimeInfoGetter;
import oquantour.po.PlateRealTimePO;
import oquantour.po.PlateinfoPO;
import oquantour.po.StockPO;
import oquantour.po.util.StockNameInfo;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by island on 5/11/17.
 */
@Transactional(readOnly = false)
@Repository
public class PlateDaoImpl implements PlateDao{
    //使用Spring注入HibernateTemplate的一个实例
    private HibernateTemplate hibernateTemplate;

    public HibernateTemplate getHibernateTemplate() {
        return hibernateTemplate;
    }

    @Resource(name="hibernateTemplate")
    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    /**
     * 根据板块名称，获得该段时间内存在数据的股票
     * @param plateName
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public Map<String, List<StockPO>> getStockInStockPlate(String plateName, Date startDate, Date endDate) {
        List<String> stockIds = (List<String>) this.getHibernateTemplate().find("select stockId from oquantour.po.StockInfoPO where plate = ?", plateName);
//        List<StockPO> stockPOS1 = (List<StockPO>) this.getHibernateTemplate()
//                .find("from oquantour.po.StockInfoPO where  = ? and dateValue between ? and ?"
//                        , new Object[]{plateName, startDate, endDate});

        Map<String, List<StockPO>> stockMap = new HashMap<>();
        for(int i = 0; i < stockIds.size(); i++){
            List<StockPO> stockPOS = (List<StockPO>)this.getHibernateTemplate().find("from oquantour.po.StockPO where stockId = ? and dateValue between ? and ?", new Object[]{stockIds.get(i), startDate, endDate});
            for(int j = 0; j < stockPOS.size(); j++){
                this.getHibernateTemplate().evict(stockPOS.get(j));
            }
            stockMap.put(stockIds.get(i), stockPOS);
        }
        /*
        if(stockPOS.size() > 0) {
            String stockID = stockPOS.get(0).getStockId();
            List<StockPO> stockPOSofStockID = new ArrayList<>();
            StockPO stockPO;
            for (int i = 0; i < stockPOS.size(); i++) {
                stockPO = stockPOS.get(i);
                if(stockPO.getStockId().equals(stockID)){
                    stockPOSofStockID.add(stockPO);
                }
                else{
                    stockMap.put(stockID, stockPOSofStockID);
                    stockPOSofStockID.clear();
                    stockID = stockPO.getStockId();
                    stockPOSofStockID.add(stockPO);
                }
            }
        }*/
        return stockMap;
    }

    /**
     * 根据股票信息获得它所在的板块
     * @param stockInfo 股票名或代码
     * @return 板块名
     */
    @Override
    public String getStockPlate(String stockInfo) {
        List<String> plates= (List<String>) this.getHibernateTemplate()
                .find("select distinct plate from oquantour.po.StockInfoPO where stockId = ? or stockName = ?",
                        new String[]{stockInfo, stockInfo});
        if(plates.size() > 0)
            return plates.get(0);
        else
            return "";
    }

    /**
     * 得到板块中的所有股票 代码;名称
     * @param plateName 板块名
     * @return 板块中的所有股票的stockID;stockName
     */
    @Override
    public List<String> getAllStockInfoInPlate(String... plateName) {
        List<String> stocks = new ArrayList<>();
        for(int j = 0; j < plateName.length; j++) {
            List<StockNameInfo> stockNameInfos = (List<StockNameInfo>) this.getHibernateTemplate()
                    .find("select distinct new oquantour.po.util.StockNameInfo(stockId, stockName) " +
                                    "from oquantour.po.StockInfoPO where plate = ?"
                            , plateName[j]);
            String nameInfo;
            for (int i = 0; i < stockNameInfos.size(); i++) {
                nameInfo = stockNameInfos.get(i).getStockID() + ";" + stockNameInfos.get(i).getStockName();
                if (!stocks.contains(nameInfo)) {
                    stocks.add(nameInfo);
                }
            }
        }
        return stocks;
    }

    /**
     * 获得板块一段时间内每个交易日的收益率
     * @param plateName
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public List<PlateinfoPO> getPlateInfo(String plateName, Date startDate, Date endDate) {
        List<PlateinfoPO> plateinfoPOS = (List<PlateinfoPO>) this.getHibernateTemplate().find("from oquantour.po.PlateinfoPO where plateName = ? and dateValue between ? and ?", new Object[]{plateName, startDate, endDate});
        for(int i = 0; i < plateinfoPOS.size(); i++){
            this.getHibernateTemplate().evict(plateinfoPOS.get(i));
        }
        return plateinfoPOS;
    }

    /**
     * 获得所有板块名称
     * @return
     */
    @Override
    public List<String> getAllPlateName() {
        List<String> plateName = (List<String>) this.getHibernateTemplate().find("select distinct plate from oquantour.po.StockInfoPO");

        return plateName;
    }

    /**
     * 获得实时板块数据
     * @return Map<板块名称, PlateRealTimePO></>
     */
    @Override
    public Map<String, PlateRealTimePO> getRealTimePlateInfo() {
        List<PlateRealTimePO> plateRealTimePOS = (List<PlateRealTimePO>)this.getHibernateTemplate().find("from oquantour.po.PlateRealTimePO");
        Map<String, PlateRealTimePO> map = new HashMap<>();
        for(int i = 0; i < plateRealTimePOS.size(); i++){
            this.getHibernateTemplate().evict(plateRealTimePOS.get(i));
            map.put(plateRealTimePOS.get(i).getPlateName(), plateRealTimePOS.get(i));
        }

        return map;
    }

    /**
     * 更新实时板块数据
     */
    @Override
    public void updateRealTimePlateInfo() {
        RealTimeInfoGetter realTimeInfoGetter = new RealTimeInfoGetter();
        Map<String, PlateRealTimePO> map = realTimeInfoGetter.getRealTimePlate();
        for(String s: map.keySet()){
            this.getHibernateTemplate().saveOrUpdate(map.get(s));
        }
    }

    /**
     * 添加板块信息
     */
    @Override
    public void addPlateInfo() {
        List<String> plates = getAllPlateName();
        Map<String, List<String>> stocksInPlate = new HashMap<>();
        for(int i = 0; i < plates.size(); i++){
            List<String> stocks = (List<String>) this.getHibernateTemplate().find("select stockId from oquantour.po.StockInfoPO where plate = ?", plates.get(i));
            stocksInPlate.put(plates.get(i), stocks);
        }
//        for()
//        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        System.out.println(plates.size());
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = new Date(simpleDateFormat.parse("2005-01-01").getTime());
            Date endDate = new Date(simpleDateFormat.parse("2017-12-31").getTime());
            System.out.println(startDate);
            List<Date> dates = (List<Date>) this.getHibernateTemplate().find("select distinct dateValue from oquantour.po.StockPO");

            for(int i = 0; i < dates.size(); i++){
                System.out.println(dates.get(i));
                if(dates.get(i).before(endDate) && dates.get(i).after(startDate)){
                    for(int j = 0; j < plates.size(); j++) {

                        List<Double> rr = (List<Double>) this.getHibernateTemplate().find("select avg(stockPO.returnRate) from oquantour.po.StockPO stockPO, oquantour.po.StockInfoPO stockInfoPO where stockInfoPO.plate = ? and stockPO.dateValue = ?", new Object[]{plates.get(j), dates.get(i)});
                        PlateinfoPO plateinfoPO = new PlateinfoPO();
                        plateinfoPO.setDateValue(dates.get(i));
                        plateinfoPO.setPlateName(plates.get(j));
                        plateinfoPO.setReturnRate(rr.get(0));
                        System.out.println(plates.get(j) + " " + dates.get(i) + " " + rr.get(0));
                    }
                }
            }

        }catch (ParseException e){
            e.printStackTrace();
        }
    }

    /**
     * 更新板块信息
     * @param plateinfoPO
     */
    @Override
    public void updatePlateInfo(PlateinfoPO plateinfoPO) {
        this.getHibernateTemplate().saveOrUpdate(plateinfoPO);
    }
}
