package oquantour.po;

import javax.persistence.*;

/**
 * Created by island on 2017/6/12.
 */
@Entity
@IdClass(NewsPOPK.class)
public class NewsPO {
    private String title;
    private String dateValue;
    private String url;
    private String content;

    @Id
    @Column(name = "Title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Id
    @Column(name = "DateValue")
    public String getDateValue() {
        return dateValue;
    }

    public void setDateValue(String dateValue) {
        this.dateValue = dateValue;
    }

    @Basic
    @Column(name = "Url")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Basic
    @Column(name = "Content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NewsPO newsPO = (NewsPO) o;

        if (title != null ? !title.equals(newsPO.title) : newsPO.title != null) return false;
        if (dateValue != null ? !dateValue.equals(newsPO.dateValue) : newsPO.dateValue != null) return false;
        if (url != null ? !url.equals(newsPO.url) : newsPO.url != null) return false;
        if (content != null ? !content.equals(newsPO.content) : newsPO.content != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (dateValue != null ? dateValue.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        return result;
    }
}
