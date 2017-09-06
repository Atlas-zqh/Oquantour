package oquantour.data.daoImpl;

import oquantour.data.dao.IndustryDao;
import oquantour.po.IndustryPO;
import oquantour.po.StockPO;
import oquantour.po.StockRealTimePO;
import oquantour.po.util.ChartInfo;
import org.apache.commons.collections.map.AbstractMapDecorator;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by island on 2017/6/6.
 */
@Transactional(readOnly = true)
@Repository
public class IndustryDaoImpl implements IndustryDao {
    //使用Spring注入HibernateTemplate的一个实例
    private HibernateTemplate hibernateTemplate;

    public HibernateTemplate getHibernateTemplate() {
        return hibernateTemplate;
    }

    @Resource(name = "hibernateTemplate")
    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    @Override
    public List<ChartInfo> getIndustryReturnRate(String plateName, Date startDate, Date endDate) {
        List<IndustryPO> industryPOS = (List<IndustryPO>) this.getHibernateTemplate().find("from oquantour.po.IndustryPO where industry = ? and dateValue between ? and ?", new Object[]{plateName, startDate, endDate});
        List<ChartInfo> chartInfos = new ArrayList<>();
        for(int i = 0; i < industryPOS.size(); i++){
            this.getHibernateTemplate().evict(industryPOS.get(i));
            if(industryPOS.get(i).getReturnRate() != null) {
                ChartInfo chartInfo = new ChartInfo();
                chartInfo.setDateXAxis(industryPOS.get(i).getDateValue());
                chartInfo.setyAxis(industryPOS.get(i).getReturnRate());
//                System.out.println(plateName + " " + industryPOS.get(i).getDateValue() + " " + industryPOS.get(i).getReturnRate());
                chartInfos.add(chartInfo);
            }
        }
        return chartInfos;
    }

    /**
     * 获得所有行业名
     *
     * @return
     */
    @Override
    public List<String> getAllIndustries() {
        List<String> industries = (List<String>) this.getHibernateTemplate().find("select distinct industry from oquantour.po.StockInfoPO");
        return industries;
    }

    /**
     * 获得行业中所有股票数据
     * @param industryName
     * @return
     */
    @Override
    public Map<String, StockRealTimePO> getStocksInIndustry(String industryName) {
        List<String> stockIds = (List<String>)this.getHibernateTemplate().find("select distinct stockId from oquantour.po.StockInfoPO where industry = ?", industryName);
        Map<String, StockRealTimePO> stocks = new HashMap<>();
        for(int i = 0; i < stockIds.size(); i++){
            List<StockRealTimePO> stockRealTimePOS = (List<StockRealTimePO>)this.getHibernateTemplate().find("from oquantour.po.StockRealTimePO where stockId = ?", stockIds.get(i));
            if(stockRealTimePOS.size() > 0){
                this.getHibernateTemplate().evict(stockRealTimePOS.get(0));
                stocks.put(stockIds.get(i), stockRealTimePOS.get(0));
            }
        }
        return stocks;
    }

    /**
     * 获得股票所在的板块名
     *
     * @param stockIDs
     * @return
     */
    @Override
    public Map<String, String> getIndustryOfStock(String... stockIDs) {
        Map<String, String> stocks = new HashMap<>();
        for (int i = 0; i < stockIDs.length; i++) {
            List<String> industries = (List<String>) this.getHibernateTemplate().find("select industry from oquantour.po.StockInfoPO where stockId = ?", stockIDs[i]);
            if (industries.size() > 0)
                stocks.put(stockIDs[i], industries.get(0));
        }
        return stocks;
    }

    /**
     * 获得行业中所有股票的ID
     * @param industryName
     * @return
     */
    @Override
    public List<String> getStockIDsInIndustry(String industryName) {
        List<String> stockIds = (List<String>)this.getHibernateTemplate().find("select distinct stockId from oquantour.po.StockInfoPO where industry = ?", industryName);

        return stockIds;
    }

