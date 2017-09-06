package po;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * PO of Stock, used for storing information of daily stock.
 * <p>
 * Created by keenan on 03/03/2017.
 */
public class Stock {
    // 序号（没有用的）
    private int serial;
    // 日期
    private Date date;
    // 开盘价
    private double open;
    // 当日最高价
    private double high;
    // 当日最低价
    private double low;
    // 当日收盘价
    private double close;
    // 成交量
    private int volume;
    // 复权收盘价
    private double adjClose;
    // 股票代码
    private String code;
    // 股票名
    private String name;
    // 市场
    private String market;

    public Stock(int serial, Date date, double open, double high, double low, double close, int volume, double adjClose, String code, String name, String market) {
        this.serial = serial;
        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
        this.adjClose = adjClose;
        this.code = code;
        this.name = name;
        this.market = market;
    }

    public Stock(String line, Date date) {

        String[] attributes = line.split(";");

        this.serial = Integer.parseInt(attributes[0]);

        this.date = date;


        this.open = Double.parseDouble(attributes[2]);

        this.high = Double.parseDouble(attributes[3]);

        this.low = Double.parseDouble(attributes[4]);

        this.close = Double.parseDouble(attributes[5]);

        this.volume = Integer.parseInt(attributes[6]);

        this.adjClose = Double.parseDouble(attributes[7]);

        this.code = attributes[8];

        this.name = attributes[9];

        this.market = attributes[10];

    }

    public int getSerial() {
        return serial;
    }

    public void setSerial(int serial) {
        this.serial = serial;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public double getAdjClose() {
        return adjClose;
    }

    public void setAdjClose(double adjClose) {
        this.adjClose = adjClose;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else {
            if (this.getClass() == obj.getClass()) {
                Stock stock = (Stock) obj;
                if (this.getCode().equals(stock.getCode())) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }
}
