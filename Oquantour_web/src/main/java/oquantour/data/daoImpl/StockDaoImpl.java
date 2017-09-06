package oquantour.data.daoImpl;

import oquantour.data.dao.StockDao;
import oquantour.data.datagetter.DailyInfoGetter;
import oquantour.data.datagetter.RealTimeInfoGetter;
import oquantour.exception.FormativeNotExistsException;
import oquantour.exception.StockLessThanOneHundredException;
import oquantour.po.*;
import oquantour.util.tools.BasicIndices;
import oquantour.util.tools.HibernateUtil;
import oquantour.po.util.StockNameInfo;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate5.HibernateCallback;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.orm.hibernate5.SessionFactoryUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.TypedQuery;
import java.io.EOFException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by island on 5/3/17.
 */
@Repository
public class StockDaoImpl implements StockDao {


    //使用Spring注入HibernateTemplate的一个实例
    private HibernateTemplate hibernateTemplate;

    public HibernateTemplate getHibernateTemplate() {
        return hibernateTemplate;
    }

    @Resource(name = "hibernateTemplate")
    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    @Resource(name = "sessionFactory")
    private SessionFactory sessionFactory;


    public Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public List queryBySql(String sql) {
        List<Object[]> list = getSession().createSQLQuery(sql).list();
        return list;
    }

    public int excuteBySql(String sql) {
        int result;
        SQLQuery query = this.getSession().createSQLQuery(sql);
        result = query.executeUpdate();
        return result;
    }


    /**
     * 根据股票代码查询股票数据
     *
     * @param stockCode
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public List<StockPO> searchStock(String stockCode) {
        List<StockPO> stockPOS = (List<StockPO>) this.getHibernateTemplate().find("from oquantour.po.StockPO where stockId = ?",
                stockCode);
        for (int i = 0; i < stockPOS.size(); i++) {
            this.getHibernateTemplate().evict(stockPOS.get(i));
        }

        if (stockPOS != null || stockPOS.size() > 0)
            return stockPOS;
        else
            return new ArrayList<>();
    }

    /**
     * 根据股票代码和日期范围查询股票数据
     *
     * @param stockCode
     * @param startDate
     * @param endDate
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public List<StockPO> searchStock(String stockCode, Date startDate, Date endDate) {
        List<StockPO> stockPOS = (List<StockPO>) this.getHibernateTemplate().find("from oquantour.po.StockPO where stockId = ? and (dateValue between ? and ?)",
                new Object[]{stockCode, startDate, endDate});
        for (int i = 0; i < stockPOS.size(); i++) {
            this.getHibernateTemplate().evict(stockPOS.get(i));
        }
        if (stockPOS != null || stockPOS.size() > 0)
            return stockPOS;
        else
            return new ArrayList<>();
    }

    /**
     * 获得实时股票数据
     *
     * @param stockIDs
     * @return Map<StockID，StockRealTimePO></>
     */
    @Transactional(readOnly = true)
    @Override
    public Map<String, StockRealTimePO> getRealTimeStockInfo(String... stockIDs) {
        Map<String, StockRealTimePO> map = new HashMap<>();
        for (int i = 0; i < stockIDs.length; i++) {
            List<StockRealTimePO> stockRealTimePOS = (List<StockRealTimePO>) this.getHibernateTemplate().find("from oquantour.po.StockRealTimePO where stockId = ?", stockIDs[i]);
            if (stockRealTimePOS.size() > 0) {
                this.getHibernateTemplate().evict(stockRealTimePOS.get(0));
                map.put(stockIDs[i], stockRealTimePOS.get(0));
            }
        }
        return map;
    }

    /**
     * 更新股票实时数据
     */
    @Transactional(readOnly = false)
    @Override
    public void updateRealTimeStockInfo() {
        RealTimeInfoGetter realTimeInfoGetter = new RealTimeInfoGetter();
        List<String> stockCodeAndName = getAllStockCodeAndName();
        Map<String, String> stocks = new HashMap<>();
        for (int i = 0; i < stockCodeAndName.size(); i++) {
            stocks.put(stockCodeAndName.get(i).split(";")[0], stockCodeAndName.get(i).split(";")[1]);
        }
        List<String> allStocks = (List<String>) this.getHibernateTemplate().find("select distinct stockId from oquantour.po.StockInfoPO");
        Map<String, StockRealTimePO> map = realTimeInfoGetter.getRealTimeStock(new ArrayList<>());
        for (String s : map.keySet()) {
            if (stocks.get(s) != null) {
                this.getHibernateTemplate().saveOrUpdate(map.get(s));
                System.out.println("有的" + s);
                stocks.remove(s);
            }
        }
        for (String s : stocks.keySet()) {
            List<StockPO> stockPOS = (List<StockPO>) this.getHibernateTemplate().find("from oquantour.po.StockPO where stockId = ? order by dateValue desc", s);
            for (int i = 0; i < 2; i++) {
                this.getHibernateTemplate().evict(stockPOS.get(i));
            }
            StockPO stockPO = stockPOS.get(0);
            StockRealTimePO stockRealTimePO = new StockRealTimePO();
            stockRealTimePO.setStockId(s);
            stockRealTimePO.setStockName(stocks.get(s));
            stockRealTimePO.setChangepercent(stockPO.getChg());
            stockRealTimePO.setTrade(stockPO.getClosePrice());
            stockRealTimePO.setSettlement(stockPOS.get(1).getClosePrice());
            stockRealTimePO.setOpen(stockPO.getOpenPrice());
            stockRealTimePO.setHigh(stockPO.getHighPrice());
            stockRealTimePO.setLow(stockPO.getLowPrice());
            stockRealTimePO.setVolume(stockPO.getVolume().longValue());
            stockRealTimePO.setAmount(stockPO.getAmount().longValue());
            this.getHibernateTemplate().saveOrUpdate(stockRealTimePO);
            System.out.println("没有的" + s);
        }
    }

