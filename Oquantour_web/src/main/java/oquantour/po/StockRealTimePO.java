package oquantour.po;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by island on 2017/6/6.
 */
@Entity
public class StockRealTimePO {
    //股票代码
    private String stockId;
    //股票名称
    private String stockName;
    //涨跌幅
    private double changepercent;
    //trade:现价
    private double trade;
    //open:开盘价
    private double open;
    //high:最高价
    private double high;
    //low:最低价
    private double low;
    //settlement:昨日收盘价
    private double settlement;
    //volume:成交量
    private Long volume;
    //turnoverratio:换手率
    private double turnoverratio;
    //amount:成交额
    private Long amount;
    //per:市盈率
    private double per;
    //pb:市净率
    private double pb;
    //mktcap:总市值
    private double mktcap;
    //nmc:流通市值
    private double nmc;

    @Id
    @Column(name = "StockID")
    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    @Basic
    @Column(name = "StockName")
    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    @Basic
    @Column(name = "Changepercent")
    public double getChangepercent() {
        return changepercent;
    }

    public void setChangepercent(double changepercent) {
        this.changepercent = changepercent;
    }

    @Basic
    @Column(name = "Trade")
    public double getTrade() {
        return trade;
    }

    public void setTrade(double trade) {
        this.trade = trade;
    }

    @Basic
    @Column(name = "OpenPrice")
    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    @Basic
    @Column(name = "HighPrice")
    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    @Basic
    @Column(name = "LowPrice")
    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    @Basic
    @Column(name = "Settlement")
    public double getSettlement() {
        return settlement;
    }

    public void setSettlement(double settlement) {
        this.settlement = settlement;
    }

    @Basic
    @Column(name = "Volume")
    public Long getVolume() {
        return volume;
    }

    public void setVolume(Long volume) {
        this.volume = volume;
    }

    @Basic
    @Column(name = "Turnoverratio")
    public double getTurnoverratio() {
        return turnoverratio;
    }

    public void setTurnoverratio(double rurnoverratio) {
        this.turnoverratio = rurnoverratio;
    }

    @Basic
    @Column(name = "Amount")
    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    @Basic
    @Column(name = "Per")
    public double getPer() {
        return per;
    }

    public void setPer(double per) {
        this.per = per;
    }

    @Basic
    @Column(name = "Pb")
    public double getPb() {
        return pb;
    }

    public void setPb(double pb) {
        this.pb = pb;
    }
    @Basic
    @Column(name = "Mktcap")
    public double getMktcap() {
        return mktcap;
    }

    public void setMktcap(double mktcap) {
        this.mktcap = mktcap;
    }

    @Basic
    @Column(name = "Nmc")
    public double getNmc() {
        return nmc;
    }

    public void setNmc(double nmc) {
        this.nmc = nmc;
    }
}
