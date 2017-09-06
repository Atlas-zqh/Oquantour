package vo;

import javafx.beans.property.SimpleStringProperty;
import po.Stock;

import java.util.Date;

/**
 * Created by island on 2017/4/18.
 */
public class StockBackTestWinnerVO {
    //日期
    public String date;
    //股票名字
    public String stockName;
    //股票ID
    public String stockID;
    //开盘价
    public String open;
    //收盘价
    public String close;
    //复权收盘价
    public String stdclose;
    //最高价
    public String high;
    //最低价
    public String low;


    public SimpleStringProperty specDate;
    public SimpleStringProperty specCode;
    public SimpleStringProperty specName;
    public SimpleStringProperty specOpen;
    public SimpleStringProperty specClose;
    public SimpleStringProperty specStdClose;
    public SimpleStringProperty specHigh;
    public SimpleStringProperty specLow;

    /**
     * 显示市场温度计榜单时使用的构造方法
     * @param specCode
     * @param specName
     * @param specOpen
     * @param specClose
     * @param specHigh
     * @param specLow
     */
    public StockBackTestWinnerVO(String specDate, String specCode, String specName, String specOpen,
                   String specClose, String specStdClose, String specHigh, String specLow) {
        this.specDate = new SimpleStringProperty(specDate);
        this.specCode = new SimpleStringProperty(specCode);
        this.specName = new SimpleStringProperty(specName);
        this.specOpen = new SimpleStringProperty(specOpen);
        this.specClose = new SimpleStringProperty(specClose);
        this.specStdClose = new SimpleStringProperty(specStdClose);
        this.specHigh = new SimpleStringProperty(specHigh);
        this.specLow = new SimpleStringProperty(specLow);
    }

    public String getSpecCode() {
        return specCode.get();
    }

    public String getSpecName() {
        return specName.get();
    }

    public String getSpecDate() {
        return specDate.get();
    }

    public String getSpecOpen() {
        return specOpen.get();
    }

    public String getSpecClose() {
        return specClose.get();
    }

    public String getSpecStdClose() {
        return specStdClose.get();
    }

    public String getSpecHigh() {
        return specHigh.get();
    }

    public String getSpecLow() {
        return specLow.get();
    }
}
