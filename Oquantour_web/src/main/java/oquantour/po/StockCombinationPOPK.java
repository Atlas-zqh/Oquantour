package oquantour.po;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by island on 2017/6/1.
 */
public class StockCombinationPOPK implements Serializable {
    private String userName;
    private String stocks;
    private String combinationName;
    private Timestamp saveTime;

    @Column(name = "UserName")
    @Id
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Column(name = "Stocks")
    @Id
    public String getStocks() {
        return stocks;
    }

    public void setStocks(String stocks) {
        this.stocks = stocks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StockCombinationPOPK that = (StockCombinationPOPK) o;

        if (userName != null ? !userName.equals(that.userName) : that.userName != null) return false;
        if (stocks != null ? !stocks.equals(that.stocks) : that.stocks != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userName != null ? userName.hashCode() : 0;
        result = 31 * result + (stocks != null ? stocks.hashCode() : 0);
        return result;
    }

    @Column(name = "CombinationName")
    @Basic
    @Id
    public String getCombinationName() {
        return combinationName;
    }

    public void setCombinationName(String combinationName) {
        this.combinationName = combinationName;
    }

    @Column(name = "SaveTime")
    @Basic
    @Id
    public Timestamp getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(Timestamp saveTime) {
        this.saveTime = saveTime;
    }
}
