package oquantour.po;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Date;

/**
 * Created by island on 2017/6/7.
 */
public class PlateinfoPOPK implements Serializable {
    private String plateName;
    private Date dateValue;

    @Column(name = "PlateName")
    @Id
    public String getPlateName() {
        return plateName;
    }

    public void setPlateName(String plateName) {
        this.plateName = plateName;
    }

    @Column(name = "DateValue")
    @Basic
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

        PlateinfoPOPK that = (PlateinfoPOPK) o;

        if (plateName != null ? !plateName.equals(that.plateName) : that.plateName != null) return false;
        if (dateValue != null ? !dateValue.equals(that.dateValue) : that.dateValue != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = plateName != null ? plateName.hashCode() : 0;
        result = 31 * result + (dateValue != null ? dateValue.hashCode() : 0);
        return result;
    }
}
