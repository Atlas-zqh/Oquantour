package oquantour.po;

import javax.persistence.*;
import java.sql.Date;

/**
 * Created by island on 5/7/17.
 */
@Entity
@IdClass(PlateinfoPOPK.class)
public class PlateinfoPO {
    private String plateName;
    private Date dateValue;
    private Double returnRate;
    private Double closePrice;
    private Double highPrice;
    private Double lowPrice;
    private Double openPrice;
    private Double fluctuation;
    private Double chg;
    private Double volume;
    private Double amount;
    private Double adjClosePrice;

    @Id
    @Column(name = "PlateName")
    public String getPlateName() {
        return plateName;
    }

    public void setPlateName(String plateName) {
        this.plateName = plateName;
    }

    @Id
    @Column(name = "DateValue")
    public Date getDateValue() {
        return dateValue;
    }

    public void setDateValue(Date dateValue) {
        this.dateValue = dateValue;
    }

    @Basic
    @Column(name = "ReturnRate")
    public Double getReturnRate() {
        return returnRate;
    }

    public void setReturnRate(Double returnRate) {
        this.returnRate = returnRate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlateinfoPO that = (PlateinfoPO) o;

        if (plateName != null ? !plateName.equals(that.plateName) : that.plateName != null) return false;
        if (dateValue != null ? !dateValue.equals(that.dateValue) : that.dateValue != null) return false;
        if (returnRate != null ? !returnRate.equals(that.returnRate) : that.returnRate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = plateName != null ? plateName.hashCode() : 0;
        result = 31 * result + (dateValue != null ? dateValue.hashCode() : 0);
        result = 31 * result + (returnRate != null ? returnRate.hashCode() : 0);
        return result;
    }

    @Basic
    @Column(name = "ClosePrice")
    public Double getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(Double closePrice) {
        this.closePrice = closePrice;
    }

    @Basic
    @Column(name = "HighPrice")
    public Double getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(Double highPrice) {
        this.highPrice = highPrice;
    }

    @Basic
    @Column(name = "LowPrice")
    public Double getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(Double lowPrice) {
        this.lowPrice = lowPrice;
    }

    @Basic
    @Column(name = "OpenPrice")
    public Double getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(Double openPrice) {
        this.openPrice = openPrice;
    }

    @Basic
    @Column(name = "Fluctuation")
    public Double getFluctuation() {
        return fluctuation;
    }

    public void setFluctuation(Double fluctuation) {
        this.fluctuation = fluctuation;
    }

    @Basic
    @Column(name = "Chg")
    public Double getChg() {
        return chg;
    }

    public void setChg(Double chg) {
        this.chg = chg;
    }

    @Basic
    @Column(name = "Volume")
    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    @Basic
    @Column(name = "Amount")
    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    @Basic
    @Column(name = "AdjClosePrice")
    public Double getAdjClosePrice() {
        return adjClosePrice;
    }

    public void setAdjClosePrice(Double adjClosePrice) {
        this.adjClosePrice = adjClosePrice;
    }
}
