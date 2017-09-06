package oquantour.data.datagetter;

import oquantour.data.dao.StockDao;
import oquantour.po.StockPO;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by island on 5/28/17.
 */
public class HistoryInfoGetter {

    PythonExecutor executor;

    Paramaters paramaters;

    static ClassPathXmlApplicationContext ctx;

    StockDao stockDao;

    public HistoryInfoGetter() {
        executor = new PythonExecutor();
        paramaters = new Paramaters();
    }

    public void addStockInfo() {
        ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        stockDao = (StockDao) ctx.getBean("stockDao");

        List<String> stocks = stockDao.getAllStockCodeAndName();
/*
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<StockPO> stockPOS = new ArrayList<>();
                    executor.excute("StockInfoNone.py", new Object[]{Paramaters.getPath() + stocks.get(0).split(";")[0]+ ".csv",  stocks.get(0).split(";")[0]});
                    executor.excute("StockInfo.py", new Object[]{Paramaters.getPath() + stocks.get(0).split(";")[0]+ "qfq" + ".csv",  stocks.get(0).split(";")[0], "qfq"});
                    BufferedReader reader = Paramaters.getBufferedReader(stocks.get(0).split(";")[0]+ ".csv");
                    BufferedReader reader1 = Paramaters.getBufferedReader(stocks.get(0).split(";")[0]+ "qfq" +  ".csv");
                    String tem = reader.readLine();
                    String tem1 = reader1.readLine();
                    while ((tem = reader.readLine()) != null && (tem1 = reader1.readLine()) != null) {
                        String[] args = tem.split(",");
                        String[] args1 = tem1.split(",");
                        StockPO stockPO = new StockPO();
                        stockPO.setStockId(stocks.get(0).split(";")[0]);
                        java.sql.Date date = new java.sql.Date(simpleDateFormat.parse(args[0]).getTime());
                        stockPO.setDateValue(date);
                        stockPO.setOpenPrice(Double.parseDouble(args[1]));
                        stockPO.setHighPrice(Double.parseDouble(args[2]));
                        stockPO.setLowPrice(Double.parseDouble(args[4]));
                        stockPO.setClosePrice(Double.parseDouble(args[3]));
                        stockPO.setVolume(Double.parseDouble(args[5]));
                        stockPO.setAmount(Double.parseDouble(args[6]));
                        stockPO.setAdjClose(Double.parseDouble(args1[3]));
                        stockPO.setMa5((double)(-1));
                        stockPO.setMa10((double)(-1));
                        stockPO.setMa20((double)(-1));
                        stockPO.setMa30((double)(-1));
                        stockPO.setMa60((double)(-1));
                        stockPO.setMa120((double)(-1));
                        stockPO.setMa240((double)(-1));
                        stockPO.setChg((double)(-1));
                        stockPO.setReturnRate((double)(-1));
                        stockPOS.add(stockPO);
                    }
                    calMA(stockPOS);

                }catch (IOException e1){
                    e1.printStackTrace();
                }catch (InterruptedException e2){
                    e2.printStackTrace();
                }catch (ParseException e3){
                    e3.printStackTrace();
                }
            }
        });
        thread.start();
*/

        int start = 0;
        List<Integer> wait = new ArrayList<>();

        try {
            String stockID = stocks.get(start).split(";")[0];
            executor.excute("StockInfoNone.py", new Object[]{paramaters.getPath() + stockID + ".csv", stockID});
            executor.excute("StockInfo.py", new Object[]{paramaters.getPath() + stockID + "qfq" + ".csv", stockID, "qfq"});
        }catch (IOException e1){
            e1.printStackTrace();
        }catch (InterruptedException e2){
            e2.printStackTrace();
        }
//        int threadCount = 0;
//        List<Thread> threads = new ArrayList<>();
//        List<Integer> finishThreads = new ArrayList<>();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i = start + 1; i < stocks.size(); i++) {
                    try {
                        String stockID = stocks.get(i).split(";")[0];
                        executor.excute("StockInfoNone.py", new Object[]{paramaters.getPath() + stockID + ".csv", stockID});
                        executor.excute("StockInfo.py", new Object[]{paramaters.getPath() + stockID + "qfq" + ".csv", stockID, "qfq"});
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (InterruptedException e2) {
                        e2.printStackTrace();
                    }
                }
            }
        });
        thread.start();
        for (int m = start; m < stocks.size(); m++) {
//            System.out.print(threadCount + " " + threads.size() + " ");
//            System.out.println(i);

            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                List<StockPO> stockPOS = new ArrayList<>();
                String stockID = stocks.get(m).split(";")[0];
                System.out.println(stockID);
                BufferedReader reader = paramaters.getBufferedReader(stockID + ".csv");
                BufferedReader reader1 = paramaters.getBufferedReader(stockID + "qfq" + ".csv");
                if (reader != null && reader1 != null) {
                    String tem = reader.readLine();
                    String tem1 = reader1.readLine();
                    while ((tem = reader.readLine()) != null && (tem1 = reader1.readLine()) != null) {
                        String[] args = tem.split(",");
                        String[] args1 = tem1.split(",");
                        StockPO stockPO = new StockPO();
                        stockPO.setStockId(stocks.get(m).split(";")[0]);
                        java.sql.Date date = new java.sql.Date(simpleDateFormat.parse(args[0]).getTime());
                        stockPO.setDateValue(date);
                        stockPO.setOpenPrice(Double.parseDouble(args[1]));
                        stockPO.setHighPrice(Double.parseDouble(args[2]));
                        stockPO.setLowPrice(Double.parseDouble(args[4]));
                        stockPO.setClosePrice(Double.parseDouble(args[3]));
                        stockPO.setVolume(Double.parseDouble(args[5]));
                        stockPO.setAmount(Double.parseDouble(args[6]));
                        stockPO.setAdjClose(Double.parseDouble(args1[3]));
                        stockPO.setMa5((double) (-1));
                        stockPO.setMa10((double) (-1));
                        stockPO.setMa20((double) (-1));
                        stockPO.setMa30((double) (-1));
                        stockPO.setMa60((double) (-1));
                        stockPO.setMa120((double) (-1));
                        stockPO.setMa240((double) (-1));
                        stockPO.setChg((double) (-1));
                        stockPO.setReturnRate((double) (-1));
                        stockPO.setRsv((double) (-1));
                        stockPO.setMb((double)(-1));
                        stockPO.setUp((double)(-1));
                        stockPO.setDn((double)(-1));
                        stockPO.setkValue((double) (50));
                        stockPO.setdValue((double) (50));
                        stockPO.setjValue((double) (50));
                        stockPO.setEma12((double) (0));
                        stockPO.setEma26((double) (0));
                        stockPO.setDea((double) (0));
                        stockPOS.add(stockPO);
                    }
                    StockPO stockPO;
                    int count;
                    for (int i = stockPOS.size() - 1; i >= 0; i--) {
                        count = stockPOS.size() - 1 - i;
                        stockPO = stockPOS.get(i);
                        if (count >= 4) {
                            stockPO.setMa5(maValue(i, 5, stockPOS));
                        }
                        if (count >= 9) {
                            stockPO.setMa10(maValue(i, 10, stockPOS));
                        }

                        if (count >= 19) {
                            double avg = maValue(i, 20, stockPOS);
                            stockPO.setMa20(avg);
                            double sum = 0;
                            double md = 0;
                            for (int k = i; k < i + 20; k++) {
                                sum += Math.pow(stockPOS.get(k).getClosePrice() - avg, 2);
                            }
                            md = Math.sqrt(sum / 20);
                            double mb = maValue(i, 19, stockPOS);
                            stockPO.setMb(mb);
                            double up = mb + 2 * md;
                            stockPO.setUp(up);
                            double dn = mb - 2 * md;
                            stockPO.setDn(dn);
                        }
                        if (count >= 29) {
                            stockPO.setMa30(maValue(i, 30, stockPOS));
                        }
                        if (count >= 59) {
                            stockPO.setMa60(maValue(i, 60, stockPOS));
                        }
                        if (count >= 119) {
                            stockPO.setMa120(maValue(i, 120, stockPOS));
                        }
                        if (count >= 239) {
                            stockPO.setMa240(maValue(i, 240, stockPOS));
                        }

                        if (stockPO.getHighPrice().doubleValue() != stockPO.getLowPrice().doubleValue())
                            stockPO.setRsv(((stockPO.getClosePrice() - stockPO.getLowPrice()) / (stockPO.getHighPrice() - stockPO.getLowPrice())) * 100);
                        else
                            stockPO.setRsv((double) (0));

                        if (i < stockPOS.size() - 1) {
                            stockPO.setReturnRate(Math.log(stockPOS.get(i).getAdjClose() / stockPOS.get(i + 1).getAdjClose()));
                            stockPO.setChg((stockPOS.get(i).getAdjClose() - stockPOS.get(i + 1).getAdjClose()) / stockPOS.get(i + 1).getAdjClose());

                            //计算kdj
                            stockPO.setkValue((2 * stockPOS.get(i + 1).getkValue() + stockPO.getRsv()) / 3);
                            stockPO.setdValue((2 * stockPOS.get(i + 1).getdValue() + stockPO.getkValue()) / 3);
                            stockPO.setjValue(3 * stockPO.getkValue() - 2 * stockPO.getdValue());

                            //计算macd
                            stockPO.setEma12((11 * stockPOS.get(i + 1).getEma12() + 2 * stockPO.getClosePrice()) / 13);
                            stockPO.setEma26((25 * stockPOS.get(i + 1).getEma26() + 2 * stockPO.getClosePrice()) / 27);
                            stockPO.setDif(stockPO.getEma12() - stockPO.getEma26());
                            stockPO.setDea((8 * stockPOS.get(i + 1).getDea() + 2 * stockPO.getDif()) / 10);

                        }
                        stockPO.setDif(stockPO.getEma12() - stockPO.getEma26());

                        //计算rsi
                        double up = 0;
                        double down = 0;
                        double temp = 0;
                        if (i < stockPOS.size() - 13) {
                            for (int j = i; j < i + 13; j++) {
                                temp = stockPOS.get(j).getClosePrice() - stockPOS.get(j + 1).getClosePrice();
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
                        } else {
                            stockPO.setRsi((double) (0));
                        }

                        //计算dmi
                        if (i < stockPOS.size() - 1) {
                            StockPO lastDay = stockPOS.get(i + 1);
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

                            if (i < stockPOS.size() - 13) {
                                double dmp12 = 0;
                                double dmm12 = 0;
                                double tr12 = 0;

                                for (int j = i; j < i + 12; j++) {
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
                                double dx=0;
                                if(diSum != 0)
                                    dx = (diDif / diSum) * 100;

                                stockPO.setDx(dx);
                                double adx = 0;
                                for (int j = i; j < i + 12; j++) {
                                    adx += stockPOS.get(j).getDx();
                                }
                                stockPO.setAdx(adx / 12);
                                double adxr = 0;
                                adxr = stockPO.getAdx() + stockPOS.get(i + 12).getAdx();
                                stockPO.setAdxr(adxr / 2);
                            } else {
                                int len = stockPOS.size() - i;
                                double dmp12 = 0;
                                double dmm12 = 0;
                                double tr12 = 0;

                                for (int j = i; j < i + len; j++) {
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
                                if(diSum != 0)
                                    dx = (diDif / diSum) * 100;
                                stockPO.setDx(dx);
                                double adx = 0;
                                for (int j = i; j < i + len; j++) {
                                    adx += stockPOS.get(j).getDx();
                                }
                                stockPO.setAdx(adx / len);
                                double adxr = stockPO.getAdx() + stockPOS.get(stockPOS.size() - 2).getAdx();
                                stockPO.setAdxr(adxr / 2);
                            }

                        } else {
                            stockPO.setDmPlus((double) (0));
                            stockPO.setDmMinus((double) (0));
                            stockPO.setTr((double) (0));
                            stockPO.setDiMinus((double) (0));
                            stockPO.setDiPlus((double) (0));
                            stockPO.setDx((double) (0));
                            stockPO.setAdx((double) (0));
                            stockPO.setAdxr((double) (0));
                        }
/*
                        System.out.print(stockPO.getStockId() + " ");
                        System.out.print(stockPO.getMb() + " ");
                        System.out.print(stockPO.getUp() + " ");
                        System.out.print(stockPO.getDn() + " ");
                        System.out.print(stockPO.getAdjClose() + " ");
                        System.out.print(stockPO.getHighPrice() + " ");
                        System.out.print(stockPO.getLowPrice() + " ");
                        System.out.print(stockPO.getOpenPrice() + " ");
                        System.out.print(stockPO.getClosePrice() + " ");
                        System.out.print(stockPO.getChg() + " ");
                        System.out.print(stockPO.getReturnRate() + " ");
                        System.out.print(stockPO.getAmount() + " ");
                        System.out.print(stockPO.getRsv() + " ");
                        System.out.print(stockPO.getkValue() + " ");
                        System.out.print(stockPO.getdValue() + " ");
                        System.out.print(stockPO.getjValue() + " ");
                        System.out.print(stockPO.getEma12() + " ");
                        System.out.print(stockPO.getEma26() + " ");
                        System.out.print(stockPO.getDif() + " ");
                        System.out.print(stockPO.getDea() + " ");
                        System.out.print(stockPO.getRsi() + " ");
                        System.out.print(stockPO.getDmMinus() + " ");
                        System.out.print(stockPO.getDmPlus() + " ");
                        System.out.print(stockPO.getTr() + " ");
                        System.out.print(stockPO.getDiPlus() + " ");
                        System.out.print(stockPO.getDiMinus() + " ");
                        System.out.print(stockPO.getDx() + " ");
                        System.out.print(stockPO.getAdx() + " ");
                        System.out.print(stockPO.getAdxr() + " ");
*/
//                        System.out.println();
//                        System.out.print(stockPO.getStockId() + " ");
//                        System.out.print(stockPO.getStockId() + " ");

                        stockDao.add(stockPO);
                        System.out.println(stockPO.getStockId() + " " + stockPO.getDateValue());
                    }
                }else{
                    wait.add(m);
                }
            } catch (IOException e1) {
                e1.printStackTrace();
//            } catch (InterruptedException e2) {
//                e2.printStackTrace();
            } catch (ParseException e3) {
                e3.printStackTrace();
            }

        }

        for(int i = 0; i < wait.size(); i++){
            System.out.print(wait.get(i) + ";");
        }

        //System.out.print(stocks.get(i));
    }



    private double maValue(int position, int matype, List<StockPO> stockPOS){
        double sum = 0;
        for(int i = position; i < position + matype; i++){
//            System.out.println(i);
            sum += stockPOS.get(i).getClosePrice();
        }
        return sum / matype;
    }

}
