package oquantour.po;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by island on 5/15/17.
 */
@Entity
public class UserPO {
    private String userName;
    private String phone;
    private String userPassword;
    private String selectedStocks;

    public UserPO(){

    }

    public UserPO(String userName, String phone, String userPassword){
        this.userName = userName;
        this.phone = phone;
        this.userPassword = userPassword;
    }

    @Id
    @Column(name = "UserName")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Basic
    @Column(name = "Phone")
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Basic
    @Column(name = "UserPassword")
    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    @Basic
    @Column(name = "SelectedStocks")
    public String getSelectedStocks() {
        return selectedStocks;
    }

    public void setSelectedStocks(String selectedStocks) {
        this.selectedStocks = selectedStocks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserPO userPO = (UserPO) o;

        if (userName != null ? !userName.equals(userPO.userName) : userPO.userName != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = userName != null ? userName.hashCode() : 0;
        return result;
    }
}
