package oquantour.po.util;

import oquantour.po.StockPO;

import java.sql.Date;
import java.util.Map;

/**
 * Created by keenan on 11/05/2017.
 */
public class BackTestWinner {
    private Date date;

    // 股票和买入的股数
    private Map<StockPO, Integer> shares;

    private Double dailyRemainings;

    public BackTestWinner(Date date, Map<StockPO, Integer> shares, Double dailyRemainings) {
        this.date = date;
        this.shares = shares;
        this.dailyRemainings = dailyRemainings;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Map<StockPO, Integer> getShares() {
        return shares;
    }

    public void setShares(Map<StockPO, Integer> shares) {
        this.shares = shares;
    }

    public Double getDailyRemainings() {
        return dailyRemainings;
    }

    public void setDailyRemainings(Double dailyRemainings) {
        this.dailyRemainings = dailyRemainings;
    }
}
