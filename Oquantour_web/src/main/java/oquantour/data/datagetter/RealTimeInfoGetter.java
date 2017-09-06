package oquantour.data.datagetter;


import oquantour.po.PlateRealTimePO;
import oquantour.po.StockRealTimePO;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by island on 2017/6/6.
 */
public class RealTimeInfoGetter {

    PythonExecutor executor;

    Paramaters paramaters;

    public RealTimeInfoGetter() {
        executor = new PythonExecutor();
        paramaters = new Paramaters();
    }

    public Map<String, StockRealTimePO> getRealTimeStock(List<String> stockIds){

        Map<String, StockRealTimePO> stocks = new HashMap<>();
        try {
            executor.excute("RealTimeStock.py", new Object[]{paramaters.getPath() + "realTimeStock.csv"});
            BufferedReader reader = paramaters.getBufferedReader("realTimeStock.csv");
            if(reader != null){
                String tem = reader.readLine();
                while ((tem = reader.readLine()) != null ) {
                    String[] args = tem.split(",");
                    if(stockIds.contains(args[1]) || stockIds.size() == 0){
                        StockRealTimePO stockRealTimePO = new StockRealTimePO();
                        stockRealTimePO.setStockId(args[1]);
                        stockRealTimePO.setStockName(args[2].trim());
                        stockRealTimePO.setChangepercent(Double.parseDouble(args[3]) / 100);
                        stockRealTimePO.setTrade(Double.parseDouble(args[4]));
                        stockRealTimePO.setOpen(Double.parseDouble(args[5]));
                        stockRealTimePO.setHigh(Double.parseDouble(args[6]));
                        stockRealTimePO.setLow(Double.parseDouble(args[7]));
                        stockRealTimePO.setSettlement(Double.parseDouble(args[8]));
                        stockRealTimePO.setVolume(Long.parseLong(args[9]));
                        stockRealTimePO.setTurnoverratio(Double.parseDouble(args[10]));
                        stockRealTimePO.setAmount(Long.parseLong(args[11]));
                        stockRealTimePO.setPer(Double.parseDouble(args[12]));
                        stockRealTimePO.setPb(Double.parseDouble(args[13]));
                        stockRealTimePO.setMktcap(Double.parseDouble(args[14]));
                        stockRealTimePO.setNmc(Double.parseDouble(args[15]));
                        stocks.put(args[1], stockRealTimePO);
                    }
                }
            }

        }catch (IOException e1){
            e1.printStackTrace();
        }catch (InterruptedException e2){
            e2.printStackTrace();
        }
        return stocks;
    }

    public Map<String, PlateRealTimePO> getRealTimePlate(){
        Map<String, PlateRealTimePO> plates = new HashMap<>();
        try{
            executor.excute("PlateInfo.py", new Object[]{paramaters.getPath() + "realTimePlate.csv"});
            BufferedReader reader = paramaters.getBufferedReader("realTimePlate.csv");
            if(reader != null) {
                String tem = reader.readLine();
                while ((tem = reader.readLine()) != null) {
                    String[] args = tem.split(",");
                    PlateRealTimePO plateRealTimePO = new PlateRealTimePO();
                    plateRealTimePO.setPlateId(args[1]);
                    plateRealTimePO.setPlateName(args[2].replace(" ", ""));
                    plateRealTimePO.setChange(Double.parseDouble(args[3]));
                    plateRealTimePO.setOpenPrice(Double.parseDouble(args[4]));
                    plateRealTimePO.setPreclosePrice(Double.parseDouble(args[5]));
                    plateRealTimePO.setClosePrice(Double.parseDouble(args[6]));
                    plateRealTimePO.setHighPrice(Double.parseDouble(args[7]));
                    plateRealTimePO.setLowPrice(Double.parseDouble(args[8]));
                    plateRealTimePO.setVolume(Double.parseDouble(args[9]));
                    plateRealTimePO.setAmount(Double.parseDouble(args[10]));
                    plates.put(args[2], plateRealTimePO);
                    System.out.println(plateRealTimePO.getPlateName());
                }
            }
        }catch (IOException e1){
            e1.printStackTrace();
        }catch (InterruptedException e2){
            e2.printStackTrace();
        }

        return plates;
    }
}
