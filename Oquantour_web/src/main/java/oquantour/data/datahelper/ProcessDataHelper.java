package oquantour.data.datahelper;


import oquantour.data.dao.StockDao;
import oquantour.po.StockInfoPO;
import oquantour.po.StockPO;
import oquantour.util.tools.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.sql.Date;
import java.util.List;


/**
 * Created by island on 5/3/17.
 */
public class ProcessDataHelper {
    static ClassPathXmlApplicationContext ctx;

    StockDao stockDao;
    List<String> type1 = readTxt("/Users/island/IdeaProjects/Oquantour/datasource/plate/中小板.txt");
    List<String> type2 = readTxt("/Users/island/IdeaProjects/Oquantour/datasource/plate/创业板.txt");
    List<String> type3 = readTxt("/Users/island/IdeaProjects/Oquantour/datasource/plate/深市主板.txt");

    public ProcessDataHelper(){
        ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        stockDao = (StockDao)ctx.getBean("stockDao");
    }

    private List<String> readTxt(String path){
        File file = new File(path);

        if (!file.exists()) {
            return new ArrayList<>();
        }

        BufferedReader bufferedReader = null;
        List<String> s = new ArrayList<>();

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String str = null;
            while ((str = bufferedReader.readLine()) != null)
                s.add(str);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return s;
    }

    public void addStockNameInfo(){
        String path = "/Users/island/PycharmProjects/StockDate/basicInfo.txt";
        List<String> s = readTxt(path);
        List<StockInfoPO> stockInfoPOS = new ArrayList<>();
        for(int i = 1; i < s.size(); i++){
            String[] info = s.get(i).split(",");
            StockInfoPO stockInfoPO = new StockInfoPO();
            stockInfoPO.setSearchCount(0);
            stockInfoPO.setStockId(info[0]);
            stockInfoPO.setStockName(info[1]);
            stockInfoPO.setIndustry(info[2]);
            stockInfoPO.setArea(info[3]);
            stockInfoPO.setPe(Double.parseDouble(info[4]));
            stockInfoPO.setOutstanding(Double.parseDouble(info[5]));
            stockInfoPO.setTotals(Double.parseDouble(info[6]));
            stockInfoPO.setTotalAssets(Double.parseDouble(info[7]));
            stockInfoPO.setLiquidAssets(Double.parseDouble(info[8]));
            stockInfoPO.setFixedAssets(Double.parseDouble(info[9]));
            stockInfoPO.setReserved(Double.parseDouble(info[10]));
            stockInfoPO.setReservedPerShare(Double.parseDouble(info[11]));
            stockInfoPO.setEsp(Double.parseDouble(info[12]));
            stockInfoPO.setBvps(Double.parseDouble(info[13]));
            stockInfoPO.setPb(Double.parseDouble(info[14]));
            stockInfoPO.setTimeToMarket(info[15]);
            stockInfoPO.setUndp(Double.parseDouble(info[16]));
            stockInfoPO.setPerundp(Double.parseDouble(info[17]));
            stockInfoPO.setRev(Double.parseDouble(info[18]));
            stockInfoPO.setProfit(Double.parseDouble(info[19]));
            stockInfoPO.setGpr(Double.parseDouble(info[20]));
            stockInfoPO.setNpr(Double.parseDouble(info[21]));
            stockInfoPO.setHolders(Integer.parseInt(info[22]));
            if(stockInfoPO.getStockId().startsWith("60")){
                stockInfoPO.setPlate("沪市A股");
                stockInfoPOS.add(stockInfoPO);
            }
            if(stockInfoPO.getStockId().startsWith("900")){
                stockInfoPO.setPlate("沪市B股");
                stockInfoPOS.add(stockInfoPO);
            }
            if(stockInfoPO.getStockId().startsWith("000") || stockInfoPO.getStockId().startsWith("001")){
                stockInfoPO.setPlate("深市A股");
                stockInfoPOS.add(stockInfoPO);
            }
            if(stockInfoPO.getStockId().startsWith("200")){
                stockInfoPO.setPlate("深市B股");
                stockInfoPOS.add(stockInfoPO);
            }
            if(stockInfoPO.getStockId().startsWith("002")){
                stockInfoPO.setPlate("中小板");
                stockInfoPOS.add(stockInfoPO);
            }
            if(stockInfoPO.getStockId().startsWith("300")){
                stockInfoPO.setPlate("创业板");
                stockInfoPOS.add(stockInfoPO);
            }
        }
        for(int i = 0; i < stockInfoPOS.size(); i++){
            stockDao.addStockInfo(stockInfoPOS.get(i));
            System.out.println(stockInfoPOS.get(i).getStockId());
        }
    }

    public String definePlateName(String code){
        return null;
    }

