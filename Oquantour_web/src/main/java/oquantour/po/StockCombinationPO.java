package oquantour.po;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by island on 2017/6/1.
 */
@Entity
@IdClass(StockCombinationPOPK.class)
public class StockCombinationPO {
    private String userName;
    private String position;
    private String combinationName;
    private String stocks;
    private String prices;
    private Timestamp saveTime;

    @Id
    @Column(name = "UserName")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Basic
    @Column(name = "Position")
    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Id
    @Column(name = "CombinationName")
    public String getCombinationName() {
        return combinationName;
    }

    public void setCombinationName(String combinationName) {
        this.combinationName = combinationName;
    }

    @Basic
    @Column(name = "Stocks")
    public String getStocks() {
        return stocks;
    }

    public void setStocks(String stocks) {
        this.stocks = stocks;
    }

    @Basic
    @Column(name = "Prices")
    public String getPrices() {
        return prices;
    }

    public void setPrices(String prices) {
        this.prices = prices;
    }

    @Id
    @Column(name = "SaveTime")
    public Timestamp getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(Timestamp saveTime) {
        this.saveTime = saveTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StockCombinationPO that = (StockCombinationPO) o;

        if (userName != null ? !userName.equals(that.userName) : that.userName != null) return false;
        if (position != null ? !position.equals(that.position) : that.position != null) return false;
        if (combinationName != null ? !combinationName.equals(that.combinationName) : that.combinationName != null)
            return false;
        if (stocks != null ? !stocks.equals(that.stocks) : that.stocks != null) return false;
        if (saveTime != null ? !saveTime.equals(that.saveTime) : that.saveTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userName != null ? userName.hashCode() : 0;
        result = 31 * result + (position != null ? position.hashCode() : 0);
        result = 31 * result + (combinationName != null ? combinationName.hashCode() : 0);
        result = 31 * result + (stocks != null ? stocks.hashCode() : 0);
        result = 31 * result + (saveTime != null ? saveTime.hashCode() : 0);
        return result;
    }
}
