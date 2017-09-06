package oquantour.po;

import javax.persistence.*;
import java.sql.Date;

/**
 * Created by island on 2017/6/9.
 */
@Entity
@IdClass(IndustryPOPK.class)
public class IndustryPO {
    private String industry;
    private Date dateValue;
    private Double returnRate;

    @Id
    @Column(name = "Industry")
    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    @Id
    @Column(name = "DateValue")
    public Date getDateValue() {
        return dateValue;
    }

    public void setDateValue(Date dateValue) {
        this.dateValue = dateValue;
    }

    @Basic
    @Column(name = "ReturnRate")
    public Double getReturnRate() {
        return returnRate;
    }

    public void setReturnRate(Double returnRate) {
        this.returnRate = returnRate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IndustryPO that = (IndustryPO) o;

        if (industry != null ? !industry.equals(that.industry) : that.industry != null) return false;
        if (dateValue != null ? !dateValue.equals(that.dateValue) : that.dateValue != null) return false;
        if (returnRate != null ? !returnRate.equals(that.returnRate) : that.returnRate != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = industry != null ? industry.hashCode() : 0;
        result = 31 * result + (dateValue != null ? dateValue.hashCode() : 0);
        result = 31 * result + (returnRate != null ? returnRate.hashCode() : 0);
        return result;
    }
}
