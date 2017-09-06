package oquantour.po;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by island on 2017/6/6.
 */
@Entity
public class PlateRealTimePO {
    //指数代码
    private String plateId;
    //指数名称
    private String plateName;
    //涨跌幅
    private double change;
    //开盘点位
    private double openPrice;
    //昨日收盘点位
    private double preclosePrice;
    //收盘点位
    private double closePrice;
    //最高点位
    private double highPrice;
    //最低点位
    private double lowPrice;
    //成交量手(手)
    private double volume;
    //成交金额（亿元）
    private double amount;

    @Id
    @Column(name = "PlateName")
    public String getPlateName() {
        return plateName;
    }

    public void setPlateName(String plateName) {
        this.plateName = plateName;
    }

    @Basic
    @Column(name = "ChangeRate")
    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }

    @Basic
    @Column(name = "OpenPrice")
    public double getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(double openPrice) {
        this.openPrice = openPrice;
    }

    @Basic
    @Column(name = "PreclosePrice")
    public double getPreclosePrice() {
        return preclosePrice;
    }

    public void setPreclosePrice(double preclosePrice) {
        this.preclosePrice = preclosePrice;
    }

    @Basic
    @Column(name = "ClosePrice")
    public double getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(double closePrice) {
        this.closePrice = closePrice;
    }

    @Basic
    @Column(name = "HighPrice")
    public double getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(double highPrice) {
        this.highPrice = highPrice;
    }

    @Basic
    @Column(name = "LowPrice")
    public double getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(double lowPrice) {
        this.lowPrice = lowPrice;
    }

    @Basic
    @Column(name = "Volume")
    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    @Basic
    @Column(name = "Amount")
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Basic
    @Column(name = "PlateID")
    public String getPlateId() {
        return plateId;
    }

    public void setPlateId(String plateId) {
        this.plateId = plateId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlateRealTimePO that = (PlateRealTimePO) o;

        if (Double.compare(that.openPrice, openPrice) != 0) return false;
        if (Double.compare(that.preclosePrice, preclosePrice) != 0) return false;
        if (Double.compare(that.closePrice, closePrice) != 0) return false;
        if (Double.compare(that.highPrice, highPrice) != 0) return false;
        if (Double.compare(that.lowPrice, lowPrice) != 0) return false;
        if (Double.compare(that.volume, volume) != 0) return false;
        if (Double.compare(that.amount, amount) != 0) return false;
        if (plateName != null ? !plateName.equals(that.plateName) : that.plateName != null) return false;
        if (plateId != null ? !plateId.equals(that.plateId) : that.plateId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = plateName != null ? plateName.hashCode() : 0;
        result = 31 * result + (plateId != null ? plateId.hashCode() : 0);
        temp = Double.doubleToLongBits(openPrice);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(preclosePrice);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(closePrice);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(highPrice);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(lowPrice);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(volume);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(amount);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