    public void addStockInfo(String path){
        List<String> s = readTxt(path);
        List<StockPO> stockPOS = new ArrayList<>();
        for(int i = 0; i < s.size(); i++){
            StockPO stockPO = encapsulateStock(s.get(i));
            stockPOS.add(stockPO);
        }
        StockPO stockPO;
        for(int i = 0; i < stockPOS.size(); i++){
            stockPO = stockPOS.get(i);
            /*
            for(int j = 0; j < type1.size(); j++){
                if(stockPO.getStockId().equals(type1.get(j).split(";")[0])){
                    stockPO.setPlate("中小板");
                }
            }
            for(int j = 0; j < type2.size(); j++){
                if(stockPO.getStockId().equals(type2.get(j).split(";")[0])){
                    stockPO.setPlate("创业板");
                }
            }
            for(int j = 0; j < type3.size(); j++){
                if(stockPO.getStockId().equals(type3.get(j).split(";")[0])){
                    stockPO.setPlate("深市主板");
                }
            }
            */
            System.out.println(stockPOS.get(i).getStockId() + " " + stockPOS.get(i).getDateValue());

            stockDao.add(stockPOS.get(i));
        }
    }


    private List<StockPO> calChg( List<StockPO> stockPOS){
        StockPO stockPO;
        for(int i = stockPOS.size() - 1; i > 0; i--) {
            stockPO = stockPOS.get(i);

        }
        return stockPOS;
    }



    private double getChg(int position, List<StockPO> stockPOS){
        return 0;
    }

    public StockPO encapsulateStock(String stockInfo) {
        String[] attributes = stockInfo.split(";");
        String[] infos = new String[attributes.length];

        if (attributes.length != 11) {
            return null;
        }

        try {
            StockPO stockPO = new StockPO();

            int i = Integer.parseInt(attributes[0]);

            Date sqlDate = str2Date(attributes[1]);
            stockPO.setDateValue(sqlDate);

            double open = Double.parseDouble(attributes[2]);
            stockPO.setOpenPrice(open);

            double high = Double.parseDouble(attributes[3]);
            stockPO.setHighPrice(high);

            double low = Double.parseDouble(attributes[4]);
            stockPO.setLowPrice(low);

            double close = Double.parseDouble(attributes[5]);
            stockPO.setClosePrice(close);

            int volume = Integer.parseInt(attributes[6]);
            //stockPO.setVolume(volume);

            double adjClose = Double.parseDouble(attributes[7]);
            stockPO.setAdjClose(adjClose);

            String code = attributes[8];
            stockPO.setStockId(code);

            String name = attributes[9];

            String market = attributes[10];

            stockPO.setMa5((double)(-1));
            stockPO.setMa10((double)(-1));
            stockPO.setMa20((double)(-1));
            stockPO.setMa30((double)(-1));
            stockPO.setMa60((double)(-1));
            stockPO.setMa120((double)(-1));
            stockPO.setMa240((double)(-1));
            stockPO.setChg((double)(-1));
            stockPO.setReturnRate((double)(-1));

            return stockPO;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public StockPO encapsulateStock1(String stockInfo) {
        StockPO stockPO = new StockPO();
        String[] attributes = stockInfo.split(",");
        String[] infos = new String[attributes.length];
        for(int i = 0; i < attributes.length - 1; i++){
            infos[i] = attributes[i].split(":")[1];
            System.out.println(infos[i]);
        }
        String[] dates = attributes[attributes.length - 1].split(" ");
        System.out.print(dates[1]);
        System.out.print(dates[2]);
        System.out.println(dates[5].substring(0, 4));

        stockPO.setMa5((double)(-1));
        stockPO.setMa10((double)(-1));
        stockPO.setMa20((double)(-1));
        stockPO.setMa30((double)(-1));
        stockPO.setMa60((double)(-1));
        stockPO.setMa120((double)(-1));
        stockPO.setMa240((double)(-1));

        return new StockPO();
    }

        public boolean addStock(StockPO stockPO){
        Session session = null;
        try {
            session = HibernateUtil.getSession();
            session.beginTransaction();

            session.save(stockPO);
            return true;
        } catch (HibernateException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (null != session) {
                session.getTransaction().commit();
                HibernateUtil.closeSession(session);
            }
        }
    }

    /**
     * 将日期格式字符串转为Date对象
     *
     * @param str 日期格式的字符串
     * @return 对应的Date对象
     */
    public Date str2Date(String str) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yy");
             Date sqlDate = new java.sql.Date(simpleDateFormat.parse(str).getTime());
                System.out.println(sqlDate);

            return sqlDate;
        } catch (ParseException e) {
            return null;
        }

    }

    /**
     * Get XML String of utf-8
     *
     * @return XML-Formed string
     */
    private String getUTF8XMLString(String xml) {

        try {
            byte[] b = xml.getBytes("gbk");//编码
            String sa = new String(b, "gbk");//解码:用什么字符集编码就用什么字符集解码

            b = sa.getBytes("utf-8");//编码
            sa = new String(b, "utf-8");//解码
            //System.out.println(sa.replace(" ", ""));

            return sa.replace(" ", "");
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }

    return null;
    }
}
