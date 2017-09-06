package oquantour.po;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by island on 2017/6/12.
 */
public class NewsPOPK implements Serializable {
    private String title;
    private String dateValue;

    @Column(name = "Title")
    @Id
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "DateValue")
    @Id
    public String getDateValue() {
        return dateValue;
    }

    public void setDateValue(String dateValue) {
        this.dateValue = dateValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NewsPOPK newsPOPK = (NewsPOPK) o;

        if (title != null ? !title.equals(newsPOPK.title) : newsPOPK.title != null) return false;
        if (dateValue != null ? !dateValue.equals(newsPOPK.dateValue) : newsPOPK.dateValue != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (dateValue != null ? dateValue.hashCode() : 0);
        return result;
    }
}
