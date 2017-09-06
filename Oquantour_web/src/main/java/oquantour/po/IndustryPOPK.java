package oquantour.po;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.sql.Date;

/**
 * Created by island on 2017/6/9.
 */
public class IndustryPOPK implements Serializable {
    private Date dateValue;
    private String industry;

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

        IndustryPOPK that = (IndustryPOPK) o;

        if (industry != null ? !industry.equals(that.industry) : that.industry != null) return false;
        if (dateValue != null ? !dateValue.equals(that.dateValue) : that.dateValue != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = industry != null ? industry.hashCode() : 0;
        result = 31 * result + (dateValue != null ? dateValue.hashCode() : 0);
        return result;
    }

    @Column(name = "Industry")
    @Id
    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }
}
