package oquantour.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * Created by island on 5/25/17.
 */
public class StockCombination {
    private String userName;
    private List<String> stocks;
    private List<Double> positions;
    private Timestamp time;
    private String combinationName;
    private List<Double> prices;

    public StockCombination(List<String> stocks, List<Double> positions, Timestamp time) {
        this.stocks = stocks;
        this.positions = positions;
        this.time = time;
    }

    public StockCombination() {
    }

    public String getUserName() {
        return userName;
    }

    public List<String> getStocks() {
        return stocks;
    }

    public List<Double> getPositions() {
        return positions;
    }

    public Timestamp getTime() {
        return time;
    }

    public String getCombinationName() {
        return combinationName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setStocks(List<String> stocks) {
        this.stocks = stocks;
    }

    public void setPositions(List<Double> positions) {
        this.positions = positions;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public void setCombinationName(String combinationName) {
        this.combinationName = combinationName;
    }

    public List<Double> getPrices() {
        return prices;
    }

    public void setPrices(List<Double> prices) {
        this.prices = prices;
    }
}
