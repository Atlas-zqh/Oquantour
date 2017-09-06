package data.dataImpl;

import data.dataHelper.StockInfoReader;
import data.dataService.StockPlateDataService;
import po.PlatePO;
import po.SearchPO;
import po.Stock;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Pxr on 2017/3/23.
 */
public class StockPlateDataServiceImpl implements StockPlateDataService {

    private StockDataServiceImpl stockDataService;

    public StockPlateDataServiceImpl() {
        stockDataService = new StockDataServiceImpl();
    }

    @Override
    public List<List<Stock>> getStockInStockPlate(SearchPO searchPO) {
        List<String> stockCodeList = getAllStockInfoInPlate(searchPO.getPlateName());
        List<List<Stock>> allStock = new ArrayList<>();
        for (String stockInfo : stockCodeList) {
            String stockCode = stockInfo.split(";")[0];
            List<Stock> stockList = stockDataService.searchStock(new SearchPO(stockCode, searchPO.getStartDate(), searchPO.getEndDate()));
            if (stockList.size() > 0) {
                allStock.add(stockList);
            }
        }
        return allStock;
    }

    /**
     * 按照股票的信息（名称或股票号）获得它所在的板块
     * 如果该股票不属于任何板块，则返回""
     *
     * @param stockInfo 股票名或号码
     * @return 板块名
     */
    public String getStockPlate(String stockInfo) {
        String plateFilePath = System.getProperty("user.dir") + "/datasource/plate";//板块路径
        List<File> plateFileList = getFile(plateFilePath);//获得板块路径下所有板块文件
        BufferedReader bufferedReader = null;
        try {
            for (File plateFile : plateFileList) {//循环所有文件
                bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(plateFile)));
                String str = null;
                while ((str = bufferedReader.readLine()) != null) {
                    String stockCode = str.split(";")[0];//股票号码
                    String stockName = str.split(";")[1];//股票名称
                    if (stockInfo.equals(stockCode) && stockInfo.equals(stockName)) {//只要股票信息等于其中任何一项，则返回该文件名，去掉txt后缀
                        return plateFile.getName().split(".")[0];
                    }
                }
                bufferedReader.close();//关闭读取流
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";//若板块不属于任何股票，返回""
    }

    /**
     * 读取一个文件夹下的所有txt结尾的文件
     *
     * @param dicName 文件夹的路径
     * @return 所有txt结尾的文件
     */
    private List<File> getFile(String dicName) {
        File dir = new File(dicName);
        File[] files = dir.listFiles(); // 该文件目录下文件全部放入数组
        List<File> fileList = new ArrayList<>();
        for (File file : files) {
            if (file.getName().endsWith("txt"))
                fileList.add(file);
        }
        return fileList;
    }

    @Override
    public List<String> getAllStockInfoInPlate(String plateName) {
        File f = new File(System.getProperty("user.dir") + "/datasource/plate/" + plateName + ".txt");
        List<String> stockInfoList = new ArrayList<>();
        if (!f.exists())
            return new ArrayList<>();
        else {
            BufferedReader bufferedReader = null;
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
                String str = null;
                while ((str = bufferedReader.readLine()) != null) {
                    stockInfoList.add(str.split(";")[0] + ";" + str.split(";")[1]);
                }
                return stockInfoList;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return stockInfoList;
    }

    @Override
    public List<PlatePO> getPlateReturnRate(SearchPO searchPO) {
        BufferedReader bufferedReader = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yy");
        List<PlatePO> platePOS = new ArrayList<>();
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(System.getProperty("user.dir") + "/datasource/plate/" + searchPO.getPlateName() + "指.txt"))));
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                Date date = simpleDateFormat.parse(str.split(";")[0]);
                double rate = Double.parseDouble(str.split(";")[1]);
                if ((date.equals(searchPO.getStartDate()) || date.after(searchPO.getStartDate())) && (date.equals(searchPO.getEndDate()) || date.before(searchPO.getEndDate()))) {
                    platePOS.add(new PlatePO(date, rate));
                }
            }
        } catch (ParseException e2) {
            e2.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return platePOS;
    }
}
