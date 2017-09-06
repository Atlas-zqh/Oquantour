package oquantour.data.datagetter;

import oquantour.po.StockPO;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by island on 2017/6/7.
 */
public class DailyInfoGetter {
    PythonExecutor executor;

    Paramaters paramaters;

    public DailyInfoGetter() {
        executor = new PythonExecutor();
        paramaters = new Paramaters();
    }

    public Map<String, StockPO> getDailyInfo(List<String> stockIDs, String date) {
        Map<String, StockPO> stocks = new HashMap<>();
            String stockID = "";
            int finishCount = 0;
            int threadCount = 0;
            int maxThread = 50;
            List<Thread> threads = new ArrayList<>();
            List<Integer> dead = new ArrayList<>();
            for (int i = 0; i < stockIDs.size(); i++) {
                final int count = i;
                if (threadCount < maxThread) {
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                String stockID = stockIDs.get(count).split(";")[0];
                                executor.excute("StockDailyNone.py", new Object[]{paramaters.getPath() + stockID + ".csv", stockID, date});
                                executor.excute("StockDaily.py", new Object[]{paramaters.getPath() + stockID + "qfq" + ".csv", stockID, "qfq", date});

                                BufferedReader reader = paramaters.getBufferedReader(stockID + ".csv");
                                BufferedReader reader1 = paramaters.getBufferedReader(stockID + "qfq" + ".csv");
                                if (reader != null && reader1 != null) {
                                    String tem = reader.readLine();
                                    String tem1 = reader1.readLine();
                                    while ((tem = reader.readLine()) != null && (tem1 = reader1.readLine()) != null) {
                                        String[] args = tem.split(",");
                                        String[] args1 = tem1.split(",");
                                        StockPO stockPO = new StockPO();
                                        stockPO.setStockId(stockID);
                                        try {
                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                            stockPO.setDateValue(new Date(sdf.parse(date).getTime()));
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
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
                                        stockPO.setMb((double) (-1));
                                        stockPO.setUp((double) (-1));
                                        stockPO.setDn((double) (-1));
                                        stockPO.setkValue((double) (50));
                                        stockPO.setdValue((double) (50));
                                        stockPO.setjValue((double) (50));
                                        stockPO.setEma12((double) (0));
                                        stockPO.setEma26((double) (0));
                                        stockPO.setDea((double) (0));
                                        stocks.put(stockID, stockPO);
                                    }
                                }

                            } catch (IOException e1) {
                                e1.printStackTrace();
                            } catch (InterruptedException e2) {
                                e2.printStackTrace();
                            }
                        }
                    });
                    threads.add(thread);
                    threadCount++;
                    thread.start();

                }else{
                    for(int j = 0; j < threads.size(); j++){
                        if(!threads.get(j).isAlive()){
                            dead.add(j);
                            threadCount--;
                            finishCount++;
                        }

                    }
                    for(int j = dead.size() - 1; j >= 0; j--){
                        threads.remove(threads.get(dead.get(j)));
                    }
                    dead.clear();
                    i--;
                }

            }

        System.out.println("start");

        String path = paramaters.getPath();
            while(finishCount < stockIDs.size()){
                for(int j = 0; j < threads.size(); j++){
                    if(!threads.get(j).isAlive()){
                        dead.add(j);
                        threadCount--;
                        finishCount++;
                    }

                }
                for(int j = dead.size() - 1; j >= 0; j--){
                    threads.remove(threads.get(dead.get(j)));
                }
                dead.clear();
                System.out.println(finishCount + " " + stockIDs.size());
            }
            System.out.println("done");

        return stocks;
    }
}
