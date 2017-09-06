package oquantour.po;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Date;

/**
 * Created by island on 2017/6/12.
 */
public class TopListPOPK implements Serializable {
    private String stockId;
    private Date dateValue;
    private String reason;

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

        TopListPOPK that = (TopListPOPK) o;

        if (stockId != null ? !stockId.equals(that.stockId) : that.stockId != null) return false;
        if (dateValue != null ? !dateValue.equals(that.dateValue) : that.dateValue != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = stockId != null ? stockId.hashCode() : 0;
        result = 31 * result + (dateValue != null ? dateValue.hashCode() : 0);
        return result;
    }

    @Column(name = "Reason")
    @Basic
    @Id
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
