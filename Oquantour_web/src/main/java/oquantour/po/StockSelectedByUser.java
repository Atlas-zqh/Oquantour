package oquantour.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

/**
 * Created by island on 5/25/17.
 */
@Entity
@IdClass(StockSelectedByUserPK.class)
public class StockSelectedByUser {
    private String userName;
    private String stockId;

    @Id
    @Column(name = "UserName")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Id
    @Column(name = "StockID")
    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StockSelectedByUser that = (StockSelectedByUser) o;

        if (userName != null ? !userName.equals(that.userName) : that.userName != null) return false;
        if (stockId != null ? !stockId.equals(that.stockId) : that.stockId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userName != null ? userName.hashCode() : 0;
        result = 31 * result + (stockId != null ? stockId.hashCode() : 0);
        return result;
    }
}
