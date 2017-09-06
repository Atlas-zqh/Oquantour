package oquantour.po;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by island on 2017/6/4.
 */
public class StockBasicInfoPOPK implements Serializable {
    private String stockId;
    private String quarterOfYear;

    @Column(name = "StockID")
    @Basic
    @Id
    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    @Column(name = "QuarterOfYear")
    @Basic
    @Id
    public String getQuarterOfYear() {
        return quarterOfYear;
    }

    public void setQuarterOfYear(String quarterOfYear) {
        this.quarterOfYear = quarterOfYear;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StockBasicInfoPOPK that = (StockBasicInfoPOPK) o;

        if (stockId != null ? !stockId.equals(that.stockId) : that.stockId != null) return false;
        if (quarterOfYear != null ? !quarterOfYear.equals(that.quarterOfYear) : that.quarterOfYear != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = stockId != null ? stockId.hashCode() : 0;
        result = 31 * result + (quarterOfYear != null ? quarterOfYear.hashCode() : 0);
        return result;
    }
}
