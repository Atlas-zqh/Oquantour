package oquantour.po;

import javax.persistence.*;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by island on 5/7/17.
 */
@Entity
@IdClass(StockPOPK.class)
public class StockPO implements Comparable{
    private String stockId;
    private Date dateValue;
    private Double adjClose;
    private Double closePrice;
    private Double highPrice;
    private Double lowPrice;
    private Double openPrice;
    private Double volume;
    private Double ma5;
    private Double ma10;
    private Double ma20;
    private Double ma30;
    private Double ma60;
    private Double ma120;
    private Double ma240;
    // 涨跌幅
    private Double chg;
    private Double returnRate;
    private Double amount;
    private Double rsv;
    private Double kValue;
    private Double dValue;
    private Double jValue;
    private Double ema12;
    private Double ema26;
    private Double dif;
    private Double dea;
    private Double rsi;
    private Double dmPlus;
    private Double dmMinus;
    private Double tr;
    private Double diPlus;
    private Double diMinus;
    private Double adx;
    private Double adxr;
    private Double dx;
    private Double mb;
    private Double up;
    private Double dn;

    public StockPO(String stockId, java.util.Date dateValue, Double adjClose, Double closePrice, Double highPrice, Double lowPrice, Double openPrice, Double volume, Double ma5, Double ma10, Double ma20, Double ma30, Double ma60, Double ma120, Double ma240, Double chg, Double returnRate, Double amount, Double rsv, Double kValue, Double dValue, Double jValue, Double ema12, Double ema26, Double dif, Double dea, Double rsi, Double dmPlus, Double dmMinus, Double tr, Double diPlus, Double diMinus, Double adx, Double adxr, Double dx, Double mb, Double up, Double dn) {
        this.stockId = stockId;
        this.dateValue = new Date(dateValue.getTime());
        this.adjClose = adjClose;
        this.closePrice = closePrice;
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
        this.openPrice = openPrice;
        this.volume = volume;
        this.ma5 = ma5;
        this.ma10 = ma10;
        this.ma20 = ma20;
        this.ma30 = ma30;
        this.ma60 = ma60;
        this.ma120 = ma120;
        this.ma240 = ma240;
        this.chg = chg;
        this.returnRate = returnRate;
        this.amount = amount;
        this.rsv = rsv;
        this.kValue = kValue;
        this.dValue = dValue;
        this.jValue = jValue;
        this.ema12 = ema12;
        this.ema26 = ema26;
        this.dif = dif;
        this.dea = dea;
        this.rsi = rsi;
        this.dmPlus = dmPlus;
        this.dmMinus = dmMinus;
        this.tr = tr;
        this.diPlus = diPlus;
        this.diMinus = diMinus;
        this.adx = adx;
        this.adxr = adxr;
        this.dx = dx;
        this.mb = mb;
        this.up = up;
        this.dn = dn;
    }