    /**
     * 根据日期搜索市场中所有股票的信息
     *
     * @param startDate
     * @param endDate
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public Map<String, List<StockPO>> searchAllStocksByDate(Date startDate, Date endDate) {
        Map<String, List<StockPO>> map = new HashMap<>();
        List<String> stockInfos = getAllStockCodeAndName();
        long start = System.currentTimeMillis();
        int finishCount = 0;
        int threadCount = 0;
        int maxThreadCount = 100;
        List<Thread> threads = new ArrayList<>();
        List<Integer> deadThreads = new ArrayList<>();
        final Date sDate = startDate;
        final Date eDate = endDate;
        for (int i = 0; i < stockInfos.size(); i++) {
            final int count = i;
            if (threadCount < maxThreadCount) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String stockId = stockInfos.get(count).split(";")[0];
                        List<StockPO> stockPOS = (List<StockPO>) getHibernateTemplate().find("select new oquantour.po.StockPO(stockId, dateValue, adjClose, closePrice, highPrice, lowPrice, openPrice, volume, ma5, ma10, ma20, ma30, ma60, ma120, ma240, chg, returnRate, amount, rsv, kValue, dValue, jValue, ema12, ema26, dif, dea, rsi, dmPlus, dmMinus, tr, diPlus, diMinus, adx, adxr, dx, mb, up, dn) from oquantour.po.StockPO where stockId = ? and (dateValue between ? and ?)",
                                new Object[]{stockId, sDate, eDate});
                        map.put(stockId, stockPOS);
                        System.out.println(stockId);
                    }
                });
                threads.add(thread);
                threadCount++;
                thread.start();
            } else {
                for (int j = 0; j < maxThreadCount; j++) {
                    if (!threads.get(j).isAlive()) {
                        threadCount--;
                        deadThreads.add(j);
                        finishCount++;
                    }
                }
                for (int j = deadThreads.size() - 1; j >= 0; j--) {
                    threads.remove(threads.get(deadThreads.get(j)));
                }
                deadThreads.clear();
                i--;
            }
        }
        System.out.println(System.currentTimeMillis() - start);
        while (finishCount < stockInfos.size()) {

        }
//        if (stockPOS == null || stockPOS.size() == 0) {
//            return new HashMap<>();
//        } else {
//            String stockID = stockPOS.get(0).getStockId();
//            List<StockPO> stockPOSofStockId = new ArrayList<>();
//            StockPO stockPO;
//            for (int i = 0; i < stockPOS.size(); i++) {
//                this.getHibernateTemplate().evict(stockPOS.get(i));
//                stockPO = stockPOS.get(i);
//                if (stockPO.getStockId().equals(stockID)) {
//                    stockPOSofStockId.add(stockPO);
//                } else {
//                    map.put(stockID, stockPOSofStockId);
//                    stockID = stockPO.getStockId();
//                    stockPOSofStockId = new ArrayList<>();
//                    stockPOSofStockId.add(stockPO);
//                    System.out.print(stockID);
//                }
//
//            }
        return map;

    }

    /**
     * 得到股票代码;股票名称（无论传入的是股票名还是股票代码）
     *
     * @param stockInfoList 股票名或股票代码
     * @return 股票代码
     */
    @Transactional(readOnly = true)
    @Override
    public List<String> getStockCode(String... stockInfoList) {
        List<String> stocks = new ArrayList<>();

        String hql = "";
        Session sess = HibernateUtil.getSession();
        TypedQuery<StockNameInfo> sni;
        List<StockNameInfo> stockNameInfos;
        String name;
        for (int i = 0; i < stockInfoList.length; i++) {
            name = stockInfoList[i];
            hql = "select distinct new oquantour.po.util.StockNameInfo(stockId ,stockName) from oquantour.po.StockInfoPO where stockId =: name or stockName =: name";
            sni = sess.createQuery(hql, StockNameInfo.class);
            stockNameInfos = sni.getResultList();
            if (stockNameInfos.size() > 0)
                stocks.add(stockNameInfos.get(0).getStockID() + ";" + stockNameInfos.get(0).getStockName());
        }
        return stocks;
    }

    /**
     * 得到所有的股票代码和名字，格式为"名字;代码"
     *
     * @return 所有的股票代码和名字，格式为"名字;代码"
     */
    @Transactional(readOnly = true)
    @Override
    public List<String> getAllStockCodeAndName() {
//        String hql = "select distinct new oquantour.po.util.StockNameInfo(stockId ,stockName) from oquantour.po.StockPO";
//        Session sess = HibernateUtil.getSession();
//        TypedQuery<StockNameInfo> sni= sess.createQuery(hql, StockNameInfo.class);
//        List<StockNameInfo> stockNameInfos =  sni.getResultList();
//
        List<String> stockInfos = new ArrayList<>();
//        for(int i = 0; i < stockNameInfos.size(); i++){
//            stockInfos.add(stockNameInfos.get(i).getStockName() + ";" + stockNameInfos.get(i).getStockID());
//        }
//        HibernateUtil.closeSession(sess);

        List<StockInfoPO> stockInfoPOS = (List<StockInfoPO>) this.hibernateTemplate.find("from oquantour.po.StockInfoPO");
        for (int i = 0; i < stockInfoPOS.size(); i++) {
            StockInfoPO stockInfoPO = stockInfoPOS.get(i);
            this.getHibernateTemplate().evict(stockInfoPO);
            stockInfos.add(stockInfoPO.getStockId() + ";" + stockInfoPO.getStockName());
        }
        return stockInfos;
    }

