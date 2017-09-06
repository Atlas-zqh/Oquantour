package oquantour.po;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Date;

/**
 * Created by island on 5/7/17.
 */
public class StockPOPK implements Serializable {
    private String stockId;
    private Date dateValue;

    @Column(name = "StockID")
    @Id
    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    @Column(name = "DateValue")
    @Id
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

        StockPOPK stockPOPK = (StockPOPK) o;

        if (stockId != null ? !stockId.equals(stockPOPK.stockId) : stockPOPK.stockId != null) return false;
        if (dateValue != null ? !dateValue.equals(stockPOPK.dateValue) : stockPOPK.dateValue != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = stockId != null ? stockId.hashCode() : 0;
        result = 31 * result + (dateValue != null ? dateValue.hashCode() : 0);
        return result;
    }
}