    private Date stringToDate(String date){
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date newDate = new Date(simpleDateFormat.parse(date).getTime());
            return newDate;
        }catch (ParseException e){
            e.printStackTrace();
        }
        return new Date(new java.util.Date().getTime());
    }

    public StockPO(Date date, String stockId, Double adjClose, Double closePrice, Double highPrice, Double lowPrice, Double openPrice, String stockName, Double volume) {
        this.dateValue = date;
        this.stockId = stockId;
        this.adjClose = adjClose;
        this.closePrice = closePrice;
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
        this.openPrice = openPrice;
        this.volume = volume;
    }

    public StockPO(String stockId, Date dateValue, Double closePrice, Double highPrice, Double lowPrice, Double openPrice, Double volume) {
        this.stockId = stockId;
        this.dateValue = dateValue;
        this.closePrice = closePrice;
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
        this.openPrice = openPrice;
        this.volume = volume;
    }

    /**
     * 画周k、月k使用
     */
    public StockPO(String stockId, Date dateValue, Double adjClose, Double closePrice, Double highPrice, Double lowPrice, Double openPrice, Double volume, Double ma5, Double ma10, Double ma20, Double ma30, Double ma60, Double ma120, Double ma240) {
        this.stockId = stockId;
        this.dateValue = dateValue;
        this.adjClose = adjClose;
        this.closePrice = closePrice;
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
        this.openPrice = openPrice;
        this.volume = volume;
        this.ma5 = ma5;
        this.ma10 = ma10;
        this.ma20 = ma20;
        this.ma30 = ma30;
        this.ma60 = ma60;
        this.ma120 = ma120;
        this.ma240 = ma240;
    }

    public StockPO() {
    }

    @Override
    public int compareTo(Object o) {
        StockPO stockPO = (StockPO) o;
        Date date = stockPO.dateValue;

        return this.dateValue.compareTo(date);
    }

    @Basic
    @Column(name = "Chg")
    public Double getChg() {
        return chg;
    }

    public void setChg(Double chg) {
        this.chg = chg;
    }

    @Id
    @Column(name = "StockID")
    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
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
    @Column(name = "AdjClose")
    public Double getAdjClose() {
        return adjClose;
    }

    public void setAdjClose(Double adjClose) {
        this.adjClose = adjClose;
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
    @Column(name = "Volume")
    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StockPO stockPO = (StockPO) o;

        if (stockId != null ? !stockId.equals(stockPO.stockId) : stockPO.stockId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = stockId != null ? stockId.hashCode() : 0;

        return result;
    }

    @Basic
    @Column(name = "MA5")
    public Double getMa5() {
        return ma5;
    }

    public void setMa5(Double ma5) {
        this.ma5 = ma5;
    }

    @Basic
    @Column(name = "MA10")
    public Double getMa10() {
        return ma10;
    }

    public void setMa10(Double ma10) {
        this.ma10 = ma10;
    }

    @Basic
    @Column(name = "MA20")
    public Double getMa20() {
        return ma20;
    }

    public void setMa20(Double ma20) {
        this.ma20 = ma20;
    }

    @Basic
    @Column(name = "MA30")
    public Double getMa30() {
        return ma30;
    }

    public void setMa30(Double ma30) {
        this.ma30 = ma30;
    }

    @Basic
    @Column(name = "MA60")
    public Double getMa60() {
        return ma60;
    }

    public void setMa60(Double ma60) {
        this.ma60 = ma60;
    }

    @Basic
    @Column(name = "MA120")
    public Double getMa120() {
        return ma120;
    }

    public void setMa120(Double ma120) {
        this.ma120 = ma120;
    }

    @Basic
    @Column(name = "MA240")
    public Double getMa240() {
        return ma240;
    }

    public void setMa240(Double ma240) {
        this.ma240 = ma240;
    }

    @Basic
    @Column(name = "ReturnRate")
    public Double getReturnRate() {
        return returnRate;
    }

    public void setReturnRate(Double returnRate) {
        this.returnRate = returnRate;
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
    @Column(name = "RSV")
    public Double getRsv() {
        return rsv;
    }

    public void setRsv(Double rsv) {
        this.rsv = rsv;
    }

    @Basic
    @Column(name = "KValue")
    public Double getkValue() {
        return kValue;
    }

    public void setkValue(Double kValue) {
        this.kValue = kValue;
    }

    @Basic
    @Column(name = "DValue")
    public Double getdValue() {
        return dValue;
    }

    public void setdValue(Double dValue) {
        this.dValue = dValue;
    }

    @Basic
    @Column(name = "JValue")
    public Double getjValue() {
        return jValue;
    }

    public void setjValue(Double jValue) {
        this.jValue = jValue;
    }

    @Basic
    @Column(name = "EMA12")
    public Double getEma12() {
        return ema12;
    }

    public void setEma12(Double ema12) {
        this.ema12 = ema12;
    }

    @Basic
    @Column(name = "EMA26")
    public Double getEma26() {
        return ema26;
    }

    public void setEma26(Double ema26) {
        this.ema26 = ema26;
    }

    @Basic
    @Column(name = "Dif")
    public Double getDif() {
        return dif;
    }

    public void setDif(Double dif) {
        this.dif = dif;
    }

    @Basic
    @Column(name = "Dea")
    public Double getDea() {
        return dea;
    }

    public void setDea(Double dea) {
        this.dea = dea;
    }

    @Basic
    @Column(name = "Rsi")
    public Double getRsi() {
        return rsi;
    }

    public void setRsi(Double rsi) {
        this.rsi = rsi;
    }

    @Basic
    @Column(name = "DmPlus")
    public Double getDmPlus() {
        return dmPlus;
    }

    public void setDmPlus(Double dmPlus) {
        this.dmPlus = dmPlus;
    }

    @Basic
    @Column(name = "DmMinus")
    public Double getDmMinus() {
        return dmMinus;
    }

    public void setDmMinus(Double dmMinus) {
        this.dmMinus = dmMinus;
    }

    @Basic
    @Column(name = "Tr")
    public Double getTr() {
        return tr;
    }

    public void setTr(Double tr) {
        this.tr = tr;
    }

    @Basic
    @Column(name = "DiPlus")
    public Double getDiPlus() {
        return diPlus;
    }

    public void setDiPlus(Double diPlus) {
        this.diPlus = diPlus;
    }

    @Basic
    @Column(name = "DiMinus")
    public Double getDiMinus() {
        return diMinus;
    }

    public void setDiMinus(Double diMinus) {
        this.diMinus = diMinus;
    }

    @Basic
    @Column(name = "Adx")
    public Double getAdx() {
        return adx;
    }

    public void setAdx(Double adx) {
        this.adx = adx;
    }

    @Basic
    @Column(name = "Adxr")
    public Double getAdxr() {
        return adxr;
    }

    public void setAdxr(Double adxr) {
        this.adxr = adxr;
    }

    @Basic
    @Column(name = "Dx")
    public Double getDx() {
        return dx;
    }

    public void setDx(Double dx) {
        this.dx = dx;
    }

    @Basic
    @Column(name = "MB")
    public Double getMb() {
        return mb;
    }

    public void setMb(Double mb) {
        this.mb = mb;
    }

    @Basic
    @Column(name = "UP")
    public Double getUp() {
        return up;
    }

    public void setUp(Double up) {
        this.up = up;
    }

    @Basic
    @Column(name = "DN")
    public Double getDn() {
        return dn;
    }

    public void setDn(Double dn) {
        this.dn = dn;
    }
}