    /**
     * 过滤回测股票
     *
     * @param startDate
     * @param endDate
     * @param stocks
     * @param formativePeriod
     * @param filter_ST
     * @param filter_NoData
     * @param filter_Suspension
     * @param largerThan100
     * @return
     * @throws FormativeNotExistsException
     * @throws StockLessThanOneHundredException
     */
    @Transactional(readOnly = true)
    @Override
    public Map<Date, List<StockPO>> filterStock(Date startDate, Date endDate, List<String> stocks,
                                                int formativePeriod, boolean filter_ST, boolean filter_NoData, boolean filter_Suspension, boolean largerThan100) throws
            FormativeNotExistsException, StockLessThanOneHundredException {

        List<Date> transactionDays = (List<Date>) this.getHibernateTemplate().find("select distinct dateValue from oquantour.po.StockPO");
        Collections.sort(transactionDays);
        //System.out.println(startDate);
        //System.out.println(endDate);
        int endIndex = 0;
        int startIndex = 0;
        int index = 0;


        System.out.println(transactionDays.size());
        for (int i = 0; i < transactionDays.size() - 1; i++) {
//            System.out.println(transactionDays.get(i) + " " + startDate);
            if (transactionDays.get(i).before(startDate)) {
                index = i + 1;
            }
            if (transactionDays.get(i).before(endDate)) {
                endIndex = i;
            }
        }
        startDate = transactionDays.get(index);
        endDate = transactionDays.get(endIndex);

        if (index < formativePeriod) {
            throw new FormativeNotExistsException();
        } else {
            startIndex = index - formativePeriod;

        }
        Date formativeStartDate = transactionDays.get(startIndex);
        Date formativeEndDate = endDate;

//        System.out.println(formativeStartDate);
//        System.out.println(endDate);

        int length = endIndex - startIndex;
        System.out.println(startIndex + " " + endIndex);

        String sql = "select * from stockpo s WHERE" +
//                stocks.get(0) +
                " s.DateValue BETWEEN " +
                formativeStartDate +
                " and " +
                formativeEndDate +
                ";";
        System.out.println(sql);


        Map<Date, List<StockPO>> dateMap = new TreeMap<>();
        Map<String, List<StockPO>> stockMap = new TreeMap<>();
        int finishCount = 0;
        int threadCount = 0;
        int maxThreadCount = 30;
        List<Thread> threads = new ArrayList<>();
        List<Integer> deadThreads = new ArrayList<>();
        long start = System.currentTimeMillis();

        System.out.println(stocks.size() + " " + length);
        if (stocks.size() > 120) {
            List<String> newStockIDs = new ArrayList<>();
            while (newStockIDs.size() < 120) {
                int num = (int) (Math.random() * stocks.size());
                String id = stocks.get(num);
                if(!newStockIDs.contains(id))
                    newStockIDs.add(id);
            }
            stocks = newStockIDs;
        }

        System.out.println(stocks.size());

        if (stocks.size() > 120) {
            for (int i = 0; i < stocks.size(); i++) {
                List<StockPO> stockPOS = new ArrayList<>();
                stockMap.put(stocks.get(i), stockPOS);
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(formativeStartDate);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date curDate = new Date(calendar.getTime().getTime());
            while ((curDate.before(formativeEndDate) || curDate.equals(formativeEndDate))) {
//                System.out.println(new Date(calendar.getTime().getTime()) + " " + transactionDays.contains(new Date(calendar.getTime().getTime())));
                if (threadCount < maxThreadCount && transactionDays.contains(curDate)) {
                    final Date currentDate = new Date(calendar.getTime().getTime());
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                    curDate = new Date(calendar.getTime().getTime());
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println(calendar.getTime());
                            List<StockPO> stockPOS = (List<StockPO>) getHibernateTemplate().find("select new oquantour.po.StockPO(stockId, dateValue, adjClose, closePrice, highPrice, lowPrice, openPrice, volume, ma5, ma10, ma20, ma30, ma60, ma120, ma240, chg, returnRate, amount, rsv, kValue, dValue, jValue, ema12, ema26, dif, dea, rsi, dmPlus, dmMinus, tr, diPlus, diMinus, adx, adxr, dx, mb, up, dn) from oquantour.po.StockPO where dateValue = ?", new Object[]{currentDate});
                            System.out.println(stockPOS.get(0).getDateValue() + " size:" + stockPOS.size());
                            for (int i = 0; i < stockPOS.size(); i++) {
                                String stockID = stockPOS.get(i).getStockId();
                                if (stockMap.get(stockID) != null) {
                                    stockMap.get(stockID).add(stockPOS.get(i));
                                }
                            }
                            System.out.println(stockPOS.get(0).getDateValue() + " add finish");


                        }
                    });
                    threads.add(thread);
                    threadCount++;
                    thread.start();
                } else {
                    if (!transactionDays.contains(curDate)) {
                        calendar.add(Calendar.DAY_OF_MONTH, 1);
                        curDate = new Date(calendar.getTime().getTime());
                    }
                    for (int j = 0; j < maxThreadCount && j < threads.size(); j++) {
                        if (!threads.get(j).isAlive()) {
                            threadCount--;
                            deadThreads.add(j);
                            finishCount++;
                        }
                    }
                    for (int j = deadThreads.size() - 1; j >= 0; j--) {
                        threads.remove(threads.get(deadThreads.get(j)));
                    }
                    deadThreads.clear();
                }
            }
            while (threadCount > 0) {
                for (int j = 0; j < threads.size(); j++) {
                    if (!threads.get(j).isAlive()) {
                        deadThreads.add(j);
                        threadCount--;
                        finishCount++;
                    }

                }
                for (int j = deadThreads.size() - 1; j >= 0; j--) {
                    threads.remove(threads.get(deadThreads.get(j)));
                }
                deadThreads.clear();
            }
        } else {
            for (int i = 0; i < stocks.size(); i++) {
                final int count = i;
                final String stockID = stocks.get(count);
                if (threadCount < maxThreadCount) {
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println(stockID);
                            List<StockPO> stockPOS = (List<StockPO>) getHibernateTemplate().find("select new oquantour.po.StockPO(stockId, dateValue, adjClose, closePrice, highPrice, lowPrice, openPrice, volume, ma5, ma10, ma20, ma30, ma60, ma120, ma240, chg, returnRate, amount, rsv, kValue, dValue, jValue, ema12, ema26, dif, dea, rsi, dmPlus, dmMinus, tr, diPlus, diMinus, adx, adxr, dx, mb, up, dn) from oquantour.po.StockPO where stockId = ? and (dateValue between ? and ?)", new Object[]{stockID, formativeStartDate, formativeEndDate});
                            if (stockPOS.size() > 0) {
                                stockMap.put(stockPOS.get(0).getStockId(), stockPOS);
                            }
                            System.out.println(stockID + "取完");
                        }
                    });
                    threads.add(thread);
                    threadCount++;
                    thread.start();
                } else {
                    for (int j = 0; j < maxThreadCount; j++) {
                        if (!threads.get(j).isAlive()) {
                            threadCount--;
                            deadThreads.add(j);
                            finishCount++;
                        }
                    }
                    for (int j = deadThreads.size() - 1; j >= 0; j--) {
                        threads.remove(threads.get(deadThreads.get(j)));
                    }
                    deadThreads.clear();
                    i--;
                }
