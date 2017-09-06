package vo;

import javafx.beans.property.SimpleStringProperty;
import po.Stock;

import java.util.Date;

/**
 * Created by Pxr on 2017/3/9.
 */
public class StockVO {

    public Date date;

    public double open;

    public double high;

    public double low;

    public double close;

    public int volume;

    public double adjClose;

    public String code;

    public String name;

    public String market;

    public double chg;

    public SimpleStringProperty specCode;
    public SimpleStringProperty specName;
    public SimpleStringProperty specMarket;
    public SimpleStringProperty specChg;
    public SimpleStringProperty specOpen;
    public SimpleStringProperty specClose;
    public SimpleStringProperty specHigh;
    public SimpleStringProperty specLow;
    public SimpleStringProperty specVolume;

    public StockVO(Stock stock, double chg) {
        this.date = stock.getDate();
        this.open = stock.getOpen();
        this.high = stock.getHigh();
        this.low = stock.getLow();
        this.close = stock.getClose();
        this.volume = stock.getVolume();
        this.adjClose = stock.getAdjClose();
        this.code = stock.getCode();
        this.name = stock.getName();
        this.market = stock.getMarket();
        this.chg = chg;
    }

    /**
     * 显示市场温度计榜单时使用的构造方法
     * @param specCode
     * @param specName
     * @param specMarket
     * @param specChg
     * @param specOpen
     * @param specClose
     * @param specHigh
     * @param specLow
     * @param specVolume
     */
    public StockVO(String specCode, String specName, String specMarket, String specChg, String specOpen,
                   String specClose, String specHigh, String specLow, String specVolume) {
        this.specCode = new SimpleStringProperty(specCode);
        this.specName = new SimpleStringProperty(specName);
        this.specMarket = new SimpleStringProperty(specMarket);
        this.specChg = new SimpleStringProperty(specChg);
        this.specOpen = new SimpleStringProperty(specOpen);
        this.specClose = new SimpleStringProperty(specClose);
        this.specHigh = new SimpleStringProperty(specHigh);
        this.specLow = new SimpleStringProperty(specLow);
        this.specVolume = new SimpleStringProperty(specVolume);
    }

    public String getSpecCode() {
        return specCode.get();
    }

    public String getSpecName() {
        return specName.get();
    }

    public String getSpecMarket() {
        return specMarket.get();
    }

    public String getSpecChg() {
        return specChg.get();
    }

    public String getSpecOpen() {
        return specOpen.get();
    }

    public String getSpecClose() {
        return specClose.get();
    }

    public String getSpecHigh() {
        return specHigh.get();
    }

    public String getSpecLow() {
        return specLow.get();
    }

    public String getSpecVolume() {
        return specVolume.get();
    }
}