    /**
     * 添加行业信息
     * @param industry
     */
    @Override
    public void addIndustryInfo(String industry) {
        /*
        System.out.println(System.currentTimeMillis());
        List<StockPO> stockPOSs = (List<StockPO>) this.getHibernateTemplate().find("from oquantour.po.StockPO");
        System.out.println(stockPOSs.size());
        System.out.println(System.currentTimeMillis());
*/
//        System.out.println(System.currentTimeMillis());
        List<Date> dates = (List<Date>) this.getHibernateTemplate().find("select distinct dateValue from oquantour.po.StockPO");
//        System.out.println(System.currentTimeMillis());
        Map<Date, List<StockPO>> dateListMap = new HashMap<>();
        for (int i = 0; i < dates.size(); i++) {
            List<StockPO> stockPOS = new ArrayList<>();
            dateListMap.put(dates.get(i), stockPOS);
        }
        List<String> industries = getAllIndustries();
        System.out.println(industries.size());

        int threadCount = 0;
        List<Thread> threads = new ArrayList<>();
        List<Integer> deadThreads = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            List<String> stockIds = (List<String>) getHibernateTemplate().find("select stockId from oquantour.po.StockInfoPO where industry = ?", industry);
            System.out.print("stockID.size:" + stockIds.size());
            for (int j = 0; j < stockIds.size(); j++) {
                List<StockPO> stockPOS = (List<StockPO>) getHibernateTemplate().find("from oquantour.po.StockPO where stockId = ?", stockIds.get(j));
                System.out.println(stockIds.get(j) + ":" + stockPOS.size());
                for (int k = 0; k < stockPOS.size(); k++) {
                    this.getHibernateTemplate().evict(stockPOS.get(k));
                    dateListMap.get(stockPOS.get(k).getDateValue()).add(stockPOS.get(k));
                }
            }

            List<IndustryPO> industryPOS = new ArrayList<>();
            for (Date date : dateListMap.keySet()) {
                List<StockPO> stockPOS = dateListMap.get(date);
                IndustryPO industryPO = new IndustryPO();
                industryPO.setIndustry(industry);
                industryPO.setDateValue(date);
                double returnRate = 0;
                for (int j = 0; j < stockPOS.size(); j++) {
                    if (stockPOS.get(j).getReturnRate() != null)
                        returnRate += stockPOS.get(j).getReturnRate();
                    else {
                        stockPOS.remove(j);
                        j--;
                    }
                }
                if (stockPOS.size() != 0) {
                    returnRate = returnRate / stockPOS.size();
                    industryPO.setReturnRate(returnRate);
                }
                industryPOS.add(industryPO);

                System.out.println(industryPO.getIndustry() + " " + industryPO.getDateValue() + " " + industryPO.getReturnRate());
                System.out.println(date + "," + industry + ":" + stockPOS.size());
                dateListMap.get(date).clear();
            }

            for (int j = 0; j < industryPOS.size(); j++) {
                System.out.println(industryPOS.get(j));
                addIndustry(industryPOS.get(j));

            }
        }
//            if (threadCount < 5) {
//                Thread thread = new Thread(new Runnable() {
//                    @Transactional(readOnly = false)
//                    @Override
//                    public void run() {
//                        List<String> stockIds = (List<String>) getHibernateTemplate().find("select stockId from oquantour.po.StockInfoPO where industry = ?", industries.get(count));
//                        System.out.print("stockID.size:" + stockIds.size());
//                        for (int j = 0; j < stockIds.size(); j++) {
//                            List<StockPO> stockPOS = (List<StockPO>) getHibernateTemplate().find("from oquantour.po.StockPO where stockId = ?", stockIds.get(j));
//                            System.out.println(stockIds.get(j) + ":" + stockPOS.size());
//                            for (int k = 0; k < stockPOS.size(); k++) {
//                                dateListMap.get(stockPOS.get(k).getDateValue()).add(stockPOS.get(k));
//                            }
//                        }
//
//                        List<IndustryPO> industryPOS = new ArrayList<>();
//                        for (Date date : dateListMap.keySet()) {
//                            List<StockPO> stockPOS = dateListMap.get(date);
//                            IndustryPO industryPO = new IndustryPO();
//                            industryPO.setIndustry(industries.get(count));
//                            industryPO.setDateValue(date);
//                            double returnRate = 0;
//                            for (int j = 0; j < stockPOS.size(); j++) {
//                                if(stockPOS.get(j).getReturnRate() != null)
//                                    returnRate += stockPOS.get(j).getReturnRate();
//                                else{
//                                    stockPOS.remove(j);
//                                    j--;
//                                }
//                            }
//                            if (stockPOS.size() != 0) {
//                                returnRate = returnRate / stockPOS.size();
//                                industryPO.setReturnRate(returnRate);
//                            }
//                            industryPOS.add(industryPO);
////                    Transaction tran=session.openTran.....
////                    getHibernateTemplate().save(user);
////                    tran.commit();
//                            System.out.println(industryPO.getIndustry() + " " + industryPO.getDateValue() + " " + industryPO.getReturnRate());
//                            System.out.println(date + "," + industries.get(count) + ":" + stockPOS.size());
//                            dateListMap.get(date).clear();
//                        }
//
//                        for (int j = 0; j < industryPOS.size(); j++) {
//                            System.out.println(industryPOS.get(j));
//                            addIndustry(industryPOS.get(j));
//
//
////                            getHibernateTemplate().getSessionFactory().getCurrentSession().c;
//
//
//                            //                    hibernateTemplate.getSessionFactory().getCurrentSession().con().commit();
//                            //                            System.out.println(industryInfoPOS.size());
//                        }
//                        industryPOS.clear();
//                    }
//
//                });
//                threads.add(thread);
//                threadCount++;
//                thread.start();
//
////                    final int count = j;
////                    this.hibernateTemplate.execute(
////                            new HibernateCallback()
////                            {
////                                public Object doInHibernate(Session session) throws HibernateException
////                                {
////                                    Transaction tx = session.beginTransaction();
////
////                                    tx.begin();
////                                    session.save(industryPOS.get(count));
////                                    tx.commit();
////                                    session.close();
////                                    return null;
////                                }
////                            }
////                    );
//
//            }
//            else{
//                i--;
//                for(int j = 0; j < threads.size(); j++){
//                    if(!threads.get(j).isAlive()) {
//                        deadThreads.add(j);
//                        System.out.println("============================" + j + "============================");
//                    }
//                }
//                for(int j = deadThreads.size() - 1; j >= 0; j--){
//                    threads.remove(deadThreads.get(j));
//                }
//            }
//        }
        System.out.println("done");
//        }catch (ParseException e){
//            e.printStackTrace();
//        }
    }


    public void addIndustry(IndustryPO industryPO) {
        this.getHibernateTemplate().saveOrUpdate(industryPO);
    }
}