//            System.out.println(stocks.get(i));
            }

            while (threadCount > 0) {
                for (int j = 0; j < threads.size(); j++) {
                    if (!threads.get(j).isAlive()) {
                        deadThreads.add(j);
                        threadCount--;
                        finishCount++;
                    }

                }
                for (int j = deadThreads.size() - 1; j >= 0; j--) {
                    threads.remove(threads.get(deadThreads.get(j)));
                }
                deadThreads.clear();
            }
        }
        System.out.println(System.currentTimeMillis() - start);


        if (threadCount == 0) {
            System.out.println("开始筛数据");
            List<StockPO> stockPOSofS = new ArrayList<>();
            List<String> removeStocks = new ArrayList<>();
            if (filter_ST) {
                Set entries = stockMap.entrySet();
                if (entries.size() > 0) {
                    List<String> st = getAllStStocks();
                    for (String stockID : stockMap.keySet()) {
                        if (st.contains(stockID))
                            removeStocks.add(stockID);
                    }
                }
            }
            if (filter_NoData) {
                for (String s : stockMap.keySet()) {
                    stockPOSofS = stockMap.get(s);
                    for (int i = 0; i < stockPOSofS.size(); i++) {
                        if (stockPOSofS.get(i).getVolume() == 0) {
                            removeStocks.add(s);
                            i = stockPOSofS.size() - 1;
                        }
                    }
                }
            }
            if (filter_Suspension) {
                for (String s : stockMap.keySet()) {
                    stockPOSofS = stockMap.get(s);
                    if (stockPOSofS.size() < length) {
                        removeStocks.add(s);
                    }
                }
            }
            for (int i = 0; i < removeStocks.size(); i++) {
                if (stockMap.get(removeStocks.get(i)) != null)
                    stockMap.remove(removeStocks.get(i));
            }
            if (largerThan100 && stockMap.size() < 100) {
                throw new StockLessThanOneHundredException();
            }
            for (int i = startIndex; i < endIndex + 1; i++) {
                List<StockPO> stockPOList = new ArrayList<>();
                dateMap.put(transactionDays.get(i), stockPOList);
            }
            for (String s : stockMap.keySet()) {
                stockPOSofS = stockMap.get(s);
                for (int i = 0; i < stockPOSofS.size(); i++) {
                    //System.out.println(stockPOSofS.get(i).getDateValue() + " " + stockPOSofS.get(i).getStockId());
                    dateMap.get(stockPOSofS.get(i).getDateValue()).add(stockPOSofS.get(i));
                }
            }
        }
        long end = System.currentTimeMillis() - start;
        System.out.println(end);
        return dateMap;
    }

    /**
     * 多因子筛选股票
     *
     * @param map
     * @return <股票名, 股票号>
     */
    @Transactional(readOnly = true)
    @Override
    public Map<String, String> filterStock(Map<BasicIndices, double[]> map) {
        for (BasicIndices basicIndices : map.keySet()) {
            System.out.println(basicIndices + ":" + map.get(basicIndices)[0] + " " + map.get(basicIndices)[1]);
        }
//        System.out.println("获得所有id");
//        List<String> allStocks = (List<String>)this.getHibernateTemplate().find("select distinct stockId from oquantour.po.StockInfoPO");
//        System.out.println("获得所有id结束");

        //获得基本面数据数据
//        System.out.println("开始获得数据");
        Map<String, StockBasicInfoPO> stockBasicInfoPOMap = new HashMap<>();
        List<StockBasicInfoPO> stockBasicInfoPOS = (List<StockBasicInfoPO>) this.getHibernateTemplate().find("from oquantour.po.StockBasicInfoPO where quarterOfYear = ?", new Object[]{"2017-1"});
        for (int i = 0; i < stockBasicInfoPOS.size(); i++) {
            this.getHibernateTemplate().evict(stockBasicInfoPOS.get(i));
            stockBasicInfoPOMap.put(stockBasicInfoPOS.get(i).getStockId(), stockBasicInfoPOS.get(i));
        }
//        System.out.println("结束获得数据");

//        basicIndices.add(BasicIndices.adratio);
        //所有指标
        BasicIndices[] basicIndices1 = {BasicIndices.adratio, BasicIndices.arturnover, BasicIndices.cashflowratio, BasicIndices.cashratio,
                BasicIndices.cf_liabilities, BasicIndices.cf_nm, BasicIndices.cf_sales, BasicIndices.roe,
                BasicIndices.net_profit_ratio, BasicIndices.gross_profit_rate, BasicIndices.inventory_turnover, BasicIndices.mbrg,
                BasicIndices.nprg, BasicIndices.nav, BasicIndices.targ, BasicIndices.epsg,
                BasicIndices.currentratio, BasicIndices.quickratio, BasicIndices.sheqratio, BasicIndices.rateofreturn};

        //根据指标筛选
        for (int i = 0; i < basicIndices1.length; i++) {
//            System.out.println(basicIndices1[i]);
            stockBasicInfoPOMap = filterBasicInfo(map, stockBasicInfoPOMap, basicIndices1[i]);
            System.out.println(basicIndices1[i] + " " + stockBasicInfoPOMap.size());
        }

        List<StockInfoPO> stockInfoPOS = (List<StockInfoPO>) this.getHibernateTemplate().find("from oquantour.po.StockInfoPO");
        Map<String, StockInfoPO> stringStockInfoPOMap = new HashMap<>();
        for (int i = 0; i < stockInfoPOS.size(); i++) {
            this.getHibernateTemplate().evict(stockInfoPOS.get(i));
            if (stockBasicInfoPOMap.get(stockInfoPOS.get(i).getStockId()) != null) {
//                System.out.println(stockInfoPOS.get(i).getStockId());
                stringStockInfoPOMap.put(stockInfoPOS.get(i).getStockId(), stockInfoPOS.get(i));
            }
        }

        BasicIndices[] basicIndices2 = {BasicIndices.pe, BasicIndices.pb, BasicIndices.rev, BasicIndices.profit, BasicIndices.npr};
        for (int i = 0; i < basicIndices2.length; i++) {
//            System.out.println(basicIndices2[i]);
            stringStockInfoPOMap = filterStockInfo(map, stringStockInfoPOMap, basicIndices2[i]);
            System.out.println(basicIndices2[i] + " " + stringStockInfoPOMap.size());
        }

        Map<String, String> stocks = new HashMap<>();
        for (String stockID : stringStockInfoPOMap.keySet()) {
            stocks.put(stockID, stringStockInfoPOMap.get(stockID).getStockName());
        }
        return stocks;
    }

    private Map<String, StockBasicInfoPO> filterBasicInfo(Map<BasicIndices, double[]>
                                                                  map, Map<String, StockBasicInfoPO> stockBasicInfoPOMap, BasicIndices basicIndices) {
        if (map.get(basicIndices) != null) {
            List<String> finishKeys = new ArrayList<>();
            for (String stockID : stockBasicInfoPOMap.keySet()) {
                double value = 0;
                if (basicIndices == BasicIndices.adratio && stockBasicInfoPOMap.get(stockID).getAdRatio() != null) {
                    value = stockBasicInfoPOMap.get(stockID).getAdRatio();
                }
                if (basicIndices == BasicIndices.arturnover && stockBasicInfoPOMap.get(stockID).getArturnover() != null) {
                    value = stockBasicInfoPOMap.get(stockID).getArturnover();
                }
                if (basicIndices == BasicIndices.cashflowratio && stockBasicInfoPOMap.get(stockID).getCashflowRatio() != null) {
                    value = stockBasicInfoPOMap.get(stockID).getCashflowRatio();
                }
                if (basicIndices == BasicIndices.cashratio && stockBasicInfoPOMap.get(stockID).getCashRatio() != null) {
                    value = stockBasicInfoPOMap.get(stockID).getCashRatio();
                }
                if (basicIndices == BasicIndices.cf_liabilities && stockBasicInfoPOMap.get(stockID).getCfLiabilities() != null) {
                    value = stockBasicInfoPOMap.get(stockID).getCfLiabilities();
                }
                if (basicIndices == BasicIndices.cf_nm && stockBasicInfoPOMap.get(stockID).getCfNm() != null) {
                    value = stockBasicInfoPOMap.get(stockID).getCfNm();
                }
                if (basicIndices == BasicIndices.cf_sales && stockBasicInfoPOMap.get(stockID).getCfSales() != null) {
                    value = stockBasicInfoPOMap.get(stockID).getCfSales();
                }
                if (basicIndices == BasicIndices.roe && stockBasicInfoPOMap.get(stockID).getRoe() != null) {
                    value = stockBasicInfoPOMap.get(stockID).getRoe();
                }
                if (basicIndices == BasicIndices.net_profit_ratio && stockBasicInfoPOMap.get(stockID).getNetProfitRatio() != null) {
                    value = stockBasicInfoPOMap.get(stockID).getNetProfitRatio();
                }
                if (basicIndices == BasicIndices.gross_profit_rate && stockBasicInfoPOMap.get(stockID).getGrossProfitRate() != null) {
                    value = stockBasicInfoPOMap.get(stockID).getGrossProfitRate();
                }
                if (basicIndices == BasicIndices.inventory_turnover && stockBasicInfoPOMap.get(stockID).getInventoryTurnover() != null) {
                    value = stockBasicInfoPOMap.get(stockID).getInventoryTurnover();
                }
                if (basicIndices == BasicIndices.mbrg && stockBasicInfoPOMap.get(stockID).getMbrg() != null) {
                    value = stockBasicInfoPOMap.get(stockID).getMbrg();
                }
                if (basicIndices == BasicIndices.nprg && stockBasicInfoPOMap.get(stockID).getNprg() != null) {
                    value = stockBasicInfoPOMap.get(stockID).getNprg();
                }
                if (basicIndices == BasicIndices.nav && stockBasicInfoPOMap.get(stockID).getNav() != null) {
                    value = stockBasicInfoPOMap.get(stockID).getNav();
                }
                if (basicIndices == BasicIndices.targ && stockBasicInfoPOMap.get(stockID).getTarg() != null) {
                    value = stockBasicInfoPOMap.get(stockID).getTarg();
                }
                if (basicIndices == BasicIndices.epsg && stockBasicInfoPOMap.get(stockID).getEpsg() != null) {
                    value = stockBasicInfoPOMap.get(stockID).getEpsg();
                }
                if (basicIndices == BasicIndices.currentratio && stockBasicInfoPOMap.get(stockID).getCurrentRatio() != null) {
                    value = stockBasicInfoPOMap.get(stockID).getCurrentRatio();
                }
                if (basicIndices == BasicIndices.quickratio && stockBasicInfoPOMap.get(stockID).getQuickRatio() != null) {
                    value = stockBasicInfoPOMap.get(stockID).getQuickRatio();
                }
                if (basicIndices == BasicIndices.sheqratio && stockBasicInfoPOMap.get(stockID).getSheqRatio() != null) {
                    value = stockBasicInfoPOMap.get(stockID).getSheqRatio();
                }
                if (basicIndices == BasicIndices.rateofreturn && stockBasicInfoPOMap.get(stockID).getRateOfReturn() != null) {
                    value = stockBasicInfoPOMap.get(stockID).getRateOfReturn();
                }
                double[] range = map.get(basicIndices);
                if (value < range[0] || value > range[1] || value == 0) {
                    System.out.println(stockID + "被筛");
                    finishKeys.add(stockID + " " + value);
                }
            }
            for (int i = 0; i < finishKeys.size(); i++) {
                stockBasicInfoPOMap.remove(finishKeys.get(i));
            }
        }
        return stockBasicInfoPOMap;
    }

    private Map<String, StockInfoPO> filterStockInfo(Map<BasicIndices, double[]>
                                                             map, Map<String, StockInfoPO> stockBasicInfoPOMap, BasicIndices basicIndices) {
        if (map.get(basicIndices) != null) {
            List<String> finishKeys = new ArrayList<>();
            for (String stockID : stockBasicInfoPOMap.keySet()) {
                double value = 0;
                if (basicIndices == BasicIndices.pe && stockBasicInfoPOMap.get(stockID).getPe() != null) {
                    value = stockBasicInfoPOMap.get(stockID).getPe();
                }
                if (basicIndices == BasicIndices.pb && stockBasicInfoPOMap.get(stockID).getPb() != null) {
                    value = stockBasicInfoPOMap.get(stockID).getPb();
                }
                if (basicIndices == BasicIndices.rev && stockBasicInfoPOMap.get(stockID).getRev() != null) {
                    value = stockBasicInfoPOMap.get(stockID).getRev();
                }
                if (basicIndices == BasicIndices.profit && stockBasicInfoPOMap.get(stockID).getProfit() != null) {
                    value = stockBasicInfoPOMap.get(stockID).getProfit();
                }
                if (basicIndices == BasicIndices.npr && stockBasicInfoPOMap.get(stockID).getNpr() != null) {
                    value = stockBasicInfoPOMap.get(stockID).getNpr();
                }
                double[] range = map.get(basicIndices);
                if (value < range[0] || value > range[1] || value == 0)
                    finishKeys.add(stockID);
            }
            for (int i = 0; i < finishKeys.size(); i++) {
                stockBasicInfoPOMap.remove(finishKeys.get(i));
            }
        }
        return stockBasicInfoPOMap;
    }

    /**
     * 根据股票号和日期获得前n个交易日的股票信息
     *
     * @param date
     * @param num
     * @param stockCode
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public Map<String, List<StockPO>> getStocksOfPreDates(Date date, int num, String... stockCode) {
        Map<String, List<StockPO>> stocks = new HashMap<>();

        int maxThreadCount = 50;
        int threadCount = 0;
        List<Thread> threads = new ArrayList<>();
        List<Integer> deadThreads = new ArrayList<>();
        for (int i = 0; i < stockCode.length; i++) {
            final int count = i;
            if (threadCount < maxThreadCount) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String stockId = stockCode[count].split(";")[0];
                        System.out.println("正在取" + stockId + "的前" + num + "天数据");
//                        System.out.println(stockId);
                        List<StockPO> stockPOS = (List<StockPO>) getHibernateTemplate().find("from oquantour.po.StockPO where stockId = ? and (DateDiff(dateValue, ?) < 0)", new Object[]{stockId, date});
                        //Collections.sort(stockPOS);
                        int size = stockPOS.size();
                        for (int j = 0; j < size - num; j++) {
                            getHibernateTemplate().evict(stockPOS.get(0));
                            stockPOS.remove(0);
                        }
                        for (int j = 0; j < stockPOS.size(); j++) {
                            getHibernateTemplate().evict(stockPOS.get(j));
                        }
                        stocks.put(stockId, stockPOS);
                    }
                });
                threads.add(thread);
                thread.run();
                threadCount++;
            } else {
                for (int j = 0; j < maxThreadCount; j++) {
                    if (!threads.get(j).isAlive()) {
                        threadCount--;
                        deadThreads.add(j);
                    }
                }
                for (int j = deadThreads.size() - 1; j >= 0; j--) {
                    threads.remove(threads.get(deadThreads.get(j)));
                }
                deadThreads.clear();
                i--;
            }
        }
        /*
        String stockId = "";
        List<StockPO> stockPOS = new ArrayList<>();
        for(int i = 0; i < stockCode.length; i++){
            stockId = stockCode[i];
            stockPOS = (List<StockPO>) this.getHibernateTemplate().find("from oquantour.po.StockPO where stockId = ? and (DateDiff(dateValue, ?) < 0)", new Object[]{stockId, date});
            //Collections.sort(stockPOS);
            int size = stockPOS.size();
            for(int j = 0; j < size - num; j++){
                stockPOS.remove(0);
            }
            stocks.put(stockId, stockPOS);
        }
        */
        return stocks;
    }

    /**
     * 获得某交易日前num日的所有股票数据
     *
     * @param date
     * @param num
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public Map<String, List<StockPO>> getStocksOfPreDates(Date date, int num) {
        Map<String, List<StockPO>> stocks = new HashMap<>();
        List<String> stockCode = getAllStockCodeAndName();

        int maxThreadCount = 5;
        int threadCount = 0;
        List<Thread> threads = new ArrayList<>();
        List<Integer> deadThreads = new ArrayList<>();
        for (int i = 0; i < stockCode.size(); i++) {
            final int count = i;
            if (threadCount < maxThreadCount) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String stockId = stockCode.get(count).split(";")[0];
                        System.out.println("正在取" + stockId + "的前" + num + "天数据");
                        List<StockPO> stockPOS = (List<StockPO>) getHibernateTemplate().find("from oquantour.po.StockPO where stockId = ? and (DateDiff(dateValue, ?) < 0)", new Object[]{stockId, date});
                        //Collections.sort(stockPOS);
                        int size = stockPOS.size();
                        for (int j = 0; j < size - num; j++) {
                            getHibernateTemplate().evict(stockPOS.get(0));
                            stockPOS.remove(0);
                        }
                        for (int j = 0; j < stockPOS.size(); j++) {
                            getHibernateTemplate().evict(stockPOS.get(j));
                        }
                        stocks.put(stockId, stockPOS);
                    }
                });
                threads.add(thread);
                thread.run();
                threadCount++;
            } else {
                for (int j = 0; j < maxThreadCount; j++) {
                    if (!threads.get(j).isAlive()) {
                        threadCount--;
                        deadThreads.add(j);
                    }
                }
                for (int j = deadThreads.size() - 1; j >= 0; j--) {
                    threads.remove(deadThreads.get(j));
                }
                i--;
            }

            /*
            stockId = stockCode.get(i).split(";")[0];
            stockPOS = (List<StockPO>) this.getHibernateTemplate().find("from oquantour.po.StockPO where stockId = ? and (DateDiff(dateValue, ?) < 0)", new Object[]{stockId, date});
            //Collections.sort(stockPOS);
            int size = stockPOS.size();
            for(int j = 0; j < size - num; j++){
                stockPOS.remove(0);
            }
            stocks.put(stockId, stockPOS);
*/
        }
        return stocks;
    }

    /**
     * 根据传入的日期判断该日是否为该股票的交易日
     *
     * @param date
     * @param stockCode
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public boolean isTransactionDay(Date date, String stockCode) {
        List<StockPO> stockPOS = (List<StockPO>) this.getHibernateTemplate().find("from oquantour.po.StockPO s where s.stockId = ? and s.dateValue = ?", new Object[]{stockCode, date});
        for (int i = 0; i < stockPOS.size(); i++) {
            this.getHibernateTemplate().evict(stockPOS.get(i));
        }
        if (stockPOS.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 判断该日是否为交易日
     *
     * @param date
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public boolean isTransactionDay(Date date) {
        List<Date> dates = (List<Date>) this.getHibernateTemplate().find("select distinct dateValue from oquantour.po.StockPO");
        if (dates.contains(date))
            return true;
        else
            return false;
    }

    /**
     * 获得一只股票所有交易日的信息
     *
     * @param stockID
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public List<StockPO> getAllInfoOfOneStock(String stockID) {
        //String queryString = "select  from bean.StockPO s where s.StockID=:stockID";

        String paramName = "stockID";

        String value = stockID;

        List<StockPO> stockPOS = (List<StockPO>) this.getHibernateTemplate().find("from oquantour.po.StockPO where stockId = ?", stockID);
        for (int i = 0; i < stockPOS.size(); i++) {
            getHibernateTemplate().evict(stockPOS.get(i));
            if (i > 1 && i < 8 && stockPOS.size() > 8) {
                stockPOS.get(i).setAdxr((stockPOS.get(i).getAdx() + stockPOS.get(1).getAdx()) / 2);
            }
            if (i > 7) {
                stockPOS.get(i).setAdxr((stockPOS.get(i).getAdx() + stockPOS.get(i - 7).getAdx()) / 2);
            }
        }
        /*
        for(int i = 0; i < stockPOS.size(); i++){
            System.out.println(stockPOS.get(i).getAdxr());
        }*/
        return stockPOS;
    }

    /**
     * 获得某行业在某段时间内所有的股票数据
     *
     * @param industry
     * @param startDate
     * @param endDate
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public Map<String, List<StockPO>> getStockInfoByIndustryAndDate(String industry, Date startDate, Date endDate) {
        List<String> stockIDs = (List<String>) this.getHibernateTemplate().find("select stockId from oquantour.po.StockInfoPO where industry = ?", industry);
        Map<String, List<StockPO>> stocks = new HashMap<>();
        for (int i = 0; i < stockIDs.size(); i++) {
            String id = stockIDs.get(i);
            stocks.put(id, searchStock(id, startDate, endDate));
        }
        return stocks;
    }

    @Transactional(readOnly = false)
    @Override
    public void updateDailyStockInfo(String date) {
        List<String> stockIDs = getAllStockCodeAndName();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println(date);
        DailyInfoGetter dailyInfoGetter = new DailyInfoGetter();
//        dailyInfoGetter.getDailyInfo(stockIDs, date);
        Map<String, StockPO> dailyInfos = dailyInfoGetter.getDailyInfo(stockIDs, date);
        System.out.println(dailyInfos.size());

        int finishCount = 0;
        int threadCount = 0;
        int maxCount = 50;
        List<Thread> threads = new ArrayList<>();
        List<Integer> deadThreads = new ArrayList<>();

        List<String> stockIDss = new ArrayList<>();
        for (String stockID : dailyInfos.keySet()) {
            stockIDss.add(stockID);
        }

        List<StockPO> result = new ArrayList<>();

        for (int k = 0; k < stockIDss.size(); k++) {
            final String stockID = stockIDss.get(k);
            if (threadCount < maxCount) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
//                        try {
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        Map<String, List<StockPO>> preInfos = getStocksOfPreDates(dailyInfos.get(stockID).getDateValue(), 239, stockID);
                        getHibernateTemplate().clear();
                        List<StockPO> stockPOS = preInfos.get(stockID);
                        StockPO stockPO = dailyInfos.get(stockID);
                        stockPOS.add(stockPO);
                        if (stockPOS.size() == 240) {
                            stockPO.setMa5(maValue(stockPOS, 5));
                            stockPO.setMa10(maValue(stockPOS, 10));
                            stockPO.setMa20(maValue(stockPOS, 20));
                            stockPO.setMa30(maValue(stockPOS, 30));
                            stockPO.setMa60(maValue(stockPOS, 60));
                            stockPO.setMa120(maValue(stockPOS, 120));
                            stockPO.setMa240(maValue(stockPOS, 240));

                            if (stockPO.getHighPrice().doubleValue() != stockPO.getLowPrice().doubleValue())
                                stockPO.setRsv(((stockPO.getClosePrice() - stockPO.getLowPrice()) / (stockPO.getHighPrice() - stockPO.getLowPrice())) * 100);
                            else
                                stockPO.setRsv((double) (0));

                            int today = stockPOS.size() - 1;
                            stockPO.setReturnRate(Math.log(stockPOS.get(today).getAdjClose() / stockPOS.get(today - 1).getAdjClose()));
                            stockPO.setChg((stockPOS.get(today).getAdjClose() - stockPOS.get(today - 1).getAdjClose()) / stockPOS.get(today - 1).getAdjClose());

                            //计算kdj
                            stockPO.setkValue((2 * stockPOS.get(today - 1).getkValue() + stockPO.getRsv()) / 3);
                            stockPO.setdValue((2 * stockPOS.get(today - 1).getdValue() + stockPO.getkValue()) / 3);
                            stockPO.setjValue(3 * stockPO.getkValue() - 2 * stockPO.getdValue());

                            //计算macd
                            stockPO.setEma12((11 * stockPOS.get(today - 1).getEma12() + 2 * stockPO.getClosePrice()) / 13);
                            stockPO.setEma26((25 * stockPOS.get(today - 1).getEma26() + 2 * stockPO.getClosePrice()) / 27);
                            stockPO.setDif(stockPO.getEma12() - stockPO.getEma26());
                            stockPO.setDea((8 * stockPOS.get(today - 1).getDea() + 2 * stockPO.getDif()) / 10);

                            //计算rsi
                            double up = 0;
                            double down = 0;
                            double temp = 0;
                            for (int j = today; j > today - 13; j--) {
                                temp = stockPOS.get(j).getClosePrice() - stockPOS.get(j - 1).getClosePrice();
                                if (temp > 0)
                                    up += temp;
                                else
                                    down += temp;
                            }
                            if (down > 0)
                                temp = 1 + up / down;
                            else
                                temp = 1 + up;
                            stockPO.setRsi(100 - (100 / temp));


                            //计算dmi
                            StockPO lastDay = stockPOS.get(today - 1);
                            double dmp = stockPO.getHighPrice() - lastDay.getHighPrice();
                            double dmm = lastDay.getLowPrice() - stockPO.getLowPrice();
                            if (dmp <= 0)
                                dmp = 0;
                            if (dmm <= 0)
                                dmm = 0;
                            if (dmm <= dmp)
                                dmm = 0;
                            else
                                dmp = 0;
                            stockPO.setDmPlus(dmp);
                            stockPO.setDmMinus(dmm);

                            double tr = stockPO.getHighPrice() - stockPO.getLowPrice();
                            temp = Math.abs(stockPO.getHighPrice() - lastDay.getClosePrice());
                            if (tr < temp)
                                tr = temp;
                            temp = Math.abs(stockPO.getLowPrice() - lastDay.getClosePrice());
                            if (tr < temp)
                                tr = temp;
                            stockPO.setTr(tr);

                            double dmp12 = 0;
                            double dmm12 = 0;
                            double tr12 = 0;

                            for (int j = today; j > today - 12; j--) {
                                dmp12 += stockPOS.get(j).getDmPlus();
                                dmm12 += stockPOS.get(j).getDmMinus();
                                tr12 += stockPOS.get(j).getTr();
                            }
                            if (tr12 != 0) {
                                stockPO.setDiMinus((dmm12 / tr12) * 100);
                                stockPO.setDiPlus((dmp12 / tr12) * 100);
                            } else {
                                stockPO.setDiMinus((double) (0));
                                stockPO.setDiPlus((double) (0));
                            }
                            double diDif = Math.abs(stockPO.getDiPlus() - stockPO.getDiMinus());
                            double diSum = Math.abs(stockPO.getDiPlus() + stockPO.getDiMinus());
                            double dx = 0;
                            if (diSum != 0)
                                dx = (diDif / diSum) * 100;

                            stockPO.setDx(dx);
                            double adx = 0;
                            for (int j = today; j > today - 12; j--) {
                                adx += stockPOS.get(j).getDx();
                            }
                            stockPO.setAdx(adx / 12);
                            double adxr = 0;
                            for (int j = today; j > today - 12; j--) {
                                adxr += stockPOS.get(j).getAdx();
                            }
                            stockPO.setAdxr(adxr / 2);

                            double avg = maValue(stockPOS, 20);
                            double sum = 0;
                            double md = 0;
                            for (int k = today; k > today - 20; k--) {
                                sum += Math.pow(stockPOS.get(k).getClosePrice() - avg, 2);
                            }
                            md = Math.sqrt(sum);
                            double mb = maValue(stockPOS, 19);
                            stockPO.setMb(mb);
                            double upValue = mb + 2 * md;
                            stockPO.setUp(upValue);
                            double dnValue = mb - 2 * md;
                            stockPO.setDn(dnValue);

                            result.add(stockPO);
                        }
                        System.out.println("更新" + stockID + "长度：" + stockPOS.size());
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
                    }
                });
                threads.add(thread);
                threadCount++;
                thread.start();
            } else {
                for (int j = 0; j < maxCount; j++) {
                    if (!threads.get(j).isAlive()) {
                        threadCount--;
                        deadThreads.add(j);
                        finishCount++;
                        System.out.println(finishCount);
                    }
                }
                for (int j = deadThreads.size() - 1; j >= 0; j--) {
                    threads.remove(threads.get(deadThreads.get(j)));
                }
                deadThreads.clear();
                k--;
            }
        }
        while (threadCount > 0) {
            System.out.println(finishCount + " " + result.size());
            for (int j = 0; j < maxCount && j < threads.size(); j++) {
                if (!threads.get(j).isAlive()) {
                    threadCount--;
                    deadThreads.add(j);
                    finishCount++;
                }
            }
            for (int j = deadThreads.size() - 1; j >= 0; j--) {
                threads.remove(threads.get(deadThreads.get(j)));
            }
            deadThreads.clear();
        }


        for (int i = 0; i < result.size(); i++) {
            add(result.get(i));
            System.out.println(result.get(i).getStockId());
        }


    }

    private double maValue(List<StockPO> stockPOS, int matype) {
        double sum = 0;
        for (int i = stockPOS.size() - 1; i >= stockPOS.size() - matype; i--) {
//            System.out.println(i);
            if (stockPOS.get(i).getClosePrice() != null)
                sum += stockPOS.get(i).getClosePrice();
            else
                matype--;
        }
        return sum / matype;
    }

    /**
     * 获得所有st股票代码
     *
     * @return
     */
    @Transactional(readOnly = true)
    public List<String> getAllStStocks() {
        List<String> strings = getAllStockCodeAndName();
        List<String> stStocks = new ArrayList<>();
        for (int i = 0; i < strings.size(); i++) {
            if (strings.get(i).split(";")[1].contains("S") && strings.get(i).split(";")[1].contains("T"))
                stStocks.add(strings.get(i).split(";")[0]);
        }
        return stStocks;
    }

    /**
     * 更新股票信息
     *
     * @param stockPO
     */
    @Override
    public void update(StockPO stockPO) {
        this.getHibernateTemplate().update(stockPO);
    }

    /**
     * 删除股票信息
     *
     * @param stockPO
     */
    @Override
    public void delete(StockPO stockPO) {
        this.getHibernateTemplate().delete(stockPO);
    }

    /**
     * 增加股票信息
     *
     * @param stockPO
     */
    @Transactional(readOnly = false)
    @Override
    public void add(StockPO stockPO) {
        this.getHibernateTemplate().saveOrUpdate(stockPO);
    }

    /**
     * 添加股票基本信息
     *
     * @param stockInfoPO
     */
    @Override
    public void addStockInfo(StockInfoPO stockInfoPO) {
        this.getHibernateTemplate().saveOrUpdate(stockInfoPO);
    }

    /**
     * 筛选无数据股票
     */
    @Transactional(readOnly = false)
    @Override
    public void filterAllStocks() {
        List<String> allStocks = (List<String>) this.getHibernateTemplate().find("select distinct stockId from oquantour.po.StockPO");
        List<StockInfoPO> basicStocks = (List<StockInfoPO>) this.getHibernateTemplate().find("from oquantour.po.StockInfoPO");
        for (int i = 0; i < basicStocks.size(); i++) {
            if (!allStocks.contains(basicStocks.get(i).getStockId())) {
                this.getHibernateTemplate().delete(basicStocks.get(i));
            }
        }

    }

    /**
     * 获得所有股票数据
     */
    @Transactional(readOnly = true)
    @Override
    public void getAllStockInfos() {
        long start = System.currentTimeMillis();
        List<String> stockInfos = getAllStockCodeAndName();

        int finishCount = 0;
        int threadCount = 0;
        int maxThreadCount = 200;
        List<Thread> threads = new ArrayList<>();
        List<Integer> deadThreads = new ArrayList<>();
        for (int i = 0; i < stockInfos.size(); i++) {
            final int count = i;
            if (threadCount < maxThreadCount) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String stockID = stockInfos.get(count).split(";")[0];
//                        stockMap.put(stockID, getAllInfoOfOneStock(stockID));
                        System.out.println(stockID);
                    }
                });
                threadCount++;
                threads.add(thread);
                thread.start();
            } else {
                for (int j = 0; j < maxThreadCount; j++) {
                    if (!threads.get(j).isAlive()) {
                        threadCount--;
                        deadThreads.add(j);
                        finishCount++;
                    }
                }
                for (int j = deadThreads.size() - 1; j >= 0; j--) {
                    threads.remove(threads.get(deadThreads.get(j)));
                }
                deadThreads.clear();
                i--;
            }
        }
        System.out.println(System.currentTimeMillis() - start);

    }

    public void test() {
        try {
            String sql = "SELECT * INTO OUTFILE 'data.txt' FIELDS TERMINATED BY ',' FROM stockpo;";
            System.out.println(System.currentTimeMillis());
            PreparedStatement ps = SessionFactoryUtils.getDataSource(sessionFactory).getConnection()
                    .prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            List list = new ArrayList();
            int count = 0;
//            while (rs.next()) {
//                count++;
//                list.add(new Long(rs.getLong(1)));
//            }
            System.out.println(count);
            rs.close();
            ps.close();
            System.out.println(System.currentTimeMillis());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
