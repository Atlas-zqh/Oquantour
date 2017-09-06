package oquantour.po;

import javax.persistence.*;
import java.sql.Date;

/**
 * Created by island on 2017/6/12.
 */
@Entity
@IdClass(TopListPOPK.class)
public class TopListPO {
    private String stockId;
    private String stockName;
    private Double pchange;
    private Double amount;
    private Double buy;
    private Double bratio;
    private Double sell;
    private Double sratio;
    private String reason;
    private Date dateValue;

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
    @Column(name = "Pchange")
    public Double getPchange() {
        return pchange;
    }

    public void setPchange(Double pchange) {
        this.pchange = pchange;
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
    @Column(name = "Buy")
    public Double getBuy() {
        return buy;
    }

    public void setBuy(Double buy) {
        this.buy = buy;
    }

    @Basic
    @Column(name = "Bratio")
    public Double getBratio() {
        return bratio;
    }

    public void setBratio(Double bratio) {
        this.bratio = bratio;
    }

    @Basic
    @Column(name = "Sell")
    public Double getSell() {
        return sell;
    }

    public void setSell(Double sell) {
        this.sell = sell;
    }

    @Basic
    @Column(name = "Sratio")
    public Double getSratio() {
        return sratio;
    }

    public void setSratio(Double sratio) {
        this.sratio = sratio;
    }

    @Id
    @Column(name = "Reason")
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Id
    @Column(name = "DateValue")
    public Date getDateValue() {
        return dateValue;
    }

    public void setDateValue(Date dateValue) {
        this.dateValue = dateValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TopListPO topListPO = (TopListPO) o;

        if (stockId != null ? !stockId.equals(topListPO.stockId) : topListPO.stockId != null) return false;
        if (stockName != null ? !stockName.equals(topListPO.stockName) : topListPO.stockName != null) return false;
        if (pchange != null ? !pchange.equals(topListPO.pchange) : topListPO.pchange != null) return false;
        if (amount != null ? !amount.equals(topListPO.amount) : topListPO.amount != null) return false;
        if (buy != null ? !buy.equals(topListPO.buy) : topListPO.buy != null) return false;
        if (bratio != null ? !bratio.equals(topListPO.bratio) : topListPO.bratio != null) return false;
        if (sell != null ? !sell.equals(topListPO.sell) : topListPO.sell != null) return false;
        if (sratio != null ? !sratio.equals(topListPO.sratio) : topListPO.sratio != null) return false;
        if (reason != null ? !reason.equals(topListPO.reason) : topListPO.reason != null) return false;
        if (dateValue != null ? !dateValue.equals(topListPO.dateValue) : topListPO.dateValue != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = stockId != null ? stockId.hashCode() : 0;
        result = 31 * result + (stockName != null ? stockName.hashCode() : 0);
        result = 31 * result + (pchange != null ? pchange.hashCode() : 0);
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        result = 31 * result + (buy != null ? buy.hashCode() : 0);
        result = 31 * result + (bratio != null ? bratio.hashCode() : 0);
        result = 31 * result + (sell != null ? sell.hashCode() : 0);
        result = 31 * result + (sratio != null ? sratio.hashCode() : 0);
        result = 31 * result + (reason != null ? reason.hashCode() : 0);
        result = 31 * result + (dateValue != null ? dateValue.hashCode() : 0);
        return result;
    }
}
