package data.dataHelper;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import po.Stock;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by keenan on 06/03/2017.
 */
public class StockInfoReader {

    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yy");

    /**
     * 获取股票信息
     *
     * @param flag 若flag为0，则代表是按股票号查找
     *             若flag为1，则代表是按年份查找
     * @param mark 不同的查找关键词，可能是股票代码，也可能是日期（格式为MM/dd/yy）
     * @return 搜索结果：对应股票号的所有股票信息，或该年份的所有股票信息
     * 若按股票号搜索，若股票号对应的股票不存在，则返回的队列中没有元素
     * 若按年份搜索，若该年份没有股票信息，则队列中也没有元素
     */
    public List<Stock> readStockInfo(int flag, String mark) {

        if (flag != 1 && flag != 0) {
            return new ArrayList<>();
        }

        List<Stock> stocksFound = new ArrayList();
        String path = "";

        // 如果是按股票号码查找，则flag为0
        if (flag == 0) {
            path = System.getProperty("user.dir") + "/datasource/stocks/" + mark.substring(0, 1) + "/" + mark + ".txt";
        } else
        // 如果是按日期查找，则flag为1
        {
            path = System.getProperty("user.dir") + "/datasource/yearInfo/" + mark.substring(mark.length() - 2) + ".txt";
        }

        try {
            stocksFound = txtReader(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stocksFound;

    }

    /**
     * 根据年份获得股票信息
     *
     * @param startYear 开始年份
     * @param endYear   结束年份
     * @return 那些年份的股票信息
     */
    public List<String> getStockByYear(int startYear, int endYear) {
        List<String> strings = new ArrayList<>();
        try {
            for (int i = startYear; i <= endYear; i++) {
                if (i - 100 < 10) {
                    strings.addAll(FileUtils.readLines(new File(System.getProperty("user.dir") + "/datasource/yearInfo/0" + (i - 100) + ".txt")));
                } else {
                    strings.addAll(FileUtils.readLines(new File(System.getProperty("user.dir") + "/datasource/yearInfo/" + (i - 100) + ".txt")));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strings;
    }

//    public Map<String, List<Stock>> getMeanValue(List<String> stockList, Date startDate, Date endDate, int meanValueDays) {
//        try {
//            Date date = null;
//            for (String s : stockList) {
//                List<String> strings = FileUtils.readLines(new File("datasource/stocks/" + s.substring(0, 1) + "/" + s + ".txt"));
//                for (int i = 0, size = strings.size(); i < size; i++) {
//                    date = str2Date(strings.get(i).split(";")[1]);
//
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }

    /**
     * 读取大量股票时的方法
     *
     * @param stockList 股票号码列表
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return 股票名对应其数据的map
     */
    public Map<String, List<Stock>> readManyStocks(List<String> stockList, Date startDate, Date endDate) {
        Map<String, List<Stock>> map = new HashMap<>();//一个股票名对应一个list
        List<String> strings = getStockByYear(startDate.getYear(), endDate.getYear());
        for (String s : stockList) {
            map.put(s, new ArrayList<>());
        }
        int i = 0, size = strings.size();
        Stock stock = null;
        for (; i < size; i++) {
            String s[] = StringUtils.split(strings.get(i), ";");
            Date date = str2Date(s[1]);
            if (null != map.get(s[8])) {
                if (!date.before(startDate) && !date.after(endDate)) {
                    stock = new Stock(strings.get(i), date);
                    map.get(s[8]).add(stock);
                }
            }
        }
        return map;
    }

    /**
     * 根据给定的地址，从txt文件中获取股票信息
     *
     * @param path 文件地址
     * @return 该文件中的所有股票
     */
    private List<Stock> txtReader(String path) throws IOException {

        File file = new File(path);

        if (!file.exists()) {
            return new ArrayList<>();
        }

        BufferedReader bufferedReader = null;
        List<Stock> stocks = new ArrayList<>();

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String str = null;
            while ((str = bufferedReader.readLine()) != null)
                stocks.add(encapsulateStock(str));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stocks;

    }

    /**
     * 将txt中读到的每一行的股票信息封装成Stock对象
     * 属性的顺序为：
     * Serial:0
     * Date:1
     * Open:2
     * High:3
     * Low:4
     * Close:5
     * Volume:6
     * AdjClose:7
     * code:8
     * name:9
     * market:10
     *
     * @param stockInfo
     * @return 封装完成的股票信息，可能为null
     */
    private Stock encapsulateStock(String stockInfo) {
        String[] attributes = stockInfo.split(";");

        if (attributes.length != 11) {
            return null;
        }

        try {
            int serial = Integer.parseInt(attributes[0]);

//            System.out.println(attributes[1]);

            Date date = str2Date(attributes[1]);

            double open = Double.parseDouble(attributes[2]);

            double high = Double.parseDouble(attributes[3]);

            double low = Double.parseDouble(attributes[4]);

            double close = Double.parseDouble(attributes[5]);

            int volume = Integer.parseInt(attributes[6]);

            double adjClose = Double.parseDouble(attributes[7]);

            String code = attributes[8];

            String name = attributes[9];

            String market = attributes[10];

            return new Stock(serial, date, open, high, low, close, volume, adjClose, code, name, market);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 将日期格式字符串转为Date对象
     *
     * @param str 日期格式的字符串
     * @return 对应的Date对象
     */
    private Date str2Date(String str) {
        Date date;
//        System.out.println(str+" pxr");
        try {
            synchronized (simpleDateFormat){
            date = simpleDateFormat.parse(str);}
            return date;
        } catch (ParseException e) {
            return null;

        }

    }

    /**
     * 读取所有交易日
     *
     * @return 所有交易日组成的list
     */
    public List<Date> readTransactionDays() {
        File f = new File(System.getProperty("user.dir") + "/datasource/transactionDays.txt");
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
            String[] transactionStringList = bufferedReader.readLine().split(";");
            List<Date> transactionDateList = new ArrayList<>();
            for (String s : transactionStringList) {
                transactionDateList.add(str2Date(s));
            }
            return transactionDateList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

}
