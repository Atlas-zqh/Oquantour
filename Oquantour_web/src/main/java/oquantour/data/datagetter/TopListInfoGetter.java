package oquantour.data.datagetter;

import oquantour.data.dao.StockDao;
import oquantour.po.StockRealTimePO;
import oquantour.po.TopListPO;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by island on 2017/6/12.
 */
public class TopListInfoGetter {

    PythonExecutor executor;

    Paramaters paramaters;

    public TopListInfoGetter() {
        executor = new PythonExecutor();
        paramaters = new Paramaters();
    }

    public List<TopListPO> getTopList(Date date){
        List<TopListPO> topListPOS = new ArrayList<>();
        Map<String, StockRealTimePO> stocks = new HashMap<>();
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");

        StockDao stockDao = (StockDao) ctx.getBean("stockDao");

        List<String> stockInfos = stockDao.getAllStockCodeAndName();
        List<String> stockIDs = new ArrayList<>();
        for(int i = 0; i < stockInfos.size(); i++){
            stockIDs.add(stockInfos.get(i).split(";")[0]);
        }
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String sDate = simpleDateFormat.format(date);
            executor.excute("TopList.py", new Object[]{paramaters.getPath() + "topList.csv", sDate});
            BufferedReader reader = paramaters.getBufferedReader("topList.csv");
            if (reader != null) {
                String tem = reader.readLine();
                while ((tem = reader.readLine()) != null) {
                    String[] args = tem.split(",");
                    TopListPO topListPO = new TopListPO();
                    topListPO.setStockId(args[1]);
                    topListPO.setStockName(args[2].replace(" ", ""));
                    if(isDouble(args[3]))
                        topListPO.setPchange(Double.parseDouble(args[3]));
                    if(isDouble(args[4]))
                        topListPO.setAmount(Double.parseDouble(args[4]));
                    if(isDouble(args[5]))
                        topListPO.setBuy(Double.parseDouble(args[5]));
                    if(isDouble(args[6]))
                        topListPO.setSell(Double.parseDouble(args[6]));
                    topListPO.setReason(args[7]);
                    if(isDouble(args[8]))
                        topListPO.setBratio(Double.parseDouble(args[8]));
                    if(isDouble(args[9]))
                        topListPO.setSratio(Double.parseDouble(args[9]));
                    topListPO.setDateValue(date);
                    if(stockIDs.contains(topListPO.getStockId()))
                        topListPOS.add(topListPO);
                }
            }
        }catch (IOException e1){
            e1.printStackTrace();
        }catch (InterruptedException e2){
            e2.printStackTrace();
        }
        return topListPOS;
    }

    boolean isDouble(String str)
    {
        try
        {
            Double.parseDouble(str);
            return true;
        }
        catch(NumberFormatException ex){}
        return false;
    }
}
