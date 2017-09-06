package oquantour.data.datagetter;

import oquantour.data.dao.BasicDao;
import oquantour.data.dao.StockDao;
import oquantour.data.daoImpl.StockDaoImpl;
import oquantour.po.StockBasicInfoPO;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by island on 2017/6/2.
 */
public class BasicInfoGetter {
    PythonExecutor executor;

    Paramaters paramaters;

    static ClassPathXmlApplicationContext ctx;

    StockDao stockDao;

    BasicDao basicDao;

    public BasicInfoGetter() {
        executor = new PythonExecutor();
        paramaters = new Paramaters();
    }


    public void getBasicInfo(){
        ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
//        stockDao = (StockDao)ctx.getBean("stockDao");
        basicDao = (BasicDao)ctx.getBean("basicDao");
        List<String> paths = new ArrayList<>();

            try {
                for(int j = 2005; j <= 2017; j++) {
                    List<Integer> season = new ArrayList<>();
                    season.add(1);
                    if(j < 2017){
                        season.add(2);
                        season.add(3);
                        season.add(4);
                    }
                    for(int k = 0; k < season.size(); k++) {
                        paths.clear();
                        String path = paramaters.getPath() + j + season.get(k);
                        paths.add(path + "report.csv");
                        paths.add(path + "profit.csv");
                        paths.add(path + "operation.csv");
                        paths.add(path + "growth.csv");
                        paths.add(path + "debtpaying.csv");
                        paths.add(path + "cashflow.csv");
                        executor.excute("BasicInfo.py", new Object[]{j, season.get(k).intValue(), paths.get(0), paths.get(1), paths.get(2), paths.get(3), paths.get(4), paths.get(5)});

                        Map<String, StockBasicInfoPO> map = new HashMap<>();
                        BufferedReader reader = paramaters.getBufferedReader( "" + j + season.get(k) + "report.csv");
                        if(reader != null) {
                            String tem = reader.readLine();
                            while ((tem = reader.readLine()) != null) {
                                String[] args = tem.split(",");
                                StockBasicInfoPO stockBasicInfoPO = new StockBasicInfoPO();
                                stockBasicInfoPO.setStockId(args[1]);
                                stockBasicInfoPO.setQuarterOfYear(j + "-" + season.get(k));
                                if(args.length>=10) {
                                    if (isDouble(args[5]))
                                        stockBasicInfoPO.setBvps(Double.parseDouble(args[5]));
                                    if (isDouble(args[8]))
                                        stockBasicInfoPO.setNetProfits(Double.parseDouble(args[8]));
                                    if (isDouble(args[9]))
                                        stockBasicInfoPO.setProfitsYoy(Double.parseDouble(args[9]));
                                }
                                map.put(args[1], stockBasicInfoPO);
                            }
                        }

                        BufferedReader reader1 = paramaters.getBufferedReader("" + j + season.get(k) + "profit.csv");
                        if(reader1 != null) {
                            String tem = reader1.readLine();
                            while ((tem = reader1.readLine()) != null) {
                                String[] args = tem.split(",");
                                StockBasicInfoPO stockBasicInfoPO;
                                if(map.get(args[1]) != null) {
                                    stockBasicInfoPO = map.get(args[1]);
                                    map.remove(args[1]);
                                }
                                else {
                                    stockBasicInfoPO = new StockBasicInfoPO();
                                    stockBasicInfoPO.setStockId(args[1]);
                                    stockBasicInfoPO.setQuarterOfYear(j + "-" + season.get(k));
                                }
                                if(args.length >= 10) {
                                    if (isDouble(args[3]))
                                        stockBasicInfoPO.setRoe(Double.parseDouble(args[3]));
                                    if (isDouble(args[4]))
                                        stockBasicInfoPO.setNetProfitRatio(Double.parseDouble(args[4]));
                                    if (isDouble(args[5]))
                                        stockBasicInfoPO.setGrossProfitRate(Double.parseDouble(args[5]));
                                    if (isDouble(args[6]))
                                        stockBasicInfoPO.setNetProfits(Double.parseDouble(args[6]));
                                    if (isDouble(args[7]))
                                        stockBasicInfoPO.setEsp(Double.parseDouble(args[7]));
                                    if (isDouble(args[8]))
                                        stockBasicInfoPO.setBusinessIncome(Double.parseDouble(args[8]));
                                    if (isDouble(args[9]))
                                        stockBasicInfoPO.setBips(Double.parseDouble(args[9]));
                                }
                                map.put(args[1], stockBasicInfoPO);


                            }
                        }

                        BufferedReader reader2 = paramaters.getBufferedReader("" + j + season.get(k) + "operation.csv");
                        if(reader2 != null) {
                            String tem = reader2.readLine();
                            while ((tem = reader2.readLine()) != null) {
                                String[] args = tem.split(",");
                                StockBasicInfoPO stockBasicInfoPO;
                                if (map.get(args[1]) != null) {
                                    stockBasicInfoPO = map.get(args[1]);
                                    map.remove(args[1]);
                                }
                                else {
                                    stockBasicInfoPO = new StockBasicInfoPO();
                                    stockBasicInfoPO.setStockId(args[1]);
                                    stockBasicInfoPO.setQuarterOfYear(j + "-" + season.get(k));
                                }
                                if(args.length >= 9) {
                                    if (isDouble(args[3]))
                                        stockBasicInfoPO.setArturnover(Double.parseDouble(args[3]));
                                    if (isDouble(args[4]))
                                        stockBasicInfoPO.setArturndays(Double.parseDouble(args[4]));
                                    if (isDouble(args[5]))
                                        stockBasicInfoPO.setInventoryTurnover(Double.parseDouble(args[5]));
                                    if (isDouble(args[6]))
                                        stockBasicInfoPO.setInventoryDays(Double.parseDouble(args[6]));
                                    if (isDouble(args[7]))
                                        stockBasicInfoPO.setCurrentassetTurnover(Double.parseDouble(args[7]));
                                    if (isDouble(args[8]))
                                        stockBasicInfoPO.setCurrentassetDays(Double.parseDouble(args[8]));
                                }
                                map.put(args[1], stockBasicInfoPO);

                            }
                        }


                        BufferedReader reader3 = paramaters.getBufferedReader("" + j + season.get(k) + "growth.csv");
                        if(reader3 != null) {
                            String tem = reader3.readLine();
                            while ((tem = reader3.readLine()) != null) {
                                String[] args = tem.split(",");
                                StockBasicInfoPO stockBasicInfoPO;
                                if (map.get(args[1]) != null) {
                                    stockBasicInfoPO = map.get(args[1]);
                                    map.remove(args[1]);
                                }
                                else {
                                    stockBasicInfoPO = new StockBasicInfoPO();
                                    stockBasicInfoPO.setStockId(args[1]);
                                    stockBasicInfoPO.setQuarterOfYear(j + "-" + season.get(k));
                                }

                                if(args.length >= 9) {
                                    if (isDouble(args[3]))
                                        stockBasicInfoPO.setMbrg(Double.parseDouble(args[3]));
                                    if (isDouble(args[4]))
                                        stockBasicInfoPO.setNprg(Double.parseDouble(args[4]));
                                    if (isDouble(args[5]))
                                        stockBasicInfoPO.setNav(Double.parseDouble(args[5]));
                                    if (isDouble(args[6]))
                                        stockBasicInfoPO.setTarg(Double.parseDouble(args[6]));
                                    if (isDouble(args[7]))
                                        stockBasicInfoPO.setEpsg(Double.parseDouble(args[7]));
                                    if (isDouble(args[8]))
                                        stockBasicInfoPO.setSeg(Double.parseDouble(args[8]));
                                }
                                map.put(args[1], stockBasicInfoPO);

                            }
                        }

                        BufferedReader reader4 = paramaters.getBufferedReader("" + j + season.get(k) + "debtpaying.csv");
                        if(reader4 != null) {
                            String tem = reader4.readLine();
                            while ((tem = reader4.readLine()) != null) {
                                String[] args = tem.split(",");
                                StockBasicInfoPO stockBasicInfoPO;
                                if (map.get(args[1]) != null) {
                                    stockBasicInfoPO = map.get(args[1]);
                                    map.remove(args[1]);
                                }
                                else {
                                    stockBasicInfoPO = new StockBasicInfoPO();
                                    stockBasicInfoPO.setStockId(args[1]);
                                    stockBasicInfoPO.setQuarterOfYear(j + "-" + season.get(k));
                                }

                                if(args.length >= 9) {
                                    if (isDouble(args[3]))
                                        stockBasicInfoPO.setCurrentRatio(Double.parseDouble(args[3]));
                                    if (isDouble(args[4]))
                                        stockBasicInfoPO.setQuickRatio(Double.parseDouble(args[4]));
                                    if (isDouble(args[5]))
                                        stockBasicInfoPO.setCashRatio(Double.parseDouble(args[5]));
                                    if (isDouble(args[6]))
                                        stockBasicInfoPO.setIcRatio(Double.parseDouble(args[6]));
                                    if (isDouble(args[7]))
                                        stockBasicInfoPO.setSheqRatio(Double.parseDouble(args[7]));
                                    if (isDouble(args[8]))
                                        stockBasicInfoPO.setAdRatio(Double.parseDouble(args[8]));
                                }
                                map.put(args[1], stockBasicInfoPO);
                            }
                        }

                        BufferedReader reader5 = paramaters.getBufferedReader("" + j + season.get(k) + "cashflow.csv");
                        if(reader5 != null) {
                            String tem = reader5.readLine();
                            while ((tem = reader5.readLine()) != null) {
                                String[] args = tem.split(",");
                                StockBasicInfoPO stockBasicInfoPO;
                                if (map.get(args[1]) != null) {
                                    stockBasicInfoPO = map.get(args[1]);
                                    map.remove(args[1]);
                                }
                                else {
                                    stockBasicInfoPO = new StockBasicInfoPO();
                                    stockBasicInfoPO.setStockId(args[1]);
                                    stockBasicInfoPO.setQuarterOfYear(j + "-" + season.get(k));
                                }

                                if(args.length >= 8) {
                                    if (isDouble(args[3]))
                                        stockBasicInfoPO.setCfSales(Double.parseDouble(args[3]));
                                    if (isDouble(args[4]))
                                        stockBasicInfoPO.setRateOfReturn(Double.parseDouble(args[4]));
                                    if (isDouble(args[5]))
                                        stockBasicInfoPO.setCfNm(Double.parseDouble(args[5]));
                                    if (isDouble(args[6]))
                                        stockBasicInfoPO.setCfLiabilities(Double.parseDouble(args[6]));
                                    if (isDouble(args[7]))
                                        stockBasicInfoPO.setCashflowRatio(Double.parseDouble(args[7]));
                                }
                                map.put(args[1], stockBasicInfoPO);
                            }
                        }
                        for (String key : map.keySet()) {
                            System.out.println(key);
                            basicDao.addBasicInfo(map.get(key));
                        }
                        map.clear();
                    }
                }
            }catch (InterruptedException e1){
                e1.printStackTrace();
            }catch (IOException e2){
                e2.printStackTrace();
            }

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
